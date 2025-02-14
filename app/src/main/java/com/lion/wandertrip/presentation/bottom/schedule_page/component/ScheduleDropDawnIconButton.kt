package com.lion.wandertrip.presentation.bottom.schedule_page.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lion.wandertrip.ui.theme.NanumSquareRound
import com.lion.wandertrip.ui.theme.NanumSquareRoundRegular

@Composable
fun ScheduleDropDawnIconButton(
    icon: ImageVector,
    size: Int,
    menuItems: List<String> = listOf("편집", "삭제"),
    onMenuItemClick: (String) -> Unit = {}
) {
    // 메뉴 확장 상태
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true }, // 버튼 클릭 시 메뉴 표시
            modifier = Modifier.size(size.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(12.dp),
            containerColor = Color.White,
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontFamily = NanumSquareRoundRegular) },
                    onClick = {
                        expanded = false
                        onMenuItemClick(item) // 선택한 메뉴 아이템 클릭 이벤트
                    }
                )
            }
        }
    }
}

// 📌 미리보기 추가
@Preview(showBackground = true)
@Composable
fun PreviewScheduleDropDawnIconButton() {
    ScheduleDropDawnIconButton(
        icon = Icons.Filled.MoreVert,
        size = 24
    ) { selectedItem ->
        println("선택된 메뉴: $selectedItem") // ✅ 클릭한 메뉴 아이템 확인
    }
}