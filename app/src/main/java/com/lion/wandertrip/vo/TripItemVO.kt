package com.lion.wandertrip.vo

import com.lion.wandertrip.model.TripItemModel

class TripItemVO {
    var addr1: String = ""
    var addr2: String = ""
    var areaCode: String = ""
    var contentId: String = ""
    var contentTypeId: String = ""
    var firstImage: String = ""
    var mapLat: Double = 0.0
    var mapLong: Double = 0.0
    var tel: String = ""
    var title: String = ""

    fun toTripItemModel(): TripItemModel {
        val tripItemModel = TripItemModel()
        tripItemModel.addr1 = addr1
        tripItemModel.addr2 = addr2
        tripItemModel.areaCode = areaCode
        tripItemModel.contentId = contentId
        tripItemModel.contentTypeId = contentTypeId
        tripItemModel.firstImage = firstImage
        tripItemModel.mapLat = mapLat
        tripItemModel.mapLong = mapLong
        tripItemModel.tel = tel
        tripItemModel.title = title
        return tripItemModel
    }
}
