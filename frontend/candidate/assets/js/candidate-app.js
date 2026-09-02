
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
    "01 &bull; Personality (PQ10)": "01 • الشخصية (PQ10)",
    "140 items • 40 mins • Likert": "140 عنصر • 40 دقيقة • ليكرت",
    "02 • SJT Ranking": "02 • حكم المواقف (SJT)",
    "02 &bull; SJT Ranking": "02 • حكم المواقف (SJT)",
    "16 scenarios • 45 mins • Ranking": "16 سيناريو • 45 دقيقة • ترتيب",
    "03 • Derailers & Drivers": "03 • محاذير السلوك والدوافع",
    "03 &bull; Derailers & Drivers": "03 • محاذير السلوك والدوافع",
    "60 items • 20 mins • Likert": "60 عنصر • 20 دقيقة • ليكرت",
    "04 • Cognitive Abilities": "04 • القدرات المعرفية",
    "04 • Cognitive (GCAT)": "04 • القدرات المعرفية (GCAT)",
    "04 &bull; Cognitive (GCAT)": "04 • القدرات المعرفية (GCAT)",
    "24 patterns • 20 mins • MCQ": "24 نمط • 20 دقيقة • خيارات متعددة",
    "42 questions • 20 mins • MCQ": "42 سؤال • 20 دقيقة • خيارات متعددة",
    "Personality (PQ10)": "الشخصية (PQ10)",
    "Personality Assessment": "تقييم الشخصية",
    "Personality Evaluation (PQ10)": "تقييم الشخصية (PQ10)",
    "Situational Judgment (SJT)": "حكم المواقف (SJT)",
    "SJT Ranking": "حكم المواقف (SJT)",
    "Situational Judgment Assessment": "تقييم حكم المواقف",
    "Derailers & Drivers": "محاذير السلوك والدوافع",
    "Derailers Assessment": "تقييم محاذير السلوك",
    "Cognitive Abilities": "القدرات المعرفية",
    "Cognitive (GCAT)": "القدرات المعرفية (GCAT)",
    "Cognitive Abilities (GCAT)": "القدرات المعرفية (GCAT)",
    "Part 1: Personality (PQ10)": "اختبار الشخصية",
    "Part 2: SJT Ranking": "اختبار الحكم على المواقف",
    "Part 2: Situational Judgment (SJT)": "اختبار الحكم على المواقف",
    "Part 3: Derailers & Drivers": "اختبار السلوكيات المعطلة",
    "Part 4: Cognitive Abilities": "اختبار القدرات المعرفية",
    "Part 4: Cognitive (GCAT)": "اختبار القدرات المعرفية",
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
    "Report": "التقرير",
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
    "Battery Overview": "نظرة عامة على البطارية",
    "Previous Part Submitted Successfully": "تم تسليم الجزء السابق بنجاح",
    "Taking a brief break. You can start the next part immediately, or it will begin automatically in": "استراحة قصيرة. يمكنك بدء الجزء التالي فوراً، أو سيبدأ تلقائياً خلال",
    "s...": " ثانية...",
    "Error": "خطأ",
    "Failed to load test items.": "فشل في تحميل عناصر الاختبار.",
    "Submission Failed": "فشل التسليم",
    "Failed to submit battery responses. Please check connection.": "فشل في تسليم إجابات البطارية. يرجى التحقق من الاتصال.",
    "Failed to start assessment battery. Please try again.": "فشل في بدء بطارية التقييم. يرجى المحاولة مرة أخرى.",
    "Previous": "السابق",
    "Next": "التالي",
    "Submit Battery": "إرسال البطارية",
    "Profile Validity & Social Desirability": "صلاحية الملف والتظاهر الاجتماعي",
    "Elevated Impression Management": "ميل مرتفع للتظاهر الاجتماعي",
    "Normal Self-Report Profile": "استجابة طبيعية وموثوقة",
    "Interpret self-report responses with caution (High Social Desirability Risk)": "يُنصح بتفسير نتائج التقرير الذاتي بحذر (مؤشر تظاهر اجتماعي مرتفع)",
    "Honest and spontaneous self-report responses recorded": "استجابات صادقة وتلقائية مسجلة",
    "17 items per trait (Max 68.0 pts)": "17 عنصراً لكل سمة (الحد الأقصى 68 نقطة)",
    "Statement": "العبارة",
    "Item": "عنصر",
    "of": "من",
    "Confirm Submission": "تأكيد الإرسال",
    "Are you sure you want to finalize and submit this battery?\nYou cannot return to these questions.": "هل أنت متأكد أنك تريد إنهاء وإرسال هذه البطارية؟ لا يمكنك العودة إلى هذه الأسئلة.",
    "Overall Composite Score": "الدرجة الكلية المركبة",
    "Promotion Readiness": "جاهزية الترقية",
    "View Detailed Scores": "عرض النتائج التفصيلية",
    "View Scores": "عرض النتائج",
    "01 • Personality": "01 • الشخصية",
    "02 • Judgment": "02 • الحكم الموقفي",
    "03 • Derailers": "03 • المعوقات",
    "04 • Cognitive": "04 • القدرات المعرفية",
    "Executive Assessment Report": "تقرير التقييم التنفيذي",
    "Detailed Psychometric & Cognitive Breakdown": "تفصيل القياس النفسي والمعرفي الشامل",
    "Print / Save PDF": "طباعة / حفظ PDF",
    "Download JSON": "تحميل JSON",
    "Official Psychometric Evaluation Record": "سجل التقييم النفسي الرسمي المعتمد",
    "8 Competency Traits": "8 سمات كفاءة",
    "6 Derailer Risk Categories": "6 فئات لمخاطر التعطيل",
    "3 Cognitive Aptitude Subtests": "3 اختبارات فرعية معرفية",
    "Trait Code": "رمز السمة",
    "Dimension": "البُعد",
    "Raw Points": "النقاط الخام",
    "Percentage": "النسبة المئوية",
    "Risk Category": "فئة الخطر",
    "Subtest": "الاختبار الفرعي",
    "Accuracy": "الدقة",
    "Questions Correct": "الأسئلة الصحيحة",
    "Percentile": "المئين",
    "Score Breakdown": "تفصيل الدرجات",
    "Close": "إغلاق",
    "Submitted to HR": "تم الإرسال للموارد البشرية",
    "Scored": "تم التقييم",
    "Executive Leadership Assessment": "تقييم القيادة التنفيذية",
    "All 4 batteries have been successfully evaluated and scored.": "تم تقييم واحتساب درجات جميع البطاريات الأربع بنجاح.",
    "General Exam Instructions": "تعليمات وإرشادات الاختبار",
    "Key guidelines for candidates": "إرشادات هامة للمرشحين",
    "Continuous Session": "جلسة متواصلة واحدة",
    "Allocate approx. 90 minutes of quiet, uninterrupted time. Timers run continuously once started.": "خصّص حوالي 90 دقيقة من الوقت الهادئ دون انقطاع. المؤقت يعمل بشكل مستمر بمجرد البدء.",
    "Spontaneous Responses": "الإجابة العفوية والصادقة",
    "In personality & derailer batteries, choose the first response that naturally represents your behavior.": "في بطاريات الشخصية والمخاطر، اختر الاستجابة التلقائية التي تمثلك في بيئة العمل اليومية.",
    "Strategic Judgment (SJT)": "الحكم الموقفي الاستراتيجي",
    "In SJT scenarios, evaluate each managerial action and rank options from most effective to least effective.": "في مواقف القيادة، قيّم كل إجراء إداري ورتب الخيارات من الأكثر إلى الأقل فعالية.",
    "Cognitive Abilities (GCAT)": "القدرات المعرفية (GCAT)",
    "Pace yourself (approx. 30–45s per item). Do not linger on a single item; answer every question.": "وزّع وقتك بدقة (حوالي 30-45 ثانية لكل سؤال). تجنب التردد الطويل وأجب عن جميع الأسئلة.",
    "System & Stability": "استقرار النظام والاتصال",
    "Use a stable internet connection on desktop Chrome or Edge. Avoid refreshing the page during tests.": "استخدم اتصال إنترنت مستقر ومتصفح Chrome أو Edge على الحاسوب وتجنب تحديث الصفحة.",
    "Battery Transition:": "الانتقال بين البطاريات:",
    "A 1-minute preparation window is provided between each battery before the next section starts.": "يُمنح فاصل زمني مدته دقيقة واحدة بين كل بطارية وأخرى للاستعداد الذهني والانتقال."
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
            // Bypass i18n for survey question & answer contents (keep strictly in Arabic & RTL)
            if (node.id === "questionBody" || node.classList?.contains("survey-content") || node.classList?.contains("survey-options-container")) {
                return;
            }
            for (let child of node.childNodes) {
                translateNode(child, toLang);
            }
        }
    }
}

