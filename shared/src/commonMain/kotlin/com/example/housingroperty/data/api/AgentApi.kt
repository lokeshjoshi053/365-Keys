package com.example.housingroperty.data.api

import com.example.housingroperty.data.models.*

interface AgentApi {
    suspend fun getProfile(): PartnerProfile
    suspend fun updateProfile(fullName: String, email: String, phone: String, bankAccount: String?): PartnerProfile
    suspend fun submitPartnerDetails(fullName: String, panDoc: String, bankDetails: String): PartnerProfile
    suspend fun getDashboardStats(): DashboardStats
    suspend fun getLeads(): List<Lead>
    suspend fun registerLead(name: String, phone: String, plot: String?): Lead
    suspend fun getNetworkLevels(): List<NetworkLevel>
    suspend fun getReferralTree(): TreeNode
    suspend fun getCommissions(): List<CommissionItem>
    suspend fun getCommissionById(id: String): CommissionItem?
    suspend fun getMatchingSummary(): MatchingSummary
    suspend fun getPayoutInfo(): PayoutInfo
    suspend fun getNotifications(): List<AgentNotification>
}

class DummyAgentApiClient : AgentApi {

    private var profile = PartnerProfile(
        fullName = "Amit Rathi",
        roleTitle = "Agent Partner",
        status = "Approved",
        agentCode = "AGT-3315",
        referralLink = "ref.315plots.in/AGT-3315"
    )

    private val stats = DashboardStats()

    private val leads = mutableListOf(
        Lead("lead-1", "Priya Sharma", "+91 98765 11223", "Plot C-310 · P...", "Plot C-310", "In progress"),
        Lead("lead-2", "Karan Verma", "+91 98765 22334", "Booking confirmed", "Plot A-114", "Booked"),
        Lead("lead-3", "Neha Joshi", "+91 98765 33445", "New lead", "Plot B-202", "New")
    )

    private val networkLevels = listOf(
        NetworkLevel(1, 4),
        NetworkLevel(2, 11),
        NetworkLevel(3, 22)
    )

    private val referralTree = TreeNode(
        id = "root",
        name = "Amit Rathi (You)",
        subtitle = "Agent · AGT-3315",
        badgeText = "37 total",
        level = 0,
        children = listOf(
            TreeNode(
                id = "priya",
                name = "Priya Sharma",
                subtitle = "Level 1 · 2 bookings",
                badgeText = "3 referred",
                level = 1,
                children = listOf(
                    TreeNode(
                        id = "karan",
                        name = "Karan Verma",
                        subtitle = "Level 2 · Booked",
                        badgeText = "2 referred",
                        level = 2,
                        children = listOf(
                            TreeNode(
                                id = "neha",
                                name = "Neha Joshi",
                                subtitle = "Level 3 · New lead",
                                badgeText = "0",
                                level = 3
                            ),
                            TreeNode(
                                id = "suresh",
                                name = "Suresh Iyer",
                                subtitle = "Level 3 · In progress",
                                badgeText = "0",
                                level = 3
                            )
                        )
                    )
                )
            )
        )
    )

    private val commissions = listOf(
        CommissionItem(
            id = "comm-1",
            title = "Gold Tier Booking",
            levelRate = "Level 1 · 20%",
            statusBadge = "Approved",
            gross = "₹15,100",
            adminDeduction = "-₹3,020",
            tdsDeduction = "-₹755",
            net = "₹11,325",
            source = "Booking #315-88213 · Level 1"
        ),
        CommissionItem(
            id = "comm-2",
            title = "Silver Tier Booking",
            levelRate = "Level 2 · 10%",
            statusBadge = "Pending",
            gross = "₹8,500",
            adminDeduction = "-₹1,700",
            tdsDeduction = "-₹425",
            net = "₹6,375",
            source = "Booking #315-99412 · Level 2"
        )
    )

    private val matchingSummary = MatchingSummary()
    private val payoutInfo = PayoutInfo()

    private val notifications = listOf(
        AgentNotification("notif-1", "Commission approved", "3h ago", "commission"),
        AgentNotification("notif-2", "New lead assigned", "Yesterday", "lead"),
        AgentNotification("notif-3", "Payout processed", "2 days ago", "payout")
    )

    override suspend fun getProfile(): PartnerProfile = profile

    override suspend fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
        bankAccount: String?
    ): PartnerProfile {
        profile = profile.copy(
            fullName = fullName,
            email = email,
            phone = phone,
            bankAccount = bankAccount
        )
        return profile
    }

    override suspend fun submitPartnerDetails(
        fullName: String,
        panDoc: String,
        bankDetails: String
    ): PartnerProfile {
        profile = profile.copy(
            fullName = fullName,
            panDocument = panDoc,
            bankAccount = bankDetails
        )
        return profile
    }

    override suspend fun getDashboardStats(): DashboardStats = stats

    override suspend fun getLeads(): List<Lead> = leads

    override suspend fun registerLead(name: String, phone: String, plot: String?): Lead {
        val newLead = Lead(
            id = "lead-${leads.size + 1}",
            name = name,
            phone = phone,
            subtitle = if (plot != null) "$plot · New lead" else "New lead",
            plotInterested = plot,
            statusBadge = "New"
        )
        leads.add(0, newLead)
        return newLead
    }

    override suspend fun getNetworkLevels(): List<NetworkLevel> = networkLevels

    override suspend fun getReferralTree(): TreeNode = referralTree

    override suspend fun getCommissions(): List<CommissionItem> = commissions

    override suspend fun getCommissionById(id: String): CommissionItem? =
        commissions.find { it.id == id } ?: commissions.firstOrNull()

    override suspend fun getMatchingSummary(): MatchingSummary = matchingSummary

    override suspend fun getPayoutInfo(): PayoutInfo = payoutInfo

    override suspend fun getNotifications(): List<AgentNotification> = notifications
}
