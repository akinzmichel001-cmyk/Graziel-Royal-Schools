package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.AdmissionApplication
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
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdmissionsScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val submissionSuccess by viewModel.admissionSubmissionSuccess.collectAsStateWithLifecycle()

    var studentName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var classChoice by remember { mutableStateOf("JSS 1 (College)") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var parentEmail by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var prevSchool by remember { mutableStateOf("") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Slate100,
        unfocusedTextColor = Slate200,
        focusedBorderColor = Indigo400,
        unfocusedBorderColor = DarkBorderSubtle,
        focusedLabelColor = Indigo400,
        unfocusedLabelColor = Slate400,
        focusedPlaceholderColor = Slate500,
        unfocusedPlaceholderColor = Slate500,
        cursorColor = Indigo400
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("admissions_screen_list"),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        // Hero Showcase
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("admissions_hero_card"),
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
                                    Indigo900.copy(alpha = 0.7f),
                                    DarkCardSurface,
                                    DarkCardSurface
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Amber500.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "2025/2026 ACADEMIC ADMISSIONS OPEN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Nurturing Tomorrow's Leaders with Royal Heritage",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "We offer a blended British and Nigerian curriculum fostering academic mastery, character discipline, robotics innovation, and cultural heritage.",
                            fontSize = 12.sp,
                            color = Slate300,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PillarBadge("Early Years", "Creche & Nursery", modifier = Modifier.weight(1f))
                            PillarBadge("Primary", "Grades 1 to 6", modifier = Modifier.weight(1f))
                            PillarBadge("College", "JSS 1 to SS 3", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Why Choose Graziel Royal Schools
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THE GRAZIEL ROYAL ADVANTAGE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AdvantageRow(icon = Icons.Default.School, title = "Integrated Dual Curriculum", desc = "Cambridge Checkpoint, IGCSE & WAEC/NECO excellence standards.")
                    AdvantageRow(icon = Icons.Default.VerifiedUser, title = "Robotics & STEM Innovation Hub", desc = "Hands-on coding from Primary school, AI labs & science fairs.")
                    AdvantageRow(icon = Icons.Default.Star, title = "Cultural Appreciation & Yoruba Heritage", desc = "Interactive culinary arts, traditional values, and moral ethics.")
                    AdvantageRow(icon = Icons.Default.LocationOn, title = "Secure & Serene Campus", desc = "Gated premises located at Opo-Ibogun, Ifo, Ogun State with school bus transit.")
                }
            }
        }

        // Online Application Form
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("admissions_form_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                border = BorderStroke(1.dp, DarkBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HowToReg, contentDescription = null, tint = Indigo400, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ONLINE APPLICATION FORM",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Amber500.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f))
                        ) {
                            Text("Fast-Track", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber400, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Fill this form to register your child for the upcoming entrance examination and interview session.",
                        fontSize = 11.sp,
                        color = Slate400
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (submissionSuccess) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Emerald500.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald400, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Application Received!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                                Text(
                                    text = "Thank you for choosing Graziel Royal Schools. Our Admissions Officer will contact you within 24 hours to schedule the entrance interview.",
                                    fontSize = 11.sp,
                                    color = Slate200,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        viewModel.resetAdmissionSuccess()
                                        studentName = ""
                                        parentPhone = ""
                                        parentName = ""
                                    },
                                    border = BorderStroke(1.dp, DarkBorderSubtle),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo400),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Submit Another Application", fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        // Form fields
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Candidate Full Name *", fontSize = 12.sp) },
                            placeholder = { Text("e.g. David Oluwaseun Adeleke", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admission_student_name_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dob,
                                onValueChange = { dob = it },
                                label = { Text("Date of Birth *", fontSize = 11.sp) },
                                placeholder = { Text("DD/MM/YYYY", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )

                            OutlinedTextField(
                                value = classChoice,
                                onValueChange = { classChoice = it },
                                label = { Text("Applying For Class *", fontSize = 11.sp) },
                                modifier = Modifier.weight(1.2f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = parentName,
                            onValueChange = { parentName = it },
                            label = { Text("Parent / Guardian Name *", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = parentPhone,
                                onValueChange = { parentPhone = it },
                                label = { Text("Phone Number *", fontSize = 11.sp) },
                                placeholder = { Text("+234...", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )

                            OutlinedTextField(
                                value = parentEmail,
                                onValueChange = { parentEmail = it },
                                label = { Text("Email Address", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = fieldColors
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Residential Area / Address", fontSize = 12.sp) },
                            placeholder = { Text("e.g. Opo-Ibogun, Ifo, Abeokuta corridor...", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = prevSchool,
                            onValueChange = { prevSchool = it },
                            label = { Text("Previous School Attended (Optional)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = fieldColors
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (studentName.isNotBlank() && parentPhone.isNotBlank()) {
                                    val application = AdmissionApplication(
                                        studentName = studentName,
                                        dateOfBirth = if (dob.isNotBlank()) dob else "12/04/2012",
                                        gender = gender,
                                        classApplyingFor = classChoice,
                                        parentName = if (parentName.isNotBlank()) parentName else "Parent",
                                        parentPhone = parentPhone,
                                        parentEmail = parentEmail,
                                        address = address,
                                        previousSchool = prevSchool,
                                        submissionDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                                    )
                                    viewModel.submitAdmission(application)
                                } else {
                                    Toast.makeText(context, "Please enter student name and parent phone number", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_admission_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submit Online Application", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Campus Contact & Visit Info
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate900),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CAMPUS INQUIRIES & ADMISSION OFFICE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo400,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ContactRow(icon = Icons.Default.LocationOn, text = "Opo-Ibogun, Ifo, Ogun State, Nigeria")
                    ContactRow(icon = Icons.Default.Phone, text = "+234 816 620 5113 (Admin & Admissions Line)")
                    ContactRow(icon = Icons.Default.Email, text = "admin@grazielroyalschools.edu.ng")
                    ContactRow(icon = Icons.Default.Star, text = "Motto: Knowledge, Spirit and Service")
                    ContactRow(icon = Icons.Default.VerifiedUser, text = "Founder & Proprietor: Mr. Tobi Adebayo")

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.callSchoolAdmin(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = { viewModel.openSchoolWhatsApp(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WhatsApp Us", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkCanvas)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PillarBadge(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Slate900.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, DarkBorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Amber400)
            Text(text = subtitle, fontSize = 9.sp, color = Slate300)
        }
    }
}

@Composable
private fun AdvantageRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Indigo500.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
            Text(text = desc, fontSize = 11.sp, color = Slate400)
        }
    }
}

@Composable
private fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 11.sp, color = Slate200)
    }
}
