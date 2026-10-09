package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiService
import com.example.model.AssistantToolType
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    geminiService: GeminiService = remember { GeminiService() }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedTool by remember { mutableStateOf(AssistantToolType.REELS) }
    var userInput by remember { mutableStateOf("") }
    var generatedResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Quick suggestion chips based on tool type
    val sampleChips = when (selectedTool) {
        AssistantToolType.REELS -> listOf(
            "Fitness trainer weight loss reels",
            "Freelance graphic design client case study",
            "Digital marketing student study hacks",
            "Home bakery cakes behind the scenes"
        )
        AssistantToolType.CAPTIONS -> listOf(
            "Reel caption for 5 productivity habits",
            "Instagram story announcement for new course",
            "Motivation caption for morning routine",
            "Client testimonial appreciation post"
        )
        AssistantToolType.HOOKS -> listOf(
            "Finance & investment for college students",
            "Smartphone video editing shortcuts",
            "Instagram algorithm updates 2026",
            "How to get first freelance client"
        )
        AssistantToolType.CALENDAR -> listOf(
            "Personal branding coach 7-day schedule",
            "Real estate consultant content plan",
            "E-commerce clothing brand weekly posts",
            "Tech reviewer gadget comparison calendar"
        )
        AssistantToolType.CTA -> listOf(
            "DM me 'GROWTH' for free consultation",
            "Save this reel before the algorithm hides it",
            "Click link in bio to enroll at 80% off",
            "Comment your biggest problem below"
        )
        AssistantToolType.STRATEGY -> listOf(
            "Fitness coach with 2.5k followers stuck for 4 months",
            "College student wanting ₹30k/month freelancing",
            "Local cafe owner wanting more footfall from reels"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Gemini Social Media Assistant",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Powered by Gemini 3.5 AI • Hinglish + English",
                                fontSize = 11.sp,
                                color = BrandRoseLight
                            )
                        }
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Tool selector chips
            item {
                Text(
                    text = "Select Tool:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AssistantToolType.values()) { tool ->
                        val isSelected = tool == selectedTool
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTool = tool
                                generatedResult = ""
                            },
                            label = { Text(tool.title, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPurple,
                                selectedLabelColor = Color.White,
                                containerColor = DarkSurface,
                                labelColor = DarkTextPrimary
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Description of current tool
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedTool.icon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = selectedTool.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = selectedTool.description,
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Quick Prompt Chips
            item {
                Text(
                    text = "Quick Topics (Tap to fill):",
                    fontSize = 11.sp,
                    color = DarkTextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(sampleChips) { chipText ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF221A38),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.3f)),
                            modifier = Modifier.clickable { userInput = chipText }
                        ) {
                            Text(
                                text = chipText,
                                fontSize = 11.sp,
                                color = DarkTextSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Input field & Generate Button
            item {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = { Text("Topic, Niche ya apna goal enter karein...") },
                    placeholder = { Text("e.g. Fitness trainer trying to get clients with reels") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_assistant_input"),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BrandPurpleLight,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val inputToUse = if (userInput.isNotBlank()) userInput else sampleChips.first()
                        isLoading = true
                        coroutineScope.launch {
                            val response = geminiService.generateAssistantResponse(
                                toolType = selectedTool,
                                userTopicOrGoal = inputToUse
                            )
                            isLoading = false
                            generatedResult = response
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("ai_assistant_generate_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gemini Generating...")
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "Generate", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate with AI 🚀", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Generated Output Card
            if (generatedResult.isNotBlank()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_assistant_result_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("✨", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Generated AI Output",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BrandGoldLight
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("AnsarAcademy AI Output", generatedResult)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = DarkBorder,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )

                            Text(
                                text = generatedResult,
                                fontSize = 13.sp,
                                color = DarkTextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
