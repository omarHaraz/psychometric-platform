SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-16', 5, 'MEDIUM', 'FULL', 'مسألة التسلسل الرقمي المزدوج', 'متتالية بفروق متزايدة', 'ما الاختيار التالي في سلسلة الأرقام؟

47، 59، 83، 119، ؟', 'نحسب الفروق بين الأرقام المتتالية: (59-47=12)، (83-59=24)، (119-83=36).', 'الفروق هي مضاعفات العدد 12: (+12)، ثم (+24)، ثم (+36). الزيادة القادمة يجب أن تكون (+48).', 'بإضافة 48 إلى الرقم الأخير 119، يصبح الناتج: 119 + 48 = 167.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '167', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '323', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '209', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '197', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '269', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-17', 5, 'MEDIUM', 'FULL', 'مسألة التسلسل الرقمي', 'متتالية حسابية بتزايد مضاعف', 'ما الاختيار التالي في سلسلة الأرقام؟

25، 27، 31، 39، ؟', 'نلاحظ الفروق بين الأرقام: (27-25=2)، (31-27=4)، (39-31=8).', 'مقدار الزيادة يتضاعف في كل خطوة: (+2)، (+4)، (+8). الزيادة التالية يجب أن تكون (+16).', 'بإضافة 16 إلى 39، يكون الناتج: 39 + 16 = 55.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '51', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '47', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '55', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '49', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '43', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-18', 5, 'MEDIUM', 'FULL', 'سلسلة الفروق الفردية', 'زيادة بأعداد فردية متتالية', 'ما الرقم التالي في السلسلة التالية؟

3، 8، 15، 24، 35، ؟', 'الفروق بين الأرقام هي: 5، 7، 9، 11.', 'السلسلة تتزايد بإضافة أعداد فردية متتالية. الرقم المضاف الأخير كان 11، لذا الإضافة التالية ستكون 13.', 'بإضافة 13 إلى 35، الناتج هو: 35 + 13 = 48.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '46', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '47', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '48', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '49', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '50', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-19', 5, 'MEDIUM', 'FULL', 'سلسلة الأعداد المتتالية', 'زيادة بأعداد صحيحة متتالية', 'ما الرقم التالي في السلسلة التالية؟

4، 6، 9، 13، 18، ؟', 'الفروق هي: 2، 3، 4، 5.', 'نمط الزيادة يعتمد على إضافة عدد يزيد بمقدار 1 عن الإضافة السابقة. الإضافة القادمة هي 6.', '18 + 6 = 24.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '22', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '23', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '24', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '25', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '26', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-20', 5, 'EASY', 'FULL', 'المتتالية الهندسية', 'المضاعفة الثابتة', 'ما الرقم التالي في السلسلة التالية؟

