package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
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
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    var selectedRoleIndex by remember { mutableIntStateOf(1) } // default: Teacher
    val roles = listOf(
        UserRole.ADMIN,
        UserRole.TEACHER,
        UserRole.STUDENT,
        UserRole.PARENT
    )
    val currentSelectedRole = roles[selectedRoleIndex]

    // Passkey / Student ID Form Inputs
    var teacherPasskeyInput by remember { mutableStateOf("TCH-AYO-2025") }
    var teacherIdentifierInput by remember { mutableStateOf("") }
    var adminPasskeyInput by remember { mutableStateOf("GRS-ADMIN-2025") }
    var studentIdInput by remember { mutableStateOf("GRS/2024/0428") }
    var parentChildIdInput by remember { mutableStateOf("GRS/2024/0428") }
    var isPasskeyVisible by remember { mutableStateOf(false) }

    // Secondary Firebase Cloud Mode
    var showFirebaseSection by remember { mutableStateOf(false) }
    var isSignUpMode by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var showFirebaseGuide by remember { mutableStateOf(false) }

    // Firebase Form Fields
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var classOrDesignationInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("+234 816 620 5113") }
    var childNameInput by remember { mutableStateOf("Adeleke David Oluwaseun") }

    val defaultAccounts = viewModel.getDefaultAccounts()
    val demoAccountForRole = defaultAccounts.find { it.role == currentSelectedRole }
    val teacherAccounts by viewModel.teacherAccounts.collectAsStateWithLifecycle()
    val studentRecords by viewModel.studentRecords.collectAsStateWithLifecycle()
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
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // School Emblem Banner
            SchoolLogoBadge(
                size = 68.dp,
                shapeRadius = 16.dp,
                borderAlpha = 0.5f
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "GRAZIEL ROYAL SCHOOLS",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate100,
                letterSpacing = 1.sp
            )

            Text(
                text = "Knowledge, Spirit and Service",
                fontSize = 12.sp,
                color = Amber400,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Opo-Ibogun, Ifo, Ogun State • Founder: Mr. Tobi Adebayo",
                fontSize = 11.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Firebase Backend Status Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isFirebaseConfigured) Emerald500.copy(alpha = 0.15f) else Indigo600.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, if (isFirebaseConfigured) Emerald500.copy(alpha = 0.4f) else Indigo400.copy(alpha = 0.3f)),
                modifier = Modifier.clickable { showFirebaseGuide = !showFirebaseGuide }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFirebaseConfigured) Icons.Default.CloudDone else Icons.Default.CloudQueue,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = if (isFirebaseConfigured) Emerald400 else Indigo400
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isFirebaseConfigured) "Firebase Cloud Online" else "Local Passkey Security Active • Info",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isFirebaseConfigured) Emerald400 else Slate300
                    )
                }
            }

            // Firebase Guide Modal / Expandable info
            AnimatedVisibility(visible = showFirebaseGuide) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔥 Firebase Setup Instructions",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400
                            )
                            IconButton(
                                onClick = { showFirebaseGuide = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Slate400,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. Go to Firebase Console → Create/select project.\n2. Add Android App with Package ID: com.aistudio.grazielroyalschools.app\n3. Download google-services.json and place it in the /app directory.\n4. Enable 'Email/Password' under Firebase Auth > Sign-in method.\n5. Ready! Users will authenticate securely via your Firebase Cloud backend.",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = Slate300
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error / Success Banners
            AnimatedVisibility(visible = authErrorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Rose500.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Rose500.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Rose400,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = authErrorMessage ?: "",
                            fontSize = 12.sp,
                            color = Rose400,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearAuthMessages() },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = Rose400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = authSuccessMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Emerald500.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
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
                            text = authSuccessMessage ?: "",
                            fontSize = 12.sp,
                            color = Emerald400,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearAuthMessages() },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = Emerald400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Role Segmented Selector
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                TabRow(
                    selectedTabIndex = selectedRoleIndex,
                    containerColor = DarkCardSurface,
                    contentColor = Indigo400,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedRoleIndex]),
                            color = when (currentSelectedRole) {
                                UserRole.ADMIN -> Rose400
                                UserRole.TEACHER -> Emerald400
                                UserRole.STUDENT -> Indigo400
                                UserRole.PARENT -> Amber400
                                else -> Indigo400
                            },
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    roles.forEachIndexed { index, role ->
                        val (roleTitle, roleIcon) = when (role) {
                            UserRole.ADMIN -> "Admin" to Icons.Default.AdminPanelSettings
                            UserRole.TEACHER -> "Teacher" to Icons.Default.Work
                            UserRole.STUDENT -> "Student" to Icons.Default.School
                            UserRole.PARENT -> "Parent" to Icons.Default.FamilyRestroom
                            else -> "Guest" to Icons.Default.Person
                        }
                        Tab(
                            selected = selectedRoleIndex == index,
                            onClick = {
                                selectedRoleIndex = index
                                viewModel.clearAuthMessages()
                            },
                            modifier = Modifier.testTag("auth_tab_${role.name.lowercase()}"),
                            text = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = roleIcon,
                                        contentDescription = roleTitle,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (selectedRoleIndex == index) Slate100 else Slate500
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = roleTitle,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedRoleIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedRoleIndex == index) Slate100 else Slate400
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================================
            // PRIMARY AUTHENTICATION CARD: ROLE-BASED PASSKEY / STUDENT ID PORTAL ENTRY
            // =========================================================================
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, when (currentSelectedRole) {
                    UserRole.ADMIN -> Rose500.copy(alpha = 0.5f)
                    UserRole.TEACHER -> Emerald500.copy(alpha = 0.5f)
                    UserRole.STUDENT -> Indigo500.copy(alpha = 0.5f)
                    UserRole.PARENT -> Amber500.copy(alpha = 0.5f)
                    else -> Indigo500.copy(alpha = 0.5f)
                }),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header with Icon & Role-Specific Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = when (currentSelectedRole) {
                                UserRole.ADMIN -> Rose500.copy(alpha = 0.15f)
                                UserRole.TEACHER -> Emerald500.copy(alpha = 0.15f)
                                UserRole.STUDENT -> Indigo500.copy(alpha = 0.15f)
                                UserRole.PARENT -> Amber500.copy(alpha = 0.15f)
                                else -> Indigo500.copy(alpha = 0.15f)
                            },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (currentSelectedRole) {
                                        UserRole.ADMIN -> Icons.Default.Security
                                        UserRole.TEACHER -> Icons.Default.VpnKey
                                        UserRole.STUDENT -> Icons.Default.School
                                        UserRole.PARENT -> Icons.Default.FamilyRestroom
                                        else -> Icons.Default.Person
                                    },
                                    contentDescription = null,
                                    tint = when (currentSelectedRole) {
                                        UserRole.ADMIN -> Rose400
                                        UserRole.TEACHER -> Emerald400
                                        UserRole.STUDENT -> Indigo400
                                        UserRole.PARENT -> Amber400
                                        else -> Indigo400
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = when (currentSelectedRole) {
                                    UserRole.ADMIN -> "Super Admin Portal"
                                    UserRole.TEACHER -> "Teacher Portal Access"
                                    UserRole.STUDENT -> "Student Portal Access"
                                    UserRole.PARENT -> "Parent & Guardian Portal"
                                    else -> "Portal Login"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )

                            Text(
                                text = when (currentSelectedRole) {
                                    UserRole.ADMIN -> "Authentication via Master Passkey"
                                    UserRole.TEACHER -> "Requires Admin-Issued Teacher Passkey"
                                    UserRole.STUDENT -> "Enter Official Student ID"
                                    UserRole.PARENT -> "Enter Child's Student ID"
                                    else -> "Authentication"
                                },
                                fontSize = 11.sp,
                                color = when (currentSelectedRole) {
                                    UserRole.ADMIN -> Rose400
                                    UserRole.TEACHER -> Emerald400
                                    UserRole.STUDENT -> Indigo400
                                    UserRole.PARENT -> Amber400
                                    else -> Indigo400
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role-Specific Explanatory Banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (currentSelectedRole) {
                                UserRole.TEACHER -> "🔒 For strict privacy, only teachers registered by the School Admin can login. Please enter your unique Teacher Passkey."
                                UserRole.ADMIN -> "🛡️ Super Admin Console is strictly protected. Please enter the master administrative security passkey."
                                UserRole.STUDENT -> "🎓 Please enter your official Student ID (e.g. GRS/2024/0428) assigned by the school admin to access your tests and results."
                                UserRole.PARENT -> "👨‍👩‍👧 Please enter your child's official Student ID (e.g. GRS/2024/0428) to access your ward's fee bills and report cards."
                                else -> "Enter your credentials to continue."
                            },
                            fontSize = 11.sp,
                            color = Slate300,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // =====================================================================
                    // ROLE-SPECIFIC INPUT FIELDS
                    // =====================================================================
                    when (currentSelectedRole) {
                        UserRole.TEACHER -> {
                            // Teacher Passkey Field
                            OutlinedTextField(
                                value = teacherPasskeyInput,
                                onValueChange = { teacherPasskeyInput = it },
                                label = { Text("Unique Teacher Passkey *") },
                                placeholder = { Text("e.g. TCH-AYO-2025") },
                                singleLine = true,
                                visualTransformation = if (isPasskeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { isPasskeyVisible = !isPasskeyVisible }) {
                                        Icon(
                                            imageVector = if (isPasskeyVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle Passkey Visibility",
                                            tint = Slate400
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("teacher_passkey_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Optional Teacher Name / Staff ID
                            OutlinedTextField(
                                value = teacherIdentifierInput,
                                onValueChange = { teacherIdentifierInput = it },
                                label = { Text("Staff ID or Name (Optional)") },
                                placeholder = { Text("e.g. GRS/STF/2021/014 or Mr. Adeleke") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("teacher_identifier_input"),
                                colors = customTextFieldColors()
                            )

                            // Quick sample passkey hints
                            if (teacherAccounts.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Available Teacher Passkeys in Registry:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate400
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    teacherAccounts.take(3).forEach { tch ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Emerald500.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)),
                                            modifier = Modifier
                                                .clickable {
                                                    teacherPasskeyInput = tch.passkey
                                                    teacherIdentifierInput = tch.fullName
                                                }
                                        ) {
                                            Text(
                                                text = "${tch.fullName.split(" ").last()}: ${tch.passkey}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Emerald400,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        UserRole.ADMIN -> {
                            // Admin Master Passkey Field
                            OutlinedTextField(
                                value = adminPasskeyInput,
                                onValueChange = { adminPasskeyInput = it },
                                label = { Text("Master Admin Passkey *") },
                                placeholder = { Text("GRS-ADMIN-2025") },
                                singleLine = true,
                                visualTransformation = if (isPasskeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { isPasskeyVisible = !isPasskeyVisible }) {
                                        Icon(
                                            imageVector = if (isPasskeyVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle Visibility",
                                            tint = Slate400
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_passkey_input"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Demo key badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { adminPasskeyInput = adminSecurityConfig?.adminPasskey ?: "GRS-ADMIN-2025" }
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = Rose400, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Admin Passkey: ${adminSecurityConfig?.adminPasskey ?: "GRS-ADMIN-2025"} (Tap to fill)",
                                    fontSize = 11.sp,
                                    color = Rose400,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        UserRole.STUDENT -> {
                            // Student ID Field
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

                            // Quick sample student IDs
                            if (studentRecords.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Available Enrolled Student IDs:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate400
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    studentRecords.take(3).forEach { st ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Indigo500.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
                                            modifier = Modifier.clickable { studentIdInput = st.studentId }
                                        ) {
                                            Text(
                                                text = "${st.fullName.split(" ").first()}: ${st.studentId}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Indigo400,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        UserRole.PARENT -> {
                            // Child Student ID Field
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

                            // Quick sample IDs
                            if (studentRecords.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Available Ward IDs:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate400
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    studentRecords.take(3).forEach { st ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Amber500.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f)),
                                            modifier = Modifier.clickable { parentChildIdInput = st.studentId }
                                        ) {
                                            Text(
                                                text = "${st.parentName.split(" ").last()}: ${st.studentId}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Amber400,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        else -> Unit
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // =====================================================================
                    // ROLE-SPECIFIC LOGIN ACTION BUTTON
                    // =====================================================================
                    Button(
                        onClick = {
                            when (currentSelectedRole) {
                                UserRole.TEACHER -> {
                                    viewModel.loginTeacherWithPasskey(
                                        passkey = teacherPasskeyInput,
                                        staffIdOrEmail = teacherIdentifierInput
                                    ) { _, _ -> }
                                }
                                UserRole.ADMIN -> {
                                    viewModel.loginAdminWithPasskey(
                                        passkey = adminPasskeyInput
                                    ) { _, _ -> }
                                }
                                UserRole.STUDENT -> {
                                    viewModel.loginStudentWithId(
                                        studentId = studentIdInput
                                    ) { _, _ -> }
                                }
                                UserRole.PARENT -> {
                                    viewModel.loginParentWithChildId(
                                        childStudentId = parentChildIdInput
                                    ) { _, _ -> }
                                }
                                else -> {
                                    demoAccountForRole?.let { viewModel.loginAs(it) }
                                }
                            }
                        },
                        enabled = !authLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (currentSelectedRole) {
                                UserRole.ADMIN -> Rose500
                                UserRole.TEACHER -> Emerald500
                                UserRole.STUDENT -> Indigo600
                                UserRole.PARENT -> Amber500
                                else -> Indigo600
                            }
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("portal_passkey_login_button")
                    ) {
                        if (authLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Slate100,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = when (currentSelectedRole) {
                                        UserRole.ADMIN -> Icons.Default.Lock
                                        UserRole.TEACHER -> Icons.Default.Key
                                        UserRole.STUDENT -> Icons.Default.School
                                        UserRole.PARENT -> Icons.Default.FamilyRestroom
                                        else -> Icons.Default.ArrowForward
                                    },
                                    contentDescription = null,
                                    tint = if (currentSelectedRole == UserRole.PARENT) DarkCanvas else Slate100,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when (currentSelectedRole) {
                                        UserRole.ADMIN -> "Unlock Super Admin Console"
                                        UserRole.TEACHER -> "Enter Teacher Portal"
                                        UserRole.STUDENT -> "Enter Student Portal"
                                        UserRole.PARENT -> "Enter Parent Portal"
                                        else -> "Sign In"
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentSelectedRole == UserRole.PARENT) DarkCanvas else Slate100
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================================
            // 1-TAP INSTANT DEMO LOGIN CARD (FOR FAST EVALUATION)
            // =========================================================================
            demoAccountForRole?.let { account ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.loginAs(account) }
                        .testTag("quick_demo_login_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "⚡ 1-TAP QUICK TEST BYPASS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (currentSelectedRole) {
                                    UserRole.ADMIN -> Rose400
                                    UserRole.TEACHER -> Emerald400
                                    UserRole.STUDENT -> Indigo400
                                    UserRole.PARENT -> Amber400
                                    else -> Indigo400
                                },
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Enter as ${account.fullName}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "${account.titleOrDesignation ?: account.assignedClass ?: "Member"} • ${account.regOrStaffId}",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Quick Enter",
                            tint = Slate300,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================================
            // COLLAPSIBLE FIREBASE CLOUD SIGN IN / REGISTRATION
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
                            text = "Alternative: Cloud Email & Password Auth",
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
                            text = if (isSignUpMode) "Register ${currentSelectedRole.name.lowercase().replaceFirstChar { it.uppercase() }} Account" else "Sign In with Email & Password",
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
                                placeholder = { Text("e.g., Bolanle A. Adeleke") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_input_name"),
                                colors = customTextFieldColors()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (currentSelectedRole == UserRole.TEACHER) {
                                OutlinedTextField(
                                    value = classOrDesignationInput,
                                    onValueChange = { classOrDesignationInput = it },
                                    label = { Text("Subject / Department") },
                                    placeholder = { Text("e.g., Mathematics & Physics Master") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = customTextFieldColors()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            } else if (currentSelectedRole == UserRole.STUDENT) {
                                OutlinedTextField(
                                    value = classOrDesignationInput,
                                    onValueChange = { classOrDesignationInput = it },
                                    label = { Text("Enrolled Class") },
                                    placeholder = { Text("e.g., SS 1 Science") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = customTextFieldColors()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            } else if (currentSelectedRole == UserRole.PARENT) {
                                OutlinedTextField(
                                    value = childNameInput,
                                    onValueChange = { childNameInput = it },
                                    label = { Text("Child / Ward's Full Name") },
                                    placeholder = { Text("e.g., Adeleke David Oluwaseun") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = customTextFieldColors()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address") },
                            placeholder = {
                                Text(
                                    when (currentSelectedRole) {
                                        UserRole.ADMIN -> "admin@grazielroyalschools.edu.ng"
                                        UserRole.TEACHER -> "teacher@grazielroyalschools.edu.ng"
                                        UserRole.STUDENT -> "student.2024.0428@grazielroyalschools.edu.ng"
                                        UserRole.PARENT -> "parent.adeleke@gmail.com"
                                        else -> "user@grazielroyalschools.edu.ng"
                                    }
                                )
                            },
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
                                if (isSignUpMode) {
                                    val name = if (nameInput.isNotBlank()) nameInput else "New ${currentSelectedRole.name} User"
                                    val email = if (emailInput.isNotBlank()) emailInput else "user@grazielroyalschools.edu.ng"
                                    val pwd = if (passwordInput.isNotBlank()) passwordInput else "password123"
                                    viewModel.registerWithFirebase(
                                        email = email,
                                        password = pwd,
                                        name = name,
                                        role = currentSelectedRole,
                                        classOrTitle = classOrDesignationInput,
                                        phone = phoneInput,
                                        childName = if (currentSelectedRole == UserRole.PARENT) childNameInput else null
                                    )
                                } else {
                                    val email = if (emailInput.isNotBlank()) emailInput else (demoAccountForRole?.email ?: "student@grazielroyalschools.edu.ng")
                                    val pwd = if (passwordInput.isNotBlank()) passwordInput else "password123"
                                    viewModel.signInWithFirebase(
                                        email = email,
                                        password = pwd,
                                        selectedRole = currentSelectedRole,
                                        fallbackAccount = demoAccountForRole
                                    )
                                }
                            },
                            enabled = !authLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when (currentSelectedRole) {
                                    UserRole.ADMIN -> Rose500
                                    UserRole.TEACHER -> Emerald500
                                    UserRole.STUDENT -> Indigo600
                                    UserRole.PARENT -> Amber500
                                    else -> Indigo600
                                }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("auth_submit_button")
                        ) {
                            if (authLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Slate100,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = if (isSignUpMode) "Register Account" else "Sign In",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentSelectedRole == UserRole.PARENT) DarkCanvas else Slate100
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
