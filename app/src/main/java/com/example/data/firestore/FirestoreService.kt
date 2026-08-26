package com.example.data.firestore

import android.content.Context
import android.util.Log
import com.example.data.model.Announcement
import com.example.data.model.FeeItem
import com.example.data.model.PaymentTransaction
import com.example.data.model.StudentRecord
import com.example.data.model.TimetablePeriod
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Data classes for Firestore serialization/deserialization
 */
data class FirestoreUser(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "STUDENT",
    val regOrStaffId: String = "",
    val assignedClass: String = "SS 1 Science",
    val childName: String? = null,
    val childRegNumber: String? = null,
    val childStudentId: String? = null,
    val phone: String = "+234 816 620 5113",
    val titleOrDesignation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class FirestoreBillingItem(
    val id: String = "",
    val title: String = "",
    val term: String = "2nd Term 2024/2025",
    val targetClass: String = "SS 1 Science",
    val amount: Double = 0.0,
    val isPaid: Boolean = false,
    val dueDate: String = "",
    val category: String = "Tuition",
    val studentId: String = "GRS/2024/0428",
    val studentName: String = "Adeleke David Oluwaseun",
    val parentEmail: String = "parent@grazielroyalschools.edu.ng",
    val createdAt: Long = System.currentTimeMillis()
)

data class FirestoreTimetable(
    val id: String = "",
    val assignedClass: String = "SS 1 Science",
    val dayOfWeek: String = "Monday",
    val periods: List<Map<String, Any>> = emptyList(),
    val term: String = "2nd Term",
    val academicSession: String = "2024/2025",
    val updatedAt: Long = System.currentTimeMillis()
)

data class FirestoreAnnouncement(
    val id: String = "",
    val title: String = "",
    val category: String = "Academic",
    val date: String = "",
    val summary: String = "",
    val isPinned: Boolean = false,
    val author: String = "School Administration",
    val authorRole: String = "ADMIN",
    val targetAudience: String = "All", // "All", "Students", "Parents", "Staff"
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Service managing Firestore collections with strict portal isolation
 */
class FirestoreService(private val context: Context) {

    private val tag = "FirestoreService"

    val isFirebaseInitialized: Boolean
        get() {
            return try {
                FirebaseApp.getApps(context).isNotEmpty()
            } catch (t: Throwable) {
                false
            }
        }

    val firestore: FirebaseFirestore?
        get() {
            return try {
                if (isFirebaseInitialized) FirebaseFirestore.getInstance() else null
            } catch (t: Throwable) {
                Log.w(tag, "Firestore instance not available: ${t.message}")
                null
            }
        }

    // Collection names
    companion object {
        const val USERS_COLLECTION = "users"
        const val BILLING_COLLECTION = "billing"
        const val TIMETABLES_COLLECTION = "timetables"
        const val ANNOUNCEMENTS_COLLECTION = "announcements"
        const val STUDENTS_COLLECTION = "students"
        const val PAYMENTS_COLLECTION = "payments"
    }

    /**
     * Initializes and seeds default documents into Firestore collections if empty
     */
    suspend fun initializeCollectionsIfEmpty(): Result<String> {
        val db = firestore ?: return Result.failure(Exception("Firebase is not initialized."))

        return try {
            // 1. Seed Users Collection
            val usersRef = db.collection(USERS_COLLECTION)
            val usersSnapshot = usersRef.limit(1).get().await()
            if (usersSnapshot.isEmpty) {
                val defaultUsers = listOf(
                    FirestoreUser(
                        uid = "admin_master_001",
                        fullName = "Mr. Tobi Adebayo",
                        email = "admin@grazielroyalschools.edu.ng",
                        role = "ADMIN",
                        regOrStaffId = "GRS/FND/001",
                        assignedClass = "All Classes & Sections",
                        titleOrDesignation = "Founder & School Proprietor",
                        phone = "+2348166205113"
                    ),
                    FirestoreUser(
                        uid = "teacher_ayo_014",
                        fullName = "Mr. Adeleke Ayomide",
                        email = "teacher@grazielroyalschools.edu.ng",
                        role = "TEACHER",
                        regOrStaffId = "GRS/STF/2021/014",
                        assignedClass = "SS 1 Science",
                        titleOrDesignation = "Senior Mathematics & Science Master",
                        phone = "+234 813 456 7890"
                    ),
                    FirestoreUser(
                        uid = "student_david_0428",
                        fullName = "Adeleke David Oluwaseun",
                        email = "student@grazielroyalschools.edu.ng",
                        role = "STUDENT",
                        regOrStaffId = "GRS/2024/0428",
                        assignedClass = "SS 1 Science",
                        titleOrDesignation = "Student (Science Track - Class Head)",
                        phone = "+234 816 620 5113"
                    ),
                    FirestoreUser(
                        uid = "parent_adeleke_092",
                        fullName = "Chief & Mrs. Adeleke",
                        email = "parent@grazielroyalschools.edu.ng",
                        role = "PARENT",
                        regOrStaffId = "GRS/PAR/2024/092",
                        assignedClass = "SS 1 Science",
                        childName = "Adeleke David Oluwaseun",
                        childRegNumber = "GRS/2024/0428",
                        childStudentId = "GRS/2024/0428",
                        titleOrDesignation = "Parent / Guardian",
                        phone = "+234 816 620 5113"
                    )
                )

                for (user in defaultUsers) {
                    usersRef.document(user.uid).set(user, SetOptions.merge()).await()
                }
                Log.d(tag, "Initialized default users in Firestore.")
            }

            // 2. Seed Billing Collection
            val billingRef = db.collection(BILLING_COLLECTION)
            val billingSnapshot = billingRef.limit(1).get().await()
            if (billingSnapshot.isEmpty) {
                val defaultBilling = listOf(
                    FirestoreBillingItem(
                        id = "INV-2025-001",
                        title = "2nd Term Comprehensive Tuition Fee",
                        term = "2nd Term 2024/2025",
                        targetClass = "SS 1 Science",
                        amount = 120000.0,
                        isPaid = true,
                        dueDate = "15 Jan 2025",
                        category = "Tuition",
                        studentId = "GRS/2024/0428",
                        studentName = "Adeleke David Oluwaseun",
                        parentEmail = "parent@grazielroyalschools.edu.ng"
                    ),
                    FirestoreBillingItem(
                        id = "INV-2025-002",
                        title = "STEM, ICT & AI Robotics Laboratory Levy",
                        term = "2nd Term 2024/2025",
                        targetClass = "SS 1 Science",
                        amount = 35000.0,
                        isPaid = true,
                        dueDate = "30 Jan 2025",
                        category = "ICT & STEM",
                        studentId = "GRS/2024/0428",
                        studentName = "Adeleke David Oluwaseun",
                        parentEmail = "parent@grazielroyalschools.edu.ng"
                    ),
                    FirestoreBillingItem(
                        id = "INV-2025-003",
                        title = "Mid-Term Academic Project & Field Excursion",
                        term = "2nd Term 2024/2025",
                        targetClass = "SS 1 Science",
                        amount = 18500.0,
                        isPaid = false,
                        dueDate = "10 Mar 2025",
                        category = "Development",
                        studentId = "GRS/2024/0428",
                        studentName = "Adeleke David Oluwaseun",
                        parentEmail = "parent@grazielroyalschools.edu.ng"
                    ),
                    FirestoreBillingItem(
                        id = "INV-2025-004",
                        title = "Termly Bus Transit & Fleet Maintenance",
                        term = "2nd Term 2024/2025",
                        targetClass = "SS 1 Science",
                        amount = 45000.0,
                        isPaid = false,
                        dueDate = "28 Feb 2025",
                        category = "Bus Transit",
                        studentId = "GRS/2024/0428",
                        studentName = "Adeleke David Oluwaseun",
                        parentEmail = "parent@grazielroyalschools.edu.ng"
                    )
                )

                for (item in defaultBilling) {
                    billingRef.document(item.id).set(item, SetOptions.merge()).await()
                }
                Log.d(tag, "Initialized default billing items in Firestore.")
            }

            // 3. Seed Timetables Collection
            val timetablesRef = db.collection(TIMETABLES_COLLECTION)
            val timetableSnapshot = timetablesRef.limit(1).get().await()
            if (timetableSnapshot.isEmpty) {
                val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                for (day in days) {
                    val periods = getDemoPeriodsForDay(day)
                    val timetable = FirestoreTimetable(
                        id = "SS1_Science_${day.lowercase()}",
                        assignedClass = "SS 1 Science",
                        dayOfWeek = day,
                        periods = periods.map {
                            mapOf(
                                "periodNumber" to it.periodNumber,
                                "timeRange" to it.timeRange,
                                "subject" to it.subject,
                                "teacher" to it.teacher,
                                "room" to it.room,
                                "isBreak" to it.isBreak
                            )
                        },
                        term = "2nd Term",
                        academicSession = "2024/2025"
                    )
                    timetablesRef.document(timetable.id).set(timetable, SetOptions.merge()).await()
                }
                Log.d(tag, "Initialized default timetables in Firestore.")
            }

            // 4. Seed Announcements Collection
            val announcementsRef = db.collection(ANNOUNCEMENTS_COLLECTION)
            val annSnapshot = announcementsRef.limit(1).get().await()
            if (annSnapshot.isEmpty) {
                val defaultAnnouncements = listOf(
                    FirestoreAnnouncement(
                        id = "ANN-2025-001",
                        title = "Second Term Mid-Term Assessment Schedule & Portal Guidelines",
                        category = "Academic",
                        date = "22 Feb 2025",
                        summary = "Mid-term CBT and written evaluations commence on March 3, 2025. Students are advised to verify their CBT portal passcodes.",
                        isPinned = true,
                        author = "Office of the Academic Director",
                        authorRole = "ADMIN",
                        targetAudience = "All"
                    ),
                    FirestoreAnnouncement(
                        id = "ANN-2025-002",
                        title = "Annual Inter-House Sports Festival 2025",
                        category = "Event",
                        date = "20 Feb 2025",
                        summary = "Grand Finale scheduled for March 15th at the Main Sports Complex. All parents, guardians, and alumni are cordially invited.",
                        isPinned = true,
                        author = "Sports & Physical Education Dept",
                        authorRole = "ADMIN",
                        targetAudience = "All"
                    ),
                    FirestoreAnnouncement(
                        id = "ANN-2025-003",
                        title = "Parent-Teacher Consultative Forum & Academic Progress Review",
                        category = "Notice",
                        date = "18 Feb 2025",
                        summary = "Virtual and on-campus interactive session for SS 1 & SS 3 parents on Saturday, March 8th at 10:00 AM.",
                        isPinned = false,
                        author = "Parents-Teachers Association Liaison",
                        authorRole = "ADMIN",
                        targetAudience = "Parents"
                    ),
                    FirestoreAnnouncement(
                        id = "ANN-2025-004",
                        title = "STEM & Robotics Championship Trials for Senior Classes",
                        category = "Notice",
                        date = "16 Feb 2025",
                        summary = "Registration is open for students in SS 1 - SS 3 interested in representing Graziel Royal Schools at the National Robotics Olympiad.",
                        isPinned = false,
                        author = "STEM & Innovation Club Master",
                        authorRole = "TEACHER",
                        targetAudience = "Students"
                    )
                )

                for (ann in defaultAnnouncements) {
                    announcementsRef.document(ann.id).set(ann, SetOptions.merge()).await()
                }
                Log.d(tag, "Initialized default announcements in Firestore.")
            }

            Result.success("Firestore collections (users, billing, timetables, announcements) initialized successfully.")
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize Firestore collections", e)
            Result.failure(e)
        }
    }

    // =========================================================================
    // USER MANAGEMENT & ROLE ISOLATION
    // =========================================================================

    suspend fun getUserProfile(uid: String): FirestoreUser? {
        val db = firestore ?: return null
        return try {
            val doc = db.collection(USERS_COLLECTION).document(uid).get().await()
            doc.toObject(FirestoreUser::class.java)
        } catch (e: Exception) {
            Log.e(tag, "Error fetching user profile for $uid", e)
            null
        }
    }

    suspend fun saveUserProfile(user: FirestoreUser): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            db.collection(USERS_COLLECTION).document(user.uid).set(user, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // BILLING & FEE PORTAL ISOLATION
    // Admin: Full global view
    // Parent: ONLY their student's invoices
    // Student: ONLY their own invoices
    // =========================================================================

    /**
     * Admin Global Access: Retrieve all billing items across all classes and students
     */
    fun getAllBillingStream(): Flow<List<FirestoreBillingItem>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration = db.collection(BILLING_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(tag, "Error listening to all billing", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { it.toObject(FirestoreBillingItem::class.java) }
                    trySend(items)
                }
            }

        awaitClose { registration.remove() }
    }

    /**
     * Parent Portal Isolation: Query only billing items belonging to the parent's child
     */
    fun getBillingForParentStream(childStudentId: String, parentEmail: String): Flow<List<FirestoreBillingItem>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val query = if (childStudentId.isNotBlank()) {
            db.collection(BILLING_COLLECTION).whereEqualTo("studentId", childStudentId)
        } else {
            db.collection(BILLING_COLLECTION).whereEqualTo("parentEmail", parentEmail)
        }

        val registration: ListenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(tag, "Error listening to parent billing", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { it.toObject(FirestoreBillingItem::class.java) }
                trySend(items)
            }
        }

        awaitClose { registration.remove() }
    }

    /**
     * Student Portal Isolation: Query only billing records assigned to this student ID
     */
    fun getBillingForStudentStream(studentId: String): Flow<List<FirestoreBillingItem>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val query = db.collection(BILLING_COLLECTION).whereEqualTo("studentId", studentId)
        val registration: ListenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(tag, "Error listening to student billing", error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { it.toObject(FirestoreBillingItem::class.java) }
                trySend(items)
            }
        }

        awaitClose { registration.remove() }
    }

    suspend fun saveBillingItem(item: FirestoreBillingItem): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            val docId = if (item.id.isNotBlank()) item.id else db.collection(BILLING_COLLECTION).document().id
            val finalItem = item.copy(id = docId)
            db.collection(BILLING_COLLECTION).document(docId).set(finalItem, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBillingItem(billingId: String): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            db.collection(BILLING_COLLECTION).document(billingId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // TIMETABLES MANAGEMENT
    // =========================================================================

    fun getTimetablesForClassStream(assignedClass: String): Flow<List<FirestoreTimetable>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration = db.collection(TIMETABLES_COLLECTION)
            .whereEqualTo("assignedClass", assignedClass)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(tag, "Error listening to timetables", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { it.toObject(FirestoreTimetable::class.java) }
                    trySend(items)
                }
            }

        awaitClose { registration.remove() }
    }

    suspend fun saveTimetable(timetable: FirestoreTimetable): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            val docId = if (timetable.id.isNotBlank()) timetable.id else "${timetable.assignedClass}_${timetable.dayOfWeek}".replace(" ", "_")
            val finalTimetable = timetable.copy(id = docId, updatedAt = System.currentTimeMillis())
            db.collection(TIMETABLES_COLLECTION).document(docId).set(finalTimetable, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================================================
    // ANNOUNCEMENTS & AUDIENCE FILTERING
    // =========================================================================

    /**
     * Retrieves stream of announcements tailored to user's role audience
     * - Admin/Teacher: Sees All
     * - Parents: Sees "All" and "Parents"
     * - Students: Sees "All" and "Students"
     */
    fun getAnnouncementsStream(targetRole: UserRole): Flow<List<FirestoreAnnouncement>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val registration: ListenerRegistration = db.collection(ANNOUNCEMENTS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(tag, "Error listening to announcements", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val allItems = snapshot.documents.mapNotNull { it.toObject(FirestoreAnnouncement::class.java) }
                    // Filter in accordance with portal audience rules
                    val filtered = when (targetRole) {
                        UserRole.ADMIN, UserRole.TEACHER -> allItems
                        UserRole.PARENT -> allItems.filter { it.targetAudience == "All" || it.targetAudience == "Parents" }
                        UserRole.STUDENT -> allItems.filter { it.targetAudience == "All" || it.targetAudience == "Students" }
                        else -> allItems.filter { it.targetAudience == "All" }
                    }
                    trySend(filtered)
                }
            }

        awaitClose { registration.remove() }
    }

    suspend fun postAnnouncement(announcement: FirestoreAnnouncement): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            val docId = if (announcement.id.isNotBlank()) announcement.id else "ANN_${System.currentTimeMillis()}"
            val finalAnn = announcement.copy(id = docId, timestamp = System.currentTimeMillis())
            db.collection(ANNOUNCEMENTS_COLLECTION).document(docId).set(finalAnn, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAnnouncement(announcementId: String): Result<Unit> {
        val db = firestore ?: return Result.failure(Exception("Firestore not available"))
        return try {
            db.collection(ANNOUNCEMENTS_COLLECTION).document(announcementId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper method to create demo periods for days of the week
    private fun getDemoPeriodsForDay(day: String): List<TimetablePeriod> {
        return when (day) {
            "Monday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Mathematics", "Mr. Adeleke Ayomide", "Room SS1-A"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Physics", "Dr. O. Babatunde", "Physics Lab"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "English Language", "Mrs. F. Alabi", "Room SS1-A"),
                TimetablePeriod(0, "10:15 - 10:45 AM", "Morning Break & Refreshment", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(4, "10:45 - 11:30 AM", "Chemistry", "Mr. K. Okonkwo", "Chemistry Lab"),
                TimetablePeriod(5, "11:30 - 12:15 PM", "Biology", "Mrs. C. Eze", "Biology Lab"),
                TimetablePeriod(6, "12:15 - 01:00 PM", "Civic Education", "Mr. T. Adeyemi", "Room SS1-A"),
                TimetablePeriod(0, "01:00 - 01:40 PM", "Lunch & Prayer Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(7, "01:40 - 02:25 PM", "Computer Studies & Coding", "Engr. S. Lawal", "ICT Suite 2"),
                TimetablePeriod(8, "02:25 - 03:10 PM", "Technical Drawing", "Arch. B. Adeleke", "Design Studio")
            )
            "Tuesday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Chemistry Practical", "Mr. K. Okonkwo", "Chemistry Lab"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Chemistry Theory", "Mr. K. Okonkwo", "Chemistry Lab"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Further Mathematics", "Mr. Adeleke Ayomide", "Room SS1-A"),
                TimetablePeriod(0, "10:15 - 10:45 AM", "Morning Break & Refreshment", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(4, "10:45 - 11:30 AM", "English Literature", "Mrs. F. Alabi", "Room SS1-A"),
                TimetablePeriod(5, "11:30 - 12:15 PM", "Agricultural Science", "Mr. D. Ojo", "Agric Field/Lab"),
                TimetablePeriod(6, "12:15 - 01:00 PM", "Economics", "Mrs. R. Bello", "Room SS1-A"),
                TimetablePeriod(0, "01:00 - 01:40 PM", "Lunch & Prayer Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(7, "01:40 - 02:25 PM", "Physics Tutorial", "Dr. O. Babatunde", "Physics Lab"),
                TimetablePeriod(8, "02:25 - 03:10 PM", "Physical & Health Education", "Coach M. Peters", "Sports Ground")
            )
            "Wednesday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Physics Practical", "Dr. O. Babatunde", "Physics Lab"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Mathematics Problem Solving", "Mr. Adeleke Ayomide", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Biology Practical", "Mrs. C. Eze", "Biology Lab"),
                TimetablePeriod(0, "10:15 - 10:45 AM", "Morning Break & Refreshment", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(4, "10:45 - 11:30 AM", "Geography & Climate", "Mr. S. Danladi", "Geo Room"),
                TimetablePeriod(5, "11:30 - 12:15 PM", "French / Yoruba Language", "Mme. C. Dupuis", "Language Lab"),
                TimetablePeriod(6, "12:15 - 01:00 PM", "Christian/Islamic Religious Studies", "Pastor John", "Room SS1-A"),
                TimetablePeriod(0, "01:00 - 01:40 PM", "Lunch & Prayer Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(7, "01:40 - 03:00 PM", "STEM & Robotics Club Workshop", "Engr. S. Lawal", "Innovation Hub")
            )
            "Thursday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Mathematics", "Mr. Adeleke Ayomide", "Room SS1-A"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "English Grammar & Essay", "Mrs. F. Alabi", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Physics", "Dr. O. Babatunde", "Room SS1-A"),
                TimetablePeriod(0, "10:15 - 10:45 AM", "Morning Break & Refreshment", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(4, "10:45 - 11:30 AM", "Chemistry", "Mr. K. Okonkwo", "Room SS1-A"),
                TimetablePeriod(5, "11:30 - 12:15 PM", "Data Processing", "Engr. S. Lawal", "ICT Suite 2"),
                TimetablePeriod(6, "12:15 - 01:00 PM", "Civic & Moral Education", "Mr. T. Adeyemi", "Room SS1-A"),
                TimetablePeriod(0, "01:00 - 01:40 PM", "Lunch & Prayer Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(7, "01:40 - 02:25 PM", "Library & Independent Study", "Librarian", "E-Library"),
                TimetablePeriod(8, "02:25 - 03:10 PM", "Literary & Debate / Quiz", "Mrs. F. Alabi", "Auditorium")
            )
            else -> listOf( // Friday
                TimetablePeriod(1, "08:00 - 08:45 AM", "General Mathematics Revision", "Mr. Adeleke Ayomide", "Room SS1-A"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Biology", "Mrs. C. Eze", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "English Comprehension", "Mrs. F. Alabi", "Room SS1-A"),
                TimetablePeriod(0, "10:15 - 10:45 AM", "Morning Break & Refreshment", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(4, "10:45 - 11:30 AM", "CBT & Speed Test Drill", "Mr. Adeleke Ayomide", "CBT Center"),
                TimetablePeriod(5, "11:30 - 12:15 PM", "Creative Arts & Music", "Mr. V. Johnson", "Music Studio"),
                TimetablePeriod(0, "12:15 - 01:30 PM", "Jumat / Mid-Day Dismissal & Sports", "All Staff", "Sports Complex", isBreak = true)
            )
        }
    }
}
