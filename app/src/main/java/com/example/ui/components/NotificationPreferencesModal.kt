package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

/**
 * Material 3 Notification Preferences Modal for toggling push notification
 * preferences for grades, assessment results, and school announcements.
 */
@Composable
fun NotificationPreferencesModal(
    gradesEnabled: Boolean = true,
    announcementsEnabled: Boolean = true,
    assignmentsEnabled: Boolean = true,
    onToggleGrades: (Boolean) -> Unit,
    onToggleAnnouncements: (Boolean) -> Unit,
    onToggleAssignments: (Boolean) -> Unit = {},
    studentName: String = "Adeleke David O.",
    studentClass: String = "SS 1 Science",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isTestingAlert by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("notification_preferences_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with icon and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Indigo500.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.4f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Indigo400,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Notification Preferences",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "Push notification delivery settings",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_close_notification_preferences")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Student Identity context badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkCanvas.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Amber400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$studentName • $studentClass",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate200
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Emerald500.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Emerald400, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Live Sync",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald400
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // =========================================================================
                // TOGGLE 1: GRADES & ASSESSMENT RESULTS
                // =========================================================================
                NotificationToggleItem(
                    icon = Icons.Default.Grade,
                    iconTint = Amber400,
                    iconBg = Amber500.copy(alpha = 0.15f),
                    title = "Grades & Assessment Results",
                    subtitle = "Get real-time push alerts when test scores, CBT results, and term report cards are approved or published.",
                    isChecked = gradesEnabled,
                    onCheckedChange = { isChecked ->
                        onToggleGrades(isChecked)
                        val msg = if (isChecked) "Grades push notifications enabled" else "Grades push notifications disabled"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    testTag = "switch_grades_notification"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // =========================================================================
                // TOGGLE 2: ANNOUNCEMENTS & OFFICIAL CIRCULARS
                // =========================================================================
                NotificationToggleItem(
                    icon = Icons.Default.Notifications,
                    iconTint = Indigo400,
                    iconBg = Indigo500.copy(alpha = 0.15f),
                    title = "Announcements & School News",
                    subtitle = "Immediate push alerts for principal broadcasts, event reminders, exam timetables, and circulars.",
                    isChecked = announcementsEnabled,
                    onCheckedChange = { isChecked ->
                        onToggleAnnouncements(isChecked)
                        val msg = if (isChecked) "Announcements push notifications enabled" else "Announcements push notifications disabled"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    testTag = "switch_announcements_notification"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // =========================================================================
                // TOGGLE 3: HOMEWORK & ASSIGNMENTS (SECONDARY CONVENIENCE)
                // =========================================================================
                NotificationToggleItem(
                    icon = Icons.Default.Assignment,
                    iconTint = Emerald400,
                    iconBg = Emerald500.copy(alpha = 0.15f),
                    title = "Homework & CBT Deadlines",
                    subtitle = "Reminders for newly posted homework and upcoming submission cut-offs.",
                    isChecked = assignmentsEnabled,
                    onCheckedChange = { isChecked ->
                        onToggleAssignments(isChecked)
                        val msg = if (isChecked) "Assignment alerts enabled" else "Assignment alerts disabled"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    testTag = "switch_assignments_notification"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = DarkBorderSubtle)

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            isTestingAlert = true
                            val activeCategories = buildList {
                                if (gradesEnabled) add("Grades")
                                if (announcementsEnabled) add("Announcements")
                                if (assignmentsEnabled) add("Homework")
                            }
                            val summary = if (activeCategories.isNotEmpty()) {
                                "Test Push: Active for ${activeCategories.joinToString(", ")} 🔔"
                            } else {
                                "All push notifications are currently muted."
                            }
                            Toast.makeText(context, summary, Toast.LENGTH_LONG).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Slate700),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_test_push_notification")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = Slate300,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Test Alert",
                            fontSize = 12.sp,
                            color = Slate200,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(44.dp)
                            .testTag("btn_save_notification_preferences")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Save & Done",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationToggleItem(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCanvas.copy(alpha = 0.55f),
        border = BorderStroke(
            1.dp,
            if (isChecked) iconTint.copy(alpha = 0.35f) else DarkBorderSubtle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = iconBg,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.padding(end = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) Slate100 else Slate400
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (isChecked) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Active",
                                tint = iconTint,
                                modifier = Modifier.size(13.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.NotificationsOff,
                                contentDescription = "Off",
                                tint = Slate500,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Slate400,
                        lineHeight = 15.sp
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Indigo500,
                    uncheckedThumbColor = Slate400,
                    uncheckedTrackColor = Slate800,
                    uncheckedBorderColor = Slate700
                ),
                modifier = Modifier.testTag(testTag)
            )
        }
    }
}
