import json

new_data = [
  {
    "item_code": "GCAT-DQ-61",
    "subtest_dimension": "DERAILER",
    "statement_ar": "أتعمد تهميش أو تجاهل الزملاء الذين عارضوا قراراتي سابقاً في اجتماعات العمل.",
    "justification_ar": "تقيس سمة (العدائية). تهميش الزملاء بسبب خلافات سابقة يعكس شخصية انتقامية Vindictive تدمر التعاون وتسمم بيئة العمل.",
    "selected_competencies": [
      "العدائية"
    ],
    "ideal_target": "نادراً جداً - 1",
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
    
with open('seed_derailer_item61.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
    
print("Generated seed_derailer_item61.sql")
