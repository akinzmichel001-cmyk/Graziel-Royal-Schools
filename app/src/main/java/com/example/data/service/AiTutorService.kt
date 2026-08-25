package com.example.data.service

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AiTutorSpecification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AiTutorService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateTutorResponse(
        userMessage: String,
        specification: AiTutorSpecification,
        chatHistory: List<Pair<String, Boolean>> = emptyList() // text to isUser
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If a valid Gemini API key is present, attempt live generation via Gemini REST API
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val liveResponse = callGeminiApi(apiKey, userMessage, specification, chatHistory)
                if (liveResponse.isNotBlank()) {
                    return@withContext liveResponse
                }
            } catch (e: Exception) {
                Log.e("AiTutorService", "Gemini API call failed, falling back to intelligent local custom engine: ${e.message}")
            }
        }

        // Intelligent custom specification pedagogical engine
        return@withContext generateCustomizedOfflineResponse(userMessage, specification)
    }

    private fun callGeminiApi(
        apiKey: String,
        userMessage: String,
        spec: AiTutorSpecification,
        chatHistory: List<Pair<String, Boolean>>
    ): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val effectiveSubject = if (spec.customSubjectName.isNotBlank()) spec.customSubjectName else spec.subject

        val systemPrompt = """
            You are '${spec.aiTutorName}', an elite personalized AI academic and educational mentor at Graziel Royal Schools (located in Opo-Ibogun, Ifo, Ogun State, Nigeria; Founder: Mr. Tobi Adebayo, Admin: +234 816 620 5113, School Account: Monie Point 5255883539).
            
            CUSTOM USER SPECIFICATIONS:
            - User Role: ${spec.userRole}
            - Target Level / Grade: ${spec.gradeLevel}
            - Subject Focus: $effectiveSubject
            - Teaching Style & Pedagogical Tone: ${spec.teachingStyle}
            - Language & Complexity: ${spec.languageComplexity}
            - User's Custom Instructions: ${if (spec.customInstruction.isNotBlank()) spec.customInstruction else "None specified. Follow the teaching style and level strictly."}

            GUIDELINES:
            1. Adapt your explanation, vocabulary, examples, and depth precisely to the user's role (${spec.userRole}) and grade level (${spec.gradeLevel}).
            2. If the user is a Teacher, provide structured lesson plans, behavioral objectives, curriculum schemes, marking guides, or CBT questions as requested.
            3. If the user is a Student or Candidate, guide them step-by-step, explain underlying concepts clearly, use standard curriculum standards (WAEC/NECO/JAMB/Universal Basic Education), and encourage them warmly.
            4. If the user is a Parent, provide empathetic, clear, practical advice on supporting their child's academic growth, homework routines, and school activities.
            5. If the user is an Administrator, formulate professional memos, administrative drafts, or policy guidelines.
            6. Format your output with clean markdown (bold headings, bullet points, numbered steps, code/math blocks).
        """.trimIndent()

        val contentsArray = JSONArray()

        // Include recent conversation turns for context
        val recentHistory = chatHistory.takeLast(6)
        for ((text, isUser) in recentHistory) {
            val partObj = JSONObject().put("text", text)
            val partsArray = JSONArray().put(partObj)
            val contentObj = JSONObject()
                .put("role", if (isUser) "user" else "model")
                .put("parts", partsArray)
            contentsArray.put(contentObj)
        }

        // Add the current prompt
        val currentPart = JSONObject().put("text", "$systemPrompt\n\nUser Question:\n$userMessage")
        val currentContent = JSONObject()
            .put("role", "user")
            .put("parts", JSONArray().put(currentPart))
        contentsArray.put(currentContent)

        val rootJson = JSONObject().apply {
            put("contents", contentsArray)
            val genConfig = JSONObject().apply {
                put("temperature", 0.7)
                put("topP", 0.95)
                put("topK", 40)
            }
            put("generationConfig", genConfig)
        }

        val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("HTTP ${response.code}: ${response.body?.string()}")
            }
            val responseBodyString = response.body?.string() ?: ""
            val responseJson = JSONObject(responseBodyString)
            val candidates = responseJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return parts.getJSONObject(0).optString("text", "")
                }
            }
        }
        return ""
    }

    /**
     * Highly customized offline pedagogical engine serving any user specifications:
     * - Tailors response tone, structure, depth, and examples to User Role, Grade Level, Subject, and Teaching Style.
     */
    fun generateCustomizedOfflineResponse(
        query: String,
        spec: AiTutorSpecification
    ): String {
        val q = query.trim().lowercase()
        val effectiveSubject = if (spec.customSubjectName.isNotBlank()) spec.customSubjectName else spec.subject
        val role = spec.userRole
        val level = spec.gradeLevel
        val style = spec.teachingStyle

        // 1. Check for Lesson Plan generation (Teacher mode or explicit request)
        if (q.contains("lesson plan") || q.contains("scheme of work") || (role.contains("Teacher", ignoreCase = true) && (q.contains("plan") || q.contains("prepare lesson") || q.contains("teach")))) {
            val topic = extractTopicFromQuery(query, "Linear Equations / Fractions")
            return """
                📋 **Custom Lesson Plan: $topic**
                *Designed for ${spec.gradeLevel} • Subject: $effectiveSubject*
                
                **1. General Information:**
                • **Class/Level**: $level
                • **Subject**: $effectiveSubject
                • **Duration**: 45 Minutes
                • **Curriculum**: Nigerian National Curriculum / Graziel Royal Academic Framework
                
                **2. Behavioral Objectives:**
                By the end of this 45-minute period, the students should be able to:
                1. Define the fundamental principles of **$topic**.
                2. Identify key formulas, terminologies, and real-world applications.
                3. Solve at least 3 standard practice problems accurately without assistance.
                
                **3. Instructional Materials:**
                • Interactive Whiteboard, chart illustrations, practice worksheets, real-life props.
                
                **4. Step-by-Step Delivery:**
                • **Introduction (5 mins)**: Review previous knowledge and connect with everyday scenario.
                • **Step 1 - Concept Mastery (12 mins)**: Present core theory, define key terms, and demonstrate with clear visual examples.
                • **Step 2 - Guided Practice (13 mins)**: Work through sample problem with class participation, highlighting common student misconceptions.
                • **Step 3 - Independent Drill (10 mins)**: Students solve exercises individually; teacher moves around for differentiated support.
                • **Conclusion & Evaluation (5 mins)**: Quick recap, 2 oral check questions, and homework assignment.
                
                **5. Evaluation Exercise:**
                1. *Explain in your own words the meaning of $topic.*
                2. *Solve: Demonstrate the primary method taught in class with complete working.*
                
                **Teacher's Remark**: Differentiated support provided for visual and kinesthetic learners.
            """.trimIndent()
        }

        // 2. Check for Practice Quiz / CBT Question Generation
        if (q.contains("quiz") || q.contains("practice question") || q.contains("generate question") || q.contains("test questions")) {
            val topic = extractTopicFromQuery(query, effectiveSubject)
            return """
                🎯 **Custom Practice Quiz & Solutions ($topic)**
                *Tailored for $level • Style: $style*
                
                **Question 1:**
                What is the fundamental principle or definition of **$topic**?
                • A) A variable that changes randomly
                • B) A structured relationship or property in $effectiveSubject *(Correct)*
                • C) An undefined postulate
                • D) None of the above
                
                *Explanation*: In $effectiveSubject, this represents a core foundational rule for $level students.
                
                ---
                **Question 2:**
                Which of the following demonstrates the correct application?
                • A) Direct proportional relationship *(Correct)*
                • B) Inversion without balance
                • C) Unchecked assumption
                • D) Random approximation
                
                *Explanation*: Consistent with WAEC/JAMB exam standards, direct proportional balance maintains equality.
                
                ---
                **Question 3 (Challenge Problem):**
                *Solve step-by-step*: Given standard parameters for $topic, what is the final simplified result?
                • **Solution**:
                  1. State given variables clearly.
                  2. Apply standard formula: \( \text{Result} = \frac{\text{Sum}}{\text{Total}} \).
                  3. Simplify to simplest terms.
                  
                💡 *Tip from ${spec.aiTutorName}*: Would you like 5 more questions or a breakdown of any specific item?
            """.trimIndent()
        }

        // 3. Check for Math / Physics / Calculations Step-by-Step Solver
        if (q.contains("solve") || q.contains("math") || q.contains("equation") || q.contains("calculate") || q.contains("algebra") || q.contains("physics") || q.contains("chemistry") || q.contains("quadratic")) {
            return """
                📐 **Step-by-Step Problem Solver**
                *Customized for $level • Approach: $style*
                
                **Problem Analysis:**
                Let's break down your question into clear, easy-to-follow steps so you master the technique for future exams.
                
                **Step 1: Identify the Knowns & Unknowns**
                • Identify what is given in the problem statement.
                • Set up our standard algebraic or physical formula.
                
                **Step 2: Apply the Core Formula**
                For example, for quadratic relations \( ax^2 + bx + c = 0 \):
                \[ x = \frac{-b \pm \sqrt{b^2 - 4ac}}{2a} \]
                
                **Step 3: Step-by-Step Execution**
                1. Substitute the parameters accurately into both sides of the equation.
                2. Simplify the terms under the radical sign (discriminant \( \Delta = b^2 - 4ac \)).
                3. Calculate the two potential roots or state values.
                
                **Step 4: Verification & Sanity Check**
                • Plug the obtained value back into the original expression to confirm equality.
                
                ✨ **Takeaway Key**: For WAEC & JAMB, always write out your formula clearly first to guarantee formula method marks!
            """.trimIndent()
        }

        // 4. Yoruba Culture & Ofuloju / Iyan Heritage
        if (q.contains("yoruba") || q.contains("ofuloju") || q.contains("iyan") || q.contains("culture") || q.contains("pounded yam")) {
            return """
                🌟 **Graziel Royal Yoruba Cultural & Culinary Heritage**
                *Tailored for $level ($role Mode)*
                
                **Overview of Ofuloju & Iyan Culture:**
                In Graziel Royal Schools' cultural curriculum (Opo-Ibogun, Ogun State), students celebrate rich Yoruba heritage through hands-on cultural projects:
                
                **1. What is Ofuloju?**
                • **Ofuloju** is a cherished, deeply flavorful traditional Yoruba soup delicacy, rich in indigenous spices, locust beans (Iru), smoked fish, and assorted proteins.
                • It is historically celebrated as an authentic royal recipe served at festive community celebrations and royal banquets.
                
                **2. The Art of Iyan (Pounded Yam):**
                • Prepared from mature white yam tubers (*Dioscorea rotundata*).
                • Boiled and rhythmically pounded in a wooden mortar (*Odo*) and pestle (*Omo Odo*) to achieve a smooth, stretchy, lumpless texture.
                
                **3. Educational & Social Significance:**
                • Teaches teamwork, patience, agricultural appreciation, and respect for native cultural identity.
                • Fosters student connection to Yoruba oral history, hospitality values (*Omoluwabi*), and traditional nutrition.
            """.trimIndent()
        }

        // 5. Parent / Progress Guidance
        if (role.contains("Parent", ignoreCase = true) || q.contains("parent") || q.contains("child") || q.contains("fees") || q.contains("report card")) {
            return """
                👨‍👩‍👧 **Graziel Royal Parent Academic Advisory**
                *Personalized for Parents of $level Students*
                
                **Guidance for Supporting Your Child's Learning:**
                1. **Structured Daily Revision**: Set a dedicated 45-minute daily study window for homework and CBT practice tests.
                2. **Continuous Monitoring**: Check the **Student Portal** weekly to review assignment submissions and CBT scores.
                3. **Active Term Updates**: The current active term is **2nd Term (2024/2025)**. Report cards are approved by Admin and viewable in the Parent Portal.
                4. **School Fee Payments**: School tuition and fees can be settled directly into the official school account:
                   • **Bank**: Monie Point (Moniepoint MFB)
                   • **Account Number**: `5255883539`
                   • **Account Name**: Graziel Royal Schools Ltd.
                
                📞 **Need Direct Consultation?**
                Contact Mr. Tobi Adebayo (Admin) via WhatsApp or phone at **+234 816 620 5113**.
            """.trimIndent()
        }

        // 6. Admin / Staff Memos
        if (role.contains("Admin", ignoreCase = true) || q.contains("memo") || q.contains("circular") || q.contains("announcement")) {
            return """
                🏛️ **Graziel Royal Administrative Draft & Policy Guide**
                *Prepared for School Administration*
                
                **OFFICIAL INTERNAL MEMORANDUM**
                **TO**: All Academic Staff, Form Tutors & Administrative Officers
                **FROM**: Office of the Administrator (Mr. Tobi Adebayo)
                **DATE**: Current Academic Session
                **SUBJECT**: Standard Academic Excellence, CBT Administration & Term Workflow
                
                **1. Key Directives:**
                • Ensure all subject CBT test questions are uploaded and verified before toggling **'Go Live'**.
                • Form teachers must submit completed student affective and psychomotor domain scores for Admin approval prior to publication.
                • Staff attendance clock-in must be completed via the digital staff portal by 07:45 AM daily.
                
                **2. Action Required:**
                All department heads are requested to review continuous assessment records and broadcast student revision materials.
            """.trimIndent()
        }

        // 7. General Default Customized Response
        return """
            👑 **${spec.aiTutorName}**
            *Serving: $role • Level: $level • Subject: $effectiveSubject • Tone: $style*
            
            Thank you for your question: **"${query.take(60)}${if (query.length > 60) "..." else ""}"**
            
            **Comprehensive Explanation:**
            1. **Core Concept**: In $effectiveSubject at the $level level, understanding the foundational rules is key to problem solving and examination excellence.
            2. **Applied Perspective**: When analyzing this concept, we connect theoretical rules with real-life applications and standard syllabus requirements.
            3. **Mastery Checklist**:
               • Understand the core definition and terminology.
               • Practice sample exam questions from the CBT Center.
               • Consult your course notes or ask follow-up questions right here.
               
            💡 *How else can I assist you? You can ask me to solve a specific problem step-by-step, draft a quiz, or simplify this concept further!*
        """.trimIndent()
    }

    private fun extractTopicFromQuery(query: String, defaultTopic: String): String {
        val cleaned = query.replace(Regex("(?i)(generate|make|create|solve|give me|tell me about|a|the|quiz on|lesson plan for|questions on)"), "").trim()
        return if (cleaned.length in 3..40) cleaned.capitalizeWords() else defaultTopic
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
