package com.l4kt.voicelink.util

import com.l4kt.voicelink.domain.model.TaskCategory
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskAnalyzer @Inject constructor() {

    /**
     * Analyzes the transcribed text and extracts task information
     */
    fun analyzeTranscription(transcription: String): TaskAnalysisResult {
        val title = extractTitle(transcription)
        val category = extractCategory(transcription)
        val priority = extractPriority(transcription)
        val dueDate = extractDueDate(transcription)

        return TaskAnalysisResult(
            title = title,
            category = category.displayName,
            priority = priority,
            dueDate = dueDate
        )
    }

    private fun extractTitle(transcription: String): String {
        // Extract the first sentence as the title
        val sentencePattern = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)")
        val matcher = sentencePattern.matcher(transcription)

        return if (matcher.find()) {
            // Limit title length
            val sentence = matcher.group()
            if (sentence.length > 50) sentence.substring(0, 47) + "..." else sentence
        } else {
            // If no sentence is found, use the first 50 chars
            if (transcription.length > 50) transcription.substring(0, 47) + "..." else transcription
        }
    }

    private fun extractCategory(transcription: String): TaskCategory {
        return TaskCategory.fromText(transcription)
    }

    private fun extractPriority(transcription: String): Int {
        val lowPriorityPatterns = listOf("low priority", "not urgent", "when you have time", "no rush")
        val highPriorityPatterns = listOf("high priority", "urgent", "asap", "as soon as possible", "important")

        val lowerText = transcription.lowercase(Locale.getDefault())

        return when {
            highPriorityPatterns.any { lowerText.contains(it) } -> 3 // High
            lowPriorityPatterns.any { lowerText.contains(it) } -> 1 // Low
            else -> 2 // Medium (default)
        }
    }

    private fun extractDueDate(transcription: String): Date? {
        val lowerText = transcription.lowercase(Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Today
        if (lowerText.contains("today")) {
            return calendar.time
        }

        // Tomorrow
        if (lowerText.contains("tomorrow")) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            return calendar.time
        }

        // Next week
        if (lowerText.contains("next week")) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            return calendar.time
        }

        // Day of week
        val daysOfWeek = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        for ((index, day) in daysOfWeek.withIndex()) {
            if (lowerText.contains(day)) {
                val today = calendar.get(Calendar.DAY_OF_WEEK)
                val targetDay = index + 2 // Calendar.MONDAY = 2, etc.
                var daysToAdd = (targetDay - today)

                if (daysToAdd <= 0) {
                    daysToAdd += 7 // Next week
                }

                calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
                return calendar.time
            }
        }

        // Specific date format (e.g., "on January 15")
        val monthPattern = "\\b(january|february|march|april|may|june|july|august|september|october|november|december)\\s+(\\d{1,2})\\b"
        val monthMatcher = Pattern.compile(monthPattern, Pattern.CASE_INSENSITIVE).matcher(lowerText)

        if (monthMatcher.find()) {
            val month = monthMatcher.group(1)
            val day = monthMatcher.group(2)!!.toInt()

            val monthIndex = listOf(
                "january", "february", "march", "april", "may", "june",
                "july", "august", "september", "october", "november", "december"
            ).indexOf(month!!.lowercase(Locale.getDefault()))

            if (monthIndex != -1) {
                calendar.set(Calendar.MONTH, monthIndex)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                // If the date has already passed this year, set it for next year
                if (calendar.time.before(Date())) {
                    calendar.add(Calendar.YEAR, 1)
                }

                return calendar.time
            }
        }

        // No due date found
        return null
    }
}

data class TaskAnalysisResult(
    val title: String,
    val category: String,
    val priority: Int,
    val dueDate: Date?
)