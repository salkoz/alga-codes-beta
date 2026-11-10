package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataSource
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalculatorState(
    val totalScore: Int = 118,
    val mathLiteracyScore: Int = 14,
    val readingLiteracyScore: Int = 15,
    val historyScore: Int = 18,
    val mathScore: Int = 36,
    val infoScore: Int = 35,
    val hasRuralQuota: Boolean = false,
    val hasAltynBelgi: Boolean = false,
    val hasOlympiadWinner: Boolean = false,
    val selectedGroupCode: String = "ALL", // ALL, B057, B058
    val selectedCity: String = "ALL" // ALL, Астана, Алматы
)

data class IdeState(
    val selectedLanguage: String = "Python", // Python, JavaScript
    val selectedTask: CodeTask = MockDataSource.codeTasks[0],
    val currentCode: String = MockDataSource.codeTasks[0].initialPythonCode,
    val consoleOutput: String = ">>> Среда выполнения готова.\n>>> Нажмите 'Запустить код' для тестирования.",
    val isRunning: Boolean = false,
    val executionSuccess: Boolean? = null
)

data class AppUiState(
    val userProfile: UserProfile = UserProfile(),
    val currentTab: Int = 0, // 0: Onboarding/Profile, 1: Dashboard, 2: Calculator, 3: IDE, 4: AI Agents
    val monthlyStats: List<MonthlyUntStat> = MockDataSource.initialMonthlyStats,
    val quests: List<DailyQuest> = MockDataSource.initialQuests,
    val calculatorState: CalculatorState = CalculatorState(),
    val ideState: IdeState = IdeState(),
    val selectedMentor: MentorType = MentorType.PYTHON_JS,
    val chatMessages: Map<MentorType, List<AiMessage>> = MockDataSource.mentorInitialMessages,
    val isMentorTyping: Boolean = false,
    val showScoreBreakdownDialog: Boolean = false,
    val isSyncing: Boolean = false
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(currentTab = tabIndex) }
    }

    // --- Onboarding / Profile Updates ---
    fun updateProfile(
        name: String,
        grade: String,
        targetUniversity: String,
        targetScore: Int,
        subjectPair: String
    ) {
        _uiState.update { state ->
            state.copy(
                userProfile = state.userProfile.copy(
                    name = name,
                    grade = grade,
                    targetUniversity = targetUniversity,
                    targetScore = targetScore,
                    subjectPair = subjectPair
                )
            )
        }
    }

    fun triggerCloudSync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            delay(1200)
            _uiState.update {
                it.copy(
                    isSyncing = false,
                    userProfile = it.userProfile.copy(isCloudSynced = true)
                )
            }
        }
    }

    // --- Quests & Gamification ---
    fun claimQuestReward(questId: String) {
        _uiState.update { state ->
            val quest = state.quests.find { it.id == questId } ?: return@update state
            if (quest.isClaimed || quest.currentProgress < quest.targetProgress) return@update state

            val updatedQuests = state.quests.map {
                if (it.id == questId) it.copy(isClaimed = true) else it
            }
            val newXp = state.userProfile.currentXp + quest.xpReward
            val levelUp = newXp >= state.userProfile.nextLevelXp
            val newLevel = if (levelUp) state.userProfile.level + 1 else state.userProfile.level
            val newNextXp = if (levelUp) state.userProfile.nextLevelXp + 2000 else state.userProfile.nextLevelXp

            state.copy(
                quests = updatedQuests,
                userProfile = state.userProfile.copy(
                    currentXp = newXp,
                    level = newLevel,
                    nextLevelXp = newNextXp
                )
            )
        }
    }

    // --- UNT Score Breakdown Dialog ---
    fun toggleScoreBreakdownDialog(show: Boolean) {
        _uiState.update { it.copy(showScoreBreakdownDialog = show) }
    }

    // --- Grant Calculator Actions ---
    fun updateCalculatorScore(newScore: Int) {
        val bounded = newScore.coerceIn(60, 140)
        _uiState.update { state ->
            val ratio = bounded.toFloat() / 140f
            state.copy(
                calculatorState = state.calculatorState.copy(
                    totalScore = bounded,
                    mathLiteracyScore = (15 * ratio).toInt().coerceIn(3, 15),
                    readingLiteracyScore = (15 * ratio).toInt().coerceIn(3, 15),
                    historyScore = (20 * ratio).toInt().coerceIn(5, 20),
                    mathScore = (45 * ratio).toInt().coerceIn(10, 45),
                    infoScore = (45 * ratio).toInt().coerceIn(10, 45)
                )
            )
        }
    }

    fun toggleRuralQuota(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(calculatorState = state.calculatorState.copy(hasRuralQuota = enabled))
        }
    }

    fun toggleAltynBelgi(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(calculatorState = state.calculatorState.copy(hasAltynBelgi = enabled))
        }
    }

    fun toggleOlympiadWinner(enabled: Boolean) {
        _uiState.update { state ->
            state.copy(calculatorState = state.calculatorState.copy(hasOlympiadWinner = enabled))
        }
    }

    fun setGroupCodeFilter(code: String) {
        _uiState.update { state ->
            state.copy(calculatorState = state.calculatorState.copy(selectedGroupCode = code))
        }
    }

    fun setCityFilter(city: String) {
        _uiState.update { state ->
            state.copy(calculatorState = state.calculatorState.copy(selectedCity = city))
        }
    }

    // --- IDE & Code Practice Actions ---
    fun selectCodeLanguage(lang: String) {
        _uiState.update { state ->
            val task = state.ideState.selectedTask
            val code = if (lang == "Python") task.initialPythonCode else task.initialJsCode
            state.copy(
                ideState = state.ideState.copy(
                    selectedLanguage = lang,
                    currentCode = code,
                    consoleOutput = ">>> Переключено на $lang. Нажмите 'Запустить' для выполнения."
                )
            )
        }
    }

    fun selectTask(task: CodeTask) {
        _uiState.update { state ->
            val code = if (state.ideState.selectedLanguage == "Python") task.initialPythonCode else task.initialJsCode
            state.copy(
                ideState = state.ideState.copy(
                    selectedTask = task,
                    currentCode = code,
                    consoleOutput = ">>> Выбрана задача: ${task.title}\n>>> Категория: ${task.category} | ${task.difficulty}",
                    executionSuccess = null
                )
            )
        }
    }

    fun updateCurrentCode(code: String) {
        _uiState.update { state ->
            state.copy(ideState = state.ideState.copy(currentCode = code))
        }
    }

    fun resetCode() {
        _uiState.update { state ->
            val task = state.ideState.selectedTask
            val code = if (state.ideState.selectedLanguage == "Python") task.initialPythonCode else task.initialJsCode
            state.copy(
                ideState = state.ideState.copy(
                    currentCode = code,
                    consoleOutput = ">>> Код сброшен к исходному шаблону.",
                    executionSuccess = null
                )
            )
        }
    }

    fun executeCode() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    ideState = state.ideState.copy(
                        isRunning = true,
                        consoleOutput = ">>> Компиляция и тестирование решения...\n>>> Прогон тест-кейсов ЕНТ 2026..."
                    )
                )
            }
            delay(1000)

            val currentTask = _uiState.value.ideState.selectedTask
            val output = buildString {
                append(">>> Вывод программы:\n")
                append("${currentTask.expectedOutputSnippet}\n\n")
                append("------------------------------------------\n")
                append("✓ Тест 1 (Базовый случай): ПРОЙДЕН (12ms)\n")
                append("✓ Тест 2 (Граничные условия): ПРОЙДЕН (18ms)\n")
                append("✓ Тест 3 (Скрытый тест ЕНТ): ПРОЙДЕН (14ms)\n")
                append(">>> Статус: 3/3 теста успешно завершено!\n")
                append(">>> Награда: +${currentTask.xpReward} XP начислено в профиль!")
            }

            _uiState.update { state ->
                val newXp = state.userProfile.currentXp + currentTask.xpReward
                state.copy(
                    ideState = state.ideState.copy(
                        isRunning = false,
                        consoleOutput = output,
                        executionSuccess = true
                    ),
                    userProfile = state.userProfile.copy(currentXp = newXp)
                )
            }
        }
    }

    // --- AI Agents Hub Actions ---
    fun selectMentor(mentor: MentorType) {
        _uiState.update { it.copy(selectedMentor = mentor) }
    }

    fun sendMentorMessage(userText: String) {
        if (userText.isBlank()) return
        val mentor = _uiState.value.selectedMentor
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val userMsg = AiMessage(
            id = "u_${System.currentTimeMillis()}",
            sender = "user",
            text = userText,
            timestamp = timeNow
        )

        _uiState.update { state ->
            val list = state.chatMessages[mentor]?.toMutableList() ?: mutableListOf()
            list.add(userMsg)
            val updatedMap = state.chatMessages.toMutableMap()
            updatedMap[mentor] = list
            state.copy(
                chatMessages = updatedMap,
                isMentorTyping = true
            )
        }

        viewModelScope.launch {
            delay(1200)
            val (replyText, snippet) = generateMentorResponse(mentor, userText)
            val mentorMsg = AiMessage(
                id = "m_${System.currentTimeMillis()}",
                sender = "mentor",
                text = replyText,
                codeSnippet = snippet,
                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )

            _uiState.update { state ->
                val list = state.chatMessages[mentor]?.toMutableList() ?: mutableListOf()
                list.add(mentorMsg)
                val updatedMap = state.chatMessages.toMutableMap()
                updatedMap[mentor] = list
                state.copy(
                    chatMessages = updatedMap,
                    isMentorTyping = false
                )
            }
        }
    }

    private fun generateMentorResponse(mentor: MentorType, text: String): Pair<String, String?> {
        val lower = text.lowercase()
        return when (mentor) {
            MentorType.PYTHON_JS -> {
                when {
                    "граф" in lower || "graph" in lower -> Pair(
                        "Для представления графов в задачах ЕНТ по информатике чаще всего используют списки смежности на основе словарей dict в Python. Для поиска кратчайшего пути используйте алгоритм BFS (поиск в ширину) на основе очереди deque.",
                        """from collections import deque

def bfs_shortest_path(graph, start, goal):
    queue = deque([[start]])
    visited = {start}
    while queue:
        path = queue.popleft()
        node = path[-1]
        if node == goal:
            return path
        for neighbor in graph.get(node, []):
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(path + [neighbor])
    return None"""
                    )
                    "кбту" in lower || "проходной" in lower -> Pair(
                        "Проходные баллы на IT специальности в КБТУ исторически начинаются от 126 баллов на общий конкурс. Рекомендую дополнительно решать задания по рекурсии и деревьям.",
                        null
                    )
                    "цикл" in lower || "loop" in lower -> Pair(
                        "В ЕНТ часто встречаются вложенные циклы `for` и `while`. Помните, что `range(a, b)` не включает конечное число `b`!",
                        """total = 0
for i in range(1, 6):
    total += i * 2
print("Total:", total)  # 30"""
                    )
                    else -> Pair(
                        "Отличный вопрос по программированию! В тестах ЕНТ важно не только написать работающий код, но и уложиться по сложности O(N). Проверьте этот шаблон:",
                        """# Оптимизированное решение для ЕНТ
def fast_solution(items):
    seen = set()
    for x in items:
        if x in seen:
            return True
        seen.add(x)
    return False"""
                    )
                }
            }
            MentorType.MATH_LOGIC -> {
                when {
                    "процент" in lower || "скорость" in lower -> Pair(
                        "В задачах на движение и проценты формулируйте уравнение через общую работу или общее расстояние: S = V * t. Средняя скорость — это не среднее арифметическое, а все расстояние, деленное на все затраченное время!",
                        "V_avg = (2 * v1 * v2) / (v1 + v2)  # если пути равны"
                    )
                    "комбинаторик" in lower || "вероятност" in lower -> Pair(
                        "Для вычисления сочетаний C(n, k) используйте формулу n! / (k! * (n-k)!). Это ключевая тема на ЕНТ по математической грамотности.",
                        "import math\nways = math.comb(10, 3) # 120 вариантов"
                    )
                    else -> Pair(
                        "Для решения этого задания по математике представьте неизвестные как переменные x и y. Давайте исключим варианты ответа подстановкой крайних значений (0, 1 или 100).",
                        null
                    )
                }
            }
            MentorType.GRANT_CAREER -> {
                when {
                    "квота" in lower || "серпін" in lower -> Pair(
                        "Сельская квота (35%) и программа «Серпін» дают преимущество при равенстве или снижают реальный порог на 6–10 баллов. Обязательно поставьте галочку в калькуляторе грантов Alga.codes!",
                        null
                    )
                    "aitu" in lower || "астана" in lower -> Pair(
                        "Astana IT University (AITU) — топ выбор по группе B057. Проходной балл в прошлом году составил 119 баллов. С вашим текущим прогнозом 118 баллов вероятность гранта составляет 94%!",
                        null
                    )
                    else -> Pair(
                        "Стратегия подачи на 4 гранта в Казахстане: 1-й выбор — Вуз мечты (КБТУ / AITU), 2-й и 3-й — надежный запас с гарантией 90%+ (SDU, Satbayev, ЕНУ), 4-й — 100% страховка (региональный или профильный вуз).",
                        null
                    )
                }
            }
        }
    }
}
