package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthRepository
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onNavigate: (ScreenRoute) -> Unit,
    authRepository: AuthRepository = AuthRepository.instance
) {
    val context = LocalContext.current
    val currentUser by authRepository.currentUser.collectAsState()

    var isSignupMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("student@ansaracademy.com") }
    var phone by remember { mutableStateOf("+91 98765 43210") }
    var password by remember { mutableStateOf("password123") }

    var isAdminRoleSelected by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentUser != null) "Account Profile" else if (isSignupMode) "Student Signup" else "Student Login", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(ScreenRoute.LANDING) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUser != null) {
                // Logged In Profile View
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(if (currentUser?.role == "ADMIN") "👑" else "🎓", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentUser?.name ?: "User",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = currentUser?.email ?: "",
                            fontSize = 12.sp,
                            color = DarkTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (currentUser?.role == "ADMIN") BrandGold.copy(alpha = 0.2f) else BrandGreenContainer
                        ) {
                            Text(
                                text = if (currentUser?.role == "ADMIN") "COURSE OWNER (ADMIN)" else "ENROLLED STUDENT",
                                color = if (currentUser?.role == "ADMIN") BrandGold else BrandGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (currentUser?.role == "ADMIN") {
                                    onNavigate(ScreenRoute.ADMIN)
                                } else {
                                    onNavigate(ScreenRoute.STUDENT_DASHBOARD)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (currentUser?.role == "ADMIN") "Open Admin Portal" else "Go to My Dashboard")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                authRepository.logout()
                                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("auth_logout_button")
                        ) {
                            Text("Logout", color = BrandRoseLight)
                        }
                    }
                }
            } else {
                // Login / Signup Form
                Text(
                    text = if (isSignupMode) "Join AnsarAcademy" else "Welcome Back Student",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = if (isSignupMode) "Create your student account to access courses" else "Enter credentials to access your purchased lessons",
                    fontSize = 12.sp,
                    color = DarkTextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Role selector toggle (Student vs Admin)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = !isAdminRoleSelected,
                        onClick = { isAdminRoleSelected = false },
                        label = { Text("Student Login") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandPurple,
                            selectedLabelColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = isAdminRoleSelected,
                        onClick = { isAdminRoleSelected = true },
                        label = { Text("Admin / Owner") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandGold,
                            selectedLabelColor = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSignupMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("auth_name_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (isSignupMode) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                if (!isSignupMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showForgotPasswordDialog = true }) {
                            Text("Forgot Password?", color = BrandRoseLight, fontSize = 11.sp)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Button(
                    onClick = {
                        val role = if (isAdminRoleSelected) "ADMIN" else "STUDENT"
                        if (isSignupMode) {
                            authRepository.signup(name.ifBlank { "Student" }, email, phone)
                            Toast.makeText(context, "Account created! Welcome!", Toast.LENGTH_SHORT).show()
                        } else {
                            authRepository.login(email, if (isAdminRoleSelected) "Admin" else "Aman Verma", role)
                            Toast.makeText(context, "Logged in as $role", Toast.LENGTH_SHORT).show()
                        }
                        if (role == "ADMIN") onNavigate(ScreenRoute.ADMIN) else onNavigate(ScreenRoute.STUDENT_DASHBOARD)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isAdminRoleSelected) BrandGold else BrandPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_submit_button")
                ) {
                    Text(
                        text = if (isSignupMode) "Sign Up & Start Learning" else "Sign In",
                        color = if (isAdminRoleSelected) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(onClick = { isSignupMode = !isSignupMode }) {
                    Text(
                        text = if (isSignupMode) "Already have an account? Sign In" else "Don't have an account? Sign Up",
                        color = DarkTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    if (showForgotPasswordDialog) {
        var resetEmail by remember { mutableStateOf(email) }
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("Enter your email address to receive password reset instructions:", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showForgotPasswordDialog = false
                    Toast.makeText(context, "Reset link sent to $resetEmail", Toast.LENGTH_LONG).show()
                }) {
                    Text("Send Reset Link")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
