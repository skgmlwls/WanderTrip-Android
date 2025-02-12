package com.lion.wandertrip.vo

import com.lion.wandertrip.model.DetailModel

class DetailVO {
    var detailTitle: String = ""
    var detailRating: Int = 0
    var detailLat: Double = 0.0
    var detailLong: Double = 0.0
    var detailImage: String = ""
    var detailDescription: String = ""
    var detailAddress: String = ""
    var detailPhoneNumber: String = ""
    var detailHomepage: String = ""

    fun toDetailModel(): DetailModel {
        val detailModel = DetailModel()
        detailModel.detailTitle = detailTitle
        detailModel.detailRating = detailRating
        detailModel.detailLat = detailLat
        detailModel.detailLong = detailLong
        detailModel.detailImage = detailImage
        detailModel.detailDescription = detailDescription
        detailModel.detailAddress = detailAddress
        detailModel.detailPhoneNumber = detailPhoneNumber
        detailModel.detailHomepage = detailHomepage
        return detailModel
    }
}
