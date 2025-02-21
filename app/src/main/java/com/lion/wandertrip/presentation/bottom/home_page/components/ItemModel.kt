package com.lion.wandertrip.presentation.bottom.home_page.components

import com.lion.wandertrip.util.ContentTypeId

data class SimpleTripItemModel(
    val contentID: String = "",
    val contentTypeID: ContentTypeId = ContentTypeId.TOURIST_ATTRACTION,
    val contentSmallImageUri: String = "",
    val title: String = "",
    val areaCode: String = "0",
    val siGunGuCode: String = "0",
    val cat2: String = "",
    val cat3: String = "",
) {
    fun toSimpleTripItemVO(simpleTripItemModel: SimpleTripItemModel): SimpleTripItemVO {
        val simpleTripItemVO = SimpleTripItemVO(
            contentID = simpleTripItemModel.contentID,
            contentTypeID = when (simpleTripItemModel.contentTypeID) {
                ContentTypeId.TOURIST_ATTRACTION -> ContentTypeId.TOURIST_ATTRACTION.contentTypeCode
                ContentTypeId.RESTAURANT -> ContentTypeId.RESTAURANT.contentTypeCode
                ContentTypeId.ACCOMMODATION -> ContentTypeId.ACCOMMODATION.contentTypeCode
            },
            contentSmallImageUri = simpleTripItemModel.contentSmallImageUri,
            title = simpleTripItemModel.title,
            areaCode = simpleTripItemModel.areaCode,
            siGunGuCode = simpleTripItemModel.siGunGuCode,
            cat2 = simpleTripItemModel.cat2,
            cat3 = simpleTripItemModel.cat3
        )

        return simpleTripItemVO
    }
}
data class SimpleTripItemVO (
    val contentID: String ="",
    val contentTypeID: Int = ContentTypeId.TOURIST_ATTRACTION.contentTypeCode,
    val contentSmallImageUri: String="",
    val title: String="",
    val areaCode :String ="0",
    val siGunGuCode : String="0",
    val cat2 : String = "",
    val cat3 : String = "",
){
    fun toSimpleTripItemModel(simpleTripItemVo: SimpleTripItemVO): SimpleTripItemModel {
        val simpleTripItemModel = SimpleTripItemModel(
            contentID = simpleTripItemVo.contentID,
            contentTypeID = when (simpleTripItemVo.contentTypeID) {
                ContentTypeId.TOURIST_ATTRACTION.contentTypeCode -> ContentTypeId.TOURIST_ATTRACTION
                ContentTypeId.RESTAURANT.contentTypeCode -> ContentTypeId.RESTAURANT
                else -> ContentTypeId.ACCOMMODATION
            },
            contentSmallImageUri = simpleTripItemVo.contentSmallImageUri,
            title = simpleTripItemVo.title,
            areaCode = simpleTripItemVo.areaCode,
            siGunGuCode = simpleTripItemVo.siGunGuCode,
            cat2 = simpleTripItemVo.cat2,
            cat3 = simpleTripItemVo.cat3
        )

        return simpleTripItemModel
    }
}