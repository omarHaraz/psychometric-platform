import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

old_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4\">"
new_history = "<section id=\"historySection\" class=\"bg-white border border-slate-200 rounded-xl p-6 space-y-4 max-w-4xl mx-auto w-full\">"

html = html.replace(old_history, new_history)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)