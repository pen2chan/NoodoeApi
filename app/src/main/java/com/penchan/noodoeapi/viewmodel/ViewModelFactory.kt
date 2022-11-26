package com.penchan.noodoeapi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.penchan.noodoeapi.repository.repo.IRepo
import com.penchan.noodoeapi.repository.repo.LoginRepo
import com.penchan.noodoeapi.repository.repo.ParkRepo
import com.penchan.noodoeapi.repository.repo.UserRepo
import com.penchan.noodoeapi.ui.login.LoginViewModel
import com.penchan.noodoeapi.ui.park.ParkViewModel
import com.penchan.noodoeapi.ui.user.UserViewModel

class ViewModelFactory(
    val app: Application,
    val repo: IRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(app, repo as LoginRepo) as T
        } else if (modelClass.isAssignableFrom(ParkViewModel::class.java)) {
            return ParkViewModel(app, repo as ParkRepo) as T
        } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(app, repo as UserRepo) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}