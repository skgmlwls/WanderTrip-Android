package com.lion.wandertrip.service

import android.net.Uri
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.model.TripNoteReplyModel
import com.lion.wandertrip.repository.TripNoteRepository
import com.lion.wandertrip.vo.TripNoteReplyVO
import com.lion.wandertrip.vo.TripNoteVO
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

    // 여행기 댓글을 저장하는 메서드
    // 새롭게 추가된 문서의 id를 반환한다.
    suspend fun addTripNoteReplyData(tripNoteReplyModel: TripNoteReplyModel) : String{
        // VO 객체를 생성한다.
        val tripNoteReplyVO = tripNoteReplyModel.toReplyItemVO()
        // 저장한다.
        val documentId = tripNoteRepository.addTripNoteReplyData(tripNoteReplyVO)
        return documentId
    }

    // 여행기 댓글 리스트를 가져오는 메서드
    // 여행기 문서 id를 통해 데이터 가져오기
    suspend fun selectReplyDataOneById(documentId:String) : MutableList<TripNoteReplyModel>{

        // 데이터를 가져온다
        val tripNoteReplyList = mutableListOf<TripNoteReplyModel>()
        val resultList = tripNoteRepository.selectReplyDataOneById(documentId)

        resultList.forEach {
            val tripNoteReplyVO = it["tripNoteReplyVO"] as TripNoteReplyVO
            val documentId = it["documentId"] as String
            val tripNoteReplyModel = tripNoteReplyVO.toReplyItemModel(documentId)
            tripNoteReplyList.add(tripNoteReplyModel)
        }

        return tripNoteReplyList
    }

    // 여행기 리스트를 가져오는 메서드
    suspend fun gettingTripNoteList() : MutableList<TripNoteModel>{
        // 여행기 정보를 가져온다.
        val tripNoteList = mutableListOf<TripNoteModel>()
        val resultList = tripNoteRepository.gettingTripNoteList()

        resultList.forEach {
            val tripNoteVO = it["tripNoteVO"] as TripNoteVO
            val documentId = it["documentId"] as String
            val tripNoteImage = it["tripNoteImage"] as List<String>?
            val tripNoteModel = tripNoteVO.toTripNoteModel(documentId)
            tripNoteModel.tripNoteImage = tripNoteImage!!
            tripNoteList.add(tripNoteModel)
        }

        return tripNoteList
    }

    // 여행기 문서 id를 통해 데이터 가져오기
    suspend fun selectTripNoteDataOneById(documentId:String) : TripNoteModel{
        // 글 데이터를 가져온다.
        val tripNoteVO = tripNoteRepository.selectTripNoteDataOneById(documentId)
        // BoardModel객체를 생성한다.
        val tripNoteModel = tripNoteVO.toTripNoteModel(documentId)

        return tripNoteModel
    }

    // 이미지 데이터를 가져온다.
    suspend fun gettingImage(imageFileName:String) : Uri {
        val imageUri = tripNoteRepository.gettingImage(imageFileName)
        return imageUri
    }
}