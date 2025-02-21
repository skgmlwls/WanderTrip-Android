package com.lion.wandertrip.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.retrofit.ApiResponse
import com.lion.wandertrip.retrofit.RetrofitClient
import com.lion.wandertrip.vo.ScheduleItemVO
import com.lion.wandertrip.vo.TripItemVO
import com.lion.wandertrip.vo.TripScheduleVO
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

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


    // ✅ 일정에 여행지 항목 추가 함수
    suspend fun addTripItemToSchedule(docId: String, scheduleDate: Timestamp, scheduleItemVO: ScheduleItemVO) {
        val firestore = FirebaseFirestore.getInstance()
        val subCollectionRef = firestore.collection("TripSchedule")
            .document(docId)
            .collection("TripScheduleItem")

        try {
            // ✅ Firestore에서 scheduleDate와 동일한 날짜를 가진 항목 가져오기
            val snapshot = subCollectionRef.whereEqualTo("itemDate", scheduleDate).get().await()

            // ✅ 동일한 날짜를 가진 항목 중 가장 높은 itemIndex 찾기
            val maxIndex = snapshot.documents
                .mapNotNull { it.getLong("itemIndex")?.toInt() } // 🔹 Long → Int 변환
                .maxOrNull() ?: 0 // 값이 없으면 기본값 0

            // ✅ 새로운 itemIndex 설정 (최소 1부터 시작)
            val newItemIndex = if (maxIndex == 0) 1 else maxIndex + 1

            // ✅ Firestore에 새로운 아이템 추가
            val newItemRef = subCollectionRef.document()
            scheduleItemVO.itemDocId = newItemRef.id // Firestore 문서 ID 설정
            scheduleItemVO.itemIndex = newItemIndex // 새로운 인덱스 설정

            // ✅ Firestore에 저장
            newItemRef.set(scheduleItemVO).await()

            println("새로운 여행지 추가 완료: ${scheduleItemVO.itemTitle} (index: $newItemIndex)")
        } catch (e: Exception) {
            println("Firestore 추가 실패: ${e.message}")
        }
    }



    // 공공 데이터 관련 //////////////////////////////////////////////////////////////////////////////

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
            val apiResponse = Json.decodeFromString<ApiResponse>(rawResponse)
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // hj
    // 내 여행 목록 가져오기
    suspend fun gettingMyTripSchedules(userNickName: String): MutableList<TripScheduleVO> {
        val firestore = FirebaseFirestore.getInstance()
        val collRef = firestore.collection("TripSchedule")

        val tripSchedules = mutableListOf<TripScheduleVO>()

        try {
            // userID가 일치하는 문서를 가져오기 위한 쿼리
            val querySnapshot = collRef.whereEqualTo("userNickName", userNickName).get().await()

            // 가져온 문서를 TripScheduleVO로 변환하여 리스트에 추가
            for (document in querySnapshot.documents) {
                val tripSchedule = document.toObject(TripScheduleVO::class.java)
                if (tripSchedule != null) {
                    tripSchedules.add(tripSchedule)
                }
            }

            // 쿼리 결과 로그 출력 (디버그용)
            Log.d("test100", "userID: $userNickName")

        } catch (e: Exception) {
            // 예외가 발생하면 에러 메시지 로그 출력
            Log.e("test100", "에러남: $userNickName, $e", e)
        }

        // 결과 반환
        return tripSchedules
    }
    // hj
    //닉네임 바꿀 때 사용하기
    // 닉변 전 게시물의 닉네임을 변경한 닉네임으로 update
    suspend fun changeTripScheduleNickName(oldNickName: String, newNickName: String) {
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
    // hj
    // 여행 삭제
    suspend fun deleteTripScheduleByDocId(docId : String) {
        val firestore = FirebaseFirestore.getInstance()
        val collRef = firestore.collection("TripSchedule")

        try {
            val querySnapshot = collRef.whereEqualTo("tripScheduleDocId", docId).get().await()

            if (querySnapshot.isEmpty) {
                Log.d("test100", "($docId)이 존재하지 않습니다.")
                return
            }

            for (document in querySnapshot.documents) {
                val docRef = collRef.document(document.id)
                docRef.delete().await()
            }
        } catch (e: Exception) {
            Log.e("test100", "닉네임 변경 중 오류 발생: $e", e)
        }
    }


}