function updateNavigationDirection() {
    const isRtl = (document.documentElement.getAttribute("dir") || "ltr") === "rtl";
    const prevBtn = document.getElementById("prevQuestionBtn");
    const nextBtn = document.getElementById("nextQuestionBtn");
    const startBtn = document.getElementById("startAssessmentBtn");
    const logoutBtnText = document.getElementById("logoutBtnText");

    if (prevBtn) {
        if (isRtl) {
            prevBtn.innerHTML = `<span>السابق</span><span class="material-symbols-outlined text-sm">arrow_forward</span>`;
        } else {
            prevBtn.innerHTML = `<span class="material-symbols-outlined text-sm">arrow_back</span><span>Previous</span>`;
        }
    }

    if (nextBtn) {
        if (isRtl) {
            nextBtn.innerHTML = `<span class="material-symbols-outlined text-sm">arrow_back</span><span>التالي</span>`;
        } else {
            nextBtn.innerHTML = `<span>Next</span><span class="material-symbols-outlined text-sm">arrow_forward</span>`;
        }
    }

    if (startBtn) {
        const icon = startBtn.querySelector(".material-symbols-outlined");
        const span = startBtn.querySelector("span:not(.material-symbols-outlined)");
        if (icon) {
            icon.textContent = isRtl ? "arrow_back" : "arrow_forward";
        }
        if (span) {
            span.textContent = isRtl ? "ابدأ التقييم" : "Start Assessment";
        }
    }

    if (logoutBtnText) {
        logoutBtnText.textContent = isRtl ? "تسجيل الخروج" : "Log Out";
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
    updateNavigationDirection();
}

window.applyTranslation = applyTranslation;
window.updateNavigationDirection = updateNavigationDirection;

function applyCurrentTranslation() {
    if (document.documentElement.getAttribute("dir") === "rtl") {
        applyTranslation("ar");
    } else {
        updateNavigationDirection();
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
let intermissionTimerInterval = null;
let remainingSeconds = 0;

// Battery definitions metadata
const BATTERY_METADATA = [
    {
        name: "Personality Evaluation (PQ10)",
        nameAr: "اختبار الشخصية",
        part: "Part 1 of 4",
        partAr: "الجزء 1 من 4",
        badge: "PQ10",
        itemsCount: 140,
        itemCountAr: "140 سؤالاً",
        timeLimit: "40 Minutes",
        timeLimitAr: "40 دقيقة",
        format: "Likert Scale",
        formatAr: "مقياس ليكرت",
        instructions: [
            "Read each item carefully and respond with your immediate, natural judgment.",
            "There are no 'right' or 'wrong' personality answers; consistency is evaluated.",
            "The test timer is strictly enforced by the server. Once started, it cannot be paused.",
            "Your answers auto-save continuously in real time."
        ],
        instructionsAr: [
            "اقرأ كل سؤال بعناية وأجب بناءً على حكمك الفطري والمباشر.",
            "لا توجد إجابات \"صحيحة\" أو \"خاطئة\" في تقييم الشخصية؛ بل يتم تقييم مدى اتساق الإجابات.",
            "وقت الاختبار محدد بدقة، وبمجرد بدء البطارية، لا يمكن إيقاف المؤقت مؤقتاً.",
            "يتم حفظ إجاباتك تلقائياً وباستمرار في الوقت الفعلي."
        ]
    },
    {
        name: "Situational Judgment (SJT)",
        nameAr: "اختبار الحكم على المواقف",
        part: "Part 2 of 4",
        partAr: "الجزء 2 من 4",
        badge: "SJT",
        itemsCount: 16,
        itemCountAr: "16 سؤالاً",
        timeLimit: "45 Minutes",
        timeLimitAr: "45 دقيقة",
        format: "4-Option Ranking",
        formatAr: "ترتيب الخيارات (4)",
        instructions: [
            "You will be presented with real-world executive scenarios and workplace challenges.",
            "Order the 4 available actions from Most Effective (Rank 1) to Least Effective (Rank 4).",
            "Use the up/down arrows to adjust the relative ranking of each proposed response.",
            "You have 45 minutes to complete all 16 scenarios."
        ],
        instructionsAr: [
            "اقرأ كل موقف قيادي بعناية وأجب بناءً على حكمك الفطري والمباشر.",
            "رتّب الإجراءات الأربعة المقترحة من الأكثر فعالية (الترتيب 1) إلى الأقل فعالية (الترتيب 4).",
            "وقت الاختبار محدد بدقة، وبمجرد بدء البطارية، لا يمكن إيقاف المؤقت مؤقتاً.",
            "يتم حفظ إجاباتك تلقائياً وباستمرار في الوقت الفعلي."
        ]
    },
    {
        name: "Derailers & Drivers",
        nameAr: "اختبار السلوكيات المعطلة",
        part: "Part 3 of 4",
        partAr: "الجزء 3 من 4",
        badge: "DERAILERS",
        itemsCount: 60,
        itemCountAr: "60 سؤالاً",
        timeLimit: "20 Minutes",
        timeLimitAr: "20 دقيقة",
        format: "Likert Scale",
        formatAr: "مقياس ليكرت",
        instructions: [
            "This section assesses behavior tendencies under pressure, stress, and heavy workloads.",
            "Indicate your level of agreement with each workplace scenario statement.",
            "Be frank and transparent in your self-assessment.",
            "You have 20 minutes for this 60-item battery."
        ],
        instructionsAr: [
            "اقرأ كل سؤال بعناية وأجب بناءً على حكمك الفطري والمباشر.",
            "لا توجد إجابات \"صحيحة\" أو \"خاطئة\" في تقييم السلوكيات؛ بل يتم تقييم مدى اتساق الإجابات.",
            "وقت الاختبار محدد بدقة، وبمجرد بدء البطارية، لا يمكن إيقاف المؤقت مؤقتاً.",
            "يتم حفظ إجاباتك تلقائياً وباستمرار في الوقت الفعلي."
        ]
    },
    {
        name: "Cognitive Abilities (GCAT)",
        nameAr: "اختبار القدرات المعرفية",
        part: "Part 4 of 4",
        partAr: "الجزء 4 من 4",
        badge: "GCAT",
        itemsCount: 42,
        itemCountAr: "42 سؤالاً",
        timeLimit: "20 Min Strict",
        timeLimitAr: "20 دقيقة (محدد بدقة)",
        format: "Multiple Choice (MCQ)",
        formatAr: "اختيار من متعدد (MCQ)",
        instructions: [
            "Evaluates Verbal, Numerical, and Abstract pattern reasoning aptitude.",
            "Each question has one single correct answer option.",
            "This battery has a STRICT 20-minute time cutoff enforced by the server.",
            "Work as quickly and accurately as possible."
        ],
        instructionsAr: [
            "اقرأ كل مسألة بدقة وحدد الإجابة المنطقية الصحيحة.",
            "يحتوي كل سؤال على خيار صحيح واحد فقط.",
            "وقت الاختبار محدد بدقة، وبمجرد بدء البطارية، لا يمكن إيقاف المؤقت مؤقتاً.",
            "يتم حفظ إجاباتك تلقائياً وباستمرار في الوقت الفعلي."
        ]
    }
];

// Initialize Application
function initCandidateApp() {
    checkAuth();
    initEventListeners();
    
    // Enforce Arabic RTL by default
    const htmlEl = document.documentElement;
    htmlEl.setAttribute("dir", "rtl");
    htmlEl.setAttribute("lang", "ar");
    const rtlToggle = document.getElementById("rtlToggle");
    if (rtlToggle) {
        rtlToggle.textContent = "Switch to English";
    }
    const warningBlock = document.getElementById("warningBlock");
    if (warningBlock) {
        warningBlock.classList.remove("border-l-4", "border-l-[#131b2e]", "rounded-r");
        warningBlock.classList.add("border-r-4", "border-r-[#131b2e]", "rounded-l");
    }
    applyTranslation("ar");
    updateNavigationDirection();

    loadAssessmentState();
    loadAssessmentHistory();
}

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initCandidateApp);
} else {
    initCandidateApp();
}

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
            updateNavigationDirection();
            if (activeItems && activeItems.length > 0 && activeSession) {
                renderCurrentQuestion();
            }
            if (currentAttempt) {
                updateTestSidebar(currentAttempt);
            }
        });
    }

    const startAssessmentBtn = document.getElementById("startAssessmentBtn");
    if (startAssessmentBtn) {
        startAssessmentBtn.onclick = () => {
            const bIdx = (currentAttempt && typeof currentAttempt.currentBatteryIndex === 'number') ? currentAttempt.currentBatteryIndex : 0;
            openPreBatteryInstructions(bIdx);
        };
    }

    const beginBatteryBtn = document.getElementById("beginBatteryBtn");
    if (beginBatteryBtn) {
        beginBatteryBtn.onclick = startActiveBatterySession;
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

    // Toggle Sidebars & Sections
    const dashboardSidebar = document.getElementById("dashboardSidebar");
    const testSidebar = document.getElementById("testSidebar");
    const instructionsSidebar = document.getElementById("instructionsSidebar");
    const historySection = document.getElementById("historySection");
    const mainContentArea = document.getElementById("mainContentArea");

    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (instructionsSidebar) instructionsSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
        if (mainContentArea) {
            mainContentArea.classList.add("flex-1");
            mainContentArea.classList.remove("flex-grow");
        }
    } else {
        // Dashboard views (pending, complete, empty)
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (instructionsSidebar) instructionsSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.remove("hidden");
        if (mainContentArea) {
            mainContentArea.classList.add("flex-1");
            mainContentArea.classList.remove("flex-grow");
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
        showCompletedAssessmentView(attempt);
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
function openPreBatteryInstructions(batteryIndex, isIntermission = false) {
    if (intermissionTimerInterval) {
        clearInterval(intermissionTimerInterval);
        intermissionTimerInterval = null;
    }

    const isArabic = (document.documentElement.getAttribute("dir") || "ltr") === "rtl";
    const meta = BATTERY_METADATA[batteryIndex] || BATTERY_METADATA[0];

    const instPartEl = document.getElementById("instPartNumber");
    if (instPartEl) instPartEl.textContent = isArabic ? (meta.partAr || meta.part) : meta.part;

    const instTitleEl = document.getElementById("instBatteryTitle");
    if (instTitleEl) instTitleEl.textContent = isArabic ? (meta.nameAr || meta.name) : meta.name;

    const instArabicTitleEl = document.getElementById("instBatteryArabicTitle");
    if (instArabicTitleEl) {
        if (isArabic) {
            instArabicTitleEl.classList.add("hidden");
        } else {
            instArabicTitleEl.classList.remove("hidden");
            instArabicTitleEl.textContent = meta.nameAr || "";
        }
    }

    const instItemCountEl = document.getElementById("instItemCount");
    if (instItemCountEl) instItemCountEl.textContent = isArabic ? (meta.itemCountAr || `${meta.itemsCount} سؤالاً`) : `${meta.itemsCount} Items`;

    const instTimeLimitEl = document.getElementById("instTimeLimit");
    if (instTimeLimitEl) instTimeLimitEl.textContent = isArabic ? (meta.timeLimitAr || meta.timeLimit) : meta.timeLimit;

    const instFormatTypeEl = document.getElementById("instFormatType");
    if (instFormatTypeEl) instFormatTypeEl.textContent = isArabic ? (meta.formatAr || meta.format) : meta.format;

    const lblTotalItems = document.getElementById("lblTotalItems");
    if (lblTotalItems) lblTotalItems.textContent = isArabic ? "عدد الأسئلة" : "TOTAL ITEMS";

    const lblTimeAllowed = document.getElementById("lblTimeAllowed");
    if (lblTimeAllowed) lblTimeAllowed.textContent = isArabic ? "الوقت المسموح" : "TIME ALLOWED";

    const lblType = document.getElementById("lblType");
    if (lblType) lblType.textContent = isArabic ? "نوع الأسئلة" : "TYPE";

    const instHeading = document.getElementById("instHeading");
    if (instHeading) instHeading.textContent = isArabic ? "تعليمات هامة:" : "Important Instructions:";

    const instContainer = document.getElementById("instContainer");
    if (instContainer) {
        if (isArabic) {
            instContainer.setAttribute("dir", "rtl");
            instContainer.style.textAlign = "right";
            instContainer.style.direction = "rtl";
        } else {
            instContainer.setAttribute("dir", "ltr");
            instContainer.style.textAlign = "left";
            instContainer.style.direction = "ltr";
        }
    }

    const listEl = document.getElementById("instBulletList");
    if (listEl) {
        const bullets = isArabic ? (meta.instructionsAr || meta.instructions) : meta.instructions;
        listEl.innerHTML = bullets.map(b => `<li>${b}</li>`).join("");
        if (isArabic) {
            listEl.className = "space-y-2 list-disc pr-5 pl-0 text-right";
            listEl.setAttribute("dir", "rtl");
            listEl.style.direction = "rtl";
            listEl.style.textAlign = "right";
        } else {
            listEl.className = "space-y-2 list-disc pl-5 pr-0 text-left";
            listEl.setAttribute("dir", "ltr");
            listEl.style.direction = "ltr";
            listEl.style.textAlign = "left";
        }
    }

    const beginBtnText = document.getElementById("beginBatteryBtnText");
    if (beginBtnText) beginBtnText.textContent = isArabic ? "أفهم ذلك • ابدأ البطارية" : "I Understand • Begin Battery";

    const intermissionBlock = document.getElementById("instIntermissionBlock");
    const intermissionSecs = document.getElementById("instIntermissionSecs");
    const intermissionBar = document.getElementById("instIntermissionBar");

    if (isIntermission && intermissionBlock) {
        intermissionBlock.classList.remove("hidden");
        let remaining = 60;
        if (intermissionSecs) intermissionSecs.textContent = remaining;
        if (intermissionBar) {
            intermissionBar.style.width = "100%";
            intermissionBar.style.transition = "width 1s linear";
        }

        intermissionTimerInterval = setInterval(() => {
            remaining--;
            if (intermissionSecs) intermissionSecs.textContent = remaining;
            if (intermissionBar) intermissionBar.style.width = `${(remaining / 60) * 100}%`;

            if (remaining <= 0) {
                clearInterval(intermissionTimerInterval);
                intermissionTimerInterval = null;
                // Auto-advance by force into the next battery!
                startActiveBatterySession();
            }
        }, 1000);
    } else if (intermissionBlock) {
        intermissionBlock.classList.add("hidden");
    }

    updateTestSidebar(currentAttempt);
    showView("view-instructions");
    applyCurrentTranslation();
}

// Start / Unlock Battery
async function startActiveBatterySession() {
    if (intermissionTimerInterval) {
        clearInterval(intermissionTimerInterval);
        intermissionTimerInterval = null;
    }
    const intermissionBlock = document.getElementById("instIntermissionBlock");
    if (intermissionBlock) intermissionBlock.classList.add("hidden");

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

        // For SJT, pre-populate default order so accepting default without swapping is safely recorded
        if (session.sequenceOrder === 1 || (session.batteryType && session.batteryType.toUpperCase() === 'SJT')) {
            activeItems.forEach(item => {
                if (item.options && Array.isArray(item.options)) {
                    responsesMap[item.id] = {
                        rankingOrder: item.options.map(o => o.optionKey),
                        responseTimeMs: 0
                    };
                }
            });
        }

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
    const isGlobalRtl = (document.documentElement.getAttribute("dir") || "ltr") === "rtl";

    // Survey content is strictly locked in Arabic & RTL:
    // Highest positive option ("موافق بشدة" - 5) is placed on the far right (1st column in RTL),
    // down to lowest option ("غير موافق بشدة" - 1) on the far left (5th column in RTL).
    const scaleLabels = [
        { val: 5, labelAr: "موافق بشدة" },
        { val: 4, labelAr: "موافق" },
        { val: 3, labelAr: "محايد" },
        { val: 2, labelAr: "غير موافق" },
        { val: 1, labelAr: "غير موافق بشدة" }
    ];

    let html = `
        <div class="space-y-4 survey-content" dir="rtl" style="direction: rtl; text-align: right;">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">${isGlobalRtl ? 'العبارة' : 'Statement'} ${currentItemIndex + 1}</span>
            <h2 class="text-xl sm:text-2xl font-bold text-on-surface leading-relaxed arabic-text" dir="rtl" style="direction: rtl; text-align: right;">
                ${item.statementAr || item.statement || ""}
            </h2>
        </div>

        <div class="survey-options-container grid grid-cols-1 sm:grid-cols-5 gap-3 pt-4" dir="rtl" style="direction: rtl;">
    `;

    scaleLabels.forEach(s => {
        const isActive = (currentVal === s.val);
        html += `
            <button type="button" class="likert-btn ${isActive ? 'active' : ''} flex flex-col items-center justify-center p-4 border border-slate-300 rounded-xl transition-all text-center gap-2 cursor-pointer bg-white hover:border-primary/50" data-value="${s.val}">
                <span class="w-6 h-6 rounded-full border-2 border-slate-400 flex items-center justify-center indicator"></span>
                <span class="text-xs font-bold text-slate-800 arabic-text">${s.labelAr}</span>
            </button>
        `;
    });

    html += `</div>`;
    container.innerHTML = html;
    container.setAttribute("dir", "rtl");
    container.style.direction = "rtl";
    container.style.textAlign = "right";

    // Attach Likert Click Handlers
    container.querySelectorAll(".likert-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const val = Number(btn.dataset.value);
            if (!responsesMap[item.id]) responsesMap[item.id] = {};
            responsesMap[item.id].selectedLikert = val;

            container.querySelectorAll(".likert-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
        });
    });
}

