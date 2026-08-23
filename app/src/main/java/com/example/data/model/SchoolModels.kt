package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    ADMIN,
    TEACHER,
    STUDENT,
    PARENT,
    GUEST_PROSPECTIVE
}

data class UserAccount(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val role: UserRole,
    val regOrStaffId: String,
    val assignedClass: String, // e.g. "SS 1 Science", "All Classes", "JSS 2 Gold"
    val childName: String? = null,
    val childRegNumber: String? = null,
    val phone: String = "+234 816 620 5113",
    val titleOrDesignation: String = "",
    val isMuted: Boolean = false
)

@Entity(tableName = "staff_clock_records")
data class StaffClockRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val staffName: String,
    val staffId: String,
    val date: String,
    val clockInTime: String,
    val clockOutTime: String? = null,
    val status: String = "CLOCKED_IN" // "CLOCKED_IN", "CLOCKED_OUT"
)

@Entity(tableName = "cbt_tests")
data class CbtTest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val targetClass: String, // "SS 1 Science", "SS 2 Arts", "JSS 2 Gold", "All Classes"
    val durationMinutes: Int = 15,
    val totalMarks: Int = 20,
    val isLive: Boolean = false, // Teacher "Go Live" toggle!
    val isResultsPublished: Boolean = false, // Teacher "Publish Results" toggle!
    val createdByTeacher: String = "Mr. A. Adeleke",
    val instructions: String = "Answer all multiple-choice questions carefully. Time is monitored automatically.",
    val dateCreated: String = "22 Feb 2025"
)

@Entity(tableName = "cbt_questions")
data class CbtQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testId: Int,
    val questionNumber: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String, // "A", "B", "C", "D"
    val marks: Int = 2,
    val explanation: String = ""
)

@Entity(tableName = "cbt_submissions")
data class CbtSubmission(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testId: Int,
    val studentName: String,
    val studentReg: String,
    val studentClass: String,
    val score: Int,
    val maxScore: Int,
    val percentage: Double,
    val isReviewedByTeacher: Boolean = true,
    val teacherFeedback: String = "Good effort! Keep revising the key definitions.",
    val submissionDate: String,
    val answersJson: String = "" // "1:B,2:A,3:C..."
)

@Entity(tableName = "group_chat_messages")
data class GroupChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val channelId: String, // "class_ss1_science", "class_ss2_arts", "class_jss2_gold", "staff_room", "admin_broadcast"
    val channelTitle: String,
    val senderName: String,
    val senderRole: String, // "ADMIN", "TEACHER", "STUDENT"
    val senderId: String,
    val text: String,
    val attachmentType: String = "NONE", // "NONE", "IMAGE", "DOCUMENT", "PAST_QUESTION"
    val attachmentName: String = "",
    val timestamp: String,
    val isPinned: Boolean = false,
    val isDeleted: Boolean = false
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Academic", "Event", "Notice", "Cultural"
    val date: String,
    val summary: String,
    val isPinned: Boolean = false,
    val author: String = "School Administration",
    val targetAudience: String = "All" // "All", "Students", "Parents", "Staff"
)

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val teacher: String,
    val targetClass: String = "SS 1 Science",
    val dueDate: String,
    val description: String,
    val maxScore: Int = 20,
    val score: Int? = null,
    val isSubmitted: Boolean = false,
    val submissionText: String = "",
    val feedback: String = ""
)

@Entity(tableName = "fee_items")
data class FeeItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val term: String, // "2nd Term 2024/2025"
    val targetClass: String = "SS 1 Science",
    val amount: Double,
    val isPaid: Boolean,
    val dueDate: String,
    val category: String // "Tuition", "Development", "ICT & STEM", "Uniform", "Bus Transit"
)

@Entity(tableName = "payment_transactions")
data class PaymentTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val receiptNumber: String,
    val title: String,
    val amount: Double,
    val date: String,
    val paymentMethod: String, // "Card", "Bank Transfer", "Online Portal"
    val status: String = "SUCCESS",
    val studentName: String = "Adeleke David O.",
    val studentId: String = "GRS/2024/0428",
    val academicTerm: String = "2024/2025 Second Term"
)

@Entity(tableName = "admission_applications")
data class AdmissionApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val dateOfBirth: String,
    val gender: String,
    val classApplyingFor: String, // "Creche", "Nursery 1", "Primary 3", "JSS 1", "SS 1"
    val parentName: String,
    val parentPhone: String,
    val parentEmail: String,
    val address: String,
    val previousSchool: String,
    val submissionDate: String,
    val status: String = "Under Review" // "Under Review", "Interview Scheduled", "Accepted"
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String = "Adeleke David O.",
    val studentId: String = "GRS/2024/0428",
    val grade: String = "SS 1 Science",
    val date: String,
    val dayOfWeek: String,
    val status: String, // "PRESENT", "LATE", "EXCUSED", "ABSENT"
    val checkInTime: String = "07:45 AM",
    val notes: String = ""
)

data class SubjectGrade(
    val id: Int,
    val subjectName: String,
    val ca1: Int, // max 15
    val ca2: Int, // max 15
    val projectScore: Int, // max 10
    val examScore: Int, // max 60
    val totalScore: Int, // max 100
    val gradeLetter: String, // A1, B2, B3, C4, C5, C6, D7, E8, F9
    val remark: String,
    val position: Int,
    val teacherName: String
)

data class TermReport(
    val reportId: String = "TERM2_2025",
    val termName: String,
    val session: String,
    val studentName: String,
    val studentReg: String,
    val studentClass: String,
    val classPopulation: Int,
    val totalObtained: Int,
    val totalPossible: Int,
    val averageScore: Double,
    val classPosition: Int,
    val attendanceDays: Int,
    val totalDays: Int,
    val isApprovedByAdmin: Boolean = true,
    val isPublished: Boolean = true,
    val formTeacherRemark: String,
    val principalRemark: String,
    val grades: List<SubjectGrade>
)

data class TimetablePeriod(
    val periodNumber: Int,
    val timeRange: String,
    val subject: String,
    val teacher: String,
    val room: String,
    val isBreak: Boolean = false
)

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "Just now"
)
