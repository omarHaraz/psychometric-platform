import json

data = [
  {
    "nameAr": "التحفظ",
    "definitionAr": "ميل إلى الانسحاب والعزلة عن الآخرين.",
    "indicators": [
      "الظهور بمظهر غير ودود أو غير مهتم بالآخرين.",
      "الظهور بمزاج مكتئب أو حزين.",
      "تجنب الآخرين والحفاظ على مسافة في العلاقات."
    ]
  },
  {
    "nameAr": "الانفعالية",
    "definitionAr": "ميل إلى التركيز على العيوب وإظهار المشاعر السلبية.",
    "indicators": [
      "امتلاك نظرة سلبية وانخفاض تقدير الذات.",
      "التقلب المزاجي أو القلق.",
      "الظهور بمظهر المتشكك أو المتحفظ تجاه الآخرين."
    ]
  },
  {
    "nameAr": "العدائية",
    "definitionAr": "ميل إلى العدوانية في التعامل مع الآخرين واعتماد أسلوب مباشر وصدامي في التواصل.",
    "indicators": [
      "إظهار العداء والعدوانية تجاه الآخرين.",
      "السعي لتحقيق الأهداف بأي ثمن والتعامل مع الآخرين بدافع هذا السعي.",
      "التركيز المفرط على الذات وقلة الاهتمام بآراء ومشاعر الآخرين."
    ]
  },
  {
    "nameAr": "الاندفاعية",
    "definitionAr": "ميل إلى الاندفاع والمخاطرة دون تفكير كافٍ.",
    "indicators": [
      "قلة التركيز والانتباه لفترات طويلة.",
      "التهاون في الالتزامات وعدم متابعة المهام حتى النهاية.",
      "التصرف بطيش واتخاذ قرارات متسرعة وغير مدروسة."
    ]
  },
  {
    "nameAr": "الصرامة",
    "definitionAr": "ميل إلى الصرامة وعدم المرونة وعدم التسامح مع ما يعتبر تقصيراً في الالتزامات.",
    "indicators": [
      "التعامل بصرامة وجمود في المواقف.",
      "وضع أهداف غير واقعية لأنفسهم وللآخرين.",
      "عدم التسامح مع الأخطاء واعتبار أي تقصير فشلاً."
    ]
  },
  {
    "nameAr": "اللامألوفية",
    "definitionAr": "ميل إلى إظهار سلوكيات غير مألوفة وتجاهل الأعراف الاجتماعية والتعبير عن أفكار أو معتقدات غير تقليدية.",
    "indicators": [
      "امتلاك أساليب تفكير غير اعتيادية ومعتقدات غير مألوفة.",
      "الظهور بمظهر غريب أو مختلف في نظر الآخرين.",
      "العجز عن تفسير كيفية أو أسباب أفعالهم."
    ]
  }
]

with open('backend/src/main/resources/data/derailer_types.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print("Saved derailer_types.json")

# Generate SQL seed file for derailer_items
with open('backend/src/main/resources/data/derailer_items.json', 'r', encoding='utf-8') as f:
    items = json.load(f)

name_to_id = {
    "التحفظ": 1,
    "الانفعالية": 2,
    "العدائية": 3,
    "الاندفاعية": 4,
    "الصرامة": 5,
    "اللامألوفية": 6
}

sql = "SET NAMES utf8mb4;\n\n"

for item in items:
    statement = item['statement_ar'].replace("'", "''")
    justification = item['justification_ar'].replace("'", "''")
    exam_mode = 'BOTH'
    
    # "نادراً جداً - 1" -> 1, "غالباً جداً - 5" -> 5
    ideal_target_str = item['ideal_target']
    if '1' in ideal_target_str:
        ideal_target = 1
    elif '2' in ideal_target_str:
        ideal_target = 2
    elif '3' in ideal_target_str:
        ideal_target = 3
    elif '4' in ideal_target_str:
        ideal_target = 4
    elif '5' in ideal_target_str:
        ideal_target = 5
    else:
        ideal_target = 1
        
    response_scale_type = 'FREQUENCY'
    
    sql += f"INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) "
    sql += f"VALUES ('{statement}', '{justification}', {ideal_target}, '{response_scale_type}', '{exam_mode}', 1, 0, NOW());\n"
    
    sql += "SET @last_item_id = LAST_INSERT_ID();\n"
    
    for comp in item['selected_competencies']:
        if comp in name_to_id:
            type_id = name_to_id[comp]
            sql += f"INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, {type_id});\n"
            
    sql += "\n"
    
with open('seed_derailer_items.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
    
print("Generated seed_derailer_items.sql")