// 2. SJT Ranking Question Renderer
function renderSjtRankingQuestion(item, container) {
    let options = item.options || [];
    const isGlobalRtl = (document.documentElement.getAttribute("dir") || "ltr") === "rtl";
    
    // Check if we already have a saved ranking order for this item
    const savedOrder = responsesMap[item.id]?.rankingOrder;
    if (savedOrder && savedOrder.length === options.length) {
        options = savedOrder.map(k => options.find(o => o.optionKey === k)).filter(Boolean);
    }

    let html = `
        <div class="space-y-3 survey-content" dir="rtl" style="direction: rtl; text-align: right;">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">${isGlobalRtl ? 'الموقف' : 'Scenario'} ${currentItemIndex + 1} &bull; ${item.itemCode || ""}</span>
            <h2 class="text-lg sm:text-xl font-bold text-on-surface leading-tight arabic-text" dir="rtl">${item.titleAr || ""}</h2>
            <div class="p-4 bg-slate-50 border border-slate-200 rounded-xl text-sm text-slate-700 leading-relaxed arabic-text" dir="rtl">
                ${item.narrativeAr || ""}
            </div>
            ${item.scenarioImageUrl ? `<img src="${item.scenarioImageUrl}" class="max-h-60 rounded-xl object-contain mx-auto my-2 border border-slate-200">` : ''}
        </div>

        <div class="space-y-2 pt-2 survey-options-container" dir="rtl" style="direction: rtl;">
            <div class="flex justify-between items-center px-2 text-xs font-bold text-slate-500" dir="rtl">
                <span class="text-emerald-700 flex items-center gap-1"><span class="material-symbols-outlined text-sm">north</span> الإجراء الأكثر فعالية (الترتيب 1)</span>
                <span class="text-slate-400">رتب الإجراءات باستخدام الأسهم</span>
            </div>

            <div id="sjtOptionsList" class="space-y-2.5">
    `;

    options.forEach((opt, idx) => {
        html += `
            <div class="sjt-option-card flex items-center justify-between p-3.5 bg-white border border-slate-200 rounded-xl shadow-xs hover:border-slate-300 transition-all gap-3" data-key="${opt.optionKey}" dir="rtl">
                <div class="flex items-center gap-3">
                    <span class="w-7 h-7 rounded-full bg-slate-100 border border-slate-200 text-slate-700 font-bold text-xs flex items-center justify-center shrink-0">
                        ${idx + 1}
                    </span>
                    <p class="text-xs sm:text-sm text-slate-800 arabic-text leading-relaxed text-right">${opt.statementAr}</p>
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
            <div class="text-left px-2 text-xs font-bold text-red-600 flex items-center justify-start gap-1" dir="rtl">
                <span>الإجراء الأقل فعالية (الترتيب ${options.length})</span>
                <span class="material-symbols-outlined text-sm">south</span>
            </div>
        </div>
    `;

    container.innerHTML = html;
    container.setAttribute("dir", "rtl");
    container.style.direction = "rtl";
    container.style.textAlign = "right";

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
    const isGlobalRtl = (document.documentElement.getAttribute("dir") || "ltr") === "rtl";

    let html = `
        <div class="space-y-3 survey-content" dir="rtl" style="direction: rtl; text-align: right;">
            <span class="text-xs font-bold text-slate-400 uppercase tracking-widest">${isGlobalRtl ? 'السؤال' : 'Question'} ${currentItemIndex + 1} &bull; ${item.itemCode || ""}</span>
            <h2 class="text-lg sm:text-xl font-bold text-on-surface leading-tight arabic-text" dir="rtl">${item.titleAr || ""}</h2>
            ${item.promptTextAr ? `<p class="text-sm text-slate-700 arabic-text leading-relaxed text-right" dir="rtl">${item.promptTextAr}</p>` : ''}
            
            ${item.questionImageUrl ? `
                <div class="p-3 bg-slate-50 rounded-xl border border-slate-200 text-center">
                    <img src="${item.questionImageUrl}" alt="Pattern Diagram" class="max-h-64 rounded-lg object-contain mx-auto">
                </div>
            ` : ''}
        </div>

        <div class="space-y-2.5 pt-2 survey-options-container" dir="rtl" style="direction: rtl;">
    `;

    options.forEach(opt => {
        const isSelected = (currentVal === opt.optionKey);
        html += `
            <label class="gcat-option-label flex items-center justify-between p-3.5 border ${isSelected ? 'border-primary bg-primary/5 ring-1 ring-primary' : 'border-slate-200 bg-white hover:border-slate-300'} rounded-xl cursor-pointer transition-all gap-3" dir="rtl">
                <div class="flex items-center gap-3">
                    <div class="w-6 h-6 rounded-full border-2 ${isSelected ? 'border-primary' : 'border-slate-400'} flex items-center justify-center shrink-0">
                        <div class="w-3 h-3 rounded-full bg-primary ${isSelected ? 'opacity-100' : 'opacity-0'}"></div>
                    </div>
                    <span class="w-6 h-6 rounded-md bg-slate-100 text-slate-700 font-bold text-xs flex items-center justify-center shrink-0">
                        ${opt.optionKey}
                    </span>
                    <span class="text-xs sm:text-sm text-slate-800 arabic-text text-right">${opt.textAr || ""}</span>
                </div>
                ${opt.imageUrl ? `<img src="${opt.imageUrl}" class="h-10 object-contain rounded border border-slate-100">` : ''}
                <input type="radio" name="gcat_option" value="${opt.optionKey}" ${isSelected ? 'checked' : ''} class="sr-only">
            </label>
        `;
    });

    html += `</div>`;
    container.innerHTML = html;
    container.setAttribute("dir", "rtl");
    container.style.direction = "rtl";
    container.style.textAlign = "right";

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
        });
    });
}

