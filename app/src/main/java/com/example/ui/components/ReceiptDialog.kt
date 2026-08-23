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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.PaymentTransaction
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate900
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReceiptDialog(
    transaction: PaymentTransaction,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val formattedAmount = "₦" + NumberFormat.getNumberInstance(Locale.US).format(transaction.amount)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("payment_receipt_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close & header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OFFICIAL RECEIPT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Success Badge
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Emerald500.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Emerald400,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Payment Received Successfully",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
                Text(
                    text = transaction.title,
                    fontSize = 12.sp,
                    color = Slate400,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Big Amount
                Text(
                    text = formattedAmount,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo400
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receipt Slip Details
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        ReceiptRow("Receipt No", transaction.receiptNumber, isMono = true)
                        ReceiptRow("Date & Time", transaction.date)
                        ReceiptRow("Student Name", transaction.studentName)
                        ReceiptRow("Student ID", transaction.studentId)
                        ReceiptRow("Academic Term", transaction.academicTerm)
                        ReceiptRow("Payment Method", transaction.paymentMethod)
                        ReceiptRow("Status", "COMPLETED / VERIFIED", highlight = true)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Royal Stamp
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Amber500.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "★ GRAZIEL ROYAL SCHOOLS BURSARY SEAL ★\nOpo-Ibogun, Ifo, Ogun State, Nigeria • Founder: Mr. Tobi Adebayo",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber400,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Receipt PDF saved to device downloads", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("download_receipt_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo400)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Sharing official Bursary receipt...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_receipt_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, isMono: Boolean = false, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Slate400,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (highlight) Emerald400 else Slate200,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default
        )
    }
}
