package com.lion.wandertrip.presentation.trip_note_detail_page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.lion.a02_boardcloneproject.component.CustomDividerComponent
import com.lion.wandertrip.R
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripNoteDetailScreen(
    tripNoteDetailViewModel: TripNoteDetailViewModel = hiltViewModel(),
) {

    tripNoteDetailViewModel.gettingTripNoteDetailData()
    tripNoteDetailViewModel.gettingTripScheduleDetailData()
    tripNoteDetailViewModel.gettingTripNoteReplyData()







    // tripNoteDetailList를 ViewModel에서 가져옵니다.
    val tripNoteDetailList = tripNoteDetailViewModel.tripNoteDetailList
    val tripNoteDetailData = tripNoteDetailList.firstOrNull()  // 데이터를 제대로 가져옵니다.

    // 이미지 목록 가져오기
    val images = tripNoteDetailData?.tripNoteImage ?: listOf()  // 이미지 목록을 안전하게 처리합니다.

    // HorizontalPager에 필요한 상태를 pageCount를 전달하여 초기화합니다.
    val pagerState = rememberPagerState(pageCount = { images.size })






    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { tripNoteDetailViewModel.navigationButtonClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = "", // 적절한 제목으로 변경
                        fontSize = 20.sp,
                        fontFamily = NanumSquareRound // 폰트 설정 (옵션)
                    )
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(0.dp), // 아이콘 간 간격을 0으로 설정
                        // verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 휴지통 아이콘 (오른쪽 상단에 배치)
                        IconButton(
                            onClick = { tripNoteDetailViewModel.deleteButtonClick() },
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            Icon(
                                // imageVector = Icons.Filled.Delete,
                                painter = painterResource(id = R.drawable.ic_delete_24px),
                                contentDescription = "Delete"
                            )
                        }

                        // 다운로드 아이콘 (오른쪽 상단에 배치)
                        IconButton(
                            onClick = { tripNoteDetailViewModel.bringTripNote() },
                            modifier = Modifier.padding(end = 3.dp)
                        ) {
                            Icon(
                                // imageVector = Icons.Filled.Download,
                                painter = painterResource(id = R.drawable.ic_download_24px),
                                contentDescription = "Download"
                            )
                        }
                    }
                }
            )
        },
        content = {paddingValues ->
            // LazyColumn을 사용하여 스크롤 가능하게 처리
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                // 이미지 슬라이더
                if (images.isNotEmpty()) {
                    item {
                        HorizontalPager(
                            state = pagerState,
                            // pageCount = images.size,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) { page ->
                            Image(
                                painter = rememberImagePainter(images[page]),
                                contentDescription = "Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 여행기 제목
                tripNoteDetailData?.tripNoteTitle?.let { title ->
                    item {
                        Text(
                            text = title,
                            fontSize = 27.sp,
                            fontFamily = NanumSquareRound,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // 여행기 내용
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = tripNoteDetailData?.tripNoteContent ?: "",
                            fontSize = 15.sp,
                            fontFamily = NanumSquareRoundRegular
                        )
                    }
                }

                // 사용자 닉네임과 아이콘 버튼
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = tripNoteDetailData?.userNickname ?: "닉네임 없음",
                            fontSize = 13.sp,
                            fontFamily = NanumSquareRound,
                            // modifier = Modifier.padding(end = 1.dp)
                        )
                        IconButton(onClick = { tripNoteDetailViewModel.clickNickname() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_forward_ios_24px),
                                contentDescription = "Next",
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                }

                // 가로선 추가
                item {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 11.dp), // 좌우 여백 설정
                        thickness = 1.dp // 선의 두께
                    )
                }

            }
        }
    )
}