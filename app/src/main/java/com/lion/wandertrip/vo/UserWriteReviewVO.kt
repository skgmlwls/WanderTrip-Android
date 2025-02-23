package com.lion.wandertrip.vo

import com.google.firebase.Timestamp
import com.lion.wandertrip.model.UserWriteReviewModel

data class UserWriteReviewVO(
    var userWriteReviewDocId: String = "",
    var contentsDocId: String = "",
    var contentsId: String = "",
    var timeStamp: Timestamp = Timestamp.now()
) {
    // UserWriteReviewVO를 UserWriteReviewModel로 변환하는 메서드
    fun toUserWriteReviewModel(userWriteReviewVo :UserWriteReviewVO): UserWriteReviewModel {
        return UserWriteReviewModel(
            userWriteReviewDocId = userWriteReviewVo.userWriteReviewDocId,
            contentsDocId = userWriteReviewVo.contentsDocId,
            contentsId = userWriteReviewVo.contentsId,
            timeStamp = userWriteReviewVo.timeStamp
        )
    }
}