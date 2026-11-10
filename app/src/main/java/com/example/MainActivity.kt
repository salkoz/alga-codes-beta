package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.AmbientAuraBackground
import com.example.ui.components.IPhoneHeaderBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlgaCodesTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    AmbientAuraBackground {
        // iPhone 16 Pro Mockup Frame wrapper
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            // High-end iPhone 16 Pro dark titanium frame container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(32.dp))
                    .background(DarkBackground.copy(alpha = 0.95f))
                    .border(2.dp, Color(0xFF27272A), RoundedCornerShape(32.dp))
            ) {
                Scaffold(
                    containerColor = Color.Transparent,
                    topBar = {
                        IPhoneHeaderBar(
                            isSynced = uiState.userProfile.isCloudSynced,
                            isSyncing = uiState.isSyncing,
                            onSyncClick = { viewModel.triggerCloudSync() }
                        )
                    },
                    bottomBar = {
                        AlgaBottomNavBar(
                            currentTab = uiState.currentTab,
                            onTabSelected = { viewModel.selectTab(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentTab) {
                            0 -> OnboardingProfileScreen(
                                userProfile = uiState.userProfile,
                                isSyncing = uiState.isSyncing,
                                onSaveProfile = { name, grade, uni, score, pair ->
                                    viewModel.updateProfile(name, grade, uni, score, pair)
                                },
                                onSyncNow = { viewModel.triggerCloudSync() }
                            )
                            1 -> DashboardAnalyticsScreen(
                                userProfile = uiState.userProfile,
                                monthlyStats = uiState.monthlyStats,
                                quests = uiState.quests,
                                showBreakdownDialog = uiState.showScoreBreakdownDialog,
                                onToggleBreakdownDialog = { viewModel.toggleScoreBreakdownDialog(it) },
                                onClaimQuest = { viewModel.claimQuestReward(it) },
                                onNavigateToCalculator = { viewModel.selectTab(2) },
                                onNavigateToIde = { viewModel.selectTab(3) }
                            )
                            2 -> GrantCalculatorScreen(
                                calculatorState = uiState.calculatorState,
                                onScoreChange = { viewModel.updateCalculatorScore(it) },
                                onToggleRuralQuota = { viewModel.toggleRuralQuota(it) },
                                onToggleAltynBelgi = { viewModel.toggleAltynBelgi(it) },
                                onToggleOlympiadWinner = { viewModel.toggleOlympiadWinner(it) },
                                onSelectGroupCode = { viewModel.setGroupCodeFilter(it) },
                                onSelectCity = { viewModel.setCityFilter(it) }
                            )
                            3 -> CodeTrainerScreen(
                                ideState = uiState.ideState,
                                onLanguageChange = { viewModel.selectCodeLanguage(it) },
                                onTaskSelect = { viewModel.selectTask(it) },
                                onCodeChange = { viewModel.updateCurrentCode(it) },
                                onResetCode = { viewModel.resetCode() },
                                onExecuteCode = { viewModel.executeCode() }
                            )
                            4 -> AiAgentsHubScreen(
                                selectedMentor = uiState.selectedMentor,
                                chatMessages = uiState.chatMessages,
                                isTyping = uiState.isMentorTyping,
                                onSelectMentor = { viewModel.selectMentor(it) },
                                onSendMessage = { viewModel.sendMentorMessage(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlgaBottomNavBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xEB121215), // translucent dark glass
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        val items = listOf(
            NavigationItem("Профиль", Icons.Default.Person, Icons.Outlined.Person),
            NavigationItem("Дашборд", Icons.Default.Analytics, Icons.Outlined.Analytics),
            NavigationItem("Гранты", Icons.Default.Calculate, Icons.Outlined.Calculate),
            NavigationItem("Код IDE", Icons.Default.Code, Icons.Outlined.Code),
            NavigationItem("AI Ментор", Icons.Default.SmartToy, Icons.Outlined.SmartToy)
        )

        items.forEachIndexed { index, item ->
            val isSelected = currentTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) VividOrange else TextMuted
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) VividOrange else TextMuted,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = VividOrange.copy(alpha = 0.2f),
                    selectedIconColor = VividOrange,
                    unselectedIconColor = TextMuted,
                    selectedTextColor = VividOrange,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_tab_$index")
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
)
