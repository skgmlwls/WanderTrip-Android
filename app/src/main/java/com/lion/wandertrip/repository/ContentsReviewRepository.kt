package com.lion.wandertrip.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lion.wandertrip.vo.ReviewVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

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


    suspend fun getAllReviewsWithContents(contentId: String): MutableList<ReviewVO> {
        val reviewList = mutableListOf<ReviewVO>()

        //Log.d("test100", "ContentsReviewRepository -> getAllReviewsWithContents 호출됨: $contentId")

        try {
            val db = FirebaseFirestore.getInstance()

            // ContentsData 컬렉션에서 contentsId가 같은 문서 찾기
            val contentsQuerySnapshot = db.collection("ContentsData")
                .whereEqualTo("contentId", contentId)
                .get()
                .await()

            if (contentsQuerySnapshot.isEmpty) {
                Log.w("test100", "ContentsReviewRepository -> 해당 contentsId를 가진 문서가 없음: $contentId")
                return reviewList
            }

            // 첫 번째 문서 가져오기 (보통 contentsId가 유일하다고 가정)
            val document = contentsQuerySnapshot.documents.first()
            //Log.d("test100", "ContentsReviewRepository -> 찾은 문서 ID: ${document.id}")

            // 리뷰 서브컬렉션에서 모든 문서 가져오기
            val reviewsQuerySnapshot = document.reference.collection("ContentsReview").get().await()

            //Log.d("test100", "ContentsReviewRepository -> 리뷰 문서 개수: ${reviewsQuerySnapshot.size()}")

            // 모든 리뷰 문서 처리
            for (reviewDoc in reviewsQuerySnapshot.documents) {
                val reviewVO = reviewDoc.toObject(ReviewVO::class.java)
                //Log.d("test100", "ContentsReviewRepository -> 변환된 ReviewVO: $reviewVO")

                if (reviewVO != null) {
                    reviewList.add(reviewVO)
                } else {
                    Log.w("test100", "ContentsReviewRepository -> 변환 실패한 문서 ID: ${reviewDoc.id}")
                }
            }

            //Log.d("test100", "ContentsReviewRepository -> 모든 리뷰 문서 가져오기 성공: $contentId, 총 개수: ${reviewList.size}")

        } catch (e: Exception) {
            Log.e(
                "test100",
                "ContentsReviewRepository -> getAllReviewsWithContents 실패: $contentId",
                e
            )
        }

        return reviewList
    }


    // 리뷰 등록
    suspend fun addContentsReview(contentsId: String, reviewVO: ReviewVO): String {
        try {
            val db = FirebaseFirestore.getInstance()
            val contentsQuery =
                db.collection("ContentsData").whereEqualTo("contentId", contentsId).get().await()

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

    suspend fun modifyContentsReview(contentsDocId: String, reviewVO: ReviewVO): Boolean {
        return try {
            // Firestore 인스턴스 가져오기
            val db = FirebaseFirestore.getInstance()
            Log.d("ContentsReviewRepository", "Firestore 인스턴스 가져오기 성공")

            // ContentsData 컬렉션의 해당 문서 참조
            val contentsRef = db.collection("ContentsData").document(contentsDocId)
            Log.d(
                "ContentsReviewRepository",
                "ContentsData 문서 참조 성공: contentsDocId = $contentsDocId"
            )

            // 리뷰 수정할 컬렉션의 문서 참조
            val reviewRef = contentsRef.collection("ContentsReview").document(reviewVO.reviewDocId)
            Log.d(
                "ContentsReviewRepository",
                "ContentsReview 컬렉션의 문서 참조 성공: reviewDocId = ${reviewVO.reviewDocId}"
            )

            // 리뷰 덮어쓰기
            reviewRef.set(reviewVO).await()
            Log.d("ContentsReviewRepository", "리뷰 덮어쓰기 성공: ${reviewVO.reviewDocId}")

            // 수정 성공
            true
        } catch (e: Exception) {
            // 예외 발생 시 에러 로그
            Log.e("ContentsReviewRepository", "리뷰 덮어쓰기 실패: ${reviewVO.reviewDocId}", e)
            // 수정 실패
            false
        }
    }

    /*
        //닉네임 바꿀 때 사용하기
        // 닉변 전 게시물의 닉네임을 변경한 닉네임으로 update
        suspend fun changeReviewNickName(oldNickName: String, newNickName: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collRef = firestore.collection("TripSchedule")

            try {
                val querySnapshot = collRef.whereEqualTo("userNickName", oldNickName).get().await()

                if (querySnapshot.isEmpty) {
                    Log.d("test100", "변경할 닉네임($oldNickName)이 존재하지 않습니다.")
                    return
                }

                for (document in querySnapshot.documents) {
                    val docRef = collRef.document(document.id)
                    docRef.update("userNickName", newNickName).await()
                }
            } catch (e: Exception) {
                Log.e("test100", "닉네임 변경 중 오류 발생: $e", e)
            }
        }
    */

    // 이미지 데이터를 서버로 업로드 하는 메서드
    suspend fun uploadReviewImage(sourceFilePath: List<String>, serverFilePath: MutableList<String>, contentsId: String) {
        // 업로드된 이미지의 URL들을 저장할 리스트
        val downloadUrls = mutableListOf<String>()

        // 리스트의 각 파일에 대해 업로드 작업을 순차적으로 수행
        for (i in sourceFilePath.indices) {
            val sourceFile = File(sourceFilePath[i])  // 소스 파일 경로
            val fileUri = Uri.fromFile(sourceFile)

            Log.d("FirebaseStorage", "Uploading file: ${sourceFile.path}, Exists: ${sourceFile.exists()}")

            // Firebase Storage의 경로 설정
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child("contentsReviewImage/$contentsId/${serverFilePath[i]}")

            try {
                Log.d("FirebaseStorage", "Starting upload: ${fileUri.path} to ${childReference.path}")

                // 파일 업로드
                val uploadTask = childReference.putFile(fileUri).await()

                Log.d("FirebaseStorage", "Upload success: ${fileUri.path}")

                // 업로드 완료 후 다운로드 URL 가져오기
                val downloadUrl = childReference.downloadUrl.await().toString()

                Log.d("FirebaseStorage", "Download URL: $downloadUrl")

                // 다운로드 URL을 리스트에 추가
                downloadUrls.add(downloadUrl)
            } catch (e: Exception) {
                // 업로드 실패 시 로그 출력
                Log.e("FirebaseStorage", "Image upload failed for file: ${sourceFile.path}", e)
            }
        }
    }


    // 이미지 Uri 가져온다.
    // 이미지 Uri 리스트를 가져오는 함수
    suspend fun gettingReviewImage(imageFileNameList: List<String>,contentsId: String): List<Uri> {
        Log.d("gettingImage", "이미지 파일명을 받음: ${imageFileNameList.joinToString()}")

        val storageReference = FirebaseStorage.getInstance().reference
        Log.d("gettingImage", "Firebase Storage 레퍼런스 초기화됨")

        return try {
            // 각 파일명에 대해 URI를 가져오는 작업을 비동기 처리
            val uriList = imageFileNameList.map { fileName ->
                val childStorageReference = storageReference.child("contentsReviewImage/$contentsId/$fileName")
                Log.d("gettingImage", "이미지 파일 경로: contentsReviewImage$contentsId/$fileName")
                childStorageReference.downloadUrl.await()  // 개별적으로 URI 가져오기
            }

            Log.d("gettingImage", "이미지 URI 리스트 가져옴: ${uriList.joinToString()}")
            uriList
        } catch (e: Exception) {
            Log.e("gettingImage", "이미지 URI 가져오기 실패: ${e.message}")
            emptyList()
        }
    }

}