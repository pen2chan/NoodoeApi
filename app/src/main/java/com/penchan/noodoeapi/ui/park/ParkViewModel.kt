package com.penchan.noodoeapi.ui.park

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.penchan.noodoeapi.R
import com.penchan.noodoeapi.app.MyApplication
import com.penchan.noodoeapi.base.Event
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.model.entity.*
import com.penchan.noodoeapi.repository.repo.ParkRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class ParkViewModel(
    app: Application,
    private var mRepo: ParkRepo) : AndroidViewModel(app) {

    private val mParkList: MutableLiveData<Event<ResponseResult<List<ParkLotInfo>>>> = MutableLiveData()


    val parkDescList: LiveData<Event<ResponseResult<List<ParkLotInfo>>>>
        get() = mParkList


    fun queryList() = viewModelScope.launch(Dispatchers.IO) {

        mParkList.postValue(Event(ResponseResult.Loading()))

        val descResponse = async { mRepo.getDescList() }.await()
        val availableResonse = async { mRepo.getAvailableList() }.await()

        val descList = descResponse.body()?.data?.park
        val availableList = availableResonse.body()?.data?.park

        if (descList != null && availableList != null) {
            joinParkList(descList, availableList)
        } else {
            mParkList.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                .getString(R.string.no_internet_connection))))
        }
    }

    private fun joinParkList(descList: List<Park>, availableList: List<AvailablePark>) {
        var map = HashMap<String, AvailablePark>()
        availableList.forEach {
            map.put(it.id, it)
        }

        var list = mutableListOf<ParkLotInfo>()
        descList.forEach {
            val availablePark = map[it.id]
            var parkLotInfo = ParkLotInfo(it.id)
            parkLotInfo.address = it.address
            parkLotInfo.name = it.name
            parkLotInfo.totalcar = it.totalcar

            if (availablePark != null) {
                parkLotInfo.availablecar = availablePark.availablecar

                if (availablePark.ChargeStation != null) {
                    parkLotInfo.scoketStatusList = availablePark.ChargeStation.scoketStatusList
                }
            }
            list.add(parkLotInfo)
        }
        mParkList.postValue(Event(ResponseResult.Success(list)))
    }
}