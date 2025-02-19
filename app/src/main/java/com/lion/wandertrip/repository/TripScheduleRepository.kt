package com.lion.wandertrip.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.retrofit.ApiResponse
import com.lion.wandertrip.retrofit.RetrofitClient
import com.lion.wandertrip.vo.ScheduleItemVO
import com.lion.wandertrip.vo.TripItemVO
import com.lion.wandertrip.vo.TripScheduleVO
import kotlinx.coroutines.tasks.await

class TripScheduleRepository {

    // 일정을 추가 하는 메서드
    suspend fun addTripSchedule(tripScheduleVO: TripScheduleVO) : String {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("TripSchedule")

        // 1) Firestore에서 자동 생성할 문서 레퍼런스 획득
        val docRef = collectionReference.document()  // 자동 생성된 문서 ID가 docRef.id에 담김

        // 2) 문서 ID를 VO에 저장
        tripScheduleVO.tripScheduleDocId = docRef.id

        // 3) Firestore에 저장 (코루틴을 쓰므로 .await() 사용)
        docRef.set(tripScheduleVO).await()

        return docRef.id
    }

    // 일정 조회 (VO 리턴)
    suspend fun getTripSchedule(docId: String): TripScheduleVO? {
        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("TripSchedule").document(docId)

        val snapshot = docRef.get().await()
        if (snapshot.exists()) {
            // 스냅샷을 VO로 변환
            return snapshot.toObject(TripScheduleVO::class.java)
        }
        return null
    }

    // TripSchedule 서브 컬렉션의 모든 문서를 ScheduleItemVO 리스트로 조회
    suspend fun getTripScheduleItems(docId: String): List<ScheduleItemVO>? {
        val firestore = FirebaseFirestore.getInstance()
        val subCollectionRef = firestore.collection("TripSchedule")
            .document(docId)
            .collection("TripScheduleItem")

        val snapshot = subCollectionRef.get().await()
        if (!snapshot.isEmpty) {
            return snapshot.toObjects(ScheduleItemVO::class.java)
        }
        return emptyList()
    }

    // API 호출 및 데이터 로드
    suspend fun loadTripItems(serviceKey: String, areaCode: String, contentTypeId: String) : List<TripItemVO>? {
        // ✅ TripItemModel 대신 TripItemVO 리스트 사용
        val tripItemList = mutableListOf<TripItemVO>()

        try {
            val rawResponse = RetrofitClient.apiService.getItems(
                serviceKey = serviceKey,
                numOfRows = 100000,
                pageNo = 1,
                mobileOS = "AND",
                mobileApp = "WanderTrip",
                type = "json",
                showflag = "1",
                listYN = "Y",
                arrange = "A",
                contentTypeId = contentTypeId,
                areaCode = areaCode,
            )

            // 🚀 응답 로그 출력
            Log.d("APIResponseRaw", "Response: $rawResponse")

            // JSON 파싱
            val apiResponse = RetrofitClient.gson.fromJson(rawResponse, ApiResponse::class.java)
            val items = apiResponse.response.body?.items?.item ?: emptyList()

            // ✅ 변환을 TripItemVO 내부에서 처리
            val tripItemVOs = items.map { TripItemVO.from(it) }


            tripItemList.clear()
            tripItemList.addAll(tripItemVOs)
            tripItemList.forEach {
                Log.d("APIProcessedData", "저장된 데이터: ${it.title}")
            }
            Log.d("APIProcessedData", "총 데이터 개수: ${tripItemList.size}")

        } catch (e: Exception) {
            Log.e("APIError", "API 호출 오류: ${e.message}")
        }
        return tripItemList
    }

}