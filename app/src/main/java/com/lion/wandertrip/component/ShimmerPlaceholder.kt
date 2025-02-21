package com.lion.wandertrip.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .shimmer() // valentinilk.shimmer 의 확장 함수
    )
}