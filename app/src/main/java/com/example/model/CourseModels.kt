package com.example.model

/**
 * Data models for AnsarAcademy
 */

data class CourseInfo(
    val title: String = "Social Media Growth Mastery",
    val tagline: String = "Social Media Se Apna Career Aur Business Grow Karo 🚀",
    val description: String = "Complete step-by-step masterclass covering Instagram algorithm, viral reels creation, content calendar systems, personal branding, and high-paying client acquisition.",
    // Sample editable prices clearly marked
    val originalPrice: Int = 4999, // EDITABLE SAMPLE: Original MRP
    val offerPrice: Int = 999,      // EDITABLE SAMPLE: Limited-time launch price
    val currencySymbol: String = "₹",
    val discountPercent: Int = 80,
    val instructorName: String = "Rahul Sharma",
    val instructorBio: String = "Top Social Media Strategist & Content Creator with 500k+ organic followers. Helped 50+ businesses scale to 7-figure revenue.",
    val instructorStats: String = "500K+ Followers • 8+ Years Experience • 50+ Brand Deals",
    val totalModules: Int = 10,
    val totalLessons: Int = 42,
    val totalDurationHours: String = "24+ Hours",
    val rating: Double = 4.9,
    val totalStudentsCountSample: String = "3,400+ Students Enrolled (Sample)"
)

data class CourseLesson(
    val id: String,
    val moduleId: String,
    val title: String,
    val durationMinutes: Int,
    val summary: String,
    val isCompleted: Boolean = false,
    val videoUrlPlaceholder: String = "https://www.youtube.com/watch?v=sample",
    val keyTakeaways: List<String> = emptyList(),
    val resources: List<String> = emptyList()
)

data class CourseModule(
    val id: String,
    val number: Int,
    val title: String,
    val description: String,
    val lessons: List<CourseLesson> = emptyList()
) {
    val totalDurationMinutes: Int get() = lessons.sumOf { it.durationMinutes }
    val completedLessonsCount: Int get() = lessons.count { it.isCompleted }
    val isAllCompleted: Boolean get() = lessons.isNotEmpty() && lessons.all { it.isCompleted }
}

data class Testimonial(
    val id: String,
    val studentName: String,
    val handle: String,
    val avatarEmoji: String,
    val role: String,
    val rating: Int = 5,
    val commentHinglish: String,
    val resultBadge: String,
    val isSampleEditable: Boolean = true // Clearly marks this as editable placeholder data
)

data class FaqItem(
    val id: String,
    val questionHinglish: String,
    val answerHinglish: String
)

data class StudentBenefit(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String
)

data class DownloadableResource(
    val id: String,
    val title: String,
    val category: String,
    val fileSize: String,
    val description: String
)

data class QuizAnswers(
    val experience: String = "",
    val followers: String = "",
    val goal: String = "",
    val status: String = "",
    val problem: String = ""
)

data class QuizRecommendation(
    val headline: String,
    val personalAnalysis: String,
    val recommendedModuleIds: List<String>,
    val roadmapSteps: List<String>,
    val expectedOutcome: String
)

enum class AssistantToolType(val title: String, val description: String, val icon: String) {
    REELS("Reel Ideas 🎬", "Viral Reels video concepts with visuals & audio ideas", "🎬"),
    CAPTIONS("Captions & Tags ✍️", "Engaging Hinglish captions + 20 high-reach hashtags", "✍️"),
    HOOKS("Viral Hooks 🪝", "Stop-the-scroll 3-second opening hook lines", "🪝"),
    CALENDAR("Content Calendar 📅", "7-Day structured posting plan for consistent growth", "📅"),
    CTA("High-Converting CTAs 🎯", "Closing lines that turn viewers into followers & buyers", "🎯"),
    STRATEGY("Strategy & Goal Audit 📊", "Deep personalized audit for your niche & follower count", "📊")
}

data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val phone: String = "",
    val isEnrolled: Boolean = false,
    val role: String = "STUDENT" // "STUDENT" or "ADMIN"
)

data class EnrollmentRecord(
    val orderId: String,
    val studentName: String,
    val studentEmail: String,
    val studentPhone: String,
    val amount: Int,
    val paymentMethod: String,
    val status: String, // "TEST_SUCCESSFUL", "PENDING_REAL_PAYMENT", etc.
    val timestamp: Long = System.currentTimeMillis()
)
