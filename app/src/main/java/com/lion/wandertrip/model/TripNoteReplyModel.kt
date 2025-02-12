package com.lion.wandertrip.model

import com.google.firebase.Timestamp
import com.lion.wandertrip.vo.TripNoteReplyVO
import com.lion.wandertrip.vo.ReplyItemVO

class TripNoteReplyModel(
    var tripNoteDocumentId: String = "",   // 여행기 문서 ID
    var replyList: List<ReplyItem> = emptyList() // 댓글 목록
) {
    fun toTripNoteReplyVO(): TripNoteReplyVO {
        val tripNoteReplyVO = TripNoteReplyVO()
        tripNoteReplyVO.tripNoteDocumentId = tripNoteDocumentId
        tripNoteReplyVO.replyList = replyList.map { it.toReplyItemVO() }
        return tripNoteReplyVO
    }
}

class ReplyItem(
    var replyDocumentId: String = "",   // 댓글 문서 ID
    var userNickname: String = "",      // 작성자 닉네임
    var replyText: String = "",         // 댓글 내용
    var replyState: Int = 1,            // 댓글 상태 (1: 활성화, 2: 삭제)
    var replyTimeStamp: Timestamp = Timestamp.now() // 댓글 등록 시간
) {
    fun toReplyItemVO(): ReplyItemVO {
        val replyItemVO = ReplyItemVO()
        replyItemVO.replyDocumentId = replyDocumentId
        replyItemVO.userNickname = userNickname
        replyItemVO.replyText = replyText
        replyItemVO.replyState = replyState
        replyItemVO.replyTimeStamp = replyTimeStamp
        return replyItemVO
    }
}
