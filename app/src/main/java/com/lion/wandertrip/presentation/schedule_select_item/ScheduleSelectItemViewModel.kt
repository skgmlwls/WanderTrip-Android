package com.lion.wandertrip.presentation.schedule_select_item

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.ApiResponse
import com.lion.wandertrip.model.Item
import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.retrofit.RetrofitClient
import com.lion.wandertrip.retrofit.TripItemRetrofitVO
import com.lion.wandertrip.vo.TripItemVO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleSelectItemViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    val application = context as TripApplication

    // 여행지 항목 리스트
    val tripItemModel = mutableListOf<TripItemModel>()


    // 이전 화면 으로 이동 (일정 상세 화면)
    fun backScreen() {
        application.navHostController.popBackStack()
    }

    // API 호출 및 데이터 로드
    fun loadTripItems(serviceKey: String, areaCode: String, contentTypeId: String) : List<TripItemVO>? {
        // ✅ TripItemModel 대신 TripItemVO 리스트 사용
        val tripItemList = mutableListOf<TripItemVO>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rawResponse = RetrofitClient.apiService.getItems(
                    serviceKey = serviceKey,
                    numOfRows = 100000,
                    pageNo = 1,
                    mobileOS = "AND",
                    mobileApp = "WanderTrip",
                    type = "json",
                    showflag = "1",
                    listYN = "Y",
                    arrange = "A",
                    contentTypeId = contentTypeId,
                    areaCode = areaCode
                )

                // 🚀 응답 로그 출력
                Log.d("APIResponseRaw", "Response: $rawResponse")

                // JSON 파싱
                val apiResponse = RetrofitClient.gson.fromJson(rawResponse, ApiResponse::class.java)
                val items = apiResponse.response.body?.items?.item ?: emptyList()

                // ✅ 변환을 TripItemVO 내부에서 처리
                val tripItemVOs = items.map { TripItemVO.from(it) }


                tripItemList.clear()
                tripItemList.addAll(tripItemVOs)
                tripItemList.forEach {
                    Log.d("APIProcessedData", "저장된 데이터: ${it.title}")
                }
                Log.d("APIProcessedData", "총 데이터 개수: ${tripItemList.size}")

            } catch (e: Exception) {
                Log.e("APIError", "API 호출 오류: ${e.message}")
            }
        }
        return tripItemList
    }




}