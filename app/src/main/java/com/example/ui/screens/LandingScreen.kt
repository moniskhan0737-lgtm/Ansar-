package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseRepository
import com.example.data.SampleCourseData
import com.example.model.CourseModule
import com.example.model.FaqItem
import com.example.model.Testimonial
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onNavigate: (ScreenRoute) -> Unit,
    repository: CourseRepository = CourseRepository.instance
) {
    val courseInfo by repository.courseInfo.collectAsState()
    val modules by repository.modules.collectAsState()
    val testimonials by repository.testimonials.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("landing_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // --- 1. HERO SECTION ---
        item {
            HeroSection(
                onBuyClick = { onNavigate(ScreenRoute.CHECKOUT) },
                onQuizClick = { onNavigate(ScreenRoute.AI_QUIZ) },
                onViewDetails = { onNavigate(ScreenRoute.COURSE_DETAILS) }
            )
        }

        // --- 2. SOCIAL PROOF METRICS ---
        item {
            MetricsBar()
        }

        // --- 3. LIMITED-TIME PRICING & OFFER ---
        item {
            PricingOfferSection(
                onEnrollClick = { onNavigate(ScreenRoute.CHECKOUT) }
            )
        }

        // --- 4. WHAT YOU WILL LEARN ---
        item {
            WhatYouWillLearnSection()
        }

        // --- 5. COURSE MODULES PREVIEW ---
        item {
            ModulesPreviewSection(
                modules = modules,
                onViewFullCurriculum = { onNavigate(ScreenRoute.COURSE_DETAILS) }
            )
        }

        // --- 6. STUDENT BENEFITS ---
        item {
            StudentBenefitsSection()
        }

        // --- 7. STUDENT TESTIMONIALS ---
        item {
            TestimonialsSection(testimonials = testimonials)
        }

        // --- 8. MONEY BACK GUARANTEE ---
        item {
            MoneyBackGuaranteeSection(onEnrollClick = { onNavigate(ScreenRoute.CHECKOUT) })
        }

        // --- 9. FAQ SECTION ---
        item {
            FaqSection()
        }

        // --- 10. FINAL CTA BANNER & FOOTER ---
        item {
            FinalCtaSection(onEnrollClick = { onNavigate(ScreenRoute.CHECKOUT) })
        }

        item {
            FooterSection()
        }
    }
}

