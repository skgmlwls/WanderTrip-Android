package com.lion.wandertrip.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationData(
    val title: String,
    @DrawableRes val iconRes: Int
)