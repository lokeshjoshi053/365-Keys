package com.example.housingroperty.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.housingroperty.data.repository.AgentRepositoryProvider
import com.example.housingroperty.data.repository.CustomerRepositoryProvider
import com.example.housingroperty.ui.admin.AdminRoot
import com.example.housingroperty.ui.agent.AgentRoot
import com.example.housingroperty.ui.agent.screens.PartnerOnboardingScreen
import com.example.housingroperty.ui.components.BackHandler
import com.example.housingroperty.ui.customer.CustomerRoot
import com.example.housingroperty.ui.customer.screens.ProfileSetupScreen
import com.example.housingroperty.ui.screens.*
import kotlinx.coroutines.launch

enum class AppDestination {
    SPLASH,
    WELCOME,
    ROLE_SELECTION,
    LOGIN,
    OTP_VERIFICATION,
    TERMS,
    SUCCESS,
    PROFILE_SETUP,
    CUSTOMER_PANEL,
    PARTNER_ONBOARDING,
    AGENT_PANEL,
    ADMIN_PANEL
}

@Composable
fun OnboardingFlow(
    modifier: Modifier = Modifier,
    initialDestination: AppDestination = AppDestination.SPLASH,
    onExit: () -> Unit = { com.example.housingroperty.ui.components.platformExitApp() }
) {
    var destinationStackNames by rememberSaveable { mutableStateOf(listOf(initialDestination.name)) }
    var selectedRoleName by rememberSaveable { mutableStateOf(UserRole.CUSTOMER.name) }
    var enteredPhone by rememberSaveable { mutableStateOf("9876543210") }
    val coroutineScope = rememberCoroutineScope()
    val customerRepository = CustomerRepositoryProvider.instance
    val agentRepository = AgentRepositoryProvider.instance

    val selectedRole = remember(selectedRoleName) {
        try { UserRole.valueOf(selectedRoleName) } catch (e: Exception) { UserRole.CUSTOMER }
    }

    val currentDestination = remember(destinationStackNames) {
        val name = destinationStackNames.lastOrNull()
        if (name != null) {
            try { AppDestination.valueOf(name) } catch (e: Exception) { initialDestination }
        } else {
            initialDestination
        }
    }

    fun destinationForRole(role: UserRole): AppDestination = when (role) {
        UserRole.CUSTOMER -> AppDestination.CUSTOMER_PANEL
        UserRole.AGENT -> AppDestination.AGENT_PANEL
        UserRole.ADMIN -> AppDestination.ADMIN_PANEL
    }

    fun navigateTo(dest: AppDestination, clearStack: Boolean = false) {
        destinationStackNames = if (clearStack) {
            listOf(dest.name)
        } else {
            destinationStackNames + dest.name
        }
    }

    fun navigateBack() {
        if (destinationStackNames.size > 1) {
            destinationStackNames = destinationStackNames.dropLast(1)
        }
    }

    val canGoBack = destinationStackNames.size > 1 &&
        currentDestination != AppDestination.SPLASH &&
        currentDestination != AppDestination.WELCOME &&
        currentDestination != AppDestination.CUSTOMER_PANEL &&
        currentDestination != AppDestination.AGENT_PANEL &&
        currentDestination != AppDestination.ADMIN_PANEL

    BackHandler(enabled = canGoBack) {
        navigateBack()
    }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = {
                (slideInHorizontally { width -> width / 3 } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -width / 3 } + fadeOut())
            },
            modifier = Modifier.fillMaxSize()
        ) { destination ->
            when (destination) {
                AppDestination.SPLASH -> {
                    SplashScreen(
                        onNavigateNext = { navigateTo(AppDestination.WELCOME, clearStack = true) },
                        autoAdvance = true
                    )
                }

                AppDestination.WELCOME -> {
                    WelcomeScreen(
                        onGetStarted = { navigateTo(AppDestination.ROLE_SELECTION) },
                        onAlreadyHaveAccount = {
                            selectedRoleName = UserRole.CUSTOMER.name
                            navigateTo(AppDestination.LOGIN)
                        }
                    )
                }

                AppDestination.ROLE_SELECTION -> {
                    RoleSelectionScreen(
                        initialRole = selectedRole,
                        onBack = { navigateBack() },
                        onRoleSelected = { role ->
                            selectedRoleName = role.name
                            navigateTo(AppDestination.LOGIN)
                        },
                        onDirectNavigate = { role ->
                            selectedRoleName = role.name
                            navigateTo(destinationForRole(role), clearStack = true)
                        }
                    )
                }

                AppDestination.LOGIN -> {
                    LoginScreen(
                        role = selectedRole,
                        initialPhoneNumber = enteredPhone,
                        onBack = { navigateBack() },
                        onContinueGoogle = {
                            navigateTo(destinationForRole(selectedRole), clearStack = true)
                        },
                        onSendOtp = { phone ->
                            enteredPhone = phone
                            navigateTo(AppDestination.OTP_VERIFICATION)
                        },
                        onOpenTerms = { navigateTo(AppDestination.TERMS) },
                        onOpenPrivacy = { navigateTo(AppDestination.TERMS) }
                    )
                }

                AppDestination.OTP_VERIFICATION -> {
                    OtpVerificationScreen(
                        phoneNumber = enteredPhone,
                        onBack = { navigateBack() },
                        onVerifySuccess = {
                            when (selectedRole) {
                                UserRole.CUSTOMER -> navigateTo(AppDestination.PROFILE_SETUP)
                                UserRole.AGENT -> navigateTo(AppDestination.PARTNER_ONBOARDING)
                                UserRole.ADMIN -> navigateTo(AppDestination.SUCCESS)
                            }
                        }
                    )
                }

                AppDestination.TERMS -> {
                    TermsAndConditionsScreen(
                        onBack = { navigateBack() },
                        onAccept = { navigateBack() }
                    )
                }

                AppDestination.SUCCESS -> {
                    OnboardingSuccessScreen(
                        role = selectedRole,
                        onRestartFlow = {
                            navigateTo(destinationForRole(selectedRole), clearStack = true)
                        }
                    )
                }

                AppDestination.PROFILE_SETUP -> {
                    ProfileSetupScreen(
                        onBack = { navigateBack() },
                        onSaveSuccess = { name, email ->
                            coroutineScope.launch {
                                customerRepository.saveProfile(name, email, null)
                                navigateTo(destinationForRole(selectedRole), clearStack = true)
                            }
                        }
                    )
                }

                AppDestination.CUSTOMER_PANEL -> {
                    CustomerRoot(
                        onLogout = {
                            navigateTo(AppDestination.ROLE_SELECTION, clearStack = true)
                        },
                        onExit = onExit
                    )
                }

                AppDestination.PARTNER_ONBOARDING -> {
                    PartnerOnboardingScreen(
                        onBack = { navigateBack() },
                        onSubmitSuccess = { name, pan, bank ->
                            coroutineScope.launch {
                                agentRepository.submitPartnerDetails(name, pan, bank)
                                navigateTo(AppDestination.AGENT_PANEL, clearStack = true)
                            }
                        }
                    )
                }

                AppDestination.AGENT_PANEL -> {
                    AgentRoot(
                        onLogout = {
                            navigateTo(AppDestination.ROLE_SELECTION, clearStack = true)
                        },
                        onExit = onExit
                    )
                }

                AppDestination.ADMIN_PANEL -> {
                    AdminRoot(
                        onLogout = {
                            navigateTo(AppDestination.ROLE_SELECTION, clearStack = true)
                        },
                        onExit = onExit
                    )
                }
            }
        }
    }
}
