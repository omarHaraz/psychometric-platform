import json

new_json_data = r"""[
  {
    "item_code": "GCAT-ABS-01",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "دوران السهم بزاوية ثابتة",
    "pattern_type": "دوران منتظم (90 درجة)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "سهم يشير إلى الأسفل",
      "B": "سهم يشير إلى الأعلى",
      "C": "سهم يشير إلى اليسار",
      "D": "سهم يشير إلى اليمين",
      "E": "سهم مائل"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ سلسلة من المربعات بداخلها سهم يتغير اتجاهه تباعاً: أعلى، يمين، أسفل، يسار.",
      "rule": "يدور السهم بزاوية 90 درجة في اتجاه عقارب الساعة في كل خطوة متتالية.",
      "application": "لتحديد الشكل الناقص، نقوم بتدوير السهم الأخير (اليسار) بمقدار 90 درجة في اتجاه عقارب الساعة، ليصبح مشيراً إلى الأعلى (يطابق الخيار B)."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-ABS-02",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "دورتان تعملان معاً",
    "pattern_type": "تغير مزدوج (الشكل والتعبئة)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مثلث أبيض مفرغ يشير لليسار",
      "B": "مربع أسود",
      "C": "دائرة سوداء",
      "D": "مثلث أسود يشير لليسار",
      "E": "معين أسود"
    },
    "cognitive_analysis": {
      "observation": "السلسلة تتكون من أشكال هندسية تتغير في نوعها (دائرة، مثلث، مربع) ولون تعبئتها (أسود صلب، أبيض مفرغ).",
      "rule": "هناك دورتان تعملان بالتزامن: دورة الأشكال (دائرة -> مثلث -> مربع -> تتكرر للدائرة)، ودورة التعبئة (أسود -> أبيض -> أسود -> أبيض -> تتكرر للأسود).",
      "application": "الشكل الرابع هو دائرة بيضاء. بناءً على دورة الأشكال، التالي يجب أن يكون مثلثاً. وبناءً على دورة التعبئة، التالي يجب أن يكون أسود. إذن الشكل الناقص هو مثلث أسود ممتلئ (الخيار D)."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-ABS-03",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "إضافة عنصر واحد في كل خطوة",
    "pattern_type": "زيادة عددية منتظمة",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "أربع نقاط سوداء",
      "B": "ست نقاط سوداء (صفان)",
      "C": "خمس نقاط سوداء (صفان)",
      "D": "ثلاث نقاط سوداء",
      "E": "نقطتان سوداوان"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ سلسلة من المربعات تحتوي على نقاط سوداء يتزايد عددها تدريجياً: المربع الأول به نقطة، الثاني نقطتان، الثالث ثلاث، والرابع أربع نقاط.",
      "rule": "القاعدة هي إضافة نقطة سوداء واحدة (+1) في كل خطوة، مع ترتيبها في صفوف أفقية.",
      "application": "بما أن المربع الرابع يحتوي على 4 نقاط، يجب أن يحتوي المربع الناقص على 5 نقاط (4 + 1 = 5). الخيار C هو الوحيد الذي يحتوي على خمس نقاط (أربع في الصف الأول ونقطة في الصف الثاني)."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-ABS-04",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "انتقال العنصر في مسار دائري",
    "pattern_type": "حركة دورانية في الزوايا",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مربع أسود في الزاوية العلوية اليسرى",
      "B": "مربع أسود في الزاوية السفلية اليمنى",
      "C": "مربع أسود في المنتصف",
      "D": "مربع أسود في الزاوية العلوية اليمنى",
      "E": "مربع أسود في الزاوية السفلية اليسرى"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ وجود مربع أسود صغير يغير موقعه بين زوايا المربع الخارجي في كل خطوة.",
      "rule": "المربع الصغير يتحرك بمقدار زاوية واحدة في اتجاه عقارب الساعة (أعلى يسار -> أعلى يمين -> أسفل يمين -> أسفل يسار).",
      "application": "بناءً على القاعدة، بعد وصول المربع إلى الزاوية السفلية اليسرى في الشكل الرابع، يجب أن يتحرك خطوة إضافية ليعود إلى الزاوية العلوية اليسرى في الشكل الخامس، وهذا يطابق الخيار A."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-ABS-05",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "دوران الجزء المظلل",
    "pattern_type": "دوران منتظم (90 درجة)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مربع نصفه السفلي مظلل",
      "B": "مربع نصفه الأيسر مظلل",
      "C": "مربع نصفه الأيمن مظلل",
      "D": "مربع نصفه السفلي مظلل",
      "E": "مربع نصفه العلوي مظلل"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ مربعاً مقسوماً إلى نصفين، أحدهما مظلل باللون الأسود. يتغير موقع النصف المظلل تباعاً في كل إطار.",
      "rule": "يدور الجزء المظلل داخل المربع بمقدار 90 درجة في اتجاه عقارب الساعة في كل خطوة (أعلى -> يمين -> أسفل -> يسار).",
      "application": "في الشكل الرابع، يقع الجزء المظلل في النصف الأيسر. بتطبيق قاعدة الدوران (90 درجة مع عقارب الساعة)، سيعود الجزء المظلل إلى النصف العلوي في الشكل الخامس. هذا يطابق الخيار E."
    },
    "correct_option_key": "E"
  },
  {
    "item_code": "GCAT-ABS-06",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تغير الشكل كل خطوتين",
    "pattern_type": "تغير الشكل والحجم بصورة دورية",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مثلث يشير لليسار",
      "B": "مثلث صغير يشير لليسار",
      "C": "دائرة صغيرة",
      "D": "معين",
      "E": "مربع صغير"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ سلسلة من الأشكال تتغير وفق نمط معين. الشكل الأول دائرة صغيرة، الثاني دائرة كبيرة، الثالث مربع صغير، الرابع مربع كبير.",
      "rule": "القاعدة تتضمن أمرين: 1. الشكل الأساسي يستمر لإطارين متتاليين ثم يتغير (دائرة، دائرة، ثم مربع، مربع، إذن التالي يجب أن يكون شكلاً جديداً). 2. الحجم يتناوب بين الصغير والكبير في كل شكل (صغير -> كبير -> صغير -> كبير).",
      "application": "بما أن الإطارين الثالث والرابع كانا مربعين، فالإطار الخامس يجب أن يبدأ شكلاً جديداً. النمط لا يوضح ما هو الشكل الجديد بالتحديد، ولكن بالنظر إلى الخيارات، المثلث والمعين هما الشكلان الجديدان. القاعدة الثانية تنص على أن كل شكل جديد يبدأ بحجم صغير. الخيار الوحيد الذي يمثل شكلاً جديداً بحجم صغير هو الخيار B (المثلث الصغير)."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-ABS-07",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "دوران السهم مع تناقص الطول",
    "pattern_type": "تغير مزدوج (دوران وتناقص الحجم)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "سهم يشير إلى اليسار",
      "B": "سهم يشير إلى أعلى اليمين",
      "C": "سهم يشير إلى اليمين",
      "D": "سهم قصير يشير إلى الأسفل",
      "E": "سهم يشير إلى أعلى اليسار"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ سلسلة من المربعات تحتوي على سهم يتغير اتجاهه وطوله في كل خطوة.",
      "rule": "توجد قاعدتان متزامنتان: 1) يدور السهم بمقدار 45 درجة في اتجاه عقارب الساعة في كل إطار متتالٍ. 2) يتناقص طول السهم تدريجياً بصورة منتظمة.",
      "application": "الشكل الرابع يحتوي على سهم يشير إلى أسفل اليمين. بتدويره 45 درجة إضافية مع عقارب الساعة سيشير إلى الأسفل مباشرة. ومع تطبيق قاعدة تناقص الطول، يجب أن يكون أقصر من السهم في الشكل الرابع. الخيار D هو الوحيد الذي يحقق الشرطين (يشير للأسفل وهو الأقصر)."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-ABS-08",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "انتقال دوري بين الصفوف",
    "pattern_type": "حركة دورية (انتقال رأسي)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "شريط أسود في الصف الثالث",
      "B": "شريط أسود في الصف الرابع (السفلي)",
      "C": "شريط أسود في الصف الأول (العلوي)",
      "D": "شريط أسود في الصف الثاني",
      "E": "النصف العلوي مظلل بالكامل (صفان)"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ شكلاً مقسماً إلى أربعة صفوف أفقية، حيث يُظلل صف واحد باللون الأسود ويتغير موقعه في كل إطار.",
      "rule": "ينتقل التظليل الأسود صفاً واحداً إلى الأسفل في كل خطوة متتالية. وعندما يصل إلى الصف الأخير (الرابع)، يعود مجدداً إلى الصف الأول في دورة مستمرة.",
      "application": "في الإطار الرابع، يقع التظليل في الصف الأخير. بناءً على القاعدة الدورية، في الخطوة التالية (الخامسة) يجب أن يقفز التظليل عائداً إلى الصف الأول في الأعلى، وهو ما يطابق الخيار C."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-ABS-09",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "فصل الحجم عن نوع الشكل",
    "pattern_type": "تغير مستقل (الحجم والشكل)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مثلث أسود صغير يشير لليسار",
      "B": "مثلث أسود كبير يشير لليسار",
      "C": "معين أسود صغير",
      "D": "دائرة سوداء صغيرة",
      "E": "مربع أسود صغير"
    },
    "cognitive_analysis": {
      "observation": "السلسلة تتكون من أشكال سوداء: دائرة صغيرة، دائرة كبيرة، مربع صغير، مربع كبير.",
      "rule": "هناك قاعدتان تعملان بشكل مستقل: 1) يتناوب الحجم في كل خطوة (صغير -> كبير -> صغير -> كبير). 2) يستمر نفس الشكل لإطارين متتاليين ثم يتغير لشكل جديد (دائرة مرتين، ثم مربع مرتين).",
      "application": "بناءً على القاعدة الأولى، يجب أن يكون الشكل الخامس (صغيراً). وبناءً على القاعدة الثانية، يجب أن يكون (شكلاً جديداً) بعد انتهاء دورة المربع. الخيار A (مثلث صغير) هو الخيار الأنسب الذي يقدم شكلاً أساسياً جديداً بالحجم الصغير المطلوب."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-ABS-10",
    "subtest_dimension": "ABSTRACT",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تبديل موضعي بين عنصرين",
    "pattern_type": "تبادل مواقع (تناوب)",
    "prompt_text": "اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.",
    "options": {
      "A": "مربع أبيض يسار، دائرة سوداء يمين",
      "B": "دائرة سوداء كبيرة في المنتصف",
      "C": "مربع أبيض كبير في المنتصف",
      "D": "دائرة بيضاء يسار، مربع أسود يمين",
      "E": "دائرة سوداء يسار، مربع أبيض يمين"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ إطاراً يحتوي على عنصرين: دائرة سوداء ومربع أبيض.",
      "rule": "يتبادل العنصران موقعيهما (يمين ويسار) في كل خطوة متتالية بصورة متناوبة ومستمرة.",
      "application": "في الإطار الرابع، المربع الأبيض على اليسار والدائرة السوداء على اليمين. بتطبيق قاعدة التبادل، يجب أن يعود الترتيب في الإطار الخامس إلى: الدائرة السوداء على اليسار والمربع الأبيض على اليمين. هذا يطابق الخيار E."
    },
    "correct_option_key": "E"
  }
]"""

