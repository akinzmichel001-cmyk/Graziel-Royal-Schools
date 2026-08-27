package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.StudentRecord
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
import com.example.ui.theme.Indigo900
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
 * Material 3 User Profile View for the Dashboard
 * Uses the custom Avatar component with interactive quick profile preview and essential student attributes.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileDashboardCard(
    studentName: String = "Adeleke David O.",
    studentId: String = "GRS/2024/0428",
    assignedClass: String = "SS 1 Science",
    houseName: String = "Royal Blue House",
    academicStatus: String = "Active Scholar • Honors",
    gpa: String = "4.85 / 5.0",
    attendanceRate: String = "98.2%",
    classPosition: String = "1st of 34",
    gender: String = "Male",
    dob: String = "14 May 2008",
    bloodGroup: String = "O+",
    parentName: String = "Chief Adeleke B. A.",
    emergencyContact: String = "+234 816 620 5113",
    clubAffiliation: String = "STEM & Robotics (President)",
    onViewFullProfile: () -> Unit = {},
    onViewIdCard: () -> Unit = {},
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    onOpenSettings: (() -> Unit)? = null,
    gradesNotificationEnabled: Boolean = true,
    announcementsNotificationEnabled: Boolean = true,
    assignmentsNotificationEnabled: Boolean = true,
    onToggleGrades: ((Boolean) -> Unit)? = null,
    onToggleAnnouncements: ((Boolean) -> Unit)? = null,
    onToggleAssignments: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isDetailsExpanded by rememberSaveable { mutableStateOf(false) }
    var showNotificationPreferencesModal by remember { mutableStateOf(false) }

    var localGradesEnabled by remember { mutableStateOf(gradesNotificationEnabled) }
    var localAnnouncementsEnabled by remember { mutableStateOf(announcementsNotificationEnabled) }
    var localAssignmentsEnabled by remember { mutableStateOf(assignmentsNotificationEnabled) }

    val infiniteTransition = rememberInfiniteTransition(label = "user_card_refresh_spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "user_card_spin"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_profile_dashboard_card")
            .animateContentSize(animationSpec = tween(300)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Indigo900.copy(alpha = 0.5f),
                            DarkCardSurfaceElevated,
                            DarkCardSurface
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Header Row with Avatar and Primary Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Avatar Component with custom ring and Scholar star badge
                        Avatar(
                            name = studentName,
                            imageRes = R.drawable.img_student_avatar,
                            size = 60.dp,
                            shape = CircleShape,
                            ringColor = Brush.linearGradient(listOf(Amber400, Indigo400)),
                            ringWidth = 2.5.dp,
                            badgeType = AvatarBadgeType.SCHOLAR_STAR,
                            onClick = onViewFullProfile
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = studentName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = Indigo400,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "$assignedClass • $studentId",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate300
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = houseName,
                                fontSize = 11.sp,
                                color = Indigo400
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (onRefresh != null) {
                            Surface(
                                shape = CircleShape,
                                color = DarkCanvas.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, DarkBorderSubtle),
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { onRefresh() }
                                    .testTag("btn_sync_user_profile")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Sync profile & assignments",
                                        tint = if (isRefreshing) Indigo400 else Slate400,
                                        modifier = Modifier
                                            .size(15.dp)
                                            .then(
                                                if (isRefreshing) Modifier.rotate(spinAngle) else Modifier
                                            )
                                    )
                                }
                            }
                        }

                        // Notification Preferences Settings Icon in Profile Header
                        Surface(
                            shape = CircleShape,
                            color = DarkCanvas.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    if (onOpenSettings != null) {
                                        onOpenSettings()
                                    } else {
                                        showNotificationPreferencesModal = true
                                    }
                                }
                                .testTag("btn_profile_settings")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Notification Settings",
                                    tint = Indigo400,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        // Status Pill
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Amber500.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { onViewFullProfile() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = Amber400,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Scholar",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Amber400
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // High-Density Metrics Bar
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCanvas.copy(alpha = 0.65f),
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileMetricItem(
                            label = "GPA",
                            value = gpa.take(4),
                            subtext = "Distinction",
                            valueColor = Emerald400
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(26.dp)
                                .background(DarkBorder)
                        )

                        ProfileMetricItem(
                            label = "POSITION",
                            value = classPosition.split(" ").firstOrNull() ?: "1st",
                            subtext = "of 34 Students",
                            valueColor = Amber400
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(26.dp)
                                .background(DarkBorder)
                        )

                        ProfileMetricItem(
                            label = "ATTENDANCE",
                            value = attendanceRate,
                            subtext = "On Track",
                            valueColor = Slate100
                        )
                    }
                }

                // Expandable Details Section
                AnimatedVisibility(
                    visible = isDetailsExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Divider(
                            color = DarkBorderSubtle,
                            thickness = 1.dp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Student Attributes Flow / Grid
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailChip(icon = Icons.Default.Person, label = "Gender: $gender")
                            DetailChip(icon = Icons.Default.DateRange, label = "DOB: $dob")
                            DetailChip(icon = Icons.Default.LocalHospital, label = "Blood Group: $bloodGroup")
                            DetailChip(icon = Icons.Default.Groups, label = "Club: $clubAffiliation")
                            DetailChip(icon = Icons.Default.Shield, label = "Guardian: $parentName")
                            DetailChip(icon = Icons.Default.Call, label = "Emergency: $emergencyContact")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action & Expand Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Expand/Collapse Toggle Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { isDetailsExpanded = !isDetailsExpanded }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isDetailsExpanded) "Less details" else "More student info",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Indigo400
                        )
                        Icon(
                            imageVector = if (isDetailsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Action buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = onViewIdCard,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, DarkBorder),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("btn_dashboard_id_card")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = Slate300,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Digital ID", fontSize = 11.sp, color = Slate200)
                        }

                        Button(
                            onClick = onViewFullProfile,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("btn_dashboard_full_profile")
                        ) {
                            Text("Full Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showNotificationPreferencesModal) {
        NotificationPreferencesModal(
            gradesEnabled = localGradesEnabled,
            announcementsEnabled = localAnnouncementsEnabled,
            assignmentsEnabled = localAssignmentsEnabled,
            onToggleGrades = { enabled ->
                localGradesEnabled = enabled
                onToggleGrades?.invoke(enabled)
            },
            onToggleAnnouncements = { enabled ->
                localAnnouncementsEnabled = enabled
                onToggleAnnouncements?.invoke(enabled)
            },
            onToggleAssignments = { enabled ->
                localAssignmentsEnabled = enabled
                onToggleAssignments?.invoke(enabled)
            },
            studentName = studentName,
            studentClass = assignedClass,
            onDismiss = { showNotificationPreferencesModal = false }
        )
    }
}

@Composable
private fun ProfileMetricItem(
    label: String,
    value: String,
    subtext: String,
    valueColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Slate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
        Text(
            text = subtext,
            fontSize = 9.sp,
            color = Slate400
        )
    }
}

@Composable
private fun DetailChip(
    icon: ImageVector,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkCanvas.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, DarkBorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Indigo400,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = Slate300,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
