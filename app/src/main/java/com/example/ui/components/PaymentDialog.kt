package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.FeeItem
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentDialog(
    feeItem: FeeItem,
    onDismiss: () -> Unit,
    onConfirmPayment: (FeeItem, String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("Debit / Credit Card") }
    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val formattedAmount = "₦" + NumberFormat.getNumberInstance(Locale.US).format(feeItem.amount)

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("fee_checkout_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SECURE FEE CHECKOUT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo400
                        )
                    }

                    if (!isProcessing) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Item info
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = feeItem.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = "${feeItem.term} • ${feeItem.category}",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = DarkBorder)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable:", fontSize = 12.sp, color = Slate400)
                            Text(
                                text = formattedAmount,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo400
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Payment Channel:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Payment channels
                PaymentOptionRow(
                    title = "Debit / Credit Card (Mastercard, Visa, Verve)",
                    icon = Icons.Default.CreditCard,
                    selected = selectedMethod == "Debit / Credit Card",
                    onClick = { selectedMethod = "Debit / Credit Card" }
                )

                PaymentOptionRow(
                    title = "Direct Bank Transfer (Instant NIP)",
                    icon = Icons.Default.AccountBalance,
                    selected = selectedMethod == "Direct Bank Transfer",
                    onClick = { selectedMethod = "Direct Bank Transfer" }
                )

                PaymentOptionRow(
                    title = "USSD / Mobile Banking (*737#, *894#, etc.)",
                    icon = Icons.Default.PhoneAndroid,
                    selected = selectedMethod == "USSD / Mobile Banking",
                    onClick = { selectedMethod = "USSD / Mobile Banking" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Emerald400, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "256-Bit SSL Encrypted School Payment Gateway",
                        fontSize = 10.sp,
                        color = Slate400
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        isProcessing = true
                        scope.launch {
                            delay(1200) // simulate banking gateway processing
                            isProcessing = false
                            onConfirmPayment(feeItem, selectedMethod)
                        }
                    },
                    enabled = !isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_pay_fee_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Processing Transaction...", fontSize = 13.sp, color = Color.White)
                    } else {
                        Text("Authorize & Pay $formattedAmount", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Indigo500.copy(alpha = 0.15f) else Slate900,
        border = BorderStroke(1.dp, if (selected) Indigo500 else DarkBorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Indigo400,
                    unselectedColor = Slate500
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(icon, contentDescription = null, tint = if (selected) Indigo400 else Slate400, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Slate100 else Slate300
            )
        }
    }
}
