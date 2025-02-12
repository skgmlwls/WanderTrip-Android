package com.lion.wandertrip.vo

import com.google.firebase.Timestamp
import com.lion.wandertrip.model.TripNoteReplyModel
import com.lion.wandertrip.model.ReplyItem

class TripNoteReplyVO {
    var tripNoteDocumentId: String = ""
    var replyList: List<ReplyItemVO> = emptyList()

    fun toTripNoteReplyModel(): TripNoteReplyModel {
        val tripNoteReplyModel = TripNoteReplyModel()
        tripNoteReplyModel.tripNoteDocumentId = tripNoteDocumentId
        tripNoteReplyModel.replyList = replyList.map { it.toReplyItemModel() }
        return tripNoteReplyModel
    }
}

class ReplyItemVO {
    var replyDocumentId: String = ""
    var userNickname: String = ""
    var replyText: String = ""
    var replyState: Int = 1
    var replyTimeStamp: Timestamp = Timestamp.now()

    fun toReplyItemModel(): ReplyItem {
        val replyItemModel = ReplyItem()
        replyItemModel.replyDocumentId = replyDocumentId
        replyItemModel.userNickname = userNickname
        replyItemModel.replyText = replyText
        replyItemModel.replyState = replyState
        replyItemModel.replyTimeStamp = replyTimeStamp
        return replyItemModel
    }
}
