package com.example.data

import com.example.model.*

object MockDataSource {

    val initialMonthlyStats = listOf(
        MonthlyUntStat(
            month = "Янв",
            score = 88,
            breakdowns = listOf(
                SubjectScoreBreakdown("Мат. грамотность", 11, 15, 0xFFFFB000),
                SubjectScoreBreakdown("Грамотность чтения", 13, 15, 0xFF00E5FF),
                SubjectScoreBreakdown("История Казахстана", 14, 20, 0xFF10B981),
                SubjectScoreBreakdown("Математика (профиль)", 26, 45, 0xFFFC3B00),
                SubjectScoreBreakdown("Информатика (профиль)", 24, 45, 0xFFFF6129)
            )
        ),
        MonthlyUntStat(
            month = "Фев",
            score = 96,
            breakdowns = listOf(
                SubjectScoreBreakdown("Мат. грамотность", 12, 15, 0xFFFFB000),
                SubjectScoreBreakdown("Грамотность чтения", 14, 15, 0xFF00E5FF),
                SubjectScoreBreakdown("История Казахстана", 16, 20, 0xFF10B981),
                SubjectScoreBreakdown("Математика (профиль)", 28, 45, 0xFFFC3B00),
                SubjectScoreBreakdown("Информатика (профиль)", 26, 45, 0xFFFF6129)
            )
        ),
        MonthlyUntStat(
            month = "Мар",
            score = 104,
            breakdowns = listOf(
                SubjectScoreBreakdown("Мат. грамотность", 13, 15, 0xFFFFB000),
                SubjectScoreBreakdown("Грамотность чтения", 15, 15, 0xFF00E5FF),
                SubjectScoreBreakdown("История Казахстана", 17, 20, 0xFF10B981),
                SubjectScoreBreakdown("Математика (профиль)", 31, 45, 0xFFFC3B00),
                SubjectScoreBreakdown("Информатика (профиль)", 28, 45, 0xFFFF6129)
            )
        ),
        MonthlyUntStat(
            month = "Апр",
            score = 111,
            breakdowns = listOf(
                SubjectScoreBreakdown("Мат. грамотность", 14, 15, 0xFFFFB000),
                SubjectScoreBreakdown("Грамотность чтения", 15, 15, 0xFF00E5FF),
                SubjectScoreBreakdown("История Казахстана", 18, 20, 0xFF10B981),
                SubjectScoreBreakdown("Математика (профиль)", 33, 45, 0xFFFC3B00),
                SubjectScoreBreakdown("Информатика (профиль)", 31, 45, 0xFFFF6129)
            )
        ),
        MonthlyUntStat(
            month = "Май",
            score = 118,
            isCurrent = true,
            breakdowns = listOf(
                SubjectScoreBreakdown("Мат. грамотность", 14, 15, 0xFFFFB000),
                SubjectScoreBreakdown("Грамотность чтения", 15, 15, 0xFF00E5FF),
                SubjectScoreBreakdown("История Казахстана", 18, 20, 0xFF10B981),
                SubjectScoreBreakdown("Математика (профиль)", 36, 45, 0xFFFC3B00),
                SubjectScoreBreakdown("Информатика (профиль)", 35, 45, 0xFFFF6129)
            )
        )
    )

    val initialQuests = listOf(
        DailyQuest(
            id = "q1",
            title = "Решить 3 задачи на Python",
            description = "Закрепите алгоритмы циклов и списков",
            xpReward = 150,
            currentProgress = 2,
            targetProgress = 3,
            isClaimed = false,
            iconType = "code"
        ),
        DailyQuest(
            id = "q2",
            title = "Экспресс-тест по Информатике",
            description = "Пройдите блок из 15 вопросов по системам счисления",
            xpReward = 200,
            currentProgress = 15,
            targetProgress = 15,
            isClaimed = false,
            iconType = "quiz"
        ),
        DailyQuest(
            id = "q3",
            title = "Ударный режим: 14 дней подряд",
            description = "Поддерживайте огонь ежедневных занятий",
            xpReward = 300,
            currentProgress = 14,
            targetProgress = 14,
            isClaimed = true,
            iconType = "streak"
        ),
        DailyQuest(
            id = "q4",
            title = "Разбор задачи на графы с Ментором",
            description = "Задайте вопрос AI агенту по поиску кратчайшего пути",
            xpReward = 100,
            currentProgress = 0,
            targetProgress = 1,
            isClaimed = false,
            iconType = "code"
        )
    )

