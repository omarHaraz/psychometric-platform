SET NAMES utf8mb4;

INSERT INTO derailer_items (statement_ar, justification_ar, ideal_target, response_scale_type, exam_mode, is_active, exposure_count, created_at) VALUES ('أتعمد تهميش أو تجاهل الزملاء الذين عارضوا قراراتي سابقاً في اجتماعات العمل.', 'تقيس سمة (العدائية). تهميش الزملاء بسبب خلافات سابقة يعكس شخصية انتقامية Vindictive تدمر التعاون وتسمم بيئة العمل.', 1, 'FREQUENCY', 'BOTH', 1, 0, NOW());
SET @last_item_id = LAST_INSERT_ID();
INSERT INTO derailer_item_types (item_id, type_id) VALUES (@last_item_id, 3);

