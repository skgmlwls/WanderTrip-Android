package com.lion.wandertrip.retrofit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PublicDataRepository {

//    fun fetchPublicData(onResult: (ApiResponse?) -> Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = RetrofitClient.apiService.getItems(
//                    numOfRows = 10,
//                    pageNo = 1,
//                    mobileOS = "AND",             // 예: 안드로이드
//                    mobileApp = "WanderTrip",       // 어플명
//                    serviceKey = "YOUR_SERVICE_KEY",// 실제 서비스키로 교체
//                    type = "json",
//                    showflag = "1",
//                    contentTypeId = "15"            // 예: 축제공연행사
//                )
//                onResult(response)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                onResult(null)
//            }
//        }
//    }
}