// Submit Active Battery
async function submitActiveBattery(isAutoTimeout = false) {
    if (countdownTimerInterval) clearInterval(countdownTimerInterval);
    if (heartbeatInterval) clearInterval(heartbeatInterval);

    // CRUCIAL: Immediately flush current responses before submit
    await sendHeartbeat();

    const autoAdvanceOverlay = document.getElementById("overlay-auto-advance");
    if (!isAutoTimeout && autoAdvanceOverlay) {
        autoAdvanceOverlay.classList.remove("hidden");
    }

    const payloadList = Object.keys(responsesMap).map(itemId => ({
        itemId: Number(itemId),
        selectedLikert: responsesMap[itemId].selectedLikert || null,
        rankingOrder: responsesMap[itemId].rankingOrder || null,
        selectedOption: responsesMap[itemId].selectedOption || null,
        responseTimeMs: responsesMap[itemId].responseTimeMs || 0
    }));

    try {
        const res = await fetch(`${API_BASE}/api/attempts/battery-sessions/${activeSession.id}/submit`, {
            method: "POST",
            headers: {
                ...getAuthHeader(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ responses: payloadList })
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
                openPreBatteryInstructions(updatedAttempt.currentBatteryIndex, true);
            }
        }, 2000);

    } catch (err) {
        console.error("Error submitting battery session:", err);
        window.showCustomModal({title: 'Submission Failed', message: 'Failed to submit battery responses. Please check connection.', type: 'danger', icon: 'cloud_off'});
    }
}


async function showCompletedAssessmentView(attempt) {
    showView("view-complete");
    
    try {
        const token = attempt.attemptToken;
        const res = await fetch(`${API_BASE}/api/attempts/${token}/score`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            const score = await res.json();
            populateCompletedScoreHero(score, attempt);
        }
    } catch (e) {
        console.error("Failed to load attempt score:", e);
    }
}

function populateCompletedScoreHero(score, attempt) {
    const compVal = document.getElementById("completedCompositeVal");
    const percBadge = document.getElementById("completedPercentileBadge");
    const readBadge = document.getElementById("completedReadinessBadge");
    const readLabel = document.getElementById("completedReadinessLabel");
    
    const pqScore = document.getElementById("completedPqScore");
    const pqBar = document.getElementById("completedPqBar");
    const sjtScore = document.getElementById("completedSjtScore");
    const sjtBar = document.getElementById("completedSjtBar");
    const derailerScore = document.getElementById("completedDerailerScore");
    const derailerBar = document.getElementById("completedDerailerBar");
    const gcatScore = document.getElementById("completedGcatScore");
    const gcatBar = document.getElementById("completedGcatBar");

    if (compVal) compVal.textContent = `${score.compositeScore ?? 0}%`;
    if (percBadge) percBadge.textContent = `Percentile: P${score.percentile ?? 1}`;
    
    const penaltyNotice = document.getElementById("completedPenaltyNotice");
    const penaltyText = document.getElementById("completedPenaltyText");
    const isArLang = document.documentElement.getAttribute("dir") === "rtl";

    if (score.cappedPenaltyPct && score.cappedPenaltyPct > 0) {
        if (penaltyNotice) penaltyNotice.classList.remove("hidden");
        if (penaltyText) {
            penaltyText.textContent = isArLang
                ? `تم تطبيق خصم صدق الاستجابة: -${score.cappedPenaltyPct}% (الدرجة الأولية: ${score.rawCompositeScore}%)`
                : `Validity Deduction: -${score.cappedPenaltyPct}% (Raw Composite: ${score.rawCompositeScore}%)`;
        }
    } else if (penaltyNotice) {
        penaltyNotice.classList.add("hidden");
    }
    
    const readiness = getReadinessInfo(score.readinessBand);
    if (readLabel) {
        const isAr = document.documentElement.getAttribute("dir") === "rtl";
        readLabel.textContent = isAr ? `${readiness.labelAr} (${readiness.labelEn})` : `${readiness.labelEn} (${readiness.labelAr})`;
    }
    if (readBadge) {
        readBadge.className = `inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold border ${readiness.badgeClass}`;
    }

    if (pqScore) pqScore.textContent = `${score.personalityScorePct ?? 0}%`;
    if (pqBar) pqBar.style.width = `${Math.min(100, Math.max(0, score.personalityScorePct ?? 0))}%`;

    if (sjtScore) sjtScore.textContent = `${score.sjtScorePct ?? 0}%`;
    if (sjtBar) sjtBar.style.width = `${Math.min(100, Math.max(0, score.sjtScorePct ?? 0))}%`;

    if (derailerScore) derailerScore.textContent = `${score.derailersEffectiveScorePct ?? 0}%`;
    if (derailerBar) derailerBar.style.width = `${Math.min(100, Math.max(0, score.derailersEffectiveScorePct ?? 0))}%`;

    if (gcatScore) gcatScore.textContent = `${score.cognitiveScorePct ?? 0}%`;
    if (gcatBar) gcatBar.style.width = `${Math.min(100, Math.max(0, score.cognitiveScorePct ?? 0))}%`;

    // Response Validity Indicators (مقياس التظاهر الاجتماعي ومؤشر الوسطية)
    const sdScoreEl = document.getElementById("completedSdScore");
    const sdLabelEl = document.getElementById("completedSdLabel");
    const sdBadgeEl = document.getElementById("completedSdBadge");
    const sdCardEl = document.getElementById("completedSdCard");
    const sdIconBgEl = document.getElementById("completedSdIconBg");

    const isSdElevated = !!score.elevatedImpressionManagement;
    const sdPct = score.socialDesirabilityRiskPct ?? 0;

    if (sdScoreEl) sdScoreEl.textContent = `${sdPct}%`;
    if (sdLabelEl) {
        sdLabelEl.textContent = isArLang
            ? (isSdElevated ? 'ميل مرتفع للتظاهر الاجتماعي (حذر بالتفسير)' : 'استجابة واقعية وصادقة وتلقائية')
            : (isSdElevated ? 'Elevated Impression Management' : 'Candid / Honest Profile');
    }
    if (sdBadgeEl) {
        sdBadgeEl.className = isSdElevated
            ? 'inline-block px-2.5 py-1 text-xs font-bold rounded-full bg-amber-100 text-amber-900 border border-amber-300'
            : 'inline-block px-2.5 py-1 text-xs font-bold rounded-full bg-emerald-100 text-emerald-800 border border-emerald-300';
    }
    if (sdCardEl) {
        sdCardEl.className = isSdElevated
            ? 'bg-amber-50/70 p-3.5 rounded-lg border border-amber-300 shadow-2xs flex items-center justify-between'
            : 'bg-white p-3.5 rounded-lg border border-slate-200 shadow-2xs flex items-center justify-between';
    }
    if (sdIconBgEl) {
        sdIconBgEl.className = isSdElevated
            ? 'w-8 h-8 rounded-lg bg-amber-200 text-amber-800 flex items-center justify-center shrink-0'
            : 'w-8 h-8 rounded-lg bg-emerald-50 text-emerald-700 flex items-center justify-center shrink-0';
    }

    const ctScoreEl = document.getElementById("completedCtScore");
    const ctLabelEl = document.getElementById("completedCtLabel");
    const ctBadgeEl = document.getElementById("completedCtBadge");
    const ctCardEl = document.getElementById("completedCtCard");
    const ctIconBgEl = document.getElementById("completedCtIconBg");

    const isCtElevated = !!score.elevatedCentralTendency;
    const ctPct = score.centralTendencyRatePct ?? 0;

    if (ctScoreEl) ctScoreEl.textContent = `${ctPct}%`;
    if (ctLabelEl) {
        ctLabelEl.textContent = isArLang
            ? (isCtElevated ? 'نزعة مرتفعة نحو الخيار المحايد' : 'تنوع واستجابة متوازنة عبر المقياس')
            : (isCtElevated ? 'Elevated Midpoint Tendency' : 'Balanced Differentiation');
    }
    if (ctBadgeEl) {
        ctBadgeEl.className = isCtElevated
            ? 'inline-block px-2.5 py-1 text-xs font-bold rounded-full bg-amber-100 text-amber-900 border border-amber-300'
            : 'inline-block px-2.5 py-1 text-xs font-bold rounded-full bg-emerald-100 text-emerald-800 border border-emerald-300';
    }
    if (ctCardEl) {
        ctCardEl.className = isCtElevated
            ? 'bg-amber-50/70 p-3.5 rounded-lg border border-amber-300 shadow-2xs flex items-center justify-between'
            : 'bg-white p-3.5 rounded-lg border border-slate-200 shadow-2xs flex items-center justify-between';
    }
    if (ctIconBgEl) {
        ctIconBgEl.className = isCtElevated
            ? 'w-8 h-8 rounded-lg bg-amber-200 text-amber-800 flex items-center justify-center shrink-0'
            : 'w-8 h-8 rounded-lg bg-emerald-50 text-emerald-700 flex items-center justify-center shrink-0';
    }

    const viewBtn = document.getElementById("completedViewScoresBtn");
    if (viewBtn) {
        viewBtn.onclick = () => window.viewScoreModal(attempt.attemptToken);
    }

    const downloadBtn = document.getElementById("completedDownloadBtn");
    if (downloadBtn) {
        downloadBtn.onclick = (e) => window.downloadReport(e, attempt.attemptToken);
    }

    applyCurrentTranslation();
}

