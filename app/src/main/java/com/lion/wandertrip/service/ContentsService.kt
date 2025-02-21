package com.lion.wandertrip.service

import com.lion.wandertrip.model.ContentsModel
import com.lion.wandertrip.repository.ContentsRepository

// 컨텐츠 가져오는 서비스
class ContentsService(val contentsRepository: ContentsRepository) {

    suspend fun getContentByDocId(contentDocId: String): ContentsModel {
        val contentsVO = contentsRepository.getContentByDocId(contentDocId) // 데이터를 가져옴
        return contentsVO.toDetailReviewModel() // 변환 후 반환
    }

}