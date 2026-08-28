SET NAMES utf8mb4;

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-16', 6, 'EASY', 'FULL', 'تحليل الاستنتاج من النص', 'مطابقة المعلومات الصريحة والمناقضة', 'منذ اعتماد كثير من الشركات لنظام العمل الهجين قبل ثلاث سنوات، لاحظ خبراء العقارات التجارية في المنطقة الوسطى من المدينة انخفاضاً في معدل الإشغال من 92% إلى 78%. في المقابل، ارتفع الطلب على المساحات المكتبية المرنة والمشتركة (Co-working) بنسبة تجاوزت 40% خلال نفس الفترة.

بناءً على الفقرة: انخفض الطلب على المساحات المكتبية المشتركة (Co-working) بنسبة 40% خلال السنوات الثلاث الماضية.', 'الفقرة تشير إلى أن الطلب على المساحات المشتركة "ارتفع" بنسبة تجاوزت 40%.', 'إذا كانت العبارة تعاكس المعلومات المذكورة صراحة في النص، فهي خاطئة.', 'بما أن العبارة تقول "انخفض" والنص يقول "ارتفع"، فالعبارة خاطئة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-17', 6, 'MEDIUM', 'FULL', 'تحليل الاستنتاج المستقبلي', 'استنتاج لا تدعمه المعطيات', 'منذ اعتماد كثير من الشركات لنظام العمل الهجين قبل ثلاث سنوات، لاحظ خبراء العقارات التجارية في المنطقة الوسطى من المدينة انخفاضاً في معدل الإشغال من 92% إلى 78%. في المقابل، ارتفع الطلب على المساحات المكتبية المرنة والمشتركة (Co-working) بنسبة تجاوزت 40% خلال نفس الفترة.

بناءً على الفقرة: ستعود معدلات إشغال المكاتب التقليدية إلى مستوياتها السابقة خلال العامين المقبلين.', 'النص يقدم معلومات تاريخية وحالية فقط عن معدل الإشغال ولا يذكر شيئًا عن المستقبل.', 'أي تنبؤ غير مدعوم بمعلومات في النص لا يمكن تأكيد صحته أو خطأه.', 'لا يمكن تحديد صحة العبارة المتعلقة بعودة معدلات الإشغال إلى مستوياتها السابقة.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 1, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-18', 6, 'EASY', 'FULL', 'الاستنتاج السببي', 'تأكيد علاقة السبب والنتيجة المباشرة', 'منذ اعتماد كثير من الشركات لنظام العمل الهجين قبل ثلاث سنوات، لاحظ خبراء العقارات التجارية في المنطقة الوسطى من المدينة انخفاضاً في معدل الإشغال من 92% إلى 78%. في المقابل، ارتفع الطلب على المساحات المكتبية المرنة والمشتركة (Co-working) بنسبة تجاوزت 40% خلال نفس الفترة.

بناءً على الفقرة: تسبب التوسع في العمل عن بُعد بانخفاض الطلب على المكاتب التجارية في المنطقة الوسطى من المدينة.', 'الفقرة تربط بوضوح بين "اعتماد نظام العمل الهجين" (وهو نوع من العمل عن بعد) و"انخفاض في معدل الإشغال".', 'الاستنتاج يطابق ما ورد في النص من علاقة سببية بين التغيير في نظام العمل وانخفاض الطلب.', 'العبارة تعكس بدقة المعطيات الواردة في النص وتعتبر صحيحة.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-19', 6, 'MEDIUM', 'FULL', 'الاستنتاج الكمي المباشر', 'حساب ومقارنة الأرقام من النص', 'يضم المصنع الجديد 850 موظفاً موزعين على أربعة أقسام رئيسية: الإنتاج، الجودة، اللوجستيات، والبحث والتطوير. يمثل قسم الإنتاج 55% من إجمالي الموظفين، بينما تتقاسم الأقسام الثلاثة الأخرى النسبة المتبقية (45%) بالتساوي تقريباً فيما بينها.

