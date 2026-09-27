package com.example.housingroperty.data.repository

import com.example.housingroperty.data.api.CustomerApi
import com.example.housingroperty.data.api.DummyCustomerApiClient
import com.example.housingroperty.data.models.*

interface CustomerRepository {
    suspend fun getProfile(): UserProfile
    suspend fun saveProfile(name: String, email: String, nominee: String?, phone: String? = null): UserProfile
    suspend fun getPlots(): List<Plot>
    suspend fun getPlotById(plotId: String): Plot?
    suspend fun getPlans(): List<BookingPlan>
    suspend fun getActiveBooking(): Booking?
    suspend fun createBooking(plotId: String, planId: String): Booking
    suspend fun getDocuments(): List<DocumentItem>
    suspend fun getPayments(): List<PaymentItem>
    suspend fun getNotifications(): List<NotificationItem>
    suspend fun getReferrerInfo(): ReferrerInfo
}

class CustomerRepositoryImpl(
    private val api: CustomerApi = DummyCustomerApiClient()
) : CustomerRepository {

    override suspend fun getProfile(): UserProfile = api.getProfile()

    override suspend fun saveProfile(name: String, email: String, nominee: String?, phone: String?): UserProfile =
        api.saveProfile(name, email, nominee, phone)

    override suspend fun getPlots(): List<Plot> = api.getPlots()

    override suspend fun getPlotById(plotId: String): Plot? = api.getPlotById(plotId)

    override suspend fun getPlans(): List<BookingPlan> = api.getPlans()

    override suspend fun getActiveBooking(): Booking? = api.getActiveBooking()

    override suspend fun createBooking(plotId: String, planId: String): Booking =
        api.createBooking(plotId, planId)

    override suspend fun getDocuments(): List<DocumentItem> = api.getDocuments()

    override suspend fun getPayments(): List<PaymentItem> = api.getPayments()

    override suspend fun getNotifications(): List<NotificationItem> = api.getNotifications()

    override suspend fun getReferrerInfo(): ReferrerInfo = api.getReferrerInfo()
}

// Singleton provider for easy UI access
object CustomerRepositoryProvider {
    val instance: CustomerRepository by lazy { CustomerRepositoryImpl() }
}
