import re
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

new_i18n = """
    "Preparation Checklist": "قائمة التحضير",
    "Ensure you are in a quiet, distraction-free environment.": "تأكد من وجودك في بيئة هادئة وخالية من المشتتات.",
    "Allocate enough uninterrupted time (approx. 90 minutes).": "خصص وقتًا كافيًا دون انقطاع (حوالي 90 دقيقة).",
    "Verify you have a stable and reliable internet connection.": "تحقق من وجود اتصال ثابت وموثوق بالإنترنت.",
    "Privacy & Security": "الخصوصية والأمان",
    "Your responses and personal data are strictly confidential and protected by military-grade encryption. Information is solely used by the Arab Experts Institute for professional evaluation purposes.": "استجاباتك وبياناتك الشخصية سرية تمامًا ومحمية بتشفير عالي الجودة. يتم استخدام المعلومات حصريًا من قبل معهد الخبراء العرب لأغراض التقييم المهني.",
    "Secure Connection": "اتصال آمن","""

js = js.replace(new_i18n, "")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)