10، 20، 40، 80، ؟', 'كل رقم يمثل ضعف الرقم الذي يسبقه.', 'يتم ضرب كل عنصر في العدد (2) لاستنتاج العنصر التالي.', '80 × 2 = 160.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '120', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '140', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '160', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '180', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '200', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-21', 5, 'MEDIUM', 'FULL', 'استنتاج العلاقة التناسبية', 'القسمة الثابتة', 'الصلة بين 40 و 10، توازي الصلة بين 16 و ؟', 'نبحث عن العلاقة الحسابية المباشرة التي تحول الرقم 40 إلى 10.', 'العلاقة هي القسمة على 4 (40 ÷ 4 = 10).', 'بتطبيق القاعدة على الرقم الثاني: 16 ÷ 4 = 4.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '2.6', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '4', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '6', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '8', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-22', 5, 'MEDIUM', 'FULL', 'النسب في الأعداد العشرية', 'القسمة العشرية', 'الصلة بين 0.21 و 0.07 توازي الصلة بين 0.48 و ؟', 'الرقم 0.07 هو ثلث الرقم 0.21.', 'العامل الرياضي هو القسمة على 3.', 'بقسمة 0.48 على 3 يكون الناتج 0.16.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '0.24', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '0.21', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '0.3', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '0.16', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '0.35', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-23', 5, 'MEDIUM', 'FULL', 'الكسور المتداخلة', 'ضرب الكسور المتتابعة', 'ما هو خمس ربع عشر الرقم 1000؟', 'المطلوب حساب (1/5) × (1/4) × (1/10) × 1000.', 'نضرب المقامات معاً للحصول على الكسر النهائي: 5 × 4 × 10 = 200. أي أننا نبحث عن (1/200) من 1000.', '1000 ÷ 200 = 5.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '7', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '5', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '6', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '2', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '4', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-24', 5, 'MEDIUM', 'FULL', 'معادلة المسافات النسبية', 'المعادلات الجبرية', 'تذهب أنت وزميلك إلى العمل وقد قطعتم سوياً 30 كيلومتر. إذا قطعت ضعف المسافة التي قطعها زميلك، فما المسافة التي يقطعها زميلك؟', 'إجمالي المسافة هو 30. نسبة مسافتي لمسافة زميلي هي 2:1.', 'إذا كانت مسافة الزميل (س)، مسافتي هي (2س). المعادلة هي: س + 2س = 30.', '3س = 30، إذن س (مسافة الزميل) = 10.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10 كيلومتر', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '25 كيلومتر', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '5 كيلومتر', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '15 كيلومتر', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '20 كيلومتر', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-25', 5, 'EASY', 'FULL', 'حساب نسبة الزيادة المئوية', 'النسب المئوية', 'إذا زاد عدد من 200 إلى 250، فما نسبة الزيادة؟', 'العدد الأصلي 200 والعدد الجديد 250، مقدار الزيادة هو 50.', 'نسبة الزيادة = (مقدار الزيادة ÷ العدد الأصلي) × 100.', '(50 ÷ 200) × 100 = 25%.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '20%', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '25%', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '30%', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '40%', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '50%', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-26', 5, 'HARD', 'FULL', 'مسائل الأعمار المستمرة', 'حساب الفارق الزمني الثابت', 'جاسم عمره 12 سنة وشقيقته هي ثلاثة أضعاف عمره. كم سيكون عمر شقيقته عندما يبلغ جاسم 25 عاماً؟', 'عمر جاسم 12، وعمر شقيقته 12 × 3 = 36. الفارق بينهما (36 - 12) = 24 سنة.', 'الفارق العمري يبقى ثابتاً مهما مر الزمن.', 'عندما يصبح جاسم 25، سيكون عمر الشقيقة 25 + 24 = 49 سنة.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '55', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '46', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '37', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '59', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '49', 1, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-27', 5, 'HARD', 'FULL', 'معادلات الأعمار المعقدة', 'الربط بين الأزمنة', 'عمر شخص الآن 15 سنة، وبعد 5 سنوات سيكون عمر أخيه ضعف عمر الشخص الحالي. كم عمر الأخ الآن؟', 'عمر الشخص الحالي هو 15. ضعف هذا العمر الحالي هو 30.', 'عمر الأخ بعد 5 سنوات سيكون 30. لإيجاد عمر الأخ الحالي، نطرح 5 من 30.', 'عمر الأخ الحالي = 30 - 5 = 25 سنة.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '15', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '20', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '25', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '30', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-28', 5, 'MEDIUM', 'FULL', 'تقسيم المجموع بالفروق', 'المعادلات الخطية', 'مجموع أعمار شخصين 40 سنة، أحدهما أكبر من الآخر بـ 10 سنوات. كم عمر الأكبر؟', 'لدينا المجموع (40) والفارق (10).', 'إذا فرضنا أن الأصغر (س)، يكون الأكبر (س + 10). المعادلة: 2س + 10 = 40.', '2س = 30 ➔ س (الأصغر) = 15. إذن عمر الأكبر = 15 + 10 = 25.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '20', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '22', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '25', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '30', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '35', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-29', 5, 'MEDIUM', 'FULL', 'تطور نسب الأعمار بمرور الزمن', 'إيجاد المتغير الزمني', 'عمر الأب 40 سنة، وعمر ابنه 10 سنوات. بعد كم سنة يصبح عمر الأب ضعف عمر الابن؟', 'نحتاج إلى عدد سنوات (س) بحيث يكون (40 + س) = 2 × (10 + س).', 'بحل المعادلة الجبرية: 40 + س = 20 + 2س.', 'بنقل المتغيرات، نجد أن س = 20 سنة.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '5', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '10', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '15', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '20', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '25', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-30', 5, 'EASY', 'FULL', 'مسائل الفروق البسيطة', 'الجمع المباشر', 'فرق العمر بين شخصين هو 6 سنوات، وإذا كان عمر الأصغر 18 سنة، فكم عمر الأكبر؟', 'العمر الأكبر هو مجموع عمر الأصغر مضافاً إليه الفارق الزمني بينهما.', 'العملية هي: العمر الأصغر + الفارق.', '18 + 6 = 24 سنة.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '20', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '22', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '24', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '26', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '28', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-31', 5, 'HARD', 'FULL', 'سلسلة الفروق المركبة (المستوى المتقدم)', 'متتالية بفروق متزايدة بانتظام', 'ما الرقم التالي في السلسلة التالية؟

