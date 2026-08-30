import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

sidebars = """
        <!-- RIGHT SIDEBARS (Contextual) -->
        
        <!-- Spacer for Empty View -->
        <aside id="rightSpacerEmpty" class="hidden md:block md:w-1/4 shrink-0"></aside>

        <!-- Sidebar for Pending View -->
        <aside id="rightSidebarPending" class="hidden md:flex md:w-1/4 shrink-0 flex-col gap-5">
            <!-- Testing Checklist Card -->
            <div class="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <div class="flex items-center gap-3 mb-4">
                    <div class="w-8 h-8 rounded-lg bg-amber-50 text-amber-600 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-[18px]">checklist</span>
                    </div>
                    <h3 class="font-bold text-sm text-slate-800">Preparation Checklist</h3>
                </div>
                <ul class="space-y-3 text-[13px] text-slate-600">
                    <li class="flex items-start gap-2">
                        <span class="material-symbols-outlined text-[16px] text-emerald-500 shrink-0">check_circle</span>
                        <span>Ensure you are in a quiet, distraction-free environment.</span>
                    </li>
                    <li class="flex items-start gap-2">
                        <span class="material-symbols-outlined text-[16px] text-emerald-500 shrink-0">check_circle</span>
                        <span>Allocate enough uninterrupted time (approx. 90 minutes).</span>
                    </li>
                    <li class="flex items-start gap-2">
                        <span class="material-symbols-outlined text-[16px] text-emerald-500 shrink-0">check_circle</span>
                        <span>Verify you have a stable and reliable internet connection.</span>
                    </li>
                </ul>
            </div>

            <!-- Privacy & Security Card -->
            <div class="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <div class="flex items-center gap-3 mb-4">
                    <div class="w-8 h-8 rounded-lg bg-[#00685f]/10 text-[#00685f] flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-[18px]">shield_locked</span>
                    </div>
                    <h3 class="font-bold text-sm text-slate-800">Privacy & Security</h3>
                </div>
                <p class="text-[12px] text-slate-500 leading-relaxed">
                    Your responses and personal data are strictly confidential and protected by advanced encryption. Information is solely used by the Arab Experts Institute for professional evaluation purposes.
                </p>
                <div class="mt-4 flex items-center gap-2 text-[#00685f] bg-[#00685f]/10 px-3 py-1.5 rounded-lg w-fit">
                    <span class="material-symbols-outlined text-[14px]">lock</span>
                    <span class="text-[11px] font-bold tracking-wide uppercase">Secure Connection</span>
                </div>
            </div>
        </aside>

        <!-- Sidebar for Completed View -->
        <aside id="rightSidebarComplete" class="hidden md:flex md:w-1/4 shrink-0 flex-col gap-5">
            <!-- What Happens Next Card -->
            <div class="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <div class="flex items-center gap-3 mb-4">
                    <div class="w-8 h-8 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-[18px]">forward_to_inbox</span>
                    </div>
                    <h3 class="font-bold text-sm text-slate-800">What Happens Next?</h3>
                </div>
                <p class="text-[12px] text-slate-500 leading-relaxed">
                    Your results have been automatically forwarded to the evaluation committee. If your profile matches the role requirements, the recruitment team will reach out to you with the next steps.
                </p>
            </div>

            <!-- About Your Report Card -->
            <div class="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <div class="flex items-center gap-3 mb-4">
                    <div class="w-8 h-8 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-[18px]">analytics</span>
                    </div>
                    <h3 class="font-bold text-sm text-slate-800">About Your Report</h3>
                </div>
                <p class="text-[12px] text-slate-500 leading-relaxed">
                    The Executive Dossier provides a comprehensive overview of your leadership style, cognitive abilities, and workplace behaviors based on your psychometric responses.
                </p>
            </div>
        </aside>
"""

# Replace the old empty rightSpacer
html = re.sub(r'<aside id="rightSpacer" class="hidden md:block md:w-1/4 shrink-0"></aside>', sidebars, html)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)