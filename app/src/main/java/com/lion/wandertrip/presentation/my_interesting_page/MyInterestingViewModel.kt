package com.lion.wandertrip.presentation.my_interesting_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.UserInterestingModel
import com.lion.wandertrip.presentation.my_interesting_page.used_dummy_data.InterestingDummyData
import com.lion.wandertrip.service.UserService
import com.lion.wandertrip.util.ContentTypeId
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MyInterestingViewModel @Inject constructor(
    @ApplicationContext context: Context, userService: UserService,
): ViewModel() {
    val tripApplication = context as TripApplication
    // 유저 관심 목록 전체 전체지역
    val interestingListAll = mutableStateListOf<UserInterestingModel>()
    // 지역 필터된 유저 관심 목록 리스트
    val interestingListFilterByCity = mutableStateListOf<UserInterestingModel>()
    // 지역 리스트
    val localList = mutableStateListOf<String>()

    // 초기값은 0 -> 전체도시
    val filteredCityName = mutableStateOf("0")

    // 관광지 클릭 됐는가?
    val isCheckAttraction = mutableStateOf(true)
    // 식당 클릭 됐는가?
    val isCheckRestaurant = mutableStateOf(true)
    // 숙소 클릭 됐는가?
    val isCheckAccommodation = mutableStateOf(true)
    // 바텀 시트 관리 상태 변수
    val isSheetOpen = mutableStateOf(false)
    // 뒤로가기
    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 리스트 가져오기
    fun getInterestingList() {
        interestingListAll.addAll(InterestingDummyData.dummyDataList)
    }


    // 필터로 리스트 가져오기
    fun getInterestingFilter(cityName : String) {
        Log.d("test100","getInterestingFilterByCityList-> cityName : $cityName")
        when(cityName){
            "0"->{
                // 필터 리스트 클리어
                interestingListFilterByCity.clear()
                interestingListFilterByCity.addAll(interestingListAll)
            }
            else->{
                // 필터 리스트 클리어
                interestingListFilterByCity.clear()
                val filteredList = interestingListAll.filter {
                    it.areacode == cityName
                }
                Log.d("test100","filteredList ${filteredList}")
                interestingListFilterByCity.addAll(
                    filteredList
                )
            }
        }

        // 관광지가 선택되지 않았다면
        if (!isCheckAttraction.value) {
            interestingListFilterByCity.removeIf {
                ContentTypeId.TOURIST_ATTRACTION.contentTypeCode.toString() == it.contentTypeID
            }
        }

        // 식당이 선택되지 않았다면
        if (!isCheckRestaurant.value) {
            interestingListFilterByCity.removeIf {
                ContentTypeId.RESTAURANT.contentTypeCode.toString() == it.contentTypeID
            }
        }

        // 숙소가 선택되지 않았다면
        if (!isCheckAccommodation.value) {
            interestingListFilterByCity.removeIf {
                ContentTypeId.ACCOMMODATION.contentTypeCode.toString() == it.contentTypeID
            }
        }
    }

    // 관심 지역 가져오기
    fun getLocalList() {
        localList.add("0")
        interestingListAll.forEach {
            if(!localList.contains(it.areacode)){
                localList.add(it.areacode)
            }
        }
    }

    // 관광지 클릭 리스너
    fun onClickButtonAttraction() {
        isCheckAttraction.value = !isCheckAttraction.value
        getInterestingFilter(filteredCityName.value)
    }

    // 식당 클릭 리스너
    fun onClickButtonRestaurant() {
        isCheckRestaurant.value = !isCheckRestaurant.value
        getInterestingFilter(filteredCityName.value)

    }
    // 숙소 클릭 리스너
    fun onClickButtonAccommodation() {
        isCheckAccommodation.value = !isCheckAccommodation.value
        getInterestingFilter(filteredCityName.value)

    }


}