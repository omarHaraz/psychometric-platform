import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

html = re.sub(r"Language /.*<", r"Language / لغة<", html)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)