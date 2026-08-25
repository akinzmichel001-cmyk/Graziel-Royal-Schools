package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FeeItem
import com.example.data.model.PaymentTransaction
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
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
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinanceScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val feeItems by viewModel.feeItems.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()

    val totalInvoiced = feeItems.sumOf { it.amount }
    val totalPaid = feeItems.filter { it.isPaid }.sumOf { it.amount }
    val balanceOutstanding = totalInvoiced - totalPaid

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("finance_screen_list"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        // Finance Summary Banner
        item {
            FinanceSummaryBanner(
                totalInvoiced = totalInvoiced,
                totalPaid = totalPaid,
                balance = balanceOutstanding
            )
        }

        // Section: Termly Fees Breakdown
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = Indigo400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "2ND TERM 2024/2025 FEE SCHEDULE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate300,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (balanceOutstanding == 0.0) Emerald500.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, if (balanceOutstanding == 0.0) Emerald500.copy(alpha = 0.3f) else Amber500.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (balanceOutstanding == 0.0) "Fully Cleared" else "Action Required",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (balanceOutstanding == 0.0) Emerald400 else Amber400,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Fee items list
        items(feeItems, key = { it.id }) { item ->
            FeeItemCard(
                feeItem = item,
                onPayNow = { viewModel.selectFeeToPay(item) }
            )
        }

        // Bursary Banking Details Card
        item {
            BursaryAccountCard()
        }

        // Section: Payment History & Receipts
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = Indigo400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TRANSACTION HISTORY & RECEIPTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate300,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        items(payments, key = { it.id }) { transaction ->
            PaymentTransactionCard(
                transaction = transaction,
                onViewReceipt = { viewModel.viewReceipt(transaction) }
            )
        }
    }
}

@Composable
private fun FinanceSummaryBanner(
    totalInvoiced: Double,
    totalPaid: Double,
    balance: Double
) {
    val formatNaira: (Double) -> String = { "₦" + NumberFormat.getNumberInstance(Locale.US).format(it) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("finance_summary_banner"),
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
                            Indigo900.copy(alpha = 0.6f),
                            DarkCardSurface,
                            DarkCardSurface
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BURSARY & TUITION PORTAL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Student: Adeleke David O. (JSS 2)",
                            fontSize = 13.sp,
                            color = Slate100,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(Icons.Default.CreditCard, contentDescription = null, tint = Indigo400, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Invoiced", fontSize = 11.sp, color = Slate400)
                        Text(formatNaira(totalInvoiced), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate100)
                    }

                    Column {
                        Text("Paid to Date", fontSize = 11.sp, color = Slate400)
                        Text(formatNaira(totalPaid), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Outstanding", fontSize = 11.sp, color = Slate400)
                        Text(
                            formatNaira(balance),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (balance > 0) Amber400 else Emerald400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = DarkBorder)
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Emerald400, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Instant electronic receipts stamped upon clearance",
                        fontSize = 10.sp,
                        color = Slate400
                    )
                }
            }
        }
    }
}

@Composable
private fun FeeItemCard(
    feeItem: FeeItem,
    onPayNow: () -> Unit
) {
    val formattedAmount = "₦" + NumberFormat.getNumberInstance(Locale.US).format(feeItem.amount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("fee_item_card_${feeItem.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = feeItem.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Category: ${feeItem.category} • Due: ${feeItem.dueDate}",
                    fontSize = 11.sp,
                    color = Slate400
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedAmount,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo400
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            if (feeItem.isPaid) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PAID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                    }
                }
            } else {
                Button(
                    onClick = onPayNow,
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("pay_fee_button_${feeItem.id}")
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pay Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PaymentTransactionCard(
    transaction: PaymentTransaction,
    onViewReceipt: () -> Unit
) {
    val formattedAmount = "₦" + NumberFormat.getNumberInstance(Locale.US).format(transaction.amount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("payment_transaction_${transaction.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(1.dp, DarkBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
                Text(
                    text = "${transaction.date} • ${transaction.paymentMethod}",
                    fontSize = 11.sp,
                    color = Slate400
                )
                Text(
                    text = "Ref: ${transaction.receiptNumber}",
                    fontSize = 10.sp,
                    color = Slate500,
                    fontFamily = FontFamily.Monospace
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formattedAmount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Emerald400
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = onViewReceipt,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo400),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("View Slip", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun BursaryAccountCard() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "OFFICIAL SCHOOL PAYMENT ACCOUNT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber400,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Amber400.copy(alpha = 0.3f)),
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString("5255883539"))
                        Toast.makeText(context, "Account Number 5255883539 copied!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Amber400, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Account No", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber400)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text("Bank Name: Monie Point (Moniepoint MFB)", fontSize = 12.sp, color = Slate200)
            Text("Account Name: Graziel Royal Schools Ltd.", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate100)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Account Number: ", fontSize = 12.sp, color = Slate400)
                Text("5255883539", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Amber400, fontFamily = FontFamily.Monospace)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Note: Use your Student ID (e.g. GRS/2024/0428) or Student Full Name as the payment narration/reference for automated instant receipt generation.", fontSize = 10.sp, color = Slate400)
        }
    }
}
