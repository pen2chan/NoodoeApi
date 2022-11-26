package com.penchan.noodoeapi.model.entity

data class ParkLotInfo (
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var totalcar: Int = 0,
    var availablecar: Int = 0,
    var charging: Int = 0,
    var freecharge: Int = 0
) {
    constructor(id: String) : this() {
        this.id = id
    }
}