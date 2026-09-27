package com.example.housingroperty.data.api

import com.example.housingroperty.data.models.*

interface CustomerApi {
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

class DummyCustomerApiClient : CustomerApi {

    private var profile = UserProfile(
        fullName = "Rohan Mehta",
        email = "rohan@email.com",
        nomineeName = null,
        roleTitle = "Customer"
    )

    private var plots = listOf(
        Plot(
            id = "Plot A-114",
            title = "Plot A-114",
            block = "Block A",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,200 sq.ft",
            status = PlotStatus.AVAILABLE,
            facing = "East",
            roadWidth = "30 ft",
            pricePerSqFt = "₹4,166",
            totalPriceFormatted = "₹50,00,000",
            latitude = 23.2599,
            longitude = 77.4126,
            mapOffsetX = 0.28f,
            mapOffsetY = 0.32f
        ),
        Plot(
            id = "Plot A-115",
            title = "Plot A-115",
            block = "Block A",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,200 sq.ft",
            status = PlotStatus.BOOKED,
            facing = "North",
            roadWidth = "30 ft",
            pricePerSqFt = "₹4,166",
            totalPriceFormatted = "₹50,00,000",
            latitude = 23.2605,
            longitude = 77.4132,
            mapOffsetX = 0.40f,
            mapOffsetY = 0.32f
        ),
        Plot(
            id = "Plot B-202",
            title = "Plot B-202",
            block = "Block B",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,500 sq.ft",
            status = PlotStatus.AVAILABLE,
            facing = "North",
            roadWidth = "40 ft",
            pricePerSqFt = "₹4,250",
            totalPriceFormatted = "₹63,75,000",
            latitude = 23.2612,
            longitude = 77.4140,
            mapOffsetX = 0.65f,
            mapOffsetY = 0.30f
        ),
        Plot(
            id = "Plot B-203",
            title = "Plot B-203",
            block = "Block B",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,500 sq.ft",
            status = PlotStatus.AVAILABLE,
            facing = "East",
            roadWidth = "40 ft",
            pricePerSqFt = "₹4,250",
            totalPriceFormatted = "₹63,75,000",
            latitude = 23.2618,
            longitude = 77.4148,
            mapOffsetX = 0.78f,
            mapOffsetY = 0.30f
        ),
        Plot(
            id = "Plot C-310",
            title = "Plot C-310",
            block = "Block C",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,000 sq.ft",
            status = PlotStatus.SOLD,
            facing = "West",
            roadWidth = "30 ft",
            pricePerSqFt = "₹4,000",
            totalPriceFormatted = "₹40,00,000",
            latitude = 23.2585,
            longitude = 77.4110,
            mapOffsetX = 0.25f,
            mapOffsetY = 0.68f
        ),
        Plot(
            id = "Plot C-312",
            title = "Plot C-312",
            block = "Block C",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,150 sq.ft",
            status = PlotStatus.AVAILABLE,
            facing = "North",
            roadWidth = "30 ft",
            pricePerSqFt = "₹4,100",
            totalPriceFormatted = "₹47,15,000",
            latitude = 23.2580,
            longitude = 77.4118,
            mapOffsetX = 0.38f,
            mapOffsetY = 0.68f
        ),
        Plot(
            id = "Plot D-405",
            title = "Plot D-405",
            block = "Block D",
            location = "Bhopal Ring Rd",
            sizeSqFt = "1,800 sq.ft",
            status = PlotStatus.AVAILABLE,
            facing = "East",
            roadWidth = "50 ft",
            pricePerSqFt = "₹4,500",
            totalPriceFormatted = "₹81,00,000",
            latitude = 23.2570,
            longitude = 77.4155,
            mapOffsetX = 0.65f,
            mapOffsetY = 0.66f
        ),
        Plot(
            id = "Plot D-406",
            title = "Plot D-406",
            block = "Block D",
            location = "Bhopal Ring Rd",
            sizeSqFt = "2,100 sq.ft",
            status = PlotStatus.ON_HOLD,
            facing = "South",
            roadWidth = "50 ft",
            pricePerSqFt = "₹4,600",
            totalPriceFormatted = "₹96,60,000",
            latitude = 23.2565,
            longitude = 77.4162,
            mapOffsetX = 0.78f,
            mapOffsetY = 0.66f
        )
    )

