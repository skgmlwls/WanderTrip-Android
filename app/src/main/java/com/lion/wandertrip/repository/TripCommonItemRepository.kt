package com.lion.wandertrip.repository

import android.util.Log
import com.lion.wandertrip.model.TripCommonItem
import com.lion.wandertrip.retrofit_for_practice.TripCommonItemInterface
class TripCommonItemRepository(private val api: TripCommonItemInterface) {
    val myKey = "qe8EY3d1ixNdu4/lC0aXJXbTH/VndGcoj5DABUigtfSCLIIP48IHXbwMEkG5gkGvVW/wKl1XuuFyqYwwWQZJDg=="

    suspend fun gettingTripItemCommon(
        contentId: String,
        contentTypeId: String?
    ): TripCommonItem? {
        Log.d("test100", "TripCommonItemRepository: Starting API call")

        return try {
            val response = api.getCommonTripItem(
                serviceKey = myKey,
                mobileOS = "ETC",
                mobileApp = "com.lion.wandertrip",
                type = "json",
                contentId = contentId,
                contentTypeId = contentTypeId ?: "",
                defaultYN = "Y",
                firstImageYN = "Y",
                areacodeYN = "Y",
                catcodeYN = "Y",
                addrinfoYN = "Y",
                mapinfoYN = "Y",
                overviewYN = "Y",
                numOfRows = 10,
                pageNo = 1
            )

            if (response.isSuccessful) {
                Log.d("test100", "API request was successful")
                Log.d("test100", "Response Body: ${response.body()}")

                // 응답이 성공적이라면, 응답 데이터에서 필요한 정보를 추출하여 TripCommonItem으로 매핑
                response.body()?.let { apiResponse ->
                    val item = apiResponse.response.body.items.item.firstOrNull() // 첫 번째 항목을 가져옵니다.
                    // 매핑하여 반환
                    Log.d("test","item: $item")
                    item?.let {
                        TripCommonItem(
                            contentId = it.contentId,
                            contentTypeId = it.contentTypeId,
                            title = it.title,
                            tel = it.tel,
                            homepage = it.homepage,
                            firstImage = it.firstImage,
                            siGunGuCode = it.siGunGuCode,
                            areaCode = it.areaCode,
                            addr1 = it.addr1,
                            addr2 = it.addr2,
                            mapLat = it.mapLat,
                            mapLng = it.mapLng,
                            overview = it.overview
                        )
                    }
                }
            } else {
                Log.d("test100", "API request failed: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("test100", "Error occurred while fetching trip item", e)
            null
        }
    }
}