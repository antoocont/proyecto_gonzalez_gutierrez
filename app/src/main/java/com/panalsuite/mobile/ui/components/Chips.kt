package com.panalsuite.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.panalsuite.mobile.domain.model.Priority
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.ui.theme.Amber
import com.panalsuite.mobile.ui.theme.Navy
import com.panalsuite.mobile.ui.theme.NavyLight
import com.panalsuite.mobile.ui.theme.Slate
import com.panalsuite.mobile.ui.theme.SlateLight
import com.panalsuite.mobile.ui.theme.Teal
import com.panalsuite.mobile.ui.theme.TealLight

private val CriticalRed = Color(0xFFC62828)

@Composable
fun StatusChip(status: TicketStatus, modifier: Modifier = Modifier) {
    val (container, content) = when (status) {
        TicketStatus.OPEN -> Amber to Navy
        TicketStatus.IN_PROGRESS -> Teal to Color.White
        TicketStatus.ON_HOLD -> Slate to Color.White
        TicketStatus.RESOLVED -> TealLight to Navy
        TicketStatus.CLOSED -> NavyLight to Color.White
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = container
    ) {
        Text(
            text = status.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = content
        )
    }
}

@Composable
fun PriorityLabel(priority: Priority, modifier: Modifier = Modifier) {
    val dotColor = when (priority) {
        Priority.LOW -> SlateLight
        Priority.MEDIUM -> Teal
        Priority.HIGH -> Amber
        Priority.CRITICAL -> CriticalRed
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = priority.label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}