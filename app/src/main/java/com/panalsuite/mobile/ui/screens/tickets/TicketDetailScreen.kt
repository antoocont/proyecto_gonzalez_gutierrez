package com.panalsuite.mobile.ui.screens.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.panalsuite.mobile.R
import com.panalsuite.mobile.data.local.entity.TicketEntity
import com.panalsuite.mobile.domain.model.TicketStatus
import com.panalsuite.mobile.ui.components.HexagonShape
import com.panalsuite.mobile.ui.components.PriorityLabel
import com.panalsuite.mobile.ui.components.StatusChip
import com.panalsuite.mobile.ui.theme.Amber
import com.panalsuite.mobile.util.TimeFormat

@Composable
fun TicketDetailScreen(
    viewModel: TicketDetailViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val ticket = state.ticket

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    if (ticket == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.ticket_not_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "header") {
                TicketHeader(
                    ticket = ticket,
                    requesterName = state.requesterName,
                    assigneeName = state.assigneeName
                )
            }
            if (state.canManage) {
                item(key = "manage") {
                    ManageCard(
                        nextStatuses = state.nextStatuses,
                        canAssign = state.canAssign,
                        onStatusChange = viewModel::onStatusChange,
                        onAssignToMe = viewModel::onAssignToMe
                    )
                }
            }
            item(key = "comments-title") {
                SectionTitle(stringResource(R.string.ticket_section_comments))
            }
            if (state.comments.isEmpty()) {
                item(key = "comments-empty") {
                    Text(
                        text = stringResource(R.string.ticket_comments_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(state.comments, key = { "comment-${it.id}" }) { comment ->
                    CommentBubble(comment)
                }
            }
            item(key = "history-title") {
                SectionTitle(stringResource(R.string.ticket_section_history))
            }
            items(state.history, key = { "history-${it.id}" }) { entry ->
                HistoryRow(entry)
            }
        }
        CommentComposer(
            form = form,
            onTextChange = viewModel::onCommentChange,
            onSend = viewModel::onSendComment
        )
    }
}

@Composable
private fun TicketHeader(
    ticket: TicketEntity,
    requesterName: String,
    assigneeName: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = ticket.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = ticket.status)
                Spacer(Modifier.width(12.dp))
                PriorityLabel(priority = ticket.priority)
            }
            Text(
                text = ticket.description,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            MetaRow(stringResource(R.string.ticket_requester), requesterName)
            MetaRow(
                stringResource(R.string.ticket_assignee),
                assigneeName ?: stringResource(R.string.ticket_unassigned)
            )
            MetaRow(stringResource(R.string.ticket_branch), ticket.branch)
            MetaRow(stringResource(R.string.ticket_category), ticket.category.label)
            MetaRow(stringResource(R.string.ticket_created), TimeFormat.dateTime(ticket.createdAt))
            MetaRow(stringResource(R.string.ticket_updated), TimeFormat.dateTime(ticket.updatedAt))
        }
    }
}

@Composable
private fun MetaRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ManageCard(
    nextStatuses: List<TicketStatus>,
    canAssign: Boolean,
    onStatusChange: (TicketStatus) -> Unit,
    onAssignToMe: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (nextStatuses.isEmpty() && !canAssign) {
                Text(
                    text = stringResource(R.string.ticket_closed_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (nextStatuses.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.ticket_section_status),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    nextStatuses.forEach { next ->
                        OutlinedButton(
                            onClick = { onStatusChange(next) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.ticket_move_to, next.label))
                        }
                    }
                }
            }
            if (canAssign) {
                Button(
                    onClick = onAssignToMe,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.ticket_assign_me))
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun CommentBubble(comment: CommentItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = comment.authorRole?.let { "${comment.authorName} · ${it.label}" }
                        ?: comment.authorName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = TimeFormat.relative(comment.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = comment.message,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun HistoryRow(entry: HistoryItem) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(width = 12.dp, height = 10.dp)
                .background(Amber, HexagonShape)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${entry.authorName} · ${TimeFormat.dateTime(entry.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CommentComposer(
    form: CommentFormState,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                value = form.text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.comment_hint)) },
                isError = form.error != null,
                supportingText = {
                    form.error?.let { Text(stringResource(it)) }
                },
                maxLines = 3
            )
            Spacer(Modifier.width(4.dp))
            IconButton(
                onClick = onSend,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.comment_send),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}