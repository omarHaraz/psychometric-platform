import json

new_json_data = """[
  {
    "item_code": "SJT-16",
    "sjt_domain": "DECISION_INTEGRITY",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "توزيع موارد محدودة على عدة مشاريع",
    "narrative_ar": "لديك موارد محدودة وثلاثة مشاريع تتنافس عليها. لكل مشروع قيمة وأثر ومخاطر مختلفة.",
    "options": {
      "A": {
        "text": "توزيع الموارد بالتساوي بين المشاريع حتى لا تشعر أي جهة بعدم الإنصاف.",
        "score": 2
      },
      "B": {
        "text": "استخدام مصفوفة شفافة للأثر والاستعجال والمخاطر والاعتماديات، ثم تخصيص الموارد ووضع نقاط مراجعة.",
        "score": 5
      },
      "C": {
        "text": "منح الموارد للمشروع الأكثر ظهوراً أمام الإدارة العليا.",
        "score": 1
      },
      "D": {
        "text": "استشارة قادة المشاريع، وتخصيص الموارد حسب الأولويات، والإبقاء على احتياطي صغير للمستجدات.",
        "score": 4
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) هو الأكثر منهجية وشفافية، بينما الخيار (D) قوي لكنه أقل تحديداً في معايير القرار.",
      "common_mistake": "اعتبار التوزيع المتساوي عادلاً رغم اختلاف الأولويات.",
      "coaching_note": "العدالة في تخصيص الموارد تعني وضوح المعايير، لا المساواة الآلية."
    }
  },
  {
    "item_code": "SJT-17",
    "sjt_domain": "CHANGE_ADAPTATION",
    "complexity": "DIRECT",
    "exam_mode": "FULL",
    "scenario_title": "شائعة عن إعادة هيكلة محتملة",
    "narrative_ar": "انتشرت شائعة عن إعادة هيكلة، ولا توجد معلومات رسمية يمكنك إعلانها. بدأ القلق يؤثر في التركيز.",
    "options": {
      "A": {
        "text": "مشاركة ما سمعته بصورة غير رسمية حتى يشعر الفريق أنك صريح معهم.",
        "score": 1
      },
      "B": {
        "text": "منع الحديث عن الموضوع والتحذير من تكرار الشائعات.",
        "score": 2
      },
      "C": {
        "text": "توضيح أنه لا توجد معلومات مؤكدة يمكن مشاركتها، وتجنب التكهن، وتوجيه الفريق إلى القنوات الرسمية ومواصلة تحديثهم عند توفر معلومة.",
        "score": 5
      },
      "D": {
        "text": "إبلاغ الموارد البشرية أو الإدارة بأثر الغموض وطلب تواصل رسمي في أقرب وقت.",
        "score": 4
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يقدم صدقاً دون كشف غير مصرح أو تكهن، و (D) يعالج الحاجة التنظيمية للتواصل.",
      "common_mistake": "اعتبار مشاركة الشائعة شفافية، أو إسكات القلق بالسلطة.",
      "coaching_note": "الشفافية لا تعني قول ما لا تعرفه؛ بل توضيح حدود المعرفة وما سيحدث تالياً."
    }
  },
  {
    "item_code": "SJT-18",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "تكرار أخطاء التسليم بين فرق متعددة",
    "narrative_ar": "يتأخر مشروع مشترك بسبب أخطاء متكررة عند انتقال العمل من فريق إلى آخر. كل فريق يرى أن المشكلة في الطرف الآخر.",
    "options": {
      "A": {
        "text": "إرسال رسالة تذكير للجميع بأهمية التعاون والالتزام بالمواعيد.",
        "score": 2
      },
      "B": {
        "text": "رسم سير العمل، وتحديد مالك كل تسليم ومعايير القبول ونقاط التحكم بين الفرق.",
        "score": 5
      },
      "C": {
        "text": "تكليف فريق واحد بإدارة جميع عمليات التنسيق نيابة عن بقية الفرق.",
        "score": 3
      },
      "D": {
        "text": "إنشاء لوحة متابعة مشتركة واجتماع تنسيق قصير لمعالجة التعثرات قبل موعد التسليم.",
        "score": 4
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يعالج البنية الجذرية للتسليم، و (D) يوفر آلية متابعة مناسبة بعد توضيح البنية.",
      "common_mistake": "محاولة حل خلل العملية برسائل عامة عن التعاون.",
      "coaching_note": "عند تعثر التنسيق، افحص الواجهة والمعيار والملكية قبل لوم الفرق."
    }
  },
  {
    "item_code": "SJT-19",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "DIRECT",
    "exam_mode": "FULL",
    "scenario_title": "توتر متكرر بين عضوين",
    "narrative_ar": "يتكرر التوتر بين عضوين، وبدأ يظهر في الاجتماعات ويؤثر في التعاون.",
    "options": {
      "A": {
        "text": "فصل العضوين بصورة دائمة في مسارات عمل مختلفة.",
        "score": 2
      },
      "B": {
        "text": "الحديث مع كل طرف منفرداً، ثم إدارة جلسة مشتركة تركز على الوقائع والأثر والاتفاقات السلوكية والمتابعة.",
        "score": 5
      },
      "C": {
        "text": "تجاهل الخلاف ما دام كل منهما ينجز مهامه الأساسية.",
        "score": 1
      },
      "D": {
        "text": "تذكير الفريق كله بقواعد الاحترام من دون معالجة الخلاف مباشرة.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يعالج الأسباب والتصورات، ويحولها إلى اتفاقات قابلة للمتابعة.",
      "common_mistake": "الهروب من الخلاف بالفصل، أو الاكتفاء برسالة عامة.",
      "coaching_note": "الحل المستدام يواجه الخلاف بأمان واحترام، لا يخفيه."
    }
  },
  {
    "item_code": "SJT-20",
    "sjt_domain": "CRISIS_RESOLUTION",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "طلبات عاجلة متزامنة",
    "narrative_ar": "تلقيت في الوقت نفسه عدة طلبات عاجلة من جهات مختلفة، وكل جهة تصف طلبها بأنه أولوية. لا يمكن لفريقك إنجازها جميعاً في المواعيد المطلوبة.",
    "options": {
      "A": {
        "text": "البدء بالطلب الذي وصل أولاً حفاظاً على العدالة.",
        "score": 2
      },
      "B": {
        "text": "تقييم الأثر والاستعجال والمخاطر، وتحديد الأولويات والتفويض، ثم إبلاغ الجهات بالمفاضلات والمواعيد الواقعية.",
        "score": 5
      },
      "C": {
        "text": "قبول جميع المواعيد وتوزيع الضغط على الفريق بالتساوي.",
        "score": 1
      },
      "D": {
        "text": "مطالبة الجهات الطالبة بالتفاوض فيما بينها وتحديد ما يجب إنجازه أولاً.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يضع معايير واضحة ويمنع الوعود غير الواقعية، بينما (D) قد يفيد لكنه يتخلى جزئياً عن دور القائد في التقييم.",
      "common_mistake": "مساواة الاستعجال المعلن بالأولوية الحقيقية، أو قبول التزامات غير قابلة للتحقيق.",
      "coaching_note": "الضغط لا يلغي الأولويات؛ بل يجعلها أكثر ضرورة."
    }
  },
  {
    "item_code": "SJT-21",
    "sjt_domain": "CHANGE_ADAPTATION",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "اقتراح مبتكر لم يُختبر",
    "narrative_ar": "اقترح موظف حلاً مبتكراً قد يخفض الوقت والتكلفة، لكنه لم يُجرّب في بيئتكم ويحتاج بعض التعديل.",
    "options": {
      "A": {
        "text": "رفض الاقتراح لأن المخاطر غير معروفة ولأن الإجراء الحالي يعمل.",
        "score": 1
      },
      "B": {
        "text": "اعتماد الحل على نطاق كامل للاستفادة من الفرصة قبل غيركم.",
        "score": 1
      },
      "C": {
        "text": "تنفيذ تجربة محدودة بمعايير نجاح وضوابط ومراجعة، ثم اتخاذ قرار التوسع بناءً على النتائج.",
        "score": 5
      },
      "D": {
        "text": "طلب دراسة جدوى ومراجعة فنية أولية قبل تصميم تجربة محدودة.",
        "score": 4
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يوازن التعلم والسرعة والمخاطر، و (D) خطوة قوية تسبق التجربة لكنها أقل اكتمالاً.",
      "common_mistake": "رفض الجديد بالكامل أو تبنيه بالكامل قبل توليد أدلة.",
      "coaching_note": "التجريب المنضبط هو الجسر بين الابتكار والاعتماد."
    }
  },
  {
    "item_code": "SJT-22",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "DIRECT",
    "exam_mode": "FULL",
    "scenario_title": "سوء فهم في فريق يعمل عن بُعد",
    "narrative_ar": "فهم أحد أعضاء الفريق عن بُعد رسالة بريدية بطريقة مختلفة، ونفذ جزءاً من المهمة بصورة غير صحيحة.",
    "options": {
      "A": {
        "text": "إرسال رسالة أطول تشرح جميع التفاصيل مرة أخرى.",
        "score": 2
      },
      "B": {
        "text": "إجراء مكالمة قصيرة لتوضيح المطلوب، وطلب إعادة شرحه بكلماته، ثم إرسال خلاصة مكتوبة ونقطة تحقق.",
        "score": 5
      },
      "C": {
        "text": "إضافة مدير الموظف في الرسالة وبيان أن التعليمات كانت واضحة.",
        "score": 1
      },
      "D": {
        "text": "إرسال مثال أو نموذج مكتمل وطلب تأكيد الفهم قبل استكمال التنفيذ.",
        "score": 4
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يستخدم قناة تفاعلية ثم يوثق، و (D) يدعم الفهم عملياً لكنه لا يستكشف سبب الالتباس بالقدر نفسه.",
      "common_mistake": "زيادة طول الرسالة من دون تغيير القناة أو التحقق من الفهم.",
      "coaching_note": "عندما تفشل قناة، غيّر القناة ثم وثق الاتفاق."
    }
  },
  {
    "item_code": "SJT-23",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "DIRECT",
    "exam_mode": "FULL",
    "scenario_title": "نَسب نجاح المشروع",
    "narrative_ar": "نجح مشروع قاده فريقك، وطُلب منك عرضه أمام الإدارة. كان لبعض الأعضاء دور بارز، بينما أسهم آخرون بأعمال أقل ظهوراً لكنها ضرورية.",
    "options": {
      "A": {
        "text": "عرض النجاح بوصفه نتيجة لقيادتك وتوجيهك للفريق.",
        "score": 1
      },
      "B": {
        "text": "ربط النجاح بالهدف المشترك، وذكر إسهامات محددة ومتنوعة، وتوضيح ما تعلمه الفريق.",
        "score": 5
      },
      "C": {
        "text": "تقدير أصحاب الأداء الأكثر ظهوراً فقط لأنهم صنعوا الفارق الأكبر.",
        "score": 2
      },
      "D": {
        "text": "شكر الفريق كله بصورة عامة من دون تمييز الإسهامات.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يربط الاعتراف بالإنجاز المحدد وبالتعلم المشترك، ويعطي صورة عادلة عن مساهمة الفريق.",
      "common_mistake": "احتكار الفضل أو حصر التقدير في الأعمال الأكثر ظهوراً.",
      "coaching_note": "التقدير المحدد أكثر صدقاً وتأثيراً من الشكر العام، ويقوي السلوك الذي تريد تكراره."
    }
  },
  {
    "item_code": "SJT-24",
    "sjt_domain": "STAKEHOLDER_COLLAB",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "اختلاف في تفسير سياسة مشتركة",
    "narrative_ar": "تختلف إدارتك مع إدارة أخرى في تفسير سياسة مؤسسية، وأدى ذلك إلى قرارات متعارضة وتأخر معاملات مشتركة.",
    "options": {
      "A": {
        "text": "تجاوز الإدارة الأخرى وتطبيق تفسير إدارتك مباشرة.",
        "score": 1
      },
      "B": {
        "text": "مراجعة نص السياسة وهدفها والوقائع مع الطرف الآخر، واعتماد إجراء مؤقت متفق عليه، ورفع نقطة التفسير غير المحسومة إلى الجهة المخولة.",
        "score": 5
      },
      "C": {
        "text": "رفع الخلاف مباشرة إلى القيادة العليا من دون نقاش مشترك.",
        "score": 3
      },
      "D": {
        "text": "الاستمرار وفق تفسير إدارتك مع توثيق أسباب القرار.",
        "score": 2
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يعالج التشغيل والفهم والحوكمة معاً، ويصعّد فقط الجزء الذي يتطلب سلطة تفسيرية.",
      "common_mistake": "التصعيد المبكر أو فرض تفسير أحادي قبل تحديد موضع الخلاف.",
      "coaching_note": "افصل بين ما يمكن الاتفاق عليه تشغيلياً وما يحتاج حكماً رسمياً."
    }
  },
  {
    "item_code": "SJT-25",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "انخفاض المعنويات بعد إخفاق",
    "narrative_ar": "لم يحقق الفريق نتيجة مهمة رغم جهد كبير، وظهر الإحباط واللوم المتبادل. هناك موعد جديد قريب يتطلب استعادة التركيز.",
    "options": {
      "A": {
        "text": "التقليل من حجم الإخفاق والتأكيد أن الأمر لا يستحق القلق.",
        "score": 2
      },
      "B": {
        "text": "تحديد الأشخاص الذين تسببوا في الإخفاق حتى لا يتكرر الخطأ.",
        "score": 1
      },
      "C": {
        "text": "الاعتراف بالنتيجة، وإجراء مراجعة بلا لوم، وتحديد إجراءات تعافٍ وأهداف قريبة قابلة للتحقيق، وتقديم الدعم أثناء التنفيذ.",
        "score": 5
      },
      "D": {
        "text": "منح الفريق استراحة قصيرة ثم العودة إلى الخطة نفسها من دون مراجعة.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يجمع الاعتراف والتعلم والدعم والخطوات القصيرة التي تعيد الثقة.",
      "common_mistake": "إنكار المشاعر أو تحويل التعلم إلى لوم شخصي.",
      "coaching_note": "استعادة المعنويات لا تأتي من الخطاب وحده؛ بل من فهم ما حدث وخطة تعافٍ يمكن تنفيذها."
    }
  },
  {
    "item_code": "SJT-26",
    "sjt_domain": "DECISION_INTEGRITY",
    "complexity": "ESCALATION",
    "exam_mode": "FULL",
    "scenario_title": "مخاطرة امتثال قبل موعد حاسم",
    "narrative_ar": "اكتشفت قبل موعد تسليم حاسم أن إحدى الخطوات قد تتعارض مع متطلب امتثال، لكن تفسير المتطلب غير محسوم، وتأخير المشروع له تكلفة كبيرة.",
    "options": {
      "A": {
        "text": "الاستمرار في التنفيذ ومعالجة مسألة الامتثال بعد التسليم.",
        "score": 1
      },
      "B": {
        "text": "إيقاف المشروع كله وإبلاغ الإدارة بالمخاطرة من دون اقتراح بدائل.",
        "score": 3
      },
      "C": {
        "text": "إيقاف الخطوة محل المخاطرة فقط، واستشارة الجهة المختصة، وتصميم مسار بديل آمن، وتوثيق القرار والتأثير الزمني.",
        "score": 5
      },
      "D": {
        "text": "تكليف الموظف المسؤول بتوقيع إقرار بتحمل مسؤولية الاستمرار.",
        "score": 1
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يحمي المؤسسة، ويحد من نطاق التوقف، ويطلب تفسيراً مخولاً، ويبحث عن بديل.",
      "common_mistake": "اختيار السرعة على الامتثال، أو إيقاف كل شيء من دون تحليل نطاق المخاطرة.",
      "coaching_note": "عند مخاطر الامتثال، لا تنقل المسؤولية إلى فرد؛ عالج القرار والنظام والتوثيق."
    }
  },
  {
    "item_code": "SJT-27",
    "sjt_domain": "DECISION_INTEGRITY",
    "complexity": "ESCALATION",
    "exam_mode": "FULL",
    "scenario_title": "طلب تبسيط تقرير تنفيذي بصورة مفرطة",
    "narrative_ar": "طُلب منك مسؤول تنفيذي اختصار تقرير معقد إلى صفحة واحدة، لكن حذف بعض التحفظات قد يجعل الرسالة مضللة لقرار مهم.",
    "options": {
      "A": {
        "text": "حذف التحفظات وتنفيذ طلب المسؤول حرفياً لأن القرار النهائي مسؤوليته.",
        "score": 1
      },
      "B": {
        "text": "رفض الاختصار وإرسال التقرير الكامل كما هو.",
        "score": 2
      },
      "C": {
        "text": "إعداد ملخص تنفيذي واضح يبرز الاستنتاج والتحفظات الحرجة، وإرفاق التفاصيل، والتأكد من نوع القرار الذي سيبُنى عليه التقرير.",
        "score": 5
      },
      "D": {
        "text": "إرسال صفحة مبسطة، ثم ذكر التحفظات شفهياً فقط أثناء الاجتماع.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يحقق الوضوح والاختصار من دون إخفاء المعلومات الجوهرية، ويربط المحتوى بقرار المستخدم.",
      "common_mistake": "الاختيار بين الاختصار والدقة كأنهما متعارضان دائماً.",
      "coaching_note": "التواصل التنفيذي الجيد يختصر التعقيد، لا يحذف الحقيقة."
    }
  },
  {
    "item_code": "SJT-28",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "DIRECT",
    "exam_mode": "FULL",
    "scenario_title": "عضو لا يسهم بسبب فجوة مهارية",
    "narrative_ar": "يتأخر أحد أعضاء الفريق في تسليم مهامه، وتشير الملاحظة الأولية إلى وجود فجوة مهارية لا ضعف التزام.",
    "options": {
      "A": {
        "text": "استبداله فوراً بعضو أكثر خبرة لحماية موعد المشروع.",
        "score": 2
      },
      "B": {
        "text": "مناقشته على انفراد، وتحديد الفجوة، ووضع دعم عملي قصير، وتعديل نطاق المهمة مؤقتاً، ومتابعة التقدم بمؤشرات واضحة.",
        "score": 5
      },
      "C": {
        "text": "إعادة توزيع مهامه بصورة دائمة من دون مناقشته حتى لا يشعر بالإحراج.",
        "score": 2
      },
      "D": {
        "text": "مطالبة الزملاء بتغطية النقص إلى أن يتعلم بالممارسة غير الرسمية.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يوازن حماية النتيجة مع تطوير القدرة، ويجعل التحسن قابلاً للمتابعة.",
      "common_mistake": "تفسير ضعف المهارة على أنه ضعف التزام، أو نقل المشكلة إلى الفريق بلا خطة تطوير.",
      "coaching_note": "ميّز بين فجوة الإرادة وفجوة المهارة؛ فلكل منهما معالجة مختلفة."
    }
  },
  {
    "item_code": "SJT-29",
    "sjt_domain": "TEAM_LEADERSHIP",
    "complexity": "TRADE_OFF",
    "exam_mode": "FULL",
    "scenario_title": "تحول نقاش مهني إلى نقد شخصي",
    "narrative_ar": "خلال اجتماع، تحول خلاف حول فكرة إلى انتقاد شخصي بين اثنين من المشاركين، وبدأ الحضور يفقدون التركيز.",
    "options": {
      "A": {
        "text": "الرد بحزم على الشخص الأكثر حدة وإظهار أن أسلوبه غير مقبول.",
        "score": 1
      },
      "B": {
        "text": "إيقاف التبادل الشخصي، وإعادة صياغة موضوع الخلاف بصورة محايدة، وتثبيت قواعد الاحترام، ومواصلة النقاش إن أمكن مع متابعة خاصة لاحقاً.",
        "score": 5
      },
      "C": {
        "text": "تجاهل التعليقات والتركيز على جدول الأعمال حتى لا يتضخم الخلاف.",
        "score": 2
      },
      "D": {
        "text": "إنهاء الاجتماع فوراً وإحالة الطرفين إلى الموارد البشرية.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "B",
      "rationale": "الخيار (B) يوقف السلوك، ويحافظ على الموضوع، ويعيد قواعد النقاش، ويؤجل المعالجة الفردية لما بعد الاحتواء.",
      "common_mistake": "مقابلة الحدة بحدة، أو تجاهلها باسم التركيز على المهمة.",
      "coaching_note": "أوقف السلوك الشخصي فوراً، لكن لا تجعل احتواءه يلغي الهدف المهني للاجتماع."
    }
  },
  {
    "item_code": "SJT-30",
    "sjt_domain": "CRISIS_RESOLUTION",
    "complexity": "ESCALATION",
    "exam_mode": "FULL",
    "scenario_title": "تغييرات متأخرة قبل عرض حاسم",
    "narrative_ar": "قبل عرض مهم بساعات، وصلت تعديلات كثيرة من عدة جهات. بعضها ضروري، وبعضها تجميلي، ولا يكفي الوقت لتطبيقها جميعاً ومراجعتها.",
    "options": {
      "A": {
        "text": "قبول جميع التعديلات وتوزيعها على الفريق حتى لو لم يتوفر وقت للمراجعة.",
        "score": 1
      },
      "B": {
        "text": "رفض جميع التعديلات لأن وقت المراجعة انتهى.",
        "score": 2
      },
      "C": {
        "text": "تصنيف التعديلات حسب أثرها وضرورتها، وتجميد غير الضروري، وتعيين مسؤول تحقق، وإجراء بروفة للأجزاء المتأثرة، وتجهيز نسخة احتياطية.",
        "score": 5
      },
      "D": {
        "text": "تفويض التعديلات لأعضاء الفريق والثقة بأن كل شخص سيتحقق من عمله.",
        "score": 3
      }
    },
    "scoring_and_decision_rationales": {
      "best_option_key": "C",
      "rationale": "الخيار (C) يطبق مفاضلة واضحة ويحمي الجودة الحرجة ويضيف تحققاً وبديلاً.",
      "common_mistake": "الموافقة أو الرفض الشامل بدلاً من الفرز حسب الأثر.",
      "coaching_note": "تحت ضغط التغيير، جمّد ما لا يضيف قيمة واحمِ نقاط الفشل الحرجة."
    }
  }
]"""

