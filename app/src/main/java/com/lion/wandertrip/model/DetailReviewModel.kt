package com.lion.wandertrip.model

import com.google.firebase.Timestamp
import com.lion.wandertrip.vo.DetailReviewVO
import com.lion.wandertrip.vo.ReviewItemVO

class DetailReviewModel(
    var contentDocId: String = "",   // 컨텐츠 문서 ID (컬렉션)
    var contentId: String = "",      // 컨텐츠 ID
    var ratingScore: String = "",    // 평균 별점
    var reviewList: List<ReviewItem> = emptyList() // 리뷰 리스트 (서브 컬렉션)
) {
    fun toDetailReviewVO(): DetailReviewVO {
        val detailReviewVO = DetailReviewVO()
        detailReviewVO.contentDocId = contentDocId
        detailReviewVO.contentId = contentId
        detailReviewVO.ratingScore = ratingScore
        detailReviewVO.reviewList = reviewList.map { it.toReviewItemVO() }
        return detailReviewVO
    }
}

class ReviewItem(
    var reviewDocId: String = "",    // 리뷰 문서 ID
    var contentDocId: String = "",   // 컨텐츠 문서 ID
    var reviewWriterNickname: String = "", // 작성자 닉네임
    var reviewContent: String = "",  // 리뷰 내용
    var reviewTimeStamp: Timestamp = Timestamp.now(), // 리뷰 작성 시간
    var reviewRatingScore: Int = 0,  // 리뷰 별점
    var reviewImageList: List<String> = emptyList() // 리뷰 이미지 리스트
) {
    fun toReviewItemVO(): ReviewItemVO {
        val reviewItemVO = ReviewItemVO()
        reviewItemVO.reviewDocId = reviewDocId
        reviewItemVO.contentDocId = contentDocId
        reviewItemVO.reviewWriterNickname = reviewWriterNickname
        reviewItemVO.reviewContent = reviewContent
        reviewItemVO.reviewTimeStamp = reviewTimeStamp
        reviewItemVO.reviewRatingScore = reviewRatingScore
        reviewItemVO.reviewImageList = reviewImageList
        return reviewItemVO
    }
}
