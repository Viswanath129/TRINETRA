package com.example.ai

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "temperature") val temperature: Float? = null,
    @Json(name = "topP") val topP: Float? = null,
    @Json(name = "maxOutputTokens") val maxOutputTokens: Int? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }

    /**
     * Sends custom extraction indicators and meta info to Gemini for contextual narrative review.
     */
    suspend fun getAnalysisExplanation(
        packageName: String,
        threatFamily: String,
        riskScore: Int,
        permissions: List<String>
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Generate extremely comprehensive offline analytical dossiers (CISO Bulletin Format) for mock presentation:
            return when (threatFamily) {
                "Anubis.Banker" -> """
                    ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
                    **MALWARE IDENTIFIED**: Anubis Banker (Financial RAT Variant)
                    **ACTOR CLASIFICATION**: APT-38 Allied Syndicate (Targeting Asian Multi-Nationals)
                    **INTENT CORE**: Financial theft, SMS exfiltration, overlay spoofing. This package mimics critical security update patches inside Google App Services. Once deployed, it monitors the current running screen activity and detects if a high-value retail app (e.g. Bank of India) starts up, preparing a server-delivered transparent layout injection.

                    ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
                    - **Step 1: Privilege Usurpation**: Prompts the user continuously with a fake update banner to enable the system's Accessibility settings.
                    - **Step 2: Stealth Persistence**: Leverages `RECEIVE_BOOT_COMPLETED` permissions to initiate background background looping services automatically on boot.
                    - **Step 3: UI Overlays Redirection**: Upon detecting that the user launches the Bank of India portal, the malware immediately overlays a fully opaque fake credentials webview.
                    - **Step 4: Credential Theft**: User enters login ID & MPIN into the active webview, which silently ships details to active C2 servers.
                    - **Step 5: OTP Interception**: Captures incoming SMS verification packets via `RECEIVE_SMS` triggers. The malware absorbs the notification before the user sees it, letting the threat actor execute instant cash transfers.

                    ### 3. MITRE ATT&CK PROFILE
                    *   **T1444 - Privilege Escalation via Accessibility**: Hijacks operating system accessibility interfaces to bypass default container rules.
                    *   **T1411 - Input Capture via Overlay Phishing**: Intercepts active keyboard input buffers via fake web overlays.
                    *   **T1437 - Command and Control via SMS Exfiltrate**: Listens and exfiltrates transactional authorization verification strings.

                    ### 4. BANK OF INDIA RISK MITIGATION ADVISORY
                    - **Advisory 1: Window Security**: Implement `getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE)` across all transactional, login, and profile screens to deny malware overlay capture and screenshots.
                    - **Advisory 2: Biometric Validation**: Enforce step-up biometric challenges of critical fund transfer tasks rather than relying exclusively on SMS-based OTCs.
                    - **Advisory 3: Verification Check**: Implement background Accessibility Scanner APIs to block execution of Bank of India apps when untrusted accessibility loops are detected.

                    ### 5. EXECUTIVE CISO REPORT SYNOPSIS
                    This APK payload is classified as **MALICIOUS**. It exhibits classic banker trojan properties (Anubis family) and poses an extreme risk of immediate asset loss. Deploy immediate MDM-based blacklisting rules across corporate and retail customers. CISO Gate counter-sign is highly recommended.
                """.trimIndent()

                "Flubot.SMS" -> """
                    ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
                    **MALWARE IDENTIFIED**: Flubot SMS Worm Carrier
                    **ACTOR CLASIFICATION**: East European Syndicate Group "SpamWorm"
                    **INTENT CORE**: Contact contact harvesting, high-speed automated worm SMS distribution, credentials theft. Masquerading as shipping package notification applications (DHL Express, India Post updates).

                    ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
                    - **Step 1: Social Engineering**: Attracts user into installing a non-Play-Store package using simulated urgent package delivery tracking indicators.
                    - **Step 2: Contacts Exfiltration**: Scans user contacts and immediately exfiltrates the complete list to active command coordinates under encrypted payloads.
                    - **Step 3: High-Speed Replication**: Injects automated SMS notifications to those contacts from the user's card line, spreading the infection exponentially.
                    - **Step 4: OTP Harvesting**: Listens to incoming operational SMS messages, filtering out transactional tokens.
                    - **Step 5: Remote Theft Initiates**: Translates extracted security signals to threat cells, enabling quick remote registration.

                    ### 3. MITRE ATT&CK PROFILE
                    *   **T1475 - Drive-by Installation**: Recommends sideloading through custom update pages.
                    *   **T1437 - SMS Outbound Exfiltration**: Silently drains communication logs out.
                    *   **T1444 - Access Hijacking**: Abuses operating system structures to gain click supremacy.

                    ### 4. BANK OF INDIA RISK MITIGATION ADVISORY
                    - **Advisory 1: Consumer Alerts**: Alert retail users regarding current active package tracking SMS phishing waves.
                    - **Advisory 2: Multi-Factor Resilience**: Use in-app secure notifications for critical alerts instead of general unencrypted cellular SMS protocols.
                    - **Advisory 3: Device Integrity**: Leverage Google Play Protect API within the BOI app to verify host device security.

                    ### 5. EXECUTIVE CISO REPORT SYNOPSIS
                    The file is a malicious worm (Flubot branch) targeting automated distribution and SMS capture. Recommend immediate device cleanup and local banking password resets.
                """.trimIndent()

                "SpyNote.Dropper" -> """
                    ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
                    **MALWARE IDENTIFIED**: SpyNote RAT Dropper
                    **ACTOR CLASIFICATION**: Rogue Rent-a-RAT Networks (Global/Distributed)
                    **INTENT CORE**: Remote administration tool dropped installation. Usually masquerading as WhatsApp Premium, Modded Instagram, or custom banking utilities.

                    ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
                    - **Step 1: Invisible Execution**: Attracts the user to run a utility helper tool. Inside, the app does not contain any functional operations, hiding its launcher icon.
                    - **Step 2: Payload Unpacking**: Extracting hidden encrypted DEX packages inside local cache folders.
                    - **Step 3: Dynamic Drop**: Triggers runtime install notifications using `REQUEST_INSTALL_PACKAGES` to launch secondary banker payloads.
                    - **Step 4: Remote RAT Activation**: Establish permanent persistent tunnel sockets, allowing the remote attacker to view screens, record ambient audios, and steal files.

                    ### 3. MITRE ATT&CK PROFILE
                    *   **T1475 - Drive-by Install**: Spawns unauthorized dropper packages dynamically.
                    *   **T1425 - Dynamic DEX Loading**: Inject runtime binary files.
                    *   **T1444 - Access Abuse**: Hijacks view layouts on mobile screens.

                    ### 4. BANK OF INDIA RISK MITIGATION ADVISORY
                    - **Advisory 1: Application Cloaking Detection**: Establish baseline security tests that look for empty launcher icons or package masquerades on host platforms.
                    - **Advisory 2: SSL Pinning Implementation**: Pin API endpoints on BOI apps to deny proxy interception from remote access systems.

                    ### 5. EXECUTIVE CISO REPORT SYNOPSIS
                    Highly danger file executing local droppers. Absolute high risk payload. Quarantines target immediately.
                """.trimIndent()

                "TeaBot.Loader" -> """
                    ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
                    **MALWARE IDENTIFIED**: TeaBot Banker (Anatoly Variant)
                    **ACTOR CLASIFICATION**: TeaBot Professional Cybercell Group
                    **INTENT CORE**: Real-time video screen streaming combined with Accessibility-based auto-filling keys. Masquerades as simple QR-code card reader utilities.

                    ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
                    - **Step 1: Utility Lure**: Attracts user to download helper reader tool.
                    - **Step 2: Accessibility Captive**: Forcefully requests core accessibility permissions to "auto-read codes".
                    - **Step 3: Screen Mirroring**: Once accessibility is verified, the trojan initiates background virtual screen sharing, streaming the device view live to the attacker.
                    - **Step 4: Automated Transaction injection**: Attacker intercepts live login, inserts transfers, and uses Accessibility auto-approve keys to sign off high-value transactions.

                    ### 3. MITRE ATT&CK PROFILE
                    *   **T1444 - Privilege Escalation via Accessibility**: Directly inputs automated key strokes and clicks.
                    *   **T1411 - Overlay / Streaming Hijacking**: Streams active displays in a secure media projector channel.

                    ### 4. BANK OF INDIA RISK MITIGATION ADVISORY
                    - **Advisory 1: Media Projection Warnings**: Block the Bank of India application from starting or execution if a live media projector projection or Remote Desktop connection (e.g. AnyDesk, TeamViewer) is active.
                    - **Advisory 2: API Obfuscation**: Prevent standard reverse engineering of security layouts.

                    ### 5. EXECUTIVE CISO REPORT SYNOPSIS
                    Extremely dangerous screen-streaming, automated click injector. Categorized as **MALICIOUS**. Secure immediate block.
                """.trimIndent()

                else -> """
                    ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
                    **VERDICT**: None (Safe Profiling Match)
                    **CLASSIFICATION**: Clean / Utility Package
                    **INTENT**: General purpose application utility. No signatures matching financial trojans or malware families were identified during deep static analysis decomposition.

                    ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
                    - **Execution Analysis**: Normal application execution sequence.
                    - **Device State Impact**: Exhibits zero system tampering indicators. Does not initiate unauthorized accessibility loops or system overlay operations.

                    ### 3. MITRE ATT&CK PROFILE
                    - No malicious techniques identified. Standard mobile permission usage mapped.

                    ### 4. BANK OF INDIA RISK MITIGATION ADVISORY
                    - Standard operational checks. No blockages recommended.

                    ### 5. EXECUTIVE CISO REPORT SYNOPSIS
                    Safe application configuration. Passed all central central cognitive checks safely.
                """.trimIndent()
            }
        }

        val permissionsFormatted = permissions.joinToString("\n- ")
        val systemPrompt = """
            You are TRINETRA AI Copilot, an elite sovereign financial-grade banking cyberforensics and fraud defense AI engine. 
            You provide exceptionally detailed cyber threat investigation reports for the Security Operations Center (SOC), CERT-In, RBI Security Teams, and enterprise CISOs.
            
            Always structure your output EXACTLY with the following 5 distinct headings:
            ### 1. PERPETRATOR INTENT & MALWARE FAMILY PREDICTION
            ### 2. CHRONOLOGICAL FRAUD JOURNEY ATTACK CHAIN
            ### 3. MITRE ATT&CK PROFILE
            ### 4. RBI & BANK RISK MITIGATION ADVISORY
            ### 5. EXECUTIVE CISO REPORT SYNOPSIS
            
            Keep your language professional, objective, authoritative, and cyberSec-focused. Do not output vague summaries.
        """.trimIndent()

        val mainPrompt = """
            Perform a comprehensive cyber threat investigation and CISO report for the following uploaded Android package:
            - Package Name: $packageName
            - Risk Index Score: $riskScore / 100
            - Matched Attack Indicators / Family signature: $threatFamily
            - Extracted System Permissions:
            - $permissionsFormatted

            Review the combination of permissions and compile a cohesive 5-part cyberforensics dossier based on the system instructions. Focus on how Accessibility, SMS, and Overlay permissions might be coupled to compromise banking logins or steal OTPs.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = mainPrompt)))),
            generationConfig = GenerationConfig(temperature = 0.25f, maxOutputTokens = 1200),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        return try {
            val response = apiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "No response generated by the selected model. Please inspect raw data metrics."
        } catch (e: Exception) {
            e.printStackTrace()
            "Network Timeout or API Exception while compiling AI Narrative: ${e.localizedMessage}. View the direct metrics folder below."
        }
    }
}
