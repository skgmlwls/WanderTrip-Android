package com.lion.wandertrip.retrofit

import com.lion.wandertrip.model.TripItemModel

data class TripItemRetrofitModel(
    val title: String = "",
    val address: String = "",
    val imageUrl: String = "",
    val contentId: String = "",
    val contentTypeId: String = "",
    val mapX: String = "",
    val mapY: String = ""
) {
    fun toVO(): TripItemRetrofitVO {
        return TripItemRetrofitVO(
            title = title,
            address = address,
            imageUrl = imageUrl,
            contentId = contentId,
            contentTypeId = contentTypeId,
            mapX = mapX,
            mapY = mapY
        )
    }

    // ✅ 추가: TripItemModel 변환 함수
    fun toTripItemModel(): TripItemModel {
        return TripItemModel(
            title = title,
            // address = address,
            // imageUrl = imageUrl,
            contentId = contentId,
            contentTypeId = contentTypeId,
            // mapX = mapX,
            // mapY = mapY
        )
    }
}
