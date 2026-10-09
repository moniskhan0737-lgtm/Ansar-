package com.example.data

import com.example.model.EnrollmentRecord

/**
 * Modular payment interface for AnsarAcademy.
 * Allows switching between Test/Sandbox Mode and Real Razorpay Integration.
 *
 * RAZORPAY INTEGRATION GUIDE:
 * 1. Add Razorpay dependency in app/build.gradle.kts:
 *      implementation("com.razorpay:checkout:1.6.38")
 * 2. In AndroidManifest.xml inside <application>:
 *      <receiver android:name="com.razorpay.RzpTokenReceiver" android:exported="false">
 *          <intent-filter><action android:name="rzp.device_token.share" /></intent-filter>
 *      </receiver>
 * 3. In MainActivity implement PaymentResultWithDataListener:
 *      override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) { ... }
 *      override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) { ... }
 * 4. Pass your Razorpay Key ID (obtained from https://dashboard.razorpay.com):
 *      val checkout = Checkout()
 *      checkout.setKeyID("rzp_test_YOUR_KEY_HERE")
 *      val options = JSONObject()
 *      options.put("name", "AnsarAcademy")
 *      options.put("description", "Course: Social Media Growth Mastery")
 *      options.put("amount", amountInPaise) // e.g. 999 * 100
 *      options.put("prefill.email", studentEmail)
 *      options.put("prefill.contact", studentPhone)
 *      checkout.open(activity, options)
 */
sealed class PaymentResult {
    data class Success(val record: EnrollmentRecord) : PaymentResult()
    data class RequiresRealGatewayConfig(val message: String) : PaymentResult()
    data class Failure(val error: String) : PaymentResult()
}

enum class PaymentGatewayMode {
    TEST_DEMO_MODE,     // For testing UI & student dashboard flow without fake real-money claims
    REAL_RAZORPAY_LIVE  // Production gateway mode
}

interface PaymentGateway {
    suspend fun processPayment(
        name: String,
        email: String,
        phone: String,
        amount: Int,
        paymentMethodName: String,
        mode: PaymentGatewayMode
    ): PaymentResult
}

class ModularPaymentGatewayImpl(
    private val courseRepository: CourseRepository = CourseRepository.instance,
    private val authRepository: AuthRepository = AuthRepository.instance
) : PaymentGateway {

    override suspend fun processPayment(
        name: String,
        email: String,
        phone: String,
        amount: Int,
        paymentMethodName: String,
        mode: PaymentGatewayMode
    ): PaymentResult {
        return when (mode) {
            PaymentGatewayMode.TEST_DEMO_MODE -> {
                // Safely enroll the student in test mode
                val record = EnrollmentRecord(
                    orderId = "SB-DEMO-${System.currentTimeMillis() % 100000}",
                    studentName = name,
                    studentEmail = email,
                    studentPhone = phone,
                    amount = amount,
                    paymentMethod = "$paymentMethodName (Sandbox Test)",
                    status = "TEST_ENROLLED_DEMO",
                    timestamp = System.currentTimeMillis()
                )
                courseRepository.recordNewEnrollment(record)
                authRepository.markUserEnrolled()
                PaymentResult.Success(record)
            }
            PaymentGatewayMode.REAL_RAZORPAY_LIVE -> {
                // Honest gate: We do not pretend payment succeeded if real Razorpay credentials are not hooked up
                PaymentResult.RequiresRealGatewayConfig(
                    "Real Razorpay payment gateway is not yet linked with API keys. " +
                    "To enable real transactions, add your Razorpay Key ID in PaymentGateway.kt or configure via backend server. " +
                    "Switch to '[Test / Demo Mode]' in the toggle above to test the student dashboard immediately."
                )
            }
        }
    }
}
