package com.lion.wandertrip.presentation.my_interesting_page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lion.a02_boardcloneproject.component.CustomIconButton
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.R
import com.lion.wandertrip.presentation.my_interesting_page.components.CityDropdownButton
import com.lion.wandertrip.presentation.my_interesting_page.components.CustomChipButton
import com.lion.wandertrip.presentation.my_trip_page.MyTripViewModel
import com.lion.wandertrip.util.CustomFont
import com.lion.wandertrip.util.Tools

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInterestingScreen(myInterestingViewModel: MyInterestingViewModel = hiltViewModel()) {
    // Scroll 상태를 기억하기 위한 rememberScrollState 사용
    val scrollState = rememberScrollState()
    myInterestingViewModel.getInterestingList()
    myInterestingViewModel.getLocalList()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                navigationIconOnClick = {
                    myInterestingViewModel.onClickNavIconBack()
                },
                navigationIconImage = ImageVector.vectorResource(R.drawable.ic_arrow_back_24px),
                title = "내 저장",
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CityDropdownButton(myInterestingViewModel) {
                    myInterestingViewModel.isSheetOpen.value = true
                }
                CustomChipButton("관광지", {
                    myInterestingViewModel.onClickButtonAttraction()
                }, myInterestingViewModel.isCheckAttraction)
                CustomChipButton("식당", {
                    myInterestingViewModel.onClickButtonRestaurant()
                }, myInterestingViewModel.isCheckRestaurant)
                CustomChipButton("숙소", {
                    myInterestingViewModel.onClickButtonAccommodation()
                }, myInterestingViewModel.isCheckAccommodation)
            }
        }
        // BottomSheet가 표시될 때의 설정
        if (myInterestingViewModel.isSheetOpen.value) {
            ModalBottomSheet(
                onDismissRequest = { myInterestingViewModel.isSheetOpen.value = false }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "전체 도시",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                myInterestingViewModel.filteredCityName.value = "전체도시"
                                myInterestingViewModel.isSheetOpen.value = false
                            }
                            .padding(16.dp),
                        fontFamily = CustomFont.customFontRegular
                    )
                    myInterestingViewModel.localList.forEach { city ->
                        Text(
                            text = "$city ${Tools.areaCodeMap[city]}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    myInterestingViewModel.filteredCityName.value =
                                        "${Tools.areaCodeMap[city]}"
                                    myInterestingViewModel.isSheetOpen.value = false
                                }
                                .padding(16.dp),
                            fontFamily = CustomFont.customFontRegular

                        )
                    }
                }
            }
        }
    }
}
