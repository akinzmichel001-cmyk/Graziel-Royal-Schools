package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeeItem
import com.example.data.model.PaymentTransaction
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
import java.text.NumberFormat
import java.util.Locale

/**
 * Material 3 Fee Payment Summary Card
 * Displays the current outstanding balance, next payment due date,
 * billing progress, and a prominent 'Pay Now' action button.
 */
@Composable
fun FeePaymentSummaryCard(
    feeItems: List<FeeItem>,
    onPayNow: (FeeItem) -> Unit,
    onViewAllFees: () -> Unit,
    modifier: Modifier = Modifier,
    activeTerm: String = "2nd Term 2024/2025",
    studentName: String = "Adeleke David O.",
    studentId: String = "GRS/2024/0428",
    onViewReceipt: ((PaymentTransaction) -> Unit)? = null
) {
    val totalInvoiced = feeItems.sumOf { it.amount }
    val totalPaid = feeItems.filter { it.isPaid }.sumOf { it.amount }
    val balanceOutstanding = (totalInvoiced - totalPaid).coerceAtLeast(0.0)

    val unpaidFees = feeItems.filter { !it.isPaid }
    val nextUnpaidFeeItem = unpaidFees.firstOrNull()
    val nextDueDate = nextUnpaidFeeItem?.dueDate ?: "All Cleared"
    val hasOutstandingBalance = balanceOutstanding > 0.0

    val paymentProgress = if (totalInvoiced > 0) {
        (totalPaid / totalInvoiced).toFloat().coerceIn(0f, 1f)
    } else {
        1f
    }

    val formattedBalance = "₦" + NumberFormat.getNumberInstance(Locale.US).format(balanceOutstanding)
    val formattedTotalInvoiced = "₦" + NumberFormat.getNumberInstance(Locale.US).format(totalInvoiced)
    val formattedTotalPaid = "₦" + NumberFormat.getNumberInstance(Locale.US).format(totalPaid)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fee_payment_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(
            1.dp,
            if (hasOutstandingBalance) Amber500.copy(alpha = 0.35f) else Emerald500.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Title, Category Icon & Live Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (hasOutstandingBalance) Amber500.copy(alpha = 0.15f) else Emerald500.copy(alpha = 0.15f),
                        border = BorderStroke(
                            1.dp,
                            if (hasOutstandingBalance) Amber500.copy(alpha = 0.3f) else Emerald500.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (hasOutstandingBalance) Icons.Default.ReceiptLong else Icons.Default.CheckCircle,
                                contentDescription = "Fee Summary",
                                tint = if (hasOutstandingBalance) Amber400 else Emerald400,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "FEE PAYMENT SUMMARY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            text = activeTerm,
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (hasOutstandingBalance) Amber500.copy(alpha = 0.15f) else Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        if (hasOutstandingBalance) Amber500.copy(alpha = 0.4f) else Emerald500.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (hasOutstandingBalance) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (hasOutstandingBalance) Amber400 else Emerald400,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (hasOutstandingBalance) "Payment Due" else "100% Cleared",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasOutstandingBalance) Amber400 else Emerald400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Balance and Due Date Showcase Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = DarkCardSurfaceElevated,
                border = BorderStroke(1.dp, DarkBorderSubtle)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left: Balance Display
                        Column {
                            Text(
                                text = "Current Balance",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate400
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formattedBalance,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (hasOutstandingBalance) Rose400 else Emerald400,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.testTag("fee_balance_text")
                            )
                            Text(
                                text = if (hasOutstandingBalance) "Outstanding Tuition & Levies" else "All term invoices fully settled",
                                fontSize = 11.sp,
                                color = Slate500
                            )
                        }

                        // Right: Next Due Date Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkCanvas.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Due Date",
                                        tint = if (hasOutstandingBalance) Amber400 else Emerald400,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Next Due Date",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate400
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = nextDueDate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasOutstandingBalance) Amber400 else Slate200,
                                    modifier = Modifier.testTag("fee_due_date_text")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Term Billing Progress",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate400
                            )
                            Text(
                                text = "${(paymentProgress * 100).toInt()}% Paid ($formattedTotalPaid / $formattedTotalInvoiced)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (hasOutstandingBalance) Slate300 else Emerald400
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { paymentProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (paymentProgress >= 1f) Emerald400 else Indigo500,
                            trackColor = Slate800,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            // Next Impending Bill Highlight (if pending)
            if (nextUnpaidFeeItem != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCanvas.copy(alpha = 0.5f),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Amber500.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = nextUnpaidFeeItem.category,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Amber400,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = nextUnpaidFeeItem.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate200,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = "₦" + NumberFormat.getNumberInstance(Locale.US).format(nextUnpaidFeeItem.amount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Pay Now & View Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Primary 'Pay Now' Button
                Button(
                    onClick = {
                        if (nextUnpaidFeeItem != null) {
                            onPayNow(nextUnpaidFeeItem)
                        } else {
                            onViewAllFees()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_pay_now"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasOutstandingBalance) Indigo600 else Emerald500
                    )
                ) {
                    Icon(
                        imageVector = if (hasOutstandingBalance) Icons.Default.CreditCard else Icons.Default.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hasOutstandingBalance) "Pay Now" else "View Statement",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Secondary 'View Details' Button
                OutlinedButton(
                    onClick = { onViewAllFees() },
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("btn_view_finance_hub"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DarkBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Slate300
                    )
                ) {
                    Text(
                        text = "Billing Hub",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Security & Bank Transfer Reassurance Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Slate500,
                    modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Moniepoint 5255883539 • Automated Verification & Instant Receipt",
                    fontSize = 10.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
