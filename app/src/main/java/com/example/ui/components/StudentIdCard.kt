package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Amber400
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceElevated
import com.example.ui.theme.DarkChipBg
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800

@Composable
fun StudentIdCard(
    studentName: String = "David Adeleke O.",
    regNumber: String = "GRS/2024/0428",
    studentClass: String = "Junior Secondary (JSS 2 Royal Gold)",
    house: String = "Sapphire Blue House",
    session: String = "2024/2025 Session • Term 2",
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("student_id_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Indigo900.copy(alpha = 0.65f),
                            DarkCardSurface,
                            DarkCardSurface
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header inside Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Indigo500.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.25f))
                        ) {
                            Text(
                                text = "STUDENT ID • ACTIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo400,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = studentName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = studentClass,
                            fontSize = 12.sp,
                            color = Slate400
                        )
                    }

                    // Student Avatar
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Slate800,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_student_avatar),
                            contentDescription = studentName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.06f)), RoundedCornerShape(14.dp))
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Average", fontSize = 10.sp, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "94.8%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(DarkBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Rank", fontSize = 10.sp, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "1st / 42", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Amber400)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(28.dp)
                            .background(DarkBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Attendance", fontSize = 10.sp, color = Slate400)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "98.5%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate200)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "ID: ", fontSize = 12.sp, color = Slate400)
                        Text(
                            text = regNumber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate200,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isExpanded = !isExpanded }
                    ) {
                        Text(
                            text = if (isExpanded) "Hide details" else "View ID pass",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Indigo400
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Expanded Section
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Divider(color = DarkBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "House & Session:",
                                    fontSize = 11.sp,
                                    color = Slate400,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$house • $session",
                                    fontSize = 12.sp,
                                    color = Slate200,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Valid: July 2025 • Opo-Ibogun Campus, Ogun State",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // QR Simulation
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = "QR Pass",
                                    tint = DarkCanvas,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
