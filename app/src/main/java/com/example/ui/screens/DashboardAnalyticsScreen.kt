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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.DailyQuest
import com.example.model.MonthlyUntStat
import com.example.model.UserProfile
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun DashboardAnalyticsScreen(
    userProfile: UserProfile,
    monthlyStats: List<MonthlyUntStat>,
    quests: List<DailyQuest>,
    showBreakdownDialog: Boolean,
    onToggleBreakdownDialog: (Boolean) -> Unit,
    onClaimQuest: (String) -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToIde: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. User Profile & Gamification Header ---
        GlassCard(
            borderColor = DarkBorderHighlight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(VividOrange, CoralRed)
                                )
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = userProfile.name,
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        // Level badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(VividOrange.copy(alpha = 0.2f))
                                    .border(0.5.dp, VividOrange, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LVL ${userProfile.level}",
                                    color = VividOrange,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Text(
                                text = userProfile.levelTitle,
                                color = ElectricAmber,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                // Daily Streak counter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CoralRed.copy(alpha = 0.15f))
                        .border(1.dp, CoralRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.streakDays} дней",
                            color = CoralRed,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // XP Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Опыт (XP): ${userProfile.currentXp} / ${userProfile.nextLevelXp} XP",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${((userProfile.currentXp.toFloat() / userProfile.nextLevelXp) * 100).toInt()}%",
                    color = ElectricAmber,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            val progressRatio = (userProfile.currentXp.toFloat() / userProfile.nextLevelXp.toFloat()).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(DarkSurfaceElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(VividOrange, ElectricAmber)
                            )
                        )
                )
            }
        }

        // --- 2. Featured Analytic Card ("Динамика баллов ЕНТ") ---
        GlassCard(
            modifier = Modifier.testTag("unt_analytics_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Динамика баллов ЕНТ",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Пробные тестирования 2026",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Current result pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VividOrange.copy(alpha = 0.2f))
                        .border(1.dp, VividOrange, RoundedCornerShape(12.dp))
                        .clickable { onToggleBreakdownDialog(true) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "118 / 140",
                            color = VividOrange,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Детализация",
                            tint = VividOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Interactive Vertical Bar Chart (Jan - May)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                monthlyStats.forEach { stat ->
                    val heightRatio = (stat.score.toFloat() / 140f).coerceIn(0.1f, 1f)
                    val isCurrent = stat.isCurrent

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onToggleBreakdownDialog(true) }
                    ) {
                        Text(
                            text = "${stat.score}",
                            color = if (isCurrent) VividOrange else TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .width(if (isCurrent) 28.dp else 22.dp)
                                .height((120 * heightRatio).dp)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(
                                    if (isCurrent) {
                                        Brush.verticalGradient(
                                            colors = listOf(VividOrange, CoralRed)
                                        )
                                    } else {
                                        Brush.verticalGradient(
                                            colors = listOf(DarkSurfaceElevated, Color(0xFF27272A))
                                        )
                                    }
                                )
                                .border(
                                    width = if (isCurrent) 1.dp else 0.5.dp,
                                    color = if (isCurrent) VividOrange else DarkBorder,
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stat.month,
                            color = if (isCurrent) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Текущий прирост: +30 баллов с января",
                    color = NeonGreen,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "Нажмите столбец для деталей",
                    color = TextSubtle,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Quick Actions Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToCalculator() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ElectricAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Calculate,
                            contentDescription = null,
                            tint = ElectricAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Шанс гранта", color = TextPrimary, style = MaterialTheme.typography.labelLarge)
                        Text("Топ вузы РК", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToIde() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Код-тренажер", color = TextPrimary, style = MaterialTheme.typography.labelLarge)
                        Text("Python & JS", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // --- 3. Daily Quests & Achievements Widget ---
        GlassCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎯", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ежедневные квесты ЕНТ",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Text(
                    text = "${quests.count { it.isClaimed }}/${quests.size} выполнено",
                    color = ElectricAmber,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            quests.forEach { quest ->
                val isCompleted = quest.currentProgress >= quest.targetProgress
                val isClaimed = quest.isClaimed

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isClaimed) DarkSurfaceElevated.copy(alpha = 0.5f) else DarkSurfaceElevated)
                        .border(
                            width = 1.dp,
                            color = when {
                                isClaimed -> DarkBorder
                                isCompleted -> NeonGreen.copy(alpha = 0.5f)
                                else -> DarkBorder
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quest.title,
                            color = if (isClaimed) TextMuted else TextPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = quest.description,
                            color = TextSubtle,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Progress text
                        Text(
                            text = "Прогресс: ${quest.currentProgress}/${quest.targetProgress} | Награда: +${quest.xpReward} XP",
                            color = ElectricAmber,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if (isClaimed) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Забрано",
                                color = TextSubtle,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    } else if (isCompleted) {
                        Button(
                            onClick = { onClaimQuest(quest.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Забрать XP", color = DarkBackground, style = MaterialTheme.typography.labelSmall)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "В процессе",
                                color = TextMuted,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // --- Sub-Subject Breakdown Dialog ---
    if (showBreakdownDialog) {
        val mayStat = monthlyStats.find { it.isCurrent } ?: monthlyStats.last()
        Dialog(onDismissRequest = { onToggleBreakdownDialog(false) }) {
            GlassCard(
                borderColor = VividOrange,
                backgroundColor = Color(0xF0141416)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Детализация баллов ЕНТ",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Тестирование за Май 2026: 118 / 140",
                            color = VividOrange,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    IconButton(onClick = { onToggleBreakdownDialog(false) }) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                mayStat.breakdowns.forEach { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.subjectName,
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${item.currentScore} / ${item.maxScore}",
                                color = Color(item.colorHex),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        val ratio = item.currentScore.toFloat() / item.maxScore.toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(DarkSurfaceElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(ratio)
                                    .fillMaxHeight()
                                    .background(Color(item.colorHex))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onToggleBreakdownDialog(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = VividOrange),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Понятно", color = TextPrimary)
                }
            }
        }
    }
}
