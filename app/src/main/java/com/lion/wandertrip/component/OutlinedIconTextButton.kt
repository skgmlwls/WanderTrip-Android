package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedIconTextButton(
    buttonOnClick:() -> Unit = {},
    leadingIcon:ImageVector? = null,
    buttonTitle:String = "",
    buttonTitleAlignment:TextAlign = TextAlign.Center,
) {
    OutlinedButton(
        onClick = buttonOnClick
    ) {
        if(leadingIcon != null) {
            Icon(imageVector = leadingIcon, contentDescription = null)
            Spacer(modifier = Modifier.padding(end = 10.dp))
        }
        Text(
            modifier = Modifier.weight(1.0f),
            text = buttonTitle,
            textAlign = buttonTitleAlignment,
        )
    }
}