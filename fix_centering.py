import re
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old = """    const rightSpacer = document.getElementById("rightSpacer");
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

new = """    const rightSpacer = document.getElementById("rightSpacer");
    const mainContentArea = document.getElementById("mainContentArea");
    if (viewId === "view-instructions" || viewId === "view-active-test") {
        if (dashboardSidebar) dashboardSidebar.classList.add("hidden");
        if (testSidebar) testSidebar.classList.remove("hidden");
        if (historySection) historySection.classList.add("hidden");
        if (rightSpacer) rightSpacer.classList.add("hidden");
        if (mainContentArea) { mainContentArea.classList.add("flex-grow"); mainContentArea.classList.remove("flex-1"); }
    } else if (viewId === "view-complete" || viewId === "view-empty") {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) rightSpacer.classList.remove("hidden");
        // Make mainContentArea NOT flex-grow so rightSpacer can balance the layout
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
    }"""

js = js.replace(old, new)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)