import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

old_logout = """            <!-- Right: Logout -->
            <div class="flex items-center gap-3">
                <button id="logoutBtn" class="p-2 text-slate-500 hover:text-red-600 rounded-lg hover:bg-slate-100 transition-colors flex items-center gap-1.5" title="Sign Out">
                    <span class="text-xs font-bold uppercase tracking-wider">Log Out</span>
                    <span class="material-symbols-outlined text-[20px]">logout</span>
                </button>
            </div>"""

new_logout = """            <!-- Right: Logout Card -->
            <div class="flex items-center gap-3 bg-slate-50 border border-slate-200 rounded-xl p-1.5 pr-4 shadow-sm">
                <div class="w-8 h-8 rounded-lg bg-white text-primary flex items-center justify-center shrink-0 border border-slate-100">
                    <span class="material-symbols-outlined text-[18px]">person</span>
                </div>
                <span id="headerCandidateNameRight" class="text-sm font-bold text-slate-700">Candidate Name</span>
                <div class="w-px h-5 bg-slate-200 mx-1"></div>
                <button id="logoutBtn" class="text-slate-400 hover:text-red-600 transition-colors flex items-center gap-1.5 group" title="Sign Out">
                    <span class="text-[11px] font-bold uppercase tracking-wider hidden sm:block group-hover:text-red-600">Log Out</span>
                    <span class="material-symbols-outlined text-[20px]">logout</span>
                </button>
            </div>"""

html = html.replace(old_logout, new_logout)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)