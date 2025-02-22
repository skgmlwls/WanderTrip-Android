package com.lion.wandertrip.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.vo.ReviewVO
import kotlinx.coroutines.tasks.await

class ContentsReviewRepository {

    // 리뷰 문서 1개 가져오기
    suspend fun getContentsReviewByDocId(
        contentsDocId: String,
        contentsReviewDocId: String
    ): ReviewVO {
        return try {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("ContentsData")
                .document(contentsDocId)
                .collection("ContentsReview") // 서브컬렉션 접근
                .document(contentsReviewDocId)
                .get()
                .await()

            if (document.exists()) {
                document.toObject(ReviewVO::class.java) ?: ReviewVO()
            } else {
                ReviewVO()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ReviewVO() // 예외 발생 시 기본값 반환
        }
    }


    // 해당 컨텐츠에 있는 모든 리뷰 문서 가져오기
    suspend fun getAllReviewsWithContents(contentsDocId: String): MutableList<ReviewVO> {
        val reviewList = mutableListOf<ReviewVO>()

        try {
            val db = FirebaseFirestore.getInstance()
            val contentsRef = db.collection("ContentsData").document(contentsDocId)

            // 리뷰 서브컬렉션에서 모든 문서 가져오기
            val reviewsQuerySnapshot = contentsRef.collection("ContentsReview").get().await()

            // 모든 리뷰 문서 처리
            for (document in reviewsQuerySnapshot.documents) {
                val reviewVO = document.toObject(ReviewVO::class.java)

                reviewList.add(reviewVO!!)

            }

            //Log.d("test100", "ContentsReviewRepository -> getAllReviewsWithContents  모든 리뷰 문서 가져오기 성공: $contentsDocId")

        } catch (e: Exception) {
            // 예외 발생 시
            Log.e("test100", "ContentsReviewRepository -> getAllReviewsWithContents 모든 리뷰 문서 가져오기 실패: $contentsDocId", e)
        }

        return reviewList
    }

    // 리뷰 등록
    suspend fun addContentsReview(contentsId: String, reviewVO: ReviewVO): String {
         try {
            val db = FirebaseFirestore.getInstance()
            val contentsQuery = db.collection("ContentsData").whereEqualTo("contentId", contentsId).get().await()

            if (contentsQuery.isEmpty) {
                // Log.e("test100", "ContentsReviewRepository -> addContentsReview 해당 contentsId를 가진 문서 없음: $contentsId")
                return ""
            }

            val document = contentsQuery.documents.first() // 첫 번째 문서 사용
            val contentsRef = db.collection("ContentsData").document(document.id)

            // 리뷰 추가
            val reviewRef = contentsRef.collection("ContentsReview").document()
            reviewVO.reviewDocId = reviewRef.id
            reviewVO.contentsDocId = document.id // 찾은 문서 ID 저장
            reviewRef.set(reviewVO).await()

            // Log.d("test100", "ContentsReviewRepository -> addContentsReview 리뷰 등록 성공: ${reviewRef.id} (ContentsID: $contentsId)")

            return document.id
        } catch (e: Exception) {
            Log.e("test100", "리뷰 등록 실패: $contentsId", e)
            return ""
        }
    }

    // 리뷰 수정하기
    suspend fun modifyContentsReview(contentsID: String, reviewVO: ReviewVO): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val contentsRef = db.collection("ContentsData").document(contentsID)

            // 리뷰 수정 (덮어쓰기)
            val reviewRef = contentsRef.collection("ContentsReview").document(reviewVO.reviewDocId)
            reviewRef.set(reviewVO).await()

            // Log.d("test100", "ContentsReviewRepository -> modifyContentsReview 리뷰 덮어쓰기 성공: ${reviewVO.reviewDocId}")

            true
        } catch (e: Exception) {
            Log.e("test100", "리뷰 덮어쓰기 실패", e)
            false
        }
    }


}