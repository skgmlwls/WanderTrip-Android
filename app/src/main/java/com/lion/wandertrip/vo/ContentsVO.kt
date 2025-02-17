package com.lion.wandertrip.vo

import com.google.firebase.Timestamp
import com.lion.wandertrip.model.ContentsModel
import com.lion.wandertrip.model.ReviewModel

class ContentsVO {
    var contentDocId: String = ""
    var contentId: String = ""
    var ratingScore: String = ""
    var reviewList: List<ReviewVO> = emptyList()

    fun toDetailReviewModel(): ContentsModel {
        val detailReviewModel = ContentsModel()
        detailReviewModel.contentDocId = contentDocId
        detailReviewModel.contentId = contentId
        detailReviewModel.ratingScore = ratingScore
        detailReviewModel.reviewList = reviewList.map { it.toReviewItemModel() }
        return detailReviewModel
    }
}
