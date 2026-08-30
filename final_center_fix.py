import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# Fix the rightSpacer - remove "hidden" from the default class since JS controls visibility
html = html.replace(
    '<aside id="rightSpacer" class="hidden md:block md:w-1/4 shrink-0"></aside>',
    '<aside id="rightSpacer" class="md:w-1/4 shrink-0 hidden"></aside>'
)

# Also fix the view-complete to remove max-w-lg constraint so it fills the column
html = html.replace(
    'id="view-complete" class="hidden max-w-lg mx-auto w-full bg-white',
    'id="view-complete" class="hidden w-full bg-white'
)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)

js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

# In the view-complete branch, show the rightSpacer
old = """    } else if (viewId === "view-complete" || viewId === "view-empty") {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) rightSpacer.classList.remove("hidden");
        // Make mainContentArea NOT flex-grow so rightSpacer can balance the layout
        if (mainContentArea) { mainContentArea.classList.remove("flex-grow"); mainContentArea.classList.add("flex-1"); }
        if (historySection) historySection.classList.remove("hidden");"""

new = """    } else if (viewId === "view-complete" || viewId === "view-empty") {
        if (testSidebar) testSidebar.classList.add("hidden");
        if (dashboardSidebar) dashboardSidebar.classList.remove("hidden");
        if (rightSpacer) { rightSpacer.classList.remove("hidden"); rightSpacer.style.display = "block"; }
        // Make mainContentArea use flex-1 so all 3 columns share space equally
        if (mainContentArea) { mainContentArea.classList.remove("flex-grow"); mainContentArea.classList.add("flex-1"); }
        if (historySection) historySection.classList.remove("hidden");"""

js = js.replace(old, new)

# In all other branches, hide the spacer
old2 = """        if (rightSpacer) rightSpacer.classList.add("hidden");
        if (mainContentArea) { mainContentArea.classList.add("flex-grow"); mainContentArea.classList.remove("flex-1"); }
    }"""

new2 = """        if (rightSpacer) { rightSpacer.classList.add("hidden"); rightSpacer.style.display = ""; }
        if (mainContentArea) { mainContentArea.classList.add("flex-grow"); mainContentArea.classList.remove("flex-1"); }
    }"""

js = js.replace(old2, new2)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)