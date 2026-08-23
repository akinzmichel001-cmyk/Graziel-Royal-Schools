package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Announcement
import com.example.data.model.Assignment
import com.example.data.model.AttendanceRecord
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeacherPortalScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val attendanceList by viewModel.attendanceRecords.collectAsStateWithLifecycle()
    val staffClockRecords by viewModel.staffClockRecords.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Attendance Register, 1: Post Bulletin, 2: Create Assignment

    // Find if teacher has clocked in today
    val myClockRecord = staffClockRecords.find { it.staffId == (currentUser?.regOrStaffId ?: "STAFF/2024/001") }
    val isClockedIn = myClockRecord != null && myClockRecord.clockOutTime == null

    // State for creating announcement
    var annTitle by remember { mutableStateOf("") }
    var annSummary by remember { mutableStateOf("") }
    var annCategory by remember { mutableStateOf("Academic") }

    // State for creating assignment
    var assTitle by remember { mutableStateOf("") }
    var assSubject by remember { mutableStateOf("Mathematics") }
    var assDesc by remember { mutableStateOf("") }
    var assDue by remember { mutableStateOf("02 Mar 2025") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Slate100,
        unfocusedTextColor = Slate200,
        focusedBorderColor = Indigo400,
        unfocusedBorderColor = DarkBorderSubtle,
        focusedLabelColor = Indigo400,
        unfocusedLabelColor = Slate400,
        focusedPlaceholderColor = Slate500,
        unfocusedPlaceholderColor = Slate500,
        cursorColor = Indigo400
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("teacher_portal_screen"),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        // Teacher Profile & Staff Workspace Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Emerald500.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Emerald400),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.School, contentDescription = null, tint = Emerald400, modifier = Modifier.size(26.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "FACULTY & TEACHER WORKSPACE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = currentUser?.fullName ?: "Mr. Olumide Adeleke",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "Subject Lead: Mathematics & Basic Science • SS 1 Science",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = DarkBorderSubtle)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Staff Clock-In / Clock-Out Component (User Requirement)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate900, RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = if (isClockedIn) Emerald400 else Amber400,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isClockedIn) "Clocked In: ${myClockRecord?.clockInTime ?: "07:45 AM"}" else "Shift Status: Not Clocked In",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isClockedIn) Emerald400 else Amber400
                                )
                            }
                            Text(
                                text = if (isClockedIn) "Location: School Main Campus Gate" else "Tap below to log today's faculty attendance",
                                fontSize = 10.sp,
                                color = Slate400
                            )
                        }

                        Button(
                            onClick = {
                                if (isClockedIn) {
                                    viewModel.clockOutStaff()
                                    Toast.makeText(context, "Faculty clock-out logged successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.clockInStaff()
                                    Toast.makeText(context, "Faculty clock-in verified!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isClockedIn) Rose500 else Emerald500
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("teacher_clock_in_out_button")
                        ) {
                            Text(
                                text = if (isClockedIn) "Clock Out" else "Clock In",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }
                    }
                }
            }
        }

        // Quick Launchers: CBT Studio, Class Group Chat, Staff Room Chat
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // CBT Studio Hub Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(AppDestination.CBT_STUDIO) }
                        .testTag("teacher_launch_cbt_studio_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = Indigo500.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Indigo400),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = Indigo400, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "CBT Assessment Studio",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Create Questions • Click 'Go Live' • Review & Publish Scores",
                                    fontSize = 11.sp,
                                    color = Indigo400
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppDestination.CBT_STUDIO) },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Open", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Chat Channels Hub
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.selectChatChannel("class_ss1_science")
                                viewModel.navigateTo(AppDestination.GROUP_CHAT)
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.Forum, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Class Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                            Text("SS 1 Science", fontSize = 10.sp, color = Slate400)
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.selectChatChannel("staff_room")
                                viewModel.navigateTo(AppDestination.GROUP_CHAT)
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Staff Room", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                            Text("Faculty Chat", fontSize = 10.sp, color = Slate400)
                        }
                    }
                }
            }
        }

        // Tabs Section
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkCardSurface,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Indigo400,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Attendance (${attendanceList.size})", fontSize = 12.sp, color = if (selectedTab == 0) Indigo400 else Slate400, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Post Bulletin", fontSize = 12.sp, color = if (selectedTab == 1) Indigo400 else Slate400, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Add Homework", fontSize = 12.sp, color = if (selectedTab == 2) Indigo400 else Slate400, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium) }
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // ATTENDANCE REGISTER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAILY ROLL CALL REGISTER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Today: 24 Feb 2025",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            items(attendanceList, key = { it.id }) { record ->
                AttendanceRowItem(
                    record = record,
                    onStatusChange = { newStatus ->
                        viewModel.updateAttendanceStatus(record.id, newStatus)
                        Toast.makeText(context, "${record.studentName}: $newStatus", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        } else if (selectedTab == 1) {
            // POST BULLETIN ANNOUNCEMENT
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Broadcast Notice to Students & Parents", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = annTitle,
                            onValueChange = { annTitle = it },
                            label = { Text("Announcement Headline *", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = annSummary,
                            onValueChange = { annSummary = it },
                            label = { Text("Notice Content / Message *", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = annCategory,
                            onValueChange = { annCategory = it },
                            label = { Text("Category (Academic / Cultural / Sports / Notice)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (annTitle.isNotBlank() && annSummary.isNotBlank()) {
                                    val newAnn = Announcement(
                                        title = annTitle,
                                        summary = annSummary,
                                        date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                                        category = annCategory,
                                        author = "Faculty (${currentUser?.fullName ?: "Mr. Olumide"})",
                                        isPinned = true
                                    )
                                    viewModel.createAnnouncement(newAnn)
                                    annTitle = ""
                                    annSummary = ""
                                    Toast.makeText(context, "Announcement posted to Bulletin Board!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please fill in title and summary", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PostAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Publish Official Bulletin Notice", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        } else {
            // CREATE HOMEWORK ASSIGNMENT
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assignment, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Assign Homework Task to Class", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = assTitle,
                            onValueChange = { assTitle = it },
                            label = { Text("Task Title *", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = assSubject,
                                onValueChange = { assSubject = it },
                                label = { Text("Subject", fontSize = 11.sp) },
                                modifier = Modifier.weight(1.2f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )
                            OutlinedTextField(
                                value = assDue,
                                onValueChange = { assDue = it },
                                label = { Text("Due Date", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = assDesc,
                            onValueChange = { assDesc = it },
                            label = { Text("Questions / Homework Instructions *", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (assTitle.isNotBlank() && assDesc.isNotBlank()) {
                                    val newAss = Assignment(
                                        title = assTitle,
                                        subject = assSubject,
                                        dueDate = assDue,
                                        description = assDesc,
                                        maxScore = 20,
                                        teacher = currentUser?.fullName ?: "Mr. Olumide Adeleke"
                                    )
                                    viewModel.createAssignment(newAss)
                                    assTitle = ""
                                    assDesc = ""
                                    Toast.makeText(context, "Assignment created and dispatched!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please fill in assignment details", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dispatch Assignment to Students", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceRowItem(
    record: AttendanceRecord,
    onStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Indigo500.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(text = record.studentName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    Text(text = "Reg: ${record.studentId} • ${record.grade}", fontSize = 11.sp, color = Slate400)
                }
            }

            // Status Selector Pills
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StatusPill("Present", record.status == "Present", Emerald500) { onStatusChange("Present") }
                StatusPill("Late", record.status == "Late", Amber500) { onStatusChange("Late") }
                StatusPill("Absent", record.status == "Absent", Rose500) { onStatusChange("Absent") }
            }
        }
    }
}

@Composable
private fun StatusPill(label: String, isSelected: Boolean, activeColor: Color, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) activeColor else Slate800,
        border = if (isSelected) BorderStroke(1.dp, activeColor) else BorderStroke(1.dp, DarkBorderSubtle),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label.take(1),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Slate400,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
