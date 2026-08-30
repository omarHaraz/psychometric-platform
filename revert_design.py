import re
# Revert JS
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old_logic = """    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
    } else if (viewId === "view-complete" || viewId === "view-empty") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.add("hidden");
        if (historySection) historySection.classList.remove("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (historySection) {
            if (viewId === "view-pending-portal") {
                historySection.classList.remove("hidden");
            } else {
                historySection.classList.add("hidden");
            }
        }
    }"""

new_logic = """    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (historySection) {
            if (viewId === "view-pending-portal" || viewId === "view-complete" || viewId === "view-empty") {
                historySection.classList.remove("hidden");
            } else {
                historySection.classList.add("hidden");
            }
        }
    }"""

js = js.replace(old_logic, new_logic)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)

# Revert HTML
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

old_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4 max-w-4xl mx-auto w-full\">"
new_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4\">"

html = html.replace(old_history, new_history)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)