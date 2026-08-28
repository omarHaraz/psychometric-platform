SET NAMES utf8mb4;

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أتجاهل المواعيد النهائية إذا شعرت أن العمل يحتاج إلى مزيد من المراجعة الدقيقة.', 'تقيس سمة (الصرامة). الإفراط في المثالية على حساب الوقت يعطل العمليات التشغيلية ويؤدي إلى اختناقات (Bottleneck).', 2, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أستطيع تقبل توجيهات من أشخاص أقل مني خبرة دون الشعور بالاستياء.', 'تقيس القدرة على ضبط (العدائية). القائد المتزن يتقبل المعرفة من أي مصدر دون كبر أو هجوم.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحياناً أتحدث بما أفكر فيه فوراً، ثم أدرك لاحقاً أنه كان علي صياغة كلامي بشكل أفضل.', 'تقيس سمة (الاندفاعية). درجة متوسطة من الاندفاع قد تحدث أحياناً، ولكن يجب السيطرة عليها لعدم تفاقمها.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('في الاجتماعات العاصفة، أشارك برأيي وأطرح الأسئلة بدلاً من الصمت والمراقبة فقط.', 'تقيس القدرة على ضبط (التحفظ). الحضور والتفاعل يمنع ترك الساحة للتخبط ويوجه الفريق.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أفقد حماسي للعمل بالكامل إذا تلقيت تقييماً سلبياً من الإدارة العليا.', 'تقيس سمة (الانفعالية). التأثر الشديد بالتقييم يعكس هشاشة نفسية تؤثر على استمرارية الإنجاز.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما أقدم حلاً جديداً كلياً، أحرص على وضع خطة تجريبية لتوضيح فكرته للآخرين.', 'تقيس القدرة على ضبط (اللامألوفية). تقريب الأفكار المعقدة وتجربتها يقلل من مقاومة التغيير.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أصر على تطبيق نفس الإجراءات القديمة حتى عندما تثبت الإحصائيات عدم كفاءتها حالياً.', 'تقيس سمة (الصرامة). الجمود وعدم تقبل التغيير الإجرائي يقتل فرص الابتكار (Status Quo) ويقلل الإنتاجية.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحافظ على نبرة صوت هادئة ومحترفة حتى عندما يرفع الطرف الآخر صوته في النقاش.', 'تقيس القدرة على ضبط (العدائية). الثبات الانفعالي وعدم الانجرار للاستفزاز هو سمة القائد الناضج.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أرسل رسائل بريد إلكتروني حادة لزملائي عندما أكتشف خطأ مفاجئاً في العمل.', 'تقيس سمة (الاندفاعية). ردود الأفعال الكتابية السريعة والمنفعلة تدمر بيئة العمل وتترك أثراً سلبياً موثقاً.', 2, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحياناً أتجنب النقاشات الاجتماعية خارج نطاق العمل الرسمي مع أعضاء فريقي.', 'تقيس سمة (التحفظ). مساحة من العزلة أحياناً مقبولة، لكن يجب ألا تتحول لانفصال كامل عن الفريق.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أتقبل التغييرات المفاجئة في ميزانية المشروع دون أن يؤثر ذلك على استقراري النفسي.', 'تقيس القدرة على ضبط (الانفعالية). تقبل المتغيرات برحابة صدر يضمن استمرار الإنتاجية.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أتعمد استخدام أساليب غامضة أو غير مفهومة في الإدارة لأجعل الآخرين يعتمدون علي أكثر.', 'تقيس سمة (اللامألوفية). الغموض المتعمد يعيق شفافية المؤسسة ويخلق بيئة عمل غير صحية.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أستطيع تجاوز بعض الأخطاء الشكلية البسيطة إذا كان جوهر العمل ممتازاً ويحقق الهدف.', 'تقيس القدرة على ضبط (الصرامة). التركيز على الجوهر بدلاً من الإدارة التفصيلية (Micromanagement) يسرع الإنجاز.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحياناً أميل للعمل بمفردي تماماً لعدة أيام لإنجاز المهام المعقدة.', 'تقيس سمة (التحفظ). الانعزال المؤقت لإنجاز مهام تحتاج تركيزاً هو سلوك مقبول إذا لم يضر بالتواصل الأساسي.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أوافق فوراً على تولي مشاريع ضخمة قبل التأكد من توافر الموارد والوقت الكافي لفريقي.', 'تقيس سمة (الاندفاعية). الحماس الزائد والموافقة السريعة دون دراسة الموارد يورط المؤسسة ويحرق الفريق.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أبادر بطرح حلول ومقترحات في الاجتماعات الإدارية حتى وإن لم أكن رئيس الجلسة.', 'تقيس القدرة على ضبط (التحفظ). المبادرة الإيجابية تكسر العزلة وتثري النقاش.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أستطيع الفصل التام بين مشاكلي الشخصية وأدائي المهني داخل المؤسسة.', 'تقيس القدرة على ضبط (الانفعالية). النضج النفسي يتطلب عدم إسقاط المشاكل الخارجية على العمليات الداخلية.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أصر على تجاهل الهيكل التنظيمي للمؤسسة وتخطي المديرين بحجة تسريع العمل.', 'تقيس سمة (اللامألوفية) وتحدي الأعراف. كسر التسلسل الإداري بشكل متكرر يخلق فوضى تنظيمية.', 2, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحياناً أجد صعوبة في تقبل التغييرات التقنية الجديدة وأفضل الأنظمة القديمة.', 'تقيس سمة (الصرامة). مقاومة التغيير قد تحدث أحياناً بسبب التعود، ولكن يجب ألا تعيق التحول الرقمي.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما يطرح شخص فكرة تتعارض مع رأيي، أتعمد إحراجه أمام باقي أعضاء الفريق.', 'تقيس سمة (العدائية). إحراج الآخرين سلوك عدائي يدمر الأمان النفسي (Psychological Safety) ويمنع العصف الذهني.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أمنح نفسي وقتاً قبل الرد على أي بريد إلكتروني أو طلب يستفزني بشدة.', 'تقيس القدرة على ضبط (الاندفاعية). التروي وكبح جماح الغضب اللحظي يحمي القائد من القرارات الكارثية.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أكتفي بقراءة تقارير العمل وأتجنب النزول للميدان أو التحدث مباشرة مع الموظفين.', 'تقيس سمة (التحفظ). القيادة من خلف المكاتب تعزل القائد عن الواقع وتخلق فجوة مع فرق العمل.', 2, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('تظهر علامات التوتر على وجهي بوضوح عندما نقترب من المواعيد النهائية للمشاريع.', 'تقيس سمة (الانفعالية). إظهار التوتر أحياناً طبيعة بشرية، ولكن يجب ألا ينتقل كذعر (Panic) للفريق.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما أتبنى منهجية عمل غير تقليدية، أحرص على تدريب فريقي عليها خطوة بخطوة.', 'تقيس القدرة على ضبط (اللامألوفية). نقل المعرفة وتدريب الفريق يمنع العزلة الفكرية ويوحد جهود الإدارة.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أرفض أي اقتراح من فريقي إذا كان يتعارض مع الخطة الأصلية حتى لو كان سيوفر التكاليف.', 'تقيس سمة (الصرامة). التمسك الأعمى بالخطة ورفض التحسينات يعكس استبداداً إدارياً ويضيع الموارد.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحرص على استخدام عبارات الشكر والتقدير حتى عند توجيه ملاحظات تصحيحية قوية.', 'تقيس القدرة على ضبط (العدائية). التغليف الإيجابي للنقد يخفف من حدته ويحوله لتوجيه بناء (Constructive Feedback).', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحياناً أقاطع المتحدثين في الاجتماعات إذا شعرت أنهم يبتعدون عن الموضوع الرئيسي.', 'تقيس سمة (الاندفاعية). رغم أنها نوع من الاندفاع، إلا أن ضبط إيقاع الاجتماع أحياناً مبرر إدارياً.', 3, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أغلق باب مكتبي معظم ساعات العمل وأطلب من الموظفين عدم إزعاجي إلا في الكوارث.', 'تقيس سمة (التحفظ). سياسة الباب المغلق تقتل التواصل، وتمنع الاكتشاف المبكر للمشكلات.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أستوعب غضب العملاء أو رؤسائي بامتصاص الصدمة دون أخذ الأمر بشكل شخصي.', 'تقيس القدرة على ضبط (الانفعالية). المرونة النفسية (Resilience) في التعامل مع الغضب الخارجي هي ركيزة الذكاء العاطفي.', 4, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أشارك أفكاري المبتكرة مع الزملاء لأخذ رأيهم قبل أن أبدأ في تطبيقها بشكل فعلي.', 'تقيس القدرة على ضبط (اللامألوفية). الشورى وتلقي التغذية الراجعة يضمنان واقعية الأفكار ويقللان الانفراد الغريب.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

