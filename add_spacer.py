import re
html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

# The main container ends with:
#     </div>
# </main>
# We want to add the spacer right before </main>

spacer = """
        <!-- RIGHT SPACER (To center the main content area) -->
        <aside id="rightSpacer" class="hidden md:block md:w-1/4 shrink-0"></aside>
"""

# Let's find the closing main tag
html = html.replace("    </main>", spacer + "    </main>")

with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)