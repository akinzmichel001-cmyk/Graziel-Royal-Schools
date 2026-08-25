package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AiTutorSpecification
import com.example.data.model.ChatMessage
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
import com.example.ui.theme.Rose500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTutorScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    val messages by viewModel.aiChatMessages.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val currentSpec by viewModel.aiTutorSpecification.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var showCustomizerSheet by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Dynamic prompt suggestions based on active user role & specifications
    val samplePrompts = remember(currentSpec) {
        when {
            currentSpec.userRole.contains("Teacher", ignoreCase = true) -> listOf(
                "📋 Create a 45-min Lesson Plan for ${currentSpec.gradeLevel} on ${currentSpec.subject}",
                "📝 Generate 5 CBT multiple-choice questions with answer key & explanations",
                "📚 Draft a 3-week Scheme of Work with behavioral objectives",
                "🎯 Formulate a standard Marking Scheme & Rubric for terminal exam"
            )
            currentSpec.userRole.contains("Parent", ignoreCase = true) -> listOf(
                "👨‍👩‍👧 Best daily revision routine for my child in ${currentSpec.gradeLevel}",
                "💳 Official school bank payment details (Monie Point)",
                "📊 Explain how to interpret 2nd Term report card remarks & grades",
                "🌟 What are the key benefits of the Yoruba Cultural Day (Ofuloju) project?"
            )
            currentSpec.userRole.contains("Admin", ignoreCase = true) -> listOf(
                "🏛️ Draft an official School Circular to all parents regarding term dates",
                "📢 Compose a broadcast announcement for CBT examination guidelines",
                "📋 Write standard staff clock-in and academic punctuality memo",
                "🌟 School admission guidelines and campus inquiry response"
            )
            else -> listOf(
                "📐 Solve this problem step-by-step with clear formulas: x² - 5x + 6 = 0",
                "🌟 Tell me about the Yoruba Cultural Project with Pounded Yam & Ofuloju",
                "🎯 3 WAEC/JAMB exam practice questions for ${currentSpec.subject}",
                "💡 Give me a hint on how to start solving without spoiling the answer",
                "⚡ Explain this concept like I'm in Grade 5 (simple analogies)"
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("ai_tutor_screen")
    ) {
        // Active Custom Specification Header Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Indigo600, Amber500)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentSpec.aiTutorName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Emerald500.copy(alpha = 0.15f),
                                border = BorderStroke(0.5.dp, Emerald500.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "Custom Active",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald400,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Role: ${currentSpec.userRole} • ${currentSpec.gradeLevel}",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    // Customize Button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Indigo600.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Indigo500),
                        modifier = Modifier.clickable { showCustomizerSheet = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Tune,
                                contentDescription = "Customize",
                                tint = Indigo400,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Customize",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo400
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Clear Chat Icon
                    IconButton(
                        onClick = { viewModel.clearAiChat() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Clear Chat",
                            tint = Slate400,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                // Specification Quick Tags Pill Row
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        SpecTagPill(label = currentSpec.subject, icon = "📚")
                    }
                    item {
                        SpecTagPill(label = currentSpec.teachingStyle, icon = "💡")
                    }
                    item {
                        SpecTagPill(label = currentSpec.languageComplexity, icon = "🎯")
                    }
                    if (currentSpec.customInstruction.isNotBlank()) {
                        item {
                            SpecTagPill(label = "Custom Prompt Active", icon = "⚡")
                        }
                    }
                }
            }
        }

        // Suggested Prompt Pills based on specs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(samplePrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.clickable {
                        viewModel.sendAiPrompt(prompt, modeBadge = currentSpec.userRole)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prompt,
                            fontSize = 11.sp,
                            color = Slate200,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Chat Message History
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                AiChatBubble(
                    message = msg,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(msg.text))
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (isAiThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Indigo400,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${currentSpec.aiTutorName} is tailoring your response...",
                            fontSize = 11.sp,
                            color = Slate400,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        // Chat Input Row
        Surface(
            color = Slate900,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Ask ${currentSpec.aiTutorName} (${currentSpec.userRole} Mode)...",
                            fontSize = 12.sp,
                            color = Slate500
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_tutor_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Slate100,
                        unfocusedTextColor = Slate200,
                        focusedBorderColor = Indigo400,
                        unfocusedBorderColor = DarkBorderSubtle,
                        focusedContainerColor = DarkCardSurface,
                        unfocusedContainerColor = DarkCardSurface,
                        cursorColor = Indigo400
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userMsg = inputText.trim()
                            inputText = ""
                            viewModel.sendAiPrompt(userMsg, modeBadge = currentSpec.userRole)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Indigo600)
                        .testTag("ai_tutor_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Modal Bottom Sheet: Full Tutor Customization Studio
    if (showCustomizerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCustomizerSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Slate900,
            scrimColor = Color.Black.copy(alpha = 0.65f)
        ) {
            TutorCustomizerStudio(
                currentSpec = currentSpec,
                onSave = { updated ->
                    viewModel.updateAiSpecification(updated)
                    showCustomizerSheet = false
                },
                onDismiss = { showCustomizerSheet = false }
            )
        }
    }
}

@Composable
private fun SpecTagPill(label: String, icon: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Slate800,
        border = BorderStroke(0.5.dp, DarkBorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 9.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 9.sp, color = Slate300)
        }
    }
}

@Composable
private fun AiChatBubble(
    message: ChatMessage,
    onCopy: () -> Unit
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Indigo900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Amber400,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) Indigo600 else DarkCardSurface,
            border = if (!isUser) BorderStroke(1.dp, DarkBorder) else null,
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.92f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header inside AI bubble with role badge and copy action
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Indigo500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = message.modeBadge ?: "Custom Tutor",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo400,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy text",
                                tint = Slate400,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.text,
                    fontSize = 12.sp,
                    color = if (isUser) Color.White else Slate100,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    fontSize = 9.sp,
                    color = if (isUser) Color.White.copy(alpha = 0.7f) else Slate500,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Amber500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Slate900,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TutorCustomizerStudio(
    currentSpec: AiTutorSpecification,
    onSave: (AiTutorSpecification) -> Unit,
    onDismiss: () -> Unit
) {
    var userRole by remember { mutableStateOf(currentSpec.userRole) }
    var gradeLevel by remember { mutableStateOf(currentSpec.gradeLevel) }
    var subject by remember { mutableStateOf(currentSpec.subject) }
    var customSubjectName by remember { mutableStateOf(currentSpec.customSubjectName) }
    var teachingStyle by remember { mutableStateOf(currentSpec.teachingStyle) }
    var languageComplexity by remember { mutableStateOf(currentSpec.languageComplexity) }
    var customInstruction by remember { mutableStateOf(currentSpec.customInstruction) }
    var tutorName by remember { mutableStateOf(currentSpec.aiTutorName) }

    val roleOptions = listOf(
        "Student",
        "Teacher",
        "Parent",
        "Administrator",
        "General Scholar"
    )

    val gradeOptions = listOf(
        "Nursery / KG",
        "Primary 1 - 3 (Lower)",
        "Primary 4 - 6 (Upper)",
        "JSS 1 - 3 (Junior)",
        "SS 1 - 3 (Senior)",
        "WAEC / JAMB Candidate",
        "College / Adult"
    )

    val subjectOptions = listOf(
        "Mathematics & Sciences",
        "English & Literature",
        "Physics",
        "Chemistry",
        "Biology & Agric",
        "Basic Science & Tech",
        "Economics & Business",
        "Government & Civic",
        "Yoruba & Cultural Heritage",
        "Coding, ICT & Robotics",
        "Custom Subject"
    )

    val styleOptions = listOf(
        "Step-by-Step Patient Mentor",
        "Socratic Questioner (Hints First)",
        "Exam Drill Master (WAEC/JAMB)",
        "Simplifier (ELI5 & Analogies)",
        "Lesson Planner & Scheme Builder",
        "Fast Direct Solver"
    )

    val complexityOptions = listOf(
        "Primary (Simple & Visual)",
        "Standard & Engaging",
        "Advanced Academic / Technical"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Tune,
                    contentDescription = null,
                    tint = Amber400,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Tutor Customization Studio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400)
            }
        }

        Text(
            text = "Configure the AI to adapt its persona, level, subject, and style to your exact academic needs.",
            fontSize = 11.sp,
            color = Slate400
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. User Role
        SectionHeading("1. User Role / Persona")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            roleOptions.forEach { opt ->
                ChoiceChip(
                    text = opt,
                    isSelected = userRole.equals(opt, ignoreCase = true),
                    onSelect = { userRole = opt }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Grade Level
        SectionHeading("2. Target Level / Class")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            gradeOptions.forEach { opt ->
                ChoiceChip(
                    text = opt,
                    isSelected = gradeLevel.equals(opt, ignoreCase = true),
                    onSelect = { gradeLevel = opt }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Subject Focus
        SectionHeading("3. Subject Focus")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            subjectOptions.forEach { opt ->
                ChoiceChip(
                    text = opt,
                    isSelected = subject.equals(opt, ignoreCase = true),
                    onSelect = { subject = opt }
                )
            }
        }

        if (subject == "Custom Subject") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customSubjectName,
                onValueChange = { customSubjectName = it },
                label = { Text("Enter Custom Subject Name", fontSize = 11.sp, color = Slate400) },
                placeholder = { Text("e.g. Further Mathematics or Technical Drawing", fontSize = 11.sp, color = Slate500) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = customTextFieldColors()
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Teaching Style
        SectionHeading("4. Teaching Style & Approach")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            styleOptions.forEach { opt ->
                ChoiceChip(
                    text = opt,
                    isSelected = teachingStyle.equals(opt, ignoreCase = true),
                    onSelect = { teachingStyle = opt }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Language Complexity
        SectionHeading("5. Language & Complexity")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            complexityOptions.forEach { opt ->
                ChoiceChip(
                    text = opt,
                    isSelected = languageComplexity.equals(opt, ignoreCase = true),
                    onSelect = { languageComplexity = opt }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 6. Custom Instructions / Goals
        SectionHeading("6. Special Prompt / Custom Instructions (Optional)")
        OutlinedTextField(
            value = customInstruction,
            onValueChange = { customInstruction = it },
            placeholder = { Text("e.g., 'Emphasize WAEC Marking Schemes' or 'Always give real-world Nigerian examples'", fontSize = 11.sp, color = Slate500) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            maxLines = 3,
            colors = customTextFieldColors()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 7. Tutor Name
        SectionHeading("7. Custom AI Tutor Name")
        OutlinedTextField(
            value = tutorName,
            onValueChange = { tutorName = it },
            placeholder = { Text("e.g., Graziel Royal AI Tutor", fontSize = 11.sp, color = Slate500) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = customTextFieldColors()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DarkBorder)
            ) {
                Text("Cancel", color = Slate300)
            }

            Button(
                onClick = {
                    val updated = AiTutorSpecification(
                        userRole = userRole,
                        gradeLevel = gradeLevel,
                        subject = subject,
                        customSubjectName = customSubjectName,
                        teachingStyle = teachingStyle,
                        languageComplexity = languageComplexity,
                        customInstruction = customInstruction,
                        aiTutorName = if (tutorName.isNotBlank()) tutorName.trim() else "Graziel Royal AI Tutor"
                    )
                    onSave(updated)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Apply & Save", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Amber400,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun ChoiceChip(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Indigo600 else Slate800,
        border = BorderStroke(
            1.dp,
            if (isSelected) Amber400 else DarkBorder
        ),
        modifier = Modifier.clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Amber400,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Slate300
            )
        }
    }
}

@Composable
private fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Slate100,
    unfocusedTextColor = Slate200,
    focusedBorderColor = Indigo400,
    unfocusedBorderColor = DarkBorderSubtle,
    focusedContainerColor = DarkCardSurface,
    unfocusedContainerColor = DarkCardSurface,
    cursorColor = Indigo400
)
