import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# Revert HTML
html = re.sub(r'<!-- RIGHT SIDEBARS \(Contextual\) -->.*?</aside>\s*</aside>\s*</aside>', '<!-- RIGHT SPACER (To center the main content area) -->\n        <aside id="rightSpacer" class="hidden md:block md:w-1/4 shrink-0"></aside>', html, flags=re.DOTALL)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)

# Revert JS
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old_logic = """    const rightSpacerEmpty = document.getElementById("rightSpacerEmpty");
    const rightSidebarPending = document.getElementById("rightSidebarPending");
    const rightSidebarComplete = document.getElementById("rightSidebarComplete");
    
    // Hide all right sidebars by default
    if (rightSpacerEmpty) rightSpacerEmpty.classList.add("hidden");
    if (rightSidebarPending) rightSidebarPending.classList.add("hidden");
    if (rightSidebarComplete) rightSidebarComplete.classList.add("hidden");

    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        
        // Show specific right sidebar based on view
        if (viewId === "view-empty" && rightSpacerEmpty) {
            rightSpacerEmpty.classList.remove("hidden");
        } else if (viewId === "view-pending-portal" && rightSidebarPending) {
            rightSidebarPending.classList.remove("hidden");
        } else if (viewId === "view-complete" && rightSidebarComplete) {
            rightSidebarComplete.classList.remove("hidden");
        }
        
        if (historySection) {
            if (viewId === "view-pending-portal" || viewId === "view-complete" || viewId === "view-empty") {
                historySection.classList.remove("hidden");
            } else {
                historySection.classList.add("hidden");
            }
        }
    }"""

new_logic = """    const rightSpacer = document.getElementById("rightSpacer");
    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
        if (rightSpacer) rightSpacer.classList.add("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) rightSpacer.classList.remove("hidden");
        if (historySection) {
            if (viewId === "view-pending-portal" || viewId === "view-complete" || viewId === "view-empty") {
                historySection.classList.remove("hidden");
            } else {
                historySection.classList.add("hidden");
            }
        }
    }"""

js = js.replace(old_logic, new_logic)

new_i18n = """
    "Preparation Checklist": "قائمة التحضير",
    "Ensure you are in a quiet, distraction-free environment.": "تأكد من وجودك في بيئة هادئة وخالية من المشتتات.",
    "Allocate enough uninterrupted time (approx. 90 minutes).": "خصص وقتًا كافيًا دون انقطاع (حوالي 90 دقيقة).",
    "Verify you have a stable and reliable internet connection.": "تحقق من وجود اتصال ثابت وموثوق بالإنترنت.",
    "Privacy & Security": "الخصوصية والأمان",
    "Your responses and personal data are strictly confidential and protected by advanced encryption. Information is solely used by the Arab Experts Institute for professional evaluation purposes.": "استجاباتك وبياناتك الشخصية سرية تمامًا ومحمية بتشفير متقدم. يتم استخدام المعلومات حصريًا من قبل معهد الخبراء العرب لأغراض التقييم المهني.",
    "Secure Connection": "اتصال آمن",
    "What Happens Next?": "ماذا بعد؟",
    "Your results have been automatically forwarded to the evaluation committee. If your profile matches the role requirements, the recruitment team will reach out to you with the next steps.": "تم إرسال نتائجك تلقائيًا إلى لجنة التقييم. إذا كان ملفك الشخصي يطابق متطلبات الوظيفة، فسيتواصل معك فريق التوظيف بالخطوات التالية.",
    "About Your Report": "حول تقريرك",
    "The Executive Dossier provides a comprehensive overview of your leadership style, cognitive abilities, and workplace behaviors based on your psychometric responses.": "يوفر التقرير التنفيذي نظرة شاملة على أسلوبك القيادي وقدراتك المعرفية وسلوكياتك في مكان العمل بناءً على إجاباتك النفسية.","""

js = js.replace(new_i18n, "")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)