package com.example.housingroperty.ui.admin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.housingroperty.data.models.*
import com.example.housingroperty.data.repository.AdminRepository
import com.example.housingroperty.data.repository.AdminRepositoryProvider
import com.example.housingroperty.data.repository.CustomerRepository
import com.example.housingroperty.data.repository.CustomerRepositoryProvider
import com.example.housingroperty.ui.admin.components.AdminBottomBar
import com.example.housingroperty.ui.admin.components.AdminTab
import com.example.housingroperty.ui.admin.screens.*
import com.example.housingroperty.ui.components.ExitAppConfirmDialog
import com.example.housingroperty.ui.components.platformExitApp
import com.example.housingroperty.ui.screens.EditProfileScreen
import kotlinx.coroutines.launch

@Composable
fun AdminRoot(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenCustomerView: () -> Unit = {},
    onOpenAgentView: () -> Unit = {},
    onExit: () -> Unit = { platformExitApp() },
    adminRepository: AdminRepository = AdminRepositoryProvider.instance,
    customerRepository: CustomerRepository = CustomerRepositoryProvider.instance
) {
    var currentTab by rememberSaveable { mutableStateOf(AdminTab.OVERVIEW) }
    val coroutineScope = rememberCoroutineScope()

    var stats by remember { mutableStateOf(AdminOverviewStats()) }
    var approvals by remember { mutableStateOf(emptyList<AdminApprovalItem>()) }
    var payouts by remember { mutableStateOf(emptyList<AdminPayoutRequest>()) }
    var activities by remember { mutableStateOf(emptyList<AdminActivityLog>()) }
    var plots by remember { mutableStateOf(emptyList<Plot>()) }

    LaunchedEffect(adminRepository, customerRepository) {
        stats = adminRepository.getOverviewStats()
        approvals = adminRepository.getApprovals()
        payouts = adminRepository.getPayoutRequests()
        activities = adminRepository.getActivityLogs()
        plots = customerRepository.getPlots()
    }

    var showExitDialog by remember { mutableStateOf(false) }

    var adminName by rememberSaveable { mutableStateOf("System Administrator") }
    var adminEmail by rememberSaveable { mutableStateOf("superadmin@315plots.com") }
    var adminPhone by rememberSaveable { mutableStateOf("+91 90000 00001") }
    var adminDept by rememberSaveable { mutableStateOf("Platform Operations & Compliance") }
    var isEditingProfile by rememberSaveable { mutableStateOf(false) }

    val handleBack: () -> Unit = {
        if (isEditingProfile) {
            isEditingProfile = false
        } else if (currentTab != AdminTab.OVERVIEW) {
            currentTab = AdminTab.OVERVIEW
        } else {
            showExitDialog = true
        }
    }

    com.example.housingroperty.ui.components.BackHandler(enabled = true) {
        handleBack()
    }

    val pendingCount = approvals.count { it.status == "Pending" }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (!isEditingProfile) {
                AdminBottomBar(
                    currentTab = currentTab,
                    pendingApprovalsCount = pendingCount,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            if (isEditingProfile) {
                EditProfileScreen(
                    title = "Edit Admin Profile",
                    initialName = adminName,
                    initialEmail = adminEmail,
                    initialPhone = adminPhone,
                    roleTitle = "Super Admin",
                    extraFieldLabel = "Department / Designation",
                    extraFieldValue = adminDept,
                    onBack = { isEditingProfile = false },
                    onSave = { updatedName, updatedEmail, updatedPhone, updatedDept ->
                        adminName = updatedName
                        adminEmail = updatedEmail
                        adminPhone = updatedPhone
                        if (updatedDept != null) {
                            adminDept = updatedDept
                        }
                        isEditingProfile = false
                    }
                )
            } else {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                    modifier = Modifier.fillMaxSize()
                ) { tab ->
                    when (tab) {
                        AdminTab.OVERVIEW -> {
                            AdminDashboardScreen(
                                stats = stats,
                                activities = activities,
                                onNavigateToPlots = { currentTab = AdminTab.PLOTS },
                                onNavigateToApprovals = { currentTab = AdminTab.APPROVALS },
                                onNavigateToPayouts = { currentTab = AdminTab.PAYOUTS }
                            )
                        }

                        AdminTab.PLOTS -> {
                            AdminPlotsScreen(
                                plots = plots,
                                onTogglePlotStatus = { plot ->
                                    val newStatus = if (plot.status == PlotStatus.AVAILABLE) PlotStatus.SOLD else PlotStatus.AVAILABLE
                                    val updatedList = plots.map {
                                        if (it.id == plot.id) it.copy(status = newStatus) else it
                                    }
                                    plots = updatedList
                                    val soldCount = updatedList.count { it.status == PlotStatus.SOLD }
                                    stats = stats.copy(
                                        plotsSold = soldCount,
                                        plotsAvailable = updatedList.size - soldCount
                                    )
                                },
                                onDeletePlot = { plot ->
                                    val updatedList = plots.filter { it.id != plot.id }
                                    plots = updatedList
                                    val soldCount = updatedList.count { it.status == PlotStatus.SOLD }
                                    stats = stats.copy(
                                        totalPlots = updatedList.size,
                                        plotsSold = soldCount,
                                        plotsAvailable = updatedList.size - soldCount
                                    )
                                },
                                onAddPlot = { newPlot ->
                                    val updatedList = listOf(newPlot) + plots
                                    plots = updatedList
                                    val soldCount = updatedList.count { it.status == PlotStatus.SOLD }
                                    stats = stats.copy(
                                        totalPlots = updatedList.size,
                                        plotsSold = soldCount,
                                        plotsAvailable = updatedList.size - soldCount
                                    )
                                },
                                onUpdatePrice = { plot, newPrice ->
                                    val updatedList = plots.map {
                                        if (it.id == plot.id) it.copy(totalPriceFormatted = newPrice) else it
                                    }
                                    plots = updatedList
                                },
                                onBack = { currentTab = AdminTab.OVERVIEW }
                            )
                        }

                        AdminTab.APPROVALS -> {
                            AdminApprovalsScreen(
                                approvals = approvals,
                                onApprove = { id ->
                                    coroutineScope.launch {
                                        adminRepository.approveItem(id)
                                        approvals = adminRepository.getApprovals()
                                        stats = adminRepository.getOverviewStats()
                                    }
                                },
                                onReject = { id ->
                                    coroutineScope.launch {
                                        adminRepository.rejectItem(id)
                                        approvals = adminRepository.getApprovals()
                                        stats = adminRepository.getOverviewStats()
                                    }
                                },
                                onBack = { currentTab = AdminTab.OVERVIEW }
                            )
                        }

                        AdminTab.PAYOUTS -> {
                            AdminPayoutsScreen(
                                payouts = payouts,
                                onProcessPayout = { id ->
                                    coroutineScope.launch {
                                        adminRepository.processPayout(id)
                                        payouts = adminRepository.getPayoutRequests()
                                    }
                                },
                                onBack = { currentTab = AdminTab.OVERVIEW }
                            )
                        }

                        AdminTab.SETTINGS -> {
                            AdminProfileScreen(
                                adminName = adminName,
                                adminEmail = adminEmail,
                                onEditProfile = { isEditingProfile = true },
                                onLogout = onLogout,
                                onBack = { currentTab = AdminTab.OVERVIEW }
                            )
                        }
                    }
                }
            }
        }

        // Exit Dialog on Admin Home
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
