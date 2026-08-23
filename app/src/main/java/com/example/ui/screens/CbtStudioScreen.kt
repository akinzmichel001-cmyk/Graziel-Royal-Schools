package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CbtQuestion
import com.example.data.model.CbtSubmission
import com.example.data.model.CbtTest
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
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun CbtStudioScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val cbtSubmissions by viewModel.cbtSubmissions.collectAsStateWithLifecycle()
    val cbtQuestions by viewModel.cbtQuestions.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: All CBT Tests, 1: Review Scores & Publish
    var showCreateTestModal by remember { mutableStateOf(false) }
    var selectedSubmissionForReview by remember { mutableStateOf<CbtSubmission?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
    ) {
        // Header
        Surface(
            color = DarkCardSurface,
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
                        IconButton(onClick = { viewModel.navigateTo(AppDestination.TEACHER_PORTAL) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Slate300)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "CBT Assessment Studio",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "Question Bank, Live Proctoring & Result Publishing",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    Button(
                        onClick = { showCreateTestModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("cbt_create_new_test_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New CBT", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkCardSurface,
                    contentColor = Indigo400,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Indigo400,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Manage Tests & Go Live (${cbtTests.size})",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) Slate100 else Slate400
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Submissions & Review (${cbtSubmissions.size})",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) Slate100 else Slate400
                            )
                        }
                    )
                }
            }
        }

        // Body Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (selectedTab == 0) {
                // Tab 0: Tests List
                items(cbtTests) { test ->
                    CbtTestCard(
                        test = test,
                        questionCount = cbtQuestions.count { it.testId == test.id },
                        onToggleLive = {
                            viewModel.toggleCbtLiveStatus(test.id, test.isLive)
                            Toast.makeText(
                                context,
                                if (!test.isLive) "Test is now LIVE on Student Portals!" else "Test moved to Offline mode.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onTogglePublish = {
                            viewModel.toggleCbtPublishResults(test.id, test.isResultsPublished)
                            Toast.makeText(
                                context,
                                if (!test.isResultsPublished) "Results published to Student and Parent portals!" else "Results hidden from portals.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onPreviewExam = {
                            viewModel.startCbtExam(test)
                        }
                    )
                }
            } else {
                // Tab 1: Submissions & Grading
                if (cbtSubmissions.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No student submissions yet. Students will appear here after taking a live CBT.",
                                color = Slate400,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                } else {
                    items(cbtSubmissions) { sub ->
                        val matchingTest = cbtTests.find { it.id == sub.testId }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sub.studentName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate100
                                        )
                                        Text(
                                            text = "${sub.studentClass} • ${sub.studentReg}",
                                            fontSize = 11.sp,
                                            color = Slate400
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (sub.percentage >= 60) Emerald500.copy(alpha = 0.2f) else Amber500.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, if (sub.percentage >= 60) Emerald400 else Amber400)
                                    ) {
                                        Text(
                                            text = "${sub.score}/${sub.maxScore} (${String.format("%.0f", sub.percentage)}%)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (sub.percentage >= 60) Emerald400 else Amber400,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Exam: ${matchingTest?.title ?: "Subject Test"} • Submitted: ${sub.submissionDate}",
                                    fontSize = 12.sp,
                                    color = Indigo400
                                )

                                if (sub.teacherFeedback.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Teacher Note: \"${sub.teacherFeedback}\"",
                                        fontSize = 12.sp,
                                        color = Slate300,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { selectedSubmissionForReview = sub },
                                        colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                                        border = BorderStroke(1.dp, DarkBorderSubtle),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("review_edit_score_button_${sub.id}")
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null, tint = Amber400, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Review & Edit Score", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Edit Score & Feedback
    selectedSubmissionForReview?.let { submission ->
        var editScore by remember { mutableStateOf(submission.score.toString()) }
        var editFeedback by remember { mutableStateOf(submission.teacherFeedback) }

        AlertDialog(
            onDismissRequest = { selectedSubmissionForReview = null },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Teacher Score Review & Remarks", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Student: ${submission.studentName} (${submission.studentClass})", color = Slate200, fontSize = 13.sp)
                    Text("Auto-computed CBT Score: ${submission.score}/${submission.maxScore}", color = Slate400, fontSize = 12.sp)

                    OutlinedTextField(
                        value = editScore,
                        onValueChange = { editScore = it },
                        label = { Text("Final Adjusted Score (Max: ${submission.maxScore})") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editFeedback,
                        onValueChange = { editFeedback = it },
                        label = { Text("Teacher Commendation & Feedback") },
                        minLines = 3,
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val scoreNum = editScore.toIntOrNull() ?: submission.score
                        viewModel.updateStudentCbtScore(submission, scoreNum, editFeedback)
                        selectedSubmissionForReview = null
                        Toast.makeText(context, "Student score and feedback updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                ) {
                    Text("Save & Update Result", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedSubmissionForReview = null }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // Modal: Create New CBT Test with Questions
    if (showCreateTestModal) {
        var testTitle by remember { mutableStateOf("") }
        var testSubject by remember { mutableStateOf("Mathematics") }
        var targetClass by remember { mutableStateOf("SS 1 Science") }
        var durationMinutes by remember { mutableStateOf("20") }
        var testInstructions by remember { mutableStateOf("Answer all questions. Each carries equal marks.") }

        // Question 1 Builder
        var q1Text by remember { mutableStateOf("") }
        var q1A by remember { mutableStateOf("") }
        var q1B by remember { mutableStateOf("") }
        var q1C by remember { mutableStateOf("") }
        var q1D by remember { mutableStateOf("") }
        var q1Correct by remember { mutableStateOf("A") }

        AlertDialog(
            onDismissRequest = { showCreateTestModal = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Create New CBT Assessment", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = testTitle,
                            onValueChange = { testTitle = it },
                            label = { Text("Test Title") },
                            placeholder = { Text("e.g., 2nd Term Mid-Term Assessment") },
                            colors = customFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = testSubject,
                            onValueChange = { testSubject = it },
                            label = { Text("Subject") },
                            colors = customFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = targetClass,
                                onValueChange = { targetClass = it },
                                label = { Text("Class") },
                                colors = customFieldColors(),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = durationMinutes,
                                onValueChange = { durationMinutes = it },
                                label = { Text("Mins") },
                                colors = customFieldColors(),
                                modifier = Modifier.weight(0.6f)
                            )
                        }
                    }
                    item {
                        Divider(color = DarkBorderSubtle, modifier = Modifier.padding(vertical = 4.dp))
                        Text("Add Question 1 to Bank:", fontWeight = FontWeight.Bold, color = Slate200, fontSize = 13.sp)
                    }
                    item {
                        OutlinedTextField(
                            value = q1Text,
                            onValueChange = { q1Text = it },
                            label = { Text("Question 1") },
                            placeholder = { Text("e.g. What is the value of x if 2x + 6 = 14?") },
                            colors = customFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(value = q1A, onValueChange = { q1A = it }, label = { Text("Option A") }, colors = customFieldColors(), modifier = Modifier.fillMaxWidth())
                    }
                    item {
                        OutlinedTextField(value = q1B, onValueChange = { q1B = it }, label = { Text("Option B") }, colors = customFieldColors(), modifier = Modifier.fillMaxWidth())
                    }
                    item {
                        OutlinedTextField(value = q1C, onValueChange = { q1C = it }, label = { Text("Option C") }, colors = customFieldColors(), modifier = Modifier.fillMaxWidth())
                    }
                    item {
                        OutlinedTextField(value = q1D, onValueChange = { q1D = it }, label = { Text("Option D") }, colors = customFieldColors(), modifier = Modifier.fillMaxWidth())
                    }
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Correct Option: ", color = Slate300, fontSize = 12.sp)
                            listOf("A", "B", "C", "D").forEach { opt ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = q1Correct == opt,
                                        onClick = { q1Correct = opt },
                                        colors = RadioButtonDefaults.colors(selectedColor = Indigo400)
                                    )
                                    Text(opt, color = Slate200, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (testTitle.isNotBlank()) {
                            val questions = mutableListOf<CbtQuestion>()
                            if (q1Text.isNotBlank()) {
                                questions.add(
                                    CbtQuestion(
                                        testId = 0,
                                        questionNumber = 1,
                                        questionText = q1Text,
                                        optionA = if (q1A.isNotBlank()) q1A else "Option A",
                                        optionB = if (q1B.isNotBlank()) q1B else "Option B",
                                        optionC = if (q1C.isNotBlank()) q1C else "Option C",
                                        optionD = if (q1D.isNotBlank()) q1D else "Option D",
                                        correctOption = q1Correct,
                                        marks = 5
                                    )
                                )
                            }
                            viewModel.createCbtTestWithQuestions(
                                title = testTitle,
                                subject = testSubject,
                                targetClass = targetClass,
                                durationMinutes = durationMinutes.toIntOrNull() ?: 20,
                                instructions = testInstructions,
                                questions = questions
                            )
                            showCreateTestModal = false
                            Toast.makeText(context, "CBT Assessment created in question bank!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Save & Add to Bank", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateTestModal = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }
}

@Composable
private fun CbtTestCard(
    test: CbtTest,
    questionCount: Int,
    onToggleLive: () -> Unit,
    onTogglePublish: () -> Unit,
    onPreviewExam: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (test.isLive) Emerald500.copy(alpha = 0.5f) else DarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live Status Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (test.isLive) Emerald500.copy(alpha = 0.2f) else Slate800,
                    border = BorderStroke(1.dp, if (test.isLive) Emerald400 else DarkBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            tint = if (test.isLive) Emerald400 else Slate500,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (test.isLive) "LIVE ON PORTALS" else "OFFLINE DRAFT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (test.isLive) Emerald400 else Slate400
                        )
                    }
                }

                Text(
                    text = "${test.durationMinutes} Mins • ${test.totalMarks} Marks",
                    fontSize = 12.sp,
                    color = Amber400,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = test.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100
            )

            Text(
                text = "${test.subject} • ${test.targetClass} • By ${test.createdByTeacher}",
                fontSize = 12.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = test.instructions,
                fontSize = 12.sp,
                color = Slate300,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = DarkBorderSubtle)
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // GO LIVE BUTTON (Critical User Requirement)
                Button(
                    onClick = onToggleLive,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (test.isLive) Rose500 else Emerald500
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cbt_go_live_button_${test.id}")
                ) {
                    Icon(
                        imageVector = if (test.isLive) Icons.Default.FiberManualRecord else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (test.isLive) "End Live Test" else "Go Live",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                }

                // PUBLISH RESULTS BUTTON
                Button(
                    onClick = onTogglePublish,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (test.isResultsPublished) Slate700 else Indigo600
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cbt_publish_results_button_${test.id}")
                ) {
                    Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (test.isResultsPublished) "Published" else "Publish Results",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                }

                // Test Runner Preview
                IconButton(
                    onClick = onPreviewExam,
                    modifier = Modifier
                        .background(Slate800, CircleShape)
                        .border(BorderStroke(1.dp, DarkBorderSubtle), CircleShape)
                        .size(38.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = "Test Preview", tint = Indigo400, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun customFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = DarkCanvas,
    unfocusedContainerColor = DarkCanvas,
    focusedIndicatorColor = Indigo400,
    unfocusedIndicatorColor = DarkBorder,
    focusedLabelColor = Indigo400,
    unfocusedLabelColor = Slate400,
    focusedTextColor = Slate100,
    unfocusedTextColor = Slate200
)