    val universityPrograms = listOf(
        UniversityProgram(
            id = "aitu-b057",
            universityName = "Astana IT University",
            universityShort = "AITU",
            city = "Астана",
            groupCode = "B057",
            groupTitle = "Информационные технологии & CS",
            historicalMinScoreGeneral = 119,
            historicalMinScoreQuota = 112,
            grantSeats = 650,
            ratingRank = 1,
            badgeLabel = "Топ IT Вуз РК"
        ),
        UniversityProgram(
            id = "kbtu-b057",
            universityName = "Казахстанско-Британский технический университет",
            universityShort = "КБТУ",
            city = "Алматы",
            groupCode = "B057",
            groupTitle = "Информационные системы и программная инженерия",
            historicalMinScoreGeneral = 126,
            historicalMinScoreQuota = 118,
            grantSeats = 380,
            ratingRank = 2,
            badgeLabel = "Высший проходной"
        ),
        UniversityProgram(
            id = "sdu-b057",
            universityName = "SDU University (СДУ)",
            universityShort = "SDU",
            city = "Каскелен / Алматы",
            groupCode = "B057",
            groupTitle = "Computer Science & Software Systems",
            historicalMinScoreGeneral = 114,
            historicalMinScoreQuota = 106,
            grantSeats = 450,
            ratingRank = 3,
            badgeLabel = "Международный кампус"
        ),
        UniversityProgram(
            id = "kaznu-b058",
            universityName = "КазНУ им. аль-Фараби",
            universityShort = "КазНУ",
            city = "Алматы",
            groupCode = "B058",
            groupTitle = "Информационная безопасность",
            historicalMinScoreGeneral = 116,
            historicalMinScoreQuota = 108,
            grantSeats = 520,
            ratingRank = 4,
            badgeLabel = "Флагман РК"
        ),
        UniversityProgram(
            id = "enu-b057",
            universityName = "ЕНУ им. Л.Н. Гумилева",
            universityShort = "ЕНУ",
            city = "Астана",
            groupCode = "B057",
            groupTitle = "Информационные технологии",
            historicalMinScoreGeneral = 112,
            historicalMinScoreQuota = 102,
            grantSeats = 490,
            ratingRank = 5,
            badgeLabel = "Нац. Университет"
        ),
        UniversityProgram(
            id = "iitu-b057",
            universityName = "Международный университет информационных технологий",
            universityShort = "МУИТ",
            city = "Алматы",
            groupCode = "B057",
            groupTitle = "Software Engineering & Data Science",
            historicalMinScoreGeneral = 115,
            historicalMinScoreQuota = 107,
            grantSeats = 420,
            ratingRank = 6,
            badgeLabel = "Профильный IT"
        ),
        UniversityProgram(
            id = "satbayev-b058",
            universityName = "Satbayev University",
            universityShort = "Satbayev",
            city = "Алматы",
            groupCode = "B058",
            groupTitle = "Кибербезопасность & Робототехника",
            historicalMinScoreGeneral = 109,
            historicalMinScoreQuota = 98,
            grantSeats = 510,
            ratingRank = 7,
            badgeLabel = "Инженерный хаб"
        )
    )

