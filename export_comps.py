import json
import subprocess

# Run mysql command and capture output
cmd = r'"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -uspringstudent -pspringstudent --default-character-set=utf8mb4 psychometric_db -e "SELECT id, code, name_ar, definition_ar, display_order FROM competencies;"'
result = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8')

lines = result.stdout.strip().split('\n')
if len(lines) > 1:
    headers = lines[0].split('\t')
    data = []
    for line in lines[1:]:
        parts = line.split('\t')
        obj = {
            'id': int(parts[0]),
            'code': parts[1],
            'nameAr': parts[2],
            'definitionAr': parts[3] if parts[3] != 'NULL' else None,
            'displayOrder': int(parts[4])
        }
        data.append(obj)
        
    with open('backend/src/main/resources/data/competencies.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    print("Saved competencies.json")
else:
    print("Failed to get data")
