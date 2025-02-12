package com.lion.wandertrip.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BlueButton(
    text: String = "Button",
    paddingTop: Dp = 0.dp,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp), // 모서리를 둥글게 만듦
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0077B6), // 버튼 배경색
            contentColor = Color.White // 텍스트 색상
        )
    ) {
        Text(text = text)
    }
}