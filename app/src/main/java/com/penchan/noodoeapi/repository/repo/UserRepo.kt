package com.penchan.noodoeapi.repository.repo

import com.penchan.noodoeapi.retrofit.RetrofitClient

class UserRepo : IRepo {

    suspend fun putUserInfo(
        body: HashMap<String, String>,
        objectId: String,
        token: String) = RetrofitClient.userService.putUserInfo(body, objectId, token)
}