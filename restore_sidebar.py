js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

sidebar_code = """
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
"""

if "function updateTestSidebar" not in js:
    js = js + "\n" + sidebar_code
    
    # Hook it up to openPreBatteryInstructions and fetchAndRenderBatteryItems
    if "updateTestSidebar(currentAttempt);" not in js.split("function openPreBatteryInstructions(")[1]:
        js = js.replace("showView(\"view-instructions\");\n    applyCurrentTranslation();\n}", "updateTestSidebar(currentAttempt);\n    showView(\"view-instructions\");\n    applyCurrentTranslation();\n}")
    
    if "updateTestSidebar(currentAttempt);" not in js.split("function fetchAndRenderBatteryItems(")[1]:
        js = js.replace("showView(\"view-active-test\");\n        renderCurrentQuestion();", "updateTestSidebar(currentAttempt);\n        showView(\"view-active-test\");\n        renderCurrentQuestion();")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)