package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TriStateCheckBox(
    stateValue:MutableState<ToggleableState> = mutableStateOf(ToggleableState.Off),
    onClick: () -> Unit = {},
    text: String = "",
    paddingTop:Dp = 0.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(top = paddingTop)
    ) {
        TriStateCheckbox(
            state = stateValue.value,
            onClick = {
                if(stateValue.value == ToggleableState.On){
                    stateValue.value = ToggleableState.Off
                } else {
                    stateValue.value = ToggleableState.On
                }

                onClick()
            },
        )
        Text(
            text = text,
            modifier = Modifier.clickable {
                if(stateValue.value == ToggleableState.On){
                    stateValue.value = ToggleableState.Off
                } else {
                    stateValue.value = ToggleableState.On
                }

                onClick()
            }
        )
    }
}