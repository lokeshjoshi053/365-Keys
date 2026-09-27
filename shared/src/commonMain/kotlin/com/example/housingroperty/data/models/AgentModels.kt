package com.example.housingroperty.data.models

data class PartnerProfile(
    val fullName: String = "Amit Rathi",
    val roleTitle: String = "Agent Partner",
    val status: String = "Approved",
    val agentCode: String = "AGT-3315",
    val referralLink: String = "ref.315plots.in/AGT-3315",
    val panDocument: String? = "PAN_AMIT_RATHI.pdf",
    val bankAccount: String? = "HDFC Bank ••4821",
    val email: String = "amit.rathi@partner.com",
    val phone: String = "+91 98260 12345"
)

data class DashboardStats(
    val netCommission: String = "₹42,500",
    val activeLeadsCount: Int = 8,
    val leadsCount: String = "8",
    val bookingsCount: String = "3",
    val matchedVolume: String = "₹9L",
    val payoutDue: String = "₹12,300"
)

data class Lead(
    val id: String,
    val name: String,
    val phone: String,
    val subtitle: String,
    val plotInterested: String? = null,
    val statusBadge: String // "In progress", "Booked", "New"
)

data class NetworkLevel(
    val level: Int,
    val membersCount: Int
)

data class TreeNode(
    val id: String,
    val name: String,
    val subtitle: String,
    val badgeText: String,
    val level: Int,
    val children: List<TreeNode> = emptyList()
)

data class CommissionItem(
    val id: String,
    val title: String,
    val levelRate: String,
    val statusBadge: String, // "Approved", "Pending"
    val gross: String = "₹15,100",
    val adminDeduction: String = "-₹3,020",
    val tdsDeduction: String = "-₹755",
    val net: String = "₹11,325",
    val source: String = "Booking #315-88213 · Level 1"
)

data class MatchingSummary(
    val matchedVolume: String = "₹9,00,000",
    val powerLeg: String = "₹14L",
    val weakerLeg: String = "₹6L",
    val matchedVolShort: String = "₹9L",
    val gross50: String = "₹4.5L",
    val admin20: String = "-₹90,000",
    val net: String = "₹3,15,000"
)

data class PayoutInfo(
    val bankName: String = "HDFC Bank ••4821",
    val isVerified: Boolean = true,
    val nextPayoutAmount: String = "₹12,300",
    val scheduledDate: String = "Scheduled 30 Sep"
)

data class AgentNotification(
    val id: String,
    val title: String,
    val timeAgo: String,
    val type: String // "commission", "lead", "payout"
)
