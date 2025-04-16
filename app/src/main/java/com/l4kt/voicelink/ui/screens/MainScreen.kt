package com.l4kt.voicelink.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.l4kt.voicelink.ui.MainViewModel
import com.l4kt.voicelink.ui.components.RecordButton
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    // Animation for the mic icon when recording
    val waveScale by animateFloatAsState(
        targetValue = if (uiState.isRecording) {
            val normalizedRms = (uiState.currentRmsDb + 10f) / 20f
            val scaleBoost = normalizedRms.coerceIn(0f, 1f) * 0.5f
            1.0f + scaleBoost
        } else {
            1.0f
        },
        label = "WaveScaleAnimation"
    )

    // Show snackbar when a task is added
    LaunchedEffect(uiState.showTaskAddedMessage) {
        if (uiState.showTaskAddedMessage) {
            snackbarHostState.showSnackbar("Task added successfully!")
        }
    }

    // Show error if speech recognition fails
    LaunchedEffect(uiState.recordingError) {
        uiState.recordingError?.let { error ->
            snackbarHostState.showSnackbar("Error: $error")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("VoiceLink")
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isRecording) {
                // Recording UI
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 72.dp)
                ) {
                    // Animated mic icon
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(120.dp)
                            .scale(waveScale)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Listening...",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.transcribedText.ifEmpty { "Speak now..." },
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                // Task list
                TaskListScreen(
                    tasks = uiState.tasks,
                    onTaskClick = { /* Task detail implementation would go here */ },
                    onTaskComplete = { viewModel.completeTask(it) },
                    onTaskDelete = { viewModel.deleteTask(it) }
                )
            }

            // Record button (always visible at the bottom)
            RecordButton(
                isRecording = uiState.isRecording,
                rmsDb = uiState.currentRmsDb,
                onClick = { viewModel.toggleRecording() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )
        }
    }
}