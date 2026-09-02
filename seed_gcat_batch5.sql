SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-01', 6, 'EASY', 'FULL', 'تحديد المرادف', 'مرادف الكلمة المباشر', 'اختر الكلمة الأقرب في المعنى لكلمة: "واضح"', 'الكلمة المستهدفة هي "واضح"، وتعني الشيء البين الظاهر.', 'نبحث في الخيارات عن الكلمة التي تحمل نفس المعنى (المرادف).', 'كلمة "جلي" تعني "واضح ومكشوف"، وهي المرادف الدقيق.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'غامض', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'جلي', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'بعيد', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'مختلف', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'معقد', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-02', 6, 'EASY', 'FULL', 'تحديد المرادف', 'مرادف الكلمة المباشر', 'كلمة "يستفز" لديها معنى مشابه لـ:', 'الكلمة المستهدفة هي "يستفز"، وتعني إثارة المشاعر السلبية.', 'نبحث في الخيارات عن الكلمة التي تعبر عن إثارة الانفعال.', 'كلمة "يغضب" تعبر عن إثارة الغضب والانفعال، وهي الأقرب لمعنى يستفز.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'يغضب', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'يصد', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'يبط', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'يقابل', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'يبشر', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-03', 6, 'EASY', 'FULL', 'تحديد المرادف', 'مرادف الكلمة المباشر', 'اختر الكلمة الأقرب في المعنى لكلمة: "سريع"', 'الكلمة المستهدفة هي "سريع"، وتعني ما يحدث في وقت قصير.', 'نبحث في الخيارات عن الكلمة التي تدل على السرعة أو قصر الوقت.', 'كلمة "عاجل" تستخدم لوصف الأمور التي تتطلب سرعة في التنفيذ، فهي مرادف مناسب.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'بطيء', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'عاجل', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'ضعيف', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'بعيد', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'ثقيل', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-04', 6, 'MEDIUM', 'FULL', 'تحديد المرادف', 'مرادف الكلمة في السياق', 'اختر الكلمة الأقرب في المعنى لكلمة: "حاسم"', 'الكلمة المستهدفة هي "حاسم"، وتستخدم لوصف القرارات أو المواقف التي تنهي الجدل.', 'نبحث في الخيارات عن الكلمة التي تفيد معنى البت والإنهاء.', 'كلمة "نهائي" تعطي نفس دلالة "حاسم" في إنهاء الأمور.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'قوي', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'نهائي', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'كبير', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'سريع', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'واضح', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-05', 6, 'MEDIUM', 'FULL', 'تحديد المرادف', 'مرادف الكلمة في السياق', 'اختر الكلمة الأقرب في المعنى لكلمة: "دقيق"', 'الكلمة المستهدفة هي "دقيق"، وتعني المتقن أو الخالي من الأخطاء.', 'نبحث في الخيارات عن الكلمة التي ترتبط بالصحة والدقة.', 'كلمة "صحيح" هي الأقرب لمعنى "دقيق" في سياق صحة المعلومات أو الإجراءات.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'واضح', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'صحيح', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'كبير', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'قريب', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'بسيط', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-06', 6, 'EASY', 'FULL', 'تحديد التضاد', 'عكس الكلمة المباشر', 'اختر الكلمة التي تحمل عكس معنى كلمة: "سريع"', 'الكلمة المستهدفة هي "سريع".', 'المطلوب هو إيجاد الكلمة المضادة (عكس المعنى).', 'عكس السرعة هو البطء، لذا كلمة "بطيء" هي التضاد الصحيح.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'عاجل', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'فوري', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'بطيء', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'نشيط', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'قوي', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-07', 6, 'EASY', 'FULL', 'تحديد التضاد', 'عكس الكلمة المباشر', 'اختر الكلمة التي تحمل عكس معنى كلمة: "مرن"', 'الكلمة المستهدفة هي "مرن"، وتعني القابل للانثناء أو التكيف.', 'نبحث عن الكلمة التي تفيد الجمود وعدم القابلية للتغير.', 'كلمة "صلب" هي العكس المباشر لـ "مرن".', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'لين', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'سهل', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'صلب', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'واضح', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'متين', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-08', 6, 'EASY', 'FULL', 'تحديد التضاد', 'عكس الكلمة المباشر', 'اختر الكلمة التي تحمل عكس معنى كلمة: "واضح"', 'الكلمة المستهدفة هي "واضح".', 'المطلوب إيجاد الكلمة التي تعني الخفاء وعدم الوضوح.', 'كلمة "غامض" تعبر عن الشيء غير المفهوم أو غير الواضح، فهي التضاد.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'جلي', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'صريح', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'غامض', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'قريب', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'دقيق', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-09', 6, 'EASY', 'FULL', 'تحديد التضاد', 'عكس الكلمة المباشر', 'اختر الكلمة التي تحمل عكس معنى كلمة: "قوي"', 'الكلمة المستهدفة هي "قوي".', 'نبحث عن العكس المباشر للقوة.', 'الضعف هو نقيض القوة، إذن "ضعيف" هي الإجابة الصحيحة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'متين', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'ضعيف', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'كبير', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'سريع', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'ثابت', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-10', 6, 'EASY', 'FULL', 'تحديد التضاد', 'عكس الكلمة المباشر', 'اختر الكلمة التي تحمل عكس معنى كلمة: "كبير"', 'الكلمة المستهدفة هي "كبير".', 'نبحث عن الكلمة التي تدل على الحجم الأقل.', 'كلمة "صغير" هي العكس الواضح والمباشر لكلمة "كبير".', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'واسع', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'ضخم', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'صغير', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'قوي', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'عالي', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-11', 6, 'MEDIUM', 'FULL', 'حدّد الزوج الذي تربطه بالكلمتين العلاقة نفسها.', 'علاقة التضاد الاتجاهي', 'ما الاختيار الأكثر تقارباً مع المفردات التالية من حيث العلاقة والارتباط؟

