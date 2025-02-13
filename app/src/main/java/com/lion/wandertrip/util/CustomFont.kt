package com.lion.wandertrip.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.lion.wandertrip.R

class CustomFont {
    companion object{
        // res/font/nanum_square_roun_bold.ttf 파일을 사용하기 위한 설정
        val customFontLight = FontFamily(
            Font(R.font.nanum_square_roun_light)
        )

        // res/font/nanum_square_roun_bold.ttf 파일을 사용하기 위한 설정
        val customFontBold = FontFamily(
            Font(R.font.nanum_square_roun_bold)
        )

        // res/font/nanum_square_roun_bold.ttf 파일을 사용하기 위한 설정
        val customFontRegular = FontFamily(
            Font(R.font.nanum_square_roun_regular)
        )

    }
}