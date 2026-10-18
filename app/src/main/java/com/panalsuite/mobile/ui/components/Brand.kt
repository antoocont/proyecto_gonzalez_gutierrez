package com.panalsuite.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.unit.dp
import com.panalsuite.mobile.ui.theme.Amber
import com.panalsuite.mobile.ui.theme.Navy

val HexagonShape = GenericShape { size, _ ->
    moveTo(size.width * 0.25f, 0f)
    lineTo(size.width * 0.75f, 0f)
    lineTo(size.width, size.height / 2f)
    lineTo(size.width * 0.75f, size.height)
    lineTo(size.width * 0.25f, size.height)
    lineTo(0f, size.height / 2f)
    close()
}

@Composable
fun PanalLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 96.dp, height = 84.dp)
            .background(Amber, HexagonShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.ConfirmationNumber,
            contentDescription = null,
            tint = Navy,
            modifier = Modifier.size(38.dp)
        )
    }
}