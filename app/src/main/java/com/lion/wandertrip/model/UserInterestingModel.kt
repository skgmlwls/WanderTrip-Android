package com.lion.wandertrip.model

data class UserInterestingModel(
    val contentID: String = "",
    val contentTypeID: String = "",
    val contentTitle: String = "",
    val areacode: String = "",
    val sigungucode: String = "",
    val saveCount: Int = 0,
    val starRatingCount: Int = 0,
    val ratingScore : Float = 0.0f,
    val smallImagePath: String = ""
)