package com.example.housingroperty.data.repository

import com.example.housingroperty.data.models.*

interface AdminRepository {
    suspend fun getOverviewStats(): AdminOverviewStats
    suspend fun getApprovals(): List<AdminApprovalItem>
    suspend fun approveItem(id: String): Boolean
    suspend fun rejectItem(id: String): Boolean
    suspend fun getPayoutRequests(): List<AdminPayoutRequest>
    suspend fun processPayout(id: String): Boolean
    suspend fun getActivityLogs(): List<AdminActivityLog>
}

class InMemoryAdminRepository : AdminRepository {
    private var stats = AdminOverviewStats()

    private val approvalsList = mutableListOf(
        AdminApprovalItem(
            id = "APP-101",
            type = ApprovalType.PARTNER_KYC,
            title = "Amit Rathi",
            subtitle = "PAN Card & Bank details submitted · AGT-3315",
            submittedAt = "2 hrs ago",
            status = "Pending"
        ),
        AdminApprovalItem(
            id = "APP-102",
            type = ApprovalType.BOOKING_AGREEMENT,
            title = "Rohan Mehta — Plot A-114",
            subtitle = "Digital Agreement E-signed (Gold Tier)",
            submittedAt = "4 hrs ago",
            status = "Pending"
        ),
        AdminApprovalItem(
            id = "APP-103",
            type = ApprovalType.PARTNER_KYC,
            title = "Vikram Malhotra",
            subtitle = "Partner KYC verification pending",
            submittedAt = "Yesterday",
            status = "Pending"
        ),
        AdminApprovalItem(
            id = "APP-104",
            type = ApprovalType.BANK_UPDATE,
            title = "Priya Sharma",
            subtitle = "Updated payout account to ICICI Bank",
            submittedAt = "Yesterday",
            status = "Pending"
        ),
        AdminApprovalItem(
            id = "APP-105",
            type = ApprovalType.BOOKING_AGREEMENT,
            title = "Sunil Verma — Plot B-202",
            subtitle = "Agreement signed · Silver Tier",
            submittedAt = "2 days ago",
            status = "Pending"
        )
    )

    private val payoutsList = mutableListOf(
        AdminPayoutRequest(
            id = "PAY-501",
            agentName = "Amit Rathi",
            agentCode = "AGT-3315",
            amount = "₹12,300",
            bankDetails = "HDFC Bank ••4821",
            period = "Sep Cycle 1",
            status = "Pending"
        ),
        AdminPayoutRequest(
            id = "PAY-502",
            agentName = "Priya Sharma",
            agentCode = "AGT-2890",
            amount = "₹28,500",
            bankDetails = "ICICI Bank ••9912",
            period = "Sep Cycle 1",
            status = "Pending"
        ),
        AdminPayoutRequest(
            id = "PAY-503",
            agentName = "Karan Verma",
            agentCode = "AGT-1904",
            amount = "₹8,400",
            bankDetails = "SBI ••3124",
            period = "Sep Cycle 1",
            status = "Pending"
        )
    )

    private val activityLogs = listOf(
        AdminActivityLog(
            id = "LOG-1",
            title = "New Booking Confirmed",
            description = "Plot A-114 booked by Rohan Mehta for ₹3,00,000",
            timeAgo = "10 mins ago"
        ),
        AdminActivityLog(
            id = "LOG-2",
            title = "Lead Registered",
            description = "Agent Amit Rathi registered new lead: Neha Joshi",
            timeAgo = "1 hr ago"
        ),
        AdminActivityLog(
            id = "LOG-3",
            title = "Commission Generated",
            description = "₹42,500 net commission calculated for AGT-3315",
            timeAgo = "3 hrs ago"
        ),
        AdminActivityLog(
            id = "LOG-4",
            title = "Partner KYC Submitted",
            description = "Vikram Malhotra submitted PAN & Bank verification documents",
            timeAgo = "5 hrs ago"
        )
    )

    override suspend fun getOverviewStats(): AdminOverviewStats = stats

    override suspend fun getApprovals(): List<AdminApprovalItem> = approvalsList.toList()

    override suspend fun approveItem(id: String): Boolean {
        val index = approvalsList.indexOfFirst { it.id == id }
        if (index != -1) {
            approvalsList[index] = approvalsList[index].copy(status = "Approved")
            stats = stats.copy(pendingApprovals = (stats.pendingApprovals - 1).coerceAtLeast(0))
            return true
        }
        return false
    }

    override suspend fun rejectItem(id: String): Boolean {
        val index = approvalsList.indexOfFirst { it.id == id }
        if (index != -1) {
            approvalsList[index] = approvalsList[index].copy(status = "Rejected")
            stats = stats.copy(pendingApprovals = (stats.pendingApprovals - 1).coerceAtLeast(0))
            return true
        }
        return false
    }

    override suspend fun getPayoutRequests(): List<AdminPayoutRequest> = payoutsList.toList()

    override suspend fun processPayout(id: String): Boolean {
        val index = payoutsList.indexOfFirst { it.id == id }
        if (index != -1) {
            payoutsList[index] = payoutsList[index].copy(status = "Processed")
            return true
        }
        return false
    }

    override suspend fun getActivityLogs(): List<AdminActivityLog> = activityLogs
}

object AdminRepositoryProvider {
    val instance: AdminRepository = InMemoryAdminRepository()
}
