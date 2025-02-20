package com.lion.wandertrip.presentation.bottom.my_info_page

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.model.UserModel
import com.lion.wandertrip.service.TripScheduleService
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val userService: UserService,
    val tripScheduleService: TripScheduleService,
) : ViewModel(){

    // userModelState
    val userModel = UserModel()
    val userModelValue = mutableStateOf(userModel)
    // context
    val tripApplication = context as TripApplication

    // 보여줄 이미지의 Uri
    val showImageUri = mutableStateOf<Uri?>(null)

    val recentScheduleList = mutableStateListOf<TripScheduleModel>()

    // 프로필 편집
    fun onClickTextUserInfoModify() {
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_USER_INFO.name)
    }

    // 내 여행 화면 전환
    fun onClickIconMyTrip() {
        Log.d("test100"," 내여행")
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_TRIP.name)
    }
    // 내 저장 화면 전환
    fun onClickIconMyInteresting() {
        Log.d("test100"," 내저장")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_INTERESTING.name)
    }
    // 내 리뷰 화면 전환
    fun onClickIconMyReview() {
        Log.d("test100"," 내리부")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_REVIEW.name)
    }
    // 내 여행기 화면 전환
    fun onClickIconTripNote() {
        Log.d("test100"," 내 여행기")

        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_MY_TRIP_NOTE.name)
    }

    // 최근 게시물 클릭 리스너
    fun onClickCardRecentContent(contentId: String) {
        tripApplication.navHostController.navigate("${MainScreenName.MAIN_SCREEN_DETAIL.name}/$contentId")
    }

    // userModel 가져오기
    fun gettingUserModel() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1= async(Dispatchers.IO){
                userService.getUserByUserDocId(tripApplication.loginUserModel.userDocId)
            }
            userModelValue.value = work1.await()
            if(userModelValue.value.userProfileImageURL !=""){
                val work2 = async(Dispatchers.IO){
                    userService.gettingImage(userModelValue.value.userProfileImageURL)
                }
                showImageUri.value = work2.await()
            }
        }
    }

    // 화면 열때 리스트 가져오기
    fun getTripList() {

        viewModelScope.launch {
            val work1 = async(Dispatchers.IO){
                tripScheduleService.gettingMyTripSchedules(tripApplication.loginUserModel.userId)
            }
            val result = work1.await()
            recentScheduleList.addAll(
                result
            )
        }

    }


}