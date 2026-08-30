js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

old_code = """        const nameHeader = document.getElementById("userDisplayNameHeader");
        const emailHeader = document.getElementById("userDisplayEmailHeader");
        const sidebarName = document.getElementById("sidebarCandidateName");
        const sidebarEmail = document.getElementById("sidebarCandidateEmail");
        
        if (nameHeader) nameHeader.textContent = currentUser.name || "Candidate";
        if (emailHeader) emailHeader.textContent = currentUser.email || "";"""

new_code = """        const nameHeader = document.getElementById("userDisplayNameHeader");
        const emailHeader = document.getElementById("userDisplayEmailHeader");
        const headerNameLeft = document.getElementById("headerCandidateNameLeft");
        const sidebarName = document.getElementById("sidebarCandidateName");
        const sidebarEmail = document.getElementById("sidebarCandidateEmail");
        
        if (nameHeader) nameHeader.textContent = currentUser.name || "Candidate";
        if (emailHeader) emailHeader.textContent = currentUser.email || "";
        if (headerNameLeft) headerNameLeft.textContent = currentUser.name || "Candidate";"""

js = js.replace(old_code, new_code)

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)