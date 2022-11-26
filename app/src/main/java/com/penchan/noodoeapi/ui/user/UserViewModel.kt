package com.penchan.noodoeapi.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.penchan.noodoeapi.R
import com.penchan.noodoeapi.app.MyApplication
import com.penchan.noodoeapi.base.Event
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.model.entity.ActionResult
import com.penchan.noodoeapi.repository.repo.UserRepo
import com.penchan.noodoeapi.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class UserViewModel(
    app: Application,
    private var mRepo: UserRepo
) : AndroidViewModel(app) {

    private val mActionResult: MutableLiveData<Event<ResponseResult<ActionResult>>> = MutableLiveData()

    val actionResult: MutableLiveData<Event<ResponseResult<ActionResult>>>
        get() = mActionResult

    fun putUserData(
        body: HashMap<String, String>,
        objectId: String,
        token: String) = viewModelScope.launch(Dispatchers.IO) {
        mActionResult.postValue(Event(ResponseResult.Loading()))
        try {
            if (Utils.checkConnection(getApplication<MyApplication>())) {
                if (objectId != null && token != null) {
                    val response = mRepo.putUserInfo(body, objectId, token)
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            mActionResult.postValue(Event(ResponseResult.Success(body)))
                        }
                    } else {
                        mActionResult.postValue(Event(ResponseResult.Error(response.errorBody()!!.string())))
                    }
                } else {
                    mActionResult.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                            .getString(R.string.param_error))))
                }
            } else {
                mActionResult.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                    .getString(R.string.no_internet_connection))))
            }

        } catch (t: Throwable) {
            when(t) {
                is IOException -> {
                    mActionResult.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                        .getString(R.string.network_failure))))
                } else -> {
                    mActionResult.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                    .getString(R.string.conversion_error))))
                }
            }
        }
    }
}