package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GroupChatMessage
import com.example.data.model.UserRole
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
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun GroupChatScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allMessages by viewModel.allGroupMessages.collectAsStateWithLifecycle()
    val selectedChannel by viewModel.selectedChatChannel.collectAsStateWithLifecycle()
    val mutedUsers by viewModel.mutedUsers.collectAsStateWithLifecycle()

    var messageInput by remember { mutableStateOf("") }
    var selectedAttachmentType by remember { mutableStateOf("NONE") }
    var selectedAttachmentName by remember { mutableStateOf("") }
    var showAttachmentDialog by remember { mutableStateOf(false) }

    val userRole = currentUser?.role ?: UserRole.STUDENT
    val isModerator = userRole == UserRole.ADMIN || userRole == UserRole.TEACHER

    // Channels accessible by current user role
    data class ChatChannelItem(val id: String, val title: String, val subtitle: String, val allowedRoles: List<UserRole>)

    val availableChannels = listOf(
        ChatChannelItem("class_ss1_science", "SS 1 Science Class Group", "Students, Science Teachers & Admin", listOf(UserRole.ADMIN, UserRole.TEACHER, UserRole.STUDENT)),
        ChatChannelItem("class_ss2_arts", "SS 2 Arts Class Group", "Students, Arts Teachers & Admin", listOf(UserRole.ADMIN, UserRole.TEACHER)),
        ChatChannelItem("class_jss2_gold", "JSS 2 Gold Class Group", "Junior High Students & Form Master", listOf(UserRole.ADMIN, UserRole.TEACHER)),
        ChatChannelItem("staff_room", "Graziel Faculty Staff Room", "Teachers & Principal Only", listOf(UserRole.ADMIN, UserRole.TEACHER)),
        ChatChannelItem("admin_broadcast", "School-Wide Broadcast", "Official Announcements & Guidelines", listOf(UserRole.ADMIN, UserRole.TEACHER, UserRole.STUDENT))
    ).filter { item ->
        userRole == UserRole.ADMIN || item.allowedRoles.contains(userRole)
    }

    val channelMessages = allMessages.filter { it.channelId == selectedChannel }
    val listState = rememberLazyListState()

    LaunchedEffect(channelMessages.size) {
        if (channelMessages.isNotEmpty()) {
            listState.animateScrollToItem(channelMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
    ) {
        // Channel Bar & Header
        Surface(
            color = DarkCardSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Indigo500.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Indigo400),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Forum, contentDescription = null, tint = Indigo400, modifier = Modifier.size(18.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            val activeChannelInfo = availableChannels.find { it.id == selectedChannel }
                            Text(
                                text = activeChannelInfo?.title ?: "Class Group Chat",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "${channelMessages.size} messages • Moderated channel",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    if (isModerator) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Slate800,
                            border = BorderStroke(1.dp, Emerald400.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = if (userRole == UserRole.ADMIN) "Super Moderator" else "Teacher Moderator",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald400,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Channel Selector Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableChannels) { ch ->
                        val isSelected = ch.id == selectedChannel
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Indigo600 else Slate800,
                            border = BorderStroke(1.dp, if (isSelected) Indigo400 else DarkBorderSubtle),
                            modifier = Modifier
                                .clickable { viewModel.selectChatChannel(ch.id) }
                                .testTag("chat_channel_chip_${ch.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ch.title.split(" ").take(3).joinToString(" "),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Slate100 else Slate300
                                )
                            }
                        }
                    }
                }
            }
        }

        // Messages Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(channelMessages) { msg ->
                val isMyMessage = msg.senderId == currentUser?.regOrStaffId

                ChatMessageBubble(
                    message = msg,
                    isMyMessage = isMyMessage,
                    isModerator = isModerator,
                    onDelete = {
                        viewModel.deleteGroupChatMessage(msg.id)
                        Toast.makeText(context, "Message removed by moderator.", Toast.LENGTH_SHORT).show()
                    },
                    onMuteSender = {
                        viewModel.muteUserInChat(msg.senderId)
                    }
                )
            }
        }

        // Attachment Preview Bar
        if (selectedAttachmentType != "NONE") {
            Surface(
                color = Slate900,
                border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Indigo400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(selectedAttachmentName, color = Slate100, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    IconButton(
                        onClick = {
                            selectedAttachmentType = "NONE"
                            selectedAttachmentName = ""
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Rose400, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        // Chat Input Bar
        Surface(
            color = DarkCardSurface,
            border = BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attach file button
                IconButton(
                    onClick = { showAttachmentDialog = true },
                    modifier = Modifier.testTag("chat_attach_file_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach PDF/Resource",
                        tint = Indigo400,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = { Text("Type in ${availableChannels.find { it.id == selectedChannel }?.title ?: "chat"}...", fontSize = 13.sp) },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkCanvas,
                        unfocusedContainerColor = DarkCanvas,
                        focusedIndicatorColor = Indigo400,
                        unfocusedIndicatorColor = DarkBorder,
                        focusedTextColor = Slate100,
                        unfocusedTextColor = Slate200
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_message_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (messageInput.isNotBlank() || selectedAttachmentType != "NONE") {
                            viewModel.sendGroupChatMessage(
                                text = messageInput.trim(),
                                attachmentType = selectedAttachmentType,
                                attachmentName = selectedAttachmentName
                            )
                            messageInput = ""
                            selectedAttachmentType = "NONE"
                            selectedAttachmentName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Slate100,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Attachment Chooser Dialog
    if (showAttachmentDialog) {
        val sampleFiles = listOf(
            "SS1_Mathematics_MidTerm_Revision.pdf" to "PDF",
            "Yoruba_Cultural_Ofuloju_StudyGuide.pdf" to "PDF",
            "Physics_Lab_Oscillations_Diagram.png" to "IMAGE",
            "WAEC_Past_Questions_2024.pdf" to "PDF"
        )

        AlertDialog(
            onDismissRequest = { showAttachmentDialog = false },
            containerColor = DarkCardSurfaceElevated,
            title = {
                Text("Share Academic Resource / Document", fontWeight = FontWeight.Bold, color = Slate100)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select a study document or past question paper to share with class members:", fontSize = 12.sp, color = Slate400)

                    sampleFiles.forEach { (fileName, type) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate900,
                            border = BorderStroke(1.dp, DarkBorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAttachmentName = fileName
                                    selectedAttachmentType = type
                                    showAttachmentDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (type == "PDF") Icons.Default.Description else Icons.Default.Image,
                                    contentDescription = null,
                                    tint = Indigo400,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(fileName, fontSize = 12.sp, color = Slate200, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(onClick = { showAttachmentDialog = false }) {
                    Text("Cancel", color = Slate300)
                }
            }
        )
    }
}

@Composable
private fun ChatMessageBubble(
    message: GroupChatMessage,
    isMyMessage: Boolean,
    isModerator: Boolean,
    onDelete: () -> Unit,
    onMuteSender: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val roleBadgeColor = when (message.senderRole) {
        "ADMIN" -> Rose400
        "TEACHER" -> Emerald400
        "PARENT" -> Amber400
        else -> Indigo400
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isMyMessage) Indigo600.copy(alpha = 0.25f) else DarkCardSurfaceElevated
            ),
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isMyMessage) 14.dp else 2.dp,
                bottomEnd = if (isMyMessage) 2.dp else 14.dp
            ),
            border = BorderStroke(1.dp, if (isMyMessage) Indigo400.copy(alpha = 0.5f) else DarkBorderSubtle),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Sender Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.senderName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = roleBadgeColor.copy(alpha = 0.15f),
                            border = BorderStroke(0.5.dp, roleBadgeColor.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = message.senderRole,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = roleBadgeColor,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Moderation Menu for Teacher / Admin
                    if (isModerator) {
                        Box {
                            IconButton(
                                onClick = { menuExpanded = true },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Moderate", tint = Slate400, modifier = Modifier.size(14.dp))
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                modifier = Modifier.background(DarkCardSurfaceElevated)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete Message", color = Rose400, fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Rose400, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        menuExpanded = false
                                        onDelete()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mute / Unmute Sender", color = Amber400, fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.VolumeMute, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        menuExpanded = false
                                        onMuteSender()
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Text
                if (message.text.isNotBlank()) {
                    Text(
                        text = message.text,
                        fontSize = 13.sp,
                        color = Slate200,
                        lineHeight = 18.sp
                    )
                }

                // Attachment
                if (message.attachmentType != "NONE" && message.attachmentName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate900,
                        border = BorderStroke(1.dp, Indigo400.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (message.attachmentType == "PDF") Icons.Default.Description else Icons.Default.Image,
                                contentDescription = null,
                                tint = Indigo400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = message.attachmentName,
                                fontSize = 11.sp,
                                color = Slate200,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 10.sp,
                        color = Slate500
                    )
                }
            }
        }
    }
}
