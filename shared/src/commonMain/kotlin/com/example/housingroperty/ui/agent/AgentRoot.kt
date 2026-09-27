package com.example.housingroperty.ui.agent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.housingroperty.data.models.*
import com.example.housingroperty.data.repository.AgentRepository
import com.example.housingroperty.data.repository.AgentRepositoryProvider
import com.example.housingroperty.ui.agent.components.AgentBottomBar
import com.example.housingroperty.ui.agent.components.AgentTab
import com.example.housingroperty.ui.agent.screens.*
import com.example.housingroperty.ui.components.ExitAppConfirmDialog
import com.example.housingroperty.ui.screens.EditProfileScreen
import com.example.housingroperty.ui.components.platformExitApp
import com.example.housingroperty.ui.theme.BrandBluePrimary
import kotlinx.coroutines.launch

sealed interface AgentSubScreen {
    data object None : AgentSubScreen
    data object ReferralCode : AgentSubScreen
    data object RegisterLead : AgentSubScreen
    data object ReferralTree : AgentSubScreen
    data class CommissionDetail(val item: CommissionItem) : AgentSubScreen
    data object MatchingDetail : AgentSubScreen
    data object Payout : AgentSubScreen
    data object Notifications : AgentSubScreen
    data object EditProfile : AgentSubScreen
}

