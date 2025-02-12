package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    // 상단 헤더 Composable
    headerComposable: @Composable () -> Unit = {},
    // 메뉴 항목들
    drawerItems : List<@Composable () -> Unit> = listOf(),
    // 보여줄 화면들
    screens : List<@Composable () -> Unit> = listOf(),
    // 네비게이션을 통제하는 상태
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    // 선택한 항목의 순서 값
    navigationItemSelectedPosition:MutableIntState = mutableIntStateOf(0),
) {
    // 접었다 폈다를 코루틴 내부에서 처리해야 하므로 코루틴을 받는다.
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    headerComposable()

                    drawerItems.forEach {
                        it()
                    }
                }
            }
        }
    ) {
        screens[navigationItemSelectedPosition.value]()
    }
}

@Composable
fun LikeLionDrawerItem(
    imageVector:ImageVector? = null,
    label:String = "",
    selectedStateValue:MutableState<Boolean> = mutableStateOf(false),
    onClick:() -> Unit = {},
    drawerState: DrawerState? = null,
){
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        icon = if(imageVector != null) {
            {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null
                )
            }
        } else{
            null
        },
        label = {
            Text(text = label)
        },
        selected = selectedStateValue.value,
        onClick = {
            if(drawerState != null){
                scope.launch {
                    drawerState.close()
                }
            }

            onClick()

            selectedStateValue.value = true
        }
    )
}