package com.penchan.noodoeapi.repository.api

import com.penchan.noodoeapi.model.entity.ApiResult
import com.penchan.noodoeapi.model.entity.AvailableData
import com.penchan.noodoeapi.model.entity.DescData
import retrofit2.Response
import retrofit2.http.GET

interface ParkService {

    /**
     * Get All Desc List
     */
    @GET("/blobtcmsv/TCMSV_alldesc.json")
    suspend fun getParkDesc() : Response<ApiResult<DescData>>


    /**
     * Get All Available List
     */
    @GET("/blobtcmsv/TCMSV_allavailable.json")
    suspend fun getParkAvailable() : Response<ApiResult<AvailableData>>
}