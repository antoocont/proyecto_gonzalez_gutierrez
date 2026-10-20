package com.panalsuite.mobile.ui.screens.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panalsuite.mobile.R
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.ui.components.PriorityLabel
import com.panalsuite.mobile.ui.components.StatusChip
import com.panalsuite.mobile.util.TimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketListViewModel,
    onTicketClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val chipColors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
        selectedLabelColor = MaterialTheme.colorScheme.onTertiary
    )

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "all") {
                FilterChip(
                    selected = state.statusFilter == null,
                    onClick = { viewModel.onStatusFilterChange(null) },
                    label = { Text(stringResource(R.string.tickets_filter_all)) },
                    colors = chipColors
                )
            }
            items(TicketStatus.entries, key = { it.name }) { status ->
                FilterChip(
                    selected = state.statusFilter == status,
                    onClick = { viewModel.onStatusFilterChange(status) },
                    label = { Text(status.label) },
                    colors = chipColors
                )
            }
        }
        Text(
            text = stringResource(R.string.tickets_results, state.tickets.size),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (state.tickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.tickets_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.tickets, key = { it.id }) { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onClick = { onTicketClick(ticket.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TicketCard(
    ticket: TicketEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${ticket.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                PriorityLabel(priority = ticket.priority)
            }
            Text(
                text = ticket.title,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${ticket.branch} · ${ticket.category.label}",
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = ticket.status)
                Text(
                    text = TimeFormat.relative(ticket.updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}