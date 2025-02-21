package com.lion.wandertrip.vo

import com.google.firebase.Timestamp
import com.lion.wandertrip.model.RecentTripItemModel
import com.lion.wandertrip.util.ContentTypeId

data class RecentTripItemVO(
    val contentID :String = "",
    val contentTypeID: Int = ContentTypeId.TOURIST_ATTRACTION.contentTypeCode,
    val title : String = "",
    val imageUri : String ="",
    val clickedTimeStamp : Timestamp = Timestamp.now()
) {
    fun toRecentTripItemModel(recentTripItemVO: RecentTripItemVO): RecentTripItemModel {
        return RecentTripItemModel(
            contentID = recentTripItemVO.contentID,
            contentTypeID = when (recentTripItemVO.contentTypeID) {
                ContentTypeId.TOURIST_ATTRACTION.contentTypeCode -> ContentTypeId.TOURIST_ATTRACTION
                ContentTypeId.RESTAURANT.contentTypeCode -> ContentTypeId.RESTAURANT
                else -> ContentTypeId.ACCOMMODATION
            },
            title = recentTripItemVO.title,
            imageUri = recentTripItemVO.imageUri,
            clickedTimeStamp = recentTripItemVO.clickedTimeStamp
        )
    }

}