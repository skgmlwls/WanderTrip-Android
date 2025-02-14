package com.lion.a02_boardcloneproject.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.lion.wandertrip.model.TripScheduleModel
import com.lion.wandertrip.presentation.bottom.schedule_page.component.ScheduleItemList

@Composable
fun CustomIconButton(
    icon:ImageVector,
    iconButtonOnClick : () -> Unit = {},
) {
    IconButton(
        onClick = {
            iconButtonOnClick()
        }
    ){
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

// 미리 보기
@Preview(showBackground = true)
@Composable
fun PreviewCustomIconButton() {


    CustomIconButton(icon = Icons.Filled.MoreVert)
}