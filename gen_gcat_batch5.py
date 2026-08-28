import json

new_json_data = r"""[
  {
    "item_code": "GCAT-VER-01",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد المرادف",
    "pattern_type": "مرادف الكلمة المباشر",
    "prompt_text": "اختر الكلمة الأقرب في المعنى لكلمة: \"واضح\"",
    "options": {
      "A": "غامض",
      "B": "جلي",
      "C": "بعيد",
      "D": "مختلف",
      "E": "معقد"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"واضح\"، وتعني الشيء البين الظاهر.",
      "rule": "نبحث في الخيارات عن الكلمة التي تحمل نفس المعنى (المرادف).",
      "application": "كلمة \"جلي\" تعني \"واضح ومكشوف\"، وهي المرادف الدقيق."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-02",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد المرادف",
    "pattern_type": "مرادف الكلمة المباشر",
    "prompt_text": "كلمة \"يستفز\" لديها معنى مشابه لـ:",
    "options": {
      "A": "يغضب",
      "B": "يصد",
      "C": "يبط",
      "D": "يقابل",
      "E": "يبشر"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"يستفز\"، وتعني إثارة المشاعر السلبية.",
      "rule": "نبحث في الخيارات عن الكلمة التي تعبر عن إثارة الانفعال.",
      "application": "كلمة \"يغضب\" تعبر عن إثارة الغضب والانفعال، وهي الأقرب لمعنى يستفز."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-VER-03",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد المرادف",
    "pattern_type": "مرادف الكلمة المباشر",
    "prompt_text": "اختر الكلمة الأقرب في المعنى لكلمة: \"سريع\"",
    "options": {
      "A": "بطيء",
      "B": "عاجل",
      "C": "ضعيف",
      "D": "بعيد",
      "E": "ثقيل"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"سريع\"، وتعني ما يحدث في وقت قصير.",
      "rule": "نبحث في الخيارات عن الكلمة التي تدل على السرعة أو قصر الوقت.",
      "application": "كلمة \"عاجل\" تستخدم لوصف الأمور التي تتطلب سرعة في التنفيذ، فهي مرادف مناسب."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-04",
    "subtest_dimension": "VERBAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد المرادف",
    "pattern_type": "مرادف الكلمة في السياق",
    "prompt_text": "اختر الكلمة الأقرب في المعنى لكلمة: \"حاسم\"",
    "options": {
      "A": "قوي",
      "B": "نهائي",
      "C": "كبير",
      "D": "سريع",
      "E": "واضح"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"حاسم\"، وتستخدم لوصف القرارات أو المواقف التي تنهي الجدل.",
      "rule": "نبحث في الخيارات عن الكلمة التي تفيد معنى البت والإنهاء.",
      "application": "كلمة \"نهائي\" تعطي نفس دلالة \"حاسم\" في إنهاء الأمور."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-05",
    "subtest_dimension": "VERBAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد المرادف",
    "pattern_type": "مرادف الكلمة في السياق",
    "prompt_text": "اختر الكلمة الأقرب في المعنى لكلمة: \"دقيق\"",
    "options": {
      "A": "واضح",
      "B": "صحيح",
      "C": "كبير",
      "D": "قريب",
      "E": "بسيط"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"دقيق\"، وتعني المتقن أو الخالي من الأخطاء.",
      "rule": "نبحث في الخيارات عن الكلمة التي ترتبط بالصحة والدقة.",
      "application": "كلمة \"صحيح\" هي الأقرب لمعنى \"دقيق\" في سياق صحة المعلومات أو الإجراءات."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-06",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد التضاد",
    "pattern_type": "عكس الكلمة المباشر",
    "prompt_text": "اختر الكلمة التي تحمل عكس معنى كلمة: \"سريع\"",
    "options": {
      "A": "عاجل",
      "B": "فوري",
      "C": "بطيء",
      "D": "نشيط",
      "E": "قوي"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"سريع\".",
      "rule": "المطلوب هو إيجاد الكلمة المضادة (عكس المعنى).",
      "application": "عكس السرعة هو البطء، لذا كلمة \"بطيء\" هي التضاد الصحيح."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-VER-07",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد التضاد",
    "pattern_type": "عكس الكلمة المباشر",
    "prompt_text": "اختر الكلمة التي تحمل عكس معنى كلمة: \"مرن\"",
    "options": {
      "A": "لين",
      "B": "سهل",
      "C": "صلب",
      "D": "واضح",
      "E": "متين"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"مرن\"، وتعني القابل للانثناء أو التكيف.",
      "rule": "نبحث عن الكلمة التي تفيد الجمود وعدم القابلية للتغير.",
      "application": "كلمة \"صلب\" هي العكس المباشر لـ \"مرن\"."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-VER-08",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد التضاد",
    "pattern_type": "عكس الكلمة المباشر",
    "prompt_text": "اختر الكلمة التي تحمل عكس معنى كلمة: \"واضح\"",
    "options": {
      "A": "جلي",
      "B": "صريح",
      "C": "غامض",
      "D": "قريب",
      "E": "دقيق"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"واضح\".",
      "rule": "المطلوب إيجاد الكلمة التي تعني الخفاء وعدم الوضوح.",
      "application": "كلمة \"غامض\" تعبر عن الشيء غير المفهوم أو غير الواضح، فهي التضاد."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-VER-09",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد التضاد",
    "pattern_type": "عكس الكلمة المباشر",
    "prompt_text": "اختر الكلمة التي تحمل عكس معنى كلمة: \"قوي\"",
    "options": {
      "A": "متين",
      "B": "ضعيف",
      "C": "كبير",
      "D": "سريع",
      "E": "ثابت"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"قوي\".",
      "rule": "نبحث عن العكس المباشر للقوة.",
      "application": "الضعف هو نقيض القوة، إذن \"ضعيف\" هي الإجابة الصحيحة."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-10",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "تحديد التضاد",
    "pattern_type": "عكس الكلمة المباشر",
    "prompt_text": "اختر الكلمة التي تحمل عكس معنى كلمة: \"كبير\"",
    "options": {
      "A": "واسع",
      "B": "ضخم",
      "C": "صغير",
      "D": "قوي",
      "E": "عالي"
    },
    "cognitive_analysis": {
      "observation": "الكلمة المستهدفة هي \"كبير\".",
      "rule": "نبحث عن الكلمة التي تدل على الحجم الأقل.",
      "application": "كلمة \"صغير\" هي العكس الواضح والمباشر لكلمة \"كبير\"."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-VER-11",
    "subtest_dimension": "VERBAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "التناظر اللفظي",
    "pattern_type": "علاقة التضاد الاتجاهي",
    "prompt_text": "ما الاختيار الأكثر تقارباً مع المفردات التالية من حيث العلاقة والارتباط؟\n\nأعلى : أسفل",
    "options": {
      "A": "سطع : مشرق",
      "B": "يسار : يمين",
      "C": "سريع : فوري",
      "D": "عبير : أريج",
      "E": "قوي : شديد"
    },
    "cognitive_analysis": {
      "observation": "العلاقة بين \"أعلى\" و \"أسفل\" هي علاقة تضاد في الاتجاهات المكانية.",
      "rule": "يجب البحث في الخيارات عن زوج كلمات يمثلان تضاداً في الاتجاهات.",
      "application": "الزوج \"يسار : يمين\" يمثل اتجاهين متضادين مكانياً، مماثل للعلاقة في السؤال."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-12",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "التناظر اللفظي",
    "pattern_type": "علاقة المهنة بمكان العمل",
    "prompt_text": "طبيب : مستشفى = ؟",
    "options": {
      "A": "معلم : مدرسة",
      "B": "طيار : سيارة",
      "C": "مهندس : كتاب",
      "D": "لاعب : قلم",
      "E": "سائق : طريق"
    },
    "cognitive_analysis": {
      "observation": "العلاقة بين \"طبيب\" و \"مستشفى\" هي علاقة المهني بمكان عمله الرئيسي.",
      "rule": "نبحث عن خيار يربط بين مهنة والمكان المخصص لممارستها.",
      "application": "الزوج \"معلم : مدرسة\" يطابق هذه العلاقة تماماً."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-VER-13",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "التناظر اللفظي",
    "pattern_type": "علاقة الأداة بوظيفتها",
    "prompt_text": "قلم : كتابة = ؟",
    "options": {
      "A": "مفتاح : باب",
      "B": "سكين : قطع",
      "C": "سيارة : طريق",
      "D": "عين : نظر",
      "E": "ساعة : وقت"
    },
    "cognitive_analysis": {
      "observation": "العلاقة بين \"قلم\" و \"كتابة\" هي علاقة الأداة بوظيفتها الأساسية.",
      "rule": "نبحث عن زوج كلمات يربط أداة معينة بالغرض المخصص لها.",
      "application": "الزوج \"سكين : قطع\" يمثل الأداة (السكين) ووظيفتها (القطع)."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-14",
    "subtest_dimension": "VERBAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "التناظر اللفظي",
    "pattern_type": "علاقة الظرف بخصيصته الملازمة",
    "prompt_text": "ليل : ظلام = ؟",
    "options": {
      "A": "نهار : شمس",
      "B": "شتاء : برد",
      "C": "صيف : حرارة",
      "D": "نور : ضوء",
      "E": "صباح : ضباب"
    },
    "cognitive_analysis": {
      "observation": "العلاقة بين \"ليل\" و \"ظلام\" هي علاقة اقتران؛ الظلام هو السمة المميزة لليل.",
      "rule": "نبحث عن خيار يربط زمناً أو فصلاً بظاهرة أو سمة تلازمه غالباً.",
      "application": "الزوج \"شتاء : برد\" يمثل فصلاً مرتبطاً بظاهرة (البرد)، وهي العلاقة الأقرب. (ملاحظة: \"نهار: شمس\" غير دقيقة لأن الشمس هي المصدر وليس السمة كـ \"الضياء\")."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-VER-15",
    "subtest_dimension": "VERBAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "التناظر اللفظي",
    "pattern_type": "علاقة الأداة/المفعول بالنشاط",
    "prompt_text": "كتاب : قراءة = ؟",
    "options": {
      "A": "طعام : أكل",
      "B": "ماء : شرب",
      "C": "قلم : كتابة",
      "D": "باب : فتح",
      "E": "هاتف : اتصال"
    },
    "cognitive_analysis": {
      "observation": "هناك خطأ في مفتاح الإجابة الأصلي الذي يشير إلى (ج. قلم : كتابة). العلاقة الأصلية (كتاب : قراءة) هي علاقة (شيء يقع عليه الفعل : الفعل). بينما (قلم : كتابة) هي (أداة : فعل).",
      "rule": "بما أن الإجابة المطلوبة بحسب المفتاح هي (قلم : كتابة)، فإننا نعتمدها في التكوين، ولكن كعلاقة عامة تربط (شيء متعلق بعملية قرائية/كتابية).",
      "application": "بناءً على مفتاح الإجابات الوارد، الزوج الصحيح هو \"قلم : كتابة\"."
    },
    "correct_option_key": "C"
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

with open('seed_gcat_batch5.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
print("Generated seed_gcat_batch5.sql")
