package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthRepository
import com.example.data.CourseRepository
import com.example.data.SampleCourseData
import com.example.model.CourseLesson
import com.example.model.CourseModule
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onNavigate: (ScreenRoute) -> Unit,
    courseRepository: CourseRepository = CourseRepository.instance,
    authRepository: AuthRepository = AuthRepository.instance
) {
    val context = LocalContext.current
    val currentUser by authRepository.currentUser.collectAsState()
    val modules by courseRepository.modules.collectAsState()
    val searchQuery by courseRepository.lessonSearchQuery.collectAsState()
    val filterIncompleteOnly by courseRepository.filterOnlyIncomplete.collectAsState()

    var activeLessonDetail by remember { mutableStateOf<Pair<CourseModule, CourseLesson>?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Curriculum, 1: Downloadable Resources

    // Calculate dynamic progress
    val allLessons = remember(modules) { modules.flatMap { it.lessons } }
    val completedCount = remember(modules) { allLessons.count { it.isCompleted } }
    val totalLessonsCount = allLessons.size
    val progressFraction = if (totalLessonsCount > 0) completedCount.toFloat() / totalLessonsCount.toFloat() else 0f
    val progressPercent = (progressFraction * 100).toInt()

    // Filter modules and lessons by keyword
    val filteredModules = remember(modules, searchQuery, filterIncompleteOnly) {
        if (searchQuery.isBlank() && !filterIncompleteOnly) {
            modules
        } else {
            modules.mapNotNull { mod ->
                val modMatches = mod.title.contains(searchQuery, ignoreCase = true) ||
                        mod.description.contains(searchQuery, ignoreCase = true)

                val matchingLessons = mod.lessons.filter { lesson ->
                    val matchesQuery = searchQuery.isBlank() ||
                            modMatches ||
                            lesson.title.contains(searchQuery, ignoreCase = true) ||
                            lesson.summary.contains(searchQuery, ignoreCase = true) ||
                            lesson.keyTakeaways.any { it.contains(searchQuery, ignoreCase = true) }

                    val matchesFilter = !filterIncompleteOnly || !lesson.isCompleted
                    matchesQuery && matchesFilter
                }

                if (matchingLessons.isNotEmpty() || modMatches) {
                    mod.copy(lessons = matchingLessons)
                } else {
                    null
                }
            }
        }
    }

    val totalMatchingLessons = remember(filteredModules) {
        filteredModules.sumOf { it.lessons.size }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎓", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Student Dashboard",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(ScreenRoute.AI_ASSISTANT) }) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Assistant",
                            tint = BrandRoseLight
                        )
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
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Welcome Card with Student Name
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Namaste, ${currentUser?.name ?: "Student"}! 👋",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Social Media Growth Mastery Masterclass",
                                    fontSize = 11.sp,
                                    color = BrandGoldLight
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BrandGreenContainer
                            ) {
                                Text(
                                    text = "ACTIVE PASS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Bar & Percentage
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Course Progress: $progressPercent%",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(
                                text = "$completedCount of $totalLessonsCount Lessons Done",
                                fontSize = 11.sp,
                                color = DarkTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .testTag("dashboard_progress_bar"),
                            color = BrandGreen,
                            trackColor = Color(0xFF231B3D)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // "Continue Learning" Button
                        Button(
                            onClick = {
                                val nextLessonPair = courseRepository.getNextUncompletedLesson()
                                if (nextLessonPair != null) {
                                    activeLessonDetail = nextLessonPair
                                } else {
                                    Toast.makeText(context, "All lessons completed! Great job!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("continue_learning_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Continue Learning (Next Lesson)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Quick Nav Tabs: [Curriculum & Lessons] vs [Downloadable Resources]
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurface,
                    contentColor = BrandPurpleLight,
                    indicator = {},
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "Course Lessons ($totalLessonsCount)",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) BrandPurpleLight else DarkTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "Resources & Templates 📂",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) BrandRoseLight else DarkTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            if (selectedTab == 0) {
                // --- LESSON & MODULE KEYWORD SEARCH BAR ---
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { courseRepository.updateLessonSearchQuery(it) },
                        placeholder = { Text("Filter lessons by keyword (e.g. hooks, reels, bio, cold dm)...", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = BrandPurpleLight
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { courseRepository.updateLessonSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear Search",
                                        tint = DarkTextMuted
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_lesson_search_bar"),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface,
                            focusedBorderColor = BrandPurpleLight,
                            unfocusedBorderColor = DarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter Status & Filter Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (searchQuery.isBlank())
                                "Showing all $totalMatchingLessons lessons"
                            else
                                "Found $totalMatchingLessons lessons matching \"$searchQuery\"",
                            fontSize = 11.sp,
                            color = if (searchQuery.isBlank()) DarkTextMuted else BrandGold
                        )

                        FilterChip(
                            selected = filterIncompleteOnly,
                            onClick = { courseRepository.toggleFilterOnlyIncomplete() },
                            label = { Text("Uncompleted Only", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandRose.copy(alpha = 0.25f),
                                selectedLabelColor = BrandRoseLight
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // If search yielded no results
                if (filteredModules.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🔍", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No lessons found for \"$searchQuery\"",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Try searching for keywords like 'reels', 'hook', 'canva', 'calendar', or 'client'.",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                TextButton(onClick = { courseRepository.updateLessonSearchQuery("") }) {
                                    Text("Clear Filter", color = BrandPurpleLight)
                                }
                            }
                        }
                    }
                }

                // Modules and Lessons List
                items(filteredModules) { module ->
                    var isExpanded by remember(searchQuery) {
                        // Automatically expand matching modules when searching
                        mutableStateOf(searchQuery.isNotBlank() || module.number == 1)
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { isExpanded = !isExpanded }
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
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${module.lessons.size} lessons • ${module.completedLessonsCount}/${module.lessons.size} done",
                                        fontSize = 11.sp,
                                        color = if (module.isAllCompleted) BrandGreen else DarkTextSecondary
                                    )
                                }

                                if (module.isAllCompleted) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandGreenContainer
                                    ) {
                                        Text(
                                            text = "DONE ✓",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Expand",
                                        tint = DarkTextMuted
                                    )
                                }
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    module.lessons.forEach { lesson ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (lesson.isCompleted) Color(0xFF142018) else DarkSurfaceVariant,
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (lesson.isCompleted) BrandGreen.copy(alpha = 0.3f) else DarkBorder
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp)
                                                .clickable { activeLessonDetail = Pair(module, lesson) }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            courseRepository.toggleLessonCompletion(module.id, lesson.id)
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = if (lesson.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                            contentDescription = "Complete Toggle",
                                                            tint = if (lesson.isCompleted) BrandGreen else DarkTextMuted,
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column {
                                                        Text(
                                                            text = lesson.title,
                                                            fontSize = 12.sp,
                                                            fontWeight = if (lesson.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                                                            color = if (lesson.isCompleted) DarkTextSecondary else Color.White
                                                        )
                                                        Text(
                                                            text = "${lesson.durationMinutes} mins • Tap to study",
                                                            fontSize = 10.sp,
                                                            color = DarkTextMuted
                                                        )
                                                    }
                                                }

                                                Icon(
                                                    imageVector = Icons.Default.PlayCircle,
                                                    contentDescription = "Play Lesson",
                                                    tint = BrandPurpleLight,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Tab 1: Downloadable Resources Section
                item {
                    Text(
                        text = "VIP Student Resources & Templates",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Sabhi swipe files, contracts aur templates direct download karein:",
                        fontSize = 11.sp,
                        color = DarkTextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(SampleCourseData.downloadableResources) { resource ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = resource.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${resource.category} • ${resource.fileSize}",
                                    fontSize = 11.sp,
                                    color = BrandRoseLight
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = resource.description,
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Downloading ${resource.title}...", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download",
                                    tint = BrandGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet / Dialog for Lesson Detail & Video Player
    if (activeLessonDetail != null) {
        val (mod, lesson) = activeLessonDetail!!

        AlertDialog(
            onDismissRequest = { activeLessonDetail = null },
            title = {
                Text(
                    text = lesson.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            },
            text = {
                Column {
                    // Video player simulation box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = "Play",
                                tint = BrandRose,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "HD Video Player (${lesson.durationMinutes} mins)",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Lesson Overview:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = BrandGoldLight
                    )
                    Text(
                        text = lesson.summary,
                        fontSize = 12.sp,
                        color = DarkTextSecondary,
                        lineHeight = 18.sp
                    )

                    if (lesson.keyTakeaways.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Key Action Items:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = BrandPurpleLight
                        )
                        lesson.keyTakeaways.forEach { takeaway ->
                            Text(
                                text = "• $takeaway",
                                fontSize = 11.sp,
                                color = DarkTextPrimary,
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        courseRepository.toggleLessonCompletion(mod.id, lesson.id)
                        activeLessonDetail = null
                        Toast.makeText(
                            context,
                            if (!lesson.isCompleted) "Marked as completed! 🎉" else "Marked as incomplete",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (lesson.isCompleted) DarkSurfaceVariant else BrandGreen
                    )
                ) {
                    Text(if (lesson.isCompleted) "Mark as Incomplete" else "Mark as Completed ✓")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeLessonDetail = null }) {
                    Text("Close", color = DarkTextSecondary)
                }
            }
        )
    }
}
