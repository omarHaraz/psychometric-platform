
// Custom Modal System
window.showCustomModal = function(options) {
    const overlay = document.getElementById("customModalOverlay");
    const card = document.getElementById("customModalCard");
    const titleEl = document.getElementById("customModalTitle");
    const messageEl = document.getElementById("customModalMessage");
    const iconEl = document.getElementById("customModalIcon");
    const iconContainer = document.getElementById("customModalIconContainer");
    const actionsContainer = document.getElementById("customModalActions");
    
    if (!overlay) return;
    
    titleEl.textContent = options.title || "Information";
    
    // Support newlines in message
    messageEl.innerHTML = (options.message || "").replace(/\n/g, '<br>');
    
    iconEl.textContent = options.icon || "info";
    
    // Reset colors
    iconContainer.className = "w-14 h-14 rounded-full mx-auto flex items-center justify-center mb-4";
    if (options.type === "danger") {
        iconContainer.classList.add("bg-red-50", "text-red-600");
    } else if (options.type === "success") {
        iconContainer.classList.add("bg-emerald-50", "text-emerald-600");
    } else if (options.type === "warning") {
        iconContainer.classList.add("bg-amber-50", "text-amber-600");
    } else {
        iconContainer.classList.add("bg-[#00685f]/10", "text-[#00685f]");
    }
    
    actionsContainer.innerHTML = "";
    const isArabic = document.documentElement.getAttribute("dir") === "rtl";
    
    if (options.buttons && options.buttons.length > 0) {
        options.buttons.forEach(btnOpts => {
            const btn = document.createElement("button");
            btn.className = `flex-1 py-2.5 rounded-xl font-bold text-sm transition-colors ${
                btnOpts.style === "primary" ? "bg-[#00685f] text-white hover:bg-[#004e47]" :
                btnOpts.style === "danger" ? "bg-red-600 text-white hover:bg-red-700" :
                "bg-white border border-slate-200 text-slate-600 hover:bg-slate-50"
            }`;
            let t = btnOpts.text;
            if (isArabic) {
                if (t === "Cancel") t = "إلغاء";
                if (t === "Confirm") t = "تأكيد";
                if (t === "Submit") t = "إرسال";
                if (t === "Log Out") t = "تسجيل الخروج";
            }
            btn.textContent = t;
            btn.addEventListener("click", () => {
                closeCustomModal();
                if (btnOpts.onClick) btnOpts.onClick();
            });
            actionsContainer.appendChild(btn);
        });
    } else {
        // Default OK button
        const btn = document.createElement("button");
        btn.className = "flex-1 py-2.5 rounded-xl font-bold text-sm bg-[#00685f] text-white hover:bg-[#004e47] transition-colors";
        btn.textContent = isArabic ? "حسناً" : "OK";
        btn.addEventListener("click", closeCustomModal);
        actionsContainer.appendChild(btn);
    }
    
    overlay.classList.remove("hidden");
    // trigger reflow
    void overlay.offsetWidth;
    overlay.style.opacity = "1";
    card.classList.remove("scale-95", "opacity-0");
    card.classList.add("scale-100", "opacity-100");
    
    applyCurrentTranslation();
}

window.closeCustomModal = function() {
    const overlay = document.getElementById("customModalOverlay");
    const card = document.getElementById("customModalCard");
    if (!overlay) return;
    
    overlay.style.opacity = "0";
    card.classList.remove("scale-100", "opacity-100");
    card.classList.add("scale-95", "opacity-0");
    setTimeout(() => {
        overlay.classList.add("hidden");
    }, 200);
}



const i18nDict = {
    "Dashboard": "لوحة القيادة",
    "Reports": "التقارير",
    "Settings": "الإعدادات",
    "LOG OUT": "تسجيل الخروج",
    "Update Profile": "تحديث الملف الشخصي",
    "Technical Support": "الدعم الفني",
    "Experiencing technical issues? Contact testing support for assistance.": "هل تواجه مشاكل فنية؟ اتصل بدعم الاختبار للحصول على المساعدة.",
    "Contact Support": "اتصل بالدعم",
    "Language / لغة": "Language / لغة",
    "Switch to Arabic": "التبديل إلى العربية",
    "Switch to English": "التبديل إلى الإنجليزية",
    "No Active Assessment Assigned": "لم يتم تعيين تقييم نشط",
    "You do not currently have any pending psychometric evaluations. Please contact your administrator to assign a test to your profile.": "ليس لديك حاليًا أي تقييمات نفسية معلقة. يرجى الاتصال بالمسؤول لتعيين اختبار لملفك الشخصي.",
    "Executive Leadership Aptitude": "كفاءة القيادة التنفيذية",
    "4 Parts": "4 أجزاء",
    "Approx. 90 mins": "حوالي 90 دقيقة",
    "Status: Pending": "الحالة: قيد الانتظار",
    "Assessment Integrity Rule:": "قاعدة نزاهة التقييم:",
    "This assessment must be completed in a single continuous sitting. Once started, you cannot pause the timer or return to previous sections. Ensure you have 90 minutes of uninterrupted time.": "يجب إكمال هذا التقييم في جلسة واحدة متواصلة. بمجرد البدء ، لا يمكنك إيقاف المؤقت مؤقتًا أو العودة إلى الأقسام السابقة. تأكد من أن لديك 90 دقيقة من الوقت دون انقطاع.",
    "01 • Personality (PQ10)": "01 • الشخصية (PQ10)",
    "140 items • 40 mins • Likert": "140 عنصر • 40 دقيقة • ليكرت",
    "02 • SJT Ranking": "02 • ترتيب SJT",
    "16 scenarios • 45 mins • Ranking": "16 سيناريو • 45 دقيقة • ترتيب",
    "03 • Derailers & Drivers": "03 • المحفزات والمعوقات",
    "60 items • 20 mins • Likert": "60 عنصر • 20 دقيقة • ليكرت",
    "04 • Cognitive Abilities": "04 • القدرات المعرفية",
    "24 patterns • 20 mins • MCQ": "24 نمط • 20 دقيقة • خيارات متعددة",
    "READY": "جاهز",
    "LOCKED": "مغلق",
    "SUBMITTED": "مكتمل",
    "Assessment History": "تاريخ التقييم",
    "No past assessments found.": "لم يتم العثور على تقييمات سابقة.",
    "Completed": "مكتمل",
    "Started": "بدأ",
    "at": "في",
    "In Progress": "قيد التقدم",
    "Download Report": "تحميل التقرير",
    "Assessment Completed!": "اكتمل التقييم!",
    "All 4 batteries have been successfully submitted and locked.": "تم تقديم وإغلاق جميع البطاريات الأربع بنجاح.",
    "Results Available": "النتائج متاحة",
    "Your psychometric responses and cognitive tests have been successfully calculated by the scoring engine.": "تم حساب استجاباتك النفسية والاختبارات المعرفية بنجاح.",
    "Click here to get the result": "انقر هنا للحصول على النتيجة",
    "Back to Portal Home": "العودة إلى الرئيسية",
    "Downloading...": "جاري التحميل...",
    "Progress Overview": "نظرة عامة على التقدم",
    "Part 1 of 4": "الجزء 1 من 4",
    "Part 2 of 4": "الجزء 2 من 4",
    "Part 3 of 4": "الجزء 3 من 4",
    "Part 4 of 4": "الجزء 4 من 4",
    "Synchronizing Assessment Session...": "جاري مزامنة جلسة التقييم...",
    "Time Cutoff Reached": "انتهى الوقت",
    "Your allocated time for this battery has expired. Your answered items have been safely recorded.": "انتهى الوقت المخصص لهذه البطارية. تم تسجيل إجاباتك بأمان.",
    "Auto-advancing to the next assessment section...": "يتم الانتقال تلقائيًا إلى قسم التقييم التالي...",
    "Pre-Battery Instructions": "تعليمات قبل البدء",
    "I Understand • Begin Battery": "أفهم ذلك • ابدأ البطارية",
    "Battery Overview": "نظرة عامة على البطارية"
};