    private val plans = listOf(
        BookingPlan("express", "Express Token", "₹21,000", 21000),
        BookingPlan("advance", "Advance Token", "₹71,000", 71000),
        BookingPlan("silver", "Silver", "₹1,51,000", 151000),
        BookingPlan("gold", "Gold", "₹3,00,000", 300000, isPopular = true),
        BookingPlan("platinum", "Platinum", "₹6,51,000", 651000)
    )

    private var currentBooking: Booking? = Booking(
        orderId = "#315-88213",
        plotId = "Plot A-114",
        plotTitle = "Plot A-114",
        block = "Block A",
        location = "Bhopal Ring Rd",
        sizeSqFt = "1,200 sq.ft",
        planName = "Gold Tier",
        amountFormatted = "₹3,00,000",
        txnId = "PYU3829SD",
        stepText = "Step 2 of 4",
        stepSummary = "Documents pending"
    )

    private var documents = listOf(
        DocumentItem("doc-1", "Booking Agreement...", "Sent for signature", "Sent", isActionNeeded = true),
        DocumentItem("doc-2", "Payment Receipt...", "Verified", "Signed", isActionNeeded = false)
    )

    private var payments = listOf(
        PaymentItem("pay-1", "₹3,00,000", "12 Sep · Gold token", "Verified"),
        PaymentItem("pay-2", "₹21,000", "Booking fee", "Verified")
    )

    private val notifications = listOf(
        NotificationItem("notif-1", "Document signed", "1h ago", "doc"),
        NotificationItem("notif-2", "Payment verified", "2h ago", "payment"),
        NotificationItem("notif-3", "New plots released", "Yesterday", "plot")
    )

    override suspend fun getProfile(): UserProfile = profile

    override suspend fun saveProfile(name: String, email: String, nominee: String?, phone: String?): UserProfile {
        profile = profile.copy(
            fullName = name,
            email = email,
            nomineeName = nominee,
            phone = phone ?: profile.phone
        )
        return profile
    }

    override suspend fun getPlots(): List<Plot> = plots

    override suspend fun getPlotById(plotId: String): Plot? {
        return plots.find { it.id == plotId } ?: plots.firstOrNull()
    }

    override suspend fun getPlans(): List<BookingPlan> = plans

    override suspend fun getActiveBooking(): Booking? = currentBooking

    override suspend fun createBooking(plotId: String, planId: String): Booking {
        val plot = getPlotById(plotId)
        val plan = plans.find { it.id == planId } ?: plans.first()
        val orderId = "#315-" + (10000..99999).random()
        val txnId = "PYU" + (1000..9999).random() + "SD"

        val newBooking = Booking(
            orderId = orderId,
            plotId = plotId,
            plotTitle = plot?.title ?: plotId,
            block = plot?.block ?: "Block A",
            location = plot?.location ?: "Bhopal Ring Rd",
            sizeSqFt = plot?.sizeSqFt ?: "1,200 sq.ft",
            planName = plan.name,
            amountFormatted = plan.amountFormatted,
            txnId = txnId,
            stepText = "Step 2 of 4",
            stepSummary = "Documents pending"
        )
        currentBooking = newBooking

        // Update plot status to BOOKED
        plots = plots.map {
            if (it.id == plotId) it.copy(status = PlotStatus.BOOKED) else it
        }

        // Add payment record
        payments = listOf(
            PaymentItem(
                id = "pay-${payments.size + 1}",
                amountFormatted = plan.amountFormatted,
                dateOrSubtitle = "Just now · ${plan.name}",
                statusBadge = "Verified"
            )
        ) + payments

        // Add document record
        documents = listOf(
            DocumentItem(
                id = "doc-${documents.size + 1}",
                title = "Booking Agreement ($plotId)",
                subtitle = "Sent for signature",
                statusBadge = "Pending",
                isActionNeeded = true
            )
        ) + documents

        return newBooking
    }

    override suspend fun getDocuments(): List<DocumentItem> = documents

    override suspend fun getPayments(): List<PaymentItem> = payments

    override suspend fun getNotifications(): List<NotificationItem> = notifications

    override suspend fun getReferrerInfo(): ReferrerInfo = ReferrerInfo()
}
