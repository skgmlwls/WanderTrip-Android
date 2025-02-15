package com.lion.wandertrip.presentation.my_trip_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.lion.wandertrip.model.TripNoteModel

@Composable
fun TripNoteItem(tripNote: TripNoteModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            // 여행기 제목
            Text(
                text = tripNote.tripNoteTitle,
            )

            // 여행기 대표 이미지
            Image(
                painter = rememberImagePainter(tripNote.tripNoteImage.firstOrNull() ?: ""),
                contentDescription = "Trip Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            // 아이콘과 텍스트
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 좌측 아이콘들 (좋아요, 채팅)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Like")
                    Text(text = "${tripNote.tripNoteScrapCount}개", modifier = Modifier.padding(start = 8.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.ChatBubble, contentDescription = "Chat")
                    Text(text = "${tripNote.tripNoteScrapCount}개", modifier = Modifier.padding(start = 8.dp))
                }

                // 우측 날짜와 팝업 메뉴
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "24.02.02 11:30",
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = {
                        // 팝업 메뉴 클릭 시 행동 추가
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            }
        }
    }
}
