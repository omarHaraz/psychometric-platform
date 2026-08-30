js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

# renderTestSidebar
if "applyCurrentTranslation" not in js.split("function renderTestSidebar")[1].split("}")[0]:
    js = js.replace("navList.innerHTML = html;\n}", "navList.innerHTML = html;\n    applyCurrentTranslation();\n}")

# renderHistoryList
if "applyCurrentTranslation" not in js.split("function renderHistoryList")[1].split("}")[-1]:
    # Need to be careful because there's a loop inside renderHistoryList. Let's find "container.innerHTML = html;\n}"
    js = js.replace("container.innerHTML = html;\n}", "container.innerHTML = html;\n    applyCurrentTranslation();\n}")

# renderCurrentQuestion
if "applyCurrentTranslation" not in js.split("function renderCurrentQuestion")[1].split("}")[0]: # Wait, there are many closing braces
    # Instead, find the end of renderCurrentQuestion which has:
    # } else if (batteryType === "GCAT") {
    #     renderGcatMcqQuestion(item, container);
    # }
    js = js.replace("renderGcatMcqQuestion(item, container);\n    }\n}", "renderGcatMcqQuestion(item, container);\n    }\n    applyCurrentTranslation();\n}")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)