package com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data

import com.lion.wandertrip.model.TripItemModel
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.ScheduleDummyData.Companion.scheduleStartDate1
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.ScheduleDummyData.Companion.scheduleStartDate2
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.ScheduleDummyData.Companion.scheduleStartDate3
import com.lion.wandertrip.presentation.bottom.my_info_page.used_dummy_data.ScheduleDummyData.Companion.scheduleStartDate4

class RecentPostsDummyData {
    companion object{
        // 12: 관광지, 32: 숙박, 39: 음식점
        val RecentPostsDummyDataList = mutableListOf<TripItemModel>(
           TripItemModel(
               title = "속초해수욕장 대관람차(속초아이)",
               contentTypeId = "12",
               firstImage = "http://tong.visitkorea.or.kr/cms/resource/72/3069472_image3_1.JPG" ),
            TripItemModel(
                title = "광화문",
                contentTypeId = "12",
                firstImage = "http://tong.visitkorea.or.kr/cms/resource/72/3069472_image3_1.JPG"    ),
            TripItemModel(
                title = "광화문갈비 원주점",
                contentTypeId = "39",
                firstImage = "https://lh3.googleusercontent.com/p/AF1QipMW_jX2TFUAY5VEtjHv7wIwzPKUc3nJ2Fakwpn4=s680-w680-h510"
            )
        )
    }
}