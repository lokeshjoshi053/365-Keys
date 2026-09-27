package com.example.housingroperty.data.models

enum class ApprovalType(val label: String) {
    PARTNER_KYC("Partner KYC"),
    BOOKING_AGREEMENT("Agreement"),
    BANK_UPDATE("Bank Account")
}

data class AdminOverviewStats(
    val totalRevenue: String = "₹1.42 Cr",
    val totalPlots: Int = 315,
    val plotsSold: Int = 48,
    val plotsAvailable: Int = 267,
    val activePartners: Int = 142,
    val pendingApprovals: Int = 6,
    val pendingPayouts: String = "₹3,45,000"
)

data class AdminApprovalItem(
    val id: String,
    val type: ApprovalType,
    val title: String,
    val subtitle: String,
    val submittedAt: String,
    val status: String = "Pending"
)

data class AdminPayoutRequest(
    val id: String,
    val agentName: String,
    val agentCode: String,
    val amount: String,
    val bankDetails: String,
    val period: String,
    val status: String = "Pending"
)

data class AdminActivityLog(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String
)