function getReadinessInfo(band) {
    switch (band) {
        case "EXCELLENT":
            return {
                labelEn: "Excellent",
                labelAr: "متميز",
                badgeClass: "bg-emerald-100 text-emerald-800 border-emerald-300",
                descEn: "Candidate exhibits exceptional leadership aptitude across cognitive, behavioral, and judgment dimensions.",
                descAr: "يُظهر المرشح كفاءة قيادية استثنائية عبر الأبعاد المعرفية والسلوكية والحكم الموقفي."
            };
        case "STRONG":
            return {
                labelEn: "Strong",
                labelAr: "متقدم",
                badgeClass: "bg-teal-100 text-teal-800 border-teal-300",
                descEn: "Candidate displays strong executive readiness and robust analytical problem-solving skills.",
                descAr: "يتمتع المرشح بجاهزية قيادية متقدمة ومهارات تحليلية قوية لحل المشكلات."
            };
        case "ACCEPTABLE":
            return {
                labelEn: "Acceptable",
                labelAr: "مقبول",
                badgeClass: "bg-indigo-100 text-indigo-800 border-indigo-300",
                descEn: "Candidate meets the required benchmarks for leadership responsibilities with moderate development areas.",
                descAr: "يستوفي المرشح المعايير المطلوبة للمسؤوليات القيادية مع وجود مجالات تطوير متوسطة."
            };
        case "FOUNDATIONAL_ADVANCED":
            return {
                labelEn: "Foundational Advanced",
                labelAr: "تأسيسي متقدم",
                badgeClass: "bg-amber-100 text-amber-800 border-amber-300",
                descEn: "Candidate shows foundational leadership competencies with specific skill development recommended.",
                descAr: "يُظهر المرشح كفاءات قيادية تأسيسية مع التوصية بتطوير مهارات محددة."
            };
        case "FOUNDATIONAL":
        default:
            return {
                labelEn: "Foundational",
                labelAr: "تأسيسي",
                badgeClass: "bg-rose-100 text-rose-800 border-rose-300",
                descEn: "Candidate is currently at a foundational stage. Targeted executive coaching is advised.",
                descAr: "يمر المرشح حاليًا بمرحلة تأسيسية، ويُنصح ببرامج توجيه قيادي مستهدفة."
            };
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
        
        let actionHtml = "";
        if (isCompleted) {
            actionHtml = `
                <div class="flex items-center gap-1.5 shrink-0">
                    <button onclick="window.viewScoreModal('${attempt.attemptToken}')" class="bg-primary/10 hover:bg-primary/20 text-primary border border-primary/20 py-1.5 px-2.5 rounded-lg text-xs font-bold flex items-center justify-center gap-1 transition-colors whitespace-nowrap">
                        <span class="material-symbols-outlined text-[15px]">analytics</span>
                        <span>View Scores</span>
                    </button>
                    <button onclick="window.downloadReport(event, '${attempt.attemptToken}')" class="bg-white hover:bg-slate-100 text-slate-700 border border-slate-300 py-1.5 px-2.5 rounded-lg text-xs font-bold flex items-center justify-center gap-1 transition-colors shadow-2xs whitespace-nowrap">
                        <span class="material-symbols-outlined text-[15px] text-primary">download</span>
                        <span>Report</span>
                    </button>
                </div>
            `;
        } else {
            actionHtml = `
                <div class="flex items-center gap-2">
                    <span class="bg-amber-50 text-amber-700 border border-amber-200 px-2.5 py-1 rounded-full text-[11px] font-semibold flex items-center gap-1">
                        <span class="material-symbols-outlined text-[14px]">pending_actions</span>
                        <span>In Progress</span>
                    </span>
                    <button onclick="window.location.search = '?token=${attempt.attemptToken}'" class="text-xs text-primary font-bold hover:underline">
                        Resume &rarr;
                    </button>
                </div>
            `;
        }
        
        html += `
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 p-3.5 bg-slate-50/70 hover:bg-slate-50 rounded-xl border border-slate-200/90 transition-all">
                <div class="space-y-0.5 min-w-0">
                    <div class="flex items-center gap-2">
                        <h3 class="text-xs sm:text-sm font-bold text-slate-800 truncate">Executive Leadership Assessment</h3>
                        ${isCompleted ? `<span class="bg-emerald-50 text-emerald-700 border border-emerald-200 px-2 py-0.5 rounded-full text-[10px] font-bold shrink-0">Scored</span>` : ''}
                    </div>
                    <p class="text-[11px] text-slate-500">${prefix} ${dateStr} at ${timeStr}</p>
                </div>
                ${actionHtml}
            </div>
        `;
    });
    
    container.innerHTML = html;
    applyCurrentTranslation();
}

window.viewScoreModal = async function(token) {
    const modal = document.getElementById("scoreReportModal");
    const modalBody = document.getElementById("scoreModalBody");
    const candidateInfo = document.getElementById("modalCandidateInfo");
    if (!modal || !modalBody) return;

    modalBody.innerHTML = `
        <div class="flex flex-col items-center justify-center py-16 text-center space-y-3">
            <div class="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin"></div>
            <p class="text-xs text-slate-500 font-medium">Loading assessment score breakdown...</p>
        </div>
    `;
    modal.classList.remove("hidden");

    try {
        const res = await fetch(`${API_BASE}/api/attempts/${token}/score`, {
            headers: getAuthHeader()
        });
        if (!res.ok) {
            throw new Error("Score not available");
        }
        const score = await res.json();
        
        if (candidateInfo) {
            candidateInfo.textContent = `${score.candidateName || 'Candidate'} • Token: ${token.substring(0, 8)}...`;
        }

        renderScoreModalContent(score, token);

        // Set up print and download handlers
        const printBtn = document.getElementById("modalPrintBtn");
        if (printBtn) {
            printBtn.onclick = () => generatePrintableReportWindow(score, token);
        }

        const jsonBtn = document.getElementById("modalDownloadJsonBtn");
        if (jsonBtn) {
            jsonBtn.onclick = () => downloadScoreJson(score, token);
        }

    } catch (e) {
        modalBody.innerHTML = `
            <div class="p-8 text-center space-y-3">
                <span class="material-symbols-outlined text-4xl text-rose-500">error</span>
                <h4 class="text-base font-bold text-slate-800">Score Report Not Found</h4>
                <p class="text-xs text-slate-500">The assessment is still being processed or scores have not been generated.</p>
            </div>
        `;
    }

    const closeBtn = document.getElementById("closeScoreModalBtn");
    if (closeBtn) {
        closeBtn.onclick = () => modal.classList.add("hidden");
    }
};

