package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title:String = "",
    navigationIconImage:ImageVector? = null,
    navigationIconOnClick:() -> Unit = {},
    menuItems : @Composable RowScope.() -> Unit = {},
){
    TopAppBar(
        // 타이틀
        title = {
            Text(text = title)
        },
        // 네비게이션 아이콘
        navigationIcon = if(navigationIconImage == null){
            {}
        } else {
            {
                IconButton(
                    onClick = navigationIconOnClick
                ) {
                    Icon(
                        imageVector = navigationIconImage,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            menuItems()
        },
    )
}