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
import androidx.compose.ui.unit.sp
import com.example.model.UserProfile
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun OnboardingProfileScreen(
    userProfile: UserProfile,
    isSyncing: Boolean,
    onSaveProfile: (String, String, String, Int, String) -> Unit,
    onSyncNow: () -> Unit
) {
    var name by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var selectedGrade by remember(userProfile.grade) { mutableStateOf(userProfile.grade) }
    var selectedUniversity by remember(userProfile.targetUniversity) { mutableStateOf(userProfile.targetUniversity) }
    var targetScore by remember(userProfile.targetScore) { mutableIntStateOf(userProfile.targetScore) }
    var selectedSubjectPair by remember(userProfile.subjectPair) { mutableStateOf(userProfile.subjectPair) }
    var isSavedNotificationVisible by remember { mutableStateOf(false) }

    val subjectPairs = listOf(
        "Информатика + Математика",
        "Математика + Физика",
        "Информатика + Физика",
        "География + Математика"
    )

    val universities = listOf(
        "Astana IT University (AITU)",
        "КБТУ (Алматы)",
        "SDU University",
        "КазНУ им. аль-Фараби",
        "ЕНУ им. Л.Н. Гумилева",
        "МУИТ (IITU)"
    )

    val grades = listOf("10 класс", "11 класс", "Выпускник колледжа")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner
        GlassCard(
            borderColor = DarkBorderHighlight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Профиль абитуриента ЕНТ",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Персональный трек подготовки к гранту РК",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(VividOrange.copy(alpha = 0.2f))
                        .border(1.dp, VividOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Академический профиль",
                        tint = VividOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cloud sync bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceElevated)
                    .clickable { onSyncNow() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSyncing) Icons.Default.Refresh else Icons.Default.CloudDone,
                        contentDescription = "Синхронизация",
                        tint = if (isSyncing) ElectricAmber else NeonGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSyncing) "Синхронизация с сервером..." else "Синхронизировано с сервером",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = if (isSyncing) "..." else "Обновить",
                    color = VividOrange,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Form Fields Card
        GlassCard {
            Text(
                text = "1. Данные кандидата",
                color = ElectricAmber,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("ФИО / Имя студента", color = TextMuted) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = VividOrange,
                    unfocusedBorderColor = DarkBorder,
                    cursorColor = VividOrange
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_name_input")
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Класс обучения:",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                grades.forEach { grade ->
                    val isSelected = selectedGrade == grade
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) VividOrange.copy(alpha = 0.25f) else DarkSurfaceElevated)
                            .border(1.dp, if (isSelected) VividOrange else DarkBorder, RoundedCornerShape(10.dp))
                            .clickable { selectedGrade = grade }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = grade,
                            color = if (isSelected) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Subject Pair Selector Card
        GlassCard {
            Text(
                text = "2. Профильная пара предметов ЕНТ",
                color = ElectricAmber,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Выберите комбинацию профильных предметов для расчета проходных баллов",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            subjectPairs.forEach { pair ->
                val isSelected = selectedSubjectPair == pair
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) VividOrange.copy(alpha = 0.2f) else DarkSurfaceElevated)
                        .border(1.dp, if (isSelected) VividOrange else DarkBorder, RoundedCornerShape(12.dp))
                        .clickable { selectedSubjectPair = pair }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSelected) VividOrange else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = pair,
                            color = if (isSelected) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (isSelected) {
                        Text(
                            text = "B057/B058",
                            color = ElectricAmber,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // University & Target Score Card
        GlassCard {
            Text(
                text = "3. Целевой университет и балл ЕНТ",
                color = ElectricAmber,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Целевой вуз РК:",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                universities.forEach { uni ->
                    val isSelected = selectedUniversity == uni
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) CoralRed.copy(alpha = 0.25f) else DarkSurfaceElevated)
                            .border(1.dp, if (isSelected) CoralRed else DarkBorder, RoundedCornerShape(10.dp))
                            .clickable { selectedUniversity = uni }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = null,
                            tint = if (isSelected) CoralRed else TextSubtle,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uni,
                            color = if (isSelected) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Целевой балл ЕНТ:",
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$targetScore / 140",
                    color = VividOrange,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Slider(
                value = targetScore.toFloat(),
                onValueChange = { targetScore = it.toInt() },
                valueRange = 70f..140f,
                steps = 69,
                colors = SliderDefaults.colors(
                    thumbColor = VividOrange,
                    activeTrackColor = VividOrange,
                    inactiveTrackColor = DarkSurfaceElevated
                ),
                modifier = Modifier.testTag("target_score_slider")
            )
        }

        // Save Button
        Button(
            onClick = {
                onSaveProfile(name, selectedGrade, selectedUniversity, targetScore, selectedSubjectPair)
                isSavedNotificationVisible = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = VividOrange),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_profile_button")
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = TextPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Сохранить профиль",
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall
            )
        }

        AnimatedVisibility(visible = isSavedNotificationVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeonGreen.copy(alpha = 0.2f))
                    .border(1.dp, NeonGreen, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓ Профиль успешно обновлен и синхронизирован!",
                    color = NeonGreen,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