// Create reverse dictionary
const reverseI18nDict = {};
for (const [en, ar] of Object.entries(i18nDict)) {
    reverseI18nDict[ar] = en;
}

function translateNode(node, toLang) {
    if (node.nodeType === Node.TEXT_NODE) {
        let text = node.textContent.trim();
        if (text) {
            // Find matches in the dictionary
            if (toLang === "ar" && i18nDict[text]) {
                node.textContent = node.textContent.replace(text, i18nDict[text]);
            } else if (toLang === "en" && reverseI18nDict[text]) {
                node.textContent = node.textContent.replace(text, reverseI18nDict[text]);
            } else if (text.includes(" • ")) {
                 // Try partial match for batteries
                 let parts = text.split(" • ");
                 let translatedParts = parts.map(p => {
                     let pt = p.trim();
                     if (toLang === "ar" && i18nDict[pt]) return i18nDict[pt];
                     if (toLang === "en" && reverseI18nDict[pt]) return reverseI18nDict[pt];
                     return pt;
                 });
                 if (translatedParts.join(" • ") !== text) {
                     node.textContent = node.textContent.replace(text, translatedParts.join(" • "));
                 }
            } else if (text.includes(" at ")) {
                let parts = text.split(" at ");
                if (toLang === "ar") {
                    let first = parts[0].trim();
                    let tf = i18nDict[first.split(" ")[0]]; // Started/Completed
                    if (tf) {
                        node.textContent = tf + " " + first.substring(first.indexOf(" ")+1) + " في " + parts[1];
                    }
                }
            } else if (text.includes(" في ")) {
                let parts = text.split(" في ");
                if (toLang === "en") {
                    let first = parts[0].trim();
                    let tf = reverseI18nDict[first.split(" ")[0]]; // بدأ/مكتمل
                    if (tf) {
                        node.textContent = tf + " " + first.substring(first.indexOf(" ")+1) + " at " + parts[1];
                    }
                }
            }
        }
    } else if (node.nodeType === Node.ELEMENT_NODE) {
        if (node.tagName !== "SCRIPT" && node.tagName !== "STYLE") {
            for (let child of node.childNodes) {
                translateNode(child, toLang);
            }
        }
    }
}

function applyTranslation(lang) {
    translateNode(document.body, lang);
    // Also update dynamic elements rendered by JS
    if (lang === "ar") {
        document.body.style.fontFamily = "Cairo, sans-serif";
    } else {
        document.body.style.fontFamily = "";
    }
}

window.applyTranslation = applyTranslation;

function applyCurrentTranslation() {
    if (document.documentElement.getAttribute("dir") === "rtl") {
        applyTranslation("ar");
    }
}



import { API_BASE } from "../../../shared/config/api-config.js";

// State
let currentUser = null;
let currentAttempt = null;
let activeSession = null;
let activeItems = [];
let currentItemIndex = 0;
let responsesMap = {};
let itemStartTimes = {};
let countdownTimerInterval = null;
let heartbeatInterval = null;
let remainingSeconds = 0;

