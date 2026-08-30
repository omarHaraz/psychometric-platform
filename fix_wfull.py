html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# Remove w-full from mainContentArea so flex sizing can work properly
html = html.replace(
    '<div id="mainContentArea" class="w-full flex-grow flex flex-col gap-6">',
    '<div id="mainContentArea" class="flex-grow flex flex-col gap-6 min-w-0">'
)

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)