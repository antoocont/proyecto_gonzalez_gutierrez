package com.panalsuite.mobile.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.panalsuite.mobile.R
import com.panalsuite.mobile.data.local.entity.UserEntity
import com.panalsuite.mobile.ui.components.PanalLogo
import com.panalsuite.mobile.ui.theme.Amber
import com.panalsuite.mobile.ui.theme.Navy
import com.panalsuite.mobile.ui.theme.NavyLight

@Composable
fun HomePlaceholderScreen(
    user: UserEntity,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy, NavyLight)))
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PanalLogo()
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.home_greeting, user.fullName),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = user.role.label,
            style = MaterialTheme.typography.titleMedium,
            color = Amber
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_session_note),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        OutlinedButton(
            onClick = onLogout,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
        ) {
            Text(stringResource(R.string.home_logout))
        }
    }
}