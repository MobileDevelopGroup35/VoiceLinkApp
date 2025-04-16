package com.l4kt.voicelink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.l4kt.voicelink.domain.model.TaskCategory

@Composable
fun CategoryChip(
    categoryName: String,
    modifier: Modifier = Modifier
) {
    // Find the matching category or use OTHER as fallback
    val category = TaskCategory.entries.find { it.displayName == categoryName } ?: TaskCategory.OTHER

    // Get color based on category
    val color = getCategoryColor(category)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(end = 4.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = categoryName,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}

@Composable
private fun getCategoryColor(category: TaskCategory): Color {
    return when (category) {
        TaskCategory.WORK -> MaterialTheme.colorScheme.primary
        TaskCategory.PERSONAL -> Color(0xFF9C27B0)
        TaskCategory.SHOPPING -> Color(0xFF4CAF50)
        TaskCategory.HEALTH -> Color(0xFFE91E63)
        TaskCategory.LEARNING -> Color(0xFF2196F3)
        TaskCategory.FAMILY -> Color(0xFFFF9800)
        TaskCategory.TRAVEL -> Color(0xFF00BCD4)
        TaskCategory.OTHER -> Color.Gray
    }
}