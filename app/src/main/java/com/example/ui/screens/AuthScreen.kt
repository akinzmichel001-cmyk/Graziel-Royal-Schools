package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    var selectedRoleIndex by remember { mutableIntStateOf(2) } // default: Student
    val roles = listOf(
        UserRole.ADMIN,
        UserRole.TEACHER,
        UserRole.STUDENT,
        UserRole.PARENT
    )
    val currentSelectedRole = roles[selectedRoleIndex]

    var isSignUpMode by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var resetEmailInput by remember { mutableStateOf("") }
    var showFirebaseGuide by remember { mutableStateOf(false) }

    // Form fields
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var classOrDesignationInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("+234 816 620 5113") }
    var childNameInput by remember { mutableStateOf("Adeleke David Oluwaseun") }

    val defaultAccounts = viewModel.getDefaultAccounts()
    val demoAccountForRole = defaultAccounts.find { it.role == currentSelectedRole }

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
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Indigo600,
                border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.4f)),
                modifier = Modifier.size(64.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_school_logo),
                    contentDescription = "Graziel Royal Emblem",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }

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
                        text = if (isFirebaseConfigured) "Firebase Cloud Connected" else "Firebase Auth Enabled • Tap for setup guide",
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

            // Role Description Badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Slate900,
                border = BorderStroke(1.dp, DarkBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (badgeText, badgeColor) = when (currentSelectedRole) {
                        UserRole.ADMIN -> "Full Access • Super Admin & Chat Moderation" to Rose400
                        UserRole.TEACHER -> "CBT Creator • Staff Clock-In • Grading & Staff Room" to Emerald400
                        UserRole.STUDENT -> "Online CBT Tests • Homework • Class Group Chat" to Indigo400
                        UserRole.PARENT -> "Student Bills • Report Cards • WhatsApp Helpline" to Amber400
                        else -> "Portal Access" to Slate300
                    }
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = badgeText,
                        fontSize = 12.sp,
                        color = Slate200,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1-Click Fast Demo Login Card
            demoAccountForRole?.let { account ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, when (currentSelectedRole) {
                        UserRole.ADMIN -> Rose500.copy(alpha = 0.4f)
                        UserRole.TEACHER -> Emerald500.copy(alpha = 0.4f)
                        UserRole.STUDENT -> Indigo500.copy(alpha = 0.4f)
                        UserRole.PARENT -> Amber500.copy(alpha = 0.4f)
                        else -> Indigo500.copy(alpha = 0.4f)
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.loginAs(account) }
                        .testTag("quick_demo_login_button")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1-TAP INSTANT DEMO LOGIN",
                                fontSize = 11.sp,
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
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Slate300,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = account.fullName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )

                        Text(
                            text = "${account.titleOrDesignation ?: account.assignedClass ?: "Member"} • ${account.regOrStaffId}",
                            fontSize = 12.sp,
                            color = Slate400
                        )

                        if (account.childName != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ward: ${account.childName} (${account.childRegNumber})",
                                fontSize = 12.sp,
                                color = Amber400
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.loginAs(account) },
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
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Enter as ${account.fullName.split(" ").first()}",
                                fontWeight = FontWeight.Bold,
                                color = if (currentSelectedRole == UserRole.PARENT) DarkCanvas else Slate100
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider OR Firebase Custom Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = DarkBorder)
                Text(
                    text = if (isSignUpMode) " CREATE FIREBASE ACCOUNT " else " OR FIREBASE AUTH SIGN IN ",
                    fontSize = 11.sp,
                    color = Slate500,
                    fontWeight = FontWeight.SemiBold
                )
                Divider(modifier = Modifier.weight(1f), color = DarkBorder)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Firebase Form Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
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
