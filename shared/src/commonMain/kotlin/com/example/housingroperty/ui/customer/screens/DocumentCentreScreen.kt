package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.DocumentItem
import com.example.housingroperty.ui.components.AppToastBanner
import com.example.housingroperty.ui.components.EmptyStateView
import com.example.housingroperty.ui.components.SignAgreementBottomSheet
import com.example.housingroperty.ui.components.ToastType
import com.example.housingroperty.ui.customer.components.DocsNavIcon
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*

@Composable
fun DocumentCentreScreen(
    documents: List<DocumentItem>,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedDoc by remember { mutableStateOf<DocumentItem?>(null) }
    var showSignSheet by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Document Centre",
            subtitle = "${documents.size} verified files",
            onBackClick = onBack
        )

        // Toast feedback (Mockup T1)
        toastMessage?.let { msg ->
            AppToastBanner(
                message = msg,
                type = ToastType.SUCCESS,
                modifier = Modifier.padding(bottom = 12.dp),
                onDismiss = { toastMessage = null }
            )
        }

        if (documents.isEmpty()) {
            EmptyStateView(
                title = "No documents found",
                subtitle = "Your booking agreements and receipts will show up here once generated.",
                icon = { DocsNavIcon(size = 46.dp, color = Color(0xFF1E293B)) }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(documents) { doc ->
                    Surface(
                        onClick = {
                            selectedDoc = doc
                            showSignSheet = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                DocsNavIcon(size = 22.dp, color = Color(0xFF64748B))
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doc.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = doc.subtitle,
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            }

                            StatusBadge(label = doc.statusBadge)
                        }
                    }
                }
            }
        }

        // E-sign bottom sheet (Mockup B2)
        if (showSignSheet) {
            SignAgreementBottomSheet(
                onDismiss = { showSignSheet = false },
                onSignNow = {
                    showSignSheet = false
                    toastMessage = "Document signed and verified successfully"
                }
            )
        }
    }
}
