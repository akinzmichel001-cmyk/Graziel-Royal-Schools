package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdminSecurityConfig
import com.example.data.model.AdmissionApplication
import com.example.data.model.Announcement
import com.example.data.model.Assignment
import com.example.data.model.AttendanceRecord
import com.example.data.model.CbtQuestion
import com.example.data.model.CbtSubmission
import com.example.data.model.CbtTest
import com.example.data.model.FeeItem
import com.example.data.model.GroupChatMessage
import com.example.data.model.PaymentTransaction
import com.example.data.model.StaffClockRecord
import com.example.data.model.StudentRecord
import com.example.data.model.TeacherAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    // Teacher Accounts & Passkeys
    @Query("SELECT * FROM teacher_accounts ORDER BY fullName ASC")
    fun getAllTeacherAccounts(): Flow<List<TeacherAccount>>

    @Query("SELECT * FROM teacher_accounts WHERE staffId = :staffId OR email = :email LIMIT 1")
    suspend fun getTeacherByStaffIdOrEmail(staffId: String, email: String): TeacherAccount?

    @Query("SELECT * FROM teacher_accounts WHERE (staffId = :query OR email = :query) AND passkey = :passkey AND isActive = 1 LIMIT 1")
    suspend fun authenticateTeacher(query: String, passkey: String): TeacherAccount?

    @Query("SELECT * FROM teacher_accounts WHERE passkey = :passkey AND isActive = 1 LIMIT 1")
    suspend fun findTeacherByPasskey(passkey: String): TeacherAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherAccount(teacher: TeacherAccount): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacherAccounts(teachers: List<TeacherAccount>)

    @Update
    suspend fun updateTeacherAccount(teacher: TeacherAccount)

    @Query("UPDATE teacher_accounts SET passkey = :newPasskey WHERE id = :id")
    suspend fun updateTeacherPasskey(id: Int, newPasskey: String)

    @Query("DELETE FROM teacher_accounts WHERE id = :id")
    suspend fun deleteTeacherAccount(id: Int)

    // Student & Parent Records
    @Query("SELECT * FROM student_records ORDER BY fullName ASC")
    fun getAllStudentRecords(): Flow<List<StudentRecord>>

    @Query("SELECT * FROM student_records WHERE studentId = :studentId LIMIT 1")
    suspend fun getStudentByStudentId(studentId: String): StudentRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentRecord(student: StudentRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentRecords(students: List<StudentRecord>)

    @Update
    suspend fun updateStudentRecord(student: StudentRecord)

    @Query("DELETE FROM student_records WHERE id = :id")
    suspend fun deleteStudentRecord(id: Int)

    // Admin Security Passkey
    @Query("SELECT * FROM admin_security WHERE id = 1 LIMIT 1")
    fun getAdminSecurityConfig(): Flow<AdminSecurityConfig?>

    @Query("SELECT * FROM admin_security WHERE id = 1 LIMIT 1")
    suspend fun getAdminSecurityConfigOnce(): AdminSecurityConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdminSecurityConfig(config: AdminSecurityConfig)

    @Query("UPDATE admin_security SET adminPasskey = :passkey WHERE id = 1")
    suspend fun updateAdminPasskey(passkey: String)

    @Query("UPDATE admin_security SET activeTerm = :term, activeSession = :session WHERE id = 1")
    suspend fun updateAcademicTermAndSession(term: String, session: String)

    @Query("UPDATE admin_security SET bankName = :bankName, bankAccountNumber = :accountNumber, bankAccountName = :accountName WHERE id = 1")
    suspend fun updateBankDetails(bankName: String, accountNumber: String, accountName: String)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY isPinned DESC, id DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)

    // Assignments
    @Query("SELECT * FROM assignments ORDER BY id DESC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignments(assignments: List<Assignment>)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    // Fees
    @Query("SELECT * FROM fee_items ORDER BY isPaid ASC, id ASC")
    fun getAllFeeItems(): Flow<List<FeeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeItem(feeItem: FeeItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeItems(feeItems: List<FeeItem>)

    @Update
    suspend fun updateFeeItem(feeItem: FeeItem)

    // Payments
    @Query("SELECT * FROM payment_transactions ORDER BY id DESC")
    fun getAllPayments(): Flow<List<PaymentTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<PaymentTransaction>)

    // Admission Applications
    @Query("SELECT * FROM admission_applications ORDER BY id DESC")
    fun getAllAdmissionApplications(): Flow<List<AdmissionApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmissionApplication(application: AdmissionApplication)

    // Attendance
    @Query("SELECT * FROM attendance_records ORDER BY id DESC")
    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecords(records: List<AttendanceRecord>)

    // CBT Tests
    @Query("SELECT * FROM cbt_tests ORDER BY id DESC")
    fun getAllCbtTests(): Flow<List<CbtTest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtTest(test: CbtTest): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtTests(tests: List<CbtTest>)

    @Update
    suspend fun updateCbtTest(test: CbtTest)

    @Query("UPDATE cbt_tests SET isLive = :isLive WHERE id = :testId")
    suspend fun updateCbtLiveStatus(testId: Int, isLive: Boolean)

    @Query("UPDATE cbt_tests SET isResultsPublished = :isPublished WHERE id = :testId")
    suspend fun updateCbtPublishStatus(testId: Int, isPublished: Boolean)

    // CBT Questions
    @Query("SELECT * FROM cbt_questions WHERE testId = :testId ORDER BY questionNumber ASC")
    fun getQuestionsForTest(testId: Int): Flow<List<CbtQuestion>>

    @Query("SELECT * FROM cbt_questions ORDER BY id ASC")
    fun getAllCbtQuestions(): Flow<List<CbtQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtQuestion(question: CbtQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtQuestions(questions: List<CbtQuestion>)

    // CBT Submissions
    @Query("SELECT * FROM cbt_submissions ORDER BY id DESC")
    fun getAllCbtSubmissions(): Flow<List<CbtSubmission>>

    @Query("SELECT * FROM cbt_submissions WHERE testId = :testId ORDER BY id DESC")
    fun getSubmissionsForTest(testId: Int): Flow<List<CbtSubmission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtSubmission(submission: CbtSubmission)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCbtSubmissions(submissions: List<CbtSubmission>)

    @Update
    suspend fun updateCbtSubmission(submission: CbtSubmission)

    // Staff Clock In / Out
    @Query("SELECT * FROM staff_clock_records ORDER BY id DESC")
    fun getAllStaffClockRecords(): Flow<List<StaffClockRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaffClockRecord(record: StaffClockRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaffClockRecords(records: List<StaffClockRecord>)

    @Update
    suspend fun updateStaffClockRecord(record: StaffClockRecord)

    // Group Chat Messages
    @Query("SELECT * FROM group_chat_messages WHERE channelId = :channelId AND isDeleted = 0 ORDER BY id ASC")
    fun getMessagesForChannel(channelId: String): Flow<List<GroupChatMessage>>

    @Query("SELECT * FROM group_chat_messages WHERE isDeleted = 0 ORDER BY id ASC")
    fun getAllGroupChatMessages(): Flow<List<GroupChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChatMessage(message: GroupChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChatMessages(messages: List<GroupChatMessage>)

    @Query("UPDATE group_chat_messages SET isDeleted = 1 WHERE id = :messageId")
    suspend fun softDeleteChatMessage(messageId: Int)

    @Query("DELETE FROM group_chat_messages WHERE channelId = :channelId")
    suspend fun clearChannelMessages(channelId: String)
}
