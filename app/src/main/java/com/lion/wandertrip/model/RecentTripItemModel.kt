package com.lion.wandertrip.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.lion.wandertrip.util.ContentTypeId
import com.lion.wandertrip.vo.RecentTripItemVO

data class RecentTripItemModel(
    val contentID :String = "",
    val contentTypeID: ContentTypeId =ContentTypeId.TOURIST_ATTRACTION,
    val title : String = "",
    val imageUri : String ="",
    val clickedTimeStamp : Timestamp = Timestamp.now()
) {
    fun toRecentTripItemVO(recentTripItemModel: RecentTripItemModel): RecentTripItemVO {
        return RecentTripItemVO(
            contentID = recentTripItemModel.contentID,
            contentTypeID = recentTripItemModel.contentTypeID.contentTypeCode, // Enum을 Int로 변환
            title = recentTripItemModel.title,
            imageUri = recentTripItemModel.imageUri,
            clickedTimeStamp = recentTripItemModel.clickedTimeStamp
        )
    }
}