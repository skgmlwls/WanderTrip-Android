package com.lion.wandertrip.component

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
@Composable
fun CustomDraggableRatingBar(
    maxRating: Int = 5,
    ratingState: MutableState<Float>, // 별점 상태를 직접 관리할 수 있도록 추가
    onRatingChanged: (Float) -> Unit
) {
    val starSize = 50.dp // 별의 크기
    val starPadding = 4.dp // 별 간의 간격

    // 별을 5개로 설정
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(vertical = 20.dp, horizontal = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // 별 간의 간격을 일정하게 배치
        ) {
            // 각 별을 표시
            for (i in 1..maxRating) {
                Icon(
                    imageVector = if (i <= ratingState.value) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(starSize)
                        .pointerInput(Unit) {
                            // 클릭 이벤트
                            detectTapGestures { offset ->
                                val newRating = (offset.x / (starSize.toPx() + starPadding.toPx()))
                                    .coerceIn(0f, maxRating.toFloat())
                                ratingState.value = newRating
                                onRatingChanged(newRating)
                            }
                        }
                        .pointerInput(Unit) {
                            // 드래그 이벤트
                            detectHorizontalDragGestures { _, dragAmount ->
                                val newRating = (ratingState.value + (dragAmount / (starSize.toPx() + starPadding.toPx())))
                                    .coerceIn(0f, maxRating.toFloat())
                                ratingState.value = newRating
                                onRatingChanged(newRating)
                            }
                        }
                )
            }
        }
    }
}
