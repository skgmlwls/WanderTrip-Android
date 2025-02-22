package com.lion.wandertrip.service

import android.util.Log
import androidx.core.graphics.rotationMatrix
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.repository.ContentsReviewRepository
import com.lion.wandertrip.vo.ReviewVO

class ContentsReviewService(val contentsReviewRepository: ContentsReviewRepository) {

    // 해당 리뷰 가져오기
    suspend fun getContentsReviewByDocId(contentsDocId: String, contentsReviewDocId: String): ReviewModel {
        var reviewModel = ReviewModel()

        try {
            val reviewVO = contentsReviewRepository.getContentsReviewByDocId(contentsDocId, contentsReviewDocId)

            // 변환 성공 시
            reviewModel = reviewVO.toReviewItemModel()

        } catch (e: Exception) {
            // 예외 발생 시
            Log.e("test100", "리뷰 문서 가져오기 실패: $contentsReviewDocId", e)
        }

        return reviewModel
    }

    // 해당 컨텐츠에 있는 모든 리뷰 문서 가져오기
    suspend fun getAllReviewsWithContents(contentsDocId: String): MutableList<ReviewModel> {
        // 리뷰를 저장할 리스트 초기화
        val modelList = mutableListOf<ReviewModel>()

        try {
            // 컨텐츠에 관련된 모든 리뷰를 가져옴
            val voList = contentsReviewRepository.getAllReviewsWithContents(contentsDocId)

            // 가져온 ReviewVO 리스트를 ReviewModel로 변환하여 리스트에 추가
            voList.forEach {
                modelList.add(it.toReviewItemModel())
            }

            // 성공적으로 리뷰들을 가져왔다면 로그 출력
        } catch (e: Exception) {
            // 예외 발생 시 로그에 오류 메시지를 출력하고 빈 리스트를 반환
            Log.e("test100", "모든 리뷰 문서 가져오기 실패: $contentsDocId", e)
        }

        // 변환된 리뷰 모델 리스트 반환
        return modelList
    }

    // 리뷰 등록
    suspend fun addContentsReview(contentsId: String, contentsReviewModel: ReviewModel): String {
         try {
            // 리뷰 추가를 위한 repository 메서드 호출
            val result = contentsReviewRepository.addContentsReview(contentsId, contentsReviewModel.toReviewItemVO())

            // 리뷰 추가 성공 시 로그 출력

            return result // 성공한 경우 true 반환
        } catch (e: Exception) {
             // 예외 발생 시 로그에 오류 메시지 출력
             Log.e("test100", "리뷰 등록 실패: $contentsId", e)
            return ""
         }
    }

    // 리뷰 수정하기
    suspend fun modifyContentsReview(contentsID: String, reviewVO: ReviewVO): Boolean {
        return try {
            // 리뷰 수정 요청을 위한 repository 메서드 호출
            val result = contentsReviewRepository.modifyContentsReview(contentsID, reviewVO)

            result // 성공한 경우 true 반환
        } catch (e: Exception) {
            // 예외 발생 시 로그에 오류 메시지 출력
            Log.e("test100", "리뷰 수정 실패: $contentsID", e)

            // 실패 시 false 반환
            false
        }
    }

}