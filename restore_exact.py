import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# Restore logo size to h-10 but keep the Candidate Name gone from the left.
old_logo = """            <!-- Left: Logo -->
            <div class="flex items-center">
                <img src="../auth/assets/images/logo.png" alt="Arab Experts Institute" class="h-14 w-auto object-contain py-1">
            </div>"""

new_logo = """            <!-- Left: Logo & Portal Title -->
            <div class="flex items-center gap-4">
                <img src="../auth/assets/images/logo.png" alt="Arab Experts Institute" class="h-10 w-auto object-contain pr-4">
            </div>"""

html = html.replace(old_logo, new_logo)

# Add max-w-4xl back to history
old_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4\">"
new_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4 max-w-4xl mx-auto w-full\">"

html = html.replace(old_history, new_history)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)