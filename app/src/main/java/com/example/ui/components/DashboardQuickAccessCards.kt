package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Assignment
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

/**
 * Material 3 Quick Access Dashboard Section:
 * Displays responsive, interactive Material 3 cards for:
 * 1. Report Cards (Term average, rank, remarks, and one-tap stamped report card viewer)
 * 2. Attendance (Term attendance rate, streak, weekly attendance timeline, punctuality)
 * 3. Upcoming Assignments (Impending deadlines, submission progress, one-tap submission action)
 */
@Composable
fun DashboardQuickAccessSection(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier,
    initialTab: String = "ALL" // "ALL", "REPORT", "ATTENDANCE", "ASSIGNMENTS"
) {
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    val attendanceRecords by viewModel.attendanceRecords.collectAsStateWithLifecycle()
    val isReportApproved by viewModel.isReportCardApproved.collectAsStateWithLifecycle()
    val report = viewModel.getCurrentReportCard()

    var selectedCategory by remember { mutableStateOf(initialTab) }

    val pendingAssignments = assignments.filter { !it.isSubmitted }
    val completedAssignments = assignments.filter { it.isSubmitted }
    val totalAssignmentsCount = assignments.size.coerceAtLeast(1)
    val assignmentProgress = (completedAssignments.size.toFloat() / totalAssignmentsCount.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("dashboard_quick_access_section")
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Indigo500.copy(alpha = 0.15f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACADEMIC SNAPSHOT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate300,
                    letterSpacing = 0.8.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkCardSurfaceElevated,
                border = BorderStroke(1.dp, DarkBorderSubtle)
            ) {
                Text(
                    text = "Material 3 Live",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Amber400,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val filterTabs = listOf(
                "ALL" to "All Highlights",
                "REPORT" to "Report Cards",
                "ATTENDANCE" to "Attendance (98.5%)",
                "ASSIGNMENTS" to "Assignments (${pendingAssignments.size})"
            )

            items(filterTabs) { (key, label) ->
                val isSelected = selectedCategory == key
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Indigo600 else DarkCardSurface,
                    border = BorderStroke(1.dp, if (isSelected) Indigo500 else DarkBorder),
                    modifier = Modifier
                        .testTag("filter_tab_$key")
                        .clickable { selectedCategory = key }
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Slate400,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // High-Density Metrics Strip (Always visible for quick glance)
        if (selectedCategory == "ALL") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickMetricTile(
                    title = "GPA / Average",
                    value = "${String.format("%.1f", report.averageScore)}%",
                    subtitle = "Rank: 2nd of 28",
                    icon = Icons.Default.Grade,
                    accentColor = Indigo400,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setShowReportCardDetail(true) }
                )

                QuickMetricTile(
                    title = "Attendance",
                    value = "98.5%",
                    subtitle = "${report.attendanceDays}/${report.totalDays} Days",
                    icon = Icons.Default.CheckCircle,
                    accentColor = Emerald400,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppDestination.SCHEDULE) }
                )

                QuickMetricTile(
                    title = "Pending Tasks",
                    value = "${pendingAssignments.size}",
                    subtitle = "Next: Physics",
                    icon = Icons.Default.Assignment,
                    accentColor = Amber400,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppDestination.HOMEWORK) }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        // 1. Report Card Quick Access Card
        if (selectedCategory == "ALL" || selectedCategory == "REPORT") {
            ReportCardQuickAccessCard(
                report = report,
                isApproved = isReportApproved,
                onViewFullReport = { viewModel.setShowReportCardDetail(true) },
                onNavigateToAcademics = { viewModel.navigateTo(AppDestination.ACADEMICS) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 2. Attendance Quick Access Card
        if (selectedCategory == "ALL" || selectedCategory == "ATTENDANCE") {
            AttendanceQuickAccessCard(
                presentDays = report.attendanceDays,
                totalDays = report.totalDays,
                onViewSchedule = { viewModel.navigateTo(AppDestination.SCHEDULE) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 3. Upcoming Assignments Quick Access Card
        if (selectedCategory == "ALL" || selectedCategory == "ASSIGNMENTS") {
            UpcomingAssignmentsQuickAccessCard(
                assignments = assignments,
                pendingCount = pendingAssignments.size,
                progress = assignmentProgress,
                onSelectAssignment = { viewModel.selectAssignment(it) },
                onViewAllHomework = { viewModel.navigateTo(AppDestination.HOMEWORK) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

/**
 * Compact mini tile for quick top scanning
 */
@Composable
private fun QuickMetricTile(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.4.sp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate100
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 1. Material 3 Quick Access Card for Report Cards
 */
@Composable
fun ReportCardQuickAccessCard(
    report: TermReport,
    isApproved: Boolean,
    onViewFullReport: () -> Unit,
    onNavigateToAcademics: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("report_card_quick_access_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Indigo600, Indigo400)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Grade,
                            contentDescription = "Report Cards",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Terminal Report Card",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = "${report.termName} • ${report.session}",
                            fontSize = 11.sp,
                            color = Indigo400
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isApproved) Emerald500.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, if (isApproved) Emerald500.copy(alpha = 0.4f) else Amber500.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isApproved) Icons.Default.Verified else Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = if (isApproved) Emerald400 else Amber400,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isApproved) "Official Stamp" else "Under Review",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isApproved) Emerald400 else Amber400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Highlight Performance Matrix Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkCanvas.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, DarkBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TERM AVERAGE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${String.format("%.1f", report.averageScore)}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Emerald400
                        )
                        Text("Grade: A1 Distinction", fontSize = 10.sp, color = Slate300)
                    }

                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .width(1.dp)
                            .background(DarkBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CLASS RANK", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${report.classPosition}nd",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Amber400
                        )
                        Text("Out of ${report.classPopulation} Students", fontSize = 10.sp, color = Slate300)
                    }

                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .width(1.dp)
                            .background(DarkBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL MARKS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${report.totalObtained}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Slate100
                        )
                        Text("of ${report.totalPossible} pts", fontSize = 10.sp, color = Slate400)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subject Grade Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val topGrades = report.grades.take(3)
                topGrades.forEach { subject ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = subject.subjectName.take(9),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate300,
                                maxLines = 1
                            )
                            Text(
                                text = "${subject.totalScore}% • ${subject.gradeLetter}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (subject.totalScore >= 75) Emerald400 else Amber400
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewFullReport,
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp)
                        .testTag("btn_view_stamped_report_card")
                ) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Stamped Report",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToAcademics,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate200),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_academics_breakdown")
                ) {
                    Text(
                        text = "Term Breakdown",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 2. Material 3 Quick Access Card for Attendance
 */
@Composable
fun AttendanceQuickAccessCard(
    presentDays: Int,
    totalDays: Int,
    onViewSchedule: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rate = if (totalDays > 0) (presentDays.toFloat() / totalDays.toFloat()) * 100f else 95f
    val absentDays = (totalDays - presentDays).coerceAtLeast(0)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("attendance_quick_access_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Emerald500, Emerald400)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Attendance",
                            tint = DarkCanvas,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Attendance & Punctuality",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = "$presentDays of $totalDays School Days Recorded",
                            fontSize = 11.sp,
                            color = Emerald400
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "14-Day Streak",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar and Rate
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Term Attendance Target (95%)",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Text(
                        text = "${String.format("%.1f", rate)}% • On Track",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald400
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { (rate / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Emerald400,
                    trackColor = Slate800,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Weekly Timeline Day Circles
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DarkCanvas.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, DarkBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
                    days.forEachIndexed { index, day ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(day, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                            Surface(
                                shape = CircleShape,
                                color = Emerald500.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.5f)),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Emerald400,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Text("07:45 AM", fontSize = 8.sp, color = Slate500)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = onViewSchedule,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Slate800,
                        contentColor = Slate100
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("btn_view_attendance_timetable")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Emerald400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Schedule & Attendance Log",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 3. Material 3 Quick Access Card for Upcoming Assignments
 */
@Composable
fun UpcomingAssignmentsQuickAccessCard(
    assignments: List<Assignment>,
    pendingCount: Int,
    progress: Float,
    onSelectAssignment: (Assignment) -> Unit,
    onViewAllHomework: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingList = assignments.filter { !it.isSubmitted }
    val topUpcoming = pendingList.take(2)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("upcoming_assignments_quick_access_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Amber500, Rose500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "Assignments",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Upcoming Assignments",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = "$pendingCount task(s) awaiting submission",
                            fontSize = 11.sp,
                            color = Amber400
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (pendingCount > 0) Amber500.copy(alpha = 0.15f) else Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, if (pendingCount > 0) Amber500.copy(alpha = 0.4f) else Emerald500.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (pendingCount > 0) "$pendingCount Due Soon" else "All Done!",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pendingCount > 0) Amber400 else Emerald400,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Completion Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Submission Completion",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Text(
                        text = "${(progress * 100).toInt()}% Done",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (progress >= 0.7f) Emerald400 else Amber400
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (progress >= 0.7f) Emerald400 else Amber400,
                    trackColor = Slate800,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Top Upcoming Assignments List Items
            if (topUpcoming.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCanvas.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Emerald400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Great job! All homework assignments have been completed and submitted.",
                            fontSize = 11.sp,
                            color = Slate300
                        )
                    }
                }
            } else {
                topUpcoming.forEach { assignment ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkCanvas.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectAssignment(assignment) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Amber500.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = assignment.subject,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Amber400,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Due ${assignment.dueDate}",
                                        fontSize = 10.sp,
                                        color = Rose400,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = assignment.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate100,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Max score: ${assignment.maxScore} pts • Teacher: ${assignment.teacher}",
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                            }

                            Button(
                                onClick = { onSelectAssignment(assignment) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("btn_submit_assignment_${assignment.id}")
                            ) {
                                Text("Submit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // View all homework button
            OutlinedButton(
                onClick = onViewAllHomework,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber400),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .testTag("btn_view_all_assignments")
            ) {
                Icon(
                    imageVector = Icons.Default.PendingActions,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "View All Homework & Solutions (${assignments.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
