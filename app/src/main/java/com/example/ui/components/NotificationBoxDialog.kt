package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Announcement
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
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun NotificationBoxDialog(
    viewModel: SchoolViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val admissions by viewModel.applications.collectAsStateWithLifecycle()
    val staffClockRecords by viewModel.staffClockRecords.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val adminConfig by viewModel.adminSecurityConfig.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf("All") }
    var showBroadcastComposer by remember { mutableStateOf(false) }

    val activeTerm = adminConfig?.activeTerm ?: "2nd Term"
    val activeSession = adminConfig?.activeSession ?: "2024/2025"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .testTag("notification_box_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Indigo600.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.5f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Amber400,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "NOTIFICATION BOX",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Slate100,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Amber500.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "$activeTerm $activeSession",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber400,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Graziel Royal Schools Official Live Dispatch & System Log",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Slate800, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate300, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Admin Special Broadcast Trigger
                if (currentRole == UserRole.ADMIN) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = Rose400, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Administrator Control", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                    Text("Dispatch notifications & alerts to parents/staff", fontSize = 10.sp, color = Slate400)
                                }
                            }

                            Button(
                                onClick = { showBroadcastComposer = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Rose500),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("admin_compose_broadcast_button")
                            ) {
                                Text("New Broadcast", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("All", "Announcements", "Fee Payments", "Admissions", "Staff Clock-In", "CBT Exams")
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = filter,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Slate900,
                                labelColor = Slate300,
                                selectedContainerColor = Indigo600,
                                selectedLabelColor = Slate100
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == filter,
                                borderColor = DarkBorder,
                                selectedBorderColor = Indigo400
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = DarkBorder)
                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Notifications Feed
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Announcements / Broadcasts
                    if (selectedFilter == "All" || selectedFilter == "Announcements") {
                        items(announcements) { ann ->
                            NotificationAnnouncementItem(ann)
                        }
                    }

                    // Fee Payments (Bursary alerts)
                    if (selectedFilter == "All" || selectedFilter == "Fee Payments") {
                        items(payments) { p ->
                            NotificationPaymentItem(p)
                        }
                    }

                    // Admission alerts
                    if (selectedFilter == "All" || selectedFilter == "Admissions") {
                        items(admissions) { adm ->
                            NotificationAdmissionItem(adm)
                        }
                    }

                    // Staff Clock-ins
                    if (selectedFilter == "All" || selectedFilter == "Staff Clock-In") {
                        items(staffClockRecords) { clock ->
                            NotificationStaffClockItem(clock)
                        }
                    }

                    // CBT Test releases
                    if (selectedFilter == "All" || selectedFilter == "CBT Exams") {
                        items(cbtTests) { test ->
                            NotificationCbtItem(test)
                        }
                    }
                }
            }
        }
    }

    // Broadcast Composer Dialog
    if (showBroadcastComposer) {
        var broadcastTitle by remember { mutableStateOf("") }
        var broadcastSummary by remember { mutableStateOf("") }
        var broadcastAudience by remember { mutableStateOf("All") }
        var broadcastCategory by remember { mutableStateOf("Notice") }
        var isPinned by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showBroadcastComposer = false },
            containerColor = DarkCardSurfaceElevated,
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = Rose400, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Broadcast School Notification", fontWeight = FontWeight.Bold, color = Slate100, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "This alert will be instantly delivered to all selected recipients in the Notification Box.",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    OutlinedTextField(
                        value = broadcastTitle,
                        onValueChange = { broadcastTitle = it },
                        label = { Text("Notification Title") },
                        placeholder = { Text("e.g., Term Calendar / Important Resumption Notice") },
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = broadcastSummary,
                        onValueChange = { broadcastSummary = it },
                        label = { Text("Notification Message / Body") },
                        placeholder = { Text("Enter detailed announcement text...") },
                        minLines = 3,
                        maxLines = 5,
                        colors = customFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Target Audience:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate300)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("All", "Parents", "Students", "Teachers").forEach { aud ->
                            FilterChip(
                                selected = broadcastAudience == aud,
                                onClick = { broadcastAudience = aud },
                                label = { Text(aud, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Slate900,
                                    selectedContainerColor = Indigo600
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (broadcastTitle.isNotBlank() && broadcastSummary.isNotBlank()) {
                            viewModel.postOfficialBroadcast(
                                title = broadcastTitle,
                                summary = broadcastSummary,
                                category = broadcastCategory,
                                targetAudience = broadcastAudience,
                                isPinned = isPinned
                            ) { success ->
                                if (success) {
                                    Toast.makeText(context, "Notification broadcasted successfully!", Toast.LENGTH_SHORT).show()
                                    showBroadcastComposer = false
                                } else {
                                    Toast.makeText(context, "Failed to broadcast notification.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please fill in title and message", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rose500)
                ) {
                    Text("Dispatch Alert", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showBroadcastComposer = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun NotificationAnnouncementItem(ann: Announcement) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, if (ann.isPinned) Amber500.copy(alpha = 0.4f) else DarkBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (ann.isPinned) Amber500.copy(alpha = 0.15f) else Indigo500.copy(alpha = 0.15f),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (ann.isPinned) Icons.Default.PushPin else Icons.Default.Campaign,
                                contentDescription = null,
                                tint = if (ann.isPinned) Amber400 else Indigo400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ann.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Indigo500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = ann.targetAudience,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo400,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = ann.summary,
                fontSize = 11.sp,
                color = Slate300,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "By: ${ann.author}",
                    fontSize = 10.sp,
                    color = Slate500
                )
                Text(
                    text = ann.date,
                    fontSize = 10.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun NotificationPaymentItem(p: com.example.data.model.PaymentTransaction) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = Emerald500.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Fee Payment Verified", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    Text("${p.studentName} (${p.studentId})", fontSize = 11.sp, color = Slate300)
                    Text("Receipt: ${p.receiptNumber} • ${p.paymentMethod}", fontSize = 10.sp, color = Slate400)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₦${String.format("%,d", p.amount.toInt())}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald400
                )
                Text(p.date, fontSize = 9.sp, color = Slate500)
            }
        }
    }
}

@Composable
private fun NotificationAdmissionItem(adm: com.example.data.model.AdmissionApplication) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, DarkBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = CircleShape,
                    color = Amber500.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("New Admission Application", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    Text("${adm.studentName} • Applying for ${adm.classApplyingFor}", fontSize = 11.sp, color = Slate300)
                    Text("Parent: ${adm.parentName} (${adm.parentPhone})", fontSize = 10.sp, color = Slate400)
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (adm.status == "OFFERED" || adm.status == "APPROVED") Emerald500.copy(alpha = 0.2f) else Amber500.copy(alpha = 0.2f)
            ) {
                Text(
                    text = adm.status,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (adm.status == "OFFERED" || adm.status == "APPROVED") Emerald400 else Amber400,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationStaffClockItem(clock: com.example.data.model.StaffClockRecord) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, DarkBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Indigo500.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Staff Attendance Log", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    Text("${clock.staffName} (${clock.staffId})", fontSize = 11.sp, color = Slate300)
                    Text("In: ${clock.clockInTime} • ${clock.date}", fontSize = 10.sp, color = Slate400)
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Emerald500.copy(alpha = 0.2f)
            ) {
                Text(
                    text = clock.status,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald400,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationCbtItem(test: com.example.data.model.CbtTest) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, DarkBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Indigo500.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(test.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    Text("${test.subject} • ${test.targetClass}", fontSize = 11.sp, color = Slate300)
                    Text("Teacher: ${test.createdByTeacher}", fontSize = 10.sp, color = Slate400)
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (test.isLive) Emerald500.copy(alpha = 0.2f) else Slate800
            ) {
                Text(
                    text = if (test.isLive) "LIVE EXAM" else if (test.isResultsPublished) "RESULTS OUT" else "SCHEDULED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (test.isLive) Emerald400 else if (test.isResultsPublished) Amber400 else Slate400,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Indigo400,
    unfocusedBorderColor = DarkBorder,
    focusedLabelColor = Indigo400,
    unfocusedLabelColor = Slate400,
    focusedTextColor = Slate100,
    unfocusedTextColor = Slate200,
    focusedContainerColor = Slate900,
    unfocusedContainerColor = Slate900
)