// Battery definitions metadata
const BATTERY_METADATA = [
    {
        name: "Personality (PQ10)",
        nameAr: "تقييم الشخصية القيادية",
        part: "Part 1 of 4",
        badge: "PQ10",
        itemsCount: 140,
        timeLimit: "40 Minutes",
        format: "Likert Scale",
        instructions: [
            "Read each leadership statement carefully and choose the option that best reflects your natural behavior.",
            "There are no right or wrong personality answers; consistency and authenticity are evaluated.",
            "The 40-minute timer is server-monitored and cannot be paused.",
            "Responses are continuously buffered and auto-saved in real time."
        ]
    },
    {
        name: "Situational Judgment (SJT)",
        nameAr: "الحكم على المواقف والقرارات القيادية",
        part: "Part 2 of 4",
        badge: "SJT",
        itemsCount: 16,
        timeLimit: "45 Minutes",
        format: "4-Option Ranking",
        instructions: [
            "You will be presented with real-world executive scenarios and workplace challenges.",
            "Order the 4 available actions from Most Effective (Rank 1) to Least Effective (Rank 4).",
            "Use the up/down arrows to adjust the relative ranking of each proposed response.",
            "You have 45 minutes to complete all 16 scenarios."
        ]
    },
    {
        name: "Derailers & Drivers",
        nameAr: "السلوكيات المعطلة ومؤشر الخطر",
        part: "Part 3 of 4",
        badge: "DERAILERS",
        itemsCount: 60,
        timeLimit: "20 Minutes",
        format: "Likert Scale",
        instructions: [
            "This section assesses behavior tendencies under pressure, stress, and heavy workloads.",
            "Indicate your level of agreement with each workplace scenario statement.",
            "Be frank and transparent in your self-assessment.",
            "You have 20 minutes for this 60-item battery."
        ]
    },
    {
        name: "Cognitive Aptitude (GCAT)",
        nameAr: "القدرات الإدراكية والتفكير التحليلي",
        part: "Part 4 of 4",
        badge: "GCAT",
        itemsCount: 42,
        timeLimit: "20 Min Strict",
        format: "Multiple Choice (MCQ)",
        instructions: [
            "Evaluates Verbal, Numerical, and Abstract pattern reasoning aptitude.",
            "Each question has one single correct answer option.",
            "This battery has a STRICT 20-minute time cutoff enforced by the server.",
            "Work as quickly and accurately as possible."
        ]
    }
];

// Initialize Application
document.addEventListener("DOMContentLoaded", () => {
    checkAuth();
    initEventListeners();
    loadAssessmentState();
    loadAssessmentHistory();
});

// Check Authentication
async function checkAuth() {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        window.location.href = "../auth/login.html";
        return;
    }
    try {
        currentUser = JSON.parse(userStr);
        if (!currentUser || !currentUser.token) {
            window.location.href = "../auth/login.html";
            return;
        }
        
        // Fetch fresh user data from server
        try {
            const res = await fetch(`${API_BASE}/api/auth/me`, {
                headers: getAuthHeader()
            });
            if (res.ok) {
                const freshUser = await res.json();
                currentUser.name = freshUser.name;
                currentUser.email = freshUser.email;
                localStorage.setItem("user", JSON.stringify(currentUser));
            }
        } catch (e) {
            console.error("Failed to fetch fresh user data", e);
        }

        // Set user profile in header & sidebar
        const nameHeader = document.getElementById("userDisplayNameHeader");
        const emailHeader = document.getElementById("userDisplayEmailHeader");
        const headerNameLeft = document.getElementById("headerCandidateNameLeft");
        const headerNameRight = document.getElementById("headerCandidateNameRight");
        const sidebarName = document.getElementById("sidebarCandidateName");
        const sidebarEmail = document.getElementById("sidebarCandidateEmail");
        
        if (nameHeader) nameHeader.textContent = currentUser.name || "Candidate";
        if (emailHeader) emailHeader.textContent = currentUser.email || "";
        if (headerNameLeft) headerNameLeft.textContent = currentUser.name || "Candidate";
        if (headerNameRight) headerNameRight.textContent = currentUser.name || "Candidate";
        if (sidebarName) sidebarName.textContent = currentUser.name || "Candidate";
        if (sidebarEmail) sidebarEmail.textContent = currentUser.email || "";
    } catch (e) {
        localStorage.removeItem("user");
        window.location.href = "../auth/login.html";
    }
}

function getAuthHeader() {
    return {
        "Authorization": `Bearer ${currentUser.token}`,
        "Content-Type": "application/json"
    };
}

// Event Listeners
function initEventListeners() {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            const isArabic = document.documentElement.getAttribute("dir") === "rtl";
            const titleStr = isArabic ? "تسجيل الخروج" : "Log Out";
            const msgStr = isArabic ? "هل أنت متأكد أنك تريد تسجيل الخروج؟" : "Are you sure you want to logout?";
            window.showCustomModal({
                title: titleStr,
                message: msgStr,
                type: "warning",
                icon: "logout",
                buttons: [
                    { text: "Cancel", style: "secondary" },
                    { text: "Log Out", style: "danger", onClick: () => {
                        localStorage.removeItem("user");
                        window.location.href = "../auth/login.html";
                    }}
                ]
            });
        });
    }

    const rtlToggle = document.getElementById("rtlToggle");
    if (rtlToggle) {
        rtlToggle.addEventListener("click", () => {
            const htmlEl = document.documentElement;
            const currentDir = htmlEl.getAttribute("dir") || "ltr";
            const warningBlock = document.getElementById("warningBlock");
            if (currentDir === "ltr") {
                htmlEl.setAttribute("dir", "rtl");
                rtlToggle.textContent = "Switch to English";
                applyTranslation("ar");
                if (warningBlock) {
                    warningBlock.classList.remove("border-l-4", "border-l-[#131b2e]", "rounded-r");
                    warningBlock.classList.add("border-r-4", "border-r-[#131b2e]", "rounded-l");
                }
            } else {
                htmlEl.setAttribute("dir", "ltr");
                rtlToggle.textContent = "Switch to Arabic";
                applyTranslation("en");
                if (warningBlock) {
                    warningBlock.classList.remove("border-r-4", "border-r-[#131b2e]", "rounded-l");
                    warningBlock.classList.add("border-l-4", "border-l-[#131b2e]", "rounded-r");
                }
            }
        });
    }

    const startAssessmentBtn = document.getElementById("startAssessmentBtn");
    if (startAssessmentBtn) {
        startAssessmentBtn.addEventListener("click", () => {
            openPreBatteryInstructions(currentAttempt.currentBatteryIndex || 0);
        });
    }

    const beginBatteryBtn = document.getElementById("beginBatteryBtn");
    if (beginBatteryBtn) {
        beginBatteryBtn.addEventListener("click", startActiveBatterySession);
    }

    const prevBtn = document.getElementById("prevQuestionBtn");
    if (prevBtn) {
        prevBtn.addEventListener("click", () => {
            if (currentItemIndex > 0) {
                recordItemTime(currentItemIndex);
                currentItemIndex--;
                renderCurrentQuestion();
            }
        });
    }

    const nextBtn = document.getElementById("nextQuestionBtn");
    if (nextBtn) {
        nextBtn.addEventListener("click", () => {
            if (currentItemIndex < activeItems.length - 1) {
                recordItemTime(currentItemIndex);
                currentItemIndex++;
                renderCurrentQuestion();
            }
        });
    }

    const submitBtn = document.getElementById("submitBatteryBtn");
    if (submitBtn) {
        submitBtn.addEventListener("click", () => {
            const isArabic = document.documentElement.getAttribute("dir") === "rtl";
            const titleStr = isArabic ? "تأكيد الإرسال" : "Confirm Submission";
            const msgStr = isArabic ? "هل أنت متأكد أنك تريد إنهاء وإرسال هذه البطارية؟ لا يمكنك العودة إلى هذه الأسئلة." : "Are you sure you want to finalize and submit this battery?\nYou cannot return to these questions.";
            
            window.showCustomModal({
                title: titleStr,
                message: msgStr,
                type: "warning",
                icon: "publish",
                buttons: [
                    { text: "Cancel", style: "secondary" },
                    { text: "Submit", style: "primary", onClick: () => {
                        recordItemTime(currentItemIndex);
                        submitActiveBattery();
                    }}
                ]
            });
        });
    }
}

