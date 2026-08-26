package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StudentRecord
import com.example.data.model.SubjectGrade
import com.example.data.model.TermReport
import com.example.data.model.UserRole
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
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val studentRecords by viewModel.studentRecords.collectAsStateWithLifecycle()
    val selectedStudentState by viewModel.selectedStudentForProfile.collectAsStateWithLifecycle()
    val adminConfig by viewModel.adminSecurityConfig.collectAsStateWithLifecycle()

    val isManagement = currentUser?.role == UserRole.ADMIN || currentUser?.role == UserRole.TEACHER

    // Active student to display (fall back to selected, then matching logged-in student, or first student in database)
    val activeStudent: StudentRecord = remember(selectedStudentState, studentRecords, currentUser) {
        selectedStudentState
            ?: if (currentUser?.role == UserRole.STUDENT) {
                studentRecords.find { it.studentId == (currentUser?.regOrStaffId ?: "GRS/2024/0428") }
                    ?: studentRecords.firstOrNull()
                    ?: defaultFallbackStudent()
            } else if (currentUser?.role == UserRole.PARENT) {
                studentRecords.find { it.studentId == (currentUser?.childRegNumber ?: "GRS/2024/0428") }
                    ?: studentRecords.firstOrNull()
                    ?: defaultFallbackStudent()
            } else {
                studentRecords.firstOrNull() ?: defaultFallbackStudent()
            }
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) } // 0: Academic Dossier, 1: Basic Info, 2: Parent & Emergency, 3: Behavior & Leadership
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedClassFilter by rememberSaveable { mutableStateOf("All Classes") }

    // Dialog States
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangeStatusDialog by remember { mutableStateOf(false) }
    var showPromoteClassDialog by remember { mutableStateOf(false) }
    var showEditRemarksDialog by remember { mutableStateOf(false) }
    var showDossierSummaryDialog by remember { mutableStateOf(false) }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    fun callPhoneNumber(phone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace(" ", "")}")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Dialing $phone...", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(phone: String, studentName: String) {
        val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
        val msg = "Hello from Graziel Royal Schools Management regarding $studentName (${activeStudent.studentId})."
        val url = "https://wa.me/$cleanPhone?text=${Uri.encode(msg)}"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening WhatsApp for $phone...", Toast.LENGTH_SHORT).show()
        }
    }

    // Filter students for management switcher
    val filteredStudents = remember(studentRecords, searchQuery, selectedClassFilter) {
        studentRecords.filter { student ->
            val matchesClass = selectedClassFilter == "All Classes" || student.assignedClass.equals(selectedClassFilter, ignoreCase = true)
            val matchesQuery = searchQuery.isBlank() ||
                    student.fullName.contains(searchQuery, ignoreCase = true) ||
                    student.studentId.contains(searchQuery, ignoreCase = true) ||
                    student.parentName.contains(searchQuery, ignoreCase = true) ||
                    student.assignedClass.contains(searchQuery, ignoreCase = true)
            matchesClass && matchesQuery
        }
    }

    val houseColor = when {
        activeStudent.houseName.contains("Blue", ignoreCase = true) -> Indigo400
        activeStudent.houseName.contains("Green", ignoreCase = true) -> Emerald400
        activeStudent.houseName.contains("Gold", ignoreCase = true) || activeStudent.houseName.contains("Sun", ignoreCase = true) -> Amber400
        else -> Rose400
    }

    val statusColor = when {
        activeStudent.academicStatus.contains("Scholar", ignoreCase = true) || activeStudent.academicStatus.contains("Honors", ignoreCase = true) -> Amber400
        activeStudent.academicStatus.contains("Good", ignoreCase = true) || activeStudent.academicStatus.contains("Active", ignoreCase = true) -> Emerald400
        activeStudent.academicStatus.contains("Observation", ignoreCase = true) -> Amber500
        activeStudent.academicStatus.contains("Probation", ignoreCase = true) -> Rose400
        else -> Indigo400
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkCanvas,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Student Academic Profile",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isManagement) Emerald500.copy(alpha = 0.2f) else Amber400.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, if (isManagement) Emerald400.copy(alpha = 0.5f) else Amber400.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = if (isManagement) "Management Access" else "Official Dossier",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isManagement) Emerald400 else Amber400,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "${adminConfig?.activeSession ?: "2024/2025"} • ${adminConfig?.activeTerm ?: "2nd Term"} • Graziel Royal Schools",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("student_profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Slate100
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDossierSummaryDialog = true },
                        modifier = Modifier.testTag("student_profile_share_dossier_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Export Dossier",
                            tint = Amber400
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Slate900)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =========================================================================
            // MANAGEMENT SECTION: QUICK STUDENT SEARCH & SWITCHER CAROUSEL
            // =========================================================================
            if (isManagement) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = Indigo400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Student Management Directory",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate100
                                    )
                                }
                                Text(
                                    text = "${studentRecords.size} Enrolled",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Indigo400
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Search bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search by name, student ID, class or parent...", fontSize = 12.sp, color = Slate500) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp)) },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate400, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Indigo400,
                                    unfocusedBorderColor = DarkBorderSubtle,
                                    focusedContainerColor = Slate900,
                                    unfocusedContainerColor = Slate900,
                                    focusedTextColor = Slate100,
                                    unfocusedTextColor = Slate100
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("student_profile_search_input")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Class Filter Chips
                            val classFilterOptions = listOf("All Classes", "SS 1 Science", "SS 2 Arts", "JSS 2 Gold")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                classFilterOptions.forEach { className ->
                                    val isSelected = selectedClassFilter == className
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedClassFilter = className },
                                        label = { Text(className, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Indigo600,
                                            selectedLabelColor = Slate100,
                                            containerColor = Slate900,
                                            labelColor = Slate300
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            borderColor = if (isSelected) Indigo400 else DarkBorderSubtle,
                                            selectedBorderColor = Indigo400,
                                            enabled = true,
                                            selected = isSelected
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick Select Horizontal Avatar Carousel
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(filteredStudents) { student ->
                                    val isCurrent = student.id == activeStudent.id
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isCurrent) Indigo600.copy(alpha = 0.25f) else Slate900,
                                        border = BorderStroke(1.dp, if (isCurrent) Amber400 else DarkBorderSubtle),
                                        modifier = Modifier
                                            .clickable { viewModel.selectStudentForProfile(student) }
                                            .testTag("student_chip_${student.id}")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = when {
                                                    student.houseName.contains("Blue", true) -> Indigo500
                                                    student.houseName.contains("Green", true) -> Emerald500
                                                    student.houseName.contains("Gold", true) -> Amber500
                                                    else -> Rose500
                                                }.copy(alpha = 0.3f),
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = student.fullName.take(1).uppercase(),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Slate100
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = student.fullName.substringBefore(" ").take(12),
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isCurrent) Amber400 else Slate200
                                                )
                                                Text(
                                                    text = student.assignedClass,
                                                    fontSize = 9.sp,
                                                    color = Slate400
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // HERO STUDENT IDENTIFICATION DOSSIER CARD
            // =========================================================================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, houseColor.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_hero_profile_card")
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Decorative header gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            houseColor.copy(alpha = 0.25f),
                                            Indigo600.copy(alpha = 0.2f),
                                            DarkCardSurfaceElevated
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                // Large Profile Photo Avatar
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Slate900,
                                        border = BorderStroke(2.5.dp, houseColor),
                                        modifier = Modifier.size(68.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = activeStudent.fullName.split(" ")
                                                    .filter { it.isNotBlank() }
                                                    .take(2)
                                                    .map { it.first() }
                                                    .joinToString(""),
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Amber400,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = activeStudent.fullName,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate100
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "ID: ",
                                                fontSize = 12.sp,
                                                color = Slate400
                                            )
                                            Text(
                                                text = activeStudent.studentId,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Amber400
                                            )
                                            IconButton(
                                                onClick = { copyToClipboard("Student ID", activeStudent.studentId) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy ID",
                                                    tint = Slate400,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Indigo500.copy(alpha = 0.25f),
                                                border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.5f))
                                            ) {
                                                Text(
                                                    text = activeStudent.assignedClass,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Indigo400,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = houseColor.copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, houseColor.copy(alpha = 0.4f))
                                            ) {
                                                Text(
                                                    text = activeStudent.houseName,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = houseColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                if (isManagement) {
                                    IconButton(
                                        onClick = { showEditProfileDialog = true },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Slate800, CircleShape)
                                            .testTag("edit_student_profile_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = Amber400,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = DarkBorderSubtle.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Academic Status Badge & Management Toggle Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = statusColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "Official Academic Standing",
                                            fontSize = 10.sp,
                                            color = Slate400
                                        )
                                        Text(
                                            text = activeStudent.academicStatus,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = statusColor
                                        )
                                    }
                                }

                                if (isManagement) {
                                    OutlinedButton(
                                        onClick = { showChangeStatusDialog = true },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.6f)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier
                                            .height(32.dp)
                                            .testTag("change_academic_status_button")
                                    ) {
                                        Text("Update Status", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = statusColor)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // KEY PERFORMANCE INDICATOR (KPI) METRICS GRID
            // =========================================================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = "Cumulative GPA",
                        value = activeStudent.gpa.substringBefore(" ("),
                        subtitle = activeStudent.gpa.substringAfter("(", "Distinction").replace(")", ""),
                        icon = Icons.Default.Grade,
                        color = Amber400,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Class Rank",
                        value = activeStudent.classPosition.substringBefore(" of"),
                        subtitle = "Out of 34 Students",
                        icon = Icons.Default.EmojiEvents,
                        color = Emerald400,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Attendance",
                        value = activeStudent.attendanceRate,
                        subtitle = "Term Register",
                        icon = Icons.Default.CheckCircle,
                        color = Indigo400,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // =========================================================================
            // PROFILE NAVIGATION TABS
            // =========================================================================
            item {
                val tabTitles = listOf("Academics", "Basic Info", "Parent & Contact", "Conduct & Remarks")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Slate900,
                    contentColor = Amber400,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Amber400,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(12.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) Amber400 else Slate400,
                                    maxLines = 1
                                )
                            },
                            modifier = Modifier.testTag("student_profile_tab_$index")
                        )
                    }
                }
            }

            // =========================================================================
            // TAB CONTENT
            // =========================================================================
            when (selectedTab) {
                0 -> {
                    // TAB 0: ACADEMIC DOSSIER & SUBJECT PERFORMANCE
                    item {
                        AcademicDossierTab(
                            student = activeStudent,
                            viewModel = viewModel,
                            isManagement = isManagement,
                            onPromoteClick = { showPromoteClassDialog = true }
                        )
                    }
                }
                1 -> {
                    // TAB 1: BASIC INFORMATION & BIOMETRICS
                    item {
                        BasicInformationTab(
                            student = activeStudent,
                            isManagement = isManagement,
                            onEditClick = { showEditProfileDialog = true }
                        )
                    }
                }
                2 -> {
                    // TAB 2: PARENT / GUARDIAN DOSSIER & EMERGENCY
                    item {
                        ParentAndEmergencyTab(
                            student = activeStudent,
                            onCallParent = { callPhoneNumber(activeStudent.parentPhone) },
                            onWhatsAppParent = { openWhatsApp(activeStudent.parentPhone, activeStudent.fullName) },
                            onCallEmergency = { callPhoneNumber(activeStudent.emergencyContact) }
                        )
                    }
                }
                3 -> {
                    // TAB 3: CONDUCT, LEADERSHIP & FORM MASTER REMARKS
                    item {
                        ConductAndRemarksTab(
                            student = activeStudent,
                            isManagement = isManagement,
                            onEditRemarks = { showEditRemarksDialog = true }
                        )
                    }
                }
            }

            // =========================================================================
            // MANAGEMENT ADMINISTRATIVE ACTION BAR (ADMIN & TEACHERS)
            // =========================================================================
            if (isManagement) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate900),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Amber400.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VpnKey, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Management Operations & Controls",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Administer student promotion, academic dossiers, parent correspondence, and student transcript status.",
                                fontSize = 11.sp,
                                color = Slate400
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { showPromoteClassDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_promote_student_button")
                                ) {
                                    Icon(Icons.Default.Upgrade, contentDescription = null, tint = Slate100, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Promote Class", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                }

                                Button(
                                    onClick = { showEditProfileDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_edit_student_record_button")
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Edit Record", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showChangeStatusDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_standing_status_button")
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Set Standing", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                }

                                OutlinedButton(
                                    onClick = { showDossierSummaryDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Slate600),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("admin_print_dossier_button")
                                ) {
                                    Icon(Icons.Default.Print, contentDescription = null, tint = Slate300, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Print Dossier", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate300)
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // =========================================================================
    // MODAL DIALOGS FOR MANAGEMENT ACTIONS
    // =========================================================================

    // 1. EDIT PROFILE DIALOG
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(activeStudent.fullName) }
        var editClass by remember { mutableStateOf(activeStudent.assignedClass) }
        var editHouse by remember { mutableStateOf(activeStudent.houseName) }
        var editDob by remember { mutableStateOf(activeStudent.dob) }
        var editGender by remember { mutableStateOf(activeStudent.gender) }
        var editBlood by remember { mutableStateOf(activeStudent.bloodGroup) }
        var editParentName by remember { mutableStateOf(activeStudent.parentName) }
        var editParentPhone by remember { mutableStateOf(activeStudent.parentPhone) }
        var editParentEmail by remember { mutableStateOf(activeStudent.parentEmail) }
        var editAddress by remember { mutableStateOf(activeStudent.homeAddress) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = DarkCardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Amber400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Student Record", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = editClass,
                            onValueChange = { editClass = it },
                            label = { Text("Class (e.g. SS 1 Science)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editHouse,
                            onValueChange = { editHouse = it },
                            label = { Text("House", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = editDob,
                            onValueChange = { editDob = it },
                            label = { Text("DOB (e.g. 14 May 2008)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editGender,
                            onValueChange = { editGender = it },
                            label = { Text("Gender", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = editParentName,
                        onValueChange = { editParentName = it },
                        label = { Text("Parent / Guardian Full Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = editParentPhone,
                            onValueChange = { editParentPhone = it },
                            label = { Text("Parent Phone", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editBlood,
                            onValueChange = { editBlood = it },
                            label = { Text("Blood Group", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = editAddress,
                        onValueChange = { editAddress = it },
                        label = { Text("Residential Address", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = activeStudent.copy(
                            fullName = editName.trim(),
                            assignedClass = editClass.trim(),
                            houseName = editHouse.trim(),
                            dob = editDob.trim(),
                            gender = editGender.trim(),
                            bloodGroup = editBlood.trim(),
                            parentName = editParentName.trim(),
                            parentPhone = editParentPhone.trim(),
                            parentEmail = editParentEmail.trim(),
                            homeAddress = editAddress.trim()
                        )
                        viewModel.updateStudentProfile(updated)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = Slate400)
                }
            }
        )
    }

    // 2. CHANGE ACADEMIC STATUS DIALOG
    if (showChangeStatusDialog) {
        val statusOptions = listOf(
            "Academic Scholar (Honors)" to Amber400,
            "Academic Scholar (Junior)" to Amber400,
            "Active (Good Standing)" to Emerald400,
            "Under Academic Observation" to Amber500,
            "On Academic Probation" to Rose400,
            "Graduated / Alumni" to Indigo400
        )
        AlertDialog(
            onDismissRequest = { showChangeStatusDialog = false },
            containerColor = DarkCardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Academic Standing", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Select official academic status for ${activeStudent.fullName}:",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    statusOptions.forEach { (statusName, col) ->
                        val isSelected = activeStudent.academicStatus.equals(statusName, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) col.copy(alpha = 0.2f) else Slate900,
                            border = BorderStroke(1.dp, if (isSelected) col else DarkBorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateStudentAcademicStatus(activeStudent.id, statusName)
                                    showChangeStatusDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = statusName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) col else Slate200
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = col, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showChangeStatusDialog = false }) {
                    Text("Close", color = Slate400)
                }
            }
        )
    }

    // 3. PROMOTE / CHANGE CLASS DIALOG
    if (showPromoteClassDialog) {
        val classOptions = listOf(
            "JSS 1 Bronze", "JSS 2 Gold", "JSS 3 Diamond",
            "SS 1 Science", "SS 1 Commercial", "SS 1 Arts",
            "SS 2 Science", "SS 2 Commercial", "SS 2 Arts",
            "SS 3 Science", "SS 3 Commercial", "SS 3 Arts",
            "Graduated / Alumni"
        )
        var targetClass by remember { mutableStateOf(activeStudent.assignedClass) }

        AlertDialog(
            onDismissRequest = { showPromoteClassDialog = false },
            containerColor = DarkCardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Upgrade, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Promote / Transfer Class", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Current Class: ${activeStudent.assignedClass}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber400
                    )
                    Text(
                        text = "Select destination class for ${activeStudent.fullName}:",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(classOptions) { cls ->
                            val isSelected = targetClass == cls
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Indigo600.copy(alpha = 0.25f) else Slate900,
                                border = BorderStroke(1.dp, if (isSelected) Indigo400 else DarkBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { targetClass = cls }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cls,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Indigo400 else Slate200
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Indigo400, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.promoteStudentClass(activeStudent.id, targetClass)
                        showPromoteClassDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Confirm Promotion", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPromoteClassDialog = false }) {
                    Text("Cancel", color = Slate400)
                }
            }
        )
    }

    // 4. EDIT FORM MASTER REMARK DIALOG
    if (showEditRemarksDialog) {
        var newRemark by remember { mutableStateOf(activeStudent.behaviorRemark) }
        AlertDialog(
            onDismissRequest = { showEditRemarksDialog = false },
            containerColor = DarkCardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Amber400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Form Master Remark", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Official behavioral appraisal and character appraisal for terminal dossier:",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newRemark,
                        onValueChange = { newRemark = it },
                        label = { Text("Appraisal & Conduct Remark", fontSize = 11.sp) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateStudentBehaviorRemark(activeStudent.id, newRemark.trim())
                        showEditRemarksDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Text("Save Appraisal", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditRemarksDialog = false }) {
                    Text("Cancel", color = Slate400)
                }
            }
        )
    }

    // 5. DOSSIER SUMMARY & TRANSCRIPT PRINT MODAL
    if (showDossierSummaryDialog) {
        val dossierSummaryText = """
            ==============================================
            GRAZIEL ROYAL SCHOOLS - OFFICIAL STUDENT DOSSIER
            ==============================================
            Student Name: ${activeStudent.fullName}
            Official Reg ID: ${activeStudent.studentId}
            Current Class: ${activeStudent.assignedClass}
            Academic Standing: ${activeStudent.academicStatus}
            House: ${activeStudent.houseName}
            Cumulative GPA: ${activeStudent.gpa}
            Class Rank: ${activeStudent.classPosition}
            Term Attendance: ${activeStudent.attendanceRate}
            Date of Birth: ${activeStudent.dob} (${activeStudent.gender})
            Blood Group: ${activeStudent.bloodGroup}
            Parent / Guardian: ${activeStudent.parentName} (${activeStudent.parentPhone})
            Home Address: ${activeStudent.homeAddress}
            Extracurricular: ${activeStudent.clubAffiliations}
            Form Master Remark: ${activeStudent.behaviorRemark}
            Date of Issue: 22 Feb 2025 • Graziel Royal Schools, Opo-Ibogun, Ogun State
            ==============================================
        """.trimIndent()

        AlertDialog(
            onDismissRequest = { showDossierSummaryDialog = false },
            containerColor = DarkCardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Amber400, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Official Student Dossier Transcript", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Formatted administrative summary ready for copy, printing or parent transmission:",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = dossierSummaryText,
                            fontSize = 10.sp,
                            color = Slate300,
                            lineHeight = 14.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        copyToClipboard("Official Student Dossier", dossierSummaryText)
                        showDossierSummaryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy Dossier Transcript", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDossierSummaryDialog = false }) {
                    Text("Close", color = Slate400)
                }
            }
        )
    }
}

// =========================================================================
// SUB-TAB 0: ACADEMIC DOSSIER COMPOSABLE
// =========================================================================
@Composable
private fun AcademicDossierTab(
    student: StudentRecord,
    viewModel: SchoolViewModel,
    isManagement: Boolean,
    onPromoteClick: () -> Unit
) {
    val reportCard = viewModel.getReportCard("2nd Term")
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Academic Level & Form Master Header
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Current Class & Stream", fontSize = 11.sp, color = Slate400)
                        Text(text = student.assignedClass, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Emerald500.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Senior STEM Wing",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald400,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = DarkBorderSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Form Master / Class Teacher", fontSize = 10.sp, color = Slate400)
                        Text(text = "Mr. Adeleke Ayomide (Senior Math & Physics)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Amber400)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Class Population", fontSize = 10.sp, color = Slate400)
                        Text(text = "34 Students Enrolled", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate200)
                    }
                }
            }
        }

        // Terminal Subject Scores Breakdown Table
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Terminal Subject Gradebook",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    Text(text = "2nd Term 2024/25", fontSize = 11.sp, color = Slate400)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Table Header
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Slate900,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subject", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400, modifier = Modifier.weight(1.8f))
                        Text("CA (40)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        Text("Exam (60)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        Text("Total", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        Text("Grade", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400, textAlign = TextAlign.End, modifier = Modifier.weight(0.8f))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Subject Rows
                reportCard.grades.forEach { grade ->
                    val caTotal = grade.ca1 + grade.ca2 + grade.projectScore
                    val gradeColor = when (grade.gradeLetter) {
                        "A1" -> Emerald400
                        "B2", "B3" -> Amber400
                        "C4", "C5", "C6" -> Indigo400
                        else -> Rose400
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Slate900.copy(alpha = 0.5f),
                        border = BorderStroke(0.5.dp, DarkBorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.8f)) {
                                Text(
                                    text = grade.subjectName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate100,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = grade.teacherName,
                                    fontSize = 9.sp,
                                    color = Slate500
                                )
                            }
                            Text("$caTotal", fontSize = 11.sp, color = Slate300, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("${grade.examScore}", fontSize = 11.sp, color = Slate300, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("${grade.totalScore}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Amber400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = gradeColor.copy(alpha = 0.2f),
                                modifier = Modifier.weight(0.8f)
                            ) {
                                Text(
                                    text = grade.gradeLetter,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = gradeColor,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 1.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Official Report Card Sheet Launcher Button
                Button(
                    onClick = { viewModel.openReportCardDetail() },
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_profile_open_report_card_button")
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View Full Official Report Card Sheet", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                }
            }
        }

        // CBT Assessment Highlights
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CBT Computer-Based Testing Metrics",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    Text(text = "Average: 95.0%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CbtMiniMetric(
                        subject = "Mathematics",
                        score = "20/20 (100%)",
                        status = "Perfect Score",
                        color = Emerald400,
                        modifier = Modifier.weight(1f)
                    )
                    CbtMiniMetric(
                        subject = "Biology",
                        score = "18/20 (90%)",
                        status = "Distinction",
                        color = Emerald400,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CbtMiniMetric(
    subject: String,
    score: String,
    status: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Slate900,
        border = BorderStroke(1.dp, DarkBorderSubtle),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = subject, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = score, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = status, fontSize = 9.sp, color = Slate400)
        }
    }
}

// =========================================================================
// SUB-TAB 1: BASIC INFORMATION & BIOMETRICS
// =========================================================================
@Composable
private fun BasicInformationTab(
    student: StudentRecord,
    isManagement: Boolean,
    onEditClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Biometric & Personal Dossier",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    if (isManagement) {
                        IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Amber400, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(label = "Official Legal Name", value = student.fullName, icon = Icons.Default.Person)
                InfoRow(label = "Official Registration ID", value = student.studentId, icon = Icons.Default.VpnKey)
                InfoRow(label = "Current Class / Grade", value = student.assignedClass, icon = Icons.Default.School)
                InfoRow(label = "Date of Birth", value = student.dob, icon = Icons.Default.DateRange)
                InfoRow(label = "Gender", value = student.gender, icon = Icons.Default.Person)
                InfoRow(label = "School House", value = student.houseName, icon = Icons.Default.EmojiEvents)
                InfoRow(label = "Blood Group & Genotype", value = student.bloodGroup, icon = Icons.Default.MedicalServices)
                InfoRow(label = "Date Enrolled", value = student.dateEnrolled, icon = Icons.Default.DateRange)
                InfoRow(label = "Residential Address", value = student.homeAddress, icon = Icons.Default.Home)
            }
        }
    }
}

// =========================================================================
// SUB-TAB 2: PARENT & GUARDIAN EMERGENCY CONTACTS
// =========================================================================
@Composable
private fun ParentAndEmergencyTab(
    student: StudentRecord,
    onCallParent: () -> Unit,
    onWhatsAppParent: () -> Unit,
    onCallEmergency: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Primary Guardian Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Parent & Primary Guardian",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Emerald500.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Verified Contact",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald400,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(label = "Guardian Name", value = student.parentName, icon = Icons.Default.Person)
                InfoRow(label = "Telephone Number", value = student.parentPhone, icon = Icons.Default.Phone)
                InfoRow(label = "Email Address", value = student.parentEmail, icon = Icons.Default.Mail)
                InfoRow(label = "Home Residence", value = student.homeAddress, icon = Icons.Default.LocationOn)

                Spacer(modifier = Modifier.height(12.dp))

                // Direct Contact Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCallParent,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("student_profile_call_parent_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Parent", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                    }

                    Button(
                        onClick = onWhatsAppParent,
                        colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                        border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("student_profile_whatsapp_parent_button")
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    }
                }
            }
        }

        // Emergency Medical Contact Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, tint = Rose400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Emergency Medical Contact",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    Text(text = "Blood: ${student.bloodGroup}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Rose400)
                }

                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(label = "Emergency Hotline", value = student.emergencyContact, icon = Icons.Default.Phone)
                InfoRow(label = "School Clinic Status", value = "Fully Cleared • No Known Allergies", icon = Icons.Default.Verified)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onCallEmergency,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Rose400),
                    border = BorderStroke(1.dp, Rose400.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Rose400, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Dial Emergency Medical Line (${student.emergencyContact})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 3: CONDUCT, LEADERSHIP & REMARKS
// =========================================================================
@Composable
private fun ConductAndRemarksTab(
    student: StudentRecord,
    isManagement: Boolean,
    onEditRemarks: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Form Master & Principal Appraisal
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Amber400.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Form Master Appraisal & Conduct",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    if (isManagement) {
                        IconButton(onClick = onEditRemarks, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Amber400, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Teacher Appraisal:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Amber400)
                            Row {
                                repeat(5) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Amber400, modifier = Modifier.size(13.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${student.behaviorRemark}\"",
                            fontSize = 12.sp,
                            color = Slate200,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "— Mr. Adeleke Ayomide (Form Master, SS 1 Science)",
                            fontSize = 10.sp,
                            color = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Principal's Remarks
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Principal's Moral & Discipline Remark:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo400)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"An exemplary student whose character, intellectual discipline, and leadership inspire peers. Highly recommended for Senior Prefect honors.\"",
                            fontSize = 12.sp,
                            color = Slate200,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        // Extracurricular & Leadership Affiliations
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, DarkBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Extracurricular, Clubs & Honors",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(label = "Active Clubs & Societies", value = student.clubAffiliations, icon = Icons.Default.Groups)
                InfoRow(label = "House Sports Division", value = "${student.houseName} • Track & Field Lead Runner", icon = Icons.Default.EmojiEvents)
                InfoRow(label = "Competitions & Honors", value = "2024 Ogun State Science Olympiad • 2nd Place Finalist", icon = Icons.Default.MilitaryTech)
            }
        }
    }
}

// =========================================================================
// REUSABLE HELPER COMPOSABLES
// =========================================================================
@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate900,
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 10.sp, color = Slate400, maxLines = 1)
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Slate400,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Slate500,
            modifier = Modifier
                .size(15.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = Slate400)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
        }
    }
}

private fun defaultFallbackStudent(): StudentRecord {
    return StudentRecord(
        id = 1,
        fullName = "Adeleke David Oluwaseun",
        studentId = "GRS/2024/0428",
        assignedClass = "SS 1 Science",
        parentName = "Chief & Mrs. Adeleke",
        parentPhone = "+234 816 620 5113",
        parentEmail = "parent@grazielroyalschools.edu.ng",
        passcode = "0428",
        dateEnrolled = "Sept 2024",
        isActive = true,
        gender = "Male",
        dob = "14 May 2008",
        houseName = "Royal Blue House",
        bloodGroup = "O+",
        academicStatus = "Academic Scholar (Honors)",
        gpa = "4.85 / 5.0 (Distinction)",
        classPosition = "1st of 34 Students",
        attendanceRate = "98.2%",
        clubAffiliations = "STEM & Robotics Club (President), Literary & Debate Society",
        behaviorRemark = "Outstanding leadership, disciplined demeanor, and exemplary academic curiosity.",
        emergencyContact = "+234 816 620 5113",
        homeAddress = "Plot 12, Royal Palm Estate, Ifo/Ota Axis, Ogun State"
    )
}
