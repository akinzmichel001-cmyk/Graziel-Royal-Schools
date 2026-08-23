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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TimetablePeriod
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun ScheduleScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDay by viewModel.selectedTimetableDay.collectAsStateWithLifecycle()
    val periods = viewModel.getTimetable()
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("schedule_screen_list"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // Day selector row
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CLASS SCHEDULE & TIMETABLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(days) { day ->
                        val isSelected = day == selectedDay
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Indigo600 else Slate900,
                            border = BorderStroke(1.dp, if (isSelected) Indigo500 else DarkBorder),
                            modifier = Modifier
                                .testTag("day_selector_$day")
                                .clickable { viewModel.setSelectedTimetableDay(day) }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = day.take(3).uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Amber400 else Slate500
                                )
                                Text(
                                    text = day,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate300
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Day Info Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$selectedDay Schedule (8:00 AM - 3:30 PM)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Amber500.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "${periods.size} Periods",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Timetable Period Items
        items(periods) { period ->
            TimetablePeriodCard(period = period)
        }

        // Term Academic Calendar Highlights
        item {
            AcademicCalendarHighlights()
        }
    }
}

@Composable
private fun TimetablePeriodCard(
    period: TimetablePeriod
) {
    val isBreak = period.isBreak

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("period_item_${period.periodNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBreak) Slate900 else DarkCardSurface
        ),
        border = BorderStroke(1.dp, if (isBreak) Amber500.copy(alpha = 0.3f) else DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Period Number
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isBreak) Amber500.copy(alpha = 0.15f) else Indigo500.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${period.periodNumber}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isBreak) Amber400 else Indigo400
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = period.subject,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isBreak) Amber400 else Slate100
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Slate500, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = period.timeRange,
                        fontSize = 11.sp,
                        color = Slate400,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (!isBreak) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Indigo400, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = period.teacher, fontSize = 10.sp, color = Slate400)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Emerald400, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = period.room, fontSize = 10.sp, color = Emerald400, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AcademicCalendarHighlights() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EventNote, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "UPCOMING 2ND TERM DATES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate300,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            EventRow("28 Feb 2025", "Mid-Term Assessment & Parents Open Day", isKey = true)
            EventRow("05 Mar 2025", "Colour Day & Creative Arts Festival", isKey = false)
            EventRow("15 Mar 2025", "Annual Yoruba Project & Cultural Showcase", isKey = true)
            EventRow("22 Mar 2025", "Inter-House Sports Athletics Championship", isKey = false)
            EventRow("07 Apr 2025", "Second Term Final Examination Week", isKey = true)
            EventRow("17 Apr 2025", "Vacation & Report Card Release", isKey = false)
        }
    }
}

@Composable
private fun EventRow(date: String, title: String, isKey: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isKey) Amber500.copy(alpha = 0.15f) else Slate800,
            border = BorderStroke(1.dp, if (isKey) Amber500.copy(alpha = 0.3f) else DarkBorderSubtle)
        ) {
            Text(
                text = date,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isKey) Amber400 else Slate300,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            color = if (isKey) Slate100 else Slate400,
            fontWeight = if (isKey) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
