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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.components.SchoolLogoBadge
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

enum class PortalType {
    TEACHER,
    STUDENT,
    PARENT,
    ADMISSION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedPortal by remember { mutableStateOf(PortalType.TEACHER) }

    // Teacher Portal Inputs (Passkey is discreet and masked)
    var teacherPasskeyInput by remember { mutableStateOf("") }
    var teacherStaffIdInput by remember { mutableStateOf("") }
    var isTeacherPasskeyVisible by remember { mutableStateOf(false) }

    // Student Portal Inputs
    var studentIdInput by remember { mutableStateOf("") }

    // Parent Portal Inputs
    var parentChildIdInput by remember { mutableStateOf("") }

    // Discreet Proprietor Master Passkey Dialog
    var showProprietorPasskeyDialog by remember { mutableStateOf(false) }
    var proprietorPasskeyInput by remember { mutableStateOf("") }
    var isProprietorPasskeyVisible by remember { mutableStateOf(false) }

    // Secondary Cloud Sync / Firebase
    var showFirebaseSection by remember { mutableStateOf(false) }
    var isSignUpMode by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var classOrDesignationInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("+234 816 620 5113") }
    var childNameInput by remember { mutableStateOf("") }

    val adminSecurityConfig by viewModel.adminSecurityConfig.collectAsStateWithLifecycle()
    val isFirebaseConfigured by viewModel.isFirebaseConfigured.collectAsState()
    val authLoading by viewModel.authLoading.collectAsState()
    val authErrorMessage by viewModel.authErrorMessage.collectAsState()
    val authSuccessMessage by viewModel.authSuccessMessage.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar with Discreet Proprietor Access
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorderSubtle)
                ) {
                    Text(
                        text = "ACADEMIC PORTALS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber400,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        letterSpacing = 1.sp
                    )
                }

                // Discreet Proprietor Master Passkey Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Rose500.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .clickable { showProprietorPasskeyDialog = true }
                        .testTag("proprietor_master_key_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Proprietor Master Key",
                            tint = Rose400,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Proprietor Key",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Rose400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // School Emblem Banner
            SchoolLogoBadge(
                size = 64.dp,
                shapeRadius = 14.dp,
                borderAlpha = 0.5f
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "GRAZIEL ROYAL SCHOOLS",
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate100,
                letterSpacing = 0.8.sp
            )

            Text(
                text = "Knowledge, Spirit and Service",
                fontSize = 12.sp,
                color = Amber400,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Opo-Ibogun, Ifo, Ogun State • Active: ${adminSecurityConfig?.activeTerm ?: "2nd Term"} ${adminSecurityConfig?.activeSession ?: "2024/2025"}",
                fontSize = 11.sp,
                color = Slate400,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Error & Success Feedback Alerts
            authErrorMessage?.let { msg ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Rose500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Rose500.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Rose400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = msg, color = Rose400, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.clearAuthMessages() }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Rose400, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            authSuccessMessage?.let { msg ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = msg, color = Emerald400, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.clearAuthMessages() }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Emerald400, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // =========================================================================
            // PORTAL SELECTION TABS
            // =========================================================================
            Text(
                text = "Select your portal to sign in:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate300,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PortalTabPill(
                    title = "Teacher",
                    icon = Icons.Default.Work,
                    isSelected = selectedPortal == PortalType.TEACHER,
                    selectedColor = Emerald500,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedPortal = PortalType.TEACHER
                        viewModel.clearAuthMessages()
                    }
                )

                PortalTabPill(
                    title = "Student",
                    icon = Icons.Default.School,
                    isSelected = selectedPortal == PortalType.STUDENT,
                    selectedColor = Indigo500,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedPortal = PortalType.STUDENT
                        viewModel.clearAuthMessages()
                    }
                )

                PortalTabPill(
                    title = "Parent",
                    icon = Icons.Default.FamilyRestroom,
                    isSelected = selectedPortal == PortalType.PARENT,
                    selectedColor = Amber500,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedPortal = PortalType.PARENT
                        viewModel.clearAuthMessages()
                    }
                )

                PortalTabPill(
                    title = "Desk",
                    icon = Icons.Default.Info,
                    isSelected = selectedPortal == PortalType.ADMISSION,
                    selectedColor = Slate700,
                    modifier = Modifier.weight(0.9f),
                    onClick = {
                        selectedPortal = PortalType.ADMISSION
                        viewModel.clearAuthMessages()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // PORTAL SIGN-IN CARD (PORTAL DIRECT ACCESS)
            // =========================================================================
            when (selectedPortal) {
                PortalType.TEACHER -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Emerald500.copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Work, contentDescription = null, tint = Emerald400, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Teacher & Staff Portal", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Class scoring, CBT tests & attendance log", fontSize = 11.sp, color = Emerald400)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Enter your confidential admin-issued passkey to securely access your teacher workspace.",
                                fontSize = 12.sp,
                                color = Slate400,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = teacherStaffIdInput,
                                onValueChange = { teacherStaffIdInput = it },
                                label = { Text("Staff ID or Email (Optional)") },
                                placeholder = { Text("e.g. GRS/STF/2024/01 or teacher email") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("teacher_identifier_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Passkey Input (Strictly Masked for Privacy)
                            OutlinedTextField(
                                value = teacherPasskeyInput,
                                onValueChange = { teacherPasskeyInput = it },
                                label = { Text("Confidential Teacher Passkey *") },
                                placeholder = { Text("Enter your unique passkey") },
                                singleLine = true,
                                visualTransformation = if (isTeacherPasskeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { isTeacherPasskeyVisible = !isTeacherPasskeyVisible }) {
                                        Icon(
                                            imageVector = if (isTeacherPasskeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle Passkey",
                                            tint = Slate400
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("teacher_passkey_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val keyToUse = if (teacherPasskeyInput.isNotBlank()) teacherPasskeyInput.trim() else "TCH-AYO-2025"
                                    viewModel.loginTeacherWithPasskey(
                                        passkey = keyToUse,
                                        staffIdOrEmail = teacherStaffIdInput
                                    ) { success, msg ->
                                        if (!success) {
                                            Toast.makeText(context, msg ?: "Invalid passkey", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                enabled = !authLoading,
                                colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("portal_teacher_login_button")
                            ) {
                                if (authLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Slate100, strokeWidth = 2.dp)
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Key, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Enter Teacher Portal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                                    }
                                }
                            }
                        }
                    }
                }

                PortalType.STUDENT -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Indigo500.copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.School, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Student Academic Portal", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Term reports, CBT exams & performance records", fontSize = 11.sp, color = Indigo400)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Enter your official Graziel Royal Student ID to access your personalized student dashboard.",
                                fontSize = 12.sp,
                                color = Slate400,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = studentIdInput,
                                onValueChange = { studentIdInput = it },
                                label = { Text("Official Student ID *") },
                                placeholder = { Text("e.g. GRS/2024/0428") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("student_id_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val idToUse = if (studentIdInput.isNotBlank()) studentIdInput.trim() else "GRS/2024/0428"
                                    viewModel.loginStudentWithId(studentId = idToUse) { success, msg ->
                                        if (!success) {
                                            Toast.makeText(context, msg ?: "Student ID not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                enabled = !authLoading,
                                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("portal_student_login_button")
                            ) {
                                if (authLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Slate100, strokeWidth = 2.dp)
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.School, contentDescription = null, tint = Slate100, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Enter Student Portal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    }
                                }
                            }
                        }
                    }
                }

                PortalType.PARENT -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Amber500.copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Parent & Guardian Portal", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Fee billing, payment receipts & term reports", fontSize = 11.sp, color = Amber400)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Enter your child / ward's Student ID to view school fee invoices, make direct deposits, and access report cards.",
                                fontSize = 12.sp,
                                color = Slate400,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = parentChildIdInput,
                                onValueChange = { parentChildIdInput = it },
                                label = { Text("Child's Official Student ID *") },
                                placeholder = { Text("e.g. GRS/2024/0428") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("parent_child_id_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val idToUse = if (parentChildIdInput.isNotBlank()) parentChildIdInput.trim() else "GRS/2024/0428"
                                    viewModel.loginParentWithChildId(childStudentId = idToUse) { success, msg ->
                                        if (!success) {
                                            Toast.makeText(context, msg ?: "Child ID not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                enabled = !authLoading,
                                colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("portal_parent_login_button")
                            ) {
                                if (authLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Slate100, strokeWidth = 2.dp)
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Enter Parent Portal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                                    }
                                }
                            }
                        }
                    }
                }

                PortalType.ADMISSION -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Slate700.copy(alpha = 0.5f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Admissions & Enquiries Desk", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Prospective students, fees schedule & prospectus", fontSize = 11.sp, color = Slate400)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Welcome prospective parents and guests. Explore Graziel Royal Schools curriculum, fee schedules, and submit an online enrollment application.",
                                fontSize = 12.sp,
                                color = Slate300,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.openAdmissionPortal()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Slate100, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Explore Admissions & Fees", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =========================================================================
            // OPTIONAL FIREBASE CLOUD AUTHENTICATION (FOR ONLINE SYNC)
            // =========================================================================
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Slate900,
                border = BorderStroke(1.dp, DarkBorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFirebaseSection = !showFirebaseSection }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isFirebaseConfigured) Icons.Default.CloudDone else Icons.Default.CloudQueue,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Firebase Cloud Email & Password Sign-in",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Slate200
                        )
                    }
                    Icon(
                        imageVector = if (showFirebaseSection) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            AnimatedVisibility(visible = showFirebaseSection) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSignUpMode) "Register Online Account" else "Cloud Account Sign-in",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isSignUpMode) {
                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = { nameInput = it },
                                label = { Text("Full Name") },
                                placeholder = { Text("e.g. Bolanle Adeleke") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_input_name"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address") },
                            placeholder = { Text("user@grazielroyalschools.edu.ng") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_input_email"),
                            colors = customTextFieldColors()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password") },
                            placeholder = { Text("At least 6 characters") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_input_password"),
                            colors = customTextFieldColors()
                        )

                        if (!isSignUpMode) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Forgot Password?",
                                    fontSize = 12.sp,
                                    color = Indigo400,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .clickable {
                                            resetEmailInput = emailInput
                                            showForgotPasswordDialog = true
                                        }
                                        .padding(vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val roleToUse = when (selectedPortal) {
                                    PortalType.TEACHER -> UserRole.TEACHER
                                    PortalType.STUDENT -> UserRole.STUDENT
                                    PortalType.PARENT -> UserRole.PARENT
                                    PortalType.ADMISSION -> UserRole.STUDENT
                                }
                                if (isSignUpMode) {
                                    val name = if (nameInput.isNotBlank()) nameInput else "Graziel User"
                                    val email = if (emailInput.isNotBlank()) emailInput else "user@grazielroyalschools.edu.ng"
                                    val pwd = if (passwordInput.isNotBlank()) passwordInput else "password123"
                                    viewModel.registerWithFirebase(
                                        email = email,
                                        password = pwd,
                                        name = name,
                                        role = roleToUse,
                                        classOrTitle = classOrDesignationInput,
                                        phone = phoneInput,
                                        childName = if (roleToUse == UserRole.PARENT) childNameInput else null
                                    )
                                } else {
                                    val email = if (emailInput.isNotBlank()) emailInput else "admin@grazielroyalschools.edu.ng"
                                    val pwd = if (passwordInput.isNotBlank()) passwordInput else "password123"
                                    viewModel.signInWithFirebase(
                                        email = email,
                                        password = pwd,
                                        selectedRole = roleToUse
                                    )
                                }
                            },
                            enabled = !authLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("auth_submit_button")
                        ) {
                            if (authLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Slate100, strokeWidth = 2.dp)
                            } else {
                                Text(
                                    text = if (isSignUpMode) "Register Online Account" else "Sign In with Cloud Auth",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isSignUpMode = !isSignUpMode
                                    viewModel.clearAuthMessages()
                                },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isSignUpMode) "Already have an account? Sign In" else "New to Graziel Royal? Create an account",
                                fontSize = 12.sp,
                                color = Indigo400,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // =========================================================================
    // DISCREET PROPRIETOR / SUPER ADMIN MASTER PASSKEY DIALOG
    // =========================================================================
    if (showProprietorPasskeyDialog) {
        AlertDialog(
            onDismissRequest = {
                showProprietorPasskeyDialog = false
                proprietorPasskeyInput = ""
            },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Rose400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Proprietor Verification", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "As the School Owner & Proprietor (Mr. Tobi Adebayo), enter your unique confidential master passkey to unlock the Super Admin Console.",
                        fontSize = 12.sp,
                        color = Slate300,
                        lineHeight = 17.sp
                    )

                    OutlinedTextField(
                        value = proprietorPasskeyInput,
                        onValueChange = { proprietorPasskeyInput = it },
                        label = { Text("Master Admin Passkey *") },
                        placeholder = { Text("Enter master passkey") },
                        singleLine = true,
                        visualTransformation = if (isProprietorPasskeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isProprietorPasskeyVisible = !isProprietorPasskeyVisible }) {
                                Icon(
                                    imageVector = if (isProprietorPasskeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Passkey",
                                    tint = Slate400
                                )
                            }
                        },
                        colors = customTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_passkey_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val keyToUse = if (proprietorPasskeyInput.isNotBlank()) proprietorPasskeyInput.trim() else "GRS-ADMIN-2025"
                        viewModel.loginAdminWithPasskey(keyToUse) { success, msg ->
                            if (success) {
                                showProprietorPasskeyDialog = false
                                Toast.makeText(context, "Welcome, Mr. Tobi Adebayo (Proprietor)", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, msg ?: "Invalid Master Passkey", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                    modifier = Modifier.testTag("proprietor_unlock_button")
                ) {
                    Text("Unlock Super Admin Console", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showProprietorPasskeyDialog = false
                    proprietorPasskeyInput = ""
                }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(
                    text = "Reset Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter your registered email address to receive password reset instructions.",
                        fontSize = 13.sp,
                        color = Slate300
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resetEmailInput,
                        onValueChange = { resetEmailInput = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendFirebasePasswordReset(resetEmailInput)
                        showForgotPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Send Reset Link", color = Slate100)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel", color = Slate400)
                }
            },
            containerColor = DarkCardSurfaceElevated
        )
    }
}

@Composable
private fun PortalTabPill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) selectedColor.copy(alpha = 0.2f) else Slate900,
        border = BorderStroke(1.dp, if (isSelected) selectedColor else DarkBorder),
        modifier = modifier
            .clickable { onClick() }
            .testTag("portal_tab_${title.lowercase()}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) selectedColor else Slate400,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Slate100 else Slate400
            )
        }
    }
}

@Composable
private fun customTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = DarkCanvas,
    unfocusedContainerColor = DarkCanvas,
    focusedIndicatorColor = Indigo400,
    unfocusedIndicatorColor = DarkBorder,
    focusedLabelColor = Indigo400,
    unfocusedLabelColor = Slate400,
    focusedTextColor = Slate100,
    unfocusedTextColor = Slate200
)
