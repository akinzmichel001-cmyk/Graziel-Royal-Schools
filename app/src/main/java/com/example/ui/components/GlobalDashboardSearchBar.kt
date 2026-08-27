package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Announcement
import com.example.data.model.Assignment
import com.example.data.model.CbtTest
import com.example.data.model.SubjectGrade
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceElevated
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo300
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
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

enum class SearchCategoryFilter(val label: String) {
    ALL("All"),
    ASSIGNMENTS("Assignments"),
    ANNOUNCEMENTS("Announcements"),
    SUBJECTS("Subjects"),
    CBT_TESTS("CBT Tests")
}

sealed class GlobalSearchResultItem {
    data class AssignmentItem(val assignment: Assignment) : GlobalSearchResultItem()
    data class AnnouncementItem(val announcement: Announcement) : GlobalSearchResultItem()
    data class SubjectItem(val subject: SubjectGrade) : GlobalSearchResultItem()
    data class CbtTestItem(val test: CbtTest) : GlobalSearchResultItem()
}

/**
 * Global Dashboard Search Bar component.
 * Allows students, teachers, and parents to quickly search and filter assignments,
 * school announcements, subject grading details, and CBT assessments with live matching.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GlobalDashboardSearchBar(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(SearchCategoryFilter.ALL) }
    var isExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Observe data sources from ViewModel
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val cbtTests by viewModel.cbtTests.collectAsStateWithLifecycle()
    val currentReport = remember(viewModel) { viewModel.getCurrentReportCard() }
    val subjects = currentReport.grades

    // Dialog preview states
    var previewSubject by remember { mutableStateOf<SubjectGrade?>(null) }
    var previewAnnouncement by remember { mutableStateOf<Announcement?>(null) }

    // Quick suggestion keywords for students
    val quickSuggestions = listOf(
        "Mathematics",
        "Physics Lab",
        "Chemistry Assignment",
        "Cultural Exhibition",
        "Biology Ecosystem",
        "Robotics & Coding",
        "Yoruba Language"
    )

    // Compute matching results based on search query and category filter
    val searchResults: List<GlobalSearchResultItem> = remember(searchQuery, selectedFilter, assignments, announcements, subjects, cbtTests) {
        if (searchQuery.trim().isEmpty()) {
            return@remember emptyList()
        }

        val q = searchQuery.trim().lowercase()
        val results = mutableListOf<GlobalSearchResultItem>()

        // 1. Filter Assignments
        if (selectedFilter == SearchCategoryFilter.ALL || selectedFilter == SearchCategoryFilter.ASSIGNMENTS) {
            assignments.filter {
                it.title.lowercase().contains(q) ||
                it.subject.lowercase().contains(q) ||
                it.teacher.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.targetClass.lowercase().contains(q)
            }.forEach { results.add(GlobalSearchResultItem.AssignmentItem(it)) }
        }

        // 2. Filter Announcements
        if (selectedFilter == SearchCategoryFilter.ALL || selectedFilter == SearchCategoryFilter.ANNOUNCEMENTS) {
            announcements.filter {
                it.title.lowercase().contains(q) ||
                it.summary.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.author.lowercase().contains(q)
            }.forEach { results.add(GlobalSearchResultItem.AnnouncementItem(it)) }
        }

        // 3. Filter Subjects & Grades
        if (selectedFilter == SearchCategoryFilter.ALL || selectedFilter == SearchCategoryFilter.SUBJECTS) {
            subjects.filter {
                it.subjectName.lowercase().contains(q) ||
                it.teacherName.lowercase().contains(q) ||
                it.remark.lowercase().contains(q) ||
                it.gradeLetter.lowercase().contains(q)
            }.forEach { results.add(GlobalSearchResultItem.SubjectItem(it)) }
        }

        // 4. Filter CBT Tests
        if (selectedFilter == SearchCategoryFilter.ALL || selectedFilter == SearchCategoryFilter.CBT_TESTS) {
            cbtTests.filter {
                it.title.lowercase().contains(q) ||
                it.subject.lowercase().contains(q) ||
                it.createdByTeacher.lowercase().contains(q) ||
                it.instructions.lowercase().contains(q)
            }.forEach { results.add(GlobalSearchResultItem.CbtTestItem(it)) }
        }

        results
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("dashboard_global_search_bar"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        border = BorderStroke(
            1.dp,
            if (isExpanded || searchQuery.isNotBlank()) Indigo500.copy(alpha = 0.5f) else DarkBorderSubtle
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Search Input Row
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isNotBlank()) isExpanded = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_query_input"),
                placeholder = {
                    Text(
                        text = "Search assignments, notices, subjects...",
                        fontSize = 13.sp,
                        color = Slate500
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (searchQuery.isNotBlank()) Indigo400 else Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                                isExpanded = false
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.testTag("btn_clear_search")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = Slate400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Slate800,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "GLOBAL",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate400,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Indigo500,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkCanvas,
                    unfocusedContainerColor = DarkCanvas,
                    focusedTextColor = Slate100,
                    unfocusedTextColor = Slate200
                )
            )

            // Category Filter Chips
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchCategoryFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    val chipColor = when (filter) {
                        SearchCategoryFilter.ALL -> Indigo400
                        SearchCategoryFilter.ASSIGNMENTS -> Indigo400
                        SearchCategoryFilter.ANNOUNCEMENTS -> Amber400
                        SearchCategoryFilter.SUBJECTS -> Emerald400
                        SearchCategoryFilter.CBT_TESTS -> Rose400
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedFilter = filter
                            if (searchQuery.isNotEmpty()) isExpanded = true
                        },
                        label = {
                            Text(
                                text = filter.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor.copy(alpha = 0.2f),
                            selectedLabelColor = chipColor,
                            containerColor = Slate800,
                            labelColor = Slate400
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) chipColor else DarkBorderSubtle,
                            selectedBorderColor = chipColor,
                            borderWidth = 1.dp
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }

            // Quick Suggestions when search query is empty
            if (searchQuery.isEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "QUICK SEARCH SUGGESTIONS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickSuggestions.forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate800.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, DarkBorderSubtle),
                            modifier = Modifier
                                .clickable {
                                    searchQuery = suggestion
                                    isExpanded = true
                                }
                                .testTag("suggestion_$suggestion")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = suggestion,
                                    fontSize = 11.sp,
                                    color = Slate300
                                )
                            }
                        }
                    }
                }
            }

            // Live Search Results List
            if (searchQuery.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = DarkBorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RESULTS (${searchResults.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "Query: \"$searchQuery\"",
                        fontSize = 11.sp,
                        color = Indigo400,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (searchResults.isEmpty()) {
                    // No Results Found State
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkCanvas,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Slate500,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No matching items found for \"$searchQuery\"",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate300
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Try searching by subject name (e.g. Mathematics, Biology), teacher, or announcement topic.",
                                fontSize = 11.sp,
                                color = Slate500,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                } else {
                    // Scrollable list of matched result cards (Max height constrained to preserve dashboard layout)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp)
                            .testTag("search_results_list"),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { item ->
                            when (item) {
                                is GlobalSearchResultItem.AssignmentItem -> {
                                    SearchResultAssignmentCard(
                                        assignment = item.assignment,
                                        onOpenAssignment = {
                                            viewModel.selectAssignment(item.assignment)
                                            viewModel.navigateTo(AppDestination.HOMEWORK)
                                        }
                                    )
                                }
                                is GlobalSearchResultItem.AnnouncementItem -> {
                                    SearchResultAnnouncementCard(
                                        announcement = item.announcement,
                                        onReadNotice = {
                                            previewAnnouncement = item.announcement
                                        }
                                    )
                                }
                                is GlobalSearchResultItem.SubjectItem -> {
                                    SearchResultSubjectCard(
                                        subject = item.subject,
                                        onViewSubjectDetails = {
                                            previewSubject = item.subject
                                        },
                                        onGoToAcademics = {
                                            viewModel.navigateTo(AppDestination.ACADEMICS)
                                        }
                                    )
                                }
                                is GlobalSearchResultItem.CbtTestItem -> {
                                    SearchResultCbtCard(
                                        test = item.test,
                                        onStartTest = {
                                            viewModel.startCbtExam(item.test)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Subject Detail Inspector
    previewSubject?.let { subject ->
        SubjectDetailModalDialog(
            subject = subject,
            onDismiss = { previewSubject = null },
            onOpenFullReport = {
                previewSubject = null
                viewModel.navigateTo(AppDestination.ACADEMICS)
            }
        )
    }

    // Modal: Announcement Detail Inspector
    previewAnnouncement?.let { notice ->
        AnnouncementDetailModalDialog(
            announcement = notice,
            onDismiss = { previewAnnouncement = null }
        )
    }
}

// ----------------------------------------------------
// RESULT CARDS
// ----------------------------------------------------

@Composable
fun SearchResultAssignmentCard(
    assignment: Assignment,
    onOpenAssignment: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCanvas,
        border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenAssignment() }
            .testTag("result_assignment_${assignment.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Indigo500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.3f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = Indigo400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Indigo500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "ASSIGNMENT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo300,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = assignment.subject,
                            fontSize = 11.sp,
                            color = Slate400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = assignment.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Due: ${assignment.dueDate} • By ${assignment.teacher}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (assignment.isSubmitted) Emerald500.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f),
                border = BorderStroke(
                    1.dp,
                    if (assignment.isSubmitted) Emerald500.copy(alpha = 0.4f) else Amber500.copy(alpha = 0.4f)
                ),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = if (assignment.isSubmitted) "Submitted (${assignment.score ?: 0}/20)" else "Submit Now",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (assignment.isSubmitted) Emerald400 else Amber400,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchResultAnnouncementCard(
    announcement: Announcement,
    onReadNotice: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCanvas,
        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReadNotice() }
            .testTag("result_announcement_${announcement.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Amber500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "ANNOUNCEMENT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber400,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = announcement.category,
                            fontSize = 11.sp,
                            color = Slate400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = announcement.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${announcement.date} • ${announcement.author}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Slate800,
                border = BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = "Read Notice",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate200,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchResultSubjectCard(
    subject: SubjectGrade,
    onViewSubjectDetails: () -> Unit,
    onGoToAcademics: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCanvas,
        border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewSubjectDetails() }
            .testTag("result_subject_${subject.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Emerald500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.3f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Emerald400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Emerald500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "SUBJECT CURRICULUM",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald300,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "Teacher: ${subject.teacherName}",
                            fontSize = 11.sp,
                            color = Slate400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subject.subjectName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = "Total Score: ${subject.totalScore}% • Rank: #${subject.position} in class • ${subject.remark}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Emerald500.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f)),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = subject.gradeLetter,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Emerald400
                    )
                    Text(
                        text = "${subject.totalScore}%",
                        fontSize = 10.sp,
                        color = Emerald300
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultCbtCard(
    test: CbtTest,
    onStartTest: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCanvas,
        border = BorderStroke(1.dp, Rose500.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStartTest() }
            .testTag("result_cbt_${test.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Rose500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Rose500.copy(alpha = 0.3f)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = Rose400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Rose500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "CBT ASSESSMENT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Rose400,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = test.subject,
                            fontSize = 11.sp,
                            color = Slate400,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = test.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${test.durationMinutes} mins • ${test.totalMarks} marks • By ${test.createdByTeacher}",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (test.isLive) Emerald500.copy(alpha = 0.2f) else Slate800,
                border = BorderStroke(1.dp, if (test.isLive) Emerald500.copy(alpha = 0.4f) else DarkBorder),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = if (test.isLive) "Take Exam" else "Upcoming",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (test.isLive) Emerald400 else Slate400,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// ----------------------------------------------------
// MODAL DETAILS DIALOGS
// ----------------------------------------------------

@Composable
fun SubjectDetailModalDialog(
    subject: SubjectGrade,
    onDismiss: () -> Unit,
    onOpenFullReport: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("subject_detail_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
            border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Title and Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Emerald500.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = Emerald400,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = subject.subjectName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate100
                            )
                            Text(
                                text = "Senior Secondary Science Curriculum",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Score Highlights
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCanvas,
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "TOTAL", fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Bold)
                            Text(text = "${subject.totalScore}%", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Emerald400)
                        }
                        Divider(modifier = Modifier.height(30.dp).width(1.dp), color = DarkBorderSubtle)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "GRADE", fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Bold)
                            Text(text = subject.gradeLetter, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Emerald300)
                        }
                        Divider(modifier = Modifier.height(30.dp).width(1.dp), color = DarkBorderSubtle)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "RANK", fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Bold)
                            Text(text = "#${subject.position}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Amber400)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Continuous Assessment Breakdown
                Text(
                    text = "ASSESSMENT COMPONENT BREAKDOWN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkCanvas,
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("1st Continuous Assessment (CA1)", fontSize = 12.sp, color = Slate300)
                            Text("${subject.ca1} / 15", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("2nd Continuous Assessment (CA2)", fontSize = 12.sp, color = Slate300)
                            Text("${subject.ca2} / 15", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Practical Lab Project & Assignment", fontSize = 12.sp, color = Slate300)
                            Text("${subject.projectScore} / 10", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Terminal Examination", fontSize = 12.sp, color = Slate300)
                            Text("${subject.examScore} / 60", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate100)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Teacher Remark
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate800.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Teacher's Evaluation Remark:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"${subject.remark} — Student exhibits strong cognitive mastery in ${subject.subjectName}.\"",
                            fontSize = 12.sp,
                            color = Slate200,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Assigned Faculty: ${subject.teacherName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Indigo400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOpenFullReport,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View in Academic Transcript", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AnnouncementDetailModalDialog(
    announcement: Announcement,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("announcement_detail_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardSurfaceElevated),
            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Amber500.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = announcement.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber400,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = announcement.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Published on ${announcement.date} • By ${announcement.author}",
                    fontSize = 11.sp,
                    color = Slate400
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkCanvas,
                    border = BorderStroke(1.dp, DarkBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = announcement.summary,
                        fontSize = 13.sp,
                        color = Slate200,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800)
                ) {
                    Text("Dismiss", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate200)
                }
            }
        }
    }
}
