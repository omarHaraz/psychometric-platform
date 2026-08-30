import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# I will find the exact block and replace it
start_idx = html.find("<!-- RIGHT SIDEBARS (Contextual) -->")
end_idx = html.find("</main>")

if start_idx != -1 and end_idx != -1:
    old_block = html[start_idx:end_idx]
    new_block = "<!-- RIGHT SPACER (To center the main content area) -->\n        <aside id=\"rightSpacer\" class=\"hidden md:block md:w-1/4 shrink-0\"></aside>\n    "
    html = html[:start_idx] + new_block + html[end_idx:]

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)