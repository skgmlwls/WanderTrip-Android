package com.lion.wandertrip.service

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.model.ScheduleItem
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.repository.TripScheduleRepository
import com.lion.wandertrip.vo.ScheduleItemVO
import kotlinx.coroutines.tasks.await

class TripScheduleService(val tripScheduleRepository: TripScheduleRepository) {

    // 일정을 추가 하는 메서드
    suspend fun addTripSchedule(tripScheduleModel: TripScheduleModel) : String {
        // Model -> VO 변환
        val tripScheduleVO = tripScheduleModel.toTripScheduleVO()

        val docId = tripScheduleRepository.addTripSchedule(tripScheduleVO)
        Log.d("TripScheduleService", "docId: $docId")

        // Repository에 VO를 넘겨서 Firestore에 저장
        return docId
    }

    // 일정 조회 (Firestore → VO 변환 → Model 변환)
    suspend fun getTripSchedule(docId: String): TripScheduleModel? {
        val tripScheduleVO = tripScheduleRepository.getTripSchedule(docId) ?: return null
        val tripScheduleModel = tripScheduleVO.toTripScheduleModel()
        Log.d("TripScheduleService", "getTripSchedule: 문서 $docId 조회 완료")
        return tripScheduleModel
    }

    // TripSchedule 서브 컬렉션의 모든 문서를 ScheduleItemVO 리스트로 조회
    suspend fun getTripScheduleItems(docId: String): List<ScheduleItem>? {
        val itemVOList = tripScheduleRepository.getTripScheduleItems(docId) ?: emptyList()
        return itemVOList.map { it.toScheduleItemModel() }
    }

    // 일정에 여행지 항목 추가
    suspend fun addTripItemToSchedule(docId: String, scheduleDate: Timestamp, scheduleItem: ScheduleItem) {
        val scheduleItemVO = scheduleItem.toScheduleItemVO()
        tripScheduleRepository.addTripItemToSchedule(docId, scheduleDate, scheduleItemVO)
    }

    // 일정 항목 삭제 후 itemIndex 재조정
    suspend fun removeTripScheduleItem(scheduleDocId: String, itemDocId: String, itemDate: Timestamp) {
        Log.d("removeTripScheduleItem", "scheduleDocId: $scheduleDocId, itemDocId: $itemDocId")
        tripScheduleRepository.removeTripScheduleItem(scheduleDocId, itemDocId)
    }

    // 공공 데이터 관련 ///////////////////////////////////////////////////////////////////////////////

    // API 호출 및 데이터 로드
    suspend fun loadTripItems(serviceKey: String, areaCode: String, contentTypeId: String) : List<TripItemModel>? {
        val tripItemVOList = tripScheduleRepository.loadTripItems(serviceKey, areaCode, contentTypeId)
        val tripItemModelList = tripItemVOList?.map { it.toTripItemModel() }

        return tripItemModelList
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // hj
    // 내 스케줄 가져오기
    suspend fun gettingMyTripSchedules(userID:String) :MutableList<TripScheduleModel>{
        val voList = tripScheduleRepository.gettingMyTripSchedules(userID)
        val result = mutableListOf<TripScheduleModel>()
        voList.forEach {
            result.add(it.toTripScheduleModel())
        }
        return result
    }
}