new_data = json.loads(new_json_data)

try:
    with open('backend/src/main/resources/data/gcat_items.json', 'r', encoding='utf-8') as f:
        existing_data = json.load(f)
except Exception:
    existing_data = []

existing_data.extend(new_data)
with open('backend/src/main/resources/data/gcat_items.json', 'w', encoding='utf-8') as f:
    json.dump(existing_data, f, ensure_ascii=False, indent=2)
print("Appended gcat_items.json")

# The mapping of subtest_dimension to ID
subtest_mapping = {
    "ABSTRACT": 4,
    "NUMERICAL": 5,
    "VERBAL": 6
}

sql = "SET NAMES utf8mb4;\n\n"

for item in new_data:
    item_code = item['item_code']
    subtest_dim = item['subtest_dimension']
    subtest_id = subtest_mapping.get(subtest_dim, 5)
    
    difficulty = item['difficulty']
    exam_mode = item['exam_mode']
    
    title = item['title_in_arabic'].replace("'", "''")
    pattern_type = item['pattern_type'].replace("'", "''")
    prompt_text = item['prompt_text'].replace("'", "''")
    
    observation = item['cognitive_analysis']['observation'].replace("'", "''")
    rule = item['cognitive_analysis']['rule'].replace("'", "''")
    application = item['cognitive_analysis']['application'].replace("'", "''")
    best_key = item['correct_option_key']
    
    sql += "INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) "
    sql += f"VALUES ('{item_code}', {subtest_id}, '{difficulty}', '{exam_mode}', '{title}', '{pattern_type}', '{prompt_text}', '{observation}', '{rule}', '{application}', '{best_key}', 1, 0, NOW());\n"
    
    sql += "SET @question_id = LAST_INSERT_ID();\n"
    
    for key, opt_text in item['options'].items():
        opt_text = opt_text.replace("'", "''")
        is_correct = 1 if key == best_key else 0
        display_order = 1 if key == 'A' else 2 if key == 'B' else 3 if key == 'C' else 4 if key == 'D' else 5
        
        sql += f"INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) "
        sql += f"VALUES (@question_id, '{key}', '{opt_text}', {is_correct}, {display_order});\n"
        
    sql += "\n"

with open('seed_gcat_batch7.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
print("Generated seed_gcat_batch7.sql")
