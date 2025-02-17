package com.lion.wandertrip.presentation.detail_page

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.DetailModel
import com.lion.wandertrip.presentation.detail_page.used_dummy_data.DetailDummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(@ApplicationContext context: Context) :ViewModel(){
    val tripApplication = context as TripApplication

    // 소개 상태
    val isClickIntroState = mutableStateOf(true)
    // 기본 정보 상태
    val isClickBasicInfoState = mutableStateOf(false)
    // 후기 상태
    val isClickReviewState = mutableStateOf(false)
    // 컨텐트 모델
    val contentModelValue = mutableStateOf(DetailModel())

    // 컨텐트 ID 로 모델 가져오기
    fun getContentModel() {
        contentModelValue.value = DetailDummyData.detailDummyModel
    }

    // 소개 버튼 리스너
    fun onClickButtonIntro() {
        isClickIntroState.value=true
        isClickBasicInfoState.value=false
        isClickReviewState.value=false
    }
    // 기본 정보 리스너
    fun onClickButtonBasicInfo() {
        isClickIntroState.value=false
        isClickBasicInfoState.value=true
        isClickReviewState.value=false

    }
    // 후기 리스너
    fun onClickButtonReview() {
        isClickIntroState.value=false
        isClickBasicInfoState.value=false
        isClickReviewState.value=true

    }

    // 지도 눌렀을때 리스너
    fun onClickIconMap() {

    }

    // 더보기 눌렀을때 리스너
    fun onClickIconMenu() {

    }

    // 뒤로가기 리스너
    fun onClickNavIconBack() {
        tripApplication.navHostController.popBackStack()
    }

    // 홈페이지 따는 정규식
    fun getHomePage() {
        val inputString = """<a href="https://plaza.seoul.go.kr/?doing_wp_cron=1701728399.7732338905334472656250" target="_blank" title="새창 : 홈페이지로 이동">https://plaza.seoul.go.kr</a>"""
        val regex = "https://[\\S]+".toRegex()  // https:로 시작하는 URL을 찾는 정규식
        val match = regex.find(inputString)

        val homepageUrl = match?.value
        println(homepageUrl)  // "https://plaza.seoul.go.kr"
    }



}