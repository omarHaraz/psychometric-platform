import json

new_json_data = r"""[
  {
    "item_code": "GCAT-NUM-16",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "مسألة التسلسل الرقمي المزدوج",
    "pattern_type": "متتالية بفروق متزايدة",
    "prompt_text": "ما الاختيار التالي في سلسلة الأرقام؟\n\n47، 59، 83، 119، ؟",
    "options": {
      "A": "167",
      "B": "323",
      "C": "209",
      "D": "197",
      "E": "269"
    },
    "cognitive_analysis": {
      "observation": "نحسب الفروق بين الأرقام المتتالية: (59-47=12)، (83-59=24)، (119-83=36).",
      "rule": "الفروق هي مضاعفات العدد 12: (+12)، ثم (+24)، ثم (+36). الزيادة القادمة يجب أن تكون (+48).",
      "application": "بإضافة 48 إلى الرقم الأخير 119، يصبح الناتج: 119 + 48 = 167."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-17",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "مسألة التسلسل الرقمي",
    "pattern_type": "متتالية حسابية بتزايد مضاعف",
    "prompt_text": "ما الاختيار التالي في سلسلة الأرقام؟\n\n25، 27، 31، 39، ؟",
    "options": {
      "A": "51",
      "B": "47",
      "C": "55",
      "D": "49",
      "E": "43"
    },
    "cognitive_analysis": {
      "observation": "نلاحظ الفروق بين الأرقام: (27-25=2)، (31-27=4)، (39-31=8).",
      "rule": "مقدار الزيادة يتضاعف في كل خطوة: (+2)، (+4)، (+8). الزيادة التالية يجب أن تكون (+16).",
      "application": "بإضافة 16 إلى 39، يكون الناتج: 39 + 16 = 55."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-18",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "سلسلة الفروق الفردية",
    "pattern_type": "زيادة بأعداد فردية متتالية",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n3، 8، 15، 24، 35، ؟",
    "options": {
      "A": "46",
      "B": "47",
      "C": "48",
      "D": "49",
      "E": "50"
    },
    "cognitive_analysis": {
      "observation": "الفروق بين الأرقام هي: 5، 7، 9، 11.",
      "rule": "السلسلة تتزايد بإضافة أعداد فردية متتالية. الرقم المضاف الأخير كان 11، لذا الإضافة التالية ستكون 13.",
      "application": "بإضافة 13 إلى 35، الناتج هو: 35 + 13 = 48."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-19",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "سلسلة الأعداد المتتالية",
    "pattern_type": "زيادة بأعداد صحيحة متتالية",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n4، 6، 9، 13، 18، ؟",
    "options": {
      "A": "22",
      "B": "23",
      "C": "24",
      "D": "25",
      "E": "26"
    },
    "cognitive_analysis": {
      "observation": "الفروق هي: 2، 3، 4، 5.",
      "rule": "نمط الزيادة يعتمد على إضافة عدد يزيد بمقدار 1 عن الإضافة السابقة. الإضافة القادمة هي 6.",
      "application": "18 + 6 = 24."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-20",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "المتتالية الهندسية",
    "pattern_type": "المضاعفة الثابتة",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n10، 20، 40، 80، ؟",
    "options": {
      "A": "120",
      "B": "140",
      "C": "160",
      "D": "180",
      "E": "200"
    },
    "cognitive_analysis": {
      "observation": "كل رقم يمثل ضعف الرقم الذي يسبقه.",
      "rule": "يتم ضرب كل عنصر في العدد (2) لاستنتاج العنصر التالي.",
      "application": "80 × 2 = 160."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-21",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "استنتاج العلاقة التناسبية",
    "pattern_type": "القسمة الثابتة",
    "prompt_text": "الصلة بين 40 و 10، توازي الصلة بين 16 و ؟",
    "options": {
      "A": "12",
      "B": "2.6",
      "C": "4",
      "D": "6",
      "E": "8"
    },
    "cognitive_analysis": {
      "observation": "نبحث عن العلاقة الحسابية المباشرة التي تحول الرقم 40 إلى 10.",
      "rule": "العلاقة هي القسمة على 4 (40 ÷ 4 = 10).",
      "application": "بتطبيق القاعدة على الرقم الثاني: 16 ÷ 4 = 4."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-22",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "النسب في الأعداد العشرية",
    "pattern_type": "القسمة العشرية",
    "prompt_text": "الصلة بين 0.21 و 0.07 توازي الصلة بين 0.48 و ؟",
    "options": {
      "A": "0.24",
      "B": "0.21",
      "C": "0.3",
      "D": "0.16",
      "E": "0.35"
    },
    "cognitive_analysis": {
      "observation": "الرقم 0.07 هو ثلث الرقم 0.21.",
      "rule": "العامل الرياضي هو القسمة على 3.",
      "application": "بقسمة 0.48 على 3 يكون الناتج 0.16."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-23",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "الكسور المتداخلة",
    "pattern_type": "ضرب الكسور المتتابعة",
    "prompt_text": "ما هو خمس ربع عشر الرقم 1000؟",
    "options": {
      "A": "7",
      "B": "5",
      "C": "6",
      "D": "2",
      "E": "4"
    },
    "cognitive_analysis": {
      "observation": "المطلوب حساب (1/5) × (1/4) × (1/10) × 1000.",
      "rule": "نضرب المقامات معاً للحصول على الكسر النهائي: 5 × 4 × 10 = 200. أي أننا نبحث عن (1/200) من 1000.",
      "application": "1000 ÷ 200 = 5."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-24",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "معادلة المسافات النسبية",
    "pattern_type": "المعادلات الجبرية",
    "prompt_text": "تذهب أنت وزميلك إلى العمل وقد قطعتم سوياً 30 كيلومتر. إذا قطعت ضعف المسافة التي قطعها زميلك، فما المسافة التي يقطعها زميلك؟",
    "options": {
      "A": "10 كيلومتر",
      "B": "25 كيلومتر",
      "C": "5 كيلومتر",
      "D": "15 كيلومتر",
      "E": "20 كيلومتر"
    },
    "cognitive_analysis": {
      "observation": "إجمالي المسافة هو 30. نسبة مسافتي لمسافة زميلي هي 2:1.",
      "rule": "إذا كانت مسافة الزميل (س)، مسافتي هي (2س). المعادلة هي: س + 2س = 30.",
      "application": "3س = 30، إذن س (مسافة الزميل) = 10."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-25",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب نسبة الزيادة المئوية",
    "pattern_type": "النسب المئوية",
    "prompt_text": "إذا زاد عدد من 200 إلى 250، فما نسبة الزيادة؟",
    "options": {
      "A": "20%",
      "B": "25%",
      "C": "30%",
      "D": "40%",
      "E": "50%"
    },
    "cognitive_analysis": {
      "observation": "العدد الأصلي 200 والعدد الجديد 250، مقدار الزيادة هو 50.",
      "rule": "نسبة الزيادة = (مقدار الزيادة ÷ العدد الأصلي) × 100.",
      "application": "(50 ÷ 200) × 100 = 25%."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-26",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "مسائل الأعمار المستمرة",
    "pattern_type": "حساب الفارق الزمني الثابت",
    "prompt_text": "جاسم عمره 12 سنة وشقيقته هي ثلاثة أضعاف عمره. كم سيكون عمر شقيقته عندما يبلغ جاسم 25 عاماً؟",
    "options": {
      "A": "55",
      "B": "46",
      "C": "37",
      "D": "59",
      "E": "49"
    },
    "cognitive_analysis": {
      "observation": "عمر جاسم 12، وعمر شقيقته 12 × 3 = 36. الفارق بينهما (36 - 12) = 24 سنة.",
      "rule": "الفارق العمري يبقى ثابتاً مهما مر الزمن.",
      "application": "عندما يصبح جاسم 25، سيكون عمر الشقيقة 25 + 24 = 49 سنة."
    },
    "correct_option_key": "E"
  },
  {
    "item_code": "GCAT-NUM-27",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "معادلات الأعمار المعقدة",
    "pattern_type": "الربط بين الأزمنة",
    "prompt_text": "عمر شخص الآن 15 سنة، وبعد 5 سنوات سيكون عمر أخيه ضعف عمر الشخص الحالي. كم عمر الأخ الآن؟",
    "options": {
      "A": "10",
      "B": "15",
      "C": "20",
      "D": "25",
      "E": "30"
    },
    "cognitive_analysis": {
      "observation": "عمر الشخص الحالي هو 15. ضعف هذا العمر الحالي هو 30.",
      "rule": "عمر الأخ بعد 5 سنوات سيكون 30. لإيجاد عمر الأخ الحالي، نطرح 5 من 30.",
      "application": "عمر الأخ الحالي = 30 - 5 = 25 سنة."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-28",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "تقسيم المجموع بالفروق",
    "pattern_type": "المعادلات الخطية",
    "prompt_text": "مجموع أعمار شخصين 40 سنة، أحدهما أكبر من الآخر بـ 10 سنوات. كم عمر الأكبر؟",
    "options": {
      "A": "20",
      "B": "22",
      "C": "25",
      "D": "30",
      "E": "35"
    },
    "cognitive_analysis": {
      "observation": "لدينا المجموع (40) والفارق (10).",
      "rule": "إذا فرضنا أن الأصغر (س)، يكون الأكبر (س + 10). المعادلة: 2س + 10 = 40.",
      "application": "2س = 30 ➔ س (الأصغر) = 15. إذن عمر الأكبر = 15 + 10 = 25."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-29",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "تطور نسب الأعمار بمرور الزمن",
    "pattern_type": "إيجاد المتغير الزمني",
    "prompt_text": "عمر الأب 40 سنة، وعمر ابنه 10 سنوات. بعد كم سنة يصبح عمر الأب ضعف عمر الابن؟",
    "options": {
      "A": "5",
      "B": "10",
      "C": "15",
      "D": "20",
      "E": "25"
    },
    "cognitive_analysis": {
      "observation": "نحتاج إلى عدد سنوات (س) بحيث يكون (40 + س) = 2 × (10 + س).",
      "rule": "بحل المعادلة الجبرية: 40 + س = 20 + 2س.",
      "application": "بنقل المتغيرات، نجد أن س = 20 سنة."
    },
    "correct_option_key": "D"
  },
  {
    "item_code": "GCAT-NUM-30",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "مسائل الفروق البسيطة",
    "pattern_type": "الجمع المباشر",
    "prompt_text": "فرق العمر بين شخصين هو 6 سنوات، وإذا كان عمر الأصغر 18 سنة، فكم عمر الأكبر؟",
    "options": {
      "A": "20",
      "B": "22",
      "C": "24",
      "D": "26",
      "E": "28"
    },
    "cognitive_analysis": {
      "observation": "العمر الأكبر هو مجموع عمر الأصغر مضافاً إليه الفارق الزمني بينهما.",
      "rule": "العملية هي: العمر الأصغر + الفارق.",
      "application": "18 + 6 = 24 سنة."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-31",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "سلسلة الفروق المركبة (المستوى المتقدم)",
    "pattern_type": "متتالية بفروق متزايدة بانتظام",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n3، 7، 14، 25، 41، ؟",
    "options": {
      "A": "58",
      "B": "60",
      "C": "63",
      "D": "66",
      "E": "69"
    },
    "cognitive_analysis": {
      "observation": "الفروق الأساسية: 4، 7، 11، 16. نلاحظ فروق هذه الفروق: 3، 4، 5.",
      "rule": "الفارق الجديد في الفروق يجب أن يكون 6. الفارق الأساسي التالي هو 16 + 6 = 22.",
      "application": "بإضافة 22 للرقم 41، يصبح الناتج 63."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-32",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "السلسلة المركبة المزدوجة",
    "pattern_type": "الضرب في 2 مع جمع تسلسلي",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n2، 5، 12، 27، 58، ؟",
    "options": {
      "A": "111",
      "B": "117",
      "C": "121",
      "D": "125",
      "E": "131"
    },
    "cognitive_analysis": {
      "observation": "المتتالية تتبع قاعدة (الضرب في 2 وإضافة تسلسل).",
      "rule": "القاعدة: (الرقم السابق × 2) + n، حيث n يزيد بمقدار 1 كل مرة: (2×2)+1=5، (5×2)+2=12، (12×2)+3=27، (27×2)+4=58.",
      "application": "للرقم الأخير: (58 × 2) + 5 = 116 + 5 = 121."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-33",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "سلسلة المربعات الكاملة",
    "pattern_type": "مربعات الأعداد الصحيحة",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n1، 4، 9، 16، 25، ؟",
    "options": {
      "A": "30",
      "B": "35",
      "C": "36",
      "D": "40",
      "E": "45"
    },
    "cognitive_analysis": {
      "observation": "الأرقام هي ناتج ضرب الأعداد في نفسها (تربيع).",
      "rule": "1²=1، 2²=4، 3²=9، 4²=16، 5²=25.",
      "application": "الرقم التالي هو 6² = 36."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-34",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "سلسلة فيبوناتشي المعدلة",
    "pattern_type": "جمع العنصرين السابقين",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n5، 7، 12، 19، 31، ؟",
    "options": {
      "A": "40",
      "B": "45",
      "C": "50",
      "D": "55",
      "E": "62"
    },
    "cognitive_analysis": {
      "observation": "كل رقم (بداية من الثالث) هو نتاج جمع الرقمين اللذين يسبقانه مباشرة.",
      "rule": "5+7=12، 7+12=19، 12+19=31.",
      "application": "19 + 31 = 50."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-35",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "المتتالية الهندسية المضاعفة",
    "pattern_type": "الضرب الثابت في 3",
    "prompt_text": "ما الرقم التالي في السلسلة التالية؟\n\n6، 18، 54، 162، ؟",
    "options": {
      "A": "324",
      "B": "486",
      "C": "540",
      "D": "648",
      "E": "729"
    },
    "cognitive_analysis": {
      "observation": "כל رقم يمثل ثلاثة أضعاف الرقم الذي قبله.",
      "rule": "يتم ضرب كل عنصر في العدد الثابت 3.",
      "application": "162 × 3 = 486."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-36",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "ربط السلاسل والمعادلات الضمنية",
    "pattern_type": "استنتاج المدخلات للسلاسل المتوازية",
    "prompt_text": "استنتج القيمة: 21 → 4، 43 → 6، 73 → 8، إذن 10 ← ؟",
    "options": {
      "A": "101",
      "B": "103",
      "C": "111",
      "D": "121",
      "E": "131"
    },
    "cognitive_analysis": {
      "observation": "المنظومة عبارة عن سلسلتين مرتبطتين. تسلسل النتائج هو (4، 6، 8، 10).",
      "rule": "المدخلات الأساسية هي سلسلة (21، 43، 73). الفروق بينها تتزايد بشكل منتظم: (43-21=22)، (73-43=30). الزيادة التالية يجب أن تكون 38.",
      "application": "73 + 38 = 111. الرقم 111 هو الذي سينتج المخرج 10."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-37",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "حساب النسب المئوية المركبة",
    "pattern_type": "إيجاد الأصل ثم النسبة الجديدة",
    "prompt_text": "إذا كان 25% من عدد = 50، فما قيمة 10% من نفس العدد؟",
    "options": {
      "A": "10",
      "B": "15",
      "C": "20",
      "D": "25",
      "E": "30"
    },
    "cognitive_analysis": {
      "observation": "الربع (25%) يمثل 50، أي أن العدد الكلي يمثل 4 أضعاف هذه القيمة.",
      "rule": "العدد الكلي = 50 × 4 = 200.",
      "application": "10% من العدد 200 = 200 × 0.10 = 20."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-38",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "النسب المئوية التتابعية المتضادة",
    "pattern_type": "الزيادة والنقصان النسبي",
    "prompt_text": "عدد زاد بنسبة 20% ثم نقص الناتج بنسبة 20%، ما النتيجة النهائية بالنسبة للعدد الأصلي؟",
    "options": {
      "A": "يزيد",
      "B": "ينقص",
      "C": "يبقى كما هو",
      "D": "لا يمكن التحديد",
      "E": "ينقص قليلاً"
    },
    "cognitive_analysis": {
      "observation": "الزيادة بنسبة 20% تجعل العدد (1.2). النقصان اللاحق يطبق على العدد الجديد (1.2) وليس الأصل.",
      "rule": "النتيجة النهائية = الأصل × 1.20 × 0.80 = الأصل × 0.96.",
      "application": "بما أن النتيجة النهائية 96% من الأصل، فهو قد نقص قليلاً بنسبة 4%."
    },
    "correct_option_key": "E"
  },
  {
    "item_code": "GCAT-NUM-39",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "المقارنة بين النسب المئوية الكبيرة",
    "pattern_type": "حساب وتقدير النسب",
    "prompt_text": "أي القيمتين أكبر؟ \n- الأولى: 18% × 600,000\n- الثانية: 12% × 900,000",
    "options": {
      "A": "الأولى",
      "B": "الثانية",
      "C": "متساوية",
      "D": "لا يمكن التحديد",
      "E": "الأولى أكبر قليلاً"
    },
    "cognitive_analysis": {
      "observation": "نحسب كل قيمة على حدة.",
      "rule": "القيمة الأولى = 0.18 × 600,000 = 108,000. القيمة الثانية = 0.12 × 900,000 = 108,000.",
      "application": "القيمتان متطابقتان تماماً."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-40",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "EASY",
    "exam_mode": "FULL",
    "title_in_arabic": "التناسب الرياضي المباشر",
    "pattern_type": "حل التناسب (الضرب التبادلي)",
    "prompt_text": "إذا كان 3 : 5 = س : 20، فما قيمة س؟",
    "options": {
      "A": "10",
      "B": "12",
      "C": "15",
      "D": "18",
      "E": "20"
    },
    "cognitive_analysis": {
      "observation": "العلاقة التناسبية تعني أن (3 ÷ 5) يجب أن يساوي (س ÷ 20).",
      "rule": "نستخدم الضرب التبادلي: 5 × س = 3 × 20.",
      "application": "5س = 60، بقسمة الطرفين على 5، نجد أن س = 12."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-41",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "MEDIUM",
    "exam_mode": "FULL",
    "title_in_arabic": "مسائل الأعمار المستقبلية والمضاعفات",
    "pattern_type": "إيجاد المتغير الزمني لمعادلة",
    "prompt_text": "عمر الأب 50 سنة، وعمر الابن 20 سنة، بعد كم سنة يصبح عمر الأب ضعف عمر الابن؟",
    "options": {
      "A": "5",
      "B": "10",
      "C": "15",
      "D": "20",
      "E": "25"
    },
    "cognitive_analysis": {
      "observation": "نحتاج تحديد سنوات (س) تضاف لكلا العمرين بحيث يتحقق التضاعف.",
      "rule": "المعادلة: 50 + س = 2 × (20 + س).",
      "application": "50 + س = 40 + 2س. بنقل س، نجد أن س = 10 سنوات."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-42",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "تداخل الزمن الماضي والمستقبل",
    "pattern_type": "بناء معادلة زمنية مركبة",
    "prompt_text": "عمر شخص بعد 10 سنوات سيكون ضعف عمره قبل 10 سنوات، كم عمره الآن؟",
    "options": {
      "A": "20",
      "B": "25",
      "C": "30",
      "D": "35",
      "E": "40"
    },
    "cognitive_analysis": {
      "observation": "لنفترض أن عمره الحالي هو (س). عمره بعد 10 سنوات (س + 10)، وقبل 10 سنوات (س - 10).",
      "rule": "المعادلة: س + 10 = 2 × (س - 10).",
      "application": "س + 10 = 2س - 20. بنقل المتغيرات، س = 30 سنة."
    },
    "correct_option_key": "C"
  },
  {
    "item_code": "GCAT-NUM-43",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "المجموع الكلي والعلاقات بين عدة أطراف",
    "pattern_type": "المعادلات الجبرية متعددة الأطراف",
    "prompt_text": "مجموع أعمار ثلاثة أشخاص 60 سنة، إذا كان أحدهم أكبر من الثاني بـ 10 سنوات، والثالث أصغر من الثاني بـ 10 سنوات، كم عمر الأكبر؟",
    "options": {
      "A": "25",
      "B": "30",
      "C": "35",
      "D": "40",
      "E": "20"
    },
    "cognitive_analysis": {
      "observation": "نضع الثاني كمتغير أساسي (س). إذن الأكبر (س + 10) والأصغر (س - 10).",
      "rule": "المجموع: (س + 10) + س + (س - 10) = 60. المعادلة تبسط إلى 3س = 60.",
      "application": "س (الثاني) = 20. إذن عمر الأكبر = 20 + 10 = 30 سنة."
    },
    "correct_option_key": "B"
  },
  {
    "item_code": "GCAT-NUM-44",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "ثبات الفارق الزمني والنسب المئوية",
    "pattern_type": "استغلال ثبات الفارق العمري",
    "prompt_text": "عمر شخص الآن ضعف عمر أخيه، وبعد 10 سنوات يصبح الفرق بينهما 10 سنوات، كم عمر الأخ الآن؟",
    "options": {
      "A": "10",
      "B": "15",
      "C": "20",
      "D": "25",
      "E": "30"
    },
    "cognitive_analysis": {
      "observation": "الفارق الزمني بين شخصين يظل ثابتاً طوال الحياة. الفارق هو 10 سنوات بناءً على المعطى المستقبلي.",
      "rule": "بما أن عمره الآن ضعف أخيه (أي 2س و س)، فالفارق الحالي هو (2س - س) = س.",
      "application": "بما أن الفارق ثابت دائماً (10)، إذن س (عمر الأخ الآن) = 10 سنوات."
    },
    "correct_option_key": "A"
  },
  {
    "item_code": "GCAT-NUM-45",
    "subtest_dimension": "NUMERICAL",
    "difficulty": "HARD",
    "exam_mode": "FULL",
    "title_in_arabic": "المعادلات العمرية المتغيرة النسبة",
    "pattern_type": "تكوين معادلة التضاعف",
    "prompt_text": "عمر أحمد الآن ثلاثة أضعاف عمر أخيه، وبعد 6 سنوات يصبح ضعف عمره، كم عمر أحمد الآن؟",
    "options": {
      "A": "12",
      "B": "18",
      "C": "24",
      "D": "30",
      "E": "36"
    },
    "cognitive_analysis": {
      "observation": "عمر الأخ الحالي (س)، أحمد (3س). بعد 6 سنوات يصبح الأخ (س+6) وأحمد (3س+6).",
      "rule": "المعادلة بناءً على المعطى: 3س + 6 = 2 × (س + 6).",
      "application": "3س + 6 = 2س + 12 ➔ بنقل المتغيرات، س = 6. إذن عمر أحمد الآن هو (3 × 6) = 18 سنة."
    },
    "correct_option_key": "B"
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

with open('seed_gcat_batch3.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
print("Generated seed_gcat_batch3.sql")
