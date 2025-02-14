package com.lion.a02_boardcloneproject.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale


@Composable
fun CustomImageBitmap(
    imageBitmap: ImageBitmap,
    contentScale: ContentScale = ContentScale.Fit
) {
    Image(
        modifier = Modifier.fillMaxWidth(),
        bitmap = imageBitmap,
        contentDescription = null,
        contentScale = contentScale,
    )
}