package com.lion.wandertrip.retrofit

import com.lion.wandertrip.model.Item

data class TripItemRetrofitVO(
    val title: String = "",
    val address: String = "",
    val imageUrl: String = "",
    val contentId: String = "",
    val contentTypeId: String = "",
    val mapX: String = "",
    val mapY: String = ""
) {
    fun toModel(): TripItemRetrofitModel {
        return TripItemRetrofitModel(
            title = title,
            address = address,
            imageUrl = imageUrl,
            contentId = contentId,
            contentTypeId = contentTypeId,
            mapX = mapX,
            mapY = mapY
        )
    }

    companion object {
        fun from(item: Item): TripItemRetrofitVO {
            return TripItemRetrofitVO(
                title = item.title ?: "",
                address = "${item.addr1 ?: ""} ${item.addr2 ?: ""}".trim(),
                imageUrl = item.firstimage ?: "",
                contentId = item.contentid ?: "",
                contentTypeId = item.contenttypeid ?: "",
                mapX = item.mapx ?: "",
                mapY = item.mapy ?: ""
            )
        }
    }
}
