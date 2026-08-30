import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# Add the right spacer before </main>
if "rightSpacer" not in html:
    html = html.replace("    </main>", "        <!-- RIGHT SPACER (To center the main content area) -->\n        <aside id=\"rightSpacer\" class=\"hidden md:block md:w-1/4 shrink-0\"></aside>\n    </main>")

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)

js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old_logic = """    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
    } else {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (historySection) {"""

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
        if (historySection) {"""

js = js.replace(old_logic, new_logic)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)