// View Management
function showView(viewId) {
    const views = [
        "view-loading",
        "view-empty",
        "view-pending-portal",
        "view-instructions",
        "view-active-test",
        "view-complete"
    ];
    views.forEach(v => {
        const el = document.getElementById(v);
        if (el) el.classList.add("hidden");
    });

    const activeEl = document.getElementById(viewId);
    if (activeEl) activeEl.classList.remove("hidden");

    // Toggle Sidebars
    const dashboardSidebar = document.getElementById("dashboardSidebar");
    const testSidebar = document.getElementById("testSidebar");
    const historySection = document.getElementById("historySection");

    const rightSpacer = document.getElementById("rightSpacer");
    const mainContentArea = document.getElementById("mainContentArea");
    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
        if (rightSpacer) { rightSpacer.classList.add("hidden"); rightSpacer.style.display = ""; }
        if (mainContentArea) { mainContentArea.classList.add("flex-grow"); mainContentArea.classList.remove("flex-1"); }
    } else if (viewId === "view-complete" || viewId === "view-empty") {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) { rightSpacer.classList.remove("hidden"); rightSpacer.style.display = "block"; }
        // Make mainContentArea use flex-1 so all 3 columns share space equally
        if (mainContentArea) { mainContentArea.classList.remove("flex-grow"); mainContentArea.classList.add("flex-1"); }
        if (historySection) historySection.classList.remove("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) rightSpacer.classList.add("hidden");
        if (mainContentArea) { mainContentArea.classList.add("flex-grow"); mainContentArea.classList.remove("flex-1"); }
        if (historySection) {
            if (viewId === "view-pending-portal") {
                historySection.classList.remove("hidden");
            } else {
                historySection.classList.add("hidden");
            }
        }
    }
}

// Load Assessment State
async function loadAssessmentState() {
    showView("view-loading");

    const urlParams = new URLSearchParams(window.location.search);
    const queryToken = urlParams.get("token");

    try {
        let attempt = null;
        if (queryToken) {
            const res = await fetch(`${API_BASE}/api/attempts/${queryToken}`, {
                headers: getAuthHeader()
            });
            if (res.ok) {
                attempt = await res.json();
            }
        }

        if (!attempt) {
            const res = await fetch(`${API_BASE}/api/attempts/me/pending`, {
                headers: getAuthHeader()
            });
            if (res.ok) {
                attempt = await res.json();
            }
        }

        if (!attempt) {
            showView("view-empty");
            return;
        }

        currentAttempt = attempt;
        handleAttemptState(attempt);

    } catch (err) {
        console.error("Error loading assessment:", err);
        showView("view-empty");
    }
}

// Handle Attempt State
function handleAttemptState(attempt) {
    if (attempt.state === "ALL_SUBMITTED" || attempt.state === "SCORED") {
        showView("view-complete");
        return;
    }

    if (attempt.state === "INIT") {
        showPendingPortal(attempt);
        return;
    }

    if (attempt.state === "IN_PROGRESS") {
        const currentIndex = attempt.currentBatteryIndex || 0;
        const currentSession = attempt.batterySessions.find(s => s.sequenceOrder === currentIndex);
        
        if (currentSession && currentSession.state === "IN_PROGRESS") {
            // Already started active battery
            activeSession = currentSession;
            resumeActiveBattery(currentSession);
        } else {
            openPreBatteryInstructions(currentIndex);
        }
    }
}

// Show Pending Portal View
function showPendingPortal(attempt) {
    showView("view-pending-portal");
    updateBatteryCardStates(attempt);
    applyCurrentTranslation();
}

function updateBatteryCardStates(attempt) {
    const currentIndex = attempt.currentBatteryIndex || 0;
    
    for (let i = 0; i < 4; i++) {
        const card = document.getElementById(`card-battery-${i}`);
        if (!card) continue;
        
        const badge = card.querySelector(".battery-badge");
        if (i < currentIndex) {
            if (badge) {
                badge.textContent = "COMPLETED";
                badge.className = "battery-badge text-[10px] font-bold px-2 py-0.5 rounded-full bg-slate-100 text-slate-500";
            }
        } else if (i === currentIndex) {
            if (badge) {
                badge.textContent = attempt.state === "INIT" ? "READY TO START" : "IN PROGRESS";
                badge.className = "battery-badge text-[10px] font-bold px-2 py-0.5 rounded-full bg-emerald-50 text-primary border border-primary/20";
            }
        } else {
            if (badge) {
                badge.textContent = "LOCKED";
                badge.className = "battery-badge text-[10px] font-bold px-2 py-0.5 rounded-full bg-slate-100 text-slate-400";
            }
        }
    }
}

