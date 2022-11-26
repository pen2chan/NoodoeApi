package com.penchan.noodoeapi.repository.repo

import com.penchan.noodoeapi.retrofit.RetrofitClient

class ParkRepo : IRepo {

    suspend fun getDescList() = RetrofitClient.parkService.getParkDesc()
    suspend fun getAvailableList() = RetrofitClient.parkService.getParkAvailable()

}