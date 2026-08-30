import re

js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

i18n_code = """
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
"""

if "function applyTranslation" not in js:
    # Insert at top
    js = i18n_code + "\n\n" + js
    
    # Update rtlToggle listener
    old_listener = """    const rtlToggle = document.getElementById("rtlToggle");
    if (rtlToggle) {
        rtlToggle.addEventListener("click", () => {
            const htmlEl = document.documentElement;
            const currentDir = htmlEl.getAttribute("dir") || "ltr";
            const warningBlock = document.getElementById("warningBlock");
            if (currentDir === "ltr") {
                htmlEl.setAttribute("dir", "rtl");
                rtlToggle.textContent = "Switch to English";
                if (warningBlock) {
                    warningBlock.classList.remove("border-l-4", "border-l-[#131b2e]", "rounded-r");
                    warningBlock.classList.add("border-r-4", "border-r-[#131b2e]", "rounded-l");
                }
            } else {
                htmlEl.setAttribute("dir", "ltr");
                rtlToggle.textContent = "Switch to Arabic";
                if (warningBlock) {
                    warningBlock.classList.remove("border-r-4", "border-r-[#131b2e]", "rounded-l");
                    warningBlock.classList.add("border-l-4", "border-l-[#131b2e]", "rounded-r");
                }
            }
        });
    }"""
    
    new_listener = """    const rtlToggle = document.getElementById("rtlToggle");
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
    }"""
    
    js = js.replace(old_listener, new_listener)
    
    with open(js_path, "w", encoding="utf-8") as f:
        f.write(js)
    print("Injected i18n into candidate-app.js")
else:
    print("Already injected")