    val codeTasks = listOf(
        CodeTask(
            id = "task-1",
            title = "Сумма четных делителей числа",
            difficulty = "Easy",
            category = "ЕНТ Логика",
            description = "В вопросах ЕНТ по информатике часто требуется найти сумму всех четных натуральных делителей заданного числа N. Напишите эффективную функцию.",
            initialPythonCode = """# ЕНТ Информатика: Задача на делители
def solve(n: int) -> int:
    div_sum = 0
    for i in range(1, n + 1):
        if n % i == 0 and i % 2 == 0:
            div_sum += i
    return div_sum

# Тестовый вызов
n = 12
result = solve(n)
print(f"Сумма четных делителей числа {n}: {result}")
""",
            initialJsCode = """// ЕНТ Информатика: Задача на делители
function solve(n) {
    let sum = 0;
    for (let i = 1; i <= n; i++) {
        if (n % i === 0 && i % 2 === 0) {
            sum += i;
        }
    }
    return sum;
}

const n = 12;
console.log("Сумма четных делителей числа " + n + ": " + solve(n));
""",
            expectedOutputSnippet = "Сумма четных делителей числа 12: 24",
            xpReward = 120,
            completed = true
        ),
        CodeTask(
            id = "task-2",
            title = "Быстрый расчет среднего балла ЕНТ",
            difficulty = "Medium",
            category = "Алгоритмы",
            description = "Рассчитайте средний балл за 5 предметов ЕНТ и определите процент готовности к гранту с округлением до сотых.",
            initialPythonCode = """# Расчет среднего балла ЕНТ
scores = [14, 15, 18, 36, 35]  # Всего: 118 из 140

def calculate_stats(scores_list):
    total = sum(scores_list)
    avg = total / len(scores_list)
    grant_readiness = (total / 140) * 100
    return total, round(avg, 2), round(grant_readiness, 1)

total, avg, readiness = calculate_stats(scores)
print(f"Сумма: {total}/140 | Средний: {avg} | Готовность: {readiness}%")
""",
            initialJsCode = """// Расчет среднего балла ЕНТ
const scores = [14, 15, 18, 36, 35];

function calculateStats(arr) {
    const total = arr.reduce((a, b) => a + b, 0);
    const avg = (total / arr.length).toFixed(2);
    const readiness = ((total / 140) * 100).toFixed(1);
    return { total, avg, readiness };
}

const res = calculateStats(scores);
console.log("Сумма: " + res.total + "/140 | Средний: " + res.avg + " | Готовность: " + res.readiness + "%");
""",
            expectedOutputSnippet = "Сумма: 118/140 | Средний: 23.6 | Готовность: 84.3%",
            xpReward = 180,
            completed = false
        ),
        CodeTask(
            id = "task-3",
            title = "Поиск подстроки ДНК/Кода в ЕНТ",
            difficulty = "Hard",
            category = "Структуры данных",
            description = "Найдите длину максимальной непрерывной последовательности одинаковых символов в строке шифра ЕНТ за O(N).",
            initialPythonCode = """# Поиск максимальной последовательности
def max_consecutive_len(s: str) -> int:
    if not s:
        return 0
    max_len = 1
    current_len = 1
    for i in range(1, len(s)):
        if s[i] == s[i - 1]:
            current_len += 1
            max_len = max(max_len, current_len)
        else:
            current_len = 1
    return max_len

code = "01100111100"
print(f"Длина серии: {max_consecutive_len(code)}")
""",
            initialJsCode = """// Поиск максимальной последовательности
function maxConsecutiveLen(s) {
    if (!s) return 0;
    let max = 1, cur = 1;
    for (let i = 1; i < s.length; i++) {
        if (s[i] === s[i-1]) {
            cur++;
            if (cur > max) max = cur;
        } else {
            cur = 1;
        }
    }
    return max;
}

const code = "01100111100";
console.log("Длина серии: " + maxConsecutiveLen(code));
""",
            expectedOutputSnippet = "Длина серии: 4",
            xpReward = 250,
            completed = false
        )
    )

    val mentorInitialMessages = mapOf(
        MentorType.PYTHON_JS to listOf(
            AiMessage(
                id = "m1",
                sender = "mentor",
                text = "Привет, будущий программист! Я помогу с разбором задач ЕНТ по информатике, синтаксисом Python & JavaScript и отладкой кода.",
                timestamp = "10:14"
            ),
            AiMessage(
                id = "m2",
                sender = "mentor",
                text = "В тестах ЕНТ 2026 особое внимание уделяется срезам строк, рекурсии и словарным структурам в Python.",
                codeSnippet = "s = 'AlgaCodes'\nprint(s[1:7:2])  # Выведет: 'lao'",
                timestamp = "10:15"
            )
        ),
        MentorType.MATH_LOGIC to listOf(
            AiMessage(
                id = "m3",
                sender = "mentor",
                text = "Сәлем! Я твой эксперт по Математической грамотности и профильной Математике. Разберем любую сложную задачу пошагово.",
                timestamp = "09:30"
            ),
            AiMessage(
                id = "m4",
                sender = "mentor",
                text = "Главный секрет матграмотности — быстрое составление уравнений и исключение заведомо невозможных вариантов.",
                timestamp = "09:32"
            )
        ),
        MentorType.GRANT_CAREER to listOf(
            AiMessage(
                id = "m5",
                sender = "mentor",
                text = "Здравствуйте! Готов рассчитать ваши шансы на государственный образовательный грант в вузы Казахстана и подобрать приоритеты заявки (4 вуза).",
                timestamp = "08:45"
            ),
            AiMessage(
                id = "m6",
                sender = "mentor",
                text = "По группе B057 (Информационные технологии) средний проходной балл в топ вузы варьируется от 109 до 126 баллов. С учетом квоты Серпін или сельской квоты порог может быть на 6–10 баллов ниже.",
                timestamp = "08:48"
            )
        )
    )
}
