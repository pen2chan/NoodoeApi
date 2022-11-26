package com.penchan.noodoeapi.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.penchan.noodoeapi.R
import com.penchan.noodoeapi.app.MyApplication
import com.penchan.noodoeapi.base.Event
import com.penchan.noodoeapi.base.ResponseResult
import com.penchan.noodoeapi.model.entity.LoginInfo
import com.penchan.noodoeapi.repository.repo.LoginRepo
import com.penchan.noodoeapi.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class LoginViewModel(
    app: Application,
    private var mRepo: LoginRepo) : AndroidViewModel(app) {

    private val mLoginInfo: MutableLiveData<Event<ResponseResult<LoginInfo>>> = MutableLiveData()

    val loginInfo: LiveData<Event<ResponseResult<LoginInfo>>>
        get() = mLoginInfo

    fun loginUser(body: HashMap<String, String>) = viewModelScope.launch(Dispatchers.IO) {
        mLoginInfo.postValue(Event(ResponseResult.Loading()))
        try {
            if (Utils.checkConnection(getApplication<MyApplication>())) {
                val response = mRepo.loginUser(body)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        mLoginInfo.postValue(Event(ResponseResult.Success(body)))
                    }
                } else {
                    mLoginInfo.postValue(Event(ResponseResult.Error(response.errorBody()!!.string())))
                }
            } else {
                mLoginInfo.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                    .getString(R.string.no_internet_connection))))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> {
                    mLoginInfo.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                        .getString(R.string.network_failure))))
                } else -> {
                    mLoginInfo.postValue(Event(ResponseResult.Error(getApplication<MyApplication>()
                    .getString(R.string.conversion_error))))
                }
            }
        }
    }
}