3، 7، 14، 25، 41، ؟', 'الفروق الأساسية: 4، 7، 11، 16. نلاحظ فروق هذه الفروق: 3، 4، 5.', 'الفارق الجديد في الفروق يجب أن يكون 6. الفارق الأساسي التالي هو 16 + 6 = 22.', 'بإضافة 22 للرقم 41، يصبح الناتج 63.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '58', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '60', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '63', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '66', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '69', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-32', 5, 'HARD', 'FULL', 'السلسلة المركبة المزدوجة', 'الضرب في 2 مع جمع تسلسلي', 'ما الرقم التالي في السلسلة التالية؟

2، 5، 12، 27، 58، ؟', 'المتتالية تتبع قاعدة (الضرب في 2 وإضافة تسلسل).', 'القاعدة: (الرقم السابق × 2) + n، حيث n يزيد بمقدار 1 كل مرة: (2×2)+1=5، (5×2)+2=12، (12×2)+3=27، (27×2)+4=58.', 'للرقم الأخير: (58 × 2) + 5 = 116 + 5 = 121.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '111', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '117', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '121', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '125', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '131', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-33', 5, 'EASY', 'FULL', 'سلسلة المربعات الكاملة', 'مربعات الأعداد الصحيحة', 'ما الرقم التالي في السلسلة التالية؟

1، 4، 9، 16، 25، ؟', 'الأرقام هي ناتج ضرب الأعداد في نفسها (تربيع).', '1²=1، 2²=4، 3²=9، 4²=16، 5²=25.', 'الرقم التالي هو 6² = 36.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '30', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '35', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '36', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '40', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '45', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-34', 5, 'HARD', 'FULL', 'سلسلة فيبوناتشي المعدلة', 'جمع العنصرين السابقين', 'ما الرقم التالي في السلسلة التالية؟

5، 7، 12، 19، 31، ؟', 'كل رقم (بداية من الثالث) هو نتاج جمع الرقمين اللذين يسبقانه مباشرة.', '5+7=12، 7+12=19، 12+19=31.', '19 + 31 = 50.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '40', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '45', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '50', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '55', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '62', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-35', 5, 'MEDIUM', 'FULL', 'المتتالية الهندسية المضاعفة', 'الضرب الثابت في 3', 'ما الرقم التالي في السلسلة التالية؟

