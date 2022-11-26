package com.penchan.noodoeapi.repository.api

import com.penchan.noodoeapi.model.entity.LoginInfo
import retrofit2.Response
import retrofit2.http.*

interface LoginService {

    /**
     * Post Login
     */
    @Headers("X-Parse-Application-Id:vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    @POST("/api/login")
    suspend fun postLogin(@Body body: HashMap<String, String>) : Response<LoginInfo>

}