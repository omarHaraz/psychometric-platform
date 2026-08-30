import re
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

# 1. Add loadAssessmentHistory to DOMContentLoaded
js = js.replace("loadAssessmentState();\n});", "loadAssessmentState();\n    loadAssessmentHistory();\n});")

# 2. Update showView
old_showView = """function showView(viewId) {
    const views = [
        "view-loading",
        "view-empty",
        "view-pending-portal",
        "view-instructions",
        "view-active-test",
        "view-complete"
    ];
    views.forEach(v => {
        const el = document.getElementById(v);
        if (el) el.classList.add("hidden");
    });

    const activeEl = document.getElementById(viewId);
    if (activeEl) activeEl.classList.remove("hidden");
}"""
new_showView = """function showView(viewId) {
    const views = [
        "view-loading",
        "view-empty",
        "view-pending-portal",
        "view-instructions",
        "view-active-test",
        "view-complete"
    ];
    views.forEach(v => {
        const el = document.getElementById(v);
        if (el) el.classList.add("hidden");
    });

    const activeEl = document.getElementById(viewId);
    if (activeEl) activeEl.classList.remove("hidden");

    // Toggle Sidebars
    const dashboardSidebar = document.getElementById("dashboardSidebar");
    const testSidebar = document.getElementById("testSidebar");
    const historySection = document.getElementById("historySection");

    if (viewId === "view-instructions" || viewId === "view-active-test") {
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
    }
}"""
js = js.replace(old_showView, new_showView)

# 3. Add loadAssessmentHistory and renderHistoryList
history_code = """
async function loadAssessmentHistory() {
    try {
        const res = await fetch(`${API_BASE}/api/attempts/me/history`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            const history = await res.json();
            renderHistoryList(history);
        }
    } catch (e) {
        console.error("Failed to load history", e);
    }
}

function renderHistoryList(history) {
    const container = document.getElementById("historyListContainer");
    if (!container) return;
    
    if (!history || history.length === 0) {
        container.innerHTML = `<p class="text-sm text-slate-500 italic p-4 text-center">No past assessments found.</p>`;
        applyCurrentTranslation();
        return;
    }
    
    let html = "";
    history.forEach(attempt => {
        let isCompleted = (attempt.state === "SCORED" || attempt.state === "ALL_SUBMITTED");
        let dateObj = attempt.submitTime ? new Date(attempt.submitTime) : new Date(attempt.createdAt);
        let prefix = isCompleted ? "Completed" : "Started";
        
        const dateStr = dateObj.toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" });
        const timeStr = dateObj.toLocaleTimeString("en-US", { hour: "numeric", minute: "2-digit" });
        
        let badgeHtml = "";
        if (isCompleted) {
            badgeHtml = `
                <button onclick="downloadReport(event, '${attempt.attemptToken}')" class="bg-emerald-50 hover:bg-emerald-100 text-emerald-700 border border-emerald-200 px-3 py-1.5 rounded-full text-[11px] font-bold flex items-center gap-1.5 transition-colors">
                    <span class="material-symbols-outlined text-[14px]">download</span>
                    <span>Download Report</span>
                </button>
            `;
        } else {
            badgeHtml = `
                <span class="bg-amber-50 text-amber-600 border border-amber-200 px-2.5 py-1 rounded-full text-[11px] font-semibold flex items-center gap-1">
                    <span class="material-symbols-outlined text-[14px]">pending_actions</span>
                    <span>In Progress</span>
                </span>
            `;
        }
        
        html += `
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2 p-3 bg-slate-50/70 hover:bg-slate-50 rounded-lg border border-slate-100 transition-colors">
                <div>
                    <h3 class="text-sm font-bold text-slate-800">Executive Leadership Assessment</h3>
                    <p class="text-[11px] text-slate-500 mt-0.5">${prefix} ${dateStr} at ${timeStr}</p>
                </div>
                ${badgeHtml}
            </div>
        `;
    });
    
    container.innerHTML = html;
    applyCurrentTranslation();
}

window.downloadReport = async function(event, token) {
    const btn = event.currentTarget;
    const originalHtml = btn.innerHTML;
    btn.innerHTML = `<span class="material-symbols-outlined text-[14px] animate-spin">refresh</span><span>Downloading...</span>`;
    try {
        const res = await fetch(`${API_BASE}/api/attempts/${token}/report`, {
            headers: getAuthHeader()
        });
        if (res.ok) {
            alert("Your 5-Page Leadership Dossier report has been successfully downloaded.");
        } else {
            alert("Report is still generating. Please check back later.");
        }
    } catch (e) {
        alert("Failed to download report.");
    } finally {
        btn.innerHTML = originalHtml;
    }
};
"""
if "async function loadAssessmentHistory" not in js:
    js = js + "\n" + history_code


# 4. Inject dynamic i18n triggers (now that they exist)
if "applyCurrentTranslation();\n}" not in js.split("function showPendingPortal(")[1]:
    js = js.replace("showView(\"view-pending-portal\");\n    updateBatteryCardStates(attempt);\n}", "showView(\"view-pending-portal\");\n    updateBatteryCardStates(attempt);\n    applyCurrentTranslation();\n}")

if "applyCurrentTranslation();\n}" not in js.split("function openPreBatteryInstructions(")[1]:
    js = js.replace("showView(\"view-instructions\");\n}", "showView(\"view-instructions\");\n    applyCurrentTranslation();\n}")

js = js.replace("renderGcatMcqQuestion(item, container);\n    }\n}", "renderGcatMcqQuestion(item, container);\n    }\n    applyCurrentTranslation();\n}")
js = js.replace("updateBatteryCardStates(attempt);\n    applyCurrentTranslation();\n    applyCurrentTranslation();\n}", "updateBatteryCardStates(attempt);\n    applyCurrentTranslation();\n}")

# Update renderBatterySidebar
if "function updateTestSidebar" in js:
    # Actually wait, test sidebar generation:
    js = js.replace("navList.innerHTML = html;\n}", "navList.innerHTML = html;\n    applyCurrentTranslation();\n}")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)