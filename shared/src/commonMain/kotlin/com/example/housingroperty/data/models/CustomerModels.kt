package com.example.housingroperty.data.models

data class UserProfile(
    val fullName: String = "Rohan Mehta",
    val email: String = "rohan@email.com",
    val nomineeName: String? = null,
    val roleTitle: String = "Customer",
    val phone: String = "+91 98765 43210"
)

enum class PlotStatus(val label: String) {
    AVAILABLE("Available"),
    SOLD("Sold"),
    BOOKED("Booked"),
    ON_HOLD("On Hold")
}

data class Plot(
    val id: String,
    val title: String,
    val block: String,
    val location: String,
    val sizeSqFt: String,
    val status: PlotStatus,
    val facing: String = "East",
    val roadWidth: String = "30 ft",
    val pricePerSqFt: String = "₹4,166",
    val totalPriceFormatted: String = "₹50,00,000",
    val latitude: Double = 23.2599,
    val longitude: Double = 77.4126,
    val mapOffsetX: Float = 0.5f,
    val mapOffsetY: Float = 0.5f
) {
    val isAvailable: Boolean get() = status == PlotStatus.AVAILABLE
}

data class BookingPlan(
    val id: String,
    val name: String,
    val amountFormatted: String,
    val amount: Long,
    val isPopular: Boolean = false
)

enum class MilestoneStatus {
    COMPLETED,
    IN_PROGRESS,
    UPCOMING
}

data class Milestone(
    val step: Int,
    val title: String,
    val subtitle: String,
    val status: MilestoneStatus,
    val badgeLabel: String? = null
)

data class Booking(
    val orderId: String = "#315-88213",
    val plotId: String = "Plot A-114",
    val plotTitle: String = "Plot A-114",
    val block: String = "Block A",
    val location: String = "Bhopal Ring Rd",
    val sizeSqFt: String = "1,200 sq.ft",
    val planName: String = "Gold Tier",
    val amountFormatted: String = "₹3,00,000",
    val txnId: String = "PYU3829SD",
    val stepText: String = "Step 2 of 4",
    val stepSummary: String = "Documents pending",
    val milestones: List<Milestone> = listOf(
        Milestone(1, "Booking Confirmed", "12 Sep", MilestoneStatus.COMPLETED),
        Milestone(2, "Agreement Signing", "In progress", MilestoneStatus.IN_PROGRESS, "Pending"),
        Milestone(3, "Registration", "Upcoming", MilestoneStatus.UPCOMING)
    )
)

data class DocumentItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val statusBadge: String,
    val isActionNeeded: Boolean = false
)

data class PaymentItem(
    val id: String,
    val amountFormatted: String,
    val dateOrSubtitle: String,
    val statusBadge: String = "Verified"
)

data class NotificationItem(
    val id: String,
    val title: String,
    val timeAgo: String,
    val iconType: String // "doc", "payment", "plot"
)

data class ReferrerInfo(
    val agentName: String = "Amit Rathi",
    val agentCode: String = "AGT-3315",
    val agentRole: String = "Agent Partner",
    val customerName: String = "Rohan Mehta (You)",
    val customerStatus: String = "Joined via referral link"
)