بناءً على الفقرة: عدد الموظفين العاملين في قسم البحث والتطوير أكبر من عدد العاملين في قسم الإنتاج.', 'قسم الإنتاج يمثل 55% بينما قسم البحث والتطوير يمثل ثلث النسبة المتبقية (أي 15%).', 'يجب مقارنة النسب المئوية الواردة صراحة في النص؛ قسم الإنتاج 55% مقارنة بقسم البحث والتطوير 15%.', 'قسم الإنتاج أكبر بكثير من قسم البحث والتطوير، لذا العبارة القائلة بأن قسم البحث والتطوير هو الأكبر تعتبر خاطئة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-20', 6, 'MEDIUM', 'FULL', 'الاستنتاج الكمي الدقيق', 'حساب رياضي مبني على معلومات النص', 'يضم المصنع الجديد 850 موظفاً موزعين على أربعة أقسام رئيسية: الإنتاج، الجودة، اللوجستيات، والبحث والتطوير. يمثل قسم الإنتاج 55% من إجمالي الموظفين، بينما تتقاسم الأقسام الثلاثة الأخرى النسبة المتبقية (45%) بالتساوي تقريباً فيما بينها.

بناءً على الفقرة: عدد موظفي قسم البحث والتطوير في المصنع يبلغ 127 موظفاً تقريباً.', 'النسبة المتبقية هي 45% وتُقسم بالتساوي على ثلاثة أقسام، بما فيها قسم البحث والتطوير.', 'لحساب عدد موظفي قسم البحث والتطوير: (850 × 45%) / 3.', '382.5 / 3 = 127.5، وهو ما يعادل 127 موظفًا تقريباً. العبارة صحيحة.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-21', 6, 'HARD', 'FULL', 'الاستنتاج من فئات مجتمعة', 'استنتاج غير ممكن لعدم توفر بيانات مفصلة', 'أشار استبيان حديث شمل 1200 أسرة في المدينة إلى أن 65% من العائلات تشتري احتياجاتها الأسبوعية من الخضار والفواكه من الأسواق المحلية، بينما اعتمد الباقون على السلاسل التجارية الكبرى ومنصات التوصيل عبر الإنترنت. أشار الاستبيان أيضاً إلى أن عامل السعر كان الدافع الأول لهذا التفضيل.

بناءً على الفقرة: عدد الأسر التي تعتمد على منصات التوصيل عبر الإنترنت أكبر من عدد الأسر التي تعتمد على السلاسل التجارية الكبرى.', 'الفقرة ذكرت أن "الباقون" (أي 35%) يعتمدون على السلاسل التجارية الكبرى ومنصات التوصيل دون تحديد نسبتها المنفردة.', 'إذا كانت الفئات مجتمعة ضمن نسبة واحدة دون تفصيل، فلا يمكن المقارنة بين مكوناتها.', 'لا يمكن تحديد أيهما أكبر من خلال النص، لذا الإجابة هي لا يمكن تحديد.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 1, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-22', 6, 'EASY', 'FULL', 'تأكيد معلومة صريحة', 'استنتاج مبني على نسبة الأغلبية', 'أشار استبيان حديث شمل 1200 أسرة في المدينة إلى أن 65% من العائلات تشتري احتياجاتها الأسبوعية من الخضار والفواكه من الأسواق المحلية، بينما اعتمد الباقون على السلاسل التجارية الكبرى ومنصات التوصيل عبر الإنترنت. أشار الاستبيان أيضاً إلى أن عامل السعر كان الدافع الأول لهذا التفضيل.

بناءً على الفقرة: يفضّل أغلب المستهلكين في المدينة شراء الخضار الطازجة من الأسواق المحلية بدلاً من السلاسل الكبرى.', 'النص يوضح أن 65% يعتمدون على الأسواق المحلية.', 'نسبة 65% تعتبر أغلبية واضحة مقارنة بـ 35% لبقية الخيارات.', 'بما أن الأغلبية تفضل الأسواق المحلية، فالعبارة صحيحة وتتطابق مع النص.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-23', 6, 'EASY', 'FULL', 'الاستنتاج السببي الخاطئ', 'تناقض صريح مع النص', 'أشار استبيان حديث شمل 1200 أسرة في المدينة إلى أن 65% من العائلات تشتري احتياجاتها الأسبوعية من الخضار والفواكه من الأسواق المحلية، بينما اعتمد الباقون على السلاسل التجارية الكبرى ومنصات التوصيل عبر الإنترنت. أشار الاستبيان أيضاً إلى أن عامل السعر كان الدافع الأول لهذا التفضيل.

