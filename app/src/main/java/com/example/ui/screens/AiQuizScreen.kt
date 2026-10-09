package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiService
import com.example.model.QuizAnswers
import com.example.model.QuizRecommendation
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiQuizScreen(
    onNavigate: (ScreenRoute) -> Unit,
    geminiService: GeminiService = remember { GeminiService() }
) {
    val coroutineScope = rememberCoroutineScope()
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 5

    var experience by remember { mutableStateOf("Beginner (Just starting out)") }
    var followers by remember { mutableStateOf("Under 1,000 Followers") }
    var goal by remember { mutableStateOf("Earn from Freelancing & Clients") }
    var status by remember { mutableStateOf("College Student / Fresher") }
    var problem by remember { mutableStateOf("Reels par views nahi aate (Stuck at 200 views)") }

    var isLoading by remember { mutableStateOf(false) }
    var recommendationResult by remember { mutableStateOf<QuizRecommendation?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (recommendationResult == null) "AI Course Recommendation Quiz" else "Aapka Personalized Plan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (recommendationResult != null) {
                            recommendationResult = null
                        } else if (currentStep > 1) {
                            currentStep -= 1
                        } else {
                            onNavigate(ScreenRoute.LANDING)
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            if (isLoading) {
                // Loading state with AI pulse
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = BrandRose)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Gemini AI Aapke Answers Analyse Kar Raha Hai... 🧠",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Finding optimal modules, custom action plan & projected timeline...",
                        fontSize = 12.sp,
                        color = DarkTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (recommendationResult != null) {
                // Recommendation Result Page
                val result = recommendationResult!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BrandPurple.copy(alpha = 0.3f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = "AI",
                                            tint = BrandRoseLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "GEMINI 3.5 AI ANALYSIS",
                                            color = BrandRoseLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = result.headline,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = result.personalAnalysis,
                                    fontSize = 13.sp,
                                    color = DarkTextPrimary,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF14241C),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "🎯 Immediate Action Plan For You:",
                                            fontWeight = FontWeight.Bold,
                                            color = BrandGreen,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        result.roadmapSteps.forEach { step ->
                                            Text(
                                                text = "• $step",
                                                fontSize = 12.sp,
                                                color = Color.White,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = result.expectedOutcome,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandGold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = { onNavigate(ScreenRoute.CHECKOUT) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("quiz_enroll_custom_plan_button")
                                ) {
                                    Text(
                                        text = "Enroll With My Custom Plan (₹999)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        recommendationResult = null
                                        currentStep = 1
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Retake Quiz", color = DarkTextSecondary)
                                }
                            }
                        }
                    }
                }
            } else {
                // Quiz Questions Stepper
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Progress Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Step $currentStep of $totalSteps",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandRoseLight
                        )
                        Text(
                            text = "${(currentStep * 100) / totalSteps}% Complete",
                            fontSize = 12.sp,
                            color = DarkTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { currentStep.toFloat() / totalSteps.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = BrandRose,
                        trackColor = DarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question Content based on currentStep
                    when (currentStep) {
                        1 -> QuizQuestionView(
                            title = "1. Current Social Media Experience Kya Hai?",
                            subtitle = "Yeh batayein taaki course ka starting point match ho sake.",
                            options = listOf(
                                "Beginner (Just starting out)",
                                "Intermediate (Post karte hain par growth slow hai)",
                                "Advanced (Already have good audience, want to scale to agency/business)"
                            ),
                            selectedOption = experience,
                            onOptionSelected = { experience = it }
                        )

                        2 -> QuizQuestionView(
                            title = "2. Instagram Par Current Followers Kitne Hain?",
                            subtitle = "Follower count ke according monetization strategy badalti hai.",
                            options = listOf(
                                "Under 1,000 Followers",
                                "1,000 to 10,000 Followers",
                                "10,000 to 50,000 Followers",
                                "50,000+ Followers"
                            ),
                            selectedOption = followers,
                            onOptionSelected = { followers = it }
                        )

                        3 -> QuizQuestionView(
                            title = "3. Aapka Main Goal Kya Hai?",
                            subtitle = "Agle 30-60 dino me sabse pehle kya achieve karna chahte hain?",
                            options = listOf(
                                "Earn from Freelancing & Clients (₹30k - ₹75k/month)",
                                "Grow Personal Brand & Organic Authority",
                                "Sell Own Products / Scale Offline Business",
                                "Become a Full-Time Creator & Brand Collaborator"
                            ),
                            selectedOption = goal,
                            onOptionSelected = { goal = it }
                        )

                        4 -> QuizQuestionView(
                            title = "4. Aapka Current Status Kya Hai?",
                            subtitle = "Time management schedule tailor karne ke liye.",
                            options = listOf(
                                "College Student / Fresher",
                                "Working Professional (Job ke sath side income chahiye)",
                                "Business Owner / Entrepreneur",
                                "Full-Time Freelancer / Creator"
                            ),
                            selectedOption = status,
                            onOptionSelected = { status = it }
                        )

                        5 -> QuizQuestionView(
                            title = "5. Sabse Badi Problem Kya Face Kar Rahe Hain?",
                            subtitle = "Jiska solution aapko sabse pehle chahiye.",
                            options = listOf(
                                "Reels par views nahi aate (Stuck at 200 views)",
                                "Consistency nahi banti aur content ideas khatam ho jaate hain",
                                "Monetize kaise kare samajh nahi aata (Zero income)",
                                "Camera shyness aur editing skill ki kami"
                            ),
                            selectedOption = problem,
                            onOptionSelected = { problem = it }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Next / Submit Button
                    Button(
                        onClick = {
                            if (currentStep < totalSteps) {
                                currentStep += 1
                            } else {
                                // Final submit -> Trigger Gemini AI recommendation
                                isLoading = true
                                coroutineScope.launch {
                                    val answers = QuizAnswers(
                                        experience = experience,
                                        followers = followers,
                                        goal = goal,
                                        status = status,
                                        problem = problem
                                    )
                                    val rec = geminiService.getCourseRecommendation(answers)
                                    isLoading = false
                                    recommendationResult = rec
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("quiz_next_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (currentStep < totalSteps) "Next Question" else "Generate My AI Plan 🚀",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = if (currentStep < totalSteps) Icons.Default.ArrowForward else Icons.Default.AutoAwesome,
                                contentDescription = "Next",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizQuestionView(
    title: String,
    subtitle: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = DarkTextSecondary
        )
        Spacer(modifier = Modifier.height(18.dp))

        options.forEach { option ->
            val isSelected = option == selectedOption
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF261942) else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) BrandPurpleLight else DarkBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onOptionSelected(option) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onOptionSelected(option) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BrandPurpleLight,
                            unselectedColor = DarkTextMuted
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = option,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Color.White else DarkTextPrimary
                    )
                }
            }
        }
    }
}
