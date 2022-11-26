package com.penchan.noodoeapi.model.entity

data class AvailablePark(
    val ChargeStation: ChargeStation,
    val availablebus: Int,
    val availablecar: Int,
    val availablemotor: Int,
    val id: String
)