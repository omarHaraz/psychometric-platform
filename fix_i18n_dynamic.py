import re

js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

helper = """
function applyCurrentTranslation() {
    if (document.documentElement.getAttribute("dir") === "rtl") {
        applyTranslation("ar");
    }
}
"""

if "function applyCurrentTranslation" not in js:
    js = js.replace("window.applyTranslation = applyTranslation;", "window.applyTranslation = applyTranslation;\n" + helper)
    
    # Inject at end of renderTestSidebar
    js = re.sub(r"(navList\.innerHTML = html;\n})", r"\1\n    applyCurrentTranslation();", js)
    
    # Inject at end of updateBatteryCardStates
    js = re.sub(r"(showView\(\"view-pending-portal\"\);\n\s*updateBatteryCardStates\(attempt\);\n\s*})", r"showView(\"view-pending-portal\");\n    updateBatteryCardStates(attempt);\n    applyCurrentTranslation();\n}", js)
    
    # Inject at end of renderHistoryList
    js = re.sub(r"(container\.innerHTML = html;\n})", r"container.innerHTML = html;\n    applyCurrentTranslation();\n}", js)
    
    # Inject at end of openPreBatteryInstructions
    js = re.sub(r"(showView\(\"view-instructions\"\);\n})", r"showView(\"view-instructions\");\n    applyCurrentTranslation();\n}", js)
    
    # Inject at end of renderCurrentQuestion
    js = re.sub(r"(renderLikertQuestion\(item, container\);\n\s*}\s*else\s*if.*?{\n\s*renderSjtRankingQuestion.*?}\s*else\s*if.*?{\n\s*renderGcatMcqQuestion.*?\n\s*}\n})", r"\1\n    applyCurrentTranslation();", js)
    
    with open(js_path, "w", encoding="utf-8") as f:
        f.write(js)
    print("Injected dynamic i18n triggers")
else:
    print("Already injected")