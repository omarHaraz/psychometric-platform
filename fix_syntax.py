js_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\assets\js\candidate-app.js"
with open(js_path, "r", encoding="utf-8") as f:
    js = f.read()

js = js.replace("showView(\\\"view-pending-portal\\\");", "showView(\"view-pending-portal\");")
js = js.replace("showView(\\\"view-instructions\\\");", "showView(\"view-instructions\");")

with open(js_path, "w", encoding="utf-8") as f:
    f.write(js)