@Composable
private fun HeroSection(
    onBuyClick: () -> Unit,
    onQuizClick: () -> Unit,
    onViewDetails: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF241242), DarkBackground)
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Circular AnsarAcademy Logo with modern gradient ring border
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .border(
                        2.5.dp,
                        Brush.linearGradient(listOf(BrandRose, BrandGold, BrandPurpleLight)),
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

            Spacer(modifier = Modifier.height(14.dp))

            // Urgency badge
            Surface(
                shape = RoundedCornerShape(30.dp),
                color = BrandRose.copy(alpha = 0.25f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandRose.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("🔥", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Batch 2026 Admissions Open • 80% OFF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandRoseLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Headline
            Text(
                text = "Social Media Se Apna Career Aur Business Grow Karo 🚀",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 34.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("hero_headline")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subheadline
            Text(
                text = "Zero se shuru karke Instagram algorithm hack karo, viral reels create karo aur high-paying freelance clients ya brand deals se monthly 6-figure income banao!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = DarkTextSecondary,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Rating & Students Social Proof
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1D1735))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("⭐⭐⭐⭐⭐", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "4.9/5 Rating (3,400+ Students)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandGoldLight
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary CTAs
            Button(
                onClick = onBuyClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPurple
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("hero_buy_course_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Buy Course Now — ₹999",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(₹4,999)",
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive AI Quiz CTA
            OutlinedButton(
                onClick = onQuizClick,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandRose),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("hero_quiz_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Take 60-Sec AI Course Quiz",
                        fontWeight = FontWeight.SemiBold,
                        color = BrandRoseLight,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricsBar() {
    Surface(
        color = DarkSurface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MetricItem(number = "10", label = "Modules")
            MetricItem(number = "32+", label = "Lessons")
            MetricItem(number = "24+ Hrs", label = "Content")
            MetricItem(number = "₹999", label = "Offer Price")
        }
    }
}

@Composable
private fun MetricItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = BrandPurpleLight
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = DarkTextMuted
        )
    }
}

@Composable
private fun PricingOfferSection(onEnrollClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurfaceVariant
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandGreenContainer
                ) {
                    Text(
                        text = "80% DISCOUNT ACTIVE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF2A2247)
                ) {
                    Text(
                        text = "Lifetime Access",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "₹999",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "₹4,999",
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.LineThrough,
                    color = DarkTextMuted,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Text(
                text = "⚡ Limited seats launch price (Editable sample values)",
                fontSize = 11.sp,
                color = BrandGold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // What's included checklist
            val includes = listOf(
                "Full 10 Modules (All 32+ In-Depth Lessons)",
                "Built-in Gemini AI Social Media Assistant",
                "100+ Viral Hooks & Scripts Swipe File (PDF)",
                "Weekly VIP Community & Live Doubt Sessions",
                "Course Completion Certificate for Resume/Clients"
            )

            includes.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Check",
                        tint = BrandGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = DarkTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onEnrollClick,
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("pricing_enroll_button")
            ) {
                Text("Enroll Abhi Karein (₹999)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun WhatYouWillLearnSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "What You Will Learn",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = "Yeh course practical results par focus karta hai, sirf theory nahi.",
            fontSize = 13.sp,
            color = DarkTextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        val learnItems = listOf(
            Triple("🎯", "Algorithm & SEO Mastery", "Samjho kaise Instagram aapki reels ko explore feed par promote karta hai."),
            Triple("🎬", "Viral Video Production", "Smartphone camera se high-retention video shooting aur CapCut editing tricks."),
            Triple("🪝", "Stop-The-Scroll Hooks", "Pehle 3 second me viewers ka attention grab karne wale verified formulas."),
            Triple("📅", "30-Day Content Engine", "Notion aur Sheets template se bina stress ke consistent content banana."),
            Triple("💼", "High-Paying Client Pitching", "Cold DMs aur proposals ke zariye ₹25k-₹50k/month ke retainers close karna."),
            Triple("🤖", "Gemini AI Automation", "AI se reels script, captions aur hashtags 2 minute me generate karna.")
        )

        learnItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { (emoji, title, desc) ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(emoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = desc,
                                fontSize = 11.sp,
                                color = DarkTextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModulesPreviewSection(
    modules: List<CourseModule>,
    onViewFullCurriculum: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Course Curriculum",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "10 Complete Modules • 32+ Lessons",
                    fontSize = 12.sp,
                    color = BrandPurpleLight
                )
            }
            TextButton(onClick = onViewFullCurriculum) {
                Text("View All", color = BrandRoseLight, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Display first 4 modules preview with expand capability
        modules.take(4).forEach { module ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = module.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${module.lessons.size} lessons • ${module.totalDurationMinutes} mins",
                                fontSize = 11.sp,
                                color = DarkTextMuted
                            )
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = DarkTextSecondary
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = module.description,
                                fontSize = 12.sp,
                                color = DarkTextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            module.lessons.forEach { lesson ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = "Lesson",
                                        tint = BrandPurpleLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = lesson.title,
                                        fontSize = 12.sp,
                                        color = DarkTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentBenefitsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Student Benefits & VIP Perks",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = "AnsarAcademy ke enroll hone par aapko kya kya milta hai:",
            fontSize = 12.sp,
            color = DarkTextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SampleCourseData.studentBenefits.forEach { benefit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(benefit.iconEmoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = benefit.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = benefit.description,
                        fontSize = 11.sp,
                        color = DarkTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TestimonialsSection(testimonials: List<Testimonial>) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Real Student Success Stories",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "Dekhein kaise students ne zero se growth unlock ki (Editable Sample Data):",
                fontSize = 12.sp,
                color = DarkTextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testimonials) { item ->
                Card(
                    modifier = Modifier.width(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(item.avatarEmoji, fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.studentName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = item.role,
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandGreenContainer
                        ) {
                            Text(
                                text = "🏆 ${item.resultBadge}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "\"${item.commentHinglish}\"",
                            fontSize = 12.sp,
                            color = DarkTextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MoneyBackGuaranteeSection(onEnrollClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF14241C)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreen.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🛡️", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "7-Day 100% Money-Back Guarantee",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = BrandGreen,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Agar aapko lagta hai ki yeh course aapke liye nahi hai ya aapko value nahi mili, toh enroll hone ke 7 dino ke andar hume WhatsApp/Email karein. No questions asked — 100% full refund!",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onEnrollClick,
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Enroll Risk-Free (₹999)", color = BrandGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FaqSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Frequently Asked Questions (FAQ)",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        SampleCourseData.faqs.forEach { faq ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { expanded = !expanded }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = faq.questionHinglish,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                            contentDescription = "Toggle",
                            tint = BrandPurpleLight
                        )
                    }

                    AnimatedVisibility(visible = expanded) {
                        Text(
                            text = faq.answerHinglish,
                            fontSize = 12.sp,
                            color = DarkTextSecondary,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FinalCtaSection(onEnrollClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(BrandPurpleDark, BrandPurple)
                )
            )
            .padding(22.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Social Media Se Mahine Ke ₹50K - ₹1L+ Kamana Seekho! 🚀",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Launch offer price ₹999 jald hi end ho jayega. Apni seat abhi secure karein!",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onEnrollClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("final_cta_button")
            ) {
                Text(
                    text = "Abhi Enroll Karein (Sirf ₹999)",
                    color = BrandPurpleDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AnsarAcademy • Growth Mastery",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = DarkTextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Support: support@ansaracademy.com • WhatsApp: +91 98765 43210",
            fontSize = 11.sp,
            color = DarkTextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "© 2026 AnsarAcademy. All sample data editable.",
            fontSize = 10.sp,
            color = DarkTextMuted
        )
    }
}
