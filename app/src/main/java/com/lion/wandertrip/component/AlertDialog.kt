package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AlertDialog(
    // 다이얼로그를 보여주는 상태
    showDialogState : MutableState<Boolean>,
    confirmButtonTitle : String = "확인",
    confirmButtonOnClick : () -> Unit = {
        showDialogState.value = false
    },
    dismissButtonTitle : String? = null,
    dismissButtonOnClick : () -> Unit = {
        showDialogState.value = false
    },
    icon : ImageVector? = null,
    title : String? = null,
    text : String? = null,
) {
    if(showDialogState.value){
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            // 다이얼로그가 닫히는 사건이 발생했을 때
            onDismissRequest = {
                showDialogState.value = false
            },
            // 확인 버튼
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmButtonOnClick()
                    }
                ) {
                    Text(text = confirmButtonTitle)
                }
            },
            // 취소 버튼
            dismissButton = if(dismissButtonTitle != null){
                {
                    TextButton(
                        onClick = {
                            dismissButtonOnClick()
                        }
                    ) {
                        Text(text = dismissButtonTitle)
                    }
                }
            } else {
                null
            },
            icon = if(icon != null){
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            } else {
                null
            },
            title = if(title != null){
                {
                    Text(
                        text= title,
                    )
                }
            } else {
                null
            },
            text = if(text != null){
                {
                    Text(text = text)
                }
            } else {
                null
            },

        )
    }
}