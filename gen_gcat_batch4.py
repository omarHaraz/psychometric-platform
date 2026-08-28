import json

new_json_data = r"""[
  {
    "item_code": "GCAT-NUM-46",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب المعدلات",
    "pattern_type": "حساب المعدل المباشر والكمية المطلوبة",
    "prompt_text": "استخدم أحد المصانع 1200 كجم من الصلب لإنتاج 800 وحدة من منتج معين. إذا رغب المصنع في إنتاج 1500 وحدة بنفس المعدل، فكم كجم من الصلب سيحتاج؟",
    "options": {
      "A": "2250 كجم",
      "B": "1800 كجم",
      "C": "2000 كجم",
      "D": "2500 كجم"
    },
    "cognitive_analysis": {
      "observation": "نحتاج لحساب معدل الاستخدام للوحدة الواحدة أولاً.",
      "rule": "معدل الاستخدام = الكمية المستخدمة / عدد الوحدات.",
      "application": "1200 / 800 = 1.5 كجم للوحدة. الكمية المطلوبة = 1.5 × 1500 = 2250 كجم."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-47",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب نسبة الربح",
    "pattern_type": "حساب نسبة الربح إلى التكلفة",
    "prompt_text": "بلغت تكلفة إنتاج قطعة غيار معينة 45 درهماً، وتم بيعها بسعر 63 درهماً. ما هي نسبة الربح إلى تكلفة الإنتاج؟",
    "options": {
      "A": "30%",
      "B": "35%",
      "C": "45%",
      "D": "40%"
    },
    "cognitive_analysis": {
      "observation": "نحتاج أولاً لحساب مقدار الربح.",
      "rule": "الربح = سعر البيع - تكلفة الإنتاج. نسبة الربح إلى التكلفة = الربح / التكلفة.",
      "application": "الربح = 63 - 45 = 18 درهماً. نسبة الربح إلى التكلفة = 18 / 45 = 0.40 = 40%."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-48",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب النسب",
    "pattern_type": "حساب قيمة جزء من النسبة الإجمالية",
    "prompt_text": "يبيع تاجر السيارات نوعين من السيارات بنسبة 3:1 (بنزين إلى هجين). إذا كان إجمالي المبيعات الشهرية 160 سيارة، فكم عدد السيارات الهجينة المباعة؟",
    "options": {
      "A": "40 سيارة",
      "B": "30 سيارة",
      "C": "35 سيارة",
      "D": "45 سيارة"
    },
    "cognitive_analysis": {
      "observation": "يجب حساب إجمالي الأجزاء في النسبة لمعرفة قيمة الجزء الواحد.",
      "rule": "مجموع النسبة = 3 + 1 = 4 أجزاء. قيمة الجزء = الإجمالي / مجموع الأجزاء.",
      "application": "كل جزء = 160 / 4 = 40 سيارة. السيارات الهجينة = 1 جزء = 40 سيارة."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-49",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "مسائل العمل المشترك",
    "pattern_type": "حساب وحدات العمل والتناسب العكسي",
    "prompt_text": "استغرق فريق من 6 عمال إنجاز مشروع في 12 يوماً. إذا أراد المدير إنجاز نفس المشروع في 8 أيام فقط بمعدل العمل نفسه لكل عامل، فكم عاملاً إضافياً يحتاج؟",
    "options": {
      "A": "2 عمال",
      "B": "4 عمال",
      "C": "5 عمال",
      "D": "3 عمال"
    },
    "cognitive_analysis": {
      "observation": "نحسب إجمالي 'وحدات العمل' المطلوبة لإنجاز المشروع.",
      "rule": "إجمالي 'وحدات العمل' = عدد العمال × عدد الأيام. عدد العمال المطلوب = إجمالي وحدات العمل / عدد الأيام الجديد.",
      "application": "إجمالي 'وحدات العمل' = 6 × 12 = 72 وحدة عمل. عدد العمال المطلوب لإنجاز المشروع في 8 أيام = 72 / 8 = 9 عمال. العمال الإضافيون = 9 - 6 = 3 عمال."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-50",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب تكلفة الإيجار",
    "pattern_type": "حساب التكلفة السنوية لكل وحدة مساحة",
    "prompt_text": "تدفع أسرة إيجاراً شهرياً قدره 8500 درهم لفيلا مساحتها 500 متر مربع. ما هي تكلفة الإيجار السنوي لكل متر مربع؟",
    "options": {
      "A": "190.5 درهم",
      "B": "204 درهم",
      "C": "212.5 درهم",
      "D": "220 درهم"
    },
    "cognitive_analysis": {
      "observation": "يجب حساب الإيجار السنوي الإجمالي أولاً.",
      "rule": "الإيجار السنوي = الإيجار الشهري × 12. التكلفة لكل متر مربع = الإيجار السنوي / المساحة.",
      "application": "الإيجار السنوي = 8500 × 12 = 102,000 درهم. التكلفة لكل متر مربع = 102,000 / 500 = 204 درهم."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-51",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب المتوسط المرجح",
    "pattern_type": "حساب المتوسط الإجمالي لمجموعتين",
    "prompt_text": "يحتوي جدول رواتب الشركة على 4 فئات وظيفية بمتوسط رواتب مختلف: الفئة أ (10 موظفين، متوسط 8000 درهم)، الفئة ب (15 موظفاً، متوسط 6000 درهم). ما هو متوسط الراتب الإجمالي لكل من الفئتين معاً؟",
    "options": {
      "A": "6640 درهم",
      "B": "6800 درهم",
      "C": "7000 درهم",
      "D": "7200 درهم"
    },
    "cognitive_analysis": {
      "observation": "لا يمكن أخذ متوسط المتوسطات مباشرة. يجب حساب إجمالي الرواتب لكل فئة.",
      "rule": "إجمالي الرواتب = المتوسط × عدد الموظفين لكل فئة. المتوسط الكلي = إجمالي الرواتب للفئتين / إجمالي عدد الموظفين.",
      "application": "إجمالي رواتب الفئة أ = 10 × 8000 = 80,000 درهم. إجمالي رواتب الفئة ب = 15 × 6000 = 90,000 درهم. الإجمالي الكلي = 170,000 درهم. إجمالي عدد الموظفين = 10 + 15 = 25. المتوسط الكلي = 170,000 / 25 = 6800 درهم."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-52",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب التكلفة بعد الخصم",
    "pattern_type": "حساب التكلفة الأصلية باستبعاد نسبة الرسوم",
    "prompt_text": "إذا كانت تكلفة استيراد شحنة من 50 وحدة تساوي 175000 درهم شاملة الرسوم، وكانت الرسوم تمثل 12% من التكلفة الإجمالية، فما هي تكلفة الوحدة الواحدة قبل الرسوم؟",
    "options": {
      "A": "2800 درهم",
      "B": "3200 درهم",
      "C": "3080 درهم",
      "D": "3500 درهم"
    },
    "cognitive_analysis": {
      "observation": "التكلفة الإجمالية تشمل التكلفة الأصلية + 12% رسوم. التكلفة الأصلية تمثل (100% - 12%) من التكلفة الإجمالية.",
      "rule": "التكلفة قبل الرسوم = التكلفة الإجمالية × (1 - النسبة). تكلفة الوحدة = التكلفة قبل الرسوم / عدد الوحدات.",
      "application": "التكلفة قبل الرسوم = 175000 × (1 - 0.12) = 175000 × 0.88 = 154000 درهم. تكلفة الوحدة = 154000 / 50 = 3080 درهم."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-53",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب نسبة الزيادة",
    "pattern_type": "حساب النسبة المئوية للتغير",
    "prompt_text": "ارتفع سعر سهم شركة من 24 درهماً إلى 30 درهماً خلال شهر واحد. ما نسبة الارتفاع في السعر؟",
    "options": {
      "A": "25%",
      "B": "20%",
      "C": "30%",
      "D": "33%"
    },
    "cognitive_analysis": {
      "observation": "نحسب الفرق بين السعرين أولاً.",
      "rule": "الفرق = السعر الجديد - السعر القديم. نسبة الارتفاع = الفرق / السعر القديم.",
      "application": "الفرق = 30 - 24 = 6 دراهم. نسبة الارتفاع = 6 / 24 = 0.25 = 25%."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-54",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب السرعة",
    "pattern_type": "استخدام قانون السرعة والمسافة والزمن",
    "prompt_text": "قطعت سيارة مسافة 315 كم خلال 4.5 ساعة بسرعة ثابتة. ما هي سرعة السيارة بالكيلومتر في الساعة؟",
    "options": {
      "A": "60 كم/س",
      "B": "65 كم/س",
      "C": "75 كم/س",
      "D": "70 كم/س"
    },
    "cognitive_analysis": {
      "observation": "المعطيات هي المسافة والزمن.",
      "rule": "السرعة = المسافة / الزمن.",
      "application": "السرعة = 315 / 4.5 = 70 كم/س."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-55",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب معدل الإنتاج",
    "pattern_type": "حساب المعدل الفردي ثم الإجمالي",
    "prompt_text": "يعمل مستودع بمعدل تفريغ 40 صندوقاً في الساعة باستخدام آلتين. إذا تمت إضافة آلة ثالثة بنفس الكفاءة، فما هو معدل التفريغ الجديد بالصندوق في الساعة؟",
    "options": {
      "A": "60 صندوقاً",
      "B": "50 صندوقاً",
      "C": "55 صندوقاً",
      "D": "65 صندوقاً"
    },
    "cognitive_analysis": {
      "observation": "نحسب معدل التفريغ للآلة الواحدة أولاً.",
      "rule": "معدل الآلة الواحدة = المعدل الإجمالي / عدد الآلات. المعدل الجديد = معدل الآلة الواحدة × العدد الجديد للآلات.",
      "application": "معدل الآلة الواحدة = 40 / 2 = 20 صندوقاً في الساعة. المعدل الجديد بـ 3 آلات = 20 × 3 = 60 صندوقاً في الساعة."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-56",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب الخصم",
    "pattern_type": "حساب القيمة النهائية بعد الخصم",
    "prompt_text": "استأجرت شركة عقارية فيلا بإيجار سنوي 180,000 درهم يُدفع على 4 دفعات متساوية. إذا رغب المستأجر في الدفع دفعة واحدة سنوية للحصول على خصم 5%، فما هو المبلغ الذي سيدفعه؟",
    "options": {
      "A": "168,000 درهم",
      "B": "171,000 درهم",
      "C": "174,000 درهم",
      "D": "176,000 درهم"
    },
    "cognitive_analysis": {
      "observation": "الخصم يطبق على المبلغ السنوي الإجمالي.",
      "rule": "الخصم = الإيجار السنوي × النسبة المئوية. المبلغ بعد الخصم = الإيجار السنوي - الخصم.",
      "application": "الخصم = 180,000 × 0.05 = 9,000 درهم. المبلغ بعد الخصم = 180,000 - 9,000 = 171,000 درهم."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-57",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب تكلفة الاستهلاك",
    "pattern_type": "حساب الاستهلاك للوحدة ثم التكلفة الإجمالية",
    "prompt_text": "تستهلك سيارة كهربائية 18 كيلوواط/ساعة لقطع 100 كم. إذا كانت تكلفة الكيلوواط/ساعة 0.35 درهم، فما هي تكلفة شحن السيارة لقطع 450 كم؟",
    "options": {
      "A": "24.75 درهم",
      "B": "28.35 درهم",
      "C": "31.50 درهم",
      "D": "36.00 درهم"
    },
    "cognitive_analysis": {
      "observation": "نحتاج إلى حساب الاستهلاك للكيلومتر الواحد أولاً.",
      "rule": "الاستهلاك لكل كم = الاستهلاك المعطى / المسافة المعطاة. الاستهلاك الإجمالي = الاستهلاك لكل كم × المسافة الجديدة. التكلفة = الاستهلاك الإجمالي × تكلفة الوحدة.",
      "application": "الاستهلاك لكل كم = 18 / 100 = 0.18 كيلوواط/ساعة. الاستهلاك لـ 450 كم = 0.18 × 450 = 81 كيلوواط/ساعة. التكلفة = 81 × 0.35 = 28.35 درهم."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-58",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب نسبة النمو",
    "pattern_type": "حساب النسبة المئوية للزيادة",
    "prompt_text": "بلغت إيرادات شركة استيراد السيارات 2.4 مليون درهم في الربع الأول و3.0 مليون درهم في الربع الثاني. ما نسبة النمو بين الربعين؟",
    "options": {
      "A": "20%",
      "B": "25%",
      "C": "30%",
      "D": "35%"
    },
    "cognitive_analysis": {
      "observation": "نحسب الفرق بين الإيرادات في الربعين.",
      "rule": "الفرق = الإيرادات في الربع الثاني - الإيرادات في الربع الأول. نسبة النمو = الفرق / الإيرادات في الربع الأول.",
      "application": "الفرق = 3.0 - 2.4 = 0.6 مليون. نسبة النمو = 0.6 / 2.4 = 0.25 = 25%."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-59",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب الزيادة في التكلفة الجزئية",
    "pattern_type": "حساب التكلفة الجزئية ثم مقدار الزيادة عليها",
    "prompt_text": "في عملية بناء عمارة G+6، تبلغ نسبة تكلفة الهيكل الخرساني 40% من إجمالي تكلفة البناء البالغة 3,000,000 درهم. إذا ارتفعت أسعار الحديد والخرسانة بنسبة 15%، فما هي الزيادة في التكلفة الإجمالية للمشروع (بافتراض أن باقي التكاليف ثابتة)؟",
    "options": {
      "A": "120,000 درهم",
      "B": "150,000 درهم",
      "C": "200,000 درهم",
      "D": "180,000 درهم"
    },
    "cognitive_analysis": {
      "observation": "نحسب تكلفة الهيكل الخرساني أولاً.",
      "rule": "تكلفة الهيكل الخرساني = التكلفة الإجمالية × نسبة الهيكل. الزيادة = تكلفة الهيكل × نسبة الزيادة.",
      "application": "تكلفة الهيكل الخرساني = 3,000,000 × 0.40 = 1,200,000 درهم. الزيادة = 1,200,000 × 0.15 = 180,000 درهم."
    },
    "correct_option_key": "D"
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

with open('seed_gcat_batch4.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
print("Generated seed_gcat_batch4.sql")
