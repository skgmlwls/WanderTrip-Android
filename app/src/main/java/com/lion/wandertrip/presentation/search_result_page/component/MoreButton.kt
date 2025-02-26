package com.lion.wandertrip.presentation.search_result_page.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lion.wandertrip.ui.theme.NanumSquareRound

@Composable
fun MoreButton(category: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* TODO: 더보기 기능 구현 */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$category 검색결과 더보기",
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = NanumSquareRound
        )
    }
}