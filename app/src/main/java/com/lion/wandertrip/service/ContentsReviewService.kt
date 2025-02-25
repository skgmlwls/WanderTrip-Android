package com.lion.wandertrip.service

import android.net.Uri
import android.util.Log
import com.lion.wandertrip.model.ReviewModel
import com.lion.wandertrip.repository.ContentsReviewRepository
import com.lion.wandertrip.vo.ReviewVO

class ContentsReviewService(val contentsReviewRepository: ContentsReviewRepository) {

    // 사용자의 리뷰 문서 가져오기
    suspend fun getContentsMyReview(contentsWriterNickName: String): MutableList<ReviewModel> {
        val voList = contentsReviewRepository.getContentsMyReview(contentsWriterNickName)
        val resultList = mutableListOf<ReviewModel>()
        voList.forEach {
            resultList.add(it.toReviewItemModel())
        }
        return resultList
    }


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
             Log.e("ContentsReviewService", "리뷰 등록 실패: $contentsId", e)
            return ""
         }
    }

    // 리뷰 수정하기
    suspend fun modifyContentsReview(contentsDocID: String, reviewModel: ReviewModel): Boolean {
        return try {
            // 리뷰 수정 요청을 위한 repository 메서드 호출 전 로그 추가
            //Log.d("ContentsReviewService", "리뷰 수정 시작: contentsDocID = $contentsDocID, reviewModel = $reviewModel")

            // 리뷰 수정 요청
            val result = contentsReviewRepository.modifyContentsReview(contentsDocID, reviewModel.toReviewItemVO())

            // 성공 로그
            if (result) {
               // Log.d("ContentsReviewService", "리뷰 수정 성공: contentsDocID = $contentsDocID, reviewDocId = ${reviewModel.reviewDocId}")
            } else {
                //Log.d("ContentsReviewService", "리뷰 수정 실패 (기타): contentsDocID = $contentsDocID")
            }

            result // 성공한 경우 true 반환
        } catch (e: Exception) {
            // 예외 발생 시 에러 로그에 구체적인 메시지 출력
            Log.e("test100", "리뷰 수정 실패: contentsDocID = $contentsDocID", e)
            // 예외를 출력하여 문제 파악에 도움을 줍니다.
            Log.e("test100", "에러 메시지: ${e.message}")
            Log.e("test100", "스택 트레이스: ${Log.getStackTraceString(e)}")

            // 실패 시 false 반환
            false
        }
    }


    // 이미지 데이터를 서버로 업로드 하는 메서드
    suspend fun uploadReviewImageList(sourceFilePath: List<String>, serverFilePath: MutableList<String>, contentsId: String) :List<String>  {
       return contentsReviewRepository.uploadReviewImageList(sourceFilePath,serverFilePath,contentsId)
    }

    // 이미지 Uri 리스트를 가져오는 메서드
    suspend fun gettingReviewImage(imageFileNameList: List<String>,contentsId: String): List<Uri> {
        return contentsReviewRepository.gettingReviewImageList(imageFileNameList,contentsId)
    }

    // 리뷰 삭제 메서드
    suspend fun deleteContentsReview(contentsDocId: String, contentsReviewDocId: String) {
        contentsReviewRepository.deleteContentsReview(contentsDocId,contentsReviewDocId)
    }

}