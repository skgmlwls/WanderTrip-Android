package com.lion.wandertrip.presentation.user_info_page

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.BotNavScreenName
import com.lion.wandertrip.util.MainScreenName
import com.lion.wandertrip.util.Tools
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
) : ViewModel() {
    val tripApplication = context as TripApplication

    // 변경할 닉네임 변수
    val textFieldUserNicknameValue = mutableStateOf(tripApplication.loginUserModel.userNickName)

    // 카메라나 앨범을 통해 가져온 사진을 담을 상태변수
    val imageBitmapState = mutableStateOf<Bitmap?>(null)

    // 보여줄 이미지의 Uri
    val showImageUri = mutableStateOf<Uri?>(null)

    // 앨범이나 사진에서 이미지를 받아왔는지 상태 관리 변수
    val isImagePicked = mutableStateOf(false)

    // appplication 에서 이미지를 가져왔는지 상태 관리 변수
    val hasProfileImage = mutableStateOf(false)


    // 네비게이션 아이콘을 누르면 호출되는 메서드
    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 체크 메뉴 아이콘을 누르면 호출되는 메서드
    fun onClickMenuIconCheck() {
        tripApplication.navHostController.popBackStack()
    }

    // 체크 아이콘 누를 때 리스너
    // 유저 정보 수정 메서드
    fun onClickIconCheck() {
        CoroutineScope(Dispatchers.Main).launch {
            val userModel = tripApplication.loginUserModel
            var imageUri = userModel.userProfileImageURL

            Log.d("test100", "현재 유저 이미지 URL: $imageUri")

            if (isImagePicked.value) {
                // 새로운 이미지 파일명 생성
                imageUri = "image_${System.currentTimeMillis()}.jpg"
                Log.d("test100", "새로운 이미지 파일명: $imageUri")

                // 이미지 저장 (파일 경로 설정)
                Tools.saveBitmap(tripApplication, imageBitmapState.value!!)
                Log.d("test100", "이미지 저장 완료")
            }

            // 닉네임 업데이트
            userModel.userNickName = textFieldUserNicknameValue.value
            userModel.userProfileImageURL = imageUri
            Log.d("test100", "닉네임 변경: ${userModel.userNickName}")

            // 병렬로 Firestore에 이미지와 사용자 데이터 업데이트
            val filePath = tripApplication.getExternalFilesDir(null).toString()
            Log.d("test100", "외부 저장소 경로: $filePath")

            val uploadJob = async(Dispatchers.IO) {
                if (isImagePicked.value) {
                    Log.d("test100", "이미지 업로드 시작")
                    userService.uploadImage("$filePath/uploadTemp.jpg", imageUri)
                    Log.d("test100", "이미지 업로드 완료")
                }
            }

            val updateUser = async(Dispatchers.IO) {
                Log.d("test100", "사용자 데이터 업데이트 시작")
                userService.updateUserData(userModel)
                Log.d("test100", "사용자 데이터 업데이트 완료")
            }

            // 모든 작업 완료 후 업데이트
            uploadJob.await()
            updateUser.await()
            Log.d("test100", "모든 업데이트 완료")

            // 업데이트된 데이터 저장
            tripApplication.loginUserModel = userModel
            Log.d("test100", "유저 모델 업데이트 완료")

            // 뒤로 가기
            tripApplication.navHostController.popBackStack()
            Log.d("test100", "화면 이동 완료")
        }
    }



    fun hasImageInApplication(){
        if(tripApplication.loginUserModel.userProfileImageURL!=""){
            viewModelScope.launch {
                val work2 = async(Dispatchers.IO){
                    userService.gettingImage(tripApplication.loginUserModel.userProfileImageURL)
                }
                showImageUri.value = work2.await()
            }
        }
    }

}
