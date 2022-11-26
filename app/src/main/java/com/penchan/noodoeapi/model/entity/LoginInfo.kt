package com.penchan.noodoeapi.model.entity

data class LoginInfo(
    val objectId: String,
    val name: String,
    val timezone: String,
    val phone: String,
    val createdAt: String,
    val updatedAt: String,
    val sessionToken: String
)