// Pre-Battery Instructions Screen
function openPreBatteryInstructions(batteryIndex) {
    const meta = BATTERY_METADATA[batteryIndex] || BATTERY_METADATA[0];

    document.getElementById("instPartNumber").textContent = meta.part;
    document.getElementById("instBatteryTitle").textContent = meta.name;
    document.getElementById("instBatteryArabicTitle").textContent = meta.nameAr;
    document.getElementById("instItemCount").textContent = `${meta.itemsCount} Items`;
    document.getElementById("instTimeLimit").textContent = meta.timeLimit;
    document.getElementById("instFormatType").textContent = meta.format;

    updateTestSidebar(currentAttempt);
    showView("view-instructions");
    applyCurrentTranslation();
}

// Start / Unlock Battery
async function startActiveBatterySession() {
    showView("view-loading");

    try {
        if (currentAttempt.state === "INIT") {
            const res = await fetch(`${API_BASE}/api/attempts/${currentAttempt.attemptToken}/start`, {
                method: "POST",
                headers: getAuthHeader()
            });
            if (!res.ok) throw new Error("Failed to start attempt");
            currentAttempt = await res.json();
        }

        const currentIndex = currentAttempt.currentBatteryIndex || 0;
        activeSession = currentAttempt.batterySessions.find(s => s.sequenceOrder === currentIndex);
        
        await fetchAndRenderBatteryItems(activeSession);

    } catch (err) {
        console.error("Error starting battery session:", err);
        window.showCustomModal({title: 'Error', message: 'Failed to start assessment battery. Please try again.', type: 'danger', icon: 'error'});
        showView("view-instructions");
    }
}

async function resumeActiveBattery(session) {
    showView("view-loading");
    await fetchAndRenderBatteryItems(session);
}

// Fetch Items for Active Battery
async function fetchAndRenderBatteryItems(session) {
    try {
        const res = await fetch(`${API_BASE}/api/attempts/battery-sessions/${session.id}/items`, {
            headers: getAuthHeader()
        });
        if (!res.ok) throw new Error("Failed to fetch sanitized items");

        activeItems = await res.json();
        currentItemIndex = 0;
        responsesMap = {};
        itemStartTimes = {};

        // Setup battery header
        const meta = BATTERY_METADATA[session.sequenceOrder] || BATTERY_METADATA[0];
        document.getElementById("activeBatteryBadge").textContent = meta.badge;
        document.getElementById("activeBatteryTitle").textContent = meta.name;

        // Perform initial heartbeat to get server timer
        await sendHeartbeat();

        // Start countdown and heartbeat intervals
        startCountdownTimer();
        startHeartbeatSync();

        updateTestSidebar(currentAttempt);
        showView("view-active-test");
        renderCurrentQuestion();

    } catch (err) {
        console.error("Error fetching items:", err);
        window.showCustomModal({title: 'Error', message: 'Failed to load test items.', type: 'danger', icon: 'error'});
        showView("view-pending-portal");
    }
}

// Timer & Countdown Management
function startCountdownTimer() {
    if (countdownTimerInterval) clearInterval(countdownTimerInterval);

    updateTimerDisplay();

    countdownTimerInterval = setInterval(() => {
        if (remainingSeconds > 0) {
            remainingSeconds--;
            updateTimerDisplay();
        } else {
            clearInterval(countdownTimerInterval);
            handleTimeCutoff();
        }
    }, 1000);
}

function updateTimerDisplay() {
    const timerDisplay = document.getElementById("timerDisplay");
    const timerContainer = document.getElementById("timerContainer");
    
    const minutes = Math.floor(remainingSeconds / 60);
    const seconds = remainingSeconds % 60;
    const formatted = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

    if (timerDisplay) timerDisplay.textContent = formatted;

    if (timerContainer) {
        if (remainingSeconds <= 180) { // Under 3 mins
            timerContainer.classList.add("timer-warning");
        } else {
            timerContainer.classList.remove("timer-warning");
        }
    }
}

function startHeartbeatSync() {
    if (heartbeatInterval) clearInterval(heartbeatInterval);
    heartbeatInterval = setInterval(sendHeartbeat, 10000); // sync every 10s
}

async function sendHeartbeat() {
    if (!activeSession) return;

    const payloadList = Object.keys(responsesMap).map(itemId => ({
        itemId: Number(itemId),
        selectedLikert: responsesMap[itemId].selectedLikert || null,
        rankingOrder: responsesMap[itemId].rankingOrder || null,
        selectedOption: responsesMap[itemId].selectedOption || null,
        responseTimeMs: responsesMap[itemId].responseTimeMs || 0
    }));

    try {
        const res = await fetch(`${API_BASE}/api/attempts/battery-sessions/${activeSession.id}/heartbeat`, {
            method: "POST",
            headers: getAuthHeader(),
            body: JSON.stringify({ responses: payloadList })
        });
        if (res.ok) {
            const data = await res.json();
            remainingSeconds = data.remainingTimeSeconds || 0;
            updateTimerDisplay();
            if (remainingSeconds <= 0) {
                handleTimeCutoff();
            }
        }
    } catch (e) {
        console.warn("Heartbeat sync error:", e);
    }
}

function handleTimeCutoff() {
    if (countdownTimerInterval) clearInterval(countdownTimerInterval);
    if (heartbeatInterval) clearInterval(heartbeatInterval);

    const timeoutOverlay = document.getElementById("overlay-timeout");
    if (timeoutOverlay) timeoutOverlay.classList.remove("hidden");

    setTimeout(() => {
        submitActiveBattery(true);
    }, 2500);
}

