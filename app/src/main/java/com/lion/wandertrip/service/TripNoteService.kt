package com.lion.wandertrip.service

import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.repository.TripNoteRepository
import javax.inject.Inject

class TripNoteService @Inject constructor(val tripNoteRepository: TripNoteRepository) {

    // 이미지 데이터를 서버로 업로드 하는 메서드
    suspend fun uploadTripNoteImage(sourceFilePath:String, serverFilePath:String){
        tripNoteRepository.uploadTripNoteImage(sourceFilePath, serverFilePath)
    }

    // 여행기 데이터를 저장하는 메서드
    // 새롭게 추가된 문서의 id를 반환한다.
    suspend fun addTripNoteData(tripNoteModel: TripNoteModel) : String{
        // VO 객체를 생성한다.
        val tripNoteVO = tripNoteModel.toTripNoteVO()
        // 저장한다.
        val documentId = tripNoteRepository.addTripNoteData(tripNoteVO)
        return documentId
    }
}