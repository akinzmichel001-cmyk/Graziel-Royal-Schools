package com.example.data.repository

import com.example.data.local.SchoolDao
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
import com.example.data.model.SubjectGrade
import com.example.data.model.TermReport
import com.example.data.model.TimetablePeriod
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchoolRepository(private val dao: SchoolDao) {

    val announcements: Flow<List<Announcement>> = dao.getAllAnnouncements()
    val assignments: Flow<List<Assignment>> = dao.getAllAssignments()
    val feeItems: Flow<List<FeeItem>> = dao.getAllFeeItems()
    val payments: Flow<List<PaymentTransaction>> = dao.getAllPayments()
    val applications: Flow<List<AdmissionApplication>> = dao.getAllAdmissionApplications()
    val attendanceRecords: Flow<List<AttendanceRecord>> = dao.getAllAttendanceRecords()
    val cbtTests: Flow<List<CbtTest>> = dao.getAllCbtTests()
    val cbtQuestions: Flow<List<CbtQuestion>> = dao.getAllCbtQuestions()
    val cbtSubmissions: Flow<List<CbtSubmission>> = dao.getAllCbtSubmissions()
    val staffClockRecords: Flow<List<StaffClockRecord>> = dao.getAllStaffClockRecords()
    val allGroupMessages: Flow<List<GroupChatMessage>> = dao.getAllGroupChatMessages()

    fun getQuestionsForTest(testId: Int): Flow<List<CbtQuestion>> = dao.getQuestionsForTest(testId)
    fun getSubmissionsForTest(testId: Int): Flow<List<CbtSubmission>> = dao.getSubmissionsForTest(testId)
    fun getMessagesForChannel(channelId: String): Flow<List<GroupChatMessage>> = dao.getMessagesForChannel(channelId)

    // Demo Pre-configured accounts for instant login
    val defaultAccounts = listOf(
        UserAccount(
            id = 1,
            fullName = "Mr. Tobi Adebayo",
            email = "admin@grazielroyalschools.edu.ng",
            role = UserRole.ADMIN,
            regOrStaffId = "GRS/FND/001",
            assignedClass = "All Classes & Sections",
            titleOrDesignation = "Founder & School Proprietor",
            phone = "+2348166205113"
        ),
        UserAccount(
            id = 2,
            fullName = "Mr. Adeleke Ayomide",
            email = "teacher@grazielroyalschools.edu.ng",
            role = UserRole.TEACHER,
            regOrStaffId = "GRS/STF/2021/014",
            assignedClass = "SS 1 Science",
            titleOrDesignation = "Senior Mathematics & Science Master",
            phone = "+234 813 456 7890"
        ),
        UserAccount(
            id = 3,
            fullName = "Adeleke David Oluwaseun",
            email = "student@grazielroyalschools.edu.ng",
            role = UserRole.STUDENT,
            regOrStaffId = "GRS/2024/0428",
            assignedClass = "SS 1 Science",
            titleOrDesignation = "Student (Science Track - Class Head)",
            phone = "+234 816 620 5113"
        ),
        UserAccount(
            id = 4,
            fullName = "Chief & Mrs. Adeleke",
            email = "parent@grazielroyalschools.edu.ng",
            role = UserRole.PARENT,
            regOrStaffId = "GRS/PAR/2024/092",
            assignedClass = "SS 1 Science",
            childName = "Adeleke David Oluwaseun",
            childRegNumber = "GRS/2024/0428",
            titleOrDesignation = "Parent / Guardian",
            phone = "+234 816 620 5113"
        )
    )

    suspend fun seedInitialDataIfEmpty() {
        // Initial announcements
        val initialAnnouncements = listOf(
            Announcement(
                id = 1,
                title = "2nd Term Continuous Assessment CBT & Practical Exams",
                category = "Academic",
                date = "24 Feb 2025",
                summary = "All SS 1 and SS 2 students are notified that Mathematics and Science CBT assessments go live today. Tutors will review and publish scores afterwards.",
                isPinned = true,
                author = "Academic Dean",
                targetAudience = "All"
            ),
            Announcement(
                id = 2,
                title = "Annual Cultural & Heritage Exhibition 2025",
                category = "Cultural",
                date = "15 March 2025",
                summary = "Join us as our students showcase traditional arts, language presentations including our acclaimed Yoruba cultural project (pounded yam & Ofuloju culinary arts), and drama.",
                isPinned = true,
                author = "Office of the Principal",
                targetAudience = "All"
            ),
            Announcement(
                id = 3,
                title = "Parent-Teacher Consultation & Terminal Report Cards",
                category = "Notice",
                date = "28 Feb 2025",
                summary = "Parents can now check approved terminal report cards and settle termly fee balances directly via the official Parent Portal.",
                isPinned = false,
                author = "Bursary & Administration",
                targetAudience = "Parents"
            ),
            Announcement(
                id = 4,
                title = "Inter-House Sports Championship & Athletics",
                category = "Event",
                date = "22 March 2025",
                summary = "Royal Blue, Emerald Green, Golden Sun, and Ruby Red houses compete for the prestigious Graziel Royal Trophy at the sports complex.",
                isPinned = false,
                author = "Sports Master",
                targetAudience = "All"
            )
        )

        // Initial assignments
        val initialAssignments = listOf(
            Assignment(
                id = 1,
                title = "Algebraic Expressions & Quadratic Factorization",
                subject = "Mathematics",
                teacher = "Mr. A. Adeleke",
                targetClass = "SS 1 Science",
                dueDate = "Tomorrow, 8:00 AM",
                description = "Complete exercises 4.2 in the New General Mathematics textbook (Questions 1 to 15). Show all intermediate working steps.",
                maxScore = 20,
                score = null,
                isSubmitted = false
            ),
            Assignment(
                id = 2,
                title = "Energy Transformations in Ecological Biomes",
                subject = "Biology / Basic Science",
                teacher = "Mrs. B. Okonkwo",
                targetClass = "SS 1 Science",
                dueDate = "Friday, 4:00 PM",
                description = "Draw a comprehensive food web illustrating primary producers, herbivores, and apex predators within a tropical rainforest biome.",
                maxScore = 20,
                score = 18,
                isSubmitted = true,
                submissionText = "Attached food web diagram with 4 trophic levels and energy loss calculations.",
                feedback = "Excellent diagrammatic representation and clear trophic calculations. Well done!"
            ),
            Assignment(
                id = 3,
                title = "Essay: The Role of Technology in Modern Education",
                subject = "English Language",
                teacher = "Mrs. F. Williams",
                targetClass = "SS 1 Science",
                dueDate = "Next Monday",
                description = "Write a formal expository essay of not less than 350 words highlighting the benefits and challenges of AI in contemporary classrooms.",
                maxScore = 20,
                score = null,
                isSubmitted = false
            ),
            Assignment(
                id = 4,
                title = "Akowole Yoruba: Asa ati Isese",
                subject = "Yoruba Language",
                teacher = "Ogbeni O. Balogun",
                targetClass = "SS 1 Science",
                dueDate = "In 3 days",
                description = "Ko aroko ranpe nipa pataki ounje abinibi (Ofuloju ati Iyan) ninu asa Yoruba.",
                maxScore = 20,
                score = 19,
                isSubmitted = true,
                submissionText = "Aroko ti a ko lori orisirisi ounje ile Yoruba ati bi a se n pese won.",
                feedback = "Aroko ti o yanranti pupo! E ku ise takuntakun."
            )
        )

        // Initial Fee Items (Parent Portal only)
        val initialFees = listOf(
            FeeItem(
                id = 1,
                title = "Tuition & Academic Instructional Fee",
                term = "2nd Term 2024/2025",
                targetClass = "SS 1 Science",
                amount = 220000.0,
                isPaid = true,
                dueDate = "Jan 15, 2025",
                category = "Tuition"
            ),
            FeeItem(
                id = 2,
                title = "ICT, Coding & Robotics Lab Access",
                term = "2nd Term 2024/2025",
                targetClass = "SS 1 Science",
                amount = 45000.0,
                isPaid = true,
                dueDate = "Jan 15, 2025",
                category = "ICT & STEM"
            ),
            FeeItem(
                id = 3,
                title = "School Bus Transit Service (Ifo - Opo-Ibogun Axis)",
                term = "2nd Term 2024/2025",
                targetClass = "SS 1 Science",
                amount = 65000.0,
                isPaid = true,
                dueDate = "Jan 20, 2025",
                category = "Bus Transit"
            ),
            FeeItem(
                id = 4,
                title = "Clubs, Science Practical & Sports Levy",
                term = "2nd Term 2024/2025",
                targetClass = "SS 1 Science",
                amount = 30000.0,
                isPaid = false,
                dueDate = "March 10, 2025",
                category = "Development"
            ),
            FeeItem(
                id = 5,
                title = "Termly Nutritious Hot Lunch Club",
                term = "2nd Term 2024/2025",
                targetClass = "SS 1 Science",
                amount = 55000.0,
                isPaid = false,
                dueDate = "March 15, 2025",
                category = "Meals"
            )
        )

        // Initial payments
        val initialPayments = listOf(
            PaymentTransaction(
                id = 1,
                receiptNumber = "GRS-REC-2025-0891",
                title = "Tuition Fee - 2nd Term 2024/2025",
                amount = 220000.0,
                date = "12 Jan 2025, 10:24 AM",
                paymentMethod = "Online Card Payment",
                status = "SUCCESS"
            ),
            PaymentTransaction(
                id = 2,
                receiptNumber = "GRS-REC-2025-0892",
                title = "ICT, Coding & Robotics Fee",
                amount = 45000.0,
                date = "12 Jan 2025, 10:26 AM",
                paymentMethod = "Bank Transfer",
                status = "SUCCESS"
            ),
            PaymentTransaction(
                id = 3,
                receiptNumber = "GRS-REC-2025-0904",
                title = "School Bus Transit (2nd Term)",
                amount = 65000.0,
                date = "18 Jan 2025, 02:15 PM",
                paymentMethod = "Online Card Payment",
                status = "SUCCESS"
            )
        )

        // Initial Attendance
        val initialAttendance = listOf(
            AttendanceRecord(id = 1, studentName = "Adeleke David O.", grade = "SS 1 Science", date = "22 Feb 2025", dayOfWeek = "Friday", status = "PRESENT", checkInTime = "07:42 AM"),
            AttendanceRecord(id = 2, studentName = "Adeleke David O.", grade = "SS 1 Science", date = "21 Feb 2025", dayOfWeek = "Thursday", status = "PRESENT", checkInTime = "07:38 AM"),
            AttendanceRecord(id = 3, studentName = "Adeleke David O.", grade = "SS 1 Science", date = "20 Feb 2025", dayOfWeek = "Wednesday", status = "PRESENT", checkInTime = "07:45 AM"),
            AttendanceRecord(id = 4, studentName = "Adeleke David O.", grade = "SS 1 Science", date = "19 Feb 2025", dayOfWeek = "Tuesday", status = "LATE", checkInTime = "08:12 AM", notes = "Traffic delay along Ifo corridor"),
            AttendanceRecord(id = 5, studentName = "Adeleke David O.", grade = "SS 1 Science", date = "18 Feb 2025", dayOfWeek = "Monday", status = "PRESENT", checkInTime = "07:35 AM")
        )

        // Initial Staff Clock-In / Clock-Out Records
        val initialStaffClock = listOf(
            StaffClockRecord(
                id = 1,
                staffName = "Mr. Adeleke Ayomide",
                staffId = "GRS/STF/2021/014",
                date = "22 Feb 2025",
                clockInTime = "07:25 AM",
                clockOutTime = "04:10 PM",
                status = "CLOCKED_OUT"
            ),
            StaffClockRecord(
                id = 2,
                staffName = "Mrs. B. Okonkwo",
                staffId = "GRS/STF/2019/008",
                date = "22 Feb 2025",
                clockInTime = "07:30 AM",
                clockOutTime = null,
                status = "CLOCKED_IN"
            ),
            StaffClockRecord(
                id = 3,
                staffName = "Ogbeni O. Balogun",
                staffId = "GRS/STF/2020/022",
                date = "22 Feb 2025",
                clockInTime = "07:18 AM",
                clockOutTime = null,
                status = "CLOCKED_IN"
            )
        )

        // Initial CBT Tests
        val initialCbtTests = listOf(
            CbtTest(
                id = 1,
                title = "SS 1 Mathematics 2nd Term CBT Assessment",
                subject = "Mathematics",
                targetClass = "SS 1 Science",
                durationMinutes = 10,
                totalMarks = 20,
                isLive = true, // LIVE FOR STUDENTS TO TAKE!
                isResultsPublished = true,
                createdByTeacher = "Mr. A. Adeleke",
                instructions = "Attempt all 5 questions. Each correct answer carries 4 marks. You have 10 minutes.",
                dateCreated = "22 Feb 2025"
            ),
            CbtTest(
                id = 2,
                title = "Basic Science & Biology Ecological Quiz",
                subject = "Biology",
                targetClass = "SS 1 Science",
                durationMinutes = 15,
                totalMarks = 20,
                isLive = true, // LIVE!
                isResultsPublished = false, // Teacher can review & publish!
                createdByTeacher = "Mrs. B. Okonkwo",
                instructions = "Read each ecological concept question and select the most accurate option.",
                dateCreated = "22 Feb 2025"
            ),
            CbtTest(
                id = 3,
                title = "Yoruba Language & Cultural Heritage Quiz",
                subject = "Yoruba Language",
                targetClass = "SS 1 Science",
                durationMinutes = 12,
                totalMarks = 20,
                isLive = false, // Teacher can hit "Go Live"
                isResultsPublished = false,
                createdByTeacher = "Ogbeni O. Balogun",
                instructions = "Asa, Isese ati Ounje ile Yoruba (Ofuloju ati Iyan).",
                dateCreated = "21 Feb 2025"
            )
        )

        // CBT Questions Bank
        val initialCbtQuestions = listOf(
            // Test 1: Mathematics
            CbtQuestion(
                id = 1,
                testId = 1,
                questionNumber = 1,
                questionText = "Solve for x in the quadratic equation: x² - 5x + 6 = 0",
                optionA = "x = 2 or x = 3",
                optionB = "x = -2 or x = -3",
                optionC = "x = 1 or x = 6",
                optionD = "x = -1 or x = 6",
                correctOption = "A",
                marks = 4,
                explanation = "Factorization gives (x - 2)(x - 3) = 0, therefore x = 2 or x = 3."
            ),
            CbtQuestion(
                id = 2,
                testId = 1,
                questionNumber = 2,
                questionText = "What is the slope (gradient) of the line passing through (2, 3) and (6, 11)?",
                optionA = "1.5",
                optionB = "2.0",
                optionC = "2.5",
                optionD = "3.0",
                correctOption = "B",
                marks = 4,
                explanation = "Gradient m = (11 - 3) / (6 - 2) = 8 / 4 = 2.0"
            ),
            CbtQuestion(
                id = 3,
                testId = 1,
                questionNumber = 3,
                questionText = "Simplify the expression: (2x³y²) * (3x²y⁴)",
                optionA = "6x⁵y⁶",
                optionB = "5x⁶y⁸",
                optionC = "6x⁶y⁸",
                optionD = "5x⁵y⁶",
                correctOption = "A",
                marks = 4,
                explanation = "2 * 3 = 6; exponents add up: x^(3+2) = x^5, y^(2+4) = y^6."
            ),
            CbtQuestion(
                id = 4,
                testId = 1,
                questionNumber = 4,
                questionText = "If sin(θ) = 3/5 in a right-angled triangle, what is the value of cos(θ)?",
                optionA = "4/5",
                optionB = "3/4",
                optionC = "5/4",
                optionD = "5/3",
                correctOption = "A",
                marks = 4,
                explanation = "Using Pythagoras theorem: adjacent = √(5² - 3²) = 4, hence cos(θ) = 4/5."
            ),
            CbtQuestion(
                id = 5,
                testId = 1,
                questionNumber = 5,
                questionText = "Calculate the simple interest on ₦50,000 for 3 years at 5% per annum.",
                optionA = "₦5,000",
                optionB = "₦7,500",
                optionC = "₦8,000",
                optionD = "₦15,000",
                correctOption = "B",
                marks = 4,
                explanation = "I = (P * R * T) / 100 = (50000 * 5 * 3) / 100 = ₦7,500."
            ),

            // Test 2: Biology / Science
            CbtQuestion(
                id = 6,
                testId = 2,
                questionNumber = 1,
                questionText = "Which cellular organelle is known as the powerhouse of eukaryotic cells?",
                optionA = "Ribosome",
                optionB = "Endoplasmic Reticulum",
                optionC = "Mitochondria",
                optionD = "Golgi Apparatus",
                correctOption = "C",
                marks = 4,
                explanation = "Mitochondria synthesize ATP through cellular respiration."
            ),
            CbtQuestion(
                id = 7,
                testId = 2,
                questionNumber = 2,
                questionText = "In an ecological food chain, what percentage of energy is typically transferred to the next trophic level?",
                optionA = "50%",
                optionB = "25%",
                optionC = "10%",
                optionD = "90%",
                correctOption = "C",
                marks = 4,
                explanation = "According to Lindeman's 10% law, only ~10% of energy is transferred up each level."
            ),
            CbtQuestion(
                id = 8,
                testId = 2,
                questionNumber = 3,
                questionText = "Which pigment is primarily responsible for light absorption during photosynthesis?",
                optionA = "Carotenoid",
                optionB = "Chlorophyll a",
                optionC = "Anthocyanin",
                optionD = "Xanthophyll",
                correctOption = "B",
                marks = 4,
                explanation = "Chlorophyll a is the primary photosynthetic pigment in green plants."
            ),
            CbtQuestion(
                id = 9,
                testId = 2,
                questionNumber = 4,
                questionText = "What type of symbiotic relationship benefits one organism while the other is unharmed?",
                optionA = "Parasitism",
                optionB = "Mutualism",
                optionC = "Commensalism",
                optionD = "Predation",
                correctOption = "C",
                marks = 4,
                explanation = "Commensalism benefits one species without affecting the other."
            ),
            CbtQuestion(
                id = 10,
                testId = 2,
                questionNumber = 5,
                questionText = "Which of the following blood vessels carries oxygenated blood from the lungs to the heart?",
                optionA = "Pulmonary Artery",
                optionB = "Pulmonary Vein",
                optionC = "Vena Cava",
                optionD = "Aorta",
                correctOption = "B",
                marks = 4,
                explanation = "The pulmonary vein is the only vein carrying oxygenated blood."
            ),

            // Test 3: Yoruba
            CbtQuestion(
                id = 11,
                testId = 3,
                questionNumber = 1,
                questionText = "Kini a n pe ounje abinibi Yoruba ti a fi ewa ati agbado pese ti a mo si Ofuloju?",
                optionA = "Asaro",
                optionB = "Ofuloju",
                optionC = "Adalu",
                optionD = "Ekuru",
                correctOption = "B",
                marks = 4,
                explanation = "Ofuloju je ounje abinibi ti a se pelu imototo ati asa ile Yoruba."
            ),
            CbtQuestion(
                id = 12,
                testId = 3,
                questionNumber = 2,
                questionText = "Ewo ninu awon wonyi kii se owo eya Yoruba?",
                optionA = "Ijebu",
                optionB = "Egbaland",
                optionC = "Ijaw",
                optionD = "Oyo",
                correctOption = "C",
                marks = 4,
                explanation = "Eya Ijaw je eya otooto ni agbegbe Niger Delta."
            )
        )

        // CBT Submissions (Pre-seeded results)
        val initialCbtSubmissions = listOf(
            CbtSubmission(
                id = 1,
                testId = 1,
                studentName = "Adeleke David Oluwaseun",
                studentReg = "GRS/2024/0428",
                studentClass = "SS 1 Science",
                score = 20,
                maxScore = 20,
                percentage = 100.0,
                isReviewedByTeacher = true,
                teacherFeedback = "Flawless score! Exceptional mastery of quadratic expressions and algebra.",
                submissionDate = "22 Feb 2025, 09:15 AM",
                answersJson = "1:A,2:B,3:A,4:A,5:B"
            ),
            CbtSubmission(
                id = 2,
                testId = 1,
                studentName = "Chinedu Emmanuel K.",
                studentReg = "GRS/2024/0430",
                studentClass = "SS 1 Science",
                score = 16,
                maxScore = 20,
                percentage = 80.0,
                isReviewedByTeacher = true,
                teacherFeedback = "Well done! Review gradient calculation for questions involving negative coordinates.",
                submissionDate = "22 Feb 2025, 09:20 AM",
                answersJson = "1:A,2:A,3:A,4:A,5:B"
            ),
            CbtSubmission(
                id = 3,
                testId = 2,
                studentName = "Adeleke David Oluwaseun",
                studentReg = "GRS/2024/0428",
                studentClass = "SS 1 Science",
                score = 18,
                maxScore = 20,
                percentage = 90.0,
                isReviewedByTeacher = true,
                teacherFeedback = "Strong grasp of ecological trophic levels. Excellent performance!",
                submissionDate = "22 Feb 2025, 11:45 AM",
                answersJson = "1:C,2:C,3:B,4:C,5:A"
            )
        )

        // Initial Group Chat Messages
        val initialGroupMessages = listOf(
            // SS 1 Science Class Group Chat (Class Teacher + Students + Admin)
            GroupChatMessage(
                id = 1,
                channelId = "class_ss1_science",
                channelTitle = "SS 1 Science Class Group",
                senderName = "Mr. Adeleke Ayomide",
                senderRole = "TEACHER",
                senderId = "GRS/STF/2021/014",
                text = "Good morning Royal Scholars! Please review the quadratic equations exercises posted under homework. The CBT assessment is now LIVE on your portal.",
                attachmentType = "PAST_QUESTION",
                attachmentName = "SS1_Math_2ndTerm_Revision_Pack.pdf",
                timestamp = "08:30 AM",
                isPinned = true
            ),
            GroupChatMessage(
                id = 2,
                channelId = "class_ss1_science",
                channelTitle = "SS 1 Science Class Group",
                senderName = "Adeleke David Oluwaseun",
                senderRole = "STUDENT",
                senderId = "GRS/2024/0428",
                text = "Thank you sir! I have submitted my solution to the trophic energy diagram and completed the CBT test.",
                attachmentType = "NONE",
                attachmentName = "",
                timestamp = "09:22 AM"
            ),
            GroupChatMessage(
                id = 3,
                channelId = "class_ss1_science",
                channelTitle = "SS 1 Science Class Group",
                senderName = "Mrs. B. Okonkwo",
                senderRole = "TEACHER",
                senderId = "GRS/STF/2019/008",
                text = "Attached are the lab guide notes for next week's biology chlorophyll experiment. Read through before Monday.",
                attachmentType = "DOCUMENT",
                attachmentName = "Biology_Lab_Guide_Photosynthesis.pdf",
                timestamp = "10:15 AM"
            ),
            GroupChatMessage(
                id = 4,
                channelId = "class_ss1_science",
                channelTitle = "SS 1 Science Class Group",
                senderName = "Mr. Tobi Adebayo",
                senderRole = "ADMIN",
                senderId = "GRS/FND/001",
                text = "Notice from Administration: Keep all interactions academic, respectful, and disciplined. Excellence is our standard.",
                attachmentType = "NONE",
                attachmentName = "",
                timestamp = "11:00 AM"
            ),

            // Staff Room Chat (Teachers + Admin)
            GroupChatMessage(
                id = 5,
                channelId = "staff_room",
                channelTitle = "Graziel Royal Faculty Staff Room",
                senderName = "Mr. Tobi Adebayo",
                senderRole = "ADMIN",
                senderId = "GRS/FND/001",
                text = "Colleagues, kindly ensure all 2nd Term Continuous Assessment (CA) scores and CBT question sets are entered for admin approval by Friday 4 PM.",
                attachmentType = "DOCUMENT",
                attachmentName = "Academic_Grading_Schedule_2025.pdf",
                timestamp = "07:45 AM",
                isPinned = true
            ),
            GroupChatMessage(
                id = 6,
                channelId = "staff_room",
                channelTitle = "Graziel Royal Faculty Staff Room",
                senderName = "Mr. Adeleke Ayomide",
                senderRole = "TEACHER",
                senderId = "GRS/STF/2021/014",
                text = "SS 1 Mathematics CBT scores have been reviewed and published. Report card grades are submitted for administrative endorsement.",
                attachmentType = "NONE",
                attachmentName = "",
                timestamp = "08:15 AM"
            ),
            GroupChatMessage(
                id = 7,
                channelId = "staff_room",
                channelTitle = "Graziel Royal Faculty Staff Room",
                senderName = "Ogbeni O. Balogun",
                senderRole = "TEACHER",
                senderId = "GRS/STF/2020/022",
                text = "Cultural day preparations are progressing smoothly. The Yoruba cuisine exhibition stand (Ofuloju and Iyan) is fully organized.",
                attachmentType = "IMAGE",
                attachmentName = "cultural_pavilion_setup.jpg",
                timestamp = "09:40 AM"
            )
        )

        dao.insertAnnouncements(initialAnnouncements)
        dao.insertAssignments(initialAssignments)
        dao.insertFeeItems(initialFees)
        dao.insertPayments(initialPayments)
        dao.insertAttendanceRecords(initialAttendance)
        dao.insertStaffClockRecords(initialStaffClock)
        dao.insertCbtTests(initialCbtTests)
        dao.insertCbtQuestions(initialCbtQuestions)
        dao.insertCbtSubmissions(initialCbtSubmissions)
        dao.insertGroupChatMessages(initialGroupMessages)
    }

    // CBT Operations
    suspend fun createCbtTest(test: CbtTest, questions: List<CbtQuestion>) {
        val testId = dao.insertCbtTest(test).toInt()
        val questionsWithId = questions.map { it.copy(testId = testId) }
        dao.insertCbtQuestions(questionsWithId)
    }

    suspend fun setCbtLiveStatus(testId: Int, isLive: Boolean) {
        dao.updateCbtLiveStatus(testId, isLive)
    }

    suspend fun setCbtPublishStatus(testId: Int, isPublished: Boolean) {
        dao.updateCbtPublishStatus(testId, isPublished)
    }

    suspend fun submitCbtExam(submission: CbtSubmission) {
        dao.insertCbtSubmission(submission)
    }

    suspend fun updateCbtSubmissionScore(submission: CbtSubmission) {
        dao.updateCbtSubmission(submission)
    }

    // Staff Clock-In / Clock-Out
    suspend fun clockInStaff(staffName: String, staffId: String) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val nowTime = timeFormat.format(Date())
        val nowDate = dateFormat.format(Date())

        val record = StaffClockRecord(
            staffName = staffName,
            staffId = staffId,
            date = nowDate,
            clockInTime = nowTime,
            status = "CLOCKED_IN"
        )
        dao.insertStaffClockRecord(record)
    }

    suspend fun clockOutStaff(recordId: Int) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val nowTime = timeFormat.format(Date())
        // In a real app we'd update by ID
        val updated = StaffClockRecord(
            id = recordId,
            staffName = "Mr. Adeleke Ayomide",
            staffId = "GRS/STF/2021/014",
            date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            clockInTime = "07:30 AM",
            clockOutTime = nowTime,
            status = "CLOCKED_OUT"
        )
        dao.updateStaffClockRecord(updated)
    }

    // Group Chat Operations
    suspend fun sendGroupMessage(message: GroupChatMessage) {
        dao.insertGroupChatMessage(message)
    }

    suspend fun deleteGroupMessage(messageId: Int) {
        dao.softDeleteChatMessage(messageId)
    }

    suspend fun clearChatChannel(channelId: String) {
        dao.clearChannelMessages(channelId)
    }

    // Fee operations
    suspend fun createFeeItem(feeItem: FeeItem) {
        dao.insertFeeItem(feeItem)
    }

    suspend fun payFeeItem(feeItem: FeeItem, paymentMethod: String, studentName: String = "Adeleke David O.", studentId: String = "GRS/2024/0428"): PaymentTransaction {
        val updated = feeItem.copy(isPaid = true)
        dao.updateFeeItem(updated)

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        val receiptNo = "GRS-REC-${System.currentTimeMillis().toString().takeLast(6)}"

        val transaction = PaymentTransaction(
            receiptNumber = receiptNo,
            title = feeItem.title,
            amount = feeItem.amount,
            date = dateStr,
            paymentMethod = paymentMethod,
            status = "SUCCESS",
            studentName = studentName,
            studentId = studentId,
            academicTerm = feeItem.term
        )
        dao.insertPayment(transaction)
        return transaction
    }

    suspend fun addAnnouncement(announcement: Announcement) {
        dao.insertAnnouncement(announcement)
    }

    suspend fun addAssignment(assignment: Assignment) {
        dao.insertAssignment(assignment)
    }

    suspend fun updateAssignmentFull(assignment: Assignment) {
        dao.updateAssignment(assignment)
    }

    suspend fun submitAdmissionApplication(app: AdmissionApplication) {
        dao.insertAdmissionApplication(app)
    }

    suspend fun recordAttendance(record: AttendanceRecord) {
        dao.insertAttendanceRecord(record)
    }

    // Terminal Report Cards with Admin approval state
    private var isTerm2Approved = true
    private var isTerm2Published = true

    fun setReportCardApproval(approved: Boolean, published: Boolean) {
        isTerm2Approved = approved
        isTerm2Published = published
    }

    fun isReportApproved(): Boolean = isTerm2Approved
    fun isReportPublished(): Boolean = isTerm2Published

    fun getReportCard(term: String): TermReport {
        return if (term.contains("1st")) {
            TermReport(
                reportId = "TERM1_2024_2025",
                termName = "1st Term",
                session = "2024/2025 Academic Session",
                studentName = "Adeleke David Oluwaseun",
                studentReg = "GRS/2024/0428",
                studentClass = "SS 1 (Science)",
                classPopulation = 28,
                totalObtained = 884,
                totalPossible = 1000,
                averageScore = 88.4,
                classPosition = 3,
                attendanceDays = 64,
                totalDays = 65,
                isApprovedByAdmin = true,
                isPublished = true,
                formTeacherRemark = "David demonstrates exceptional diligence, keen curiosity, and exemplary discipline.",
                principalRemark = "An outstanding performance. Maintain this royal standard of excellence.",
                grades = listOf(
                    SubjectGrade(1, "Mathematics", 14, 14, 9, 53, 90, "A1", "Excellent", 2, "Mr. A. Adeleke"),
                    SubjectGrade(2, "English Language", 13, 14, 9, 52, 88, "A1", "Commendable", 4, "Mrs. F. Williams"),
                    SubjectGrade(3, "Physics & Mechanics", 15, 14, 10, 56, 95, "A1", "Distinction", 1, "Mrs. B. Okonkwo"),
                    SubjectGrade(4, "Chemistry", 14, 15, 9, 54, 92, "A1", "Very Good", 2, "Dr. O. Adekunle"),
                    SubjectGrade(5, "Biology", 15, 15, 10, 58, 98, "A1", "Exceptional", 1, "Mrs. B. Okonkwo"),
                    SubjectGrade(6, "ICT & Robotics", 15, 15, 10, 58, 98, "A1", "Exceptional", 1, "Engr. K. Chinedu"),
                    SubjectGrade(7, "Civic Education", 13, 13, 8, 48, 82, "B2", "Good", 5, "Mr. E. Mensah"),
                    SubjectGrade(8, "Yoruba Language & Culture", 14, 15, 9, 54, 92, "A1", "Very Good", 2, "Ogbeni O. Balogun"),
                    SubjectGrade(9, "Agricultural Science", 14, 14, 9, 51, 88, "A1", "Very Good", 3, "Mr. S. Bello"),
                    SubjectGrade(10, "Technical Drawing", 13, 14, 9, 49, 85, "B2", "Creative", 4, "Engr. D. Victor")
                )
            )
        } else {
            // 2nd Term
            TermReport(
                reportId = "TERM2_2024_2025",
                termName = "2nd Term Assessment",
                session = "2024/2025 Academic Session",
                studentName = "Adeleke David Oluwaseun",
                studentReg = "GRS/2024/0428",
                studentClass = "SS 1 (Science)",
                classPopulation = 28,
                totalObtained = 912,
                totalPossible = 1000,
                averageScore = 91.2,
                classPosition = 2,
                attendanceDays = 38,
                totalDays = 40,
                isApprovedByAdmin = isTerm2Approved,
                isPublished = isTerm2Published,
                formTeacherRemark = "Consistently proactive, outstanding in mathematics and biology assessments.",
                principalRemark = "Royalty exemplified in academic character and conduct. Keep shining!",
                grades = listOf(
                    SubjectGrade(1, "Mathematics", 15, 14, 10, 55, 94, "A1", "Distinction", 1, "Mr. A. Adeleke"),
                    SubjectGrade(2, "English Language", 14, 14, 9, 53, 90, "A1", "Excellent", 3, "Mrs. F. Williams"),
                    SubjectGrade(3, "Physics & Mechanics", 15, 15, 10, 56, 96, "A1", "Distinction", 1, "Mrs. B. Okonkwo"),
                    SubjectGrade(4, "Chemistry", 14, 14, 9, 53, 90, "A1", "Excellent", 2, "Dr. O. Adekunle"),
                    SubjectGrade(5, "Biology", 15, 15, 10, 57, 97, "A1", "Distinction", 1, "Mrs. B. Okonkwo"),
                    SubjectGrade(6, "ICT & Robotics", 15, 15, 10, 59, 99, "A1", "Exceptional", 1, "Engr. K. Chinedu"),
                    SubjectGrade(7, "Civic Education", 14, 13, 9, 50, 86, "B2", "Very Good", 4, "Mr. E. Mensah"),
                    SubjectGrade(8, "Yoruba Language & Culture", 15, 15, 10, 55, 95, "A1", "Outstanding", 1, "Ogbeni O. Balogun"),
                    SubjectGrade(9, "Agricultural Science", 15, 14, 9, 52, 90, "A1", "Excellent", 2, "Mr. S. Bello"),
                    SubjectGrade(10, "Technical Drawing", 14, 15, 10, 51, 90, "A1", "Very Creative", 2, "Engr. D. Victor")
                )
            )
        }
    }

    // Weekly Timetable Data
    fun getTimetableForDay(day: String): List<TimetablePeriod> {
        return when (day.lowercase()) {
            "monday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "School Assembly & Devotion", "All Staff", "Assembly Hall", isBreak = true),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Mathematics", "Mr. A. Adeleke", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "English Language", "Mrs. F. Williams", "Room SS1-A"),
                TimetablePeriod(4, "10:15 - 10:45 AM", "Short Break / Snack Time", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(5, "10:45 - 11:30 AM", "Physics & Mechanics", "Mrs. B. Okonkwo", "Physics Lab 1"),
                TimetablePeriod(6, "11:30 - 12:15 PM", "ICT & Coding (Python / Scratch)", "Engr. K. Chinedu", "ICT Centre"),
                TimetablePeriod(7, "12:15 - 01:00 PM", "Lunch & Mid-Day Recreation", "-", "School Dining Hall", isBreak = true),
                TimetablePeriod(8, "01:00 - 01:45 PM", "Yoruba Language", "Ogbeni O. Balogun", "Room SS1-A"),
                TimetablePeriod(9, "01:45 - 02:30 PM", "Chemistry Practical", "Dr. O. Adekunle", "Chemistry Lab"),
                TimetablePeriod(10, "02:30 - 03:15 PM", "Club Activities & Mentorship", "Club Mentors", "Auditorium")
            )
            "tuesday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Homeroom & Form Period", "Mr. A. Adeleke", "Room SS1-A"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "English Language", "Mrs. F. Williams", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Mathematics (Calculus)", "Mr. A. Adeleke", "Room SS1-A"),
                TimetablePeriod(4, "10:15 - 10:45 AM", "Snack Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(5, "10:45 - 11:30 AM", "Biology", "Mrs. B. Okonkwo", "Biology Lab"),
                TimetablePeriod(6, "11:30 - 12:15 PM", "Technical Drawing", "Engr. D. Victor", "Drawing Studio"),
                TimetablePeriod(7, "12:15 - 01:00 PM", "Lunch Break", "-", "Dining Hall", isBreak = true),
                TimetablePeriod(8, "01:00 - 01:45 PM", "Agricultural Science", "Mr. S. Bello", "School Farm / Garden"),
                TimetablePeriod(9, "01:45 - 02:30 PM", "Civic Education", "Mr. E. Mensah", "Room SS1-A"),
                TimetablePeriod(10, "02:30 - 03:15 PM", "Supervised Prep & Library Study", "Librarian", "School Library")
            )
            "wednesday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Morning Hymns & Moral Instruction", "Chaplain", "Assembly Hall"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Mathematics", "Mr. A. Adeleke", "Room SS1-A"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Physics Practical", "Mrs. B. Okonkwo", "Physics Lab"),
                TimetablePeriod(4, "10:15 - 10:45 AM", "Snack Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(5, "10:45 - 11:30 AM", "Robotics & STEM Workshop", "Engr. K. Chinedu", "STEM Innovation Hub"),
                TimetablePeriod(6, "11:30 - 12:15 PM", "English Literature", "Mrs. F. Williams", "Room SS1-A"),
                TimetablePeriod(7, "12:15 - 01:00 PM", "Lunch Break", "-", "Dining Hall", isBreak = true),
                TimetablePeriod(8, "01:00 - 01:45 PM", "Chemistry", "Dr. O. Adekunle", "Room SS1-A"),
                TimetablePeriod(9, "01:45 - 02:30 PM", "Music & Choir Training", "Mr. David Victor", "Music Studio"),
                TimetablePeriod(10, "02:30 - 03:30 PM", "Inter-House Sports Practice", "Coaches", "Sports Complex")
            )
            "thursday" -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "Mental Math & Science Drill", "Mr. Adeleke", "Room SS1-A"),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Yoruba Language (Cultural Project)", "Ogbeni O. Balogun", "Culture Pavilion"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "Biology", "Mrs. B. Okonkwo", "Room SS1-A"),
                TimetablePeriod(4, "10:15 - 10:45 AM", "Snack Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(5, "10:45 - 11:30 AM", "Mathematics", "Mr. A. Adeleke", "Room SS1-A"),
                TimetablePeriod(6, "11:30 - 12:15 PM", "Physical & Health Education", "Coach K. Adey", "Gymnasium"),
                TimetablePeriod(7, "12:15 - 01:00 PM", "Lunch Break", "-", "Dining Hall", isBreak = true),
                TimetablePeriod(8, "01:00 - 01:45 PM", "Chemistry Lab Analysis", "Dr. O. Adekunle", "Chemistry Lab"),
                TimetablePeriod(9, "01:45 - 02:30 PM", "French Conversation", "Mme. D. Laurent", "Language Lab"),
                TimetablePeriod(10, "02:30 - 03:15 PM", "CBT Clinic & Tutorial", "Tutors", "ICT Centre")
            )
            else -> listOf(
                TimetablePeriod(1, "08:00 - 08:45 AM", "General School Assembly", "Principal & Staff", "Assembly Hall", isBreak = true),
                TimetablePeriod(2, "08:45 - 09:30 AM", "Weekly CBT Revision Quiz", "All Tutors", "ICT Centre"),
                TimetablePeriod(3, "09:30 - 10:15 AM", "ICT & Presentation Skills", "Engr. K. Chinedu", "ICT Centre"),
                TimetablePeriod(4, "10:15 - 10:45 AM", "Snack Break", "-", "Cafeteria", isBreak = true),
                TimetablePeriod(5, "10:45 - 11:30 AM", "Creative Writing & Debate", "Mrs. F. Williams", "Auditorium"),
                TimetablePeriod(6, "11:30 - 12:30 PM", "Devotional & Closing Fellowship", "Chaplaincy", "Auditorium"),
                TimetablePeriod(7, "12:30 - 01:30 PM", "School Dismissal & Weekend Pickup", "-", "Main Gate", isBreak = true)
            )
        }
    }
}