@Composable
fun AgentRoot(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenCustomerHome: () -> Unit = {},
    onOpenAdminDashboard: (() -> Unit)? = null,
    onExit: () -> Unit = { platformExitApp() },
    repository: AgentRepository = AgentRepositoryProvider.instance
) {
    var currentTab by rememberSaveable { mutableStateOf(AgentTab.HOME) }
    var subScreen by remember { mutableStateOf<AgentSubScreen>(AgentSubScreen.None) }

    val coroutineScope = rememberCoroutineScope()

    var profile by remember { mutableStateOf(PartnerProfile()) }
    var stats by remember { mutableStateOf(DashboardStats()) }
    var leads by remember { mutableStateOf(emptyList<Lead>()) }
    var networkLevels by remember { mutableStateOf(emptyList<NetworkLevel>()) }
    var referralTree by remember {
        mutableStateOf(
            TreeNode(
                id = "root",
                name = "Loading...",
                subtitle = "Agent",
                badgeText = "",
                level = 0
            )
        )
    }
    var commissions by remember { mutableStateOf(emptyList<CommissionItem>()) }
    var matchingSummary by remember { mutableStateOf(MatchingSummary()) }
    var payoutInfo by remember { mutableStateOf(PayoutInfo()) }
    var notifications by remember { mutableStateOf(emptyList<AgentNotification>()) }

    LaunchedEffect(repository) {
        profile = repository.getProfile()
        stats = repository.getDashboardStats()
        leads = repository.getLeads()
        networkLevels = repository.getNetworkLevels()
        referralTree = repository.getReferralTree()
        commissions = repository.getCommissions()
        matchingSummary = repository.getMatchingSummary()
        payoutInfo = repository.getPayoutInfo()
        notifications = repository.getNotifications()
    }

    var showExitDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }

    val handleBack: () -> Unit = {
        if (subScreen != AgentSubScreen.None) {
            subScreen = AgentSubScreen.None
        } else if (currentTab != AgentTab.HOME) {
            currentTab = AgentTab.HOME
        } else {
            showExitDialog = true
        }
    }

    com.example.housingroperty.ui.components.BackHandler(enabled = true) {
        handleBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (subScreen == AgentSubScreen.None) {
                AgentBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        subScreen = AgentSubScreen.None
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            AnimatedContent(
                targetState = subScreen,
                transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                modifier = Modifier.fillMaxSize()
            ) { activeSub ->
                when (activeSub) {
                    is AgentSubScreen.ReferralCode -> {
                        ReferralCodeScreen(
                            agentCode = profile.agentCode,
                            referralLink = profile.referralLink,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.RegisterLead -> {
                        RegisterLeadScreen(
                            onBack = handleBack,
                            onCreateLead = { name, phone, plot ->
                                coroutineScope.launch {
                                    repository.registerLead(name, phone, plot)
                                    leads = repository.getLeads()
                                    subScreen = AgentSubScreen.None
                                    currentTab = AgentTab.LEADS
                                }
                            }
                        )
                    }

                    is AgentSubScreen.ReferralTree -> {
                        ReferralTreeScreen(
                            rootNode = referralTree,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.CommissionDetail -> {
                        CommissionDetailScreen(
                            commission = activeSub.item,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.MatchingDetail -> {
                        MatchingDetailScreen(
                            matching = matchingSummary,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.Payout -> {
                        PayoutScreen(
                            payoutInfo = payoutInfo,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.Notifications -> {
                        AgentNotificationsScreen(
                            notifications = notifications,
                            onBack = handleBack
                        )
                    }

                    is AgentSubScreen.EditProfile -> {
                        EditProfileScreen(
                            title = "Edit Partner Profile",
                            initialName = profile.fullName,
                            initialEmail = profile.email,
                            initialPhone = profile.phone,
                            roleTitle = profile.roleTitle,
                            extraFieldLabel = "Bank Account / UPI",
                            extraFieldValue = profile.bankAccount,
                            onSave = { newName, newEmail, newPhone, newBank ->
                                coroutineScope.launch {
                                    val updated = profile.copy(
                                        fullName = newName,
                                        email = newEmail,
                                        phone = newPhone,
                                        bankAccount = newBank
                                    )
                                    profile = updated
                                    repository.updateProfile(newName, newEmail, newPhone, newBank)
                                    subScreen = AgentSubScreen.None
                                }
                            },
                            onBack = handleBack
                        )
                    }

                    AgentSubScreen.None -> {
                        when (currentTab) {
                            AgentTab.HOME -> {
                                PartnerDashboardScreen(
                                    stats = stats,
                                    onNotificationsClick = { subScreen = AgentSubScreen.Notifications },
                                    onShareReferralClick = { subScreen = AgentSubScreen.ReferralCode },
                                    onRegisterLeadClick = { subScreen = AgentSubScreen.RegisterLead },
                                    onEarningsClick = { currentTab = AgentTab.EARNINGS },
                                    onMatchingClick = { subScreen = AgentSubScreen.MatchingDetail },
                                    onPayoutClick = { subScreen = AgentSubScreen.Payout }
                                )
                            }

                            AgentTab.LEADS -> {
                                LeadsScreen(
                                    leads = leads,
                                    onAddLeadClick = { subScreen = AgentSubScreen.RegisterLead },
                                    onBack = { currentTab = AgentTab.HOME }
                                )
                            }

                            AgentTab.NETWORK -> {
                                ReferralNetworkScreen(
                                    levels = networkLevels,
                                    onViewTreeClick = { subScreen = AgentSubScreen.ReferralTree },
                                    onBack = { currentTab = AgentTab.HOME }
                                )
                            }

                            AgentTab.EARNINGS -> {
                                CommissionDashboardScreen(
                                    commissions = commissions,
                                    onCommissionClick = { item ->
                                        subScreen = AgentSubScreen.CommissionDetail(item)
                                    },
                                    onBack = { currentTab = AgentTab.HOME }
                                )
                            }

                            AgentTab.PROFILE -> {
                                AgentProfileScreen(
                                    profile = profile,
                                    onEditProfile = {
                                        subScreen = AgentSubScreen.EditProfile
                                    },
                                    onBankDetails = { subScreen = AgentSubScreen.Payout },
                                    onSecurity = { showSecurityDialog = true },
                                    onLogout = onLogout,
                                    onBack = { currentTab = AgentTab.HOME }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Security Info Dialog
        if (showSecurityDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showSecurityDialog = false },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showSecurityDialog = false }) {
                        Text("Done", color = BrandBluePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Partner Security & Access", fontWeight = FontWeight.Bold) },
                text = {
                    Text("• RERA Registered Partner Verification: Active\n• Bank Account Binding: Verified via Penny Drop\n• Two-Factor Authentication: Enabled\n• Active Sessions: Current Device Only")
                }
            )
        }

        // Exit App Confirmation Dialog
        if (showExitDialog) {
            ExitAppConfirmDialog(
                onDismiss = { showExitDialog = false },
                onConfirmExit = {
                    showExitDialog = false
                    onExit()
                }
            )
        }
    }
}
