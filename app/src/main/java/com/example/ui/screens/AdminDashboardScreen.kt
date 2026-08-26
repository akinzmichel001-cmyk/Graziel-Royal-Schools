package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AdminSecurityConfig
import com.example.data.model.CbtTest
import com.example.data.model.FeeItem
import com.example.data.model.StudentRecord
import com.example.data.model.TeacherAccount
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

@Composable
fun AdminDashboardScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val feeItems by viewModel.feeItems.collectAsStateWithLifecycle()
    val staffClockRecords by viewModel.staffClockRecords.collectAsStateWithLifecycle()
    val isReportApproved by viewModel.isReportCardApproved.collectAsStateWithLifecycle()
    val isReportPublished by viewModel.isReportCardPublished.collectAsStateWithLifecycle()
    val teacherAccounts by viewModel.teacherAccounts.collectAsStateWithLifecycle()
    val studentRecords by viewModel.studentRecords.collectAsStateWithLifecycle()
    val adminSecurityConfig by viewModel.adminSecurityConfig.collectAsStateWithLifecycle()

    var showCreateFeeDialog by remember { mutableStateOf(false) }
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showEditPasskeyDialogForTeacher by remember { mutableStateOf<TeacherAccount?>(null) }
    var showChangeAdminPasskeyDialog by remember { mutableStateOf(false) }
    var showUpdateAcademicDialog by remember { mutableStateOf(false) }
    var showUpdateBankDialog by remember { mutableStateOf(false) }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard: $text", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Admin Header Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Rose500.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Rose400.copy(alpha = 0.4f)),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = Rose400,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "SUPER ADMIN CONSOLE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Rose400,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = currentUser?.fullName ?: "Mr. Tobi Adebayo",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = currentUser?.titleOrDesignation ?: "Founder & School Proprietor",
                                    fontSize = 12.sp,
                                    color = Amber400
                                )
                            }
                        }

                        // WhatsApp Admin hotline button
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Emerald500.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clickable { viewModel.openSchoolWhatsApp(context) }
                                .testTag("admin_whatsapp_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Forum,
                                    contentDescription = "WhatsApp",
                                    tint = Emerald400,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "WhatsApp Desk",
                                    fontSize = 11.sp,
                                    color = Emerald400,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = DarkBorderSubtle)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill("Faculty Staff", "${teacherAccounts.size}", Emerald400)
                        StatPill("Enrolled Students", "${studentRecords.size}", Indigo400)
                        StatPill("Fee Bills", "${feeItems.size}", Amber400)
                        StatPill("Approval", if (isReportApproved) "Ready" else "Pending", if (isReportApproved) Emerald400 else Rose400)
                    }
                }
            }
        }

        // =========================================================================
        // SECTION: ACADEMIC CALENDAR & ACTIVE SESSION CONTROLS
        // =========================================================================
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Amber500.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = Amber400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Academic Term & Session",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Controls school-wide term exams & billing period",
                                    fontSize = 11.sp,
                                    color = Amber400
                                )
                            }
                        }

                        Button(
                            onClick = { showUpdateAcademicDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_change_term_session_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Change Term/Year", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate900, RoundedCornerShape(10.dp))
                            .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Active Academic Term:", fontSize = 11.sp, color = Slate400)
                            Text(
                                text = adminSecurityConfig?.activeTerm ?: "2nd Term",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Amber400
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate800,
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text("Session Year:", fontSize = 10.sp, color = Slate400)
                                Text(
                                    text = adminSecurityConfig?.activeSession ?: "2024/2025",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // SECTION: SCHOOL PAYMENT BANK ACCOUNT & NOTIFICATION BOX
        // =========================================================================
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Indigo500.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalance,
                                        contentDescription = null,
                                        tint = Indigo400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "School Payment Account",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Parent fees & direct deposit account",
                                    fontSize = 11.sp,
                                    color = Indigo400
                                )
                            }
                        }

                        Button(
                            onClick = { showUpdateBankDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_edit_bank_details_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = Slate100, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Account", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Bank: ${adminSecurityConfig?.bankName ?: "Monie Point"}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Name: ${adminSecurityConfig?.bankAccountName ?: "Graziel Royal Schools Ltd."}", fontSize = 11.sp, color = Slate300)
                                    Text("Acc Number: ${adminSecurityConfig?.bankAccountNumber ?: "5255883539"}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Amber400)
                                }

                                IconButton(
                                    onClick = { copyToClipboard("Account Number", adminSecurityConfig?.bankAccountNumber ?: "5255883539") },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Account Number", tint = Amber400, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notifications Box Launcher Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openNotificationBox() },
                            colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                            border = BorderStroke(1.dp, Amber400.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_open_notification_box_button")
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Check Notification Box", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Button(
                            onClick = { showBroadcastDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_send_broadcast_button")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = Slate100, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dispatch Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }
                }
            }
        }

        // =========================================================================
        // SECTION 1: TEACHER REGISTRY & UNIQUE PASSKEY ISSUANCE (CRITICAL FOR PRIVACY)
        // =========================================================================
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Emerald500.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.VpnKey,
                                        contentDescription = null,
                                        tint = Emerald400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Teacher Registry & Passkey Issuance",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Only Admin can add teachers & issue passkeys",
                                    fontSize = 11.sp,
                                    color = Emerald400
                                )
                            }
                        }

                        Button(
                            onClick = { showAddTeacherDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_add_teacher_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Teacher", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "For strict privacy, each teacher must input their unique admin-assigned passkey to enter the Teacher Portal. Unauthorized users cannot enter.",
                        fontSize = 12.sp,
                        color = Slate400,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (teacherAccounts.isEmpty()) {
                        Text(
                            text = "No teachers registered yet. Tap '+ Add Teacher' above.",
                            fontSize = 12.sp,
                            color = Slate400,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        teacherAccounts.forEach { teacher ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Slate900,
                                border = BorderStroke(1.dp, DarkBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = teacher.fullName,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Slate100
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = Emerald500.copy(alpha = 0.2f)
                                                ) {
                                                    Text(
                                                        text = teacher.assignedClass,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Emerald400,
                                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "${teacher.subjectSpecialization} • Staff ID: ${teacher.staffId}",
                                                fontSize = 11.sp,
                                                color = Slate400
                                            )
                                            Text(
                                                text = "Email: ${teacher.email} • Tel: ${teacher.phone}",
                                                fontSize = 10.sp,
                                                color = Slate500
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.deleteTeacher(teacher.id)
                                                Toast.makeText(context, "Teacher ${teacher.fullName} removed.", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Teacher",
                                                tint = Rose400.copy(alpha = 0.8f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Divider(color = DarkBorderSubtle.copy(alpha = 0.5f))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Passkey Display Row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(DarkCanvas, RoundedCornerShape(8.dp))
                                            .border(BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Key,
                                                contentDescription = null,
                                                tint = Amber400,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Passkey:",
                                                fontSize = 11.sp,
                                                color = Slate400
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = teacher.passkey,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Amber400,
                                                letterSpacing = 1.sp
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = { copyToClipboard("Teacher Passkey", teacher.passkey) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Passkey",
                                                    tint = Slate300,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))

                                            IconButton(
                                                onClick = { showEditPasskeyDialogForTeacher = teacher },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Passkey",
                                                    tint = Indigo400,
                                                    modifier = Modifier.size(14.dp)
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
        }

        // =========================================================================
        // SECTION 2: STUDENT & PARENT ID REGISTRY (AUTHENTICATION IDENTIFIERS)
        // =========================================================================
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Indigo500.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = Indigo400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Student & Parent ID Registry",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Official Student IDs for Student & Parent Login",
                                    fontSize = 11.sp,
                                    color = Indigo400
                                )
                            }
                        }

                        Button(
                            onClick = { showAddStudentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_add_student_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Slate100, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Enroll Student", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Students and parents use the official admin-assigned Student ID (e.g. GRS/2024/0428) to enter their respective portals.",
                        fontSize = 12.sp,
                        color = Slate400,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (studentRecords.isEmpty()) {
                        Text(
                            text = "No students registered yet.",
                            fontSize = 12.sp,
                            color = Slate400,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        studentRecords.forEach { student ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Slate900,
                                border = BorderStroke(1.dp, DarkBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.openStudentProfile(student) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = student.fullName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate100
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Indigo500.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = student.assignedClass,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Indigo400,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Student ID: ",
                                                fontSize = 11.sp,
                                                color = Slate400
                                            )
                                            Text(
                                                text = student.studentId,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Amber400
                                            )
                                            IconButton(
                                                onClick = { copyToClipboard("Student ID", student.studentId) },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "Copy Student ID",
                                                    tint = Slate400,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = "Parent: ${student.parentName} (${student.parentPhone})",
                                            fontSize = 10.sp,
                                            color = Slate500
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { viewModel.openStudentProfile(student) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = "View Profile Dossier",
                                                tint = Amber400,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        IconButton(
                                            onClick = {
                                                viewModel.deleteStudent(student.id)
                                                Toast.makeText(context, "Student ${student.fullName} deleted.", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Student",
                                                tint = Rose400.copy(alpha = 0.8f),
                                                modifier = Modifier.size(16.dp)
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
        // SECTION 3: MASTER ADMIN PASSKEY SECURITY
        // =========================================================================
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Rose500.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = Rose400,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Master Admin Passkey Security",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Protects the Super Admin Console",
                                    fontSize = 11.sp,
                                    color = Rose400
                                )
                            }
                        }

                        Button(
                            onClick = { showChangeAdminPasskeyDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_change_passkey_button")
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = Slate100, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Change Key", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate900, RoundedCornerShape(10.dp))
                            .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Current Admin Security Passkey:",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                            Text(
                                text = adminSecurityConfig?.adminPasskey ?: "GRS-ADMIN-2025",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Rose400,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = { copyToClipboard("Admin Passkey", adminSecurityConfig?.adminPasskey ?: "GRS-ADMIN-2025") },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Admin Key",
                                tint = Slate300,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section: Report Card Approvals
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Indigo400,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Term Report Card Approval",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Once approved, report cards and subject grades become visible across all Student and Parent portals with official principal endorsement.",
                        fontSize = 12.sp,
                        color = Slate400,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate900, RoundedCornerShape(10.dp))
                            .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "2nd Term 2024/2025 Reports",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = if (isReportApproved) "Approved by Admin • Published to Parents" else "Awaiting Principal Verification",
                                fontSize = 11.sp,
                                color = if (isReportApproved) Emerald400 else Amber400
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.setReportCardApproval(!isReportApproved, !isReportApproved)
                                Toast.makeText(
                                    context,
                                    if (!isReportApproved) "All student report cards approved & published!" else "Report cards reverted to draft mode.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isReportApproved) Emerald500 else Rose500
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_toggle_report_approval_button")
                        ) {
                            Text(
                                text = if (isReportApproved) "Published (Revoke)" else "Approve & Publish",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }
                    }
                }
            }
        }

        // Section: Fee Billing & Finance Control
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = Amber400,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Fee Billing Control (Parent Portal)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }

                        Button(
                            onClick = { showCreateFeeDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("admin_create_bill_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = DarkCanvas, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Create Bill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Bills created here are strictly displayed on the Parent Portal only (hidden from students).",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    feeItems.take(3).forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .background(Slate900, RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                Text("${item.targetClass} • Due: ${item.dueDate}", fontSize = 11.sp, color = Slate400)
                            }
                            Text(
                                "₦${String.format("%,d", item.amount.toInt())}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400
                            )
                        }
                    }
                }
            }
        }

        // Section: Staff Attendance & Clock-In Log
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = Emerald400,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Faculty Clock-In & Time Tracker",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (staffClockRecords.isEmpty()) {
                        Text("No faculty clock-in records yet today.", fontSize = 12.sp, color = Slate400)
                    } else {
                        staffClockRecords.forEach { record ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Slate900, RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(record.staffName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                    Text("In: ${record.clockInTime} • ${record.date}", fontSize = 11.sp, color = Slate400)
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (record.clockOutTime != null) Slate700 else Emerald500.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, if (record.clockOutTime != null) Slate500 else Emerald400)
                                ) {
                                    Text(
                                        text = if (record.clockOutTime != null) "Out: ${record.clockOutTime}" else "On Duty",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (record.clockOutTime != null) Slate300 else Emerald400,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: CBT Super Dashboard & Moderation Quick Links
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quick Management Tools",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppDestination.CBT_STUDIO) },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_goto_cbt_studio_button")
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("CBT Center", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(AppDestination.GROUP_CHAT) },
                            colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                            border = BorderStroke(1.dp, DarkBorder),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_goto_chat_moderation_button")
                        ) {
                            Icon(Icons.Default.Forum, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Moderate Chats", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { showBroadcastDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Post Official School Broadcast", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog: Create Fee Bill
    if (showCreateFeeDialog) {
        var billTitle by remember { mutableStateOf("") }
        var billAmount by remember { mutableStateOf("") }
        var billClass by remember { mutableStateOf("All Classes (JSS 1 - SS 3)") }
        var billDueDate by remember { mutableStateOf("15 Apr 2025") }
        var billCategory by remember { mutableStateOf("Tuition") }

        AlertDialog(
            onDismissRequest = { showCreateFeeDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Create Fee Bill (Parent Portal)", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = billTitle,
                        onValueChange = { billTitle = it },
                        label = { Text("Bill Description") },
                        placeholder = { Text("e.g., 3rd Term Tuition & Development Fee") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = billAmount,
                        onValueChange = { billAmount = it },
                        label = { Text("Amount (₦)") },
                        placeholder = { Text("e.g., 185000") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = billClass,
                        onValueChange = { billClass = it },
                        label = { Text("Target Class") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = billDueDate,
                        onValueChange = { billDueDate = it },
                        label = { Text("Due Date") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = billAmount.toDoubleOrNull() ?: 50000.0
                        if (billTitle.isNotBlank()) {
                            viewModel.createFeeBill(
                                title = billTitle,
                                term = "2nd Term",
                                targetClass = billClass,
                                amount = amount,
                                dueDate = billDueDate,
                                category = billCategory
                            )
                            showCreateFeeDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Text("Publish Bill to Parents", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateFeeDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // Dialog: Broadcast Announcement
    if (showBroadcastDialog) {
        var broadcastTitle by remember { mutableStateOf("") }
        var broadcastSummary by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Post School-Wide Announcement", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = broadcastTitle,
                        onValueChange = { broadcastTitle = it },
                        label = { Text("Title") },
                        placeholder = { Text("e.g., Resumption Notice for Mid-Term") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = broadcastSummary,
                        onValueChange = { broadcastSummary = it },
                        label = { Text("Announcement Details") },
                        minLines = 3,
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (broadcastTitle.isNotBlank()) {
                            viewModel.publishAnnouncement(
                                title = broadcastTitle,
                                category = "Administrative",
                                summary = broadcastSummary,
                                targetAudience = "All"
                            )
                            showBroadcastDialog = false
                            Toast.makeText(context, "Announcement broadcasted!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Broadcast", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showBroadcastDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: ADD NEW TEACHER & GENERATE PASSKEY
    // =========================================================================
    if (showAddTeacherDialog) {
        var teacherName by remember { mutableStateOf("") }
        var teacherEmail by remember { mutableStateOf("") }
        var teacherPhone by remember { mutableStateOf("+234 816 620 5113") }
        var teacherClass by remember { mutableStateOf("SS 1 Science") }
        var teacherSubject by remember { mutableStateOf("Physics & Mathematics") }
        var teacherPasskey by remember { mutableStateOf("TCH-${(1000..9999).random()}-2025") }

        AlertDialog(
            onDismissRequest = { showAddTeacherDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Emerald400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Teacher & Issue Passkey", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "The teacher will use this unique passkey to login into their portal.",
                        fontSize = 11.sp,
                        color = Emerald400
                    )

                    OutlinedTextField(
                        value = teacherName,
                        onValueChange = { teacherName = it },
                        label = { Text("Teacher Full Name *") },
                        placeholder = { Text("e.g. Mr. Kolawole Adeyemi") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = teacherSubject,
                        onValueChange = { teacherSubject = it },
                        label = { Text("Subject Specialization") },
                        placeholder = { Text("e.g. Chemistry & Biology") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = teacherClass,
                        onValueChange = { teacherClass = it },
                        label = { Text("Assigned Class") },
                        placeholder = { Text("e.g. SS 2 Commercial") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = teacherPhone,
                        onValueChange = { teacherPhone = it },
                        label = { Text("Phone Number") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = teacherEmail,
                        onValueChange = { teacherEmail = it },
                        label = { Text("Email (Optional)") },
                        placeholder = { Text("teacher@grazielroyalschools.edu.ng") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Passkey field with Generator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = teacherPasskey,
                            onValueChange = { teacherPasskey = it },
                            label = { Text("Unique Passkey *") },
                            colors = customFieldColors(),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                val prefix = if (teacherName.isNotBlank()) teacherName.take(3).uppercase() else "TCH"
                                teacherPasskey = "$prefix-${(1000..9999).random()}-2025"
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Regenerate Passkey", tint = Emerald400)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (teacherName.isNotBlank() && teacherPasskey.isNotBlank()) {
                            viewModel.addNewTeacher(
                                fullName = teacherName,
                                email = teacherEmail,
                                phone = teacherPhone,
                                assignedClass = teacherClass,
                                subject = teacherSubject,
                                passkey = teacherPasskey
                            ) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                if (success) {
                                    showAddTeacherDialog = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Teacher Name and Passkey are required.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                ) {
                    Text("Add & Issue Key", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddTeacherDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: EDIT TEACHER PASSKEY
    // =========================================================================
    showEditPasskeyDialogForTeacher?.let { teacher ->
        var newPasskey by remember { mutableStateOf(teacher.passkey) }

        AlertDialog(
            onDismissRequest = { showEditPasskeyDialogForTeacher = null },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Edit Passkey for ${teacher.fullName}", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 15.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Update the secret passkey for this teacher.", fontSize = 12.sp, color = Slate400)
                    OutlinedTextField(
                        value = newPasskey,
                        onValueChange = { newPasskey = it },
                        label = { Text("Teacher Passkey") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPasskey.isNotBlank()) {
                            viewModel.updateTeacherPasskey(teacher.id, newPasskey)
                            Toast.makeText(context, "Passkey updated to: $newPasskey", Toast.LENGTH_SHORT).show()
                            showEditPasskeyDialogForTeacher = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Save Passkey", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditPasskeyDialogForTeacher = null }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: ENROLL STUDENT & ASSIGN OFFICIAL STUDENT ID
    // =========================================================================
    if (showAddStudentDialog) {
        var studentName by remember { mutableStateOf("") }
        var studentId by remember { mutableStateOf("GRS/2025/${(1000..9999).random()}") }
        var studentClass by remember { mutableStateOf("SS 1 Science") }
        var parentName by remember { mutableStateOf("") }
        var parentPhone by remember { mutableStateOf("+234 816 620 5113") }
        var parentEmail by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Indigo400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enroll Student to Registry", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Both the student and parent will use this Student ID to enter their portals.",
                        fontSize = 11.sp,
                        color = Indigo400
                    )

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Full Name *") },
                        placeholder = { Text("e.g. Adeleke David Oluwaseun") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = { Text("Official Student ID *") },
                        placeholder = { Text("GRS/2025/0428") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = studentClass,
                        onValueChange = { studentClass = it },
                        label = { Text("Class") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = parentName,
                        onValueChange = { parentName = it },
                        label = { Text("Parent / Guardian Name") },
                        placeholder = { Text("Mr. & Mrs. Adeleke") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = parentPhone,
                        onValueChange = { parentPhone = it },
                        label = { Text("Parent Phone") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentName.isNotBlank() && studentId.isNotBlank()) {
                            viewModel.addNewStudent(
                                fullName = studentName,
                                studentId = studentId,
                                assignedClass = studentClass,
                                parentName = parentName,
                                parentPhone = parentPhone,
                                parentEmail = parentEmail
                            ) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                if (success) {
                                    showAddStudentDialog = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Student Name and Student ID are required.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Enroll Student", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddStudentDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: CHANGE MASTER ADMIN PASSKEY
    // =========================================================================
    if (showChangeAdminPasskeyDialog) {
        var newAdminKey by remember { mutableStateOf(adminSecurityConfig?.adminPasskey ?: "GRS-ADMIN-2025") }

        AlertDialog(
            onDismissRequest = { showChangeAdminPasskeyDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Rose400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Super Admin Passkey", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Enter a new secure Master Passkey for the School Proprietor / Super Admin.",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                    OutlinedTextField(
                        value = newAdminKey,
                        onValueChange = { newAdminKey = it },
                        label = { Text("New Admin Passkey") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newAdminKey.length >= 4) {
                            viewModel.updateAdminSecurityPasskey(newAdminKey) { success ->
                                if (success) {
                                    Toast.makeText(context, "Admin Passkey updated successfully!", Toast.LENGTH_SHORT).show()
                                    showChangeAdminPasskeyDialog = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Passkey must be at least 4 characters long.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                ) {
                    Text("Update Passkey", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showChangeAdminPasskeyDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: UPDATE ACADEMIC TERM & SESSION YEAR
    // =========================================================================
    if (showUpdateAcademicDialog) {
        var selectedTermOption by remember { mutableStateOf(adminSecurityConfig?.activeTerm ?: "2nd Term") }
        var sessionYearText by remember { mutableStateOf(adminSecurityConfig?.activeSession ?: "2024/2025") }

        AlertDialog(
            onDismissRequest = { showUpdateAcademicDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Amber400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Term & Session Year", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Change the active academic term and calendar session year across all teacher reports, student portal, and billing modules.",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    Text("Select Academic Term:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate300)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("1st Term", "2nd Term", "3rd Term").forEach { term ->
                            val isSelected = selectedTermOption == term
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Amber500 else Slate900,
                                border = BorderStroke(1.dp, if (isSelected) Amber400 else DarkBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedTermOption = term }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = term,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) DarkCanvas else Slate300
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = sessionYearText,
                        onValueChange = { sessionYearText = it },
                        label = { Text("Academic Session Year") },
                        placeholder = { Text("e.g., 2024/2025, 2025/2026") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("2024/2025", "2025/2026", "2026/2027").forEach { quickSession ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Slate900,
                                border = BorderStroke(1.dp, DarkBorderSubtle),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { sessionYearText = quickSession }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = quickSession,
                                        fontSize = 10.sp,
                                        color = if (sessionYearText == quickSession) Amber400 else Slate400,
                                        fontWeight = if (sessionYearText == quickSession) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedTermOption.isNotBlank() && sessionYearText.isNotBlank()) {
                            viewModel.updateAcademicSessionAndTerm(selectedTermOption, sessionYearText) { success ->
                                if (success) {
                                    Toast.makeText(context, "Academic Calendar updated to $selectedTermOption $sessionYearText", Toast.LENGTH_LONG).show()
                                    showUpdateAcademicDialog = false
                                } else {
                                    Toast.makeText(context, "Failed to update academic term.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber500)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold, color = DarkCanvas)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showUpdateAcademicDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }

    // =========================================================================
    // DIALOG: UPDATE SCHOOL PAYMENT BANK ACCOUNT
    // =========================================================================
    if (showUpdateBankDialog) {
        var bankNameInput by remember { mutableStateOf(adminSecurityConfig?.bankName ?: "Monie Point") }
        var bankAccountNumInput by remember { mutableStateOf(adminSecurityConfig?.bankAccountNumber ?: "5255883539") }
        var bankAccountNameInput by remember { mutableStateOf(adminSecurityConfig?.bankAccountName ?: "Graziel Royal Schools Ltd.") }

        AlertDialog(
            onDismissRequest = { showUpdateBankDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Indigo400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update School Bank Account", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Update the official account information presented to parents for school fee payments and bank transfers.",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    OutlinedTextField(
                        value = bankNameInput,
                        onValueChange = { bankNameInput = it },
                        label = { Text("Bank Name") },
                        placeholder = { Text("Monie Point / Moniepoint MFB") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bankAccountNumInput,
                        onValueChange = { bankAccountNumInput = it },
                        label = { Text("Account Number") },
                        placeholder = { Text("5255883539") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bankAccountNameInput,
                        onValueChange = { bankAccountNameInput = it },
                        label = { Text("Account Name") },
                        placeholder = { Text("Graziel Royal Schools Ltd.") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (bankNameInput.isNotBlank() && bankAccountNumInput.isNotBlank() && bankAccountNameInput.isNotBlank()) {
                            viewModel.updateSchoolBankDetails(
                                bankName = bankNameInput,
                                accountNumber = bankAccountNumInput,
                                accountName = bankAccountNameInput
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "School Bank Account details updated successfully!", Toast.LENGTH_SHORT).show()
                                    showUpdateBankDialog = false
                                } else {
                                    Toast.makeText(context, "Failed to update bank details.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill in all bank details", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Text("Save Bank Info", fontWeight = FontWeight.Bold, color = Slate100)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showUpdateBankDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = label, fontSize = 11.sp, color = Slate400)
    }
}

@Composable
private fun customFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = DarkCanvas,
    unfocusedContainerColor = DarkCanvas,
    focusedIndicatorColor = Indigo400,
    unfocusedIndicatorColor = DarkBorder,
    focusedLabelColor = Indigo400,
    unfocusedLabelColor = Slate400,
    focusedTextColor = Slate100,
    unfocusedTextColor = Slate200
)
