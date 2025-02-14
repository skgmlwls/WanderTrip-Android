package com.lion.wandertrip.presentation.bottom.trip_note_page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.lion.a02_boardcloneproject.component.CustomTopAppBar
import com.lion.wandertrip.model.TripNoteModel
import com.lion.wandertrip.ui.theme.NanumSquareRound
import kotlinx.coroutines.launch

@Composable
fun TripNoteScreen(
    tripNoteViewModel: TripNoteViewModel = hiltViewModel()
) {

    // 리사이클러뷰 항목 데이터 초기화
    tripNoteViewModel.gettingTripNoteData()

    tripNoteViewModel.topAppBarTitle.value = "추천 여행기"


    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = tripNoteViewModel.topAppBarTitle.value,
            )
        },

        // fab 버튼
        floatingActionButton = {
            FloatingActionButton(
                onClick = { tripNoteViewModel.addButtonOnClick() },
                // modifier = Modifier.padding(bottom = 0.dp),
                modifier = Modifier
                    .padding(top = 160.dp)
                    .absoluteOffset(y = 49.dp),
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "+")
                }
            )
        }
    ) { padding ->
        // 리사이클러뷰
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(tripNoteViewModel.tripNoteList) { tripNote ->
                TripNoteItem(tripNote = tripNote,
                    onClick = { tripNoteViewModel.listItemOnClick() })
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TripNoteItem(tripNote: TripNoteModel,
                 onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        // 제목 (굵은 글씨)
        Text(
            text = tripNote.tripNoteTitle,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = NanumSquareRound,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 닉네임 (작은 글씨)
        Text(
            text = tripNote.userNickname,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontFamily = NanumSquareRound,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 이미지 리스트
        if (tripNote.tripNoteImage.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start) // Row를 왼쪽 정렬로 설정
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 이미지 간 간격 유지

            ) {
                tripNote.tripNoteImage.forEach { imageUrl ->
                    GlideImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .width(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        // 내용
        Text(
            text = tripNote.tripNoteContent,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = NanumSquareRound,
        )
    }
}

