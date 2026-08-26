package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
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
fun ParentPortalScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val feeItems by viewModel.feeItems.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val cbtSubmissions by viewModel.cbtSubmissions.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val isReportApproved by viewModel.isReportCardApproved.collectAsStateWithLifecycle()

    val studentClass = currentUser?.assignedClass ?: "SS 1 Science"
    val parentFeeItems = remember(feeItems, studentClass) {
        feeItems.filter { it.targetClass == "All Classes" || it.targetClass == studentClass }
    }
    val parentAnnouncements = remember(announcements) {
        announcements.filter { it.targetAudience == "All" || it.targetAudience.equals("Parents", ignoreCase = true) }
    }

    val totalOutstanding = parentFeeItems.filter { !it.isPaid }.sumOf { it.amount }
    val totalPaid = parentFeeItems.filter { it.isPaid }.sumOf { it.amount }

    // Published CBT submissions of the child
    val publishedCbtScores = cbtSubmissions.filter { sub ->
        cbtTests.any { it.id == sub.testId && it.isResultsPublished } || sub.isReviewedByTeacher
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Parent & Ward Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
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
                                color = Amber500.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Amber400.copy(alpha = 0.4f)),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FamilyRestroom,
                                        contentDescription = null,
                                        tint = Amber400,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "PARENT PORTAL",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Amber400,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = currentUser?.fullName ?: "Chief & Mrs. Adeleke",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = "Ward: ${currentUser?.childName ?: "Adeleke David Oluwaseun"} (SS 1 Science)",
                                    fontSize = 12.sp,
                                    color = Slate300
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact School on WhatsApp & Phone Hotline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openSchoolWhatsApp(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("parent_contact_whatsapp_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = "WhatsApp",
                                tint = DarkCanvas,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp Admin",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkCanvas
                            )
                        }

                        Button(
                            onClick = { viewModel.callSchoolAdmin(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("parent_call_admin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Call Hotline",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Section: Outstanding School Fees & Bills (Parent Only)
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
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = Amber400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Child's School Fees & Bills", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (totalOutstanding > 0) Rose500.copy(alpha = 0.2f) else Emerald500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (totalOutstanding > 0) "₦${String.format("%,d", totalOutstanding.toInt())} Due" else "All Cleared",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (totalOutstanding > 0) Rose400 else Emerald400,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (parentFeeItems.isEmpty()) {
                        Text(
                            text = "No fee invoices assigned to your ward ($studentClass) at this time.",
                            fontSize = 12.sp,
                            color = Slate400,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        parentFeeItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Slate900, RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                Text("₦${String.format("%,d", item.amount.toInt())} • Due: ${item.dueDate}", fontSize = 11.sp, color = Slate400)
                            }

                            if (item.isPaid) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Emerald500.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Paid", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.selectFeeToPay(item) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("parent_pay_fee_button_${item.id}")
                                ) {
                                    Text("Pay Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                                }
                            }
                        }
                    }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Direct School Bank Account info for transfers
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Amber500.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Amber400, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Monie Point School Account", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Amber400)
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Slate900,
                                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.3f)),
                                    modifier = Modifier.clickable {
                                        clipboardManager.setText(AnnotatedString("5255883539"))
                                        Toast.makeText(context, "Account Number 5255883539 copied!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Amber400, modifier = Modifier.size(10.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copy 5255883539", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Amber400)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Name: Graziel Royal Schools Ltd. • Bank: Monie Point", fontSize = 11.sp, color = Slate300)
                            Text("Account No: 5255883539 (Moniepoint MFB)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate100, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(AppDestination.FINANCE) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Payment Receipts History", fontSize = 12.sp, color = Slate200)
                    }
                }
            }
        }

        // Section: Child's Published CBT Exam Results
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
                            Icon(Icons.Default.Timer, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Child's CBT Online Test Scores", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (publishedCbtScores.isEmpty()) {
                        Text("No released CBT test grades for this term yet.", fontSize = 12.sp, color = Slate400)
                    } else {
                        publishedCbtScores.forEach { sub ->
                            val matchingTest = cbtTests.find { it.id == sub.testId }
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(matchingTest?.title ?: "Assessment Test", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
                                    Text("Teacher Remark: ${sub.teacherFeedback}", fontSize = 11.sp, color = Slate400, maxLines = 1)
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (sub.percentage >= 60) Emerald500.copy(alpha = 0.2f) else Amber500.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${sub.score}/${sub.maxScore} (${String.format("%.0f", sub.percentage)}%)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sub.percentage >= 60) Emerald400 else Amber400,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Term Report Card
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
                            Icon(Icons.Default.Grade, contentDescription = null, tint = Emerald400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Official Approved Report Card", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }

                        Surface(shape = RoundedCornerShape(8.dp), color = Emerald500.copy(alpha = 0.2f)) {
                            Text("Principal Endorsed", color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Access your ward's verified Continuous Assessment (CA), Examination scores, class position, and conduct remarks.",
                        fontSize = 12.sp,
                        color = Slate400,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.setShowReportCardDetail(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("parent_view_report_card_button")
                    ) {
                        Text("View & Print Full Report Card", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: School Announcements
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
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("School Announcements for Parents", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (parentAnnouncements.isEmpty()) {
                        Text("No parent-targeted announcements posted yet.", fontSize = 12.sp, color = Slate400)
                    } else {
                        parentAnnouncements.take(3).forEach { note ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Slate900, RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, DarkBorderSubtle), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(note.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate100)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(note.summary, fontSize = 11.sp, color = Slate300, lineHeight = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${note.date} • ${note.category}", fontSize = 10.sp, color = Indigo400)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