6، 18، 54، 162، ؟', 'כל رقم يمثل ثلاثة أضعاف الرقم الذي قبله.', 'يتم ضرب كل عنصر في العدد الثابت 3.', '162 × 3 = 486.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '324', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '486', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '540', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '648', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '729', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-36', 5, 'HARD', 'FULL', 'ربط السلاسل والمعادلات الضمنية', 'استنتاج المدخلات للسلاسل المتوازية', 'استنتج القيمة: 21 → 4، 43 → 6، 73 → 8، إذن 10 ← ؟', 'المنظومة عبارة عن سلسلتين مرتبطتين. تسلسل النتائج هو (4، 6، 8، 10).', 'المدخلات الأساسية هي سلسلة (21، 43، 73). الفروق بينها تتزايد بشكل منتظم: (43-21=22)، (73-43=30). الزيادة التالية يجب أن تكون 38.', '73 + 38 = 111. الرقم 111 هو الذي سينتج المخرج 10.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '101', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '103', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '111', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '121', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '131', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-37', 5, 'MEDIUM', 'FULL', 'حساب النسب المئوية المركبة', 'إيجاد الأصل ثم النسبة الجديدة', 'إذا كان 25% من عدد = 50، فما قيمة 10% من نفس العدد؟', 'الربع (25%) يمثل 50، أي أن العدد الكلي يمثل 4 أضعاف هذه القيمة.', 'العدد الكلي = 50 × 4 = 200.', '10% من العدد 200 = 200 × 0.10 = 20.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '15', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '20', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '25', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '30', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-38', 5, 'HARD', 'FULL', 'النسب المئوية التتابعية المتضادة', 'الزيادة والنقصان النسبي', 'عدد زاد بنسبة 20% ثم نقص الناتج بنسبة 20%، ما النتيجة النهائية بالنسبة للعدد الأصلي؟', 'الزيادة بنسبة 20% تجعل العدد (1.2). النقصان اللاحق يطبق على العدد الجديد (1.2) وليس الأصل.', 'النتيجة النهائية = الأصل × 1.20 × 0.80 = الأصل × 0.96.', 'بما أن النتيجة النهائية 96% من الأصل، فهو قد نقص قليلاً بنسبة 4%.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'يزيد', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'ينقص', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'يبقى كما هو', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'لا يمكن التحديد', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'ينقص قليلاً', 1, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-39', 5, 'MEDIUM', 'FULL', 'المقارنة بين النسب المئوية الكبيرة', 'حساب وتقدير النسب', 'أي القيمتين أكبر؟ 
- الأولى: 18% × 600,000
- الثانية: 12% × 900,000', 'نحسب كل قيمة على حدة.', 'القيمة الأولى = 0.18 × 600,000 = 108,000. القيمة الثانية = 0.12 × 900,000 = 108,000.', 'القيمتان متطابقتان تماماً.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'الأولى', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'الثانية', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'متساوية', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'لا يمكن التحديد', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'الأولى أكبر قليلاً', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-40', 5, 'EASY', 'FULL', 'التناسب الرياضي المباشر', 'حل التناسب (الضرب التبادلي)', 'إذا كان 3 : 5 = س : 20، فما قيمة س؟', 'العلاقة التناسبية تعني أن (3 ÷ 5) يجب أن يساوي (س ÷ 20).', 'نستخدم الضرب التبادلي: 5 × س = 3 × 20.', '5س = 60، بقسمة الطرفين على 5، نجد أن س = 12.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '12', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '15', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '18', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '20', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-41', 5, 'MEDIUM', 'FULL', 'مسائل الأعمار المستقبلية والمضاعفات', 'إيجاد المتغير الزمني لمعادلة', 'عمر الأب 50 سنة، وعمر الابن 20 سنة، بعد كم سنة يصبح عمر الأب ضعف عمر الابن؟', 'نحتاج تحديد سنوات (س) تضاف لكلا العمرين بحيث يتحقق التضاعف.', 'المعادلة: 50 + س = 2 × (20 + س).', '50 + س = 40 + 2س. بنقل س، نجد أن س = 10 سنوات.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '5', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '10', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '15', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '20', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '25', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-42', 5, 'HARD', 'FULL', 'تداخل الزمن الماضي والمستقبل', 'بناء معادلة زمنية مركبة', 'عمر شخص بعد 10 سنوات سيكون ضعف عمره قبل 10 سنوات، كم عمره الآن؟', 'لنفترض أن عمره الحالي هو (س). عمره بعد 10 سنوات (س + 10)، وقبل 10 سنوات (س - 10).', 'المعادلة: س + 10 = 2 × (س - 10).', 'س + 10 = 2س - 20. بنقل المتغيرات، س = 30 سنة.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '20', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '25', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '30', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '35', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '40', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-43', 5, 'HARD', 'FULL', 'المجموع الكلي والعلاقات بين عدة أطراف', 'المعادلات الجبرية متعددة الأطراف', 'مجموع أعمار ثلاثة أشخاص 60 سنة، إذا كان أحدهم أكبر من الثاني بـ 10 سنوات، والثالث أصغر من الثاني بـ 10 سنوات، كم عمر الأكبر؟', 'نضع الثاني كمتغير أساسي (س). إذن الأكبر (س + 10) والأصغر (س - 10).', 'المجموع: (س + 10) + س + (س - 10) = 60. المعادلة تبسط إلى 3س = 60.', 'س (الثاني) = 20. إذن عمر الأكبر = 20 + 10 = 30 سنة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '25', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '30', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '35', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '40', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '20', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-44', 5, 'HARD', 'FULL', 'ثبات الفارق الزمني والنسب المئوية', 'استغلال ثبات الفارق العمري', 'عمر شخص الآن ضعف عمر أخيه، وبعد 10 سنوات يصبح الفرق بينهما 10 سنوات، كم عمر الأخ الآن؟', 'الفارق الزمني بين شخصين يظل ثابتاً طوال الحياة. الفارق هو 10 سنوات بناءً على المعطى المستقبلي.', 'بما أن عمره الآن ضعف أخيه (أي 2س و س)، فالفارق الحالي هو (2س - س) = س.', 'بما أن الفارق ثابت دائماً (10)، إذن س (عمر الأخ الآن) = 10 سنوات.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '15', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '20', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '25', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '30', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-45', 5, 'HARD', 'FULL', 'المعادلات العمرية المتغيرة النسبة', 'تكوين معادلة التضاعف', 'عمر أحمد الآن ثلاثة أضعاف عمر أخيه، وبعد 6 سنوات يصبح ضعف عمره، كم عمر أحمد الآن؟', 'عمر الأخ الحالي (س)، أحمد (3س). بعد 6 سنوات يصبح الأخ (س+6) وأحمد (3س+6).', 'المعادلة بناءً على المعطى: 3س + 6 = 2 × (س + 6).', '3س + 6 = 2س + 12 ➔ بنقل المتغيرات، س = 6. إذن عمر أحمد الآن هو (3 × 6) = 18 سنة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '18', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '24', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '30', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '36', 0, 5);

