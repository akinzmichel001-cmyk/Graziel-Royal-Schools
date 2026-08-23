package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.StudentIdCard
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceElevated
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Rose400
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun StudentPortalScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val cbtSubmissions by viewModel.cbtSubmissions.collectAsStateWithLifecycle()
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    val isReportApproved by viewModel.isReportCardApproved.collectAsStateWithLifecycle()

    val liveCbtTests = cbtTests.filter { it.isLive }
    val mySubmissions = cbtSubmissions.filter {
        it.studentReg == (currentUser?.regOrStaffId ?: "GRS/2024/0428") || cbtTests.any { t -> t.id == it.testId && t.isResultsPublished }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Student Digital ID Card
            StudentIdCard(
                studentName = currentUser?.fullName ?: "Adeleke David Oluwaseun",
                regNumber = currentUser?.regOrStaffId ?: "GRS/2024/0428",
                studentClass = currentUser?.assignedClass ?: "SS 1 Science",
                house = "Sapphire Blue House",
                session = "2024/2025 Session • Term 2",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Section: Live CBT Exam Banner (Crucial Requirement)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (liveCbtTests.isNotEmpty()) Emerald500.copy(alpha = 0.5f) else DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (liveCbtTests.isNotEmpty()) Emerald500.copy(alpha = 0.2f) else Slate800,
                                border = BorderStroke(1.dp, if (liveCbtTests.isNotEmpty()) Emerald400 else DarkBorderSubtle),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = if (liveCbtTests.isNotEmpty()) Emerald400 else Slate400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (liveCbtTests.isNotEmpty()) "LIVE CBT EXAM ACTIVE" else "CBT Assessment Center",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (liveCbtTests.isNotEmpty()) Emerald400 else Slate100
                                )
                                Text(
                                    text = if (liveCbtTests.isNotEmpty()) "${liveCbtTests.size} test(s) live right now" else "No live tests right now",
                                    fontSize = 11.sp,
                                    color = Slate400
                                )
                            }
                        }

                        if (liveCbtTests.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Emerald500,
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.FiberManualRecord, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("LIVE NOW", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = DarkCanvas)
                                }
                            }
                        }
                    }

                    if (liveCbtTests.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        liveCbtTests.forEach { test ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Slate900, RoundedCornerShape(12.dp))
                                    .border(BorderStroke(1.dp, Emerald400.copy(alpha = 0.3f)), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(test.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("${test.subject} • ${test.durationMinutes} mins • ${test.totalMarks} marks", fontSize = 11.sp, color = Amber400)
                                }

                                Button(
                                    onClick = { viewModel.startCbtExam(test) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("student_take_cbt_button_${test.id}")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = Slate100)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Take Test", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "When your subject teacher clicks 'Go Live', the interactive test will appear here with a timer countdown.",
                            fontSize = 12.sp,
                            color = Slate400,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Section: Quick Academic Actions (Class Chat, AI Tutor, Homework, Timetable)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(AppDestination.GROUP_CHAT) }
                        .testTag("student_open_class_chat_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Forum, contentDescription = null, tint = Indigo400, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Class Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("SS 1 Science Group", fontSize = 11.sp, color = Slate400)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(AppDestination.AI_TUTOR) }
                        .testTag("student_open_ai_tutor_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Amber400, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("AI Tutor", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("Ask Homework Bot", fontSize = 11.sp, color = Slate400)
                    }
                }
            }
        }

        // Section: Published CBT Results & Scores
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Published CBT Exam Results", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (mySubmissions.isEmpty()) {
                        Text("No released CBT grades yet.", fontSize = 12.sp, color = Slate400)
                    } else {
                        mySubmissions.forEach { sub ->
                            val matchingTest = cbtTests.find { it.id == sub.testId }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Slate900, RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(matchingTest?.title ?: "Assessment Test", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                    Text("Grade: ${sub.score}/${sub.maxScore} (${String.format("%.0f", sub.percentage)}%)", fontSize = 11.sp, color = Indigo400)
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (sub.percentage >= 60) Emerald500.copy(alpha = 0.2f) else Amber500.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = if (sub.percentage >= 80) "Royal A+" else if (sub.percentage >= 60) "Credit B" else "Pass C",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sub.percentage >= 60) Emerald400 else Amber400,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Official Approved Report Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Grade, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Official Term Report Card", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        if (isReportApproved) {
                            Surface(shape = RoundedCornerShape(8.dp), color = Emerald500.copy(alpha = 0.2f)) {
                                Text("Admin Approved", color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Complete Continuous Assessment (CA1 & CA2), term examination grades, GPA, and teacher remarks.",
                        fontSize = 12.sp,
                        color = Slate400,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.setShowReportCardDetail(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Official Stamped Report Card", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Pending Homework & Assignments
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = Amber400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Homework & Study Tasks", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Text("${assignments.count { !it.isSubmitted }} Due", fontSize = 11.sp, color = Amber400, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    assignments.take(2).forEach { assignment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Slate900, RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(8.dp))
                                .clickable { viewModel.selectAssignment(assignment) }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(assignment.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                Text("${assignment.subject} • Due: ${assignment.dueDate}", fontSize = 11.sp, color = Slate400)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (assignment.isSubmitted) Emerald500.copy(alpha = 0.2f) else Slate800
                            ) {
                                Text(
                                    text = if (assignment.isSubmitted) "Submitted" else "Submit",
                                    fontSize = 11.sp,
                                    color = if (assignment.isSubmitted) Emerald400 else Indigo400,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