// Render Questions
function renderCurrentQuestion() {
    if (!activeItems || activeItems.length === 0) return;

    const item = activeItems[currentItemIndex];
    itemStartTimes[currentItemIndex] = Date.now();

    // Update Progress
    const total = activeItems.length;
    const currentNum = currentItemIndex + 1;
    document.getElementById("activeProgressLabel").textContent = `Item ${currentNum} of ${total}`;
    
    const pct = Math.round((currentNum / total) * 100);
    document.getElementById("progressBarFill").style.width = `${pct}%`;

    // Navigation buttons state
    const prevBtn = document.getElementById("prevQuestionBtn");
    const nextBtn = document.getElementById("nextQuestionBtn");
    const submitBtn = document.getElementById("submitBatteryBtn");

    if (prevBtn) prevBtn.disabled = (currentItemIndex === 0);

    if (currentItemIndex === total - 1) {
        if (nextBtn) nextBtn.classList.add("hidden");
        if (submitBtn) submitBtn.classList.remove("hidden");
    } else {
        if (nextBtn) nextBtn.classList.remove("hidden");
        if (submitBtn) submitBtn.classList.add("hidden");
    }

    const container = document.getElementById("questionBody");
    container.innerHTML = "";

    const batteryType = activeSession.batteryType;
    if (batteryType === "PQ10" || batteryType === "DERAILERS") {
        renderLikertQuestion(item, container);
    } else if (batteryType === "SJT") {
        renderSjtRankingQuestion(item, container);
    } else if (batteryType === "GCAT") {
        renderGcatMcqQuestion(item, container);
    }
    applyCurrentTranslation();
}

function recordItemTime(idx) {
    const item = activeItems[idx];
    if (!item) return;
    const start = itemStartTimes[idx] || Date.now();
    const elapsed = Date.now() - start;

    if (!responsesMap[item.id]) {
        responsesMap[item.id] = {};
    }
    responsesMap[item.id].responseTimeMs = (responsesMap[item.id].responseTimeMs || 0) + elapsed;
}

// 1. Likert Scale Question Renderer (PQ10 & Derailers)
function renderLikertQuestion(item, container) {
    const currentVal = responsesMap[item.id]?.selectedLikert || null;

    const scaleLabels = [
        { val: 1, labelEn: "Strongly Disagree", labelAr: "غير موافق بشدة" },
        { val: 2, labelEn: "Disagree", labelAr: "غير موافق" },
        { val: 3, labelEn: "Neutral", labelAr: "محايد" },
        { val: 4, labelEn: "Agree", labelAr: "موافق" },
        { val: 5, labelEn: "Strongly Agree", labelAr: "موافق بشدة" }
    ];

    let html = `
        <div class="space-y-4">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">Statement ${currentItemIndex + 1}</span>
            <h2 class="text-xl sm:text-2xl font-bold text-on-surface leading-relaxed arabic-text">
                ${item.statementAr || ""}
            </h2>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-5 gap-3 pt-4">
    `;

    scaleLabels.forEach(s => {
        const isActive = (currentVal === s.val);
        html += `
            <button type="button" class="likert-btn ${isActive ? 'active' : ''} flex flex-col items-center justify-center p-4 border border-slate-300 rounded-xl transition-all text-center gap-2 cursor-pointer bg-white" data-value="${s.val}">
                <span class="w-6 h-6 rounded-full border-2 border-slate-400 flex items-center justify-center indicator"></span>
                <span class="text-xs font-bold text-slate-800 arabic-text">${s.labelAr}</span>
                <span class="text-[10px] text-slate-500">${s.labelEn}</span>
            </button>
        `;
    });

    html += `</div>`;
    container.innerHTML = html;

    // Attach Likert Click Handlers
    container.querySelectorAll(".likert-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const val = Number(btn.dataset.value);
            if (!responsesMap[item.id]) responsesMap[item.id] = {};
            responsesMap[item.id].selectedLikert = val;

            container.querySelectorAll(".likert-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            // Auto-advance after brief selection feedback on mobile/desktop
            setTimeout(() => {
                if (currentItemIndex < activeItems.length - 1) {
                    recordItemTime(currentItemIndex);
                    currentItemIndex++;
                    renderCurrentQuestion();
                }
            }, 300);
        });
    });
}

// 2. SJT Ranking Question Renderer
function renderSjtRankingQuestion(item, container) {
    let options = item.options || [];
    
    // Check if we already have a saved ranking order for this item
    const savedOrder = responsesMap[item.id]?.rankingOrder;
    if (savedOrder && savedOrder.length === options.length) {
        options = savedOrder.map(k => options.find(o => o.optionKey === k)).filter(Boolean);
    }

    let html = `
        <div class="space-y-3">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">Scenario ${currentItemIndex + 1} &bull; ${item.itemCode || ""}</span>
            <h2 class="text-lg sm:text-xl font-bold text-on-surface leading-tight arabic-text">${item.titleAr || ""}</h2>
            <div class="p-4 bg-slate-50 border border-slate-200 rounded-xl text-sm text-slate-700 leading-relaxed arabic-text">
                ${item.narrativeAr || ""}
            </div>
            ${item.scenarioImageUrl ? `<img src="${item.scenarioImageUrl}" class="max-h-60 rounded-xl object-contain mx-auto my-2 border border-slate-200">` : ''}
        </div>

        <div class="space-y-2 pt-2">
            <div class="flex justify-between items-center px-2 text-xs font-bold text-slate-500">
                <span class="text-emerald-700 flex items-center gap-1"><span class="material-symbols-outlined text-sm">north</span> Most Effective Action (Rank 1)</span>
                <span class="text-slate-400">Order actions using arrows</span>
            </div>

            <div id="sjtOptionsList" class="space-y-2.5">
    `;

    options.forEach((opt, idx) => {
        html += `
            <div class="sjt-option-card flex items-center justify-between p-3.5 bg-white border border-slate-200 rounded-xl shadow-xs hover:border-slate-300 transition-all gap-3" data-key="${opt.optionKey}">
                <div class="flex items-center gap-3">
                    <span class="w-7 h-7 rounded-full bg-slate-100 border border-slate-200 text-slate-700 font-bold text-xs flex items-center justify-center shrink-0">
                        ${idx + 1}
                    </span>
                    <p class="text-xs sm:text-sm text-slate-800 arabic-text leading-relaxed">${opt.statementAr}</p>
                </div>
                <div class="flex flex-col gap-1 shrink-0">
                    <button type="button" class="move-up-btn p-1 text-slate-400 hover:text-primary rounded hover:bg-slate-100 disabled:opacity-20" ${idx === 0 ? 'disabled' : ''}>
                        <span class="material-symbols-outlined text-base">expand_less</span>
                    </button>
                    <button type="button" class="move-down-btn p-1 text-slate-400 hover:text-primary rounded hover:bg-slate-100 disabled:opacity-20" ${idx === options.length - 1 ? 'disabled' : ''}>
                        <span class="material-symbols-outlined text-base">expand_more</span>
                    </button>
                </div>
            </div>
        `;
    });

    html += `
            </div>
            <div class="text-right px-2 text-xs font-bold text-red-600 flex items-center justify-end gap-1">
                <span>Least Effective Action (Rank 4)</span>
                <span class="material-symbols-outlined text-sm">south</span>
            </div>
        </div>
    `;

    container.innerHTML = html;

    // Save initial ranking order if not set
    if (!responsesMap[item.id]) responsesMap[item.id] = {};
    responsesMap[item.id].rankingOrder = options.map(o => o.optionKey);

    // Reorder Handlers
    container.querySelectorAll(".move-up-btn").forEach((btn, idx) => {
        btn.addEventListener("click", () => {
            if (idx > 0) {
                const temp = options[idx];
                options[idx] = options[idx - 1];
                options[idx - 1] = temp;
                responsesMap[item.id].rankingOrder = options.map(o => o.optionKey);
                renderSjtRankingQuestion(item, container);
            }
        });
    });

    container.querySelectorAll(".move-down-btn").forEach((btn, idx) => {
        btn.addEventListener("click", () => {
            if (idx < options.length - 1) {
                const temp = options[idx];
                options[idx] = options[idx + 1];
                options[idx + 1] = temp;
                responsesMap[item.id].rankingOrder = options.map(o => o.optionKey);
                renderSjtRankingQuestion(item, container);
            }
        });
    });
}

