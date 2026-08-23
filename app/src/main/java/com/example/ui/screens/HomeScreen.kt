package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.Announcement
import com.example.ui.components.StudentIdCard
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue400
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
import com.example.ui.theme.Purple400
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun HomeScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    val pendingAssignmentsCount = assignments.count { !it.isSubmitted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Campus Banner
        item {
            HeroCampusSection(
                onExploreAdmissions = { viewModel.navigateTo(AppDestination.ADMISSIONS) },
                onLaunchAi = { viewModel.navigateTo(AppDestination.AI_TUTOR) }
            )
        }

        // Quick Action Grid
        item {
            QuickActionsSection(
                onNavigate = { viewModel.navigateTo(it) }
            )
        }

        // Student ID Pass
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STUDENT IDENTITY PASS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.8.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                StudentIdCard()
            }
        }

        // Academic & Attendance KPI Highlights
        item {
            AcademicHighlightCards(
                pendingTasks = pendingAssignmentsCount,
                onViewResults = { viewModel.navigateTo(AppDestination.ACADEMICS) },
                onViewFees = { viewModel.navigateTo(AppDestination.FINANCE) }
            )
        }

        // School Life & Cultural Spotlight
        item {
            SchoolLifeSpotlight(
                onLearnMore = { viewModel.navigateTo(AppDestination.ADMISSIONS) }
            )
        }

        // Notice Board / Announcements
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OFFICIAL SCHOOL BULLETIN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate300,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Indigo500.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.25f))
                    ) {
                        Text(
                            text = "${announcements.size} Updates",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        items(announcements, key = { it.id }) { item ->
            AnnouncementCard(announcement = item)
        }
    }
}

@Composable
private fun HeroCampusSection(
    onExploreAdmissions: () -> Unit,
    onLaunchAi: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("hero_campus_banner"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            // Campus Image
            Image(
                painter = painterResource(id = R.drawable.img_hero_campus),
                contentDescription = "Graziel Royal Schools Campus",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DarkCanvas.copy(alpha = 0.75f),
                                DarkCanvas.copy(alpha = 0.98f)
                            ),
                            startY = 40f
                        )
                    )
            )

            // Content Overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Amber500,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "★ MOTTO: KNOWLEDGE, SPIRIT AND SERVICE ★",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkCanvas,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Graziel Royal Schools",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100,
                    letterSpacing = 0.3.sp
                )

                Text(
                    text = "Opo-Ibogun, Ifo, Ogun State, Nigeria • Creche, Nursery, Primary & College",
                    fontSize = 11.sp,
                    color = Slate300
                )

                Text(
                    text = "Founder: Mr. Tobi Adebayo • Admin: +234 816 620 5113",
                    fontSize = 10.sp,
                    color = Amber400,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onExploreAdmissions,
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(36.dp)
                            .testTag("hero_admissions_button")
                    ) {
                        Icon(Icons.Default.HowToReg, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("2025/2026 Admissions", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate800,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier
                            .weight(0.8f)
                            .height(36.dp)
                            .clickable { onLaunchAi() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Amber400, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Tutor", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onNavigate: (AppDestination) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text(
            text = "PORTAL QUICK ACCESS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Slate400,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionButton(
                title = "Report Card",
                subtitle = "Results",
                icon = Icons.Default.Grade,
                bgColor = Indigo500.copy(alpha = 0.12f),
                iconColor = Indigo400,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppDestination.ACADEMICS) }
            )

            QuickActionButton(
                title = "School Fees",
                subtitle = "Pay & Receipts",
                icon = Icons.Default.CreditCard,
                bgColor = Amber500.copy(alpha = 0.12f),
                iconColor = Amber400,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppDestination.FINANCE) }
            )

            QuickActionButton(
                title = "Timetable",
                subtitle = "Classes",
                icon = Icons.Default.CalendarMonth,
                bgColor = Emerald500.copy(alpha = 0.12f),
                iconColor = Emerald400,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppDestination.SCHEDULE) }
            )

            QuickActionButton(
                title = "Homework",
                subtitle = "Tasks",
                icon = Icons.Default.Assignment,
                bgColor = Rose500.copy(alpha = 0.12f),
                iconColor = Rose400,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppDestination.HOMEWORK) }
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkCardSurface,
        border = BorderStroke(1.dp, DarkBorder),
        shadowElevation = 0.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100, maxLines = 1)
            Text(text = subtitle, fontSize = 9.sp, color = Slate400, maxLines = 1)
        }
    }
}

@Composable
private fun AcademicHighlightCards(
    pendingTasks: Int,
    onViewResults: () -> Unit,
    onViewFees: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Average score
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onViewResults() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TERM AVERAGE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Emerald400, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("91.2%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Slate100)
                Text("Rank: 2nd of 28 Pupils", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Emerald400)
            }
        }

        // Attendance / Tasks
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onViewFees() },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ATTENDANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate400)
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("95.0%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Slate100)
                Text("38 of 40 Days • On Track", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Slate400)
            }
        }
    }
}

@Composable
private fun SchoolLifeSpotlight(
    onLearnMore: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Amber400)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SPOTLIGHT: YORUBA CULTURAL PROJECT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber400,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Emerald500.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.25f))
                ) {
                    Text("Featured", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald400, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.img_school_life),
                    contentDescription = "Students in lab and cultural activities",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Holistic Learning & Cultural Heritage",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Our scholars actively explore culinary arts with Ofuloju and pounded yam, blending cultural pride, STEM, and practical life skills.",
                        fontSize = 11.sp,
                        color = Slate300,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun AnnouncementCard(
    announcement: Announcement
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("announcement_item_${announcement.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, if (announcement.isPinned) Amber500.copy(alpha = 0.4f) else DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (catBg, catColor) = when (announcement.category) {
                        "Cultural" -> Amber500.copy(alpha = 0.12f) to Amber400
                        "Academic" -> Indigo500.copy(alpha = 0.12f) to Indigo400
                        "Sports" -> Emerald500.copy(alpha = 0.12f) to Emerald400
                        else -> Purple400.copy(alpha = 0.12f) to Purple400
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = catBg,
                        border = BorderStroke(1.dp, catColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = announcement.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = catColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (announcement.isPinned) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pinned",
                            tint = Amber400,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = announcement.date,
                    fontSize = 11.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = announcement.summary,
                fontSize = 12.sp,
                color = Slate300,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "— ${announcement.author}",
                    fontSize = 10.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Slate500
                )
            }
        }
    }
}
