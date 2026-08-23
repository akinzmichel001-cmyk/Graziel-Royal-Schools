package com.example.ui.screens

import android.os.CountDownTimer
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun CbtExamScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val activeTest by viewModel.activeCbtTest.collectAsStateWithLifecycle()
    val allQuestions by viewModel.cbtQuestions.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val testQuestions = remember(activeTest, allQuestions) {
        if (activeTest != null) {
            allQuestions.filter { it.testId == activeTest!!.id }.ifEmpty {
                // Fallback default questions if test has none yet
                listOf(
                    CbtQuestion(
                        id = 1,
                        testId = activeTest!!.id,
                        questionNumber = 1,
                        questionText = "Solve for x in the linear algebraic equation: 3x - 7 = 14.",
                        optionA = "x = 7",
                        optionB = "x = 6",
                        optionC = "x = 9",
                        optionD = "x = 8",
                        correctOption = "A",
                        marks = 5
                    ),
                    CbtQuestion(
                        id = 2,
                        testId = activeTest!!.id,
                        questionNumber = 2,
                        questionText = "What is the primary significance of 'Ofuloju' in traditional Yoruba culinary culture?",
                        optionA = "Ceremonial spiced bean delicacy for royal coronation and unity",
                        optionB = "Fermented cassava drink",
                        optionC = "Palm fruit condiment",
                        optionD = "A dessert pudding",
                        correctOption = "A",
                        marks = 5
                    )
                )
            }
        } else {
            emptyList()
        }
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    var showSubmitConfirmation by remember { mutableStateOf(false) }
    var examSubmittedResult by remember { mutableStateOf<CbtSubmission?>(null) }

    // Countdown Timer State
    val initialSeconds = (activeTest?.durationMinutes ?: 15) * 60L
    var secondsRemaining by remember { mutableLongStateOf(initialSeconds) }

    DisposableEffect(activeTest) {
        val timer = object : CountDownTimer(initialSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
            }

            override fun onFinish() {
                secondsRemaining = 0
                // Auto-submit if time elapses
                if (examSubmittedResult == null && activeTest != null) {
                    viewModel.submitStudentCbtExam(activeTest!!, userAnswers, testQuestions)
                }
            }
        }.start()

        onDispose {
            timer.cancel()
        }
    }

    if (activeTest == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DarkCanvas),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No active CBT selected.", color = Slate300)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { viewModel.navigateTo(AppDestination.STUDENT_PORTAL) }) {
                    Text("Back to Student Portal")
                }
            }
        }
        return
    }

    // If submitted, show Scorecard Results Screen
    if (examSubmittedResult != null) {
        val result = examSubmittedResult!!
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DarkCanvas)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Emerald500.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Emerald400),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(36.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CBT EXAM COMPLETED!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Slate100
                    )

                    Text(
                        text = activeTest!!.title,
                        fontSize = 13.sp,
                        color = Indigo400,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = DarkBorderSubtle)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Score Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Your Score", fontSize = 12.sp, color = Slate400)
                            Text(
                                "${result.score} / ${result.maxScore}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (result.percentage >= 60) Emerald400 else Amber400
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Percentage", fontSize = 12.sp, color = Slate400)
                            Text(
                                "${String.format("%.0f", result.percentage)}%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (result.percentage >= 60) Emerald400 else Amber400
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Teacher Assessment Remarks:", fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result.teacherFeedback,
                                fontSize = 13.sp,
                                color = Slate200
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.setActiveCbtTest(null)
                            viewModel.navigateTo(AppDestination.STUDENT_PORTAL)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("cbt_return_to_portal_button")
                    ) {
                        Text("Return to Student Portal", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    val currentQ = testQuestions.getOrNull(currentQuestionIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
    ) {
        // Top CBT App Bar with Live Countdown Timer
        Surface(
            color = DarkCardSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = activeTest!!.subject.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = activeTest!!.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100,
                            maxLines = 1
                        )
                    }

                    // Timer Pill
                    val minutes = secondsRemaining / 60
                    val seconds = secondsRemaining % 60
                    val timerColor = if (secondsRemaining < 120) Rose400 else Emerald400

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, timerColor.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = timerColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = timerColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Question Number Quick Jump Bar
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(testQuestions) { idx, q ->
                        val isAnswered = userAnswers.containsKey(q.id)
                        val isCurrent = idx == currentQuestionIndex

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isCurrent -> Indigo600
                                        isAnswered -> Emerald500.copy(alpha = 0.25f)
                                        else -> Slate800
                                    }
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        when {
                                            isCurrent -> Indigo400
                                            isAnswered -> Emerald400
                                            else -> DarkBorderSubtle
                                        }
                                    ),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { currentQuestionIndex = idx },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isCurrent -> Slate100
                                    isAnswered -> Emerald400
                                    else -> Slate400
                                }
                            )
                        }
                    }
                }
            }
        }

        // Active Question Card
        if (currentQ != null) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "QUESTION ${currentQuestionIndex + 1} OF ${testQuestions.size}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo400,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${currentQ.marks} Marks",
                                    fontSize = 11.sp,
                                    color = Amber400,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentQ.questionText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate100,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                // Options A, B, C, D
                val options = listOf(
                    "A" to currentQ.optionA,
                    "B" to currentQ.optionB,
                    "C" to currentQ.optionC,
                    "D" to currentQ.optionD
                )

                itemsIndexed(options) { _, (optLetter, optText) ->
                    val isSelected = userAnswers[currentQ.id] == optLetter

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Indigo600.copy(alpha = 0.2f) else DarkCardSurface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) Indigo400 else DarkBorderSubtle
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                userAnswers[currentQ.id] = optLetter
                            }
                            .testTag("cbt_option_${optLetter}_q${currentQ.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Indigo600 else Slate800)
                                    .border(
                                        BorderStroke(1.dp, if (isSelected) Indigo400 else DarkBorderSubtle),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optLetter,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Slate100 else Slate400
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = optText,
                                fontSize = 14.sp,
                                color = if (isSelected) Slate100 else Slate300,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = Indigo400,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation Bar for Exam
        Surface(
            color = DarkCardSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex > 0) currentQuestionIndex--
                    },
                    enabled = currentQuestionIndex > 0,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate300)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Prev")
                }

                Text(
                    text = "${userAnswers.size}/${testQuestions.size} Answered",
                    fontSize = 12.sp,
                    color = Amber400,
                    fontWeight = FontWeight.SemiBold
                )

                if (currentQuestionIndex < testQuestions.size - 1) {
                    Button(
                        onClick = { currentQuestionIndex++ },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Next")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Button(
                        onClick = { showSubmitConfirmation = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("cbt_submit_exam_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Submit Exam", fontWeight = FontWeight.Bold, color = Slate100)
                    }
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmation) {
        val answeredCount = userAnswers.size
        val unansweredCount = testQuestions.size - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitConfirmation = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Confirm CBT Submission", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "You have answered $answeredCount of ${testQuestions.size} questions." +
                                if (unansweredCount > 0) " ($unansweredCount unanswered)" else " (All answered!)",
                        color = Slate200,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Are you sure you want to finalize and submit your test now?",
                        color = Slate400,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmation = false
                        // Calculate & submit
                        viewModel.submitStudentCbtExam(activeTest!!, userAnswers, testQuestions)

                        // Compute local result display
                        var score = 0
                        testQuestions.forEach { q ->
                            val chosen = userAnswers[q.id]
                            if (chosen.equals(q.correctOption, ignoreCase = true)) {
                                score += q.marks
                            }
                        }
                        val maxScore = testQuestions.sumOf { it.marks }
                        val pct = if (maxScore > 0) (score.toDouble() / maxScore.toDouble()) * 100.0 else 0.0

                        examSubmittedResult = CbtSubmission(
                            testId = activeTest!!.id,
                            studentName = currentUser?.fullName ?: "Adeleke David Oluwaseun",
                            studentReg = currentUser?.regOrStaffId ?: "GRS/2024/0428",
                            studentClass = currentUser?.assignedClass ?: "SS 1 Science",
                            score = score,
                            maxScore = maxScore,
                            percentage = pct,
                            isReviewedByTeacher = true,
                            teacherFeedback = when {
                                pct >= 80 -> "Outstanding royal performance! Excellent mastery of concepts."
                                pct >= 60 -> "Good effort. Review missed questions in the study bank."
                                else -> "Needs revision. Please consult your subject tutor for remediation."
                            },
                            submissionDate = "Just now"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                ) {
                    Text("Yes, Submit Final Exam", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSubmitConfirmation = false }) {
                    Text("Keep Answering", color = Slate300)
                }
            }
        )
    }
}
