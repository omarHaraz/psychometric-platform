import json
import sys

new_data = [
  {
    "item_code": "GCAT-DQ-31",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أتجاهل المواعيد النهائية إذا شعرت أن العمل يحتاج إلى مزيد من المراجعة الدقيقة.",
    "justification_ar": "تقيس سمة (الصرامة). الإفراط في المثالية على حساب الوقت يعطل العمليات التشغيلية ويؤدي إلى اختناقات (Bottleneck).",
    "selected_competencies": [
      "الصرامة"
    ],
    "ideal_target": "نادراً - 2",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-32",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أستطيع تقبل توجيهات من أشخاص أقل مني خبرة دون الشعور بالاستياء.",
    "justification_ar": "تقيس القدرة على ضبط (العدائية). القائد المتزن يتقبل المعرفة من أي مصدر دون كبر أو هجوم.",
    "selected_competencies": [
      "العدائية"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-33",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحياناً أتحدث بما أفكر فيه فوراً، ثم أدرك لاحقاً أنه كان علي صياغة كلامي بشكل أفضل.",
    "justification_ar": "تقيس سمة (الاندفاعية). درجة متوسطة من الاندفاع قد تحدث أحياناً، ولكن يجب السيطرة عليها لعدم تفاقمها.",
    "selected_competencies": [
      "الاندفاعية"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-34",
    "subtest_dimension": "DERAILER",
    "statement_ar": "في الاجتماعات العاصفة، أشارك برأيي وأطرح الأسئلة بدلاً من الصمت والمراقبة فقط.",
    "justification_ar": "تقيس القدرة على ضبط (التحفظ). الحضور والتفاعل يمنع ترك الساحة للتخبط ويوجه الفريق.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-35",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أفقد حماسي للعمل بالكامل إذا تلقيت تقييماً سلبياً من الإدارة العليا.",
    "justification_ar": "تقيس سمة (الانفعالية). التأثر الشديد بالتقييم يعكس هشاشة نفسية تؤثر على استمرارية الإنجاز.",
    "selected_competencies": [
      "الانفعالية"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-36",
    "subtest_dimension": "DERAILER",
    "statement_ar": "عندما أقدم حلاً جديداً كلياً، أحرص على وضع خطة تجريبية لتوضيح فكرته للآخرين.",
    "justification_ar": "تقيس القدرة على ضبط (اللامألوفية). تقريب الأفكار المعقدة وتجربتها يقلل من مقاومة التغيير.",
    "selected_competencies": [
      "اللامألوفية"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-37",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أصر على تطبيق نفس الإجراءات القديمة حتى عندما تثبت الإحصائيات عدم كفاءتها حالياً.",
    "justification_ar": "تقيس سمة (الصرامة). الجمود وعدم تقبل التغيير الإجرائي يقتل فرص الابتكار (Status Quo) ويقلل الإنتاجية.",
    "selected_competencies": [
      "الصرامة"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-38",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحافظ على نبرة صوت هادئة ومحترفة حتى عندما يرفع الطرف الآخر صوته في النقاش.",
    "justification_ar": "تقيس القدرة على ضبط (العدائية). الثبات الانفعالي وعدم الانجرار للاستفزاز هو سمة القائد الناضج.",
    "selected_competencies": [
      "العدائية"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-39",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أرسل رسائل بريد إلكتروني حادة لزملائي عندما أكتشف خطأ مفاجئاً في العمل.",
    "justification_ar": "تقيس سمة (الاندفاعية). ردود الأفعال الكتابية السريعة والمنفعلة تدمر بيئة العمل وتترك أثراً سلبياً موثقاً.",
    "selected_competencies": [
      "الاندفاعية"
    ],
    "ideal_target": "نادراً - 2",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-40",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحياناً أتجنب النقاشات الاجتماعية خارج نطاق العمل الرسمي مع أعضاء فريقي.",
    "justification_ar": "تقيس سمة (التحفظ). مساحة من العزلة أحياناً مقبولة، لكن يجب ألا تتحول لانفصال كامل عن الفريق.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-41",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أتقبل التغييرات المفاجئة في ميزانية المشروع دون أن يؤثر ذلك على استقراري النفسي.",
    "justification_ar": "تقيس القدرة على ضبط (الانفعالية). تقبل المتغيرات برحابة صدر يضمن استمرار الإنتاجية.",
    "selected_competencies": [
      "الانفعالية"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-42",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أتعمد استخدام أساليب غامضة أو غير مفهومة في الإدارة لأجعل الآخرين يعتمدون علي أكثر.",
    "justification_ar": "تقيس سمة (اللامألوفية). الغموض المتعمد يعيق شفافية المؤسسة ويخلق بيئة عمل غير صحية.",
    "selected_competencies": [
      "اللامألوفية"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-43",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أستطيع تجاوز بعض الأخطاء الشكلية البسيطة إذا كان جوهر العمل ممتازاً ويحقق الهدف.",
    "justification_ar": "تقيس القدرة على ضبط (الصرامة). التركيز على الجوهر بدلاً من الإدارة التفصيلية (Micromanagement) يسرع الإنجاز.",
    "selected_competencies": [
      "الصرامة"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-44",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحياناً أميل للعمل بمفردي تماماً لعدة أيام لإنجاز المهام المعقدة.",
    "justification_ar": "تقيس سمة (التحفظ). الانعزال المؤقت لإنجاز مهام تحتاج تركيزاً هو سلوك مقبول إذا لم يضر بالتواصل الأساسي.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-45",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أوافق فوراً على تولي مشاريع ضخمة قبل التأكد من توافر الموارد والوقت الكافي لفريقي.",
    "justification_ar": "تقيس سمة (الاندفاعية). الحماس الزائد والموافقة السريعة دون دراسة الموارد يورط المؤسسة ويحرق الفريق.",
    "selected_competencies": [
      "الاندفاعية"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-46",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أبادر بطرح حلول ومقترحات في الاجتماعات الإدارية حتى وإن لم أكن رئيس الجلسة.",
    "justification_ar": "تقيس القدرة على ضبط (التحفظ). المبادرة الإيجابية تكسر العزلة وتثري النقاش.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-47",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أستطيع الفصل التام بين مشاكلي الشخصية وأدائي المهني داخل المؤسسة.",
    "justification_ar": "تقيس القدرة على ضبط (الانفعالية). النضج النفسي يتطلب عدم إسقاط المشاكل الخارجية على العمليات الداخلية.",
    "selected_competencies": [
      "الانفعالية"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-48",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أصر على تجاهل الهيكل التنظيمي للمؤسسة وتخطي المديرين بحجة تسريع العمل.",
    "justification_ar": "تقيس سمة (اللامألوفية) وتحدي الأعراف. كسر التسلسل الإداري بشكل متكرر يخلق فوضى تنظيمية.",
    "selected_competencies": [
      "اللامألوفية"
    ],
    "ideal_target": "نادراً - 2",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-49",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحياناً أجد صعوبة في تقبل التغييرات التقنية الجديدة وأفضل الأنظمة القديمة.",
    "justification_ar": "تقيس سمة (الصرامة). مقاومة التغيير قد تحدث أحياناً بسبب التعود، ولكن يجب ألا تعيق التحول الرقمي.",
    "selected_competencies": [
      "الصرامة"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-50",
    "subtest_dimension": "DERAILER",
    "statement_ar": "عندما يطرح شخص فكرة تتعارض مع رأيي، أتعمد إحراجه أمام باقي أعضاء الفريق.",
    "justification_ar": "تقيس سمة (العدائية). إحراج الآخرين سلوك عدائي يدمر الأمان النفسي (Psychological Safety) ويمنع العصف الذهني.",
    "selected_competencies": [
      "العدائية"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-51",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أمنح نفسي وقتاً قبل الرد على أي بريد إلكتروني أو طلب يستفزني بشدة.",
    "justification_ar": "تقيس القدرة على ضبط (الاندفاعية). التروي وكبح جماح الغضب اللحظي يحمي القائد من القرارات الكارثية.",
    "selected_competencies": [
      "الاندفاعية"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-52",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أكتفي بقراءة تقارير العمل وأتجنب النزول للميدان أو التحدث مباشرة مع الموظفين.",
    "justification_ar": "تقيس سمة (التحفظ). القيادة من خلف المكاتب تعزل القائد عن الواقع وتخلق فجوة مع فرق العمل.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "نادراً - 2",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-53",
    "subtest_dimension": "DERAILER",
    "statement_ar": "تظهر علامات التوتر على وجهي بوضوح عندما نقترب من المواعيد النهائية للمشاريع.",
    "justification_ar": "تقيس سمة (الانفعالية). إظهار التوتر أحياناً طبيعة بشرية، ولكن يجب ألا ينتقل كذعر (Panic) للفريق.",
    "selected_competencies": [
      "الانفعالية"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-54",
    "subtest_dimension": "DERAILER",
    "statement_ar": "عندما أتبنى منهجية عمل غير تقليدية، أحرص على تدريب فريقي عليها خطوة بخطوة.",
    "justification_ar": "تقيس القدرة على ضبط (اللامألوفية). نقل المعرفة وتدريب الفريق يمنع العزلة الفكرية ويوحد جهود الإدارة.",
    "selected_competencies": [
      "اللامألوفية"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-55",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أرفض أي اقتراح من فريقي إذا كان يتعارض مع الخطة الأصلية حتى لو كان سيوفر التكاليف.",
    "justification_ar": "تقيس سمة (الصرامة). التمسك الأعمى بالخطة ورفض التحسينات يعكس استبداداً إدارياً ويضيع الموارد.",
    "selected_competencies": [
      "الصرامة"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-56",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحرص على استخدام عبارات الشكر والتقدير حتى عند توجيه ملاحظات تصحيحية قوية.",
    "justification_ar": "تقيس القدرة على ضبط (العدائية). التغليف الإيجابي للنقد يخفف من حدته ويحوله لتوجيه بناء (Constructive Feedback).",
    "selected_competencies": [
      "العدائية"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-57",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أحياناً أقاطع المتحدثين في الاجتماعات إذا شعرت أنهم يبتعدون عن الموضوع الرئيسي.",
    "justification_ar": "تقيس سمة (الاندفاعية). رغم أنها نوع من الاندفاع، إلا أن ضبط إيقاع الاجتماع أحياناً مبرر إدارياً.",
    "selected_competencies": [
      "الاندفاعية"
    ],
    "ideal_target": "أحياناً - 3",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-58",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أغلق باب مكتبي معظم ساعات العمل وأطلب من الموظفين عدم إزعاجي إلا في الكوارث.",
    "justification_ar": "تقيس سمة (التحفظ). سياسة الباب المغلق تقتل التواصل، وتمنع الاكتشاف المبكر للمشكلات.",
    "selected_competencies": [
      "التحفظ"
    ],
    "ideal_target": "نادراً جداً - 1",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-59",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أستوعب غضب العملاء أو رؤسائي بامتصاص الصدمة دون أخذ الأمر بشكل شخصي.",
    "justification_ar": "تقيس القدرة على ضبط (الانفعالية). المرونة النفسية (Resilience) في التعامل مع الغضب الخارجي هي ركيزة الذكاء العاطفي.",
    "selected_competencies": [
      "الانفعالية"
    ],
    "ideal_target": "غالباً - 4",
    "exam_mode": "BOTH (Full & Quick)"
  },
  {
    "item_code": "GCAT-DQ-60",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أشارك أفكاري المبتكرة مع الزملاء لأخذ رأيهم قبل أن أبدأ في تطبيقها بشكل فعلي.",
    "justification_ar": "تقيس القدرة على ضبط (اللامألوفية). الشورى وتلقي التغذية الراجعة يضمنان واقعية الأفكار ويقللان الانفراد الغريب.",
    "selected_competencies": [
      "اللامألوفية"
    ],
    "ideal_target": "غالباً جداً - 5",
    "exam_mode": "BOTH (Full & Quick)"
  }
]

with open('backend/src/main/resources/data/derailer_items.json', 'r', encoding='utf-8') as f:
    existing_data = json.load(f)

existing_data.extend(new_data)

with open('backend/src/main/resources/data/derailer_items.json', 'w', encoding='utf-8') as f:
    json.dump(existing_data, f, ensure_ascii=False, indent=2)

print("Appended successfully to derailer_items.json")

# Now generate SQL for ONLY the new items

name_to_id = {
    "التحفظ": 1,
    "الانفعالية": 2,
    "العدائية": 3,
    "الاندفاعية": 4,
    "الصرامة": 5,
    "اللامألوفية": 6
}

sql = "SET NAMES utf8mb4;\n\n"

for item in new_data:
    statement = item['statement_ar'].replace("'", "''")
    justification = item['justification_ar'].replace("'", "''")
    exam_mode = 'BOTH'
    
    ideal_target_str = item['ideal_target']
    if '1' in ideal_target_str: ideal_target = 1
    elif '2' in ideal_target_str: ideal_target = 2
    elif '3' in ideal_target_str: ideal_target = 3
    elif '4' in ideal_target_str: ideal_target = 4
    elif '5' in ideal_target_str: ideal_target = 5
    else: ideal_target = 1
        
    response_scale_type = 'FREQUENCY'
    
    sql += f"INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) "
    sql += f"VALUES ('{statement}', '{justification}', {ideal_target}, '{response_scale_type}', '{exam_mode}', 1, 0, NOW());\n"
    
    sql += "SET @last_item_id = LAST_INSERT_ID();\n"
    
    for comp in item['selected_competencies']:
        if comp in name_to_id:
            type_id = name_to_id[comp]
            sql += f"INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, {type_id});\n"
            
    sql += "\n"
    
with open('seed_derailer_batch2.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
    
print("Generated seed_derailer_batch2.sql")
