package com.lion.wandertrip.presentation.user_login_page.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lion.wandertrip.R

@Composable
fun KakaoButton(

    paddingTop: Dp = 0.dp,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .padding(top = paddingTop)
            .clickable { onClick() }
            .height(50.dp) // 버튼 높이 조정
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_kakao_login), // img_kakao_login.png 이미지를 사용
            contentDescription = "Kakao Login Button",
            modifier = Modifier.fillMaxSize() // 이미지를 전체 크기에 맞게 채우기
        )
    }
}
