package com.lion.wandertrip.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lion.wandertrip.util.UserState
import com.lion.wandertrip.vo.UserVO
import kotlinx.coroutines.tasks.await
import java.io.File

class UserRepository {

    // userID로 userVO찾기
    suspend fun selectUserDataByUserId(userId: String): UserVO? {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")

        // 쿼리 결과 가져오기
        val result = collectionReference.whereEqualTo("userId", userId).get().await()

        // 데이터가 없다면 null 반환
        if (result.isEmpty) {
            Log.d("UserData", "사용자 아이디 '$userId'에 해당하는 사용자가 없습니다.")
            return null
        }

        // 데이터가 있을 경우 첫 번째 사용자 정보 반환
        return result.toObjects(UserVO::class.java).firstOrNull()
    }

    // 사용자 닉네임을 통해 사용자 데이터를 가져오는 메서드
    suspend fun selectUserDataByUserNickName(userNickName: String): MutableList<UserVO> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val result = collectionReference.whereEqualTo("userNickName", userNickName).get().await()
        val userVoList = result.toObjects(UserVO::class.java)
        return userVoList
    }

    // 사용자 정보를 추가하는 메서드
    fun addUserData(userVO: UserVO): String {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val documentReference = collectionReference.document()
        val addUserVO = userVO
        addUserVO.userDocId = documentReference.id
        documentReference.set(addUserVO)
        return documentReference.id
    }

    // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
    suspend fun selectUserDataByUserIdOne(userId: String): MutableMap<String, *> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val result = collectionReference.whereEqualTo("userId", userId).get().await()
        val userVoList = result.toObjects(UserVO::class.java)

        val userMap = mutableMapOf(
            "user_document_id" to result.documents[0].id,
            "user_vo" to userVoList[0]
        )
        return userMap
    }

    // 자동로그인 토큰값을 갱신하는 메서드
    suspend fun updateUserAutoLoginToken(userDocumentId: String, newToken: String) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val documentReference = collectionReference.document(userDocumentId)
        val tokenMap = mapOf(
            "userAutoLoginToken" to newToken
        )
        documentReference.update(tokenMap).await()
    }

    // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
    suspend fun selectUserDataByLoginToken(loginToken: String): Map<String, *>? {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val resultList =
            collectionReference.whereEqualTo("userAutoLoginToken", loginToken).get().await()
        val userVOList = resultList.toObjects(UserVO::class.java)
        if (userVOList.isEmpty()) {
            return null
        } else {
            val userDocumentId = resultList.documents[0].id
            val returnMap = mapOf(
                "userDocumentId" to userDocumentId,
                "userVO" to userVOList[0]
            )
            return returnMap
        }
    }

    // 카카오 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
    suspend fun selectUserDataByKakaoLoginToken(kToken: String): UserVO? {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")

        return try {
            // Firestore에서 데이터를 조회
            val result = collectionReference.whereEqualTo("userKakaoToken", kToken).get().await()

            // 데이터를 객체로 변환하여 반환
            val userVo = result.toObjects(UserVO::class.java).firstOrNull()

            // 결과가 없으면 null 반환
            if (userVo == null) {
                Log.d("test100", "No user found for the given Kakao token")
            }

            userVo
        } catch (e: Exception) {
            // 예외 발생 시 로그 출력
            Log.e("test100", "Failed to fetch user data by Kakao token", e)
            null  // 예외 발생 시 null 반환
        }
    }

    // 사용자 정보 전체를 가져오는 메서드
    suspend fun selectUserDataAll(): MutableList<MutableMap<String, *>> {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val result = collectionReference.get().await()
        val userList = mutableListOf<MutableMap<String, *>>()
        result.forEach {
            val userMap = mutableMapOf(
                "user_document_id" to it.id,
                "user_vo" to it.toObject(UserVO::class.java)
            )
            userList.add(userMap)
        }
        return userList
    }

    // 사용자 문서 아이디를 통해 사용자 정보를 가져온다.
    suspend fun selectUserDataByUserDocumentIdOne(userDocumentId: String): UserVO {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val result = collectionReference.document(userDocumentId).get().await()
        val userVO = result.toObject(UserVO::class.java)!!
        return userVO
    }


    // 사용자 데이터를 수정한다.
    suspend fun updateUserData(userVO: UserVO) {
        // Firestore 인스턴스 가져오기
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")

        // 수정할 문서에 접근
        val documentReference = collectionReference.document(userVO.userDocId)

        try {
            // UserVO 객체 그대로 업데이트 (set 사용)
            documentReference.set(userVO).await()  // set() 메서드로 데이터 저장
            Log.d("Firestore", "사용자 데이터 업데이트 성공")
        } catch (e: Exception) {
            Log.e("Firestore", "사용자 데이터 업데이트 실패", e)
        }
    }

    // 사용자의 상태를 변경하는 메서드
    suspend fun updateUserState(userDocumentId: String, newState: UserState) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")
        val documentReference = collectionReference.document(userDocumentId)

        val updateMap = mapOf(
            "userState" to newState.number
        )

        documentReference.update(updateMap).await()
    }

    // 이미지 데이터를 서버로 업로드 하는 메서드
    suspend fun uploadImage(sourceFilePath: String, serverFilePath: String) {
        // 저장되어 있는 이미지의 경로
        val file = File(sourceFilePath)
        val fileUri = Uri.fromFile(file)
        // 업로드 한다.
        val firebaseStorage = FirebaseStorage.getInstance()
        val childReference = firebaseStorage.reference.child("userProfileImage/$serverFilePath")
        childReference.putFile(fileUri).await()
    }

    // userDocID 로 user 정보 가져오기
    suspend fun getUserByUserDocId(userDocId: String): UserVO? {
        val firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("UserData")

        Log.d("test200", "getUserByUserDocId() 호출됨 - userDocId: $userDocId")

        return try {
            val documentSnapshot = collectionReference.document(userDocId).get().await()

            if (documentSnapshot.exists()) {
                Log.d("test200", "문서 존재함 - userDocId: $userDocId")
                val user = documentSnapshot.toObject(UserVO::class.java)
                Log.d("test200", "변환된 UserVO: $user")
                user // Firestore 데이터를 UserVO 객체로 변환
            } else {
                Log.d("test200", "문서 없음 - userDocId: $userDocId")
                null
            }
        } catch (e: Exception) {
            Log.e("test200", "오류 발생 - userDocId: $userDocId", e)
            e.printStackTrace()
            null
        }
    }

    // 이미지 Uri 가져온다.
    // 이미지 데이터를 가져온다.
    suspend fun gettingImage(imageFileName: String): Uri {
        Log.d("gettingImage", "이미지 파일명을 받음: $imageFileName")

        val storageReference = FirebaseStorage.getInstance().reference
        Log.d("gettingImage", "Firebase Storage 레퍼런스 초기화됨")

        // 파일명을 지정하여 이미지 데이터를 가져온다.
        val childStorageReference = storageReference.child("userProfileImage/$imageFileName")
        Log.d("gettingImage", "이미지 파일 경로: userProfileImage/$imageFileName")

        try {
            val imageUri = childStorageReference.downloadUrl.await()
            Log.d("gettingImage", "이미지 URI 가져옴: $imageUri")
            return imageUri
        } catch (e: Exception) {
            Log.e("gettingImage", "이미지 URI 가져오기 실패: ${e.message}")
            throw e
        }
    }
}