function renderScoreModalContent(score, token) {
    const modalBody = document.getElementById("scoreModalBody");
    if (!modalBody) return;

    const readiness = getReadinessInfo(score.readinessBand);
    const isAr = document.documentElement.getAttribute("dir") === "rtl";

    let html = `
        <!-- Hero Summary -->
        <div class="bg-gradient-to-br from-slate-900 to-slate-800 text-white rounded-xl p-5 sm:p-6 shadow-sm space-y-4">
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 border-b border-slate-700/80 pb-4">
                <div>
                    <span class="text-xs font-semibold text-teal-400 uppercase tracking-widest">Composite Assessment Score</span>
                    <div class="flex items-baseline gap-3 mt-1">
                        <span class="text-4xl font-black text-white">${score.compositeScore}%</span>
                        <span class="text-xs font-bold px-2.5 py-0.5 rounded-full bg-teal-500/20 text-teal-300 border border-teal-500/30">
                            Percentile: P${score.percentile}
                        </span>
                    </div>
                    ${score.cappedPenaltyPct && score.cappedPenaltyPct > 0 ? `
                        <div class="mt-2 flex flex-wrap items-center gap-2 text-xs">
                            <span class="text-slate-300">Raw Composite: <strong class="text-white">${score.rawCompositeScore}%</strong></span>
                            <span class="px-2 py-0.5 rounded bg-rose-500/20 text-rose-300 border border-rose-500/30 font-semibold">Validity Deduction: -${score.cappedPenaltyPct}%</span>
                            <span class="text-teal-300">Final Adjusted: <strong class="text-white">${score.compositeScore}%</strong></span>
                        </div>
                    ` : ''}
                </div>
                <div class="sm:text-right">
                    <span class="text-xs font-semibold text-slate-400 uppercase tracking-wider block mb-1">Promotion Readiness</span>
                    <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold border ${readiness.badgeClass}">
                        <span class="material-symbols-outlined text-[14px]">verified</span>
                        <span>${isAr ? readiness.labelAr : readiness.labelEn}</span>
                    </span>
                </div>
            </div>
            <p class="text-xs text-slate-300 leading-relaxed font-medium">
                ${isAr ? readiness.descAr : readiness.descEn}
            </p>
        </div>

        <!-- 4 Batteries Overview Grid -->
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
            <div class="p-3.5 bg-slate-50 rounded-xl border border-slate-200">
                <span class="text-[10px] font-bold text-slate-400 uppercase">Personality (PQ10)</span>
                <div class="text-lg font-black text-slate-800 mt-0.5">${score.personalityScorePct}%</div>
                <span class="text-[10px] text-slate-500">140 items (Weight 28%)</span>
            </div>
            <div class="p-3.5 bg-slate-50 rounded-xl border border-slate-200">
                <span class="text-[10px] font-bold text-slate-400 uppercase">Judgment (SJT)</span>
                <div class="text-lg font-black text-slate-800 mt-0.5">${score.sjtScorePct}%</div>
                <span class="text-[10px] text-slate-500">16 scenarios (Weight 22%)</span>
            </div>
            <div class="p-3.5 bg-slate-50 rounded-xl border border-slate-200">
                <span class="text-[10px] font-bold text-slate-400 uppercase">Derailers</span>
                <div class="text-lg font-black text-slate-800 mt-0.5">${score.derailersEffectiveScorePct}%</div>
                <span class="text-[10px] text-slate-500">60 items (Weight 20%)</span>
            </div>
            <div class="p-3.5 bg-slate-50 rounded-xl border border-slate-200">
                <span class="text-[10px] font-bold text-slate-400 uppercase">Cognitive (GCAT)</span>
                <div class="text-lg font-black text-slate-800 mt-0.5">${score.cognitiveScorePct}%</div>
                <span class="text-[10px] text-slate-500">42 items (Weight 30%)</span>
            </div>
        </div>

        <!-- Validity & Response Style Panel -->
        <div class="p-4 rounded-xl border border-slate-200 bg-slate-50/80 space-y-3">
            <div class="flex items-center justify-between pb-2 border-b border-slate-200/80">
                <div class="flex items-center gap-2">
                    <span class="material-symbols-outlined text-[18px] text-teal-600">verified_user</span>
                    <span class="text-xs font-bold text-slate-800">${isAr ? 'مؤشرات صدق الاستجابة ونمط الإجابة' : 'Response Validity & Response Style'}</span>
                </div>
                <span class="text-[10px] text-slate-500">${isAr ? 'فحص جودة وموثوقية الإجابات' : 'Data Integrity & Validity Checks'}</span>
            </div>
            
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
                <!-- 1. Social Desirability -->
                <div class="p-3 rounded-lg border ${score.elevatedImpressionManagement ? 'bg-amber-50/90 border-amber-300' : 'bg-emerald-50/90 border-emerald-300'} flex items-start justify-between">
                    <div class="flex items-start gap-2.5">
                        <div class="w-7 h-7 rounded-md flex items-center justify-center shrink-0 mt-0.5 ${score.elevatedImpressionManagement ? 'bg-amber-200 text-amber-800' : 'bg-emerald-200 text-emerald-800'}">
                            <span class="material-symbols-outlined text-[17px]">${score.elevatedImpressionManagement ? 'warning' : 'thumb_up'}</span>
                        </div>
                        <div>
                            <div class="text-xs font-bold ${score.elevatedImpressionManagement ? 'text-amber-950' : 'text-emerald-950'}">
                                ${isAr ? (score.elevatedImpressionManagement ? 'ميل للتظاهر الاجتماعي' : 'نزاهة وتلقائية عالية') : (score.elevatedImpressionManagement ? 'Elevated Impression Mgmt' : 'Candid / Honest Profile')}
                            </div>
                            <div class="text-[11px] ${score.elevatedImpressionManagement ? 'text-amber-800' : 'text-emerald-800'} mt-0.5">
                                ${isAr ? (score.elevatedImpressionManagement ? 'مؤشر التظاهر: ' + (score.socialDesirabilityRiskPct || 0) + '% (حذر بالتفسير)' : 'مؤشر التظاهر: ' + (score.socialDesirabilityRiskPct || 0) + '% (استجابات واقعية)') : (score.elevatedImpressionManagement ? 'SD Risk: ' + (score.socialDesirabilityRiskPct || 0) + '% (Caution)' : 'SD Risk: ' + (score.socialDesirabilityRiskPct || 0) + '% (Normal)')}
                            </div>
                        </div>
                    </div>
                    <span class="px-2 py-0.5 text-[11px] font-bold rounded-md shrink-0 ${score.elevatedImpressionManagement ? 'bg-amber-200 text-amber-900 border border-amber-300' : 'bg-emerald-200 text-emerald-900 border border-emerald-300'}">
                        ${score.socialDesirabilityRiskPct || 0}%
                    </span>
                </div>

                <!-- 2. Central Tendency Index -->
                <div class="p-3 rounded-lg border ${score.elevatedCentralTendency ? 'bg-amber-50/90 border-amber-300' : 'bg-emerald-50/90 border-emerald-300'} flex items-start justify-between">
                    <div class="flex items-start gap-2.5">
                        <div class="w-7 h-7 rounded-md flex items-center justify-center shrink-0 mt-0.5 ${score.elevatedCentralTendency ? 'bg-amber-200 text-amber-800' : 'bg-emerald-200 text-emerald-800'}">
                            <span class="material-symbols-outlined text-[17px]">${score.elevatedCentralTendency ? 'flaky' : 'balance'}</span>
                        </div>
                        <div>
                            <div class="text-xs font-bold ${score.elevatedCentralTendency ? 'text-amber-950' : 'text-emerald-950'}">
                                ${isAr ? (score.elevatedCentralTendency ? 'نزعة مرتفعة للوسطية' : 'تنوع واستجابة متوازنة') : (score.elevatedCentralTendency ? 'Elevated Midpoint Bias' : 'Balanced Differentiation')}
                            </div>
                            <div class="text-[11px] ${score.elevatedCentralTendency ? 'text-amber-800' : 'text-emerald-800'} mt-0.5">
                                ${isAr ? (score.elevatedCentralTendency ? 'مؤشر الوسطية: ' + (score.centralTendencyRatePct || 0) + '% (تردد بالإجابات)' : 'مؤشر الوسطية: ' + (score.centralTendencyRatePct || 0) + '% (تمايز واضح)') : (score.elevatedCentralTendency ? 'Midpoint: ' + (score.centralTendencyRatePct || 0) + '% (High Neutral)' : 'Midpoint: ' + (score.centralTendencyRatePct || 0) + '% (Balanced)')}
                            </div>
                        </div>
                    </div>
                    <span class="px-2 py-0.5 text-[11px] font-bold rounded-md shrink-0 ${score.elevatedCentralTendency ? 'bg-amber-200 text-amber-900 border border-amber-300' : 'bg-emerald-200 text-emerald-900 border border-emerald-300'}">
                        ${score.centralTendencyRatePct || 0}%
                    </span>
                </div>
            </div>
        </div>

        <!-- Section 1: PQ10 8 Competency Traits -->
        <div class="space-y-3 pt-2">
            <div class="flex justify-between items-center pb-2 border-b border-slate-200">
                <h4 class="font-bold text-sm text-slate-800 flex items-center gap-2">
                    <span class="w-2.5 h-2.5 rounded-full bg-teal-500"></span>
                    <span>8 Competency Traits (PQ10)</span>
                </h4>
                <span class="text-[11px] text-slate-500">17 items per trait (Max 68.0 pts)</span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
    `;

    (score.traitScores || []).forEach(ts => {
        const maxPts = 68.0; // 17 items * 4 pts max
        html += `
            <div class="p-3 bg-white rounded-lg border border-slate-200/90 shadow-2xs space-y-1.5">
                <div class="flex justify-between items-start text-xs">
                    <span class="font-bold text-slate-800">${ts.nameAr || ts.traitCode}</span>
                    <span class="font-bold text-teal-700">${ts.scorePct}%</span>
                </div>
                <div class="flex justify-between items-center text-[10px] text-slate-400">
                    <span class="font-mono text-[9px]">${ts.traitCode}</span>
                    <span>Raw: ${ts.rawScore} / ${maxPts}</span>
                </div>
                <div class="w-full bg-slate-100 h-1.5 rounded-full overflow-hidden">
                    <div class="bg-teal-600 h-full rounded-full" style="width: ${Math.min(100, Math.max(0, ts.scorePct))}%;"></div>
                </div>
            </div>
        `;
    });

    // 9th Card in Grid: Social Desirability (مقياس التظاهر الاجتماعي / التظاهر الاجتماعي)
    html += `
            <div class="p-3 ${score.elevatedImpressionManagement ? 'bg-amber-50/90 border-amber-300' : 'bg-slate-50 border-slate-200'} rounded-lg border shadow-2xs space-y-1.5 col-span-1 sm:col-span-2">
                <div class="flex justify-between items-start text-xs">
                    <div class="flex items-center gap-2">
                        <span class="font-bold ${score.elevatedImpressionManagement ? 'text-amber-950' : 'text-slate-800'}">التظاهر الاجتماعي (مقياس التظاهر الاجتماعي)</span>
                        <span class="px-1.5 py-0.5 text-[9px] font-bold rounded ${score.elevatedImpressionManagement ? 'bg-amber-200 text-amber-900' : 'bg-slate-200 text-slate-700'}">مقياس الصدق • 4 أسئلة</span>
                    </div>
                    <span class="font-bold ${score.elevatedImpressionManagement ? 'text-amber-800' : 'text-emerald-700'}">${score.socialDesirabilityRiskPct || 0}% ${isAr ? 'مخاطرة' : 'Risk'}</span>
                </div>
                <div class="flex justify-between items-center text-[10px] text-slate-500">
                    <span class="font-mono text-[9px]">SOCIAL_DESIRABILITY</span>
                    <span>${isAr ? (score.elevatedImpressionManagement ? 'ميل مرتفع لإظهار صورة مثالية مبالغ فيها (حذر بالتفسير)' : 'استجابات واقعية وتلقائية تعكس الصدق والصراحة') : (score.elevatedImpressionManagement ? 'Elevated Impression Management (Interpret with caution)' : 'Candid and honest responses')}</span>
                </div>
                <div class="w-full bg-slate-200/80 h-1.5 rounded-full overflow-hidden">
                    <div class="${score.elevatedImpressionManagement ? 'bg-amber-500' : 'bg-emerald-500'} h-full rounded-full" style="width: ${Math.min(100, Math.max(0, score.socialDesirabilityRiskPct || 0))}%;"></div>
                </div>
            </div>
    `;

    html += `
            </div>
        </div>

        <!-- Section 2: 6 Derailer Risk Categories -->
        <div class="space-y-3 pt-2">
            <div class="flex justify-between items-center pb-2 border-b border-slate-200">
                <h4 class="font-bold text-sm text-slate-800 flex items-center gap-2">
                    <span class="w-2.5 h-2.5 rounded-full bg-amber-500"></span>
                    <span>6 Derailer Risk Categories</span>
                </h4>
                <span class="text-[11px] text-slate-500">10 items per category &bull; Max 40.0 pts</span>
            </div>
            <div class="grid grid-cols-2 sm:grid-cols-3 gap-2.5">
    `;

    (score.derailerCategoryScores || []).forEach(ds => {
        html += `
            <div class="p-3 bg-white rounded-lg border border-slate-200/90 shadow-2xs space-y-1.5">
                <div class="flex justify-between items-center text-xs">
                    <span class="font-bold text-slate-800">${ds.nameAr || ds.categoryCode || 'Category'}</span>
                    <span class="font-bold text-amber-700">${ds.scorePct}%</span>
                </div>
                <div class="text-[10px] text-slate-400">Raw: ${ds.rawScore} / 40.0</div>
                <div class="w-full bg-slate-100 h-1.5 rounded-full overflow-hidden">
                    <div class="bg-amber-500 h-full rounded-full" style="width: ${Math.min(100, Math.max(0, ds.scorePct))}%;"></div>
                </div>
            </div>
        `;
    });

    html += `
            </div>
        </div>

        <!-- Section 3: 3 GCAT Cognitive Subtests -->
        <div class="space-y-3 pt-2">
            <div class="flex justify-between items-center pb-2 border-b border-slate-200">
                <h4 class="font-bold text-sm text-slate-800 flex items-center gap-2">
                    <span class="w-2.5 h-2.5 rounded-full bg-cyan-500"></span>
                    <span>3 Cognitive Aptitude Subtests (GCAT)</span>
                </h4>
                <span class="text-[11px] text-slate-500">14 questions per subtest</span>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-3 gap-2.5">
    `;

    (score.gcatSubtestScores || []).forEach(gs => {
        html += `
            <div class="p-3 bg-white rounded-lg border border-slate-200/90 shadow-2xs space-y-1.5">
                <div class="flex justify-between items-center text-xs">
                    <span class="font-bold text-slate-800">${gs.subtest}</span>
                    <span class="font-bold text-cyan-700">${gs.scorePct}%</span>
                </div>
                <div class="text-[10px] text-slate-400">Correct: ${gs.correctCount} / ${gs.totalCount}</div>
                <div class="w-full bg-slate-100 h-1.5 rounded-full overflow-hidden">
                    <div class="bg-cyan-600 h-full rounded-full" style="width: ${Math.min(100, Math.max(0, gs.scorePct))}%;"></div>
                </div>
            </div>
        `;
    });

    html += `
            </div>
        </div>
    `;

    modalBody.innerHTML = html;
    applyCurrentTranslation();
}

