package com.l4kt.voicelink.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RecordButton(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    rmsDb: Float = 0f,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animation for the button size based on sound input
    val scale by animateFloatAsState(
        targetValue = if (isRecording) {
            // Map RMS (typically -10 to +10 dB) to a scale factor (1.0 to 1.3)
            val normalizedRms = (rmsDb + 10f) / 20f  // Normalize to 0-1 range
            val scaleBoost = normalizedRms.coerceIn(0f, 1f) * 0.3f
            1.0f + scaleBoost
        } else {
            1.0f
        },
        label = "RecordButtonScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(72.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .border(2.dp, Color.White, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = onClick
            )
    ) {
        AnimatedContent(
            targetState = isRecording,
            label = "RecordButtonIcon"
        ) { recording ->
            Icon(
                imageVector = if (recording) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (recording) "Stop recording" else "Start recording",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}