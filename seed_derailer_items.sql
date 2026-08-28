SET NAMES utf8mb4;

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما يزداد الضغط، أميل إلى تقليل تواصلي مع الآخرين حتى أرتب أفكاري وحدي.', 'تقيس سمة (التحفظ). الميل للانسحاب وتقليل التواصل تحت الضغط يعيق توجيه الفريق ويزيد من غموض الموقف.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('تؤثر الانتكاسات الصغيرة في مزاجي بدرجة تظهر على أسلوبي في العمل.', 'تقيس سمة (الانفعالية). التأثر السريع بالانتكاسات يدل على ضعف الاستقرار الانفعالي مما ينعكس سلباً على المعنويات وبيئة العمل.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('حتى تحت الضغط، أستطيع تحدي الآراء دون تحويل الخلاف إلى مواجهة شخصية.', 'تقيس القدرة على ضبط (العدائية). القائد الفعال يناقش الأفكار بموضوعية واحترافية دون تحويل الخلاف العملي إلى نزاع شخصي.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما يكون الوقت ضيقاً، أتخذ أحياناً قراراً قبل التحقق من المعلومات الأساسية.', 'تقيس سمة (الاندفاعية). التسرع في اتخاذ القرارات دون التحقق من البيانات يرفع نسبة المخاطر التشغيلية ويؤدي لأخطاء جسيمة.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أقبل تسليم عمل يحقق المستوى المطلوب دون الاستمرار في تحسين تفاصيل غير جوهرية.', 'تقيس القدرة على ضبط (الصرامة). الإدارة الفعالة تتطلب الموازنة بين جودة المخرجات والالتزام بالوقت دون الغوص في مثالية مفرطة معطلة.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('تحت الضغط، أطرح حلولاً غير مألوفة دون أن أوضح دائماً منطقها للآخرين.', 'تقيس سمة (اللامألوفية). طرح أفكار غريبة دون توضيح منطقها يسبب ارتباكاً للفريق ويضعف الثقة في القرارات الإدارية.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما تتعقد الظروف، أحافظ على تواصل واضح مع الفريق حتى لو احتجت إلى وقت للتفكير.', 'تقيس القدرة على ضبط (التحفظ). الحفاظ على قنوات التواصل مفتوحة أثناء الأزمات يبث الطمأنينة ويمنع انتشار الشائعات.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('بعد خبر غير متوقع، أستعيد اتزاني بسرعة قبل أن أتعامل مع الفريق.', 'تقيس القدرة على ضبط (الانفعالية). التعافي السريع من المفاجآت يضمن استقرار القيادة وعدم انتقال التوتر لباقي المستويات.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما تعوقني جهة أخرى، يصبح أسلوبي أكثر حدة مما يتطلبه الموقف.', 'تقيس سمة (العدائية). الحدة والمواجهة الهجومية عند التعرض للعوائق تدمر العلاقات المهنية وتعيق الشراكات الاستراتيجية.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('حتى في المواقف العاجلة، أتوقف للتأكد من المخاطر الرئيسة قبل التصرف.', 'تقيس القدرة على ضبط (الاندفاعية). التروي المدروس لدراسة المخاطر يقي المؤسسة من عواقب القرارات الارتجالية المتسرعة.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('يصعب علي تفويض مهمة إذا لم أكن واثقاً أن الشخص سينفذها بالطريقة التي أفضلها.', 'تقيس سمة (الصرامة). الإدارة التفصيلية (Micromanagement) والمثالية المفرطة تعيق التفويض وتحد من بناء وتطوير قدرات الفريق.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما أقترح فكرة غير تقليدية، أربطها بالهدف والقيود حتى يفهمها الآخرون.', 'تقيس القدرة على ضبط (اللامألوفية). التأطير المنطقي للأفكار المبتكرة يسهل تقبلها ويحولها من مجرد شطحات إلى خطط قابلة للتنفيذ.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عند تعرضي للنقد، أفضل الانسحاب من النقاش بدلاً من توضيح موقفي فوراً.', 'تقيس سمة (التحفظ). التهرب أو الانسحاب الانطوائي عند النقد يضعف الحضور القيادي ويفوت فرص التقويم المؤسسي.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما تتراكم الضغوط، أركز على الاحتمالات السلبية أكثر من الحلول المتاحة.', 'تقيس سمة (الانفعالية). التشاؤم وتوقع السيناريوهات الأسوأ تحت الضغط يستهلك طاقة الفريق ويشتت الانتباه عن إيجاد الحلول.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('في المنافسة، أضغط لتحقيق النتيجة حتى لو توترت العلاقات المهنية.', 'تقيس سمة (العدائية). التركيز المفرط على الفوز بأي ثمن قد يؤدي إلى تدمير النسيج الاجتماعي والثقة المتبادلة داخل المؤسسة.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أبدأ أحياناً إجراءات جديدة تحت الضغط قبل أن أغلق الأعمال السابقة.', 'تقيس سمة (الاندفاعية). تشتت الجهود وإطلاق المبادرات دون إغلاق الملفات السابقة يسبب فوضى تشغيلية وهدراً للموارد.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أراجع العمل مرات إضافية رغم أن معايير القبول قد تحققت.', 'تقيس سمة (الصرامة). الهوس بالتفاصيل يعطل الإنجاز ويهدر وقت المؤسسة دون إضافة قيمة حقيقية تتناسب مع الجهد المبذول.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('قد أغير اتجاه النقاش إلى فكرة جديدة قبل أن يكتمل بحث الموضوع الحالي.', 'تقيس سمة (اللامألوفية). القفز العشوائي بين الأفكار يشتت الانتباه ويمنع الوصول إلى قرارات حاسمة في الاجتماعات.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('في الأزمات، أبقى متاحاً للآخرين وأوضح ما أعرفه وما لا أعرفه.', 'تقيس القدرة على ضبط (التحفظ). الشفافية والتواجد الميداني أثناء الأزمات يبني الثقة ويمنع حالة التخبط والغموض.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أفصل بين انفعالي الشخصي والقرار المطلوب عندما أتعرض لضغط مفاجئ.', 'تقيس القدرة على ضبط (الانفعالية). الموضوعية والتحكم في الانفعالات هي ركيزة اتخاذ القرارات الصائبة في الأوقات الحرجة.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('عندما يفشل شخص في تلبية توقعاتي، أركز على الوقائع والإجراء التصحيحي دون التقليل منه.', 'تقيس القدرة على ضبط (العدائية). التركيز على المشكلة وليس الشخص يعزز المساءلة الإيجابية ويحافظ على كرامة الموظف.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحافظ على متابعة الالتزامات حتى عندما تتغير الأولويات بسرعة.', 'تقيس القدرة على ضبط (الاندفاعية). الانضباط والمتابعة يضمنان عدم ضياع المهام الأساسية وسط زحام المتغيرات والمستجدات.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أعدل أسلوبي عندما تصبح القاعدة أو الخطة غير مناسبة للظرف، ضمن الصلاحيات.', 'تقيس القدرة على ضبط (الصرامة). المرونة في تعديل الخطط والإجراءات تضمن استمرارية الأعمال بفعالية عند تغير المعطيات.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أختبر قبول الفكرة الجديدة على نطاق محدود قبل تعميمها.', 'تقيس القدرة على ضبط (اللامألوفية). المنهجية التجريبية تقلل من صدمة التغيير وتضمن واقعية الأفكار المبتكرة قبل تعميمها.', 5, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('تحت الضغط، قد أبدو بعيداً أو غير مهتم رغم أنني أركز على حل المشكلة.', 'تقيس سمة (التحفظ). الانسحاب العاطفي أو الذهني يعطي انطباعاً سلبياً باللامبالاة ويترك الفريق دون توجيه صريح.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 1);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أحتاج وقتاً طويلاً لاستعادة ثقتي بعد انتكاسة مهنية.', 'تقيس سمة (الانفعالية). الهشاشة النفسية وبطء التعافي يعيق مسيرة القائد ويجعله متردداً في اتخاذ قرارات حاسمة مستقبلاً.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 2);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('إذا اعتقدت أن هدفي صحيح، قد أتجاهل اعتراضات الآخرين على أسلوبي.', 'تقيس سمة (العدائية). التفرد بالرأي وتجاهل المخاوف المشروعة للفريق يخلق بيئة عمل استبدادية ترفع معدلات مقاومة التغيير.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أغير الأولويات بسرعة بناءً على آخر طلب عاجل دون مراجعة الأثر على الالتزامات الأخرى.', 'تقيس سمة (الاندفاعية). الاستجابة اللحظية للمستجدات دون تقييم شمولي تدمر الخطط الاستراتيجية وتخلق بيئة إطفاء حرائق دائمة.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 4);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أشعر بعدم الارتياح عندما لا أملك سيطرة مباشرة على تفاصيل التنفيذ.', 'تقيس سمة (الصرامة). المركزية الشديدة وعدم تقبل تفويض الصلاحيات يؤدي إلى اختناق العمليات الإدارية (Bottleneck).', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 5);

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('تحت الضغط، قد أستخدم أسلوباً أو فكرة يصعب على الآخرين توقعها أو متابعتها.', 'تقيس سمة (اللامألوفية). التقلب وعدم القدرة على التنبؤ بسلوك القائد (Unpredictability) يزعزع الاستقرار والأمان النفسي للفريق.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 6);

