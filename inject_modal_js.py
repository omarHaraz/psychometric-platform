import re

js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

modal_js = """
// Custom Modal System
window.showCustomModal = function(options) {
    const overlay = document.getElementById("customModalOverlay");
    const card = document.getElementById("customModalCard");
    const titleEl = document.getElementById("customModalTitle");
    const messageEl = document.getElementById("customModalMessage");
    const iconEl = document.getElementById("customModalIcon");
    const iconContainer = document.getElementById("customModalIconContainer");
    const actionsContainer = document.getElementById("customModalActions");
    
    if (!overlay) return;
    
    titleEl.textContent = options.title || "Information";
    
    // Support newlines in message
    messageEl.innerHTML = (options.message || "").replace(/\\n/g, '<br>');
    
    iconEl.textContent = options.icon || "info";
    
    // Reset colors
    iconContainer.className = "w-14 h-14 rounded-full mx-auto flex items-center justify-center mb-4";
    if (options.type === "danger") {
        iconContainer.classList.add("bg-red-50", "text-red-600");
    } else if (options.type === "success") {
        iconContainer.classList.add("bg-emerald-50", "text-emerald-600");
    } else if (options.type === "warning") {
        iconContainer.classList.add("bg-amber-50", "text-amber-600");
    } else {
        iconContainer.classList.add("bg-[#00685f]/10", "text-[#00685f]");
    }
    
    actionsContainer.innerHTML = "";
    const isArabic = document.documentElement.getAttribute("dir") === "rtl";
    
    if (options.buttons && options.buttons.length > 0) {
        options.buttons.forEach(btnOpts => {
            const btn = document.createElement("button");
            btn.className = `flex-1 py-2.5 rounded-xl font-bold text-sm transition-colors ${
                btnOpts.style === "primary" ? "bg-[#00685f] text-white hover:bg-[#004e47]" :
                btnOpts.style === "danger" ? "bg-red-600 text-white hover:bg-red-700" :
                "bg-white border border-slate-200 text-slate-600 hover:bg-slate-50"
            }`;
            let t = btnOpts.text;
            if (isArabic) {
                if (t === "Cancel") t = "إلغاء";
                if (t === "Confirm") t = "تأكيد";
                if (t === "Submit") t = "إرسال";
                if (t === "Log Out") t = "تسجيل الخروج";
            }
            btn.textContent = t;
            btn.addEventListener("click", () => {
                closeCustomModal();
                if (btnOpts.onClick) btnOpts.onClick();
            });
            actionsContainer.appendChild(btn);
        });
    } else {
        // Default OK button
        const btn = document.createElement("button");
        btn.className = "flex-1 py-2.5 rounded-xl font-bold text-sm bg-[#00685f] text-white hover:bg-[#004e47] transition-colors";
        btn.textContent = isArabic ? "حسناً" : "OK";
        btn.addEventListener("click", closeCustomModal);
        actionsContainer.appendChild(btn);
    }
    
    overlay.classList.remove("hidden");
    // trigger reflow
    void overlay.offsetWidth;
    overlay.style.opacity = "1";
    card.classList.remove("scale-95", "opacity-0");
    card.classList.add("scale-100", "opacity-100");
    
    applyCurrentTranslation();
}

window.closeCustomModal = function() {
    const overlay = document.getElementById("customModalOverlay");
    const card = document.getElementById("customModalCard");
    if (!overlay) return;
    
    overlay.style.opacity = "0";
    card.classList.remove("scale-100", "opacity-100");
    card.classList.add("scale-95", "opacity-0");
    setTimeout(() => {
        overlay.classList.add("hidden");
    }, 200);
}
"""

if "window.showCustomModal = function(options)" not in js:
    js = modal_js + "\n\n" + js

# Replace Log Out confirmation
old_logout = """            const msg = isArabic ? "هل أنت متأكد أنك تريد تسجيل الخروج؟" : "Are you sure you want to logout?";
            if (confirm(msg)) {
                localStorage.removeItem("user");
                window.location.href = "../auth/login.html";
            }"""
new_logout = """            const titleStr = isArabic ? "تسجيل الخروج" : "Log Out";
            const msgStr = isArabic ? "هل أنت متأكد أنك تريد تسجيل الخروج؟" : "Are you sure you want to logout?";
            window.showCustomModal({
                title: titleStr,
                message: msgStr,
                type: "warning",
                icon: "logout",
                buttons: [
                    { text: "Cancel", style: "secondary" },
                    { text: "Log Out", style: "danger", onClick: () => {
                        localStorage.removeItem("user");
                        window.location.href = "../auth/login.html";
                    }}
                ]
            });"""
js = js.replace(old_logout, new_logout)

# Replace final submit confirm
old_submit = """        submitBtn.addEventListener("click", () => {
            if (confirm("Are you sure you want to finalize and submit this battery? You cannot return to these questions.")) {
                recordItemTime(currentItemIndex);
                submitActiveBattery();
            }
        });"""
new_submit = """        submitBtn.addEventListener("click", () => {
            const isArabic = document.documentElement.getAttribute("dir") === "rtl";
            const titleStr = isArabic ? "تأكيد الإرسال" : "Confirm Submission";
            const msgStr = isArabic ? "هل أنت متأكد أنك تريد إنهاء وإرسال هذه البطارية؟ لا يمكنك العودة إلى هذه الأسئلة." : "Are you sure you want to finalize and submit this battery?\\nYou cannot return to these questions.";
            
            window.showCustomModal({
                title: titleStr,
                message: msgStr,
                type: "warning",
                icon: "publish",
                buttons: [
                    { text: "Cancel", style: "secondary" },
                    { text: "Submit", style: "primary", onClick: () => {
                        recordItemTime(currentItemIndex);
                        submitActiveBattery();
                    }}
                ]
            });
        });"""
js = js.replace(old_submit, new_submit)

# Replace alerts
js = js.replace("alert(\"Failed to start assessment battery. Please try again.\");", "window.showCustomModal({title: 'Error', message: 'Failed to start assessment battery. Please try again.', type: 'danger', icon: 'error'});")
js = js.replace("alert(\"Failed to load test items.\");", "window.showCustomModal({title: 'Error', message: 'Failed to load test items.', type: 'danger', icon: 'error'});")
js = js.replace("alert(\"Failed to submit battery responses. Please check connection.\");", "window.showCustomModal({title: 'Submission Failed', message: 'Failed to submit battery responses. Please check connection.', type: 'danger', icon: 'cloud_off'});")

js = js.replace("alert(\"Your 5-Page Leadership Dossier report has been successfully downloaded.\");", "window.showCustomModal({title: 'Success', message: 'Your 5-Page Leadership Dossier report has been successfully downloaded.', type: 'success', icon: 'check_circle'});")
js = js.replace("alert(\"Report is still generating. Please check back later.\");", "window.showCustomModal({title: 'Generating', message: 'Report is still generating.\\nPlease check back later.', icon: 'hourglass_empty'});")
js = js.replace("alert(\"Failed to download report.\");", "window.showCustomModal({title: 'Error', message: 'Failed to download report.', type: 'danger', icon: 'error'});")


with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)