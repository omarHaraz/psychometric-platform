import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

modal_html = """
    <!-- Generic Modal Overlay -->
    <div id="customModalOverlay" class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-[100] hidden flex items-center justify-center p-4 transition-opacity duration-200" style="opacity: 0;">
        <div class="bg-white rounded-2xl shadow-xl border border-slate-200 w-full max-w-sm overflow-hidden flex flex-col transform transition-all duration-300 scale-95 opacity-0" id="customModalCard">
            <div class="p-6 space-y-3 text-center">
                <div id="customModalIconContainer" class="w-14 h-14 rounded-full mx-auto flex items-center justify-center mb-4 bg-slate-100 text-slate-600">
                    <span id="customModalIcon" class="material-symbols-outlined text-3xl">info</span>
                </div>
                <h3 id="customModalTitle" class="text-xl font-bold text-slate-800">Title</h3>
                <p id="customModalMessage" class="text-sm text-slate-500 leading-relaxed font-medium">Message goes here.</p>
            </div>
            <div class="bg-slate-50 p-4 sm:px-6 border-t border-slate-100 flex gap-3" id="customModalActions">
                <!-- Buttons injected by JS -->
            </div>
        </div>
    </div>
"""

# Inject right after OVERLAYS & TRANSITIONS
if "id=\"customModalOverlay\"" not in html:
    html = html.replace("<!-- OVERLAYS & TRANSITIONS -->", "<!-- OVERLAYS & TRANSITIONS -->\n" + modal_html)

# Replace the technical support alert
tech_support = "alert('Technical Support: support@arabexperts.com\\nPhone: +966 11 000 0000')"
tech_support_replacement = "window.showCustomModal({title: 'Technical Support', message: 'support@arabexperts.com\\nPhone: +966 11 000 0000', icon: 'support_agent'})"
html = html.replace(tech_support, tech_support_replacement)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)