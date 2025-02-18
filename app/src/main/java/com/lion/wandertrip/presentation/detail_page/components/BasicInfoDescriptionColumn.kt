package com.lion.wandertrip.presentation.detail_page.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lion.wandertrip.presentation.detail_page.DetailViewModel
import com.lion.wandertrip.util.CustomFont

@Composable
fun BasicInfoDescriptionColumn(detailViewModel : DetailViewModel) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                "주소 : ",
                fontFamily = CustomFont.customFontBold,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                detailViewModel.contentModelValue.value.detailAddress,
                fontFamily = CustomFont.customFontRegular
            )
        }
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                "전화 : ",
                fontFamily = CustomFont.customFontBold,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                detailViewModel.contentModelValue.value.detailPhoneNumber,
                fontFamily = CustomFont.customFontRegular,
                modifier = Modifier.clickable {
                    detailViewModel.onClickTextTel(detailViewModel.contentModelValue.value.detailPhoneNumber)
                }
            )
        }
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                "홈페이지 : ",
                fontFamily = CustomFont.customFontBold,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                detailViewModel.contentModelValue.value.detailHomepage,
                fontFamily = CustomFont.customFontRegular,
                modifier = Modifier.clickable {
                    detailViewModel.onClickTextHomepage(detailViewModel.contentModelValue.value.detailHomepage)
                })
        }
    }


}