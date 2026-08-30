
import re

html_path = r"C:\Users\Logo\Desktop\Psychometric\frontend\candidate\index.html"
with open(html_path, "r", encoding="utf-8") as f:
    html = f.read()

history_pattern = re.compile(r"(\s*<!-- 2\. Assessment History Card -->\s*<section id=\"historySection\".*?</section>)", re.DOTALL)
match = history_pattern.search(html)

if match:
    history_block = match.group(1)
    # Remove it from the current location
    html = html.replace(history_block, "")
    
    # Place it after view-complete
    view_complete_end = re.search(r"(<div id=\"view-complete\".*?</div>\s*</div>)", html, re.DOTALL)
    
    if view_complete_end:
        # Actually view-complete ends at </div>, but let us just find the closing div of view-complete
        
        # A safer way: replace "</div>\n\n    </div>\n    </main>" with the history block
        html = html.replace("    </div>\n    </main>", history_block + "\n    </div>\n    </main>")
        
        with open(html_path, "w", encoding="utf-8") as f:
            f.write(html)
        print("Moved historySection in index.html")

