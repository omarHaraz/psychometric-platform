import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# I need to find the right spacer and make it empty again.
# The current one starts with <aside id="rightSpacer" class="hidden md:flex md:w-1/4 shrink-0 flex-col gap-5"> and ends with </aside>

# Regex to match the entire rightSpacer block
html = re.sub(r'<aside id="rightSpacer" class="hidden md:flex.*?</aside>', '<aside id="rightSpacer" class="hidden md:block md:w-1/4 shrink-0"></aside>', html, flags=re.DOTALL)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)