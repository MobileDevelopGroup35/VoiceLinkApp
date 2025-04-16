package com.l4kt.voicelink.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class TaskCategory(
    val displayName: String,
    val icon: ImageVector,
    val keywords: List<String>
) {
    WORK(
        "Work",
        Icons.Default.Work,
        listOf("work", "job", "office", "project", "meeting", "client", "deadline", "presentation")
    ),
    PERSONAL(
        "Personal",
        Icons.Default.Person,
        listOf("personal", "self", "me", "diary", "journal", "reflection", "goal")
    ),
    SHOPPING(
        "Shopping",
        Icons.Default.ShoppingCart,
        listOf("buy", "purchase", "shop", "store", "mall", "grocery", "supermarket", "market")
    ),
    HEALTH(
        "Health",
        Icons.Default.Healing,
        listOf("health", "doctor", "medication", "medicine", "exercise", "workout", "fitness", "gym")
    ),
    LEARNING(
        "Learning",
        Icons.Default.School,
        listOf("learn", "study", "book", "course", "class", "lesson", "tutorial", "education")
    ),
    FAMILY(
        "Family",
        Icons.Default.People,
        listOf("family", "kids", "children", "parents", "relatives", "mother", "father", "spouse")
    ),
    TRAVEL(
        "Travel",
        Icons.Default.FlightTakeoff,
        listOf("travel", "trip", "vacation", "holiday", "flight", "ticket", "booking", "reservation")
    ),
    OTHER(
        "Other",
        Icons.Default.MoreHoriz,
        listOf()  // Default category
    );

    companion object {
        fun fromText(text: String): TaskCategory {
            val lowerText = text.lowercase()

            return entries.firstOrNull { category ->
                category.keywords.any { keyword ->
                    lowerText.contains(keyword)
                }
            } ?: OTHER
        }
    }
}