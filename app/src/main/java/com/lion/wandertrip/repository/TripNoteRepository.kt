package com.lion.wandertrip.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
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
}