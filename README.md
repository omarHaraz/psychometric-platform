# Psychometric Assessment & Reporting Platform

A robust, enterprise-grade psychometric evaluation platform built with **Spring Boot 3.4+**, **Java 21**, **Redis**, and a modern web frontend.

---

## 🏗 Architecture Overview

The system delivers a complete end-to-end testing and analytics pipeline:
* **Authentication & Identity:** Stateless HMAC-SHA256 JWT lifecycle, OTP-backed candidate registration, and password recovery via Redis.
* **Role-Based Access Control (RBAC):** ROLE_SUPER_ADMIN (admin management), ROLE_ADMIN (candidate directory & sessions), and ROLE_CANDIDATE (test-taking).
* **4-Battery Assessment Engine:**
  * **Admin-Assigned Sessions:** Candidates cannot start an assessment on their own. Admins must explicitly create an `AssessmentAttempt` which triggers an email invitation with a secure token.
  * **Forced-Sequential Execution:** The 4 batteries must be completed in a strict order (PQ10 -> SJT -> Derailers -> GCAT). Candidates cannot skip or re-enter completed batteries.
  * **Server-Authoritative Timing:** All tests use strict Redis-backed timers for hard cutoffs.
  1. **Personality (PQ10) / تقييم الشخصية:** 140 Likert items measuring 10 core leadership competencies.
     * *Format:* 40 minutes - Likert scale
     * *Details:* Measuring leadership traits, reporting on strongest and weakest traits.
  2. **Situational Judgment Test (SJT) / الحكم على المواقف:** 16 multi-stage scenarios with 4-option ranking (Kendall-tau rank concordance).
     * *Format:* 30-45 minutes - Ranking
     * *Details:* Assessing leadership judgment, detailing and comparing situations.
  3. **Derailers & Drivers / السلوكيات المعطلة:** 60 items assessing behavioral derailment risk (Volatility, Micromanagement, Arrogance).
     * *Format:* 10-20 minutes - Likert scale
     * *Details:* Derailer risk index, highlighting the highest derailer behaviors present.
  4. **General Cognitive Aptitude Test (GCAT) / القدرات الإدراكية:** 42-item timed cognitive assessment.
     * *Format:* 20 minutes strict
     * *Details:* Verbal, Numerical, and Abstract reasoning, detailing answers and mistakes.
* **Normative Scoring & Calibration:**
  * Weighted Composite Score (\% \text{ PQ10} + 22\% \text{ SJT} + 20\% \text{ Derailers} + 30\% \text{ GCAT}$).
  * Standardized Logistic Percentile Norms ($\\mu=75, \\sigma=6$) and 10 STEN scores.
  * 5 Executive Promotion Readiness Bands.
* **PDF Report Dispatcher:** Asynchronous generation of 5-Page Executive Leadership Dossiers with vector SVG radar charts, heatmaps, and SHA-256 cryptographic verification.

---

## 📁 Repository Structure

`
.
├── backend/                  # Spring Boot 3.4+ REST API & Security Engine
│   ├── src/main/java/        # Java source (features/auth, features/user, common, infrastructure)
│   ├── src/main/resources/   # application.properties & Thymeleaf templates
│   └── pom.xml               # Maven configuration
│
├── frontend/                 # Client Applications
│   ├── admin/                # Administrator management portal
│   ├── auth/                 # Candidate & admin authentication (login, signup, OTP)
│   └── shared/               # Central API configuration and shared services
│
└── docs/                     # Project Specifications & Blueprints
    ├── 00-planning/          # Psychometric specification & 5-phase project plan
    ├── 01-architecture/      # Top-down system topology & infrastructure
    └── 02-codebase-reference/# UML class diagrams & 18 REST endpoints reference
`

---

## 🚀 Getting Started

### Backend Setup
1. **Prerequisites:** JDK 21+ and Maven 3.9+.
2. **Database:** Configure MySQL 8.0 datasource in pplication.properties (or set environment variables DB_URL, DB_USERNAME, DB_PASSWORD).
3. **Run Application:**
   `ash
   cd backend
   mvn spring-boot:run
   `
   Server starts on http://localhost:8081.

### Frontend Setup
1. Serve the rontend/ directory with any static web server (e.g. VS Code Live Server on port 5500, or Nginx).
2. Open http://localhost:5500/admin/pages/dashboard.html or http://localhost:5500/auth/login.html.