// 3. Cognitive MCQ Question Renderer (GCAT)
function renderGcatMcqQuestion(item, container) {
    const currentVal = responsesMap[item.id]?.selectedOption || null;
    const options = item.options || [];

    let html = `
        <div class="space-y-3">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">Question ${currentItemIndex + 1} &bull; ${item.itemCode || ""}</span>
            <h2 class="text-lg sm:text-xl font-bold text-on-surface leading-tight arabic-text">${item.titleAr || ""}</h2>
            ${item.promptTextAr ? `<p class="text-sm text-slate-700 arabic-text leading-relaxed">${item.promptTextAr}</p>` : ''}
            
            ${item.questionImageUrl ? `
                <div class="p-3 bg-slate-50 rounded-xl border border-slate-200 text-center">
                    <img src="${item.questionImageUrl}" alt="Pattern Diagram" class="max-h-64 rounded-lg object-contain mx-auto">
                </div>
            ` : ''}
        </div>

        <div class="space-y-2.5 pt-2">
    `;

    options.forEach(opt => {
        const isSelected = (currentVal === opt.optionKey);
        html += `
            <label class="gcat-option-label flex items-center justify-between p-3.5 border ${isSelected ? 'border-primary bg-primary/5 ring-1 ring-primary' : 'border-slate-200 bg-white hover:border-slate-300'} rounded-xl cursor-pointer transition-all gap-3">
                <div class="flex items-center gap-3">
                    <div class="w-6 h-6 rounded-full border-2 ${isSelected ? 'border-primary' : 'border-slate-400'} flex items-center justify-center shrink-0">
                        <div class="w-3 h-3 rounded-full bg-primary ${isSelected ? 'opacity-100' : 'opacity-0'}"></div>
                    </div>
                    <span class="w-6 h-6 rounded-md bg-slate-100 text-slate-700 font-bold text-xs flex items-center justify-center shrink-0">
                        ${opt.optionKey}
                    </span>
                    <span class="text-xs sm:text-sm text-slate-800 arabic-text">${opt.textAr || ""}</span>
                </div>
                ${opt.imageUrl ? `<img src="${opt.imageUrl}" class="h-10 object-contain rounded border border-slate-100">` : ''}
                <input type="radio" name="gcat_option" value="${opt.optionKey}" ${isSelected ? 'checked' : ''} class="sr-only">
            </label>
        `;
    });

    html += `</div>`;
    container.innerHTML = html;

    // Option change handlers
    container.querySelectorAll("input[name='gcat_option']").forEach(radio => {
        radio.addEventListener("change", (e) => {
            const val = e.target.value;
            if (!responsesMap[item.id]) responsesMap[item.id] = {};
            responsesMap[item.id].selectedOption = val;

            container.querySelectorAll(".gcat-option-label").forEach(l => {
                l.classList.remove("border-primary", "bg-primary/5", "ring-1", "ring-primary");
                l.classList.add("border-slate-200", "bg-white");
                const dot = l.querySelector(".bg-primary");
                if (dot) dot.classList.replace("opacity-100", "opacity-0");
                const ring = l.querySelector(".rounded-full.border-2");
                if (ring) { ring.classList.remove("border-primary"); ring.classList.add("border-slate-400"); }
            });

            const selectedLabel = e.target.closest("label");
            selectedLabel.classList.remove("border-slate-200", "bg-white");
            selectedLabel.classList.add("border-primary", "bg-primary/5", "ring-1", "ring-primary");
            const dot = selectedLabel.querySelector(".bg-primary");
            if (dot) dot.classList.replace("opacity-0", "opacity-100");
            const ring = selectedLabel.querySelector(".rounded-full.border-2");
            if (ring) { ring.classList.remove("border-slate-400"); ring.classList.add("border-primary"); }

            // Auto-advance
            setTimeout(() => {
                if (currentItemIndex < activeItems.length - 1) {
                    recordItemTime(currentItemIndex);
                    currentItemIndex++;
                    renderCurrentQuestion();
                }
            }, 300);
        });
    });
}