بناءً على الفقرة: جودة المنتجات كانت الدافع الأول لتفضيل الأسواق المحلية بحسب الاستبيان.', 'النص يشير صراحة إلى أن "عامل السعر" هو الدافع الأول.', 'إذا ذكرت العبارة عاملاً مختلفاً عما ورد كسبب رئيسي، فهي خاطئة.', 'العبارة تعتبر جودة المنتجات هي الدافع الأول، وهو ما يعارض النص الذي يحدد السعر. العبارة خاطئة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-24', 6, 'EASY', 'FULL', 'تناقض المعلومات الزمنية', 'تحديد الخطأ في الأرقام أو الفترات المذكورة', 'أعلنت شركة تصنيع السيارات أن تطوير النموذج الأولي لسيارتها الكهربائية الجديدة استغرق 18 شهراً، مقارنة بالخطة الأصلية التي كانت تستهدف الإنجاز خلال 14 شهراً. أوضحت الشركة أن التأخير الأساسي نتج عن تعطل خطوط توريد المكونات الدقيقة للبطاريات من الموردين الآسيويين.

بناءً على الفقرة: كانت الخطة الأصلية لإنجاز النموذج الأولي تستهدف مدة 18 شهراً.', 'النص يقول بوضوح أن "الخطة الأصلية كانت تستهدف الإنجاز خلال 14 شهراً".', 'إذا أوردت العبارة رقماً مختلفاً عما ورد في النص بالنسبة للمعلومة المحددة، فهي خاطئة.', 'العبارة تدعي أن الخطة الأصلية 18 شهراً بينما النص يقول 14 شهراً. العبارة خاطئة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-25', 6, 'MEDIUM', 'FULL', 'توقع غير مدعوم بالبيانات', 'استنتاج خطط مستقبلية غير موجودة', 'أعلنت شركة تصنيع السيارات أن تطوير النموذج الأولي لسيارتها الكهربائية الجديدة استغرق 18 شهراً، مقارنة بالخطة الأصلية التي كانت تستهدف الإنجاز خلال 14 شهراً. أوضحت الشركة أن التأخير الأساسي نتج عن تعطل خطوط توريد المكونات الدقيقة للبطاريات من الموردين الآسيويين.

بناءً على الفقرة: ستتحول الشركة إلى موردين محليين للبطاريات في المستقبل لتجنب تكرار المشكلة.', 'النص يوضح سبب التأخير فقط (تعطل خطوط التوريد).', 'لا يمكننا استنتاج خطة الشركة المستقبلية ما لم تكن مصرحاً بها في النص.', 'العبارة تتكهن بتحول مستقبلي غير مذكور في النص. لا يمكن تحديد صحتها.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 1, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-26', 6, 'EASY', 'FULL', 'تأكيد العلاقة السببية', 'استنتاج صحيح مبني على ربط الحقائق في النص', 'أعلنت شركة تصنيع السيارات أن تطوير النموذج الأولي لسيارتها الكهربائية الجديدة استغرق 18 شهراً، مقارنة بالخطة الأصلية التي كانت تستهدف الإنجاز خلال 14 شهراً. أوضحت الشركة أن التأخير الأساسي نتج عن تعطل خطوط توريد المكونات الدقيقة للبطاريات من الموردين الآسيويين.

بناءً على الفقرة: استغرق تطوير النموذج الأولي للسيارة الكهربائية وقتاً أطول من المتوقع بسبب مشكلات في سلسلة توريد البطاريات.', 'النص يقول أن الإنجاز استغرق 18 شهرًا بدلاً من 14، وأن سبب التأخير كان "تعطل خطوط توريد المكونات الدقيقة للبطاريات".', 'العبارة تلخص ما ورد في النص من علاقة سببية (تأخير بسبب مشكلات في سلسلة التوريد).', 'العبارة متوافقة تمامًا مع النص ومستنتجة بشكل صحيح منه.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-27', 6, 'HARD', 'FULL', 'الاستنتاج الحسابي من النص اللفظي', 'استخراج وتأكيد نسبة مئوية', 'أظهرت بيانات قسم المبيعات في شركة تصنيع الأجهزة المنزلية أن إجمالي المبيعات خلال الربع الرابع من العام بلغ 4.48 مليون درهم، في حين كانت مبيعات الربع الثالث 4 ملايين درهم. عزت الشركة هذا النمو إلى إطلاق حملة تسويقية جديدة استهدفت المتاجر الإلكترونية بشكل خاص.

