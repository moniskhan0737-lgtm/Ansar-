package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigate: (ScreenRoute) -> Unit,
    courseRepository: CourseRepository = CourseRepository.instance,
    authRepository: AuthRepository = AuthRepository.instance
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val paymentGateway: PaymentGateway = remember { ModularPaymentGatewayImpl(courseRepository, authRepository) }

    var studentName by remember { mutableStateOf("Aman Verma") }
    var studentEmail by remember { mutableStateOf("aman.v@example.com") }
    var studentPhone by remember { mutableStateOf("+91 98765 43210") }
    var couponCode by remember { mutableStateOf("") }
    var couponApplied by remember { mutableStateOf(false) }

    var selectedPaymentMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe / Paytm)") }
    var gatewayMode by remember { mutableStateOf(PaymentGatewayMode.TEST_DEMO_MODE) }

    var isProcessing by remember { mutableStateOf(false) }
    var gatewayAlertMessage by remember { mutableStateOf<String?>(null) }
    var enrollSuccessDialog by remember { mutableStateOf(false) }

    val originalPrice = 4999
    val launchDiscount = 4000
    val promoDiscount = if (couponApplied) 100 else 0
    val finalAmount = (originalPrice - launchDiscount - promoDiscount).coerceAtLeast(0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Checkout", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(ScreenRoute.LANDING) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            // Course Summary Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(
                                    1.5.dp,
                                    Brush.linearGradient(listOf(BrandRose, BrandGold)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_ansar_logo),
                                contentDescription = "AnsarAcademy Course Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Social Media Growth Mastery",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Lifetime Access • 10 Modules • VIP Community",
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Customer Details Form
            item {
                Text(
                    text = "Student Contact Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BrandPurpleLight,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = studentEmail,
                    onValueChange = { studentEmail = it },
                    label = { Text("Email Address (Course login & invoice)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_email_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BrandPurpleLight,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = studentPhone,
                    onValueChange = { studentPhone = it },
                    label = { Text("Phone Number / WhatsApp") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checkout_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BrandPurpleLight,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Payment Mode Selector (Sandbox Demo vs Real Razorpay)
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandGold.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💳", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Gateway Environment",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BrandGold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandPurple.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = if (gatewayMode == PaymentGatewayMode.TEST_DEMO_MODE) "SANDBOX DEMO" else "LIVE GATEWAY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            FilterChip(
                                selected = gatewayMode == PaymentGatewayMode.TEST_DEMO_MODE,
                                onClick = { gatewayMode = PaymentGatewayMode.TEST_DEMO_MODE },
                                label = { Text("Demo / Test Mode", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandGreenContainer,
                                    selectedLabelColor = BrandGreen
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = gatewayMode == PaymentGatewayMode.REAL_RAZORPAY_LIVE,
                                onClick = { gatewayMode = PaymentGatewayMode.REAL_RAZORPAY_LIVE },
                                label = { Text("Live Razorpay", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandRose.copy(alpha = 0.3f),
                                    selectedLabelColor = BrandRoseLight
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (gatewayMode == PaymentGatewayMode.TEST_DEMO_MODE)
                                "Note: Test Mode allows testing student enrollment flow without charging real cards."
                            else
                                "Note: Real gateway requires Razorpay API keys configured in PaymentGateway.kt.",
                            fontSize = 11.sp,
                            color = DarkTextMuted
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Payment Methods Selection
            item {
                Text(
                    text = "Select Payment Method",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))

                val methods = listOf(
                    "UPI (Google Pay / PhonePe / Paytm)",
                    "Credit / Debit Card (Visa, MasterCard, RuPay)",
                    "Net Banking (All Indian Banks)"
                )

                methods.forEach { method ->
                    val isSelected = method == selectedPaymentMethod
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF261942) else DarkSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) BrandPurpleLight else DarkBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedPaymentMethod = method }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedPaymentMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = BrandPurpleLight)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = method,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else DarkTextPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Coupon Code Field
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = couponCode,
                        onValueChange = { couponCode = it },
                        placeholder = { Text("Coupon Code (e.g. BOOST50)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("checkout_coupon_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface,
                            focusedBorderColor = BrandPurpleLight,
                            unfocusedBorderColor = DarkBorder
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (couponCode.equals("BOOST50", ignoreCase = true) || couponCode.equals("GROWTH100", ignoreCase = true)) {
                                couponApplied = true
                                Toast.makeText(context, "Coupon applied! ₹100 extra off", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Try coupon 'BOOST50'", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                    ) {
                        Text(if (couponApplied) "Applied ✓" else "Apply")
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Price Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        PriceRow(label = "Course MRP", amount = "₹$originalPrice")
                        PriceRow(label = "Limited-Time Launch Discount (80%)", amount = "-₹$launchDiscount", isDiscount = true)
                        if (couponApplied) {
                            PriceRow(label = "Promo Coupon Discount", amount = "-₹$promoDiscount", isDiscount = true)
                        }

                        HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Final Amount",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "₹$finalAmount",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = BrandGreen
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Pay & Enroll Button
            item {
                Button(
                    onClick = {
                        if (studentName.isBlank() || studentEmail.isBlank() || studentPhone.isBlank()) {
                            Toast.makeText(context, "Please fill your name, email, and phone number", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isProcessing = true
                        coroutineScope.launch {
                            val result = paymentGateway.processPayment(
                                name = studentName,
                                email = studentEmail,
                                phone = studentPhone,
                                amount = finalAmount,
                                paymentMethodName = selectedPaymentMethod,
                                mode = gatewayMode
                            )
                            isProcessing = false

                            when (result) {
                                is PaymentResult.Success -> {
                                    enrollSuccessDialog = true
                                }
                                is PaymentResult.RequiresRealGatewayConfig -> {
                                    gatewayAlertMessage = result.message
                                }
                                is PaymentResult.Failure -> {
                                    gatewayAlertMessage = result.error
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("checkout_pay_and_enroll_button")
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Processing...", color = Color.White)
                    } else {
                        Text(
                            text = "Pay & Enroll Now — ₹$finalAmount 🔒",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Secure", tint = DarkTextMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "100% Secure 256-Bit SSL Encrypted Checkout",
                        fontSize = 11.sp,
                        color = DarkTextMuted
                    )
                }
            }
        }
    }

    // Success dialog after test enrollment
    if (enrollSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "🎉 Enrollment Successful!",
                    fontWeight = FontWeight.Bold,
                    color = BrandGreen
                )
            },
            text = {
                Text(
                    text = "Welcome to AnsarAcademy, $studentName!\n\n" +
                            "Aapka course access activate ho chuka hai. Abhi Student Dashboard par jaakar modules aur lessons access karein.",
                    fontSize = 13.sp,
                    color = DarkTextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        enrollSuccessDialog = false
                        onNavigate(ScreenRoute.STUDENT_DASHBOARD)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) {
                    Text("Go to Student Dashboard")
                }
            }
        )
    }

    // Alert if Live Razorpay is selected without credentials
    if (gatewayAlertMessage != null) {
        AlertDialog(
            onDismissRequest = { gatewayAlertMessage = null },
            title = {
                Text(
                    text = "⚠️ Payment Gateway Notice",
                    fontWeight = FontWeight.Bold,
                    color = BrandGold
                )
            },
            text = {
                Text(
                    text = gatewayAlertMessage ?: "",
                    fontSize = 12.sp,
                    color = DarkTextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        gatewayAlertMessage = null
                        gatewayMode = PaymentGatewayMode.TEST_DEMO_MODE
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) {
                    Text("Switch to Demo Mode")
                }
            },
            dismissButton = {
                TextButton(onClick = { gatewayAlertMessage = null }) {
                    Text("Dismiss", color = DarkTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun PriceRow(label: String, amount: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = DarkTextSecondary)
        Text(
            text = amount,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDiscount) BrandGreen else DarkTextPrimary
        )
    }
}
