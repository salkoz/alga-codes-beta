package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.MockDataSource
import com.example.model.UniversityProgram
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.viewmodel.CalculatorState

@Composable
fun GrantCalculatorScreen(
    calculatorState: CalculatorState,
    onScoreChange: (Int) -> Unit,
    onToggleRuralQuota: (Boolean) -> Unit,
    onToggleAltynBelgi: (Boolean) -> Unit,
    onToggleOlympiadWinner: (Boolean) -> Unit,
    onSelectGroupCode: (String) -> Unit,
    onSelectCity: (String) -> Unit
) {
    val programs = MockDataSource.universityPrograms

    // Smart Match & Sorting Engine
    val filteredPrograms = remember(calculatorState) {
        programs.filter { prog ->
            val matchGroup = calculatorState.selectedGroupCode == "ALL" || prog.groupCode == calculatorState.selectedGroupCode
            val matchCity = calculatorState.selectedCity == "ALL" || prog.city.contains(calculatorState.selectedCity)
            matchGroup && matchCity
        }.sortedByDescending { prog ->
            // Calculate admission probability
            val threshold = if (calculatorState.hasRuralQuota) prog.historicalMinScoreQuota else prog.historicalMinScoreGeneral
            val bonus = (if (calculatorState.hasAltynBelgi) 3 else 0) + (if (calculatorState.hasOlympiadWinner) 7 else 0)
            val effectiveScore = calculatorState.totalScore + bonus
            val diff = effectiveScore - threshold
            diff
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Top Calculator Card with UNT Score Slider ---
        GlassCard(
            borderColor = DarkBorderHighlight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Продвинутый калькулятор грантов",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Прогноз поступления в топ вузы РК 2026",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VividOrange)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${calculatorState.totalScore} / 140",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Score Slider
            Text(
                text = "Итоговый балл ЕНТ: ${calculatorState.totalScore}",
                color = ElectricAmber,
                style = MaterialTheme.typography.bodyMedium
            )

            Slider(
                value = calculatorState.totalScore.toFloat(),
                onValueChange = { onScoreChange(it.toInt()) },
                valueRange = 60f..140f,
                steps = 79,
                colors = SliderDefaults.colors(
                    thumbColor = VividOrange,
                    activeTrackColor = VividOrange,
                    inactiveTrackColor = DarkSurfaceElevated
                ),
                modifier = Modifier.testTag("calculator_score_slider")
            )

            // Profile Subject Breakdown Chips
            Text(
                text = "Распределение по предметам:",
                color = TextMuted,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SubjectScoreMiniBadge("МатГрам", "${calculatorState.mathLiteracyScore}/15", ElectricAmber)
                SubjectScoreMiniBadge("ГрамЧтен", "${calculatorState.readingLiteracyScore}/15", CyberCyan)
                SubjectScoreMiniBadge("ИстКаз", "${calculatorState.historyScore}/20", NeonGreen)
                SubjectScoreMiniBadge("Мат", "${calculatorState.mathScore}/45", VividOrange)
                SubjectScoreMiniBadge("Инфо", "${calculatorState.infoScore}/45", CoralRed)
            }
        }

        // --- 2. Special Quotas & Advantages ---
        GlassCard {
            Text(
                text = "Льготы и квоты РК",
                color = ElectricAmber,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            QuotaCheckboxRow(
                title = "Сельская квота / Серпін (35%)",
                subtitle = "Снижает проходной порог на 6–10 баллов",
                isChecked = calculatorState.hasRuralQuota,
                onCheckedChange = onToggleRuralQuota
            )

            QuotaCheckboxRow(
                title = "Знак «Алтын белгі»",
                subtitle = "Преимущественное право при равенстве баллов",
                isChecked = calculatorState.hasAltynBelgi,
                onCheckedChange = onToggleAltynBelgi
            )

            QuotaCheckboxRow(
                title = "Победитель республиканских олимпиад",
                subtitle = "Приоритетное зачисление на грант МОН РК",
                isChecked = calculatorState.hasOlympiadWinner,
                onCheckedChange = onToggleOlympiadWinner
            )
        }

        // --- 3. Filters (Group code & City) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterTabButton("Все группы", calculatorState.selectedGroupCode == "ALL") { onSelectGroupCode("ALL") }
            FilterTabButton("B057 IT", calculatorState.selectedGroupCode == "B057") { onSelectGroupCode("B057") }
            FilterTabButton("B058 Кибер", calculatorState.selectedGroupCode == "B058") { onSelectGroupCode("B058") }
        }

        // --- 4. Smart Match Engine List ---
        Text(
            text = "Рекомендованные университеты (${filteredPrograms.size}):",
            color = TextPrimary,
            style = MaterialTheme.typography.titleSmall
        )

        filteredPrograms.forEach { prog ->
            val threshold = if (calculatorState.hasRuralQuota) prog.historicalMinScoreQuota else prog.historicalMinScoreGeneral
            val bonus = (if (calculatorState.hasAltynBelgi) 3 else 0) + (if (calculatorState.hasOlympiadWinner) 7 else 0)
            val effectiveScore = calculatorState.totalScore + bonus
            val diff = effectiveScore - threshold

            // Probability logic:
            val probability = when {
                diff >= 5 -> 99
                diff >= 2 -> 92
                diff >= 0 -> 82
                diff >= -3 -> 64
                diff >= -6 -> 42
                else -> 18
            }

            val statusColor = when {
                probability >= 85 -> NeonGreen
                probability >= 60 -> ElectricAmber
                else -> CoralRed
            }

            GlassCard(
                borderColor = if (probability >= 85) DarkBorderHighlight else DarkBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prog.universityShort,
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DarkSurfaceElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = prog.city,
                                    color = TextMuted,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = prog.universityName,
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${prog.groupCode}: ${prog.groupTitle}",
                            color = ElectricAmber,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    // Probability Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusColor.copy(alpha = 0.15f))
                            .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$probability%",
                                color = statusColor,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Шанс гранта",
                                color = statusColor,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Score comparisons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Порог 2025: $threshold баллов",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Мест на грант: ${prog.grantSeats}",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Recommendation Tip
                val tipText = when {
                    probability >= 90 -> "✓ Гарантированное поступление! Высокий запас прочности."
                    diff < 0 -> "💡 Совет: добавьте ${-diff + 1} балла для 90%+ гарантии в ${prog.universityShort}."
                    else -> "⚡ Рекомендуется поставить этот вуз 1-м или 2-м приоритетом в заявке."
                }

                Text(
                    text = tipText,
                    color = if (probability >= 90) NeonGreen else ElectricAmber,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SubjectScoreMiniBadge(label: String, score: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(0.5.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, color = TextMuted, style = MaterialTheme.typography.labelSmall)
            Text(text = score, color = color, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun QuotaCheckboxRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = VividOrange,
                checkmarkColor = TextPrimary,
                uncheckedColor = TextMuted
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = title, color = TextPrimary, style = MaterialTheme.typography.bodySmall)
            Text(text = subtitle, color = TextSubtle, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun FilterTabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) VividOrange.copy(alpha = 0.25f) else DarkSurfaceElevated)
            .border(1.dp, if (isSelected) VividOrange else DarkBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) TextPrimary else TextMuted,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
