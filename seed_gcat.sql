SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-03', 5, 'MEDIUM', 'FULL', 'مسألة التسلسل الرقمي', 'متتالية حسابية بتزايد مضاعف', 'ما الاختيار التالي في سلسلة الأرقام؟

25، 27، 31، 39، ؟', 'نلاحظ الفرق بين كل رقم والذي يليه: (27-25=2)، (31-27=4)، (39-31=8).', 'مقدار الزيادة يتضاعف في كل خطوة: (+2) ثم (+4) ثم (+8). الزيادة التالية يجب أن تكون ضعف الأخيرة (+16).', 'بإضافة 16 إلى الرقم الأخير 39، يصبح الناتج: 39 + 16 = 55.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '51', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '47', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '55', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '49', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '43', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-04', 5, 'EASY', 'FULL', 'مسألة التسلسل الرقمي', 'متتالية حسابية بزيادة تصاعدية', 'ما الرقم التالي في السلسلة؟

2، 4، 7، 11، ؟', 'نلاحظ الفرق بين الأرقام: (4-2=2)، (7-4=3)، (11-7=4).', 'يتم إضافة أرقام صحيحة متتالية في كل خطوة: (+2)، (+3)، (+4). الزيادة القادمة يجب أن تكون (+5).', 'بإضافة 5 إلى الرقم الأخير 11، يصبح الناتج: 11 + 5 = 16.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '13', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '14', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '15', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '16', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '18', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-05', 5, 'EASY', 'FULL', 'مسألة التسلسل الرقمي', 'متتالية حسابية بزيادة ثابتة', 'ما الرقم التالي في السلسلة؟

