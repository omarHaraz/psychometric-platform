SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-01', 4, 'EASY', 'FULL', 'دوران السهم بزاوية ثابتة', 'دوران منتظم (90 درجة)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ سلسلة من المربعات بداخلها سهم يتغير اتجاهه تباعاً: أعلى، يمين، أسفل، يسار.', 'يدور السهم بزاوية 90 درجة في اتجاه عقارب الساعة في كل خطوة متتالية.', 'لتحديد الشكل الناقص، نقوم بتدوير السهم الأخير (اليسار) بمقدار 90 درجة في اتجاه عقارب الساعة، ليصبح مشيراً إلى الأعلى (يطابق الخيار B).', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'سهم يشير إلى الأسفل', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'سهم يشير إلى الأعلى', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'سهم يشير إلى اليسار', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'سهم يشير إلى اليمين', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'سهم مائل', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-02', 4, 'MEDIUM', 'FULL', 'دورتان تعملان معاً', 'تغير مزدوج (الشكل والتعبئة)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'السلسلة تتكون من أشكال هندسية تتغير في نوعها (دائرة، مثلث، مربع) ولون تعبئتها (أسود صلب، أبيض مفرغ).', 'هناك دورتان تعملان بالتزامن: دورة الأشكال (دائرة -> مثلث -> مربع -> تتكرر للدائرة)، ودورة التعبئة (أسود -> أبيض -> أسود -> أبيض -> تتكرر للأسود).', 'الشكل الرابع هو دائرة بيضاء. بناءً على دورة الأشكال، التالي يجب أن يكون مثلثاً. وبناءً على دورة التعبئة، التالي يجب أن يكون أسود. إذن الشكل الناقص هو مثلث أسود ممتلئ (الخيار D).', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مثلث أبيض مفرغ يشير لليسار', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'مربع أسود', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'دائرة سوداء', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'مثلث أسود يشير لليسار', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'معين أسود', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-03', 4, 'EASY', 'FULL', 'إضافة عنصر واحد في كل خطوة', 'زيادة عددية منتظمة', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ سلسلة من المربعات تحتوي على نقاط سوداء يتزايد عددها تدريجياً: المربع الأول به نقطة، الثاني نقطتان، الثالث ثلاث، والرابع أربع نقاط.', 'القاعدة هي إضافة نقطة سوداء واحدة (+1) في كل خطوة، مع ترتيبها في صفوف أفقية.', 'بما أن المربع الرابع يحتوي على 4 نقاط، يجب أن يحتوي المربع الناقص على 5 نقاط (4 + 1 = 5). الخيار C هو الوحيد الذي يحتوي على خمس نقاط (أربع في الصف الأول ونقطة في الصف الثاني).', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'أربع نقاط سوداء', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'ست نقاط سوداء (صفان)', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'خمس نقاط سوداء (صفان)', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'ثلاث نقاط سوداء', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'نقطتان سوداوان', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-04', 4, 'EASY', 'FULL', 'انتقال العنصر في مسار دائري', 'حركة دورانية في الزوايا', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ وجود مربع أسود صغير يغير موقعه بين زوايا المربع الخارجي في كل خطوة.', 'المربع الصغير يتحرك بمقدار زاوية واحدة في اتجاه عقارب الساعة (أعلى يسار -> أعلى يمين -> أسفل يمين -> أسفل يسار).', 'بناءً على القاعدة، بعد وصول المربع إلى الزاوية السفلية اليسرى في الشكل الرابع، يجب أن يتحرك خطوة إضافية ليعود إلى الزاوية العلوية اليسرى في الشكل الخامس، وهذا يطابق الخيار A.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مربع أسود في الزاوية العلوية اليسرى', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'مربع أسود في الزاوية السفلية اليمنى', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'مربع أسود في المنتصف', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'مربع أسود في الزاوية العلوية اليمنى', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'مربع أسود في الزاوية السفلية اليسرى', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-05', 4, 'EASY', 'FULL', 'دوران الجزء المظلل', 'دوران منتظم (90 درجة)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ مربعاً مقسوماً إلى نصفين، أحدهما مظلل باللون الأسود. يتغير موقع النصف المظلل تباعاً في كل إطار.', 'يدور الجزء المظلل داخل المربع بمقدار 90 درجة في اتجاه عقارب الساعة في كل خطوة (أعلى -> يمين -> أسفل -> يسار).', 'في الشكل الرابع، يقع الجزء المظلل في النصف الأيسر. بتطبيق قاعدة الدوران (90 درجة مع عقارب الساعة)، سيعود الجزء المظلل إلى النصف العلوي في الشكل الخامس. هذا يطابق الخيار E.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مربع نصفه السفلي مظلل', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'مربع نصفه الأيسر مظلل', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'مربع نصفه الأيمن مظلل', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'مربع نصفه السفلي مظلل', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'مربع نصفه العلوي مظلل', 1, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-06', 4, 'EASY', 'FULL', 'تغير الشكل كل خطوتين', 'تغير الشكل والحجم بصورة دورية', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ سلسلة من الأشكال تتغير وفق نمط معين. الشكل الأول دائرة صغيرة، الثاني دائرة كبيرة، الثالث مربع صغير، الرابع مربع كبير.', 'القاعدة تتضمن أمرين: 1. الشكل الأساسي يستمر لإطارين متتاليين ثم يتغير (دائرة، دائرة، ثم مربع، مربع، إذن التالي يجب أن يكون شكلاً جديداً). 2. الحجم يتناوب بين الصغير والكبير في كل شكل (صغير -> كبير -> صغير -> كبير).', 'بما أن الإطارين الثالث والرابع كانا مربعين، فالإطار الخامس يجب أن يبدأ شكلاً جديداً. النمط لا يوضح ما هو الشكل الجديد بالتحديد، ولكن بالنظر إلى الخيارات، المثلث والمعين هما الشكلان الجديدان. القاعدة الثانية تنص على أن كل شكل جديد يبدأ بحجم صغير. الخيار الوحيد الذي يمثل شكلاً جديداً بحجم صغير هو الخيار B (المثلث الصغير).', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مثلث يشير لليسار', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'مثلث صغير يشير لليسار', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'دائرة صغيرة', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'معين', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'مربع صغير', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-07', 4, 'MEDIUM', 'FULL', 'دوران السهم مع تناقص الطول', 'تغير مزدوج (دوران وتناقص الحجم)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ سلسلة من المربعات تحتوي على سهم يتغير اتجاهه وطوله في كل خطوة.', 'توجد قاعدتان متزامنتان: 1) يدور السهم بمقدار 45 درجة في اتجاه عقارب الساعة في كل إطار متتالٍ. 2) يتناقص طول السهم تدريجياً بصورة منتظمة.', 'الشكل الرابع يحتوي على سهم يشير إلى أسفل اليمين. بتدويره 45 درجة إضافية مع عقارب الساعة سيشير إلى الأسفل مباشرة. ومع تطبيق قاعدة تناقص الطول، يجب أن يكون أقصر من السهم في الشكل الرابع. الخيار D هو الوحيد الذي يحقق الشرطين (يشير للأسفل وهو الأقصر).', 'D', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'سهم يشير إلى اليسار', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'سهم يشير إلى أعلى اليمين', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'سهم يشير إلى اليمين', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'سهم قصير يشير إلى الأسفل', 1, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'سهم يشير إلى أعلى اليسار', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-08', 4, 'EASY', 'FULL', 'انتقال دوري بين الصفوف', 'حركة دورية (انتقال رأسي)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ شكلاً مقسماً إلى أربعة صفوف أفقية، حيث يُظلل صف واحد باللون الأسود ويتغير موقعه في كل إطار.', 'ينتقل التظليل الأسود صفاً واحداً إلى الأسفل في كل خطوة متتالية. وعندما يصل إلى الصف الأخير (الرابع)، يعود مجدداً إلى الصف الأول في دورة مستمرة.', 'في الإطار الرابع، يقع التظليل في الصف الأخير. بناءً على القاعدة الدورية، في الخطوة التالية (الخامسة) يجب أن يقفز التظليل عائداً إلى الصف الأول في الأعلى، وهو ما يطابق الخيار C.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'شريط أسود في الصف الثالث', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'شريط أسود في الصف الرابع (السفلي)', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'شريط أسود في الصف الأول (العلوي)', 1, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'شريط أسود في الصف الثاني', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'النصف العلوي مظلل بالكامل (صفان)', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-09', 4, 'EASY', 'FULL', 'فصل الحجم عن نوع الشكل', 'تغير مستقل (الحجم والشكل)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'السلسلة تتكون من أشكال سوداء: دائرة صغيرة، دائرة كبيرة، مربع صغير، مربع كبير.', 'هناك قاعدتان تعملان بشكل مستقل: 1) يتناوب الحجم في كل خطوة (صغير -> كبير -> صغير -> كبير). 2) يستمر نفس الشكل لإطارين متتاليين ثم يتغير لشكل جديد (دائرة مرتين، ثم مربع مرتين).', 'بناءً على القاعدة الأولى، يجب أن يكون الشكل الخامس (صغيراً). وبناءً على القاعدة الثانية، يجب أن يكون (شكلاً جديداً) بعد انتهاء دورة المربع. الخيار A (مثلث صغير) هو الخيار الأنسب الذي يقدم شكلاً أساسياً جديداً بالحجم الصغير المطلوب.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مثلث أسود صغير يشير لليسار', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'مثلث أسود كبير يشير لليسار', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'معين أسود صغير', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'دائرة سوداء صغيرة', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'مربع أسود صغير', 0, 5);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-ABS-10', 4, 'EASY', 'FULL', 'تبديل موضعي بين عنصرين', 'تبادل مواقع (تناوب)', 'اختر الشكل الذي يكمل النمط وفق القاعدة الأكثر اتساقاً.', 'نلاحظ إطاراً يحتوي على عنصرين: دائرة سوداء ومربع أبيض.', 'يتبادل العنصران موقعيهما (يمين ويسار) في كل خطوة متتالية بصورة متناوبة ومستمرة.', 'في الإطار الرابع، المربع الأبيض على اليسار والدائرة السوداء على اليمين. بتطبيق قاعدة التبادل، يجب أن يعود الترتيب في الإطار الخامس إلى: الدائرة السوداء على اليسار والمربع الأبيض على اليمين. هذا يطابق الخيار E.', 'E', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'مربع أبيض يسار، دائرة سوداء يمين', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'دائرة سوداء كبيرة في المنتصف', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'مربع أبيض كبير في المنتصف', 0, 3);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'D', 'دائرة بيضاء يسار، مربع أسود يمين', 0, 4);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'E', 'دائرة سوداء يسار، مربع أبيض يمين', 1, 5);

