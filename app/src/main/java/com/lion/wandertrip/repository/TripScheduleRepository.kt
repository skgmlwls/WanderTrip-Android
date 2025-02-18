package com.lion.wandertrip.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.vo.ScheduleItemVO
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


}