3، 6، 9، 12، ؟', 'نلاحظ أن السلسلة تتزايد بمقدار ثابت.', 'يتم إضافة الرقم (+3) بشكل ثابت لكل عنصر لإنتاج العنصر التالي.', 'بإضافة 3 إلى الرقم الأخير 12، يصبح الناتج: 12 + 3 = 15.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '14', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '15', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '16', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '18', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '21', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-06', 5, 'MEDIUM', 'FULL', 'حساب الكسور والنسب المتداخلة', 'ضرب الكسور', 'ما هو خمس ربع عشر الرقم 1000؟', 'المسألة تتطلب إيجاد كسور متتالية من رقم محدد: (1/5) × (1/4) × (1/10) من 1000.', 'لإيجاد القيمة، نقوم بضرب الكسور في بعضها أولاً للحصول على معامل واحد، ثم نضربه في الرقم الكلي.', 'الكسر الإجمالي = (1/5) × (1/4) × (1/10) = (1/200). إذن: (1/200) × 1000 = 1000 ÷ 200 = 5.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '7', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '5', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '6', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '2', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '4', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-07', 5, 'EASY', 'FULL', 'استنتاج العلاقة النسبية', 'التناسب وتقسيم الأعداد', 'الصلة بين 40 و10، توازي الصلة بين 16 و ؟', 'نبحث عن العلاقة الحسابية المباشرة التي تحول الرقم 40 إلى 10.', 'العلاقة هي القسمة على 4 (لأن 40 ÷ 4 = 10).', 'بتطبيق نفس القاعدة على الرقم 16: نقسم 16 ÷ 4 = 4.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '2.6', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '4', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '6', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '8', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-08', 5, 'MEDIUM', 'FULL', 'النسب في الأعداد العشرية', 'التناسب العشري', 'الصلة بين 0.21 و 0.07 توازي الصلة بين 0.48 و ؟', 'نبحث عن العامل الرياضي الذي يربط الرقم 0.21 بالرقم 0.07.', 'العلاقة هي القسمة على 3 (أو أن الرقم الثاني يمثل ثلث الرقم الأول).', 'بتطبيق نفس القاعدة: 0.48 ÷ 3 = 0.16.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '0.24', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '0.21', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '0.3', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '0.16', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '0.35', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-09', 5, 'MEDIUM', 'FULL', 'مسائل المسافات والنسب', 'تكوين معادلة جبرية بسيطة', 'تذهب أنت وزميلك إلى العمل، وقطعتم 30 كم، وقطعت ضعف مسافة زميلك. كم مسافة زميلك؟', 'المسافة الكلية هي 30 كم، ومسافتي تبلغ ضعفي مسافة الزميل.', 'إذا كانت مسافة الزميل (س)، فإن مسافتي هي (2س). مجموع المسافتين يساوي 30.', 'س + 2س = 30 ➔ 3س = 30 ➔ س = 10 كم.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '10', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '25', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '5', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '15', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '20', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-10', 5, 'EASY', 'FULL', 'استنتاج العدد المجهول', 'المعادلات الجبرية المباشرة', 'إذا كان نصف عدد يساوي 20، فما قيمة العدد الكامل؟', 'المعطى هو نصف العدد (1/2 س) والذي يساوي 20.', 'لإيجاد العدد الكامل، نقوم بضرب القيمة المعطاة في 2.', '20 × 2 = 40.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '30', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '35', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '40', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '45', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '50', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-11', 5, 'HARD', 'FULL', 'مسائل الأعمار (فروق العمر الثابتة)', 'حساب الفارق الزمني والنسبة', 'جاسم عمره 12 سنة، وشقيقته ثلاثة أضعاف عمره. كم سيكون عمر شقيقته عندما يبلغ جاسم 25 سنة؟', 'يجب أولاً تحديد عمر شقيقته الحالي، ثم حساب فارق العمر الثابت بينهما.', 'عمر الشقيقة الحالي = 12 × 3 = 36 سنة. الفارق بينهما دائمًا ثابت وهو (36 - 12 = 24 سنة).', 'عندما يصبح جاسم 25 سنة، سيكون عمر شقيقته: 25 + 24 = 49 سنة.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '55', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '46', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '37', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '59', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '49', 1, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-12', 5, 'EASY', 'FULL', 'مسائل الأعمار', 'حساب الفارق العمري المباشر', 'عمر أحمد 10 سنوات، وعمر أخيه أكبر منه بـ 5 سنوات. كم عمر أخيه؟', 'عمر أحمد 10 سنوات، والأخ الأكبر يزيد عنه بـ 5 سنوات.', 'نقوم بجمع فارق العمر مع عمر أحمد الحالي.', '10 + 5 = 15 سنة.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '13', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '14', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '15', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '16', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-13', 5, 'EASY', 'FULL', 'مسائل الأعمار (الزمن المستقبلي)', 'الجمع المباشر المعتمد على الزمن', 'عمر شخص الآن 20 سنة، كم سيكون عمره بعد 5 سنوات؟', 'المطلوب حساب العمر المستقبلي بإضافة فترة زمنية محددة.', 'العمر المستقبلي = العمر الحالي + عدد السنوات القادمة.', '20 + 5 = 25 سنة.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '22', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '23', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '24', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '25', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '26', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-14', 5, 'EASY', 'FULL', 'مسائل الأعمار (النسب والمضاعفات)', 'حساب المضاعفات المباشرة', 'عمر خالد ضعف عمر أخيه، إذا كان عمر أخيه 8 سنوات، فكم عمر خالد؟', 'نعرف عمر الأخ (8) ونعرف أن عمر خالد يمثل الضعف (مضروباً في 2).', 'عمر خالد = عمر الأخ × 2.', '8 × 2 = 16 سنة.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '14', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '16', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '18', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '20', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-15', 5, 'EASY', 'FULL', 'مسائل الأعمار والفروق', 'الجمع لإيجاد العمر الأكبر', 'فرق العمر بين شخصين 6 سنوات، إذا كان عمر الأصغر 20 سنة، فكم عمر الأكبر؟', 'نعرف عمر الشخص الأصغر والفرق العمري بينهما.', 'عمر الأكبر = عمر الأصغر + فرق العمر.', '20 + 6 = 26 سنة.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '12', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '14', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '16', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '18', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', '26', 1, 5);

