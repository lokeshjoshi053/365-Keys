package com.example.housingroperty.data.repository

import com.example.housingroperty.data.api.AgentApi
import com.example.housingroperty.data.api.DummyAgentApiClient
import com.example.housingroperty.data.models.*

interface AgentRepository {
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

class AgentRepositoryImpl(
    private val api: AgentApi = DummyAgentApiClient()
) : AgentRepository {

    override suspend fun getProfile(): PartnerProfile = api.getProfile()

    override suspend fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
        bankAccount: String?
    ): PartnerProfile = api.updateProfile(fullName, email, phone, bankAccount)

    override suspend fun submitPartnerDetails(
        fullName: String,
        panDoc: String,
        bankDetails: String
    ): PartnerProfile = api.submitPartnerDetails(fullName, panDoc, bankDetails)

    override suspend fun getDashboardStats(): DashboardStats = api.getDashboardStats()

    override suspend fun getLeads(): List<Lead> = api.getLeads()

    override suspend fun registerLead(name: String, phone: String, plot: String?): Lead =
        api.registerLead(name, phone, plot)

    override suspend fun getNetworkLevels(): List<NetworkLevel> = api.getNetworkLevels()

    override suspend fun getReferralTree(): TreeNode = api.getReferralTree()

    override suspend fun getCommissions(): List<CommissionItem> = api.getCommissions()

    override suspend fun getCommissionById(id: String): CommissionItem? =
        api.getCommissionById(id)

    override suspend fun getMatchingSummary(): MatchingSummary = api.getMatchingSummary()

    override suspend fun getPayoutInfo(): PayoutInfo = api.getPayoutInfo()

    override suspend fun getNotifications(): List<AgentNotification> = api.getNotifications()
}

object AgentRepositoryProvider {
    val instance: AgentRepository by lazy { AgentRepositoryImpl() }
}