window.downloadReport = function(event, token) {
    const btn = event ? event.currentTarget : null;
    const originalHtml = btn ? btn.innerHTML : "";
    if (btn) {
        btn.innerHTML = `<span class="material-symbols-outlined text-[14px] animate-spin">refresh</span><span>Downloading...</span>`;
        setTimeout(() => {
            if (btn) btn.innerHTML = originalHtml;
        }, 3500);
    }

    // Direct binary stream endpoint - fully compatible with IDM and standard browsers
    const downloadUrl = `${API_BASE}/api/assessments/${encodeURIComponent(token)}/report/pdf`;
    
    const a = document.createElement("a");
    a.href = downloadUrl;
    a.download = `Leadership_Assessment_Report_${token.substring(0, 8)}.pdf`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
};

function downloadScoreJson(score, token) {
    const jsonStr = JSON.stringify(score, null, 2);
    const blob = new Blob([jsonStr], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `Executive_Assessment_Report_${token.substring(0, 8)}.json`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}

function generatePrintableReportWindow(score, token) {
    const readiness = getReadinessInfo(score.readinessBand);
    const candidateName = score.candidateName || currentUser.name || "Candidate";
    const dateStr = new Date().toLocaleDateString("en-US", { year: "numeric", month: "long", day: "numeric" });

    let traitRows = (score.traitScores || []).map(t => {
        const maxPts = 68.0; // 17 items * 4 pts max
        return `<tr>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-weight: bold;">${t.nameAr || t.traitCode}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-family: monospace; font-size: 11px;">${t.traitCode}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: center;">${t.rawScore} / ${maxPts}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: bold; color: #0f766e;">${t.scorePct}%</td>
        </tr>`;
    }).join("");

    const isSdElevatedRow = !!score.elevatedImpressionManagement;
    const sdRiskVal = score.socialDesirabilityRiskPct || 0;
    traitRows += `
        <tr style="background: ${isSdElevatedRow ? '#fef3c7' : '#f8fafc'}; font-style: italic;">
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-weight: bold; color: ${isSdElevatedRow ? '#92400e' : '#0f766e'};">
                مقياس التظاهر الاجتماعي (التظاهر الاجتماعي)
            </td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-family: monospace; font-size: 11px;">SOCIAL_DESIRABILITY</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: center;">4 items (مقياس صدق)</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: bold; color: ${isSdElevatedRow ? '#b45309' : '#059669'};">
                ${sdRiskVal}% ${isSdElevatedRow ? '(مرتفع)' : '(طبيعي)'}
            </td>
        </tr>
    `;

    let derailerRows = (score.derailerCategoryScores || []).map(d => {
        return `<tr>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-weight: bold;">${d.nameAr || d.categoryCode}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: center;">${d.rawScore} / 40.0</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: bold; color: #b45309;">${d.scorePct}%</td>
        </tr>`;
    }).join("");

    let gcatRows = (score.gcatSubtestScores || []).map(g => {
        return `<tr>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; font-weight: bold;">${g.subtest}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: center;">${g.correctCount} / ${g.totalCount}</td>
            <td style="padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: right; font-weight: bold; color: #0891b2;">${g.scorePct}%</td>
        </tr>`;
    }).join("");

    const sdRisk = score.socialDesirabilityRiskPct || 0;
    const isSdElevated = !!score.elevatedImpressionManagement;
    const ctRate = score.centralTendencyRatePct || 0;
    const isCtElevated = !!score.elevatedCentralTendency;
    const hasAnyValidityWarning = isSdElevated || isCtElevated;

    const validityBanner = `
        <div style="background: ${hasAnyValidityWarning ? '#fffbeb' : '#f0fdf4'}; border: 1px solid ${hasAnyValidityWarning ? '#fde68a' : '#bbf7d0'}; border-radius: 10px; padding: 14px 18px; margin-bottom: 25px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; border-bottom: 1px solid ${hasAnyValidityWarning ? '#fef3c7' : '#dcfce7'}; padding-bottom: 8px;">
                <div style="font-size: 13px; font-weight: bold; color: ${hasAnyValidityWarning ? '#92400e' : '#166534'};">
                    ${hasAnyValidityWarning ? '⚠️ Response Validity & Quality Indicators (Attention Required)' : '✅ Response Validity & Quality Indicators (High Reliability Profile)'}
                </div>
                <div style="font-size: 11px; color: #64748b;">
                    Substantive Items: 196 | Validity Scales: 2
                </div>
            </div>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                <!-- Social Desirability Box -->
                <div style="background: #fff; border: 1px solid ${isSdElevated ? '#fcd34d' : '#e2e8f0'}; border-radius: 6px; padding: 10px 12px; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <div style="font-size: 12px; font-weight: bold; color: ${isSdElevated ? '#92400e' : '#1e293b'};">
                            ${isSdElevated ? 'Social Desirability: Elevated' : 'Social Desirability: Normal'}
                        </div>
                        <div style="font-size: 10px; color: ${isSdElevated ? '#b45309' : '#64748b'}; margin-top: 2px;">
                            ${isSdElevated ? 'Endorsed unrealistic virtues — interpret self-report with caution.' : 'Candid and realistic self-description.'}
                        </div>
                    </div>
                    <div style="font-size: 12px; font-weight: bold; padding: 3px 10px; border-radius: 9999px; background: ${isSdElevated ? '#fef3c7' : '#ecfdf5'}; color: ${isSdElevated ? '#92400e' : '#166534'}; border: 1px solid ${isSdElevated ? '#fde68a' : '#bbf7d0'};">
                        ${sdRisk}%
                    </div>
                </div>

                <!-- Central Tendency Box -->
                <div style="background: #fff; border: 1px solid ${isCtElevated ? '#fcd34d' : '#e2e8f0'}; border-radius: 6px; padding: 10px 12px; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <div style="font-size: 12px; font-weight: bold; color: ${isCtElevated ? '#92400e' : '#1e293b'};">
                            ${isCtElevated ? 'Central Tendency: Elevated' : 'Central Tendency: Balanced'}
                        </div>
                        <div style="font-size: 10px; color: ${isCtElevated ? '#b45309' : '#64748b'}; margin-top: 2px;">
                            ${isCtElevated ? 'High rate of midpoint answers across substantive items.' : 'Well-differentiated response distribution.'}
                        </div>
                    </div>
                    <div style="font-size: 12px; font-weight: bold; padding: 3px 10px; border-radius: 9999px; background: ${isCtElevated ? '#fef3c7' : '#ecfdf5'}; color: ${isCtElevated ? '#92400e' : '#166534'}; border: 1px solid ${isCtElevated ? '#fde68a' : '#bbf7d0'};">
                        ${ctRate}%
                    </div>
                </div>
            </div>
        </div>
    `;

    const reportHtml = `
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Executive Assessment Report - ${candidateName}</title>
    <style>
        body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif; color: #1e293b; margin: 0; padding: 30px; line-height: 1.5; background: #fff; }
        .header { border-bottom: 2px solid #00685f; padding-bottom: 15px; margin-bottom: 25px; display: flex; justify-content: space-between; align-items: flex-end; }
        .title { font-size: 22px; font-weight: bold; color: #00685f; }
        .subtitle { font-size: 12px; color: #64748b; margin-top: 4px; }
        .hero { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px; margin-bottom: 25px; }
        .score-val { font-size: 36px; font-weight: 800; color: #00685f; }
        .badge { display: inline-block; padding: 4px 12px; border-radius: 9999px; font-size: 12px; font-weight: bold; }
        .battery-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 25px; }
        .battery-card { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px; text-align: center; }
        .battery-score { font-size: 20px; font-weight: bold; color: #1e293b; margin-top: 4px; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 25px; font-size: 12px; }
        th { background: #f1f5f9; padding: 8px; text-align: left; font-weight: bold; color: #475569; border-bottom: 2px solid #cbd5e1; }
        h3 { font-size: 15px; color: #0f172a; margin-top: 20px; margin-bottom: 8px; border-bottom: 1px solid #e2e8f0; padding-bottom: 6px; }
        @media print { body { padding: 0; } .no-print { display: none; } }
    </style>
</head>
<body>
    <div class="no-print" style="margin-bottom: 20px; display: flex; justify-content: flex-end; gap: 10px;">
        <button onclick="window.print()" style="background: #00685f; color: white; border: none; padding: 8px 18px; border-radius: 6px; font-weight: bold; cursor: pointer;">Print / Save as PDF</button>
        <button onclick="window.close()" style="background: #e2e8f0; color: #334155; border: none; padding: 8px 18px; border-radius: 6px; font-weight: bold; cursor: pointer;">Close</button>
    </div>

    <div class="header">
        <div>
            <div class="title">Executive Leadership Assessment Dossier</div>
            <div class="subtitle">Candidate: <strong>${candidateName}</strong> &bull; Evaluation Date: ${dateStr} &bull; Token: ${token}</div>
        </div>
        <div style="text-align: right; font-size: 11px; color: #64748b;">Psychometric Evaluation Platform</div>
    </div>

    <div class="hero">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <div style="font-size: 11px; text-transform: uppercase; font-weight: bold; color: #64748b;">Overall Composite Score</div>
                <div class="score-val">${score.compositeScore}% <span style="font-size: 13px; font-weight: normal; color: #64748b;">(Percentile: P${score.percentile})</span></div>
                ${score.cappedPenaltyPct && score.cappedPenaltyPct > 0 ? `
                    <div style="margin-top: 5px; font-size: 11px; color: #b45309; font-weight: bold;">
                        ⚠️ Validity Adjustment: -${score.cappedPenaltyPct}% (Raw Composite: ${score.rawCompositeScore}% &bull; Final: ${score.compositeScore}%)
                    </div>
                ` : ''}
            </div>
            <div style="text-align: right;">
                <div style="font-size: 11px; text-transform: uppercase; font-weight: bold; color: #64748b; margin-bottom: 4px;">Promotion Readiness</div>
                <span class="badge" style="background: #dcfce7; color: #166534; border: 1px solid #86efac;">${readiness.labelEn} / ${readiness.labelAr}</span>
            </div>
        </div>
        <div style="margin-top: 12px; font-size: 12px; color: #475569;">${readiness.descEn}</div>
    </div>

    ${validityBanner}

    <div class="battery-grid">
        <div class="battery-card">
            <div style="font-size: 10px; font-weight: bold; color: #64748b; text-transform: uppercase;">01 &bull; Personality (PQ10)</div>
            <div class="battery-score">${score.personalityScorePct}%</div>
            <div style="font-size: 10px; color: #94a3b8;">140 Items (28%)</div>
        </div>
        <div class="battery-card">
            <div style="font-size: 10px; font-weight: bold; color: #64748b; text-transform: uppercase;">02 &bull; Judgment (SJT)</div>
            <div class="battery-score">${score.sjtScorePct}%</div>
            <div style="font-size: 10px; color: #94a3b8;">16 Scenarios (22%)</div>
        </div>
        <div class="battery-card">
            <div style="font-size: 10px; font-weight: bold; color: #64748b; text-transform: uppercase;">03 &bull; Derailers</div>
            <div class="battery-score">${score.derailersEffectiveScorePct}%</div>
            <div style="font-size: 10px; color: #94a3b8;">60 Items (20%)</div>
        </div>
        <div class="battery-card">
            <div style="font-size: 10px; font-weight: bold; color: #64748b; text-transform: uppercase;">04 &bull; Cognitive (GCAT)</div>
            <div class="battery-score">${score.cognitiveScorePct}%</div>
            <div style="font-size: 10px; color: #94a3b8;">42 Items (30%)</div>
        </div>
    </div>

    <h3>1. Personality Dimensions (PQ10 8 Core Competency Traits)</h3>
    <table>
        <thead>
            <tr>
                <th>Trait Name</th>
                <th>Trait Code</th>
                <th style="text-align: center;">Raw Points</th>
                <th style="text-align: right;">Score %</th>
            </tr>
        </thead>
        <tbody>
            ${traitRows}
        </tbody>
    </table>

    <h3>2. Behavioral Risk Factors (6 Derailer Categories)</h3>
    <table>
        <thead>
            <tr>
                <th>Category</th>
                <th style="text-align: center;">Raw Points (Max 40)</th>
                <th style="text-align: right;">Score %</th>
            </tr>
        </thead>
        <tbody>
            ${derailerRows}
        </tbody>
    </table>

    <h3>3. Cognitive Aptitude Breakdown (GCAT Subtests)</h3>
    <table>
        <thead>
            <tr>
                <th>Subtest Dimension</th>
                <th style="text-align: center;">Questions Correct (Out of 14)</th>
                <th style="text-align: right;">Accuracy %</th>
            </tr>
        </thead>
        <tbody>
            ${gcatRows}
        </tbody>
    </table>

    <div style="margin-top: 30px; padding-top: 15px; border-top: 1px solid #e2e8f0; font-size: 10px; color: #94a3b8; text-align: center;">
        Psychometric Evaluation System &bull; Confidential Executive Document &bull; Generated on ${dateStr}
    </div>
</body>
</html>`;

    const printWindow = window.open("", "_blank");
    if (printWindow) {
        printWindow.document.write(reportHtml);
        printWindow.document.close();
    } else {
        // Fallback: download as HTML file
        const blob = new Blob([reportHtml], { type: "text/html" });
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `Executive_Assessment_Report_${token.substring(0, 8)}.html`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    }
}


function updateTestSidebar(attempt) {
    const navList = document.getElementById("batteryNavList");
    const progressText = document.getElementById("sidebarProgressText");
    if (!navList || !progressText) return;

    const isArabic = document.documentElement.getAttribute("dir") === "rtl";
    const currentIndex = (attempt && typeof attempt.currentBatteryIndex === 'number') ? attempt.currentBatteryIndex : 0;

    progressText.textContent = isArabic ? `الجزء ${currentIndex + 1} من 4` : `Part ${currentIndex + 1} of 4`;

    const batteryTitles = [
        { en: "Part 1: Personality (PQ10)", ar: "اختبار الشخصية" },
        { en: "Part 2: SJT Ranking", ar: "اختبار الحكم على المواقف" },
        { en: "Part 3: Derailers & Drivers", ar: "اختبار السلوكيات المعطلة" },
        { en: "Part 4: Cognitive Abilities", ar: "اختبار القدرات المعرفية" }
    ];

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

        const titleText = isArabic ? batteryTitles[i].ar : batteryTitles[i].en;

        html += `
            <div class="flex items-center gap-3 p-2.5 rounded-lg border ${borderClass} transition-colors">
                <div class="w-8 h-8 rounded-full flex items-center justify-center shrink-0 ${colorClass}">
                    <span class="material-symbols-outlined text-[18px]">${icon}</span>
                </div>
                <span class="text-sm ${textClass}">${titleText}</span>
            </div>
        `;
    }
    navList.innerHTML = html;
}

Object.assign(window, {
    openPreBatteryInstructions,
    startActiveBatterySession,
    renderCurrentQuestion,
    loadAssessmentState,
    loadAssessmentHistory
});
