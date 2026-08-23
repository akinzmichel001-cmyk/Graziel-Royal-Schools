package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Slate500
import com.example.ui.viewmodel.AppDestination

@Composable
fun SchoolBottomNav(
    currentDestination: AppDestination,
    currentRole: UserRole,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    if (currentDestination == AppDestination.AUTH) {
        return // Hide bottom bar on auth screen
    }

    NavigationBar(
        modifier = modifier
            .testTag("school_bottom_navigation_bar")
            .border(BorderStroke(1.dp, DarkBorder)),
        containerColor = DarkCanvas,
        tonalElevation = 0.dp
    ) {
        when (currentRole) {
            UserRole.ADMIN -> {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.ADMIN_DASHBOARD,
                    onClick = { onNavigate(AppDestination.ADMIN_DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.ADMIN_DASHBOARD) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
                            contentDescription = "Admin Console",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Console", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_admin_dashboard")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.CBT_STUDIO,
                    onClick = { onNavigate(AppDestination.CBT_STUDIO) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.CBT_STUDIO) Icons.Filled.Timer else Icons.Outlined.Timer,
                            contentDescription = "CBT Studio",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("CBT Center", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_admin_cbt")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.GROUP_CHAT,
                    onClick = { onNavigate(AppDestination.GROUP_CHAT) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.GROUP_CHAT) Icons.Filled.Forum else Icons.Outlined.Forum,
                            contentDescription = "Chat Moderation",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Chats", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_admin_chats")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.FINANCE,
                    onClick = { onNavigate(AppDestination.FINANCE) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.FINANCE) Icons.Filled.CreditCard else Icons.Outlined.CreditCard,
                            contentDescription = "Billing",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Billing", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_admin_billing")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.AI_TUTOR,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "AI Assistant",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("AI Copilot", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_admin_ai")
                )
            }

            UserRole.TEACHER -> {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.TEACHER_PORTAL,
                    onClick = { onNavigate(AppDestination.TEACHER_PORTAL) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = "Workspace",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Workspace", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_teacher_workspace")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.CBT_STUDIO,
                    onClick = { onNavigate(AppDestination.CBT_STUDIO) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "CBT Studio",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("CBT Studio", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_teacher_cbt")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.GROUP_CHAT,
                    onClick = { onNavigate(AppDestination.GROUP_CHAT) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Forum,
                            contentDescription = "Class Chat",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Group Chat", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_teacher_chat")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.SCHEDULE,
                    onClick = { onNavigate(AppDestination.SCHEDULE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Timetable",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Timetable", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_teacher_timetable")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.AI_TUTOR,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "AI Assistant",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("AI Copilot", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_teacher_ai")
                )
            }

            UserRole.STUDENT -> {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.STUDENT_PORTAL,
                    onClick = { onNavigate(AppDestination.STUDENT_PORTAL) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.STUDENT_PORTAL) Icons.Filled.School else Icons.Outlined.School,
                            contentDescription = "Student Portal",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("My Portal", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_student_portal")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.GROUP_CHAT,
                    onClick = { onNavigate(AppDestination.GROUP_CHAT) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.GROUP_CHAT) Icons.Filled.Forum else Icons.Outlined.Forum,
                            contentDescription = "Class Chat",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Class Chat", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_student_chat")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.HOMEWORK,
                    onClick = { onNavigate(AppDestination.HOMEWORK) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.HOMEWORK) Icons.Filled.Assignment else Icons.Outlined.Assignment,
                            contentDescription = "Homework",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Tasks", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_student_homework")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.SCHEDULE,
                    onClick = { onNavigate(AppDestination.SCHEDULE) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.SCHEDULE) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                            contentDescription = "Timetable",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Timetable", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_student_timetable")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.AI_TUTOR,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "AI Tutor",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("AI Tutor", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_student_ai_tutor")
                )
            }

            UserRole.PARENT -> {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.PARENT_PORTAL,
                    onClick = { onNavigate(AppDestination.PARENT_PORTAL) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.PARENT_PORTAL) Icons.Filled.FamilyRestroom else Icons.Outlined.FamilyRestroom,
                            contentDescription = "Parent Portal",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Parent Hub", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_parent_hub")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.FINANCE,
                    onClick = { onNavigate(AppDestination.FINANCE) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.FINANCE) Icons.Filled.CreditCard else Icons.Outlined.CreditCard,
                            contentDescription = "School Fees",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Fees & Bills", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_parent_fees")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.SCHEDULE,
                    onClick = { onNavigate(AppDestination.SCHEDULE) },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination == AppDestination.SCHEDULE) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                            contentDescription = "Timetable",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Timetable", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_parent_timetable")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.AI_TUTOR,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "School AI Assistant",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("School AI", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_parent_ai")
                )
            }

            else -> {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.ADMISSIONS,
                    onClick = { onNavigate(AppDestination.ADMISSIONS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "Admissions",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Admissions", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_guest_admissions")
                )

                NavigationBarItem(
                    selected = currentDestination == AppDestination.AI_TUTOR,
                    onClick = { onNavigate(AppDestination.AI_TUTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "School AI",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Inquiries AI", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = itemColors(),
                    modifier = Modifier.testTag("nav_guest_ai")
                )
            }
        }
    }
}

@Composable
private fun itemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Indigo400,
    selectedTextColor = Indigo400,
    indicatorColor = Indigo500.copy(alpha = 0.15f),
    unselectedIconColor = Slate500,
    unselectedTextColor = Slate500
)