// Submit Active Battery
async function submitActiveBattery(isAutoTimeout = false) {
    if (countdownTimerInterval) clearInterval(countdownTimerInterval);
    if (heartbeatInterval) clearInterval(heartbeatInterval);

    const autoAdvanceOverlay = document.getElementById("overlay-auto-advance");
    if (!isAutoTimeout && autoAdvanceOverlay) {
        autoAdvanceOverlay.classList.remove("hidden");
    }

    try {
        const res = await fetch(`${API_BASE}/api/attempts/battery-sessions/${activeSession.id}/submit`, {
            method: "POST",
            headers: getAuthHeader()
        });

        if (!res.ok) throw new Error("Failed to submit battery");

        const updatedAttempt = await res.json();
        currentAttempt = updatedAttempt;

        setTimeout(() => {
            if (autoAdvanceOverlay) autoAdvanceOverlay.classList.add("hidden");
            const timeoutOverlay = document.getElementById("overlay-timeout");
            if (timeoutOverlay) timeoutOverlay.classList.add("hidden");

            if (updatedAttempt.state === "ALL_SUBMITTED" || updatedAttempt.state === "SCORED") {
                showView("view-complete");
            } else {
                openPreBatteryInstructions(updatedAttempt.currentBatteryIndex);
            }
        }, 2000);

    } catch (err) {
        console.error("Error submitting battery session:", err);
        window.showCustomModal({title: 'Submission Failed', message: 'Failed to submit battery responses. Please check connection.', type: 'danger', icon: 'cloud_off'});
    }
}


async function loadAssessmentHistory() {
    try {
        const res = await fetch(`${API_BASE}/api/attempts/me/history`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            const history = await res.json();
            renderHistoryList(history);
        }
    } catch (e) {
        console.error("Failed to load history", e);
    }
}

function renderHistoryList(history) {
    const container = document.getElementById("historyListContainer");
    if (!container) return;
    
    if (!history || history.length === 0) {
        container.innerHTML = `<p class="text-sm text-slate-500 italic p-4 text-center">No past assessments found.</p>`;
        applyCurrentTranslation();
        return;
    }
    
    let html = "";
    history.forEach(attempt => {
        let isCompleted = (attempt.state === "SCORED" || attempt.state === "ALL_SUBMITTED");
        let dateObj = attempt.submitTime ? new Date(attempt.submitTime) : new Date(attempt.createdAt);
        let prefix = isCompleted ? "Completed" : "Started";
        
        const dateStr = dateObj.toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" });
        const timeStr = dateObj.toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
        
        let badgeHtml = "";
        if (isCompleted) {
            badgeHtml = `
                <button onclick="downloadReport(event, '${attempt.attemptToken}')" class="bg-emerald-50 hover:bg-emerald-100 text-emerald-700 border border-emerald-200 px-3 py-1.5 rounded-full text-[11px] font-bold flex items-center gap-1.5 transition-colors">
                    <span class="material-symbols-outlined text-[14px]">download</span>
                    <span>Download Report</span>
                </button>
            `;
        } else {
            badgeHtml = `
                <span class="bg-amber-50 text-amber-600 border border-amber-200 px-2.5 py-1 rounded-full text-[11px] font-semibold flex items-center gap-1">
                    <span class="material-symbols-outlined text-[14px]">pending_actions</span>
                    <span>In Progress</span>
                </span>
            `;
        }
        
        html += `
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2 p-3 bg-slate-50/70 hover:bg-slate-50 rounded-lg border border-slate-100 transition-colors">
                <div>
                    <h3 class="text-sm font-bold text-slate-800">Executive Leadership Assessment</h3>
                    <p class="text-[11px] text-slate-500 mt-0.5">${prefix} ${dateStr} at ${timeStr}</p>
                </div>
                ${badgeHtml}
            </div>
        `;
    });
    
    container.innerHTML = html;
    applyCurrentTranslation();
}

window.downloadReport = async function(event, token) {
    const btn = event.currentTarget;
    const originalHtml = btn.innerHTML;
    btn.innerHTML = `<span class="material-symbols-outlined text-[14px] animate-spin">refresh</span><span>Downloading...</span>`;
    try {
        const res = await fetch(`${API_BASE}/api/attempts/${token}/report`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            window.showCustomModal({title: 'Success', message: 'Your 5-Page Leadership Dossier report has been successfully downloaded.', type: 'success', icon: 'check_circle'});
        } else {
            window.showCustomModal({title: 'Generating', message: 'Report is still generating.\nPlease check back later.', icon: 'hourglass_empty'});
        }
    } catch (e) {
        window.showCustomModal({title: 'Error', message: 'Failed to download report.', type: 'danger', icon: 'error'});
    } finally {
        btn.innerHTML = originalHtml;
    }
};


function updateTestSidebar(attempt) {
    const navList = document.getElementById("batteryNavList");
    const progressText = document.getElementById("sidebarProgressText");
    if (!navList || !progressText) return;

    const titles = ["Personality (PQ10)", "SJT Ranking", "Derailers & Drivers", "Cognitive Abilities"];
    const currentIndex = attempt.currentBatteryIndex || 0;

    progressText.textContent = `Part ${currentIndex + 1} of 4`;

    let html = "";
    for (let i = 0; i < 4; i++) {
        let icon = "radio_button_unchecked";
        let colorClass = "text-slate-400 bg-slate-50";
        let textClass = "text-slate-500";
        let borderClass = "border-transparent bg-transparent";

        if (i < currentIndex) {
            icon = "check_circle";
            colorClass = "text-emerald-600 bg-emerald-50";
            textClass = "text-slate-700 font-semibold";
        } else if (i === currentIndex) {
            icon = "radio_button_checked";
            colorClass = "text-primary bg-primary/10";
            textClass = "text-primary font-bold";
            borderClass = "border-primary/20 bg-primary/5";
        }

        html += `
            <div class="flex items-center gap-3 p-2.5 rounded-lg border ${borderClass} transition-colors">
                <div class="w-8 h-8 rounded-full flex items-center justify-center shrink-0 ${colorClass}">
                    <span class="material-symbols-outlined text-[18px]">${icon}</span>
                </div>
                <span class="text-sm ${textClass}">Part ${i + 1}: ${titles[i]}</span>
            </div>
        `;
    }
    navList.innerHTML = html;
    applyCurrentTranslation();
}
