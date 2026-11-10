package com.example.model

data class UserProfile(
    val name: String = "Алихан Сериков",
    val grade: String = "11 класс",
    val targetUniversity: String = "Astana IT University (AITU)",
    val targetScore: Int = 125,
    val subjectPair: String = "Информатика + Математика",
    val level: Int = 5,
    val levelTitle: String = "Code Cyber",
    val currentXp: Int = 3450,
    val nextLevelXp: Int = 5000,
    val streakDays: Int = 14,
    val isCloudSynced: Boolean = true
)

data class SubjectScoreBreakdown(
    val subjectName: String,
    val currentScore: Int,
    val maxScore: Int,
    val colorHex: Long
)

data class MonthlyUntStat(
    val month: String,
    val score: Int,
    val isCurrent: Boolean = false,
    val breakdowns: List<SubjectScoreBreakdown>
)

data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val currentProgress: Int,
    val targetProgress: Int,
    val isClaimed: Boolean = false,
    val iconType: String = "code" // code, quiz, streak
)

data class UniversityProgram(
    val id: String,
    val universityName: String,
    val universityShort: String,
    val city: String,
    val groupCode: String, // B057, B058
    val groupTitle: String,
    val historicalMinScoreGeneral: Int,
    val historicalMinScoreQuota: Int,
    val grantSeats: Int,
    val ratingRank: Int,
    val badgeLabel: String = ""
)

data class CodeTask(
    val id: String,
    val title: String,
    val difficulty: String, // Easy, Medium, Hard
    val category: String, // Алгоритмы, Структуры данных, ЕНТ Логика
    val description: String,
    val initialPythonCode: String,
    val initialJsCode: String,
    val expectedOutputSnippet: String,
    val xpReward: Int,
    val completed: Boolean = false
)

data class AiMessage(
    val id: String,
    val sender: String, // user, mentor
    val text: String,
    val codeSnippet: String? = null,
    val timestamp: String = ""
)

enum class MentorType(val title: String, val subtitle: String, val avatarEmoji: String) {
    PYTHON_JS("Python & JS Mentor", "Синтаксис, дебаг, алгоритмы", "🐍"),
    MATH_LOGIC("Math Logic Expert", "ЕНТ математика & грамотность", "📐"),
    GRANT_CAREER("Grant & Career Advisor RK", "Вузы, квоты, стратегия грантов", "🎓")
}
