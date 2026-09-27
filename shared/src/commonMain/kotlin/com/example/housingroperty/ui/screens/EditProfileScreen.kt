package com.example.housingroperty.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.theme.*

@Composable
fun EditProfileScreen(
    title: String = "Edit Profile",
    initialName: String,
    initialEmail: String,
    initialPhone: String = "+91 98765 43210",
    roleTitle: String,
    extraFieldLabel: String? = null,
    extraFieldValue: String? = null,
    onSave: (name: String, email: String, phone: String, extra: String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone) }
    var extra by remember { mutableStateOf(extraFieldValue ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .imePadding()
    ) {
        val isLandscape = maxWidth > maxHeight || maxWidth > 650.dp

        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = title,
                subtitle = "Manage account · $roleTitle",
                onBackClick = onBack
            )

            // Optional Toast
            toastMessage?.let { msg ->
                AppToastBanner(
                    message = msg,
                    type = ToastType.SUCCESS,
                    onDismiss = { toastMessage = null }
                )
            }

            if (isLandscape) {
                // ==========================================
                // LANDSCAPE: Side-by-side Avatar & Form
                // ==========================================
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Left Column: Avatar & Summary
                    Column(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ProfileAvatarView(name = name, roleTitle = roleTitle)

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = name.ifBlank { "User Profile" },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        PillBadge(
                            text = roleTitle,
                            backgroundColor = Color(0xFFEFF6FF),
                            textColor = BrandBluePrimary
                        )
                    }

                    // Right Column: Form Inputs & Save Action
                    Column(
                        modifier = Modifier
                            .weight(1.3f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        ProfileFormField(
                            label = "Full Name",
                            value = name,
                            onValueChange = { name = it; errorMessage = null }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileFormField(
                            label = "Email Address",
                            value = email,
                            keyboardType = KeyboardType.Email,
                            onValueChange = { email = it; errorMessage = null }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileFormField(
                            label = "Mobile Number",
                            value = phone,
                            keyboardType = KeyboardType.Phone,
                            onValueChange = { phone = it; errorMessage = null }
                        )

                        if (extraFieldLabel != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            ProfileFormField(
                                label = extraFieldLabel,
                                value = extra,
                                onValueChange = { extra = it }
                            )
                        }

                        errorMessage?.let { err ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = err,
                                fontSize = 12.sp,
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                onClick = onBack,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Cancel",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            PrimaryButton(
                                text = "Save Changes",
                                onClick = {
                                    if (name.isBlank()) {
                                        errorMessage = "Please enter your name"
                                        return@PrimaryButton
                                    }
                                    if (email.isBlank()) {
                                        errorMessage = "Please enter your email"
                                        return@PrimaryButton
                                    }
                                    onSave(name.trim(), email.trim(), phone.trim(), extra.trim().ifBlank { null })
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )
                        }
                    }
                }
            } else {
                // ==========================================
                // PORTRAIT: Vertical Stacked Form
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileAvatarView(name = name, roleTitle = roleTitle)

                    Spacer(modifier = Modifier.height(12.dp))

                    PillBadge(
                        text = roleTitle,
                        backgroundColor = Color(0xFFEFF6FF),
                        textColor = BrandBluePrimary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    ProfileFormField(
                        label = "Full Name",
                        value = name,
                        onValueChange = { name = it; errorMessage = null }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ProfileFormField(
                        label = "Email Address",
                        value = email,
                        keyboardType = KeyboardType.Email,
                        onValueChange = { email = it; errorMessage = null }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ProfileFormField(
                        label = "Mobile Number",
                        value = phone,
                        keyboardType = KeyboardType.Phone,
                        onValueChange = { phone = it; errorMessage = null }
                    )

                    if (extraFieldLabel != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileFormField(
                            label = extraFieldLabel,
                            value = extra,
                            onValueChange = { extra = it }
                        )
                    }

                    errorMessage?.let { err ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = err,
                            fontSize = 12.5.sp,
                            color = Color(0xFFDC2626),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    PrimaryButton(
                        text = "Save Changes",
                        onClick = {
                            if (name.isBlank()) {
                                errorMessage = "Please enter your name"
                                return@PrimaryButton
                            }
                            if (email.isBlank()) {
                                errorMessage = "Please enter your email"
                                return@PrimaryButton
                            }
                            onSave(name.trim(), email.trim(), phone.trim(), extra.trim().ifBlank { null })
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatarView(name: String, roleTitle: String) {
    val initials = name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
        .ifBlank { "U" }

    Box(
        modifier = Modifier
            .size(86.dp)
            .shadow(elevation = 6.dp, shape = CircleShape, spotColor = BrandBluePrimary.copy(alpha = 0.35f))
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(BrandBluePrimary, Color(0xFF1443B8))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun ProfileFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF334155)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = TextStyle(
                    fontSize = 14.5.sp,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(BrandBluePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            )
        }
    }
}
