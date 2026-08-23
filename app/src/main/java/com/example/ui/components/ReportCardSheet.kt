package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.SubjectGrade
import com.example.data.model.TermReport
import com.example.ui.theme.Amber400
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Rose400
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun ReportCardSheet(
    report: TermReport,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .height(720.dp)
                .testTag("official_report_card_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = DarkCardSurface,
            border = BorderStroke(1.dp, DarkBorder),
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OFFICIAL TERMINAL REPORT SHEET",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = DarkBorder)

                // Scrollable Slip Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // School Letterhead
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate900),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "GRAZIEL ROYAL SCHOOLS",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100,
                                letterSpacing = 0.5.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Motto: Knowledge, Spirit and Service",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Amber400,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Opo-Ibogun, Ifo, Ogun State, Nigeria • Founder: Mr. Tobi Adebayo",
                                fontSize = 9.sp,
                                color = Slate400,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Student & Session Details Card
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1.5f)) {
                                    InfoField("Student Name", report.studentName, isBold = true)
                                    InfoField("Admission Reg", report.studentReg, isMono = true)
                                    InfoField("Class / Grade", report.studentClass)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    InfoField("Term & Session", "${report.termName} • 2024/2025")
                                    InfoField("Position", "${report.classPosition} of ${report.classPopulation} Pupils", isHighlight = true)
                                    InfoField("Attendance", "${report.attendanceDays} / ${report.totalDays} Days")
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Divider(color = DarkBorder)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Cumulative Score: ${report.totalObtained} / ${report.totalPossible}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate200
                                )
                                Text(
                                    text = "Term Average: ${report.averageScore}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald400
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subjects Table Header
                    Surface(
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                        color = Indigo900.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Subject", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate100, modifier = Modifier.weight(2.5f))
                            Text("CA (40)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate300, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("Exam (60)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate300, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("Total", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber400, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("Grade", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate100, textAlign = TextAlign.Center, modifier = Modifier.weight(0.9f))
                            Text("Remark", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate300, textAlign = TextAlign.End, modifier = Modifier.weight(1.4f))
                        }
                    }

                    // Subject Rows
                    report.grades.forEachIndexed { index, grade ->
                        val bg = if (index % 2 == 0) DarkCardSurface else Slate900
                        val totalCa = grade.ca1 + grade.ca2 + grade.projectScore

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bg)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = grade.subjectName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate200,
                                modifier = Modifier.weight(2.5f)
                            )
                            Text(
                                text = totalCa.toString(),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Slate400,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = grade.examScore.toString(),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Slate400,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = grade.totalScore.toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Slate100,
                                modifier = Modifier.weight(1f)
                            )
                            GradeBadge(grade.gradeLetter, modifier = Modifier.weight(0.9f))
                            Text(
                                text = grade.remark,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End,
                                color = Indigo400,
                                modifier = Modifier.weight(1.4f)
                            )
                        }
                        Divider(color = DarkBorderSubtle, thickness = 0.5.dp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Remarks Section
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Form Teacher's Remark:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400
                            )
                            Text(
                                text = "\"${report.formTeacherRemark}\"",
                                fontSize = 11.sp,
                                color = Slate300,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Principal's Commendation:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400
                            )
                            Text(
                                text = "\"${report.principalRemark}\"",
                                fontSize = 11.sp,
                                color = Slate300,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Emerald400, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("OFFICIAL SEAL ATTESTED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                }
                                Text("Resumption: 28 April 2025", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Rose400)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Sending report slip to connected printer...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo400)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Print", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Full Official Report PDF downloaded to device storage.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download PDF Slip", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoField(label: String, value: String, isBold: Boolean = false, isMono: Boolean = false, isHighlight: Boolean = false) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(text = "$label: ", fontSize = 10.sp, color = Slate400)
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = if (isBold || isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) Emerald400 else Slate200,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default
        )
    }
}

@Composable
private fun GradeBadge(grade: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (grade) {
        "A1" -> Emerald400.copy(alpha = 0.2f) to Emerald400
        "B2", "B3" -> Amber400.copy(alpha = 0.2f) to Amber400
        else -> Slate800 to Slate300
    }

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = grade,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
