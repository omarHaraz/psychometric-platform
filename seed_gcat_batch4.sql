SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-46', 5, 'EASY', 'FULL', 'حساب المعدلات', 'حساب المعدل المباشر والكمية المطلوبة', 'استخدم أحد المصانع 1200 كجم من الصلب لإنتاج 800 وحدة من منتج معين. إذا رغب المصنع في إنتاج 1500 وحدة بنفس المعدل، فكم كجم من الصلب سيحتاج؟', 'نحتاج لحساب معدل الاستخدام للوحدة الواحدة أولاً.', 'معدل الاستخدام = الكمية المستخدمة / عدد الوحدات.', '1200 / 800 = 1.5 كجم للوحدة. الكمية المطلوبة = 1.5 × 1500 = 2250 كجم.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '2250 كجم', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '1800 كجم', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '2000 كجم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '2500 كجم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-47', 5, 'EASY', 'FULL', 'حساب نسبة الربح', 'حساب نسبة الربح إلى التكلفة', 'بلغت تكلفة إنتاج قطعة غيار معينة 45 درهماً، وتم بيعها بسعر 63 درهماً. ما هي نسبة الربح إلى تكلفة الإنتاج؟', 'نحتاج أولاً لحساب مقدار الربح.', 'الربح = سعر البيع - تكلفة الإنتاج. نسبة الربح إلى التكلفة = الربح / التكلفة.', 'الربح = 63 - 45 = 18 درهماً. نسبة الربح إلى التكلفة = 18 / 45 = 0.40 = 40%.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '30%', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '35%', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '45%', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '40%', 1, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-48', 5, 'MEDIUM', 'FULL', 'حساب النسب', 'حساب قيمة جزء من النسبة الإجمالية', 'يبيع تاجر السيارات نوعين من السيارات بنسبة 3:1 (بنزين إلى هجين). إذا كان إجمالي المبيعات الشهرية 160 سيارة، فكم عدد السيارات الهجينة المباعة؟', 'يجب حساب إجمالي الأجزاء في النسبة لمعرفة قيمة الجزء الواحد.', 'مجموع النسبة = 3 + 1 = 4 أجزاء. قيمة الجزء = الإجمالي / مجموع الأجزاء.', 'كل جزء = 160 / 4 = 40 سيارة. السيارات الهجينة = 1 جزء = 40 سيارة.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '40 سيارة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '30 سيارة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '35 سيارة', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '45 سيارة', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-49', 5, 'MEDIUM', 'FULL', 'مسائل العمل المشترك', 'حساب وحدات العمل والتناسب العكسي', 'استغرق فريق من 6 عمال إنجاز مشروع في 12 يوماً. إذا أراد المدير إنجاز نفس المشروع في 8 أيام فقط بمعدل العمل نفسه لكل عامل، فكم عاملاً إضافياً يحتاج؟', 'نحسب إجمالي ''وحدات العمل'' المطلوبة لإنجاز المشروع.', 'إجمالي ''وحدات العمل'' = عدد العمال × عدد الأيام. عدد العمال المطلوب = إجمالي وحدات العمل / عدد الأيام الجديد.', 'إجمالي ''وحدات العمل'' = 6 × 12 = 72 وحدة عمل. عدد العمال المطلوب لإنجاز المشروع في 8 أيام = 72 / 8 = 9 عمال. العمال الإضافيون = 9 - 6 = 3 عمال.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '2 عمال', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '4 عمال', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '5 عمال', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '3 عمال', 1, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-50', 5, 'EASY', 'FULL', 'حساب تكلفة الإيجار', 'حساب التكلفة السنوية لكل وحدة مساحة', 'تدفع أسرة إيجاراً شهرياً قدره 8500 درهم لفيلا مساحتها 500 متر مربع. ما هي تكلفة الإيجار السنوي لكل متر مربع؟', 'يجب حساب الإيجار السنوي الإجمالي أولاً.', 'الإيجار السنوي = الإيجار الشهري × 12. التكلفة لكل متر مربع = الإيجار السنوي / المساحة.', 'الإيجار السنوي = 8500 × 12 = 102,000 درهم. التكلفة لكل متر مربع = 102,000 / 500 = 204 درهم.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '190.5 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '204 درهم', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '212.5 درهم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '220 درهم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-51', 5, 'HARD', 'FULL', 'حساب المتوسط المرجح', 'حساب المتوسط الإجمالي لمجموعتين', 'يحتوي جدول رواتب الشركة على 4 فئات وظيفية بمتوسط رواتب مختلف: الفئة أ (10 موظفين، متوسط 8000 درهم)، الفئة ب (15 موظفاً، متوسط 6000 درهم). ما هو متوسط الراتب الإجمالي لكل من الفئتين معاً؟', 'لا يمكن أخذ متوسط المتوسطات مباشرة. يجب حساب إجمالي الرواتب لكل فئة.', 'إجمالي الرواتب = المتوسط × عدد الموظفين لكل فئة. المتوسط الكلي = إجمالي الرواتب للفئتين / إجمالي عدد الموظفين.', 'إجمالي رواتب الفئة أ = 10 × 8000 = 80,000 درهم. إجمالي رواتب الفئة ب = 15 × 6000 = 90,000 درهم. الإجمالي الكلي = 170,000 درهم. إجمالي عدد الموظفين = 10 + 15 = 25. المتوسط الكلي = 170,000 / 25 = 6800 درهم.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '6640 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '6800 درهم', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '7000 درهم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '7200 درهم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-52', 5, 'MEDIUM', 'FULL', 'حساب التكلفة بعد الخصم', 'حساب التكلفة الأصلية باستبعاد نسبة الرسوم', 'إذا كانت تكلفة استيراد شحنة من 50 وحدة تساوي 175000 درهم شاملة الرسوم، وكانت الرسوم تمثل 12% من التكلفة الإجمالية، فما هي تكلفة الوحدة الواحدة قبل الرسوم؟', 'التكلفة الإجمالية تشمل التكلفة الأصلية + 12% رسوم. التكلفة الأصلية تمثل (100% - 12%) من التكلفة الإجمالية.', 'التكلفة قبل الرسوم = التكلفة الإجمالية × (1 - النسبة). تكلفة الوحدة = التكلفة قبل الرسوم / عدد الوحدات.', 'التكلفة قبل الرسوم = 175000 × (1 - 0.12) = 175000 × 0.88 = 154000 درهم. تكلفة الوحدة = 154000 / 50 = 3080 درهم.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '2800 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '3200 درهم', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '3080 درهم', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '3500 درهم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-53', 5, 'EASY', 'FULL', 'حساب نسبة الزيادة', 'حساب النسبة المئوية للتغير', 'ارتفع سعر سهم شركة من 24 درهماً إلى 30 درهماً خلال شهر واحد. ما نسبة الارتفاع في السعر؟', 'نحسب الفرق بين السعرين أولاً.', 'الفرق = السعر الجديد - السعر القديم. نسبة الارتفاع = الفرق / السعر القديم.', 'الفرق = 30 - 24 = 6 دراهم. نسبة الارتفاع = 6 / 24 = 0.25 = 25%.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '25%', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '20%', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '30%', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '33%', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-54', 5, 'EASY', 'FULL', 'حساب السرعة', 'استخدام قانون السرعة والمسافة والزمن', 'قطعت سيارة مسافة 315 كم خلال 4.5 ساعة بسرعة ثابتة. ما هي سرعة السيارة بالكيلومتر في الساعة؟', 'المعطيات هي المسافة والزمن.', 'السرعة = المسافة / الزمن.', 'السرعة = 315 / 4.5 = 70 كم/س.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '60 كم/س', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '65 كم/س', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '75 كم/س', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '70 كم/س', 1, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-55', 5, 'MEDIUM', 'FULL', 'حساب معدل الإنتاج', 'حساب المعدل الفردي ثم الإجمالي', 'يعمل مستودع بمعدل تفريغ 40 صندوقاً في الساعة باستخدام آلتين. إذا تمت إضافة آلة ثالثة بنفس الكفاءة، فما هو معدل التفريغ الجديد بالصندوق في الساعة؟', 'نحسب معدل التفريغ للآلة الواحدة أولاً.', 'معدل الآلة الواحدة = المعدل الإجمالي / عدد الآلات. المعدل الجديد = معدل الآلة الواحدة × العدد الجديد للآلات.', 'معدل الآلة الواحدة = 40 / 2 = 20 صندوقاً في الساعة. المعدل الجديد بـ 3 آلات = 20 × 3 = 60 صندوقاً في الساعة.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '60 صندوقاً', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '50 صندوقاً', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '55 صندوقاً', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '65 صندوقاً', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-56', 5, 'MEDIUM', 'FULL', 'حساب الخصم', 'حساب القيمة النهائية بعد الخصم', 'استأجرت شركة عقارية فيلا بإيجار سنوي 180,000 درهم يُدفع على 4 دفعات متساوية. إذا رغب المستأجر في الدفع دفعة واحدة سنوية للحصول على خصم 5%، فما هو المبلغ الذي سيدفعه؟', 'الخصم يطبق على المبلغ السنوي الإجمالي.', 'الخصم = الإيجار السنوي × النسبة المئوية. المبلغ بعد الخصم = الإيجار السنوي - الخصم.', 'الخصم = 180,000 × 0.05 = 9,000 درهم. المبلغ بعد الخصم = 180,000 - 9,000 = 171,000 درهم.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '168,000 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '171,000 درهم', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '174,000 درهم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '176,000 درهم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-57', 5, 'HARD', 'FULL', 'حساب تكلفة الاستهلاك', 'حساب الاستهلاك للوحدة ثم التكلفة الإجمالية', 'تستهلك سيارة كهربائية 18 كيلوواط/ساعة لقطع 100 كم. إذا كانت تكلفة الكيلوواط/ساعة 0.35 درهم، فما هي تكلفة شحن السيارة لقطع 450 كم؟', 'نحتاج إلى حساب الاستهلاك للكيلومتر الواحد أولاً.', 'الاستهلاك لكل كم = الاستهلاك المعطى / المسافة المعطاة. الاستهلاك الإجمالي = الاستهلاك لكل كم × المسافة الجديدة. التكلفة = الاستهلاك الإجمالي × تكلفة الوحدة.', 'الاستهلاك لكل كم = 18 / 100 = 0.18 كيلوواط/ساعة. الاستهلاك لـ 450 كم = 0.18 × 450 = 81 كيلوواط/ساعة. التكلفة = 81 × 0.35 = 28.35 درهم.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '24.75 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '28.35 درهم', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '31.50 درهم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '36.00 درهم', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-58', 5, 'EASY', 'FULL', 'حساب نسبة النمو', 'حساب النسبة المئوية للزيادة', 'بلغت إيرادات شركة استيراد السيارات 2.4 مليون درهم في الربع الأول و3.0 مليون درهم في الربع الثاني. ما نسبة النمو بين الربعين؟', 'نحسب الفرق بين الإيرادات في الربعين.', 'الفرق = الإيرادات في الربع الثاني - الإيرادات في الربع الأول. نسبة النمو = الفرق / الإيرادات في الربع الأول.', 'الفرق = 3.0 - 2.4 = 0.6 مليون. نسبة النمو = 0.6 / 2.4 = 0.25 = 25%.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '20%', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '25%', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '30%', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '35%', 0, 4);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-NUM-59', 5, 'HARD', 'FULL', 'حساب الزيادة في التكلفة الجزئية', 'حساب التكلفة الجزئية ثم مقدار الزيادة عليها', 'في عملية بناء عمارة G+6، تبلغ نسبة تكلفة الهيكل الخرساني 40% من إجمالي تكلفة البناء البالغة 3,000,000 درهم. إذا ارتفعت أسعار الحديد والخرسانة بنسبة 15%، فما هي الزيادة في التكلفة الإجمالية للمشروع (بافتراض أن باقي التكاليف ثابتة)؟', 'نحسب تكلفة الهيكل الخرساني أولاً.', 'تكلفة الهيكل الخرساني = التكلفة الإجمالية × نسبة الهيكل. الزيادة = تكلفة الهيكل × نسبة الزيادة.', 'تكلفة الهيكل الخرساني = 3,000,000 × 0.40 = 1,200,000 درهم. الزيادة = 1,200,000 × 0.15 = 180,000 درهم.', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', '120,000 درهم', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', '150,000 درهم', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', '200,000 درهم', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', '180,000 درهم', 1, 4);

