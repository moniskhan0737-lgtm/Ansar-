package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.CourseRepository
import com.example.model.CourseModule
import com.example.model.EnrollmentRecord
import com.example.ui.components.ScreenRoute
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onNavigate: (ScreenRoute) -> Unit,
    courseRepository: CourseRepository = CourseRepository.instance
) {
    val context = LocalContext.current
    val modules by courseRepository.modules.collectAsState()
    val testimonials by courseRepository.testimonials.collectAsState()
    val enrolledStudents by courseRepository.enrolledStudents.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: KPIs & Sales, 1: Modules & Lessons, 2: Students, 3: Testimonials

    var showAddModuleDialog by remember { mutableStateOf(false) }
    var showAddLessonDialogForModuleId by remember { mutableStateOf<String?>(null) }
    var showAddTestimonialDialog by remember { mutableStateOf(false) }

    val (totalStudents, totalSales, grossRevenue) = remember(enrolledStudents) {
        courseRepository.getAdminStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("👑", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Course Owner Admin Portal", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Manage curriculum, sales & students", fontSize = 11.sp, color = BrandGold)
                        }
                    }
                },
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Admin Tab Bar
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkSurface,
                    contentColor = BrandGold,
                    edgePadding = 0.dp
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("KPIs & Sales", fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Curriculum (${modules.size})", fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Students (${enrolledStudents.size})", fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("Testimonials (${testimonials.size})", fontSize = 11.sp) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            when (selectedTab) {
                0 -> {
                    // TAB 0: KPIs & ANALYTICS OVERVIEW
                    item {
                        Text("Analytics & Revenue Overview", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            KpiCard(
                                modifier = Modifier.weight(1f),
                                title = "Gross Revenue",
                                value = "₹${grossRevenue}",
                                subtitle = "From ${totalSales} sales",
                                icon = "💰",
                                color = BrandGreen
                            )
                            KpiCard(
                                modifier = Modifier.weight(1f),
                                title = "Total Students",
                                value = "$totalStudents",
                                subtitle = "Enrolled students",
                                icon = "👥",
                                color = BrandPurpleLight
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            KpiCard(
                                modifier = Modifier.weight(1f),
                                title = "Conversion Rate",
                                value = "7.8%",
                                subtitle = "Landing to Checkout",
                                icon = "📈",
                                color = BrandRoseLight
                            )
                            KpiCard(
                                modifier = Modifier.weight(1f),
                                title = "Offer Pricing",
                                value = "₹999",
                                subtitle = "MRP: ₹4,999 (80% off)",
                                icon = "🏷️",
                                color = BrandGold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Recent Quick Actions", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        onClick = { showAddModuleDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+ Add Module", fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = { showAddTestimonialDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("+ Testimonial", fontSize = 11.sp, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: CURRICULUM MANAGER (ADD/EDIT/DELETE MODULES & LESSONS)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Manage Course Modules & Lessons", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Button(
                                onClick = { showAddModuleDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("+ Add Module", fontSize = 11.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(modules) { module ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(module.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                        Text("${module.lessons.size} lessons • ${module.description}", fontSize = 11.sp, color = DarkTextSecondary)
                                    }
                                    Row {
                                        IconButton(onClick = { showAddLessonDialogForModuleId = module.id }) {
                                            Icon(Icons.Default.Add, contentDescription = "Add Lesson", tint = BrandGreen)
                                        }
                                        IconButton(onClick = {
                                            courseRepository.deleteModule(module.id)
                                            Toast.makeText(context, "Module deleted", Toast.LENGTH_SHORT).show()
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete Module", tint = BrandRoseLight)
                                        }
                                    }
                                }

                                if (module.lessons.isNotEmpty()) {
                                    HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(vertical = 8.dp))
                                    module.lessons.forEach { lesson ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("• ${lesson.title} (${lesson.durationMinutes}m)", fontSize = 11.sp, color = DarkTextPrimary, modifier = Modifier.weight(1f))
                                            IconButton(
                                                onClick = {
                                                    courseRepository.deleteLesson(module.id, lesson.id)
                                                    Toast.makeText(context, "Lesson removed", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Remove Lesson", tint = DarkTextMuted, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: ENROLLED STUDENTS LIST
                    item {
                        Text("Enrolled Students Directory", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text("Verified registrations and payments:", fontSize = 11.sp, color = DarkTextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(enrolledStudents) { record ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(record.studentName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                        Text(record.studentEmail, fontSize = 11.sp, color = DarkTextSecondary)
                                    }
                                    Surface(shape = RoundedCornerShape(6.dp), color = BrandGreenContainer) {
                                        Text(
                                            "₹${record.amount} PAID",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = BrandGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Order: ${record.orderId}", fontSize = 10.sp, color = DarkTextMuted)
                                    Text("Via: ${record.paymentMethod}", fontSize = 10.sp, color = BrandPurpleLight)
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: TESTIMONIALS MANAGER
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Manage Social Proof Testimonials", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Button(
                                onClick = { showAddTestimonialDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("+ Add Review", fontSize = 11.sp, color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(testimonials) { testItem ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(testItem.avatarEmoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(testItem.studentName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("(${testItem.role})", fontSize = 11.sp, color = DarkTextMuted)
                                    }
                                    Text("\"${testItem.commentHinglish}\"", fontSize = 11.sp, color = DarkTextSecondary)
                                    Text("Badge: ${testItem.resultBadge}", fontSize = 10.sp, color = BrandGreen)
                                }
                                IconButton(onClick = {
                                    courseRepository.deleteTestimonial(testItem.id)
                                    Toast.makeText(context, "Testimonial removed", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BrandRoseLight)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for Adding Module
    if (showAddModuleDialog) {
        var modTitle by remember { mutableStateOf("") }
        var modDesc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddModuleDialog = false },
            title = { Text("Add New Course Module") },
            text = {
                Column {
                    OutlinedTextField(
                        value = modTitle,
                        onValueChange = { modTitle = it },
                        label = { Text("Module Title (e.g. AI Content Systems)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = modDesc,
                        onValueChange = { modDesc = it },
                        label = { Text("Module Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (modTitle.isNotBlank()) {
                        courseRepository.addNewModule(modTitle, modDesc)
                        showAddModuleDialog = false
                        Toast.makeText(context, "Module created successfully!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Save Module")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddModuleDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Dialog for Adding Lesson to a Module
    if (showAddLessonDialogForModuleId != null) {
        val targetModId = showAddLessonDialogForModuleId!!
        var lessonTitle by remember { mutableStateOf("") }
        var lessonDuration by remember { mutableStateOf("25") }
        var lessonSummary by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddLessonDialogForModuleId = null },
            title = { Text("Add Lesson to Module") },
            text = {
                Column {
                    OutlinedTextField(
                        value = lessonTitle,
                        onValueChange = { lessonTitle = it },
                        label = { Text("Lesson Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lessonDuration,
                        onValueChange = { lessonDuration = it },
                        label = { Text("Duration (minutes)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = lessonSummary,
                        onValueChange = { lessonSummary = it },
                        label = { Text("Lesson Summary") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (lessonTitle.isNotBlank()) {
                        val duration = lessonDuration.toIntOrNull() ?: 20
                        courseRepository.addLessonToModule(targetModId, lessonTitle, duration, lessonSummary)
                        showAddLessonDialogForModuleId = null
                        Toast.makeText(context, "Lesson added!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Add Lesson")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddLessonDialogForModuleId = null }) { Text("Cancel") }
            }
        )
    }

    // Dialog for Adding Testimonial
    if (showAddTestimonialDialog) {
        var studentName by remember { mutableStateOf("") }
        var studentRole by remember { mutableStateOf("") }
        var comment by remember { mutableStateOf("") }
        var resultBadge by remember { mutableStateOf("+10k Followers") }

        AlertDialog(
            onDismissRequest = { showAddTestimonialDialog = false },
            title = { Text("Add Student Testimonial") },
            text = {
                Column {
                    OutlinedTextField(value = studentName, onValueChange = { studentName = it }, label = { Text("Student Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = studentRole, onValueChange = { studentRole = it }, label = { Text("Role / Handle") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Student Review (Hinglish)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(value = resultBadge, onValueChange = { resultBadge = it }, label = { Text("Result Badge (e.g. ₹50k Client Closed)") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (studentName.isNotBlank() && comment.isNotBlank()) {
                        courseRepository.addTestimonial(studentName, studentRole, comment, resultBadge)
                        showAddTestimonialDialog = false
                        Toast.makeText(context, "Testimonial saved!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Add Testimonial")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTestimonialDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun KpiCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = DarkTextSecondary)
                Text(icon, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = color)
            Text(subtitle, fontSize = 10.sp, color = DarkTextMuted)
        }
    }
}
