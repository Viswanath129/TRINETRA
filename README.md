# 🛡️ TRINETRA

### Generative AI-Powered Fraudulent APK Detection & Risk Scoring System

TRINETRA is an AI-assisted Android malware investigation platform designed to detect, analyze, classify, and explain fraudulent APKs targeting banking and financial ecosystems.

Built for the **PSB Cybersecurity, Fraud & AI Hackathon 2026**, TRINETRA combines APK reverse engineering, static analysis, DEX bytecode inspection, threat intelligence correlation, risk scoring, MITRE ATT&CK mapping, and Generative AI reasoning into a unified investigation workflow.

---

## 🚀 Live Demo

**Google AI Studio Prototype**

https://ai.studio/apps/de73be12-00ac-4e29-9593-3d6d158ab5a0?fullscreenApplet=true

Built using Google AI Studio's application generation and rapid prototyping workflow.

---

# 🎯 Problem Statement

Fraudulent Android applications are increasingly used to:

* Steal banking credentials
* Intercept OTP messages
* Abuse Accessibility Services
* Launch overlay phishing attacks
* Redirect UPI transactions
* Deploy remote banking trojans

Traditional malware investigation requires manual reverse engineering and extensive analyst effort.

TRINETRA automates this process using AI-assisted malware analysis and explainable risk scoring.

---

# 🏗️ System Architecture

```text
                ┌────────────────────┐
                │    APK Upload      │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ APK Extraction     │
                │ Manifest Parser    │
                │ DEX Scanner        │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ Static Analysis    │
                │ Threat Indicators  │
                │ API Detection      │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ Threat Intelligence│
                │ MITRE ATT&CK       │
                │ IOC Correlation    │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ AI Detection Engine│
                │ Risk Scoring       │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ GenAI Investigation│
                │ Threat Reasoning   │
                │ Report Generation  │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │ Analyst Dashboard  │
                │ Executive Report   │
                └────────────────────┘
```

---

# ⚙️ Core Features

## APK Intake Engine

* APK Upload
* Package Metadata Extraction
* SHA256 Generation
* Certificate Inspection

## Reverse Engineering Layer

* Manifest Parsing
* DEX Bytecode Scanning
* Permission Analysis
* Component Enumeration

## Static Analysis Engine

Detects:

* READ_SMS abuse
* RECEIVE_SMS abuse
* Accessibility abuse
* Overlay attacks
* Reflection usage
* Dynamic class loading
* Suspicious API calls

## DEX Signature Scanner

Scans bytecode references such as:

```text
SmsManager
AccessibilityService
WindowManager.addView()
DexClassLoader
java.lang.reflect
su binaries
```

---

## Fraud Capability Engine

Identifies:

* OTP Theft
* SMS Exfiltration
* Accessibility Hijacking
* Overlay Injection
* UPI Fraud Automation
* Remote Access Capabilities
* Background Payload Downloaders

---

## Risk Scoring Engine

Produces:

* Risk Score (0–100)
* Threat Classification
* Severity Mapping

```text
0–30     Safe
31–60    Suspicious
61–100   Malicious
```

---

## MITRE ATT&CK Mapping

Automatic mapping of:

* Credential Access
* Defense Evasion
* Collection
* Privilege Escalation
* Persistence
* Discovery

---

## GenAI Investigator

Generative AI automatically produces:

* Malware Intent Analysis
* Threat Storytelling
* Attack Chain Reconstruction
* Executive Summaries
* Analyst Reports

---

# 📊 Example Investigation Output

```text
Malware Family:
Anubis-like Banking Trojan

Risk Score:
94/100

Primary Objective:
Banking Credential Theft

Secondary Objective:
SMS OTP Interception

MITRE Techniques:
T1412
T1429
T1409

Recommended Action:
Immediate Block & IOC Distribution
```

---

# 📱 Native Mobile Experience

Built using:

* Kotlin
* Jetpack Compose
* Material Design 3
* Room Database
* Coroutines
* Navigation Compose
* Gemini API

Features:

* CrowdStrike-inspired UI
* Adaptive Mobile Layout
* Real-Time Threat Dashboard
* Interactive Risk Gauges
* Executive Reporting Screens

---

# 🔬 Innovation Highlights

### DEX Bytecode Threat Scanner

Real APK bytecode inspection without relying solely on permissions.

### Fraud Impact Engine

Translates malware behavior into potential financial exposure.

### Explainable AI

Provides evidence-backed risk scores.

### GenAI Investigator

Transforms technical malware artifacts into analyst-friendly intelligence.

---

# 📈 Future Roadmap

* MobSF Integration
* Frida Runtime Analysis
* Dynamic Sandboxing
* Autonomous AI Security Agents
* Threat Intelligence API Integration
* Multi-Agent Malware Investigation Framework

---

# 👥 Team Pancha JANEYA

### Team Members

* V. Kasi Viswanath
* T. Mohan
* B. Vedavyas Naidu
* K. Ramya Sai Aparna

---

# 🏆 Hackathon

PSB Cybersecurity, Fraud & AI Hackathon 2026

---

# 📜 License

This project is developed for educational, research, and cybersecurity innovation purposes.

---

# ⭐ TRINETRA

**See Beyond the APK. Detect the Intent. Prevent the Fraud.**
