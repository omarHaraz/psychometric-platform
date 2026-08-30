import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

old_header_left = """            <!-- Left: Logo & Portal Title -->
            <div class="flex items-center gap-4">
                <img src="../auth/assets/images/logo.png" alt="Arab Experts Institute" class="h-10 w-auto object-contain pr-4 border-r border-slate-200">
                
                <div class="flex items-center gap-2.5">
                    <div class="w-8 h-8 rounded-lg bg-surface-container-highest text-primary flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-[18px]">person</span>
                    </div>
                    <span id="headerCandidateNameLeft" class="text-base font-bold text-on-surface">Candidate Name</span>
                </div>
            </div>"""

new_header_left = """            <!-- Left: Logo -->
            <div class="flex items-center">
                <img src="../auth/assets/images/logo.png" alt="Arab Experts Institute" class="h-14 w-auto object-contain py-1">
            </div>"""

html = html.replace(old_header_left, new_header_left)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)