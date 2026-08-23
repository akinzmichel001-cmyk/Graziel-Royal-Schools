package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.components.AssignmentSubmitDialog
import com.example.ui.components.PaymentDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.components.ReportCardSheet
import com.example.ui.components.SchoolBottomNav
import com.example.ui.components.SchoolTopBar
import com.example.ui.screens.AcademicsScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdmissionsScreen
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CbtExamScreen
import com.example.ui.screens.CbtStudioScreen
import com.example.ui.screens.FinanceScreen
import com.example.ui.screens.GroupChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HomeworkScreen
import com.example.ui.screens.ParentPortalScreen
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.StudentPortalScreen
import com.example.ui.screens.TeacherPortalScreen
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GrazielRoyalTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.SchoolViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrazielRoyalTheme {
                GrazielRoyalApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GrazielRoyalApp(
    viewModel: SchoolViewModel
) {
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val selectedFeeToPay by viewModel.selectedFeeToPay.collectAsStateWithLifecycle()
    val activeReceipt by viewModel.activeReceipt.collectAsStateWithLifecycle()
    val showReportCardDetail by viewModel.showReportCardDetail.collectAsStateWithLifecycle()
    val selectedAssignment by viewModel.selectedAssignment.collectAsStateWithLifecycle()

    val isAuthScreen = currentDestination == AppDestination.AUTH

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("graziel_royal_main_scaffold"),
        topBar = {
            if (!isAuthScreen) {
                SchoolTopBar(
                    currentRole = currentRole,
                    onRoleChange = { viewModel.switchRole(it) },
                    onNotificationClick = {
                        when (currentRole) {
                            UserRole.ADMIN -> viewModel.navigateTo(AppDestination.ADMIN_DASHBOARD)
                            UserRole.TEACHER -> viewModel.navigateTo(AppDestination.TEACHER_PORTAL)
                            UserRole.STUDENT -> viewModel.navigateTo(AppDestination.STUDENT_PORTAL)
                            UserRole.PARENT -> viewModel.navigateTo(AppDestination.PARENT_PORTAL)
                            else -> viewModel.navigateTo(AppDestination.STUDENT_PORTAL)
                        }
                    },
                    onLogout = { viewModel.logout() }
                )
            }
        },
        bottomBar = {
            if (!isAuthScreen) {
                SchoolBottomNav(
                    currentDestination = currentDestination,
                    currentRole = currentRole,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        containerColor = DarkCanvas
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isAuthScreen) androidx.compose.foundation.layout.PaddingValues() else innerPadding)
        ) {
            Crossfade(
                targetState = currentDestination,
                label = "screen_crossfade"
            ) { destination ->
                when (destination) {
                    AppDestination.AUTH -> AuthScreen(viewModel = viewModel)
                    AppDestination.ADMIN_DASHBOARD -> AdminDashboardScreen(viewModel = viewModel)
                    AppDestination.TEACHER_PORTAL -> TeacherPortalScreen(viewModel = viewModel)
                    AppDestination.STUDENT_PORTAL -> StudentPortalScreen(viewModel = viewModel)
                    AppDestination.PARENT_PORTAL -> ParentPortalScreen(viewModel = viewModel)
                    AppDestination.CBT_STUDIO -> CbtStudioScreen(viewModel = viewModel)
                    AppDestination.CBT_EXAM -> CbtExamScreen(viewModel = viewModel)
                    AppDestination.GROUP_CHAT -> GroupChatScreen(viewModel = viewModel)
                    AppDestination.ACADEMICS -> AcademicsScreen(viewModel = viewModel)
                    AppDestination.FINANCE -> FinanceScreen(viewModel = viewModel)
                    AppDestination.SCHEDULE -> ScheduleScreen(viewModel = viewModel)
                    AppDestination.HOMEWORK -> HomeworkScreen(viewModel = viewModel)
                    AppDestination.ADMISSIONS -> AdmissionsScreen(viewModel = viewModel)
                    AppDestination.AI_TUTOR -> AiTutorScreen(viewModel = viewModel)
                }
            }

            // Interactive Dialogs & Modal Sheets
            selectedFeeToPay?.let { feeItem ->
                PaymentDialog(
                    feeItem = feeItem,
                    onDismiss = { viewModel.selectFeeToPay(null) },
                    onConfirmPayment = { item, method ->
                        viewModel.processFeePayment(item, method)
                    }
                )
            }

            activeReceipt?.let { receipt ->
                ReceiptDialog(
                    transaction = receipt,
                    onDismiss = { viewModel.dismissReceipt() }
                )
            }

            if (showReportCardDetail) {
                ReportCardSheet(
                    report = viewModel.getCurrentReportCard(),
                    onDismiss = { viewModel.setShowReportCardDetail(false) }
                )
            }

            selectedAssignment?.let { assignment ->
                AssignmentSubmitDialog(
                    assignment = assignment,
                    onDismiss = { viewModel.selectAssignment(null) },
                    onSubmit = { id, text ->
                        viewModel.submitAssignmentSolution(id, text)
                    }
                )
            }
        }
    }
}
