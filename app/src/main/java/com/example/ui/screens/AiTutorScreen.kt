package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
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
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun AiTutorScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.aiChatMessages.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val samplePrompts = listOf(
        "Tell me about the Yoruba Cultural Project with Pounded Yam & Ofuloju",
        "How do I solve linear equations step by step?",
        "Explain photosynthesis in basic science",
        "What are the school fees for this term?",
        "How can I apply for 2025/2026 admissions?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .testTag("ai_tutor_screen")
    ) {
        // AI Tutor Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
            border = BorderStroke(1.dp, DarkBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Indigo600, Amber500)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Graziel Royal AI Academic Tutor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = "Ask homework questions, syllabus concepts & school info",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Emerald400))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Online", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Emerald400)
                    }
                }
            }
        }

        // Suggested Prompt Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(samplePrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate900,
                    border = BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.clickable {
                        viewModel.sendAiMessage(prompt)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = Indigo400, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = prompt, fontSize = 11.sp, color = Slate200, maxLines = 1)
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
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                AiChatBubble(message = msg)
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
                        Text("Royal AI Tutor is typing...", fontSize = 11.sp, color = Slate400, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
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
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask your academic question...", fontSize = 12.sp, color = Slate500) },
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
                            val userMsg = inputText
                            inputText = ""
                            viewModel.sendAiMessage(userMsg)
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
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AiChatBubble(message: ChatMessage) {
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
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
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
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
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
                Icon(Icons.Default.Person, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
            }
        }
    }
}
