package com.penchan.noodoeapi.repository.api

import com.penchan.noodoeapi.model.entity.ActionResult
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    /**
     * Put User Info
     */
    @Headers("X-Parse-Application-Id:vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    @PUT("/api/users/{objectId}")
    suspend fun putUserInfo(
        @Body body: HashMap<String, String>,
        @Path("objectId") objectId: String,
        @Header("X-Parse-Session-Token") token: String
    ) : Response<ActionResult>
}