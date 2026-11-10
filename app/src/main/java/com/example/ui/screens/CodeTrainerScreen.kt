package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockDataSource
import com.example.model.CodeTask
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.viewmodel.IdeState

@Composable
fun CodeTrainerScreen(
    ideState: IdeState,
    onLanguageChange: (String) -> Unit,
    onTaskSelect: (CodeTask) -> Unit,
    onCodeChange: (String) -> Unit,
    onResetCode: () -> Unit,
    onExecuteCode: () -> Unit
) {
    val context = LocalContext.current
    val tasks = MockDataSource.codeTasks
    var selectedDifficultyFilter by remember { mutableStateOf("All") }

    val filteredTasks = remember(selectedDifficultyFilter) {
        if (selectedDifficultyFilter == "All") tasks else tasks.filter { it.difficulty == selectedDifficultyFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Top Section & Language Switcher ---
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
                        text = "Интерактивный IDE тренажер",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Практикум ЕНТ: Python & JavaScript",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Language Toggle (Python / JS)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                ) {
                    val isPy = ideState.selectedLanguage == "Python"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isPy) VividOrange else Color.Transparent)
                            .clickable { onLanguageChange("Python") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Python",
                            color = if (isPy) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (!isPy) ElectricAmber else Color.Transparent)
                            .clickable { onLanguageChange("JavaScript") }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "JS",
                            color = if (!isPy) DarkBackground else TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Task info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = ideState.selectedTask.title,
                        color = ElectricAmber,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "${ideState.selectedTask.category} • Сложность: ${ideState.selectedTask.difficulty}",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VividOrange.copy(alpha = 0.2f))
                        .border(0.5.dp, VividOrange, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${ideState.selectedTask.xpReward} XP",
                        color = VividOrange,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = ideState.selectedTask.description,
                color = TextPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // --- 2. Task Selector Catalog Tabs ---
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tasks.forEach { task ->
                val isSelected = task.id == ideState.selectedTask.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) VividOrange.copy(alpha = 0.25f) else DarkSurfaceElevated)
                        .border(1.dp, if (isSelected) VividOrange else DarkBorder, RoundedCornerShape(10.dp))
                        .clickable { onTaskSelect(task) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (task.completed) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = task.title,
                            color = if (isSelected) TextPrimary else TextMuted,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // --- 3. Syntax-Highlighted Code Editor Frame ---
        GlassCard(
            borderColor = DarkBorder
        ) {
            // IDE Window Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Window Mac/iOS dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(10.dp).background(CoralRed, CircleShape))
                    Box(modifier = Modifier.size(10.dp).background(ElectricAmber, CircleShape))
                    Box(modifier = Modifier.size(10.dp).background(NeonGreen, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (ideState.selectedLanguage == "Python") "main.py" else "solution.js",
                        color = TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Copy and Reset buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("code", ideState.currentCode)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Код скопирован в буфер", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Копировать", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onResetCode,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Сброс", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Code Editor Area with Line Numbers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp, max = 280.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0D10))
                    .border(0.5.dp, DarkBorder, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                // Line numbers
                val lines = ideState.currentCode.lines()
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    lines.indices.forEach { index ->
                        Text(
                            text = "${index + 1}",
                            color = Color(0xFF52525B),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Code Input text field
                OutlinedTextField(
                    value = ideState.currentCode,
                    onValueChange = onCodeChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("code_editor_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = VividOrange
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Run Button
            Button(
                onClick = onExecuteCode,
                enabled = !ideState.isRunning,
                colors = ButtonDefaults.buttonColors(containerColor = VividOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("run_code_button")
            ) {
                if (ideState.isRunning) {
                    CircularProgressIndicator(
                        color = TextPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Выполнение тестов ЕНТ...", color = TextPrimary)
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Запустить код & Пройти тесты", color = TextPrimary, style = MaterialTheme.typography.titleSmall)
                }
            }
        }

        // --- 4. Interactive Terminal Console Output ---
        GlassCard(
            borderColor = if (ideState.executionSuccess == true) NeonGreen.copy(alpha = 0.5f) else DarkBorder
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💻", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Консоль вывода (Runtime Console)",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                if (ideState.executionSuccess == true) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("3/3 PASSED", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF08080A))
                    .border(0.5.dp, DarkBorder, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = ideState.consoleOutput,
                    color = if (ideState.executionSuccess == true) NeonGreen else TextMuted,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
