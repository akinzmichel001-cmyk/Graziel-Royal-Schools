package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeMute
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CbtTest
import com.example.data.model.FeeItem
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

    var showCreateFeeDialog by remember { mutableStateOf(false) }
    var showBroadcastDialog by remember { mutableStateOf(false) }

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
                        StatPill("Active Staff", "${staffClockRecords.size + 4}", Emerald400)
                        StatPill("CBT Tests", "${cbtTests.size}", Indigo400)
                        StatPill("Fee Items", "${feeItems.size}", Amber400)
                        StatPill("Approval", if (isReportApproved) "Ready" else "Pending", if (isReportApproved) Emerald400 else Rose400)
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
