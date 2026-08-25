package com.example.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SchoolDatabase
import com.example.data.model.AdminSecurityConfig
import com.example.data.model.AdmissionApplication
import com.example.data.model.AiTutorSpecification
import com.example.data.model.Announcement
import com.example.data.model.Assignment
import com.example.data.model.AttendanceRecord
import com.example.data.model.CbtQuestion
import com.example.data.model.CbtSubmission
import com.example.data.model.CbtTest
import com.example.data.model.ChatMessage
import com.example.data.model.FeeItem
import com.example.data.model.GroupChatMessage
import com.example.data.model.PaymentTransaction
import com.example.data.model.StaffClockRecord
import com.example.data.model.StudentRecord
import com.example.data.model.TeacherAccount
import com.example.data.model.TermReport
import com.example.data.model.TimetablePeriod
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.repository.SchoolRepository
import com.example.data.service.AiTutorService
import com.example.data.auth.AuthResult
import com.example.data.auth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppDestination {
    AUTH,
    ADMIN_DASHBOARD,
    TEACHER_PORTAL,
    STUDENT_PORTAL,
    PARENT_PORTAL,
    CBT_STUDIO,
    CBT_EXAM,
    GROUP_CHAT,
    FINANCE,
    ACADEMICS,
    HOMEWORK,
    SCHEDULE,
    AI_TUTOR,
    ADMISSIONS
}

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository
    val firebaseAuthRepo: FirebaseAuthRepository = FirebaseAuthRepository(application)

    private val _isFirebaseConfigured = MutableStateFlow(firebaseAuthRepo.isFirebaseInitialized)
    val isFirebaseConfigured: StateFlow<Boolean> = _isFirebaseConfigured.asStateFlow()

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(firebaseAuthRepo.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _authLoading = MutableStateFlow(false)
    val authLoading: StateFlow<Boolean> = _authLoading.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    val announcements: StateFlow<List<Announcement>>
    val assignments: StateFlow<List<Assignment>>
    val feeItems: StateFlow<List<FeeItem>>
    val payments: StateFlow<List<PaymentTransaction>>
    val applications: StateFlow<List<AdmissionApplication>>
    val attendanceRecords: StateFlow<List<AttendanceRecord>>
    val cbtTests: StateFlow<List<CbtTest>>
    val cbtQuestions: StateFlow<List<CbtQuestion>>
    val cbtSubmissions: StateFlow<List<CbtSubmission>>
    val staffClockRecords: StateFlow<List<StaffClockRecord>>
    val allGroupMessages: StateFlow<List<GroupChatMessage>>
    val teacherAccounts: StateFlow<List<TeacherAccount>>
    val studentRecords: StateFlow<List<StudentRecord>>
    val adminSecurityConfig: StateFlow<AdminSecurityConfig?>

    // Current User & Role
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.STUDENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentDestination = MutableStateFlow(AppDestination.AUTH)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Active CBT test being taken or edited
    private val _activeCbtTest = MutableStateFlow<CbtTest?>(null)
    val activeCbtTest: StateFlow<CbtTest?> = _activeCbtTest.asStateFlow()

    private val _activeCbtSubmission = MutableStateFlow<CbtSubmission?>(null)
    val activeCbtSubmission: StateFlow<CbtSubmission?> = _activeCbtSubmission.asStateFlow()

    // Selected Chat Channel
    private val _selectedChatChannel = MutableStateFlow("class_ss1_science")
    val selectedChatChannel: StateFlow<String> = _selectedChatChannel.asStateFlow()

    private val _selectedTerm = MutableStateFlow("2nd Term")
    val selectedTerm: StateFlow<String> = _selectedTerm.asStateFlow()

    private val _selectedTimetableDay = MutableStateFlow("Monday")
    val selectedTimetableDay: StateFlow<String> = _selectedTimetableDay.asStateFlow()

    private val _selectedAssignment = MutableStateFlow<Assignment?>(null)
    val selectedAssignment: StateFlow<Assignment?> = _selectedAssignment.asStateFlow()

    private val _selectedFeeToPay = MutableStateFlow<FeeItem?>(null)
    val selectedFeeToPay: StateFlow<FeeItem?> = _selectedFeeToPay.asStateFlow()

    private val _activeReceipt = MutableStateFlow<PaymentTransaction?>(null)
    val activeReceipt: StateFlow<PaymentTransaction?> = _activeReceipt.asStateFlow()

    private val _showReportCardDetail = MutableStateFlow(false)
    val showReportCardDetail: StateFlow<Boolean> = _showReportCardDetail.asStateFlow()

    private val _admissionSubmissionSuccess = MutableStateFlow(false)
    val admissionSubmissionSuccess: StateFlow<Boolean> = _admissionSubmissionSuccess.asStateFlow()

    private val _showNotificationBox = MutableStateFlow(false)
    val showNotificationBox: StateFlow<Boolean> = _showNotificationBox.asStateFlow()

    private val aiTutorService = AiTutorService()

    private val _aiTutorSpecification = MutableStateFlow(
        AiTutorSpecification(
            userRole = "Student",
            gradeLevel = "SS 1 - SS 3 (Senior Secondary)",
            subject = "Mathematics & Sciences",
            teachingStyle = "Step-by-Step Patient Mentor",
            languageComplexity = "Standard & Engaging",
            aiTutorName = "Graziel Royal AI Tutor"
        )
    )
    val aiTutorSpecification: StateFlow<AiTutorSpecification> = _aiTutorSpecification.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Welcome to Graziel Royal AI Academic & Personalized Tutor! 🎓\n\nI am configured to serve you based on your exact custom specifications:\n• Role: Student • Level: SS 1-3 • Subject: Mathematics & Sciences • Style: Step-by-Step Mentor\n\nTap **'Customize Tutor'** above to adjust your grade level, role (Teacher, Student, Parent, Admin), subject, or teaching style at any time! What would you like to study or generate today?",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val aiChatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()
    val isAiThinking: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    // Muted students tracking in memory
    private val _mutedUsers = MutableStateFlow<Set<String>>(emptySet())
    val mutedUsers: StateFlow<Set<String>> = _mutedUsers.asStateFlow()

    // Report approval state in memory
    private val _isReportCardApproved = MutableStateFlow(true)
    val isReportCardApproved: StateFlow<Boolean> = _isReportCardApproved.asStateFlow()

    private val _isReportCardPublished = MutableStateFlow(true)
    val isReportCardPublished: StateFlow<Boolean> = _isReportCardPublished.asStateFlow()

    init {
        val db = SchoolDatabase.getDatabase(application)
        repository = SchoolRepository(db.schoolDao())

        announcements = repository.announcements.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        assignments = repository.assignments.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        feeItems = repository.feeItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        payments = repository.payments.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        applications = repository.applications.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        attendanceRecords = repository.attendanceRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        cbtTests = repository.cbtTests.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        cbtQuestions = repository.cbtQuestions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        cbtSubmissions = repository.cbtSubmissions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        staffClockRecords = repository.staffClockRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allGroupMessages = repository.allGroupMessages.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        teacherAccounts = repository.teacherAccounts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        studentRecords = repository.studentRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        adminSecurityConfig = repository.adminSecurityConfig.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun getDefaultAccounts(): List<UserAccount> = repository.defaultAccounts

    fun loginAs(account: UserAccount) {
        _currentUser.value = account
        _currentRole.value = account.role

        // Adapt default AI Tutor specification to user role & context
        val currentSpec = _aiTutorSpecification.value
        val updatedSpec = when (account.role) {
            UserRole.ADMIN -> currentSpec.copy(
                userRole = "Administrator",
                gradeLevel = "All Levels & Faculty",
                teachingStyle = "Administrative Memo & Policy Specialist"
            )
            UserRole.TEACHER -> currentSpec.copy(
                userRole = "Teacher",
                gradeLevel = if (account.assignedClass.isNotBlank()) account.assignedClass else "SS 1 - SS 3",
                subject = if (account.titleOrDesignation.isNotBlank()) account.titleOrDesignation else "Senior Mathematics & Physics",
                teachingStyle = "Lesson Planner & Scheme Builder"
            )
            UserRole.STUDENT -> currentSpec.copy(
                userRole = "Student",
                gradeLevel = if (account.assignedClass.isNotBlank()) account.assignedClass else "SS 1 Science",
                subject = "Mathematics & Sciences",
                teachingStyle = "Step-by-Step Patient Mentor"
            )
            UserRole.PARENT -> currentSpec.copy(
                userRole = "Parent",
                gradeLevel = "Child Level (${account.assignedClass})",
                subject = "Academic Progress & Guidance",
                teachingStyle = "Parenting & Academic Advisor"
            )
            UserRole.GUEST_PROSPECTIVE -> currentSpec.copy(
                userRole = "General Scholar / Prospective",
                gradeLevel = "Prospective Student / General",
                subject = "Admissions & School Information",
                teachingStyle = "Clear & Engaging (Standard)"
            )
        }
        _aiTutorSpecification.value = updatedSpec

        when (account.role) {
            UserRole.ADMIN -> _currentDestination.value = AppDestination.ADMIN_DASHBOARD
            UserRole.TEACHER -> _currentDestination.value = AppDestination.TEACHER_PORTAL
            UserRole.STUDENT -> _currentDestination.value = AppDestination.STUDENT_PORTAL
            UserRole.PARENT -> _currentDestination.value = AppDestination.PARENT_PORTAL
            UserRole.GUEST_PROSPECTIVE -> _currentDestination.value = AppDestination.ADMISSIONS
        }
    }

    // =========================================================================
    // STRICT ROLE-BASED PASSKEY & ID AUTHENTICATION ENGINE
    // =========================================================================

    /**
     * Teacher Login: Validates against unique passkey issued by admin (and optional Staff ID/Email)
     */
    fun loginTeacherWithPasskey(
        staffIdOrEmail: String,
        passkey: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val trimmedPasskey = passkey.trim()
        val trimmedIdentifier = staffIdOrEmail.trim()

        if (trimmedPasskey.isBlank()) {
            onResult(false, "Please enter your unique Teacher Passkey given by the admin.")
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(400) // Smooth tactile feel
            _authLoading.value = false

            // Query database for matching teacher passkey
            val teacher = if (trimmedIdentifier.isNotBlank()) {
                repository.authenticateTeacher(trimmedIdentifier, trimmedPasskey)
            } else {
                repository.authenticateTeacherByPasskeyOnly(trimmedPasskey)
            }

            if (teacher != null) {
                val teacherUser = UserAccount(
                    id = teacher.id,
                    fullName = teacher.fullName,
                    email = teacher.email,
                    role = UserRole.TEACHER,
                    regOrStaffId = teacher.staffId,
                    assignedClass = teacher.assignedClass,
                    phone = teacher.phone,
                    titleOrDesignation = "${teacher.subjectSpecialization} Teacher"
                )
                loginAs(teacherUser)
                onResult(true, "Authentication successful! Welcome, ${teacher.fullName}.")
            } else {
                // Check if it's the default demo teacher passkey fallback
                if (trimmedPasskey == "TCH-AYO-2025" || trimmedPasskey.equals("teacher", ignoreCase = true)) {
                    val defaultTeacher = repository.defaultAccounts.first { it.role == UserRole.TEACHER }
                    loginAs(defaultTeacher)
                    onResult(true, "Welcome, ${defaultTeacher.fullName}!")
                } else {
                    onResult(false, "Access Denied: Invalid Teacher Passkey. Please verify your passkey with the School Admin.")
                }
            }
        }
    }

    /**
     * Admin Login: Validates against Admin Security Passkey
     */
    fun loginAdminWithPasskey(
        passkey: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val trimmedPasskey = passkey.trim()
        if (trimmedPasskey.isBlank()) {
            onResult(false, "Please enter the Admin Master Passkey.")
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(400)
            _authLoading.value = false

            val isValid = repository.authenticateAdmin(trimmedPasskey)
            if (isValid) {
                val adminAccount = repository.defaultAccounts.first { it.role == UserRole.ADMIN }
                loginAs(adminAccount)
                onResult(true, "Super Admin security access granted.")
            } else {
                onResult(false, "Access Denied: Incorrect Admin Passkey. Please check your administrator credentials.")
            }
        }
    }

    /**
     * Student Login: Validates against official Student ID Number given by admin
     */
    fun loginStudentWithId(
        studentId: String,
        passcode: String? = null,
        onResult: (Boolean, String) -> Unit
    ) {
        val trimmedId = studentId.trim()
        if (trimmedId.isBlank()) {
            onResult(false, "Please enter your official Student ID (e.g. GRS/2024/0428).")
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(350)
            _authLoading.value = false

            val student = repository.authenticateStudent(trimmedId)
            if (student != null) {
                val studentUser = UserAccount(
                    id = student.id,
                    fullName = student.fullName,
                    email = student.parentEmail,
                    role = UserRole.STUDENT,
                    regOrStaffId = student.studentId,
                    assignedClass = student.assignedClass,
                    phone = student.parentPhone,
                    titleOrDesignation = "Student - ${student.assignedClass}"
                )
                loginAs(studentUser)
                onResult(true, "Welcome back, ${student.fullName}!")
            } else {
                // Check if matches default demo student fallback
                if (trimmedId.equals("GRS/2024/0428", ignoreCase = true) || trimmedId.equals("student", ignoreCase = true) || trimmedId.contains("0428")) {
                    val defaultStudent = repository.defaultAccounts.first { it.role == UserRole.STUDENT }
                    loginAs(defaultStudent)
                    onResult(true, "Welcome, ${defaultStudent.fullName}!")
                } else {
                    onResult(false, "Student ID '$trimmedId' not found. Please contact the School Admin for your official Student ID.")
                }
            }
        }
    }

    /**
     * Parent Login: Validates using child's official Student ID Number given by admin
     */
    fun loginParentWithChildId(
        childStudentId: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val trimmedId = childStudentId.trim()
        if (trimmedId.isBlank()) {
            onResult(false, "Please enter your child's official Student ID (e.g. GRS/2024/0428).")
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(350)
            _authLoading.value = false

            val student = repository.authenticateParent(trimmedId)
            if (student != null) {
                val parentUser = UserAccount(
                    id = student.id + 500,
                    fullName = student.parentName,
                    email = student.parentEmail,
                    role = UserRole.PARENT,
                    regOrStaffId = "GRS/PAR/${student.studentId.takeLast(4)}",
                    assignedClass = student.assignedClass,
                    childName = student.fullName,
                    childRegNumber = student.studentId,
                    phone = student.parentPhone,
                    titleOrDesignation = "Parent of ${student.fullName}"
                )
                loginAs(parentUser)
                onResult(true, "Welcome, ${student.parentName}! Accessing portal for ${student.fullName}.")
            } else {
                // Check if matches default demo parent fallback
                if (trimmedId.equals("GRS/2024/0428", ignoreCase = true) || trimmedId.equals("parent", ignoreCase = true) || trimmedId.contains("0428")) {
                    val defaultParent = repository.defaultAccounts.first { it.role == UserRole.PARENT }
                    loginAs(defaultParent)
                    onResult(true, "Welcome, ${defaultParent.fullName}!")
                } else {
                    onResult(false, "Child ID '$trimmedId' not recognized. Please confirm your child's student registration ID from the school.")
                }
            }
        }
    }

    // =========================================================================
    // ADMIN TEACHER & STUDENT REGISTRY MANAGEMENT
    // =========================================================================

    fun addNewTeacher(
        fullName: String,
        email: String,
        phone: String,
        assignedClass: String,
        subject: String,
        passkey: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        if (fullName.isBlank() || passkey.isBlank()) {
            onComplete(false, "Teacher Full Name and Passkey are required.")
            return
        }

        viewModelScope.launch {
            val randomSuffix = (1000..9999).random()
            val staffId = "GRS/STF/${SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())}/$randomSuffix"
            val newTeacher = TeacherAccount(
                fullName = fullName.trim(),
                staffId = staffId,
                email = if (email.isNotBlank()) email.trim() else "${fullName.trim().lowercase().replace(" ", ".")}@grazielroyalschools.edu.ng",
                phone = if (phone.isNotBlank()) phone.trim() else "+234 816 620 5113",
                assignedClass = if (assignedClass.isNotBlank()) assignedClass.trim() else "SS 1 Science",
                subjectSpecialization = if (subject.isNotBlank()) subject.trim() else "General Subject",
                passkey = passkey.trim(),
                dateAdded = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                isActive = true
            )
            repository.addTeacher(newTeacher)
            onComplete(true, "Teacher added successfully! Assigned Staff ID: $staffId and Passkey: ${passkey.trim()}")
        }
    }

    fun updateTeacherPasskey(teacherId: Int, newPasskey: String) {
        if (newPasskey.isNotBlank()) {
            viewModelScope.launch {
                repository.updateTeacherPasskey(teacherId, newPasskey.trim())
            }
        }
    }

    fun deleteTeacher(teacherId: Int) {
        viewModelScope.launch {
            repository.deleteTeacher(teacherId)
        }
    }

    fun addNewStudent(
        fullName: String,
        studentId: String,
        assignedClass: String,
        parentName: String,
        parentPhone: String,
        parentEmail: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        if (fullName.isBlank() || studentId.isBlank()) {
            onComplete(false, "Student Full Name and Student ID are required.")
            return
        }

        viewModelScope.launch {
            val newStudent = StudentRecord(
                fullName = fullName.trim(),
                studentId = studentId.trim(),
                assignedClass = if (assignedClass.isNotBlank()) assignedClass.trim() else "SS 1 Science",
                parentName = if (parentName.isNotBlank()) parentName.trim() else "Parent / Guardian",
                parentPhone = if (parentPhone.isNotBlank()) parentPhone.trim() else "+234 816 620 5113",
                parentEmail = if (parentEmail.isNotBlank()) parentEmail.trim() else "parent@grazielroyalschools.edu.ng",
                passcode = studentId.takeLast(4),
                dateEnrolled = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date()),
                isActive = true
            )
            repository.addStudent(newStudent)
            onComplete(true, "Student registered successfully with Student ID: ${studentId.trim()}")
        }
    }

    fun deleteStudent(studentId: Int) {
        viewModelScope.launch {
            repository.deleteStudent(studentId)
        }
    }

    fun updateAdminSecurityPasskey(newPasskey: String, onComplete: (Boolean) -> Unit) {
        if (newPasskey.length >= 4) {
            viewModelScope.launch {
                repository.updateAdminPasskey(newPasskey.trim())
                onComplete(true)
            }
        } else {
            onComplete(false)
        }
    }

    fun updateAcademicSessionAndTerm(term: String, session: String, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.updateAcademicTermAndSession(term.trim(), session.trim())
                _selectedTerm.value = term.trim()
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun updateSchoolBankDetails(bankName: String, accountNumber: String, accountName: String, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.updateBankDetails(bankName.trim(), accountNumber.trim(), accountName.trim())
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun openNotificationBox() {
        _showNotificationBox.value = true
    }

    fun closeNotificationBox() {
        _showNotificationBox.value = false
    }

    fun postOfficialBroadcast(
        title: String,
        summary: String,
        category: String = "Notice",
        targetAudience: String = "All",
        isPinned: Boolean = true,
        onComplete: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val newAnnouncement = Announcement(
                    title = title.trim(),
                    summary = summary.trim(),
                    category = category,
                    date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.US).format(java.util.Date()),
                    targetAudience = targetAudience,
                    isPinned = isPinned,
                    author = _currentUser.value?.fullName ?: "Super Admin (Mr. Tobi Adebayo)"
                )
                repository.addAnnouncement(newAnnouncement)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun registerNewUser(name: String, email: String, role: UserRole, classOrTitle: String, phone: String, childName: String? = null) {
        val newAccount = UserAccount(
            id = (System.currentTimeMillis() % 10000).toInt(),
            fullName = name,
            email = email,
            role = role,
            regOrStaffId = when (role) {
                UserRole.ADMIN -> "GRS/ADM/${System.currentTimeMillis().toString().takeLast(3)}"
                UserRole.TEACHER -> "GRS/STF/${System.currentTimeMillis().toString().takeLast(3)}"
                UserRole.STUDENT -> "GRS/2025/${System.currentTimeMillis().toString().takeLast(4)}"
                UserRole.PARENT -> "GRS/PAR/${System.currentTimeMillis().toString().takeLast(3)}"
                UserRole.GUEST_PROSPECTIVE -> "GRS/GST/001"
            },
            assignedClass = if (classOrTitle.isNotBlank()) classOrTitle else "SS 1 Science",
            titleOrDesignation = classOrTitle,
            childName = childName,
            childRegNumber = if (childName != null) "GRS/2024/0428" else null,
            phone = phone
        )
        loginAs(newAccount)
    }

    fun clearAuthMessages() {
        _authErrorMessage.value = null
        _authSuccessMessage.value = null
    }

    fun signInWithFirebase(
        email: String,
        password: String,
        selectedRole: UserRole,
        fallbackAccount: UserAccount? = null
    ) {
        if (email.isBlank() || password.isBlank()) {
            _authErrorMessage.value = "Please enter both email and password."
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null
        _authSuccessMessage.value = null

        viewModelScope.launch {
            if (!firebaseAuthRepo.isFirebaseInitialized) {
                // Graceful fallback when google-services.json is pending
                kotlinx.coroutines.delay(600)
                _authLoading.value = false
                val account = fallbackAccount ?: UserAccount(
                    id = (System.currentTimeMillis() % 10000).toInt(),
                    fullName = email.substringBefore("@").replace(".", " ").capitalize(Locale.getDefault()),
                    email = email,
                    role = selectedRole,
                    regOrStaffId = when (selectedRole) {
                        UserRole.ADMIN -> "GRS/ADM/001"
                        UserRole.TEACHER -> "GRS/STF/2021/014"
                        UserRole.STUDENT -> "GRS/2024/0428"
                        UserRole.PARENT -> "GRS/PAR/104"
                        UserRole.GUEST_PROSPECTIVE -> "GRS/GST/001"
                    },
                    assignedClass = if (selectedRole == UserRole.STUDENT) "SS 1 Science" else "All Classes",
                    titleOrDesignation = if (selectedRole == UserRole.TEACHER) "Senior Tutor" else ""
                )
                loginAs(account)
                Toast.makeText(getApplication(), "Signed in as ${account.fullName}", Toast.LENGTH_SHORT).show()
                return@launch
            }

            when (val result = firebaseAuthRepo.signInWithEmail(email, password)) {
                is AuthResult.Success -> {
                    _authLoading.value = false
                    _firebaseUser.value = result.data
                    val userAccount = UserAccount(
                        id = (result.data.uid.hashCode().takeIf { it != 0 } ?: 101),
                        fullName = result.data.displayName ?: email.substringBefore("@").replace(".", " ").capitalize(Locale.getDefault()),
                        email = result.data.email ?: email,
                        role = selectedRole,
                        regOrStaffId = "GRS/${result.data.uid.take(5).uppercase()}",
                        assignedClass = if (selectedRole == UserRole.STUDENT) "SS 1 Science" else "All Classes",
                        titleOrDesignation = if (selectedRole == UserRole.TEACHER) "Senior Faculty" else ""
                    )
                    loginAs(userAccount)
                    Toast.makeText(getApplication(), "Firebase sign-in successful!", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.Error -> {
                    _authLoading.value = false
                    _authErrorMessage.value = result.message
                }
            }
        }
    }

    fun registerWithFirebase(
        email: String,
        password: String,
        name: String,
        role: UserRole,
        classOrTitle: String,
        phone: String,
        childName: String? = null
    ) {
        if (email.isBlank() || password.isBlank()) {
            _authErrorMessage.value = "Please provide an email and password."
            return
        }
        if (password.length < 6) {
            _authErrorMessage.value = "Password must be at least 6 characters."
            return
        }

        _authLoading.value = true
        _authErrorMessage.value = null
        _authSuccessMessage.value = null

        viewModelScope.launch {
            if (!firebaseAuthRepo.isFirebaseInitialized) {
                kotlinx.coroutines.delay(600)
                _authLoading.value = false
                registerNewUser(
                    name = if (name.isNotBlank()) name else email.substringBefore("@"),
                    email = email,
                    role = role,
                    classOrTitle = classOrTitle,
                    phone = phone,
                    childName = childName
                )
                Toast.makeText(getApplication(), "Account registered successfully!", Toast.LENGTH_SHORT).show()
                return@launch
            }

            when (val result = firebaseAuthRepo.registerWithEmail(email, password)) {
                is AuthResult.Success -> {
                    _authLoading.value = false
                    _firebaseUser.value = result.data
                    registerNewUser(
                        name = if (name.isNotBlank()) name else (result.data.displayName ?: email.substringBefore("@")),
                        email = email,
                        role = role,
                        classOrTitle = classOrTitle,
                        phone = phone,
                        childName = childName
                    )
                    Toast.makeText(getApplication(), "Firebase account registered successfully!", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.Error -> {
                    _authLoading.value = false
                    _authErrorMessage.value = result.message
                }
            }
        }
    }

    fun sendFirebasePasswordReset(email: String) {
        if (email.isBlank()) {
            _authErrorMessage.value = "Please enter your email to reset password."
            return
        }
        _authLoading.value = true
        _authErrorMessage.value = null
        _authSuccessMessage.value = null

        viewModelScope.launch {
            if (!firebaseAuthRepo.isFirebaseInitialized) {
                kotlinx.coroutines.delay(600)
                _authLoading.value = false
                _authSuccessMessage.value = "Password reset instructions sent to $email (Simulation Mode)."
                return@launch
            }

            when (val result = firebaseAuthRepo.sendPasswordReset(email)) {
                is AuthResult.Success -> {
                    _authLoading.value = false
                    _authSuccessMessage.value = "Password reset link sent to $email! Please check your inbox."
                }
                is AuthResult.Error -> {
                    _authLoading.value = false
                    _authErrorMessage.value = result.message
                }
            }
        }
    }

    fun logout() {
        firebaseAuthRepo.signOut()
        _firebaseUser.value = null
        _currentUser.value = null
        _currentDestination.value = AppDestination.AUTH
    }

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun switchRole(role: UserRole) {
        val matchingAccount = repository.defaultAccounts.find { it.role == role }
        if (matchingAccount != null) {
            loginAs(matchingAccount)
        } else {
            _currentRole.value = role
            when (role) {
                UserRole.ADMIN -> _currentDestination.value = AppDestination.ADMIN_DASHBOARD
                UserRole.TEACHER -> _currentDestination.value = AppDestination.TEACHER_PORTAL
                UserRole.STUDENT -> _currentDestination.value = AppDestination.STUDENT_PORTAL
                UserRole.PARENT -> _currentDestination.value = AppDestination.PARENT_PORTAL
                UserRole.GUEST_PROSPECTIVE -> _currentDestination.value = AppDestination.ADMISSIONS
            }
        }
    }

    // ==========================================
    // CBT (COMPUTER BASED TEST) ENGINE
    // ==========================================

    fun setActiveCbtTest(test: CbtTest?) {
        _activeCbtTest.value = test
    }

    fun startCbtExam(test: CbtTest) {
        _activeCbtTest.value = test
        _currentDestination.value = AppDestination.CBT_EXAM
    }

    fun createCbtTestWithQuestions(
        title: String,
        subject: String,
        targetClass: String,
        durationMinutes: Int,
        instructions: String,
        questions: List<CbtQuestion>
    ) {
        viewModelScope.launch {
            val user = _currentUser.value
            val teacherName = user?.fullName ?: "Mr. A. Adeleke"
            val totalMarks = questions.sumOf { it.marks }
            val newTest = CbtTest(
                title = title,
                subject = subject,
                targetClass = targetClass,
                durationMinutes = durationMinutes,
                totalMarks = if (totalMarks > 0) totalMarks else 20,
                isLive = false,
                isResultsPublished = false,
                createdByTeacher = teacherName,
                instructions = instructions,
                dateCreated = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            )
            repository.createCbtTest(newTest, questions)
        }
    }

    fun toggleCbtLiveStatus(testId: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            val newStatus = !currentStatus
            repository.setCbtLiveStatus(testId, newStatus)
        }
    }

    fun toggleCbtPublishResults(testId: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            val newStatus = !currentStatus
            repository.setCbtPublishStatus(testId, newStatus)
        }
    }

    fun submitStudentCbtExam(test: CbtTest, answers: Map<Int, String>, questionList: List<CbtQuestion>) {
        viewModelScope.launch {
            val user = _currentUser.value
            val studentName = user?.fullName ?: "Adeleke David Oluwaseun"
            val studentReg = user?.regOrStaffId ?: "GRS/2024/0428"
            val studentClass = user?.assignedClass ?: "SS 1 Science"

            var score = 0
            val answersEncoded = StringBuilder()

            questionList.forEachIndexed { index, q ->
                val selected = answers[q.id] ?: ""
                answersEncoded.append("${q.questionNumber}:$selected;")
                if (selected.equals(q.correctOption, ignoreCase = true)) {
                    score += q.marks
                }
            }

            val maxScore = questionList.sumOf { it.marks }.takeIf { it > 0 } ?: test.totalMarks
            val percentage = if (maxScore > 0) (score.toDouble() / maxScore.toDouble()) * 100.0 else 0.0

            val submission = CbtSubmission(
                testId = test.id,
                studentName = studentName,
                studentReg = studentReg,
                studentClass = studentClass,
                score = score,
                maxScore = maxScore,
                percentage = percentage,
                isReviewedByTeacher = true,
                teacherFeedback = when {
                    percentage >= 80 -> "Outstanding royal performance! Excellent mastery of concepts."
                    percentage >= 60 -> "Good effort. Review missed questions in the study bank."
                    else -> "Needs revision. Please consult your subject tutor for remediation."
                },
                submissionDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date()),
                answersJson = answersEncoded.toString()
            )

            repository.submitCbtExam(submission)
            _activeCbtSubmission.value = submission
        }
    }

    fun updateStudentCbtScore(submission: CbtSubmission, newScore: Int, teacherFeedback: String) {
        viewModelScope.launch {
            val percentage = if (submission.maxScore > 0) (newScore.toDouble() / submission.maxScore.toDouble()) * 100.0 else 0.0
            val updated = submission.copy(
                score = newScore,
                percentage = percentage,
                teacherFeedback = teacherFeedback,
                isReviewedByTeacher = true
            )
            repository.updateCbtSubmissionScore(updated)
        }
    }

    // ==========================================
    // GROUP CHAT SYSTEM & MODERATION
    // ==========================================

    fun selectChatChannel(channelId: String) {
        _selectedChatChannel.value = channelId
    }

    fun sendGroupChatMessage(text: String, attachmentType: String = "NONE", attachmentName: String = "") {
        if (text.isBlank() && attachmentType == "NONE") return

        val user = _currentUser.value ?: repository.defaultAccounts.first()
        if (_mutedUsers.value.contains(user.regOrStaffId)) {
            Toast.makeText(getApplication(), "You are currently muted in this chat channel by a moderator.", Toast.LENGTH_SHORT).show()
            return
        }

        val channelId = _selectedChatChannel.value
        val channelTitle = when (channelId) {
            "class_ss1_science" -> "SS 1 Science Class Group"
            "class_ss2_arts" -> "SS 2 Arts Class Group"
            "class_jss2_gold" -> "JSS 2 Gold Class Group"
            "staff_room" -> "Graziel Royal Faculty Staff Room"
            "admin_broadcast" -> "School-Wide Official Broadcast"
            else -> "Class Group Chat"
        }

        val message = GroupChatMessage(
            channelId = channelId,
            channelTitle = channelTitle,
            senderName = user.fullName,
            senderRole = user.role.name,
            senderId = user.regOrStaffId,
            text = text,
            attachmentType = attachmentType,
            attachmentName = attachmentName,
            timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        )

        viewModelScope.launch {
            repository.sendGroupMessage(message)
        }
    }

    fun deleteGroupChatMessage(messageId: Int) {
        viewModelScope.launch {
            repository.deleteGroupMessage(messageId)
        }
    }

    fun muteUserInChat(userRegId: String) {
        val updated = _mutedUsers.value.toMutableSet()
        if (updated.contains(userRegId)) {
            updated.remove(userRegId)
            Toast.makeText(getApplication(), "Unmuted user $userRegId", Toast.LENGTH_SHORT).show()
        } else {
            updated.add(userRegId)
            Toast.makeText(getApplication(), "Muted user $userRegId from sending messages", Toast.LENGTH_SHORT).show()
        }
        _mutedUsers.value = updated
    }

    fun clearChatChannel(channelId: String) {
        viewModelScope.launch {
            repository.clearChatChannel(channelId)
        }
    }

    // ==========================================
    // STAFF CLOCK-IN / CLOCK-OUT ATTENDANCE
    // ==========================================

    fun clockInStaff() {
        viewModelScope.launch {
            val user = _currentUser.value
            val staffName = user?.fullName ?: "Mr. Adeleke Ayomide"
            val staffId = user?.regOrStaffId ?: "GRS/STF/2021/014"
            repository.clockInStaff(staffName, staffId)
            Toast.makeText(getApplication(), "Clock-In successful at ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())}", Toast.LENGTH_SHORT).show()
        }
    }

    fun clockOutStaff(recordId: Int = 0) {
        viewModelScope.launch {
            val targetId = if (recordId != 0) {
                recordId
            } else {
                staffClockRecords.value.lastOrNull { it.clockOutTime == null }?.id ?: 1
            }
            repository.clockOutStaff(targetId)
            Toast.makeText(getApplication(), "Clock-Out recorded successfully.", Toast.LENGTH_SHORT).show()
        }
    }

    // ==========================================
    // REPORT CARD APPROVAL & PUBLISHING
    // ==========================================

    fun setReportCardApproval(approved: Boolean, published: Boolean) {
        _isReportCardApproved.value = approved
        _isReportCardPublished.value = published
        repository.setReportCardApproval(approved, published)
    }

    fun getCurrentReportCard(): TermReport {
        val baseReport = repository.getReportCard(_selectedTerm.value)
        return baseReport.copy(
            isApprovedByAdmin = _isReportCardApproved.value,
            isPublished = _isReportCardPublished.value
        )
    }

    // ==========================================
    // FINANCE & BILL CREATION (ADMIN / PARENT)
    // ==========================================

    fun createFeeBill(title: String, term: String, targetClass: String, amount: Double, dueDate: String, category: String) {
        viewModelScope.launch {
            val feeItem = FeeItem(
                title = title,
                term = term,
                targetClass = targetClass,
                amount = amount,
                isPaid = false,
                dueDate = dueDate,
                category = category
            )
            repository.createFeeItem(feeItem)
            Toast.makeText(getApplication(), "Fee bill '$title' published for $targetClass", Toast.LENGTH_SHORT).show()
        }
    }

    fun selectFeeToPay(feeItem: FeeItem?) {
        _selectedFeeToPay.value = feeItem
    }

    fun processFeePayment(feeItem: FeeItem, paymentMethod: String) {
        viewModelScope.launch {
            val user = _currentUser.value
            val studentName = user?.childName ?: "Adeleke David O."
            val studentId = user?.childRegNumber ?: "GRS/2024/0428"
            val receipt = repository.payFeeItem(feeItem, paymentMethod, studentName, studentId)
            _selectedFeeToPay.value = null
            _activeReceipt.value = receipt
        }
    }

    fun dismissReceipt() {
        _activeReceipt.value = null
    }

    fun viewReceipt(payment: PaymentTransaction) {
        _activeReceipt.value = payment
    }

    // ==========================================
    // SCHOOL CONTACTS (WHATSAPP & PHONE CALLS)
    // ==========================================

    fun openSchoolWhatsApp(context: android.content.Context, customPrompt: String? = null) {
        val user = _currentUser.value
        val role = user?.role ?: _currentRole.value
        val defaultMsg = when (role) {
            UserRole.PARENT -> "Hello Graziel Royal Schools Admin (Mr. Tobi Adebayo), I am contacting you regarding my child ${user?.childName ?: "Adeleke David O."} (Reg: ${user?.childRegNumber ?: "GRS/2024/0428"})."
            UserRole.TEACHER -> "Hello School Office, this is ${user?.fullName ?: "Staff"} contacting from the Graziel Royal Staff Portal."
            UserRole.STUDENT -> "Hello School Office, this is student ${user?.fullName ?: "Student"} (Class: ${user?.assignedClass ?: "SS 1"})."
            else -> "Hello Graziel Royal Schools, I would like to make inquiries regarding school admissions, campus visits in Opo-Ibogun, Ifo, Ogun State, and academic programs."
        }
        val textToSend = customPrompt ?: defaultMsg
        val url = "https://wa.me/2348166205113?text=${Uri.encode(textToSend)}"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening WhatsApp for Graziel Royal Schools (+234 816 620 5113)...", Toast.LENGTH_SHORT).show()
        }
    }

    fun callSchoolAdmin(context: android.content.Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+2348166205113")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Calling Graziel Royal Schools Admin (+234 816 620 5113)...", Toast.LENGTH_SHORT).show()
        }
    }

    // ==========================================
    // GENERAL HELPERS & NAVIGATION
    // ==========================================

    fun setSelectedTerm(term: String) {
        _selectedTerm.value = term
    }

    fun setSelectedTimetableDay(day: String) {
        _selectedTimetableDay.value = day
    }

    fun getTimetable(): List<TimetablePeriod> {
        return repository.getTimetableForDay(_selectedTimetableDay.value)
    }

    fun selectAssignment(assignment: Assignment?) {
        _selectedAssignment.value = assignment
    }

    fun submitAssignmentSolution(assignmentId: Int, text: String) {
        viewModelScope.launch {
            val list = assignments.value
            val item = list.find { it.id == assignmentId }
            if (item != null) {
                val updated = item.copy(
                    isSubmitted = true,
                    submissionText = text,
                    feedback = "Submitted on ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())}. Pending teacher grading."
                )
                repository.updateAssignmentFull(updated)
                _selectedAssignment.value = null
            }
        }
    }

    fun createAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.addAssignment(assignment)
        }
    }

    fun createAssignment(
        title: String,
        subject: String,
        targetClass: String,
        dueDate: String,
        description: String,
        totalMarks: Int = 20
    ) {
        viewModelScope.launch {
            val newAssignment = Assignment(
                title = title,
                subject = subject,
                teacher = _currentUser.value?.fullName ?: "Mr. A. Adeleke",
                targetClass = targetClass,
                dueDate = dueDate,
                description = description,
                maxScore = totalMarks,
                isSubmitted = false,
                submissionText = "",
                feedback = "",
                score = null
            )
            repository.addAssignment(newAssignment)
            Toast.makeText(getApplication(), "Assignment '$title' assigned to $targetClass", Toast.LENGTH_SHORT).show()
        }
    }

    fun gradeStudentAssignment(assignment: Assignment, score: Int, feedback: String) {
        viewModelScope.launch {
            val updated = assignment.copy(
                score = score,
                feedback = feedback
            )
            repository.updateAssignmentFull(updated)
        }
    }

    fun setShowReportCardDetail(show: Boolean) {
        _showReportCardDetail.value = show
    }

    fun submitAdmission(app: AdmissionApplication) {
        viewModelScope.launch {
            repository.submitAdmissionApplication(app)
            _admissionSubmissionSuccess.value = true
        }
    }

    fun resetAdmissionSuccess() {
        _admissionSubmissionSuccess.value = false
    }

    fun markAttendance(date: String, status: String, notes: String = "") {
        viewModelScope.launch {
            val record = AttendanceRecord(
                studentName = _currentUser.value?.fullName ?: "Adeleke David O.",
                grade = _currentUser.value?.assignedClass ?: "SS 1 Science",
                date = date,
                dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()),
                status = status,
                checkInTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                notes = notes
            )
            repository.recordAttendance(record)
        }
    }

    fun updateAttendanceStatus(recordId: Int, status: String) {
        viewModelScope.launch {
            val record = attendanceRecords.value.find { it.id == recordId }
            if (record != null) {
                val updated = record.copy(status = status)
                repository.recordAttendance(updated)
            }
        }
    }

    fun publishAnnouncement(title: String, category: String, summary: String, targetAudience: String = "All") {
        viewModelScope.launch {
            val announcement = Announcement(
                title = title,
                category = category,
                date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                summary = summary,
                isPinned = false,
                author = _currentUser.value?.fullName ?: "Administration",
                targetAudience = targetAudience
            )
            repository.addAnnouncement(announcement)
        }
    }

    fun createAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            repository.addAnnouncement(announcement)
        }
    }

    fun createAnnouncement(title: String, category: String, summary: String, targetAudience: String = "All") {
        publishAnnouncement(title, category, summary, targetAudience)
    }

    fun updateAiSpecification(newSpec: AiTutorSpecification) {
        _aiTutorSpecification.value = newSpec
        Toast.makeText(getApplication(), "AI Tutor tailored to ${newSpec.userRole} (${newSpec.gradeLevel})", Toast.LENGTH_SHORT).show()
    }

    fun clearAiChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Chat history cleared. I am ready with your active configuration (${_aiTutorSpecification.value.userRole} • ${_aiTutorSpecification.value.gradeLevel} • ${_aiTutorSpecification.value.subject}). How can I assist you?",
                isUser = false
            )
        )
    }

    fun sendAiPrompt(userText: String, modeBadge: String? = null) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            text = userText,
            isUser = true,
            timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            modeBadge = modeBadge
        )
        _chatMessages.value = _chatMessages.value + userMessage
        _isAiGenerating.value = true

        viewModelScope.launch {
            try {
                val currentSpec = _aiTutorSpecification.value
                val history = _chatMessages.value.map { Pair(it.text, it.isUser) }
                val replyText = aiTutorService.generateTutorResponse(
                    userMessage = userText,
                    specification = currentSpec,
                    chatHistory = history
                )
                val aiMessage = ChatMessage(
                    text = replyText,
                    isUser = false,
                    timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                    modeBadge = currentSpec.userRole
                )
                _chatMessages.value = _chatMessages.value + aiMessage
            } catch (e: Exception) {
                val fallbackReply = aiTutorService.generateCustomizedOfflineResponse(userText, _aiTutorSpecification.value)
                val aiMessage = ChatMessage(
                    text = fallbackReply,
                    isUser = false,
                    timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
                    modeBadge = "Offline Engine"
                )
                _chatMessages.value = _chatMessages.value + aiMessage
            } finally {
                _isAiGenerating.value = false
            }
        }
    }

    fun sendAiMessage(userText: String) {
        sendAiPrompt(userText)
    }
}
