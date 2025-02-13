package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier


@Composable
fun CustomListComponent(
    dataList: SnapshotStateList<Map<String, *>>,
    rowComposable: @Composable (Map<String, *>) -> Unit,
    onRowClick:() -> Unit = {},
) {
    LazyColumn {
        items(dataList){ dataMap ->
            Column(
                modifier = Modifier.fillMaxWidth().clickable {
                    onRowClick()
                }
            ) {
                rowComposable(dataMap)
            }
        }
    }
}

