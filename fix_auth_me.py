js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old_check = """function checkAuth() {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        window.location.href = "../auth/login.html";
        return;
    }
    try {
        currentUser = JSON.parse(userStr);
        if (!currentUser || !currentUser.token) {
            window.location.href = "../auth/login.html";
            return;
        }

        // Set user profile in header & sidebar
        const nameHeader = document.getElementById("userDisplayNameHeader");
        const emailHeader = document.getElementById("userDisplayEmailHeader");
        const headerNameLeft = document.getElementById("headerCandidateNameLeft");
        const sidebarName = document.getElementById("sidebarCandidateName");
        const sidebarEmail = document.getElementById("sidebarCandidateEmail");
        
        if (nameHeader) nameHeader.textContent = currentUser.name || "Candidate";
        if (emailHeader) emailHeader.textContent = currentUser.email || "";
        if (headerNameLeft) headerNameLeft.textContent = currentUser.name || "Candidate";
        if (sidebarName) sidebarName.textContent = currentUser.name || "Candidate";
        if (sidebarEmail) sidebarEmail.textContent = currentUser.email || "";
    } catch (e) {
        localStorage.removeItem("user");
        window.location.href = "../auth/login.html";
    }
}"""

new_check = """async function checkAuth() {
    const userStr = localStorage.getItem("user");
    if (!userStr) {
        window.location.href = "../auth/login.html";
        return;
    }
    try {
        currentUser = JSON.parse(userStr);
        if (!currentUser || !currentUser.token) {
            window.location.href = "../auth/login.html";
            return;
        }
        
        // Fetch fresh user data from server
        try {
            const res = await fetch(`${API_BASE}/api/auth/me`, {
                headers: getAuthHeader()
            });
            if (res.ok) {
                const freshUser = await res.json();
                currentUser.name = freshUser.name;
                currentUser.email = freshUser.email;
                localStorage.setItem("user", JSON.stringify(currentUser));
            }
        } catch (e) {
            console.error("Failed to fetch fresh user data", e);
        }

        // Set user profile in header & sidebar
        const nameHeader = document.getElementById("userDisplayNameHeader");
        const emailHeader = document.getElementById("userDisplayEmailHeader");
        const headerNameLeft = document.getElementById("headerCandidateNameLeft");
        const sidebarName = document.getElementById("sidebarCandidateName");
        const sidebarEmail = document.getElementById("sidebarCandidateEmail");
        
        if (nameHeader) nameHeader.textContent = currentUser.name || "Candidate";
        if (emailHeader) emailHeader.textContent = currentUser.email || "";
        if (headerNameLeft) headerNameLeft.textContent = currentUser.name || "Candidate";
        if (sidebarName) sidebarName.textContent = currentUser.name || "Candidate";
        if (sidebarEmail) sidebarEmail.textContent = currentUser.email || "";
    } catch (e) {
        localStorage.removeItem("user");
        window.location.href = "../auth/login.html";
    }
}"""

js = js.replace(old_check, new_check)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)