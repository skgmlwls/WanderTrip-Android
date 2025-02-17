package com.lion.wandertrip.presentation.detail_page

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lion.wandertrip.TripApplication
import com.lion.wandertrip.model.DetailModel
import com.lion.wandertrip.presentation.detail_page.used_dummy_data.DetailDummyData
import com.lion.wandertrip.util.MainScreenName
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
        tripApplication.navHostController.navigate(MainScreenName.MAIN_SCREEN_GOOGLE_MAP.name)
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

    // 홈페이지 클릭 시 브라우저로 열기
    fun onClickTextHomepage(homepage: String) {
        if(homepage!=""){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(homepage))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // 플래그 추가 , context가 필요한데 context가 아닌 application으로 부르기때문에 플래그가 필요함
            tripApplication.startActivity(intent)
        }
    }

    // 전화 클릭 시 전화 앱 켜기
    fun onClickTextTel(telNum: String) {
        // 널체크
        if (telNum != "") {
            val number = telNum.replace("-","")

            // 전화 앱을 여는 Intent 생성
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            // 플래그 추가 , context가 필요한데 context가 아닌 application으로 부르기때문에 플래그가 필요함
            dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // 전화 앱을 열기
            tripApplication.startActivity(dialIntent)
        }
    }



}