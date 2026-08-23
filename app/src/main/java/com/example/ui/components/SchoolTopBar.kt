package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun SchoolTopBar(
    currentRole: UserRole,
    onRoleChange: (UserRole) -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var roleMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkCanvas)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & School Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Indigo600,
                        border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.3f)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = "Graziel Royal Schools Logo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "GRAZIEL ROYAL",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PORTAL",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo400,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Excellence, Integrity & Discipline",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = Slate400,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Notification & Role Selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Slate800,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier
                            .testTag("top_bar_notifications_button")
                            .size(38.dp)
                            .clickable { onNotificationClick() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Amber500,
                                        contentColor = DarkCanvas
                                    ) {
                                        Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "School Notifications",
                                    tint = Slate300,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Role Chip with Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Slate800,
                            border = BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier
                                .testTag("role_switcher_chip")
                                .clickable { roleMenuExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val (roleLabel, roleColor) = when (currentRole) {
                                    UserRole.ADMIN -> "Admin" to Rose400
                                    UserRole.TEACHER -> "Teacher" to Emerald400
                                    UserRole.STUDENT -> "Student" to Indigo400
                                    UserRole.PARENT -> "Parent" to Amber400
                                    else -> "Student" to Indigo400
                                }
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(roleColor)
                                    )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = roleLabel,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Slate200
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Switch Role",
                                    tint = Slate400,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = roleMenuExpanded,
                            onDismissRequest = { roleMenuExpanded = false },
                            modifier = Modifier.background(DarkCardSurfaceElevated)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Super Admin Portal", color = Slate100, fontSize = 13.sp)
                                        if (currentRole == UserRole.ADMIN) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Rose400, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                onClick = {
                                    onRoleChange(UserRole.ADMIN)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Teacher Workspace", color = Slate100, fontSize = 13.sp)
                                        if (currentRole == UserRole.TEACHER) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                onClick = {
                                    onRoleChange(UserRole.TEACHER)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Student Portal", color = Slate100, fontSize = 13.sp)
                                        if (currentRole == UserRole.STUDENT) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                onClick = {
                                    onRoleChange(UserRole.STUDENT)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Parent Portal", color = Slate100, fontSize = 13.sp)
                                        if (currentRole == UserRole.PARENT) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                onClick = {
                                    onRoleChange(UserRole.PARENT)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Logout, contentDescription = null, tint = Rose400, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Sign Out / Switch User", color = Rose400, fontSize = 13.sp)
                                    }
                                },
                                onClick = {
                                    roleMenuExpanded = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate900, RoundedCornerShape(10.dp))
                    .border(BorderStroke(1.dp, DarkBorder), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Indigo400,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "2024/2025 Session • 2nd Term",
                        fontSize = 11.sp,
                        color = Slate300,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "Opo-Ibogun, Ogun State",
                    fontSize = 11.sp,
                    color = Amber400,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
