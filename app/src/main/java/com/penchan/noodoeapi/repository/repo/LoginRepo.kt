package com.penchan.noodoeapi.repository.repo

import com.penchan.noodoeapi.retrofit.RetrofitClient

class LoginRepo : IRepo {
    suspend fun loginUser(body: HashMap<String, String>) = RetrofitClient.loginService.postLogin(body)
}