new_data = json.loads(new_json_data)

# Read existing SJT items from backend JSON
try:
    with open('backend/src/main/resources/data/sjt_items.json', 'r', encoding='utf-8') as f:
        existing_data = json.load(f)
except Exception:
    existing_data = []

# Append and save
existing_data.extend(new_data)
with open('backend/src/main/resources/data/sjt_items.json', 'w', encoding='utf-8') as f:
    json.dump(existing_data, f, ensure_ascii=False, indent=2)
print("Appended sjt_items.json")

# The mapping of domain code to ID
domain_mapping = {
    "DECISION_INTEGRITY": 1,
    "TEAM_LEADERSHIP": 2,
    "CRISIS_RESOLUTION": 3,
    "STAKEHOLDER_COLLAB": 4,
    "CHANGE_ADAPTATION": 5
}

sql = "SET NAMES utf8mb4;\n\n"

for item in new_data:
    item_code = item['item_code']
    sjt_domain = item['sjt_domain']
    domain_id = domain_mapping.get(sjt_domain, 1)
    complexity = item['complexity']
    exam_mode = item['exam_mode']
    title = item['scenario_title'].replace("'", "''")
    narrative = item['narrative_ar'].replace("'", "''")
    
    rationale = item['scoring_and_decision_rationales']['rationale'].replace("'", "''")
    common_mistake = item['scoring_and_decision_rationales'].get('common_mistake', '').replace("'", "''")
    coaching_note = item['scoring_and_decision_rationales'].get('coaching_note', '').replace("'", "''")
    best_key = item['scoring_and_decision_rationales']['best_option_key']
    
    sql += "INSERT INTO sjt_scenarios (item_code, domain_id, title_ar, narrative_ar, complexity, exam_mode, rationale_ar, common_mistake_ar, coaching_note_ar, best_option_key, is_active, exposure_count, created_at) "
    sql += f"VALUES ('{item_code}', {domain_id}, '{title}', '{narrative}', '{complexity}', '{exam_mode}', '{rationale}', '{common_mistake}', '{coaching_note}', '{best_key}', 1, 0, NOW());\n"
    
    sql += "SET @scenario_id = LAST_INSERT_ID();\n"
    
    for key, opt in item['options'].items():
        opt_text = opt['text'].replace("'", "''")
        score = opt['score']
        is_best = 1 if key == best_key else 0
        display_order = 1 if key == 'A' else 2 if key == 'B' else 3 if key == 'C' else 4
        
        sql += f"INSERT INTO sjt_options (scenario_id, option_key, action_text_ar, effectiveness_score, is_best_action, display_order) "
        sql += f"VALUES (@scenario_id, '{key}', '{opt_text}', {score}, {is_best}, {display_order});\n"
        
    sql += "\n"

with open('seed_sjt_batch2.sql', 'w', encoding='utf-8') as f:
    f.write(sql)
print("Generated seed_sjt_batch2.sql")
