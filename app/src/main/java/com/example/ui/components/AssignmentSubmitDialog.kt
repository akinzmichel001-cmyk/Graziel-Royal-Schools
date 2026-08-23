package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Assignment
import com.example.ui.theme.Amber400
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun AssignmentSubmitDialog(
    assignment: Assignment,
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit
) {
    val context = LocalContext.current
    var submissionText by remember { mutableStateOf(assignment.submissionText) }
    var attachedFile by remember { mutableStateOf(if (assignment.isSubmitted) "homework_solution.pdf" else "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("assignment_submit_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = Indigo400, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (assignment.isSubmitted) "Assignment Details" else "Submit Assignment",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Assignment info
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = assignment.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Indigo400)
                        Text(text = "${assignment.subject} • Taught by ${assignment.teacher}", fontSize = 11.sp, color = Slate400)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Due: ${assignment.dueDate}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Amber400)
                            Text(text = "Max Score: ${assignment.maxScore} marks", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate200)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Divider(color = DarkBorder)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Instructions:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate300)
                        Text(text = assignment.description, fontSize = 12.sp, color = Slate400)
                    }
                }

                if (assignment.isSubmitted) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Emerald500.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("STATUS: SUBMITTED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                if (assignment.score != null) {
                                    Text("Score: ${assignment.score}/${assignment.maxScore}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                }
                            }
                            if (assignment.submissionText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Your response: ${assignment.submissionText}", fontSize = 11.sp, color = Slate200)
                            }
                            if (assignment.feedback.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Teacher Feedback: \"${assignment.feedback}\"", fontSize = 11.sp, color = Emerald300, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your Solution / Working / Notes:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate200)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = submissionText,
                        onValueChange = { submissionText = it },
                        placeholder = { Text("Write your answers or type homework notes here...", fontSize = 12.sp, color = Slate500) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("assignment_solution_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Slate100,
                            unfocusedTextColor = Slate200,
                            focusedBorderColor = Indigo400,
                            unfocusedBorderColor = DarkBorderSubtle,
                            focusedContainerColor = Slate900,
                            unfocusedContainerColor = Slate900,
                            cursorColor = Indigo400
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                attachedFile = "document_${System.currentTimeMillis().toString().takeLast(4)}.pdf"
                                Toast.makeText(context, "Attached document: $attachedFile", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, DarkBorderSubtle),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo400)
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (attachedFile.isEmpty()) "Attach File" else attachedFile, fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                if (submissionText.isNotBlank() || attachedFile.isNotBlank()) {
                                    val finalAnswer = if (attachedFile.isNotBlank()) "$submissionText (Attachment: $attachedFile)" else submissionText
                                    onSubmit(assignment.id, finalAnswer)
                                    Toast.makeText(context, "Assignment submitted to teacher!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please enter your answer or attach a file", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("submit_assignment_action_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Turn In", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
