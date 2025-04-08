package com.l4kt.voicelink.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.l4kt.voicelink.data.database.entity.Task
import com.l4kt.voicelink.ui.theme.HighPriority
import com.l4kt.voicelink.ui.theme.LowPriority
import com.l4kt.voicelink.ui.theme.MediumPriority
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onTaskComplete: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedAlpha = if (task.isCompleted) 0.6f else 1.0f
    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    var showMenu by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (task.isCompleted)
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "TaskBackgroundColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority indicator
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        getPriorityColor(task.priority),
                        CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Task content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(completedAlpha)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = textDecoration,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Category chip
                CategoryChip(
                    categoryName = task.category,
                    modifier = Modifier.alpha(completedAlpha)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Due date if present
                task.dueDate?.let { date ->
                    val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    Text(
                        text = "Due: ${formatter.format(date)}",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = textDecoration
                    )
                }
            }

            // Checkbox and menu
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskComplete(task) }
            )

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options"
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        },
                        onClick = {
                            onTaskDelete(task)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun getPriorityColor(priority: Int): Color {
    return when (priority) {
        1 -> LowPriority
        2 -> MediumPriority
        3 -> HighPriority
        else -> MediumPriority
    }
}