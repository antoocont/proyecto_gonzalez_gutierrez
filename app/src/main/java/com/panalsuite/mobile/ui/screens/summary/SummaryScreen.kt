package com.panalsuite.mobile.ui.screens.summary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panalsuite.mobile.R
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.ui.components.PriorityLabel
import com.panalsuite.mobile.ui.theme.Amber
import com.panalsuite.mobile.ui.theme.Slate
import com.panalsuite.mobile.ui.theme.SlateLight
import com.panalsuite.mobile.ui.theme.Teal
import com.panalsuite.mobile.ui.theme.TealLight

private val PulseRed = Color(0xFFC62828)

@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Reveal(delayMillis = 0) { KpiRow(state) }
        Reveal(delayMillis = 120) { StatusCard(state) }
        Reveal(delayMillis = 240) { PriorityCard(state) }
        Reveal(delayMillis = 360) { WorkloadCard(state) }
    }
}

@Composable
private fun rememberStarted(): Boolean {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    return started
}

@Composable
private fun Reveal(delayMillis: Int, content: @Composable () -> Unit) {
    val started = rememberStarted()
    AnimatedVisibility(
        visible = started,
        enter = fadeIn(tween(400, delayMillis)) +
                slideInVertically(tween(400, delayMillis)) { it / 5 }
    ) {
        content()
    }
}

@Composable
private fun KpiRow(state: SummaryUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        KpiCard(
            label = stringResource(R.string.summary_total),
            value = state.total,
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            label = stringResource(R.string.ticket_unassigned),
            value = state.unassigned,
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            label = stringResource(R.string.summary_critical),
            value = state.criticalActive,
            modifier = Modifier.weight(1f),
            pulse = state.criticalActive > 0
        )
    }
}

@Composable
private fun KpiCard(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    pulse: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedCount(
                    value = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (pulse) {
                    Spacer(Modifier.width(8.dp))
                    PulseDot()
                }
            }
            Text(
                text = label,
                minLines = 2,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusCard(state: SummaryUiState) {
    SectionCard(title = stringResource(R.string.summary_by_status)) {
        val maxCount = (state.statusCounts.values.maxOrNull() ?: 0).coerceAtLeast(1)
        TicketStatus.entries.forEach { status ->
            val count = state.statusCounts[status] ?: 0
            StatusBarRow(
                status = status,
                count = count,
                fraction = count / maxCount.toFloat()
            )
        }
    }
}

@Composable
private fun StatusBarRow(status: TicketStatus, count: Int, fraction: Float) {
    val started = rememberStarted()
    val animatedFraction by animateFloatAsState(
        targetValue = if (started) fraction else 0f,
        animationSpec = tween(800),
        label = "statusBar"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = status.label,
            modifier = Modifier.width(88.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction)
                    .fillMaxHeight()
                    .background(status.barColor(), RoundedCornerShape(50))
            )
        }
        Text(
            text = count.toString(),
            modifier = Modifier
                .padding(start = 12.dp)
                .width(24.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun PriorityCard(state: SummaryUiState) {
    SectionCard(title = stringResource(R.string.summary_by_priority)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Priority.entries.forEach { priority ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedCount(
                        value = state.priorityCounts[priority] ?: 0,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    PriorityLabel(priority = priority)
                }
            }
        }
    }
}

@Composable
private fun WorkloadCard(state: SummaryUiState) {
    SectionCard(title = stringResource(R.string.summary_workload)) {
        state.workload.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.team,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = stringResource(R.string.summary_active_count, item.active),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun AnimatedCount(value: Int, style: TextStyle, color: Color) {
    val started = rememberStarted()
    val shown by animateIntAsState(
        targetValue = if (started) value else 0,
        animationSpec = tween(900),
        label = "count"
    )
    Text(text = shown.toString(), style = style, color = color)
}

@Composable
private fun PulseDot() {
    val transition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .alpha(pulseAlpha)
            .background(PulseRed, CircleShape)
    )
}

private fun TicketStatus.barColor(): Color = when (this) {
    TicketStatus.OPEN -> Amber
    TicketStatus.IN_PROGRESS -> Teal
    TicketStatus.ON_HOLD -> Slate
    TicketStatus.RESOLVED -> TealLight
    TicketStatus.CLOSED -> SlateLight
}