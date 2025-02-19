package com.lion.wandertrip.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.vo.TripNoteVO
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class TripNoteRepository@Inject constructor() {

    // 이미지 데이터를 서버로 업로드 하는 메서드
    suspend fun uploadTripNoteImage(sourceFilePath:String, serverFilePath:String){
        // 저장되어 있는 이미지의 경로
        val file = File(sourceFilePath)
        val fileUri = Uri.fromFile(file)
        // 업로드 한다.
        val firebaseStorage = FirebaseStorage.getInstance()
        val childReference = firebaseStorage.reference.child("image/$serverFilePath")
        childReference.putFile(fileUri).await()
    }

    // 여행기 데이터를 저장하는 메서드
    // 새롭게 추가된 문서의 id를 반환한다.
    suspend fun addTripNoteData(tripNoteVO: TripNoteVO) : String{
        val fireStore = FirebaseFirestore.getInstance()
        val collectionReference = fireStore.collection("TripNoteData")
        val documentReference = collectionReference.add(tripNoteVO).await()
        return documentReference.id
    }

    // 여행기 리스트 가져오는 메서드
    suspend fun gettingTripNoteList(): MutableList<Map<String, *>> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("TripNoteData")
        // 데이터를 가져온다.
        val result =
            collectionReference.orderBy("tripNoteTimeStamp", Query.Direction.DESCENDING).get()
                .await()
        // 반환할 리스트
        val resultList = mutableListOf<Map<String, *>>()
        // 데이터의 수 만큼 반환한다.
        result.forEach {
            val tripNoteVO = it.toObject(TripNoteVO::class.java) // TripNoteVO 객체 가져오기
            val tripNoteImage = tripNoteVO.tripNoteImage

            val map = mapOf(
                // 문서의 id
                "documentId" to it.id,
                // 데이터를 가지고 있는 객체
                "tripNoteVO" to tripNoteVO,
                // 이미지 경로
                "tripNoteImage" to tripNoteImage
            )
            resultList.add(map)
        }
        return resultList
    }
}