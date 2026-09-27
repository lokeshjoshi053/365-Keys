package com.example.housingroperty.ui.customer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.housingroperty.data.models.*
import com.example.housingroperty.data.repository.CustomerRepository
import com.example.housingroperty.data.repository.CustomerRepositoryProvider
import com.example.housingroperty.ui.components.ExitAppConfirmDialog
import com.example.housingroperty.ui.components.platformExitApp
import com.example.housingroperty.ui.customer.components.CustomerBottomBar
import com.example.housingroperty.ui.customer.components.CustomerTab
import com.example.housingroperty.ui.customer.screens.*
import com.example.housingroperty.ui.screens.EditProfileScreen
import com.example.housingroperty.ui.theme.BrandBluePrimary
import kotlinx.coroutines.launch

sealed interface CustomerSubScreen {
    data object None : CustomerSubScreen
    data class PlotDetails(val plot: Plot) : CustomerSubScreen
    data class PlanSelection(val plot: Plot) : CustomerSubScreen
    data class BookingReview(val plot: Plot, val plan: BookingPlan) : CustomerSubScreen
    data class PaymentCheckout(val plot: Plot, val plan: BookingPlan, val orderId: String) : CustomerSubScreen
    data class PaymentStatus(val amount: String, val txnId: String) : CustomerSubScreen
    data class BookingDetails(val booking: Booking) : CustomerSubScreen
    data class PlotMap(val initialPlot: Plot? = null) : CustomerSubScreen
    data object PaymentHistory : CustomerSubScreen
    data object ReferredBy : CustomerSubScreen
    data object EditProfile : CustomerSubScreen
}

@Composable
fun CustomerRoot(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenAgentDashboard: () -> Unit = {},
    onOpenAdminDashboard: (() -> Unit)? = null,
    onExit: () -> Unit = { platformExitApp() },
    repository: CustomerRepository = CustomerRepositoryProvider.instance
) {
    var currentTab by rememberSaveable { mutableStateOf(CustomerTab.HOME) }
    var subScreen by remember { mutableStateOf<CustomerSubScreen>(CustomerSubScreen.None) }

    val coroutineScope = rememberCoroutineScope()

    var profile by remember { mutableStateOf(UserProfile()) }
    var plots by remember { mutableStateOf(emptyList<Plot>()) }
    var plans by remember { mutableStateOf(emptyList<BookingPlan>()) }
    var activeBooking by remember { mutableStateOf<Booking?>(null) }
    var documents by remember { mutableStateOf(emptyList<DocumentItem>()) }
    var payments by remember { mutableStateOf(emptyList<PaymentItem>()) }
    var notifications by remember { mutableStateOf(emptyList<NotificationItem>()) }
    var referrerInfo by remember { mutableStateOf(ReferrerInfo()) }

    LaunchedEffect(repository) {
        profile = repository.getProfile()
        plots = repository.getPlots()
        plans = repository.getPlans()
        activeBooking = repository.getActiveBooking()
        documents = repository.getDocuments()
        payments = repository.getPayments()
        notifications = repository.getNotifications()
        referrerInfo = repository.getReferrerInfo()
    }

    var showSupportNotice by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val handleBack: () -> Unit = {
        when (val active = subScreen) {
            is CustomerSubScreen.PlotDetails -> subScreen = CustomerSubScreen.None
            is CustomerSubScreen.PlanSelection -> subScreen = CustomerSubScreen.PlotDetails(active.plot)
            is CustomerSubScreen.BookingReview -> subScreen = CustomerSubScreen.PlanSelection(active.plot)
            is CustomerSubScreen.PaymentCheckout -> subScreen = CustomerSubScreen.BookingReview(active.plot, active.plan)
            is CustomerSubScreen.PaymentStatus -> {
                subScreen = CustomerSubScreen.None
                currentTab = CustomerTab.HOME
            }
            is CustomerSubScreen.BookingDetails -> subScreen = CustomerSubScreen.None
            is CustomerSubScreen.PlotMap -> {
                if (active.initialPlot != null) {
                    subScreen = CustomerSubScreen.PlotDetails(active.initialPlot)
                } else {
                    subScreen = CustomerSubScreen.None
                }
            }
            is CustomerSubScreen.PaymentHistory -> subScreen = CustomerSubScreen.None
            is CustomerSubScreen.ReferredBy -> subScreen = CustomerSubScreen.None
            is CustomerSubScreen.EditProfile -> subScreen = CustomerSubScreen.None
            CustomerSubScreen.None -> {
                if (currentTab != CustomerTab.HOME) {
                    currentTab = CustomerTab.HOME
                } else {
                    showExitDialog = true
                }
            }
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
            if (subScreen == CustomerSubScreen.None) {
                CustomerBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        subScreen = CustomerSubScreen.None
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
            ) { activeSubScreen ->
                when (activeSubScreen) {
                    is CustomerSubScreen.PlotDetails -> {
                        PlotDetailsScreen(
                            plot = activeSubScreen.plot,
                            onBack = handleBack,
                            onViewOnMap = {
                                subScreen = CustomerSubScreen.PlotMap(activeSubScreen.plot)
                            },
                            onSelectPlan = {
                                subScreen = CustomerSubScreen.PlanSelection(activeSubScreen.plot)
                            }
                        )
                    }

                    is CustomerSubScreen.PlanSelection -> {
                        PlanSelectionScreen(
                            plans = plans,
                            onBack = handleBack,
                            onPlanSelected = { plan ->
                                subScreen = CustomerSubScreen.BookingReview(activeSubScreen.plot, plan)
                            }
                        )
                    }

                    is CustomerSubScreen.BookingReview -> {
                        BookingReviewScreen(
                            plot = activeSubScreen.plot,
                            plan = activeSubScreen.plan,
                            onBack = handleBack,
                            onProceedToPayment = {
                                subScreen = CustomerSubScreen.PaymentCheckout(
                                    plot = activeSubScreen.plot,
                                    plan = activeSubScreen.plan,
                                    orderId = "#315 - 88213"
                                )
                            }
                        )
                    }

                    is CustomerSubScreen.PaymentCheckout -> {
                        PaymentCheckoutScreen(
                            amountFormatted = activeSubScreen.plan.amountFormatted,
                            orderId = activeSubScreen.orderId,
                            onBack = handleBack,
                            onPaySuccess = {
                                coroutineScope.launch {
                                    val newBooking = repository.createBooking(
                                        activeSubScreen.plot.id,
                                        activeSubScreen.plan.id
                                    )
                                    activeBooking = newBooking
                                    subScreen = CustomerSubScreen.PaymentStatus(
                                        amount = activeSubScreen.plan.amountFormatted,
                                        txnId = newBooking.txnId
                                    )
                                }
                            }
                        )
                    }

                    is CustomerSubScreen.PaymentStatus -> {
                        PaymentStatusScreen(
                            amountFormatted = activeSubScreen.amount,
                            txnId = activeSubScreen.txnId,
                            onBack = handleBack,
                            onViewBooking = {
                                activeBooking?.let {
                                    subScreen = CustomerSubScreen.BookingDetails(it)
                                } ?: run {
                                    subScreen = CustomerSubScreen.None
                                    currentTab = CustomerTab.HOME
                                }
                            }
                        )
                    }

                    is CustomerSubScreen.BookingDetails -> {
                        BookingDetailsScreen(
                            booking = activeSubScreen.booking,
                            onBack = handleBack
                        )
                    }

                    is CustomerSubScreen.PlotMap -> {
                        PlotMapScreen(
                            plots = plots,
                            initialSelectedPlot = activeSubScreen.initialPlot,
                            onBack = handleBack,
                            onSelectPlan = { plot ->
                                subScreen = CustomerSubScreen.PlanSelection(plot)
                            }
                        )
                    }

                    is CustomerSubScreen.PaymentHistory -> {
                        PaymentHistoryScreen(
                            payments = payments,
                            onBack = handleBack
                        )
                    }

                    is CustomerSubScreen.ReferredBy -> {
                        ReferredByScreen(
                            referrer = referrerInfo,
                            onBack = handleBack
                        )
                    }

                    is CustomerSubScreen.EditProfile -> {
                        EditProfileScreen(
                            title = "Edit Customer Profile",
                            initialName = profile.fullName,
                            initialEmail = profile.email,
                            initialPhone = profile.phone,
                            roleTitle = profile.roleTitle,
                            extraFieldLabel = "Nominee Name",
                            extraFieldValue = profile.nomineeName,
                            onSave = { newName, newEmail, newPhone, newNominee ->
                                coroutineScope.launch {
                                    val updated = profile.copy(
                                        fullName = newName,
                                        email = newEmail,
                                        phone = newPhone,
                                        nomineeName = newNominee
                                    )
                                    profile = updated
                                    repository.saveProfile(newName, newEmail, newNominee, newPhone)
                                    subScreen = CustomerSubScreen.None
                                }
                            },
                            onBack = handleBack
                        )
                    }

                    CustomerSubScreen.None -> {
                        // Render Main Tab
                        when (currentTab) {
                            CustomerTab.HOME -> {
                                CustomerHomeScreen(
                                    userName = profile.fullName.split(" ").firstOrNull() ?: "Rohan",
                                    activeBooking = activeBooking,
                                    onNotificationsClick = { currentTab = CustomerTab.ALERTS },
                                    onBrowsePlotsClick = { currentTab = CustomerTab.PLOTS },
                                    onDocumentsClick = { currentTab = CustomerTab.DOCS },
                                    onPaymentsClick = { subScreen = CustomerSubScreen.PaymentHistory },
                                    onBookingCardClick = {
                                        activeBooking?.let {
                                            subScreen = CustomerSubScreen.BookingDetails(it)
                                        }
                                    },
                                    onOpenAdminDashboard = onOpenAdminDashboard
                                )
                            }

                            CustomerTab.PLOTS -> {
                                PlotListScreen(
                                    plots = plots,
                                    onPlotClick = { plot ->
                                        subScreen = CustomerSubScreen.PlotDetails(plot)
                                    },
                                    onBack = { currentTab = CustomerTab.HOME }
                                )
                            }

                            CustomerTab.DOCS -> {
                                DocumentCentreScreen(
                                    documents = documents,
                                    onBack = { currentTab = CustomerTab.HOME }
                                )
                            }

                            CustomerTab.ALERTS -> {
                                NotificationsScreen(
                                    notifications = notifications,
                                    onBack = { currentTab = CustomerTab.HOME }
                                )
                            }

                            CustomerTab.PROFILE -> {
                                ProfileScreen(
                                    profile = profile,
                                    onBack = { currentTab = CustomerTab.HOME },
                                    onEditProfile = {
                                        subScreen = CustomerSubScreen.EditProfile
                                    },
                                    onSecurity = {
                                        showSecurityDialog = true
                                    },
                                    onSupport = { showSupportNotice = true },
                                    onReferredBy = { subScreen = CustomerSubScreen.ReferredBy },
                                    onLogout = onLogout
                                )
                            }
                        }
                    }
                }
            }
        }

        // Security Info Dialog
        if (showSecurityDialog) {
            AlertDialog(
                onDismissRequest = { showSecurityDialog = false },
                confirmButton = {
                    TextButton(onClick = { showSecurityDialog = false }) {
                        Text("Done", color = BrandBluePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Account Security", fontWeight = FontWeight.Bold) },
                text = {
                    Text("• Two-Factor Authentication: Enabled via Mobile OTP\n• Biometric Lock: Device Default\n• Active Sessions: Current Device\n• Data Encryption: 256-bit AES at rest")
                }
            )
        }

        // Support Dialog
        if (showSupportNotice) {
            AlertDialog(
                onDismissRequest = { showSupportNotice = false },
                confirmButton = {
                    TextButton(onClick = { showSupportNotice = false }) {
                        Text("Close", color = BrandBluePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("315 PLOTS Support", fontWeight = FontWeight.Bold) },
                text = {
                    Text("Helpline: +91 8000 315 315\nEmail: support@315plots.com\nHours: Mon-Sat, 9 AM - 7 PM")
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
