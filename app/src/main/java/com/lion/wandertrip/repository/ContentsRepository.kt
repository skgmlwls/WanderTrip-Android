package com.lion.wandertrip.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.lion.wandertrip.vo.ContentsVO
import kotlinx.coroutines.tasks.await

class ContentsRepository {
    suspend fun getContentByDocId(contentsDocId: String):ContentsVO {
        return try {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("ContentsData").document(contentsDocId).get().await()

            if (document.exists()) {
                document.toObject(ContentsVO::class.java) ?: ContentsVO()
            } else {
                ContentsVO()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ContentsVO() // 예외 발생 시 기본값 반환
        }
    }

}