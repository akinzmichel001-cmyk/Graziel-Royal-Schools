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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Assignment
import com.example.ui.theme.Amber400
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun HomeworkScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    var selectedFilterIndex by remember { mutableStateOf(0) } // 0: Pending, 1: Completed

    val pendingList = assignments.filter { !it.isSubmitted }
    val completedList = assignments.filter { it.isSubmitted }
    val currentList = if (selectedFilterIndex == 0) pendingList else completedList

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("homework_screen_list"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // Tab row filter
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "ACADEMIC TASKS & HOMEWORK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TabRow(
                        selectedTabIndex = selectedFilterIndex,
                        containerColor = Slate900,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedFilterIndex]),
                                color = Indigo500,
                                height = 3.dp
                            )
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedFilterIndex == 0,
                            onClick = { selectedFilterIndex = 0 },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Pending (${pendingList.size})",
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedFilterIndex == 0) FontWeight.Bold else FontWeight.Medium,
                                        color = if (selectedFilterIndex == 0) Indigo400 else Slate400
                                    )
                                }
                            }
                        )

                        Tab(
                            selected = selectedFilterIndex == 1,
                            onClick = { selectedFilterIndex = 1 },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Completed (${completedList.size})",
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedFilterIndex == 1) FontWeight.Bold else FontWeight.Medium,
                                        color = if (selectedFilterIndex == 1) Indigo400 else Slate400
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedFilterIndex == 0) "UPCOMING SUBMISSIONS" else "GRADED & REVIEWED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate300,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "${currentList.size} Total Items",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }
        }

        if (currentList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = Emerald400, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("All caught up!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        Text("No tasks currently in this section.", fontSize = 12.sp, color = Slate400)
                    }
                }
            }
        } else {
            items(currentList, key = { it.id }) { item ->
                AssignmentCard(
                    assignment = item,
                    onClick = { viewModel.selectAssignment(item) }
                )
            }
        }
    }
}

@Composable
private fun AssignmentCard(
    assignment: Assignment,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag("assignment_card_${assignment.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, if (assignment.isSubmitted) Emerald500.copy(alpha = 0.4f) else DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Indigo500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = assignment.subject.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo400,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = if (assignment.isSubmitted) Emerald400 else Amber400, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = assignment.dueDate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (assignment.isSubmitted) Emerald400 else Amber400
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = assignment.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = assignment.description,
                fontSize = 12.sp,
                color = Slate400,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instructor: ${assignment.teacher}",
                    fontSize = 11.sp,
                    color = Slate500
                )

                if (assignment.isSubmitted) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Emerald500.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (assignment.score != null) "${assignment.score}/${assignment.maxScore} Marks" else "Submitted",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Turn In", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
