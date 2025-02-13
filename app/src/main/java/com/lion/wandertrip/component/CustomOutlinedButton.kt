package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedButton(
    text:String = "OutlinedButton",
    paddingTop:Dp = 0.dp,
    onClick:() -> Unit = {}
) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth().padding(top = paddingTop),
        onClick = onClick
    ) {
        Text(text = text)
    }
}