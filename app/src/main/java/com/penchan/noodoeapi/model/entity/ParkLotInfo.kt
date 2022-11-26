package com.penchan.noodoeapi.model.entity

data class ParkLotInfo (
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var totalcar: Int = 0,
    var availablecar: Int = -1,
    var charging: Int = -1,
    var freecharge: Int = -1,
    var scoketStatusList: List<ScoketStatus> = emptyList()
) {
    constructor(id: String) : this() {
        this.id = id
    }
}