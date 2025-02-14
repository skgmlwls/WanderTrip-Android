package com.lion.wandertrip.presentation.bottom.schedule_page.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.ui.theme.NanumSquareRound


@Composable
fun ScheduleIconButton(
    icon: ImageVector,
    size: Int,
    iconButtonOnClick : () -> Unit = {},
) {
    IconButton(
        onClick = {
            iconButtonOnClick()
        },
        Modifier.size(size.dp)
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

// 미리 보기
@Preview(showBackground = true)
@Composable
fun PreviewCustomIconButton() {
    ScheduleIconButton(icon = Icons.Filled.MoreVert, 15)
}
