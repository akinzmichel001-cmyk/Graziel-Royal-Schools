package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SubjectGrade
import com.example.data.model.TermReport
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
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Rose400
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun AcademicsScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTerm by viewModel.selectedTerm.collectAsStateWithLifecycle()
    val report = viewModel.getCurrentReportCard()

    val terms = listOf("2nd Term", "1st Term")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("academics_screen_list"),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        // Term Selector Tabs
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "SELECT ACADEMIC TERM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TabRow(
                        selectedTabIndex = if (selectedTerm == "2nd Term") 0 else 1,
                        containerColor = Slate900,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[if (selectedTerm == "2nd Term") 0 else 1]),
                                color = Indigo400,
                                height = 3.dp
                            )
                        },
                        divider = {}
                    ) {
                        terms.forEachIndexed { index, termName ->
                            val isSelected = selectedTerm.contains(termName.take(3))
                            Tab(
                                selected = isSelected,
                                onClick = { viewModel.setSelectedTerm(termName) },
                                text = {
                                    Text(
                                        text = termName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Indigo400 else Slate400
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Summary Performance Card
        item {
            PerformanceSummaryCard(
                report = report,
                onViewOfficialSlip = { viewModel.setShowReportCardDetail(true) }
            )
        }

        // Subject Grades Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = Indigo400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SUBJECT ASSESSMENTS (${report.grades.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate300,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "CA (40) + Exam (60)",
                    fontSize = 10.sp,
                    color = Slate500
                )
            }
        }

        // Subject Grades List
        items(report.grades, key = { it.id }) { grade ->
            SubjectGradeCard(grade = grade)
        }

        // Psychomotor & Behavioral Assessment
        item {
            PsychomotorSkillsCard()
        }

        // Action: Generate Official Stamped Report Slip
        item {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { viewModel.setShowReportCardDetail(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("generate_report_slip_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View & Print Official Stamped Slip", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PerformanceSummaryCard(
    report: TermReport,
    onViewOfficialSlip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("academic_performance_summary"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Indigo900.copy(alpha = 0.6f),
                            DarkCardSurface,
                            DarkCardSurface
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = report.termName.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = report.studentClass,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate100
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Emerald500.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Amber400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rank: ${report.classPosition} of ${report.classPopulation}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Average Grade", fontSize = 11.sp, color = Slate400)
                        Text("${report.averageScore}%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("Distinction Standard", fontSize = 10.sp, color = Emerald400, fontWeight = FontWeight.Medium)
                    }

                    Column {
                        Text("Total Score", fontSize = 11.sp, color = Slate400)
                        Text("${report.totalObtained}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("out of ${report.totalPossible} pts", fontSize = 10.sp, color = Slate400)
                    }

                    Column {
                        Text("Attendance", fontSize = 11.sp, color = Slate400)
                        Text("${report.attendanceDays}/${report.totalDays}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("95.0% Present", fontSize = 10.sp, color = Amber400)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DarkBorder)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Form Teacher: \"${report.formTeacherRemark.take(45)}...\"",
                        fontSize = 11.sp,
                        color = Slate300,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate800,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.clickable { onViewOfficialSlip() }
                    ) {
                        Text(
                            text = "Slip Details >",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectGradeCard(
    grade: SubjectGrade
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("subject_grade_card_${grade.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = grade.subjectName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = "Taught by ${grade.teacherName}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${grade.totalScore}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = "Pos: ${grade.position}${getOrdinal(grade.position)}",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    val (badgeBg, badgeColor) = when (grade.gradeLetter) {
                        "A1" -> Emerald500.copy(alpha = 0.15f) to Emerald400
                        "B2", "B3" -> Amber500.copy(alpha = 0.15f) to Amber400
                        else -> Indigo500.copy(alpha = 0.15f) to Indigo400
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = badgeBg,
                        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = grade.gradeLetter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar for score
            LinearProgressIndicator(
                progress = { grade.totalScore / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (grade.totalScore >= 90) Emerald400 else Indigo400,
                trackColor = Slate800
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Score breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("CA1: ${grade.ca1}/15", fontSize = 10.sp, color = Slate400)
                Text("CA2: ${grade.ca2}/15", fontSize = 10.sp, color = Slate400)
                Text("Proj: ${grade.projectScore}/10", fontSize = 10.sp, color = Slate400)
                Text("Exam: ${grade.examScore}/60", fontSize = 10.sp, color = Slate400)
                Text("Remark: ${grade.remark}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Emerald400)
            }
        }
    }
}

@Composable
private fun PsychomotorSkillsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "BEHAVIORAL & PSYCHOMOTOR EVALUATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Indigo400,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            SkillRatingRow("Punctuality & Attendance", 5)
            SkillRatingRow("Neatness & Uniform Compliance", 5)
            SkillRatingRow("Classroom Attentiveness & Discipline", 5)
            SkillRatingRow("Leadership & Team Collaboration", 4)
            SkillRatingRow("Cultural Appreciation & Creativity", 5)
            SkillRatingRow("Sports & Physical Agility", 4)
        }
    }
}

@Composable
private fun SkillRatingRow(title: String, stars: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 11.sp, color = Slate200)
        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = if (i <= stars) Amber400 else Slate700,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun getOrdinal(n: Int): String {
    return when {
        n % 100 in 11..13 -> "th"
        n % 10 == 1 -> "st"
        n % 10 == 2 -> "nd"
        n % 10 == 3 -> "rd"
        else -> "th"
    }
}
