package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.UserAccount
import com.example.ui.theme.*
import kotlinx.coroutines.delay

enum class ScreenRoute {
    LANDING,
    COURSE_DETAILS,
    AI_QUIZ,
    AI_ASSISTANT,
    CHECKOUT,
    STUDENT_DASHBOARD,
    ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnsarAcademyTopBar(
    currentScreen: ScreenRoute,
    currentUser: UserAccount?,
    onNavigate: (ScreenRoute) -> Unit,
    onAuthClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().testTag("top_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigate(ScreenRoute.LANDING) }
                    .padding(4.dp)
            ) {
                // Circular Professional AnsarAcademy Logo
                Box(
                    modifier = Modifier
                        .size(38.dp)
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
                        contentDescription = "AnsarAcademy Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Ansar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = BrandRose.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "ACADEMY",
                                color = BrandRoseLight,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "AnsarAcademy • Growth Mastery",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            // Quick actions: User Account / Login pill
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentUser != null) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                if (currentUser.role == "ADMIN") {
                                    onNavigate(ScreenRoute.ADMIN)
                                } else {
                                    onNavigate(ScreenRoute.STUDENT_DASHBOARD)
                                }
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (currentUser.role == "ADMIN") BrandGold else BrandPurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (currentUser.role == "ADMIN") "👑" else "🎓",
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentUser.name.split(" ").firstOrNull() ?: "User",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onAuthClick,
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("login_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Login",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Login", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AnsarAcademyBottomNav(
    currentScreen: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.LANDING,
            onClick = { onNavigate(ScreenRoute.LANDING) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPurpleLight,
                indicatorColor = BrandPurpleContainer
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.COURSE_DETAILS,
            onClick = { onNavigate(ScreenRoute.COURSE_DETAILS) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Course") },
            label = { Text("Course", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPurpleLight,
                indicatorColor = BrandPurpleContainer
            ),
            modifier = Modifier.testTag("nav_course")
        )
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.AI_ASSISTANT || currentScreen == ScreenRoute.AI_QUIZ,
            onClick = { onNavigate(ScreenRoute.AI_ASSISTANT) },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Tools") },
            label = { Text("AI Tools", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandRoseLight,
                indicatorColor = BrandPurpleContainer
            ),
            modifier = Modifier.testTag("nav_ai")
        )
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.STUDENT_DASHBOARD,
            onClick = { onNavigate(ScreenRoute.STUDENT_DASHBOARD) },
            icon = { Icon(Icons.Default.School, contentDescription = "Dashboard") },
            label = { Text("Dashboard", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandGreen,
                indicatorColor = BrandGreenContainer
            ),
            modifier = Modifier.testTag("nav_dashboard")
        )
        NavigationBarItem(
            selected = currentScreen == ScreenRoute.ADMIN,
            onClick = { onNavigate(ScreenRoute.ADMIN) },
            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
            label = { Text("Admin", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandGold,
                indicatorColor = BrandPurpleContainer
            ),
            modifier = Modifier.testTag("nav_admin")
        )
    }
}

/**
 * Limited-time offer countdown timer with live ticking
 */
@Composable
fun CountdownBanner(
    onEnrollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var secondsLeft by remember { mutableIntStateOf(14820) } // ~4h 7m

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600) / 60
    val seconds = secondsLeft % 60
    val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Surface(
        color = Color(0xFF1E0E38),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "🔥",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Limited Launch Offer (80% OFF)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandGold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = BrandRose
                        ) {
                            Text(
                                text = "Ends in $timeFormatted",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Original ₹4,999 → Abhi sirf ₹999 me enroll karein!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = DarkTextSecondary
                        )
                    )
                }
            }

            Button(
                onClick = onEnrollClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandRose
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("banner_enroll_button")
            ) {
                Text(
                    text = "Claim ₹999",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Sticky Mobile CTA bar at bottom
 */
@Composable
fun StickyEnrollBar(
    onEnrollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("sticky_enroll_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹999",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = BrandGreen
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "₹4,999",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = BrandRose.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "80% OFF",
                            color = BrandRoseLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
                Text(
                    text = "Lifetime Access • 7-Day Refund Guarantee",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Button(
                onClick = onEnrollClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPurple
                ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                modifier = Modifier.testTag("sticky_buy_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Enroll Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Enroll",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