أعلى : أسفل', 'العلاقة بين "أعلى" و "أسفل" هي علاقة تضاد في الاتجاهات المكانية.', 'يجب البحث في الخيارات عن زوج كلمات يمثلان تضاداً في الاتجاهات.', 'الزوج "يسار : يمين" يمثل اتجاهين متضادين مكانياً، مماثل للعلاقة في السؤال.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'سطع : مشرق', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'يسار : يمين', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'سريع : فوري', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'عبير : أريج', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'قوي : شديد', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-12', 6, 'EASY', 'FULL', 'حدّد الزوج الذي تربطه بالكلمتين العلاقة نفسها.', 'علاقة المهنة بمكان العمل', 'طبيب : مستشفى = ؟', 'العلاقة بين "طبيب" و "مستشفى" هي علاقة المهني بمكان عمله الرئيسي.', 'نبحث عن خيار يربط بين مهنة والمكان المخصص لممارستها.', 'الزوج "معلم : مدرسة" يطابق هذه العلاقة تماماً.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'معلم : مدرسة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'طيار : سيارة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'مهندس : كتاب', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'لاعب : قلم', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'سائق : طريق', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-13', 6, 'EASY', 'FULL', 'حدّد الزوج الذي تربطه بالكلمتين العلاقة نفسها.', 'علاقة الأداة بوظيفتها', 'قلم : كتابة = ؟', 'العلاقة بين "قلم" و "كتابة" هي علاقة الأداة بوظيفتها الأساسية.', 'نبحث عن زوج كلمات يربط أداة معينة بالغرض المخصص لها.', 'الزوج "سكين : قطع" يمثل الأداة (السكين) ووظيفتها (القطع).', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مفتاح : باب', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'سكين : قطع', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'سيارة : طريق', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'عين : نظر', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'ساعة : وقت', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-14', 6, 'MEDIUM', 'FULL', 'حدّد الزوج الذي تربطه بالكلمتين العلاقة نفسها.', 'علاقة الظرف بخصيصته الملازمة', 'ليل : ظلام = ؟', 'العلاقة بين "ليل" و "ظلام" هي علاقة اقتران؛ الظلام هو السمة المميزة لليل.', 'نبحث عن خيار يربط زمناً أو فصلاً بظاهرة أو سمة تلازمه غالباً.', 'الزوج "شتاء : برد" يمثل فصلاً مرتبطاً بظاهرة (البرد)، وهي العلاقة الأقرب. (ملاحظة: "نهار: شمس" غير دقيقة لأن الشمس هي المصدر وليس السمة كـ "الضياء").', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'نهار : شمس', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'شتاء : برد', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'صيف : حرارة', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'نور : ضوء', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'صباح : ضباب', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-15', 6, 'EASY', 'FULL', 'حدّد الزوج الذي تربطه بالكلمتين العلاقة نفسها.', 'علاقة الأداة/المفعول بالنشاط', 'كتاب : قراءة = ؟', 'هناك خطأ في مفتاح الإجابة الأصلي الذي يشير إلى (ج. قلم : كتابة). العلاقة الأصلية (كتاب : قراءة) هي علاقة (شيء يقع عليه الفعل : الفعل). بينما (قلم : كتابة) هي (أداة : فعل).', 'بما أن الإجابة المطلوبة بحسب المفتاح هي (قلم : كتابة)، فإننا نعتمدها في التكوين، ولكن كعلاقة عامة تربط (شيء متعلق بعملية قرائية/كتابية).', 'بناءً على مفتاح الإجابات الوارد، الزوج الصحيح هو "قلم : كتابة".', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'طعام : أكل', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'ماء : شرب', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'قلم : كتابة', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'باب : فتح', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'هاتف : اتصال', 0, 5);

