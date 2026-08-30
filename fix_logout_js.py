import re
js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

# Update checkAuth to target the new headerCandidateNameRight
js = js.replace("const headerNameLeft = document.getElementById(\"headerCandidateNameLeft\");", "const headerNameLeft = document.getElementById(\"headerCandidateNameLeft\");\n        const headerNameRight = document.getElementById(\"headerCandidateNameRight\");")
js = js.replace("if (headerNameLeft) headerNameLeft.textContent = currentUser.name || \"Candidate\";", "if (headerNameLeft) headerNameLeft.textContent = currentUser.name || \"Candidate\";\n        if (headerNameRight) headerNameRight.textContent = currentUser.name || \"Candidate\";")

# Add confirmation prompt
old_logout = """    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem("user");
            window.location.href = "../auth/login.html";
        });
    }"""
new_logout = """    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            const isArabic = document.documentElement.getAttribute("dir") === "rtl";
            const msg = isArabic ? "هل أنت متأكد أنك تريد تسجيل الخروج؟" : "Are you sure you want to logout?";
            if (confirm(msg)) {
                localStorage.removeItem("user");
                window.location.href = "../auth/login.html";
            }
        });
    }"""
js = js.replace(old_logout, new_logout)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)