package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSegmentedButton(
    // 버튼에 표시할 문자열
    labelList:List<String>,
    // 선택한 버튼의 순서값
    selectedIndex:MutableIntState = mutableIntStateOf(0)
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        labelList.forEachIndexed { index, s ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = labelList.size
                ),
                onClick = {
                    selectedIndex.intValue = index
                },
                selected = index == selectedIndex.intValue,
                label = {
                    Text(text = s)
                },
            )
        }
    }
}