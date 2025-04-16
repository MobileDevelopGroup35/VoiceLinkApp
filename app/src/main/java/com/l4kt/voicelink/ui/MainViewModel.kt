package com.l4kt.voicelink.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l4kt.voicelink.data.database.entity.Task
import com.l4kt.voicelink.data.repository.TaskRepository
import com.l4kt.voicelink.util.SpeechRecognitionState
import com.l4kt.voicelink.util.SpeechRecognizer
import com.l4kt.voicelink.util.TaskAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val speechRecognizer: SpeechRecognizer,
    private val taskAnalyzer: TaskAnalyzer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        checkForFirstRun()
        loadTasks()
    }

    private fun checkForFirstRun() {
        val prefs = context.getSharedPreferences("voicelink_prefs", Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("is_first_launch", true)

        if (isFirstRun) {
            // Clear all tasks on first run
            viewModelScope.launch {
                val tasks = taskRepository.getAllTasks().first()
                for (task in tasks) {
                    taskRepository.deleteTask(task)
                }
                // Mark as no longer first run
                prefs.edit().putBoolean("is_first_launch", false).apply()
            }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collectLatest { taskList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        tasks = taskList,
                        upcomingTasks = taskList.filter { task ->
                            task.dueDate != null && !task.isCompleted
                        }.sortedBy { it.dueDate }
                    )
                }
            }
        }
    }

    fun toggleRecording() {
        if (_uiState.value.isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        viewModelScope.launch {
            _uiState.update { it.copy(
                isRecording = true,
                transcribedText = "",
                recordingError = null
            ) }

            speechRecognizer.listen().collect { state ->
                when (state) {
                    is SpeechRecognitionState.RmsChanged -> {
                        _uiState.update { it.copy(currentRmsDb = state.rmsdB) }
                    }
                    is SpeechRecognitionState.PartialResult -> {
                        _uiState.update { it.copy(transcribedText = state.text) }
                    }
                    is SpeechRecognitionState.Result -> {
                        _uiState.update { it.copy(
                            transcribedText = state.text,
                            isRecording = false
                        ) }
                        processTranscription(state.text)
                    }
                    is SpeechRecognitionState.Error -> {
                        _uiState.update { it.copy(
                            isRecording = false,
                            recordingError = state.message
                        ) }
                    }
                    else -> { /* Other states not handled */ }
                }
            }
        }
    }

    private fun stopRecording() {
        speechRecognizer.stopListening()
        _uiState.update { it.copy(isRecording = false) }
    }

    private fun processTranscription(text: String) {
        viewModelScope.launch {
            val analysis = taskAnalyzer.analyzeTranscription(text)

            val newTask = Task(
                title = analysis.title,
                transcription = text,
                category = analysis.category,
                priority = analysis.priority,
                dueDate = analysis.dueDate,
                createdAt = Date()
            )

            val taskId = taskRepository.insertTask(newTask)

            _uiState.update { it.copy(
                lastAddedTaskId = taskId,
                showTaskAddedMessage = true
            ) }

            // Reset message after a delay
            kotlinx.coroutines.delay(3000)
            _uiState.update { it.copy(showTaskAddedMessage = false) }
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            taskRepository.toggleTaskCompletion(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }


    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
    }
}

data class MainUiState(
    val tasks: List<Task> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val isRecording: Boolean = false,
    val currentRmsDb: Float = 0f,
    val transcribedText: String = "",
    val recordingError: String? = null,
    val lastAddedTaskId: Long = -1,
    val showTaskAddedMessage: Boolean = false
)