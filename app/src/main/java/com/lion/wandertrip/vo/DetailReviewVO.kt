package com.lion.wandertrip.vo

import com.google.firebase.Timestamp
import com.lion.wandertrip.model.DetailReviewModel
import com.lion.wandertrip.model.ReviewItem

class DetailReviewVO {
    var contentDocId: String = ""
    var contentId: String = ""
    var ratingScore: String = ""
    var reviewList: List<ReviewItemVO> = emptyList()

    fun toDetailReviewModel(): DetailReviewModel {
        val detailReviewModel = DetailReviewModel()
        detailReviewModel.contentDocId = contentDocId
        detailReviewModel.contentId = contentId
        detailReviewModel.ratingScore = ratingScore
        detailReviewModel.reviewList = reviewList.map { it.toReviewItemModel() }
        return detailReviewModel
    }
}

class ReviewItemVO {
    var reviewDocId: String = ""
    var contentDocId: String = ""
    var reviewWriterNickname: String = ""
    var reviewContent: String = ""
    var reviewTimeStamp: Timestamp = Timestamp.now()
    var reviewRatingScore: Int = 0
    var reviewImageList: List<String> = emptyList()

    fun toReviewItemModel(): ReviewItem {
        val reviewItemModel = ReviewItem()
        reviewItemModel.reviewDocId = reviewDocId
        reviewItemModel.contentDocId = contentDocId
        reviewItemModel.reviewWriterNickname = reviewWriterNickname
        reviewItemModel.reviewContent = reviewContent
        reviewItemModel.reviewTimeStamp = reviewTimeStamp
        reviewItemModel.reviewRatingScore = reviewRatingScore
        reviewItemModel.reviewImageList = reviewImageList
        return reviewItemModel
    }
}
