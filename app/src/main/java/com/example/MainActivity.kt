package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.AuthRepository
import com.example.data.CourseRepository
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AnsarAcademyApp()
            }
        }
    }
}

@Composable
fun AnsarAcademyApp(
    authRepository: AuthRepository = AuthRepository.instance,
    courseRepository: CourseRepository = CourseRepository.instance
) {
    var screenStack by remember { mutableStateOf(listOf(ScreenRoute.LANDING)) }
    val currentScreen = screenStack.lastOrNull() ?: ScreenRoute.LANDING

    val currentUser by authRepository.currentUser.collectAsState()
    var showAuthScreen by remember { mutableStateOf(false) }

    fun navigateTo(route: ScreenRoute) {
        if (currentScreen != route) {
            screenStack = screenStack + route
        }
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack = screenStack.dropLast(1)
        } else {
            screenStack = listOf(ScreenRoute.LANDING)
        }
    }

    // Handle system back navigation
    BackHandler(enabled = screenStack.size > 1 || showAuthScreen) {
        if (showAuthScreen) {
            showAuthScreen = false
        } else {
            navigateBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("app_scaffold"),
        topBar = {
            Column {
                AnsarAcademyTopBar(
                    currentScreen = currentScreen,
                    currentUser = currentUser,
                    onNavigate = { route -> navigateTo(route) },
                    onAuthClick = { showAuthScreen = true }
                )
                // Countdown offer banner for Landing and Details pages
                if (currentScreen == ScreenRoute.LANDING || currentScreen == ScreenRoute.COURSE_DETAILS) {
                    CountdownBanner(
                        onEnrollClick = { navigateTo(ScreenRoute.CHECKOUT) }
                    )
                }
            }
        },
        bottomBar = {
            Column {
                // Sticky "Enroll Now" bar for Landing and Course Details screens
                if (currentScreen == ScreenRoute.LANDING || currentScreen == ScreenRoute.COURSE_DETAILS) {
                    StickyEnrollBar(
                        onEnrollClick = { navigateTo(ScreenRoute.CHECKOUT) }
                    )
                }

                // Global Bottom Navigation Bar
                AnsarAcademyBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { route -> navigateTo(route) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            if (showAuthScreen) {
                AuthScreen(
                    onNavigate = { route ->
                        showAuthScreen = false
                        navigateTo(route)
                    }
                )
            } else {
                when (currentScreen) {
                    ScreenRoute.LANDING -> LandingScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                    ScreenRoute.COURSE_DETAILS -> CourseDetailsScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                    ScreenRoute.AI_QUIZ -> AiQuizScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                    ScreenRoute.AI_ASSISTANT -> AiAssistantScreen()
                    ScreenRoute.CHECKOUT -> CheckoutScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                    ScreenRoute.STUDENT_DASHBOARD -> StudentDashboardScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                    ScreenRoute.ADMIN -> AdminScreen(
                        onNavigate = { route -> navigateTo(route) }
                    )
                }
            }
        }
    }
}