بناءً على الفقرة: زادت مبيعات الشركة الإلكترونية بنسبة 12% خلال الربع الأخير مقارنة بالربع السابق.', 'مبيعات الربع الثالث كانت 4 مليون درهم ومبيعات الربع الرابع 4.48 مليون درهم. الزيادة هي 0.48 مليون.', 'النسبة المئوية للزيادة = (مقدار الزيادة / القيمة الأصلية) × 100.', '0.48 / 4 = 0.12. إذن النسبة هي 12%. العبارة صحيحة لأنها تتطابق مع الحسابات المستندة إلى النص.', 'A', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 1, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-28', 6, 'EASY', 'FULL', 'تناقض مع معلومة محددة', 'تحديد الخطأ في هدف معلن', 'أظهرت بيانات قسم المبيعات في شركة تصنيع الأجهزة المنزلية أن إجمالي المبيعات خلال الربع الرابع من العام بلغ 4.48 مليون درهم، في حين كانت مبيعات الربع الثالث 4 ملايين درهم. عزت الشركة هذا النمو إلى إطلاق حملة تسويقية جديدة استهدفت المتاجر الإلكترونية بشكل خاص.

بناءً على الفقرة: كانت الحملة التسويقية الجديدة موجهة بشكل خاص للمتاجر التقليدية غير الإلكترونية.', 'النص يذكر بوضوح أن الحملة التسويقية استهدفت "المتاجر الإلكترونية بشكل خاص".', 'إذا كانت العبارة تنص على عكس أو غير المعلومة المذكورة، فهي خاطئة.', 'العبارة تشير إلى المتاجر التقليدية غير الإلكترونية، وهو نقيض ما ورد في النص، فالعبارة خاطئة.', 'B', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 1, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 0, 3);

INSERT INTO gcat_questions (item_code, subtest_id, difficulty, exam_mode, title_ar, pattern_type_ar, prompt_text_ar, observation_ar, rule_ar, application_ar, correct_option_key, is_active, exposure_count, created_at) VALUES ('GCAT-VER-29', 6, 'MEDIUM', 'FULL', 'التعميم وتوقع النمو المستقبلي', 'الاستنتاج بناءً على نمط ماضي دون دليل مستقبلي', 'أظهرت بيانات قسم المبيعات في شركة تصنيع الأجهزة المنزلية أن إجمالي المبيعات خلال الربع الرابع من العام بلغ 4.48 مليون درهم، في حين كانت مبيعات الربع الثالث 4 ملايين درهم. عزت الشركة هذا النمو إلى إطلاق حملة تسويقية جديدة استهدفت المتاجر الإلكترونية بشكل خاص.

بناءً على الفقرة: ستستمر الشركة في زيادة مبيعاتها بنسبة 12% في كل ربع قادم.', 'النص يناقش زيادة بنسبة 12% حدثت بين ربعين ماضيين (الثالث والرابع) فقط.', 'لا يمكن افتراض استمرار النمو بنفس النسبة في المستقبل دون معلومات داعمة في النص.', 'بما أن النص لا يحتوي على أي توقعات مستقبلية، فلا يمكن تحديد صحة هذا الاستنتاج.', 'C', 1, 0, NOW());
SET @question_id = LAST_INSERT_ID();
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'A', 'العبارة صحيحة بناء على الفقرة', 0, 1);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'B', 'العبارة خاطئة بناء على الفقرة', 0, 2);
INSERT INTO gcat_options (question_id, option_key, option_text_ar, is_correct, display_order) VALUES (@question_id, 'C', 'لا يمكن تحديد صحة العبارة من المعلومات الموجودة في الفقرة', 1, 3);

