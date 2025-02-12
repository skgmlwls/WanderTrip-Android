package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSegmentedButton(
    // 버튼에 표시할 문자열
    labelList:List<String>,
    // 각 버튼의 체크 여부 값
    checkedList: SnapshotStateList<Boolean>,
) {
    MultiChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        labelList.forEachIndexed { index, s ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = labelList.size
                ),
                checked = checkedList[index],
                onCheckedChange = {
                    checkedList[index] = !checkedList[index]
                },
                label = {
                    Text(text = labelList[index])
                }
            )
        }
    }
}