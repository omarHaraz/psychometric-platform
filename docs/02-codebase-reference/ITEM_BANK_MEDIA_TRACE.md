# Psychometric Item Bank Multi-Modal Reference & Debugging Trace

This document details the code references, database columns, and frontend-to-backend mappings for all 4 psychometric dimensions and their media assets.

---

## 1. Multi-Modal Database Schema

### GCAT Cognitive Questions (`gcat_questions`)
- `id` (`BIGINT`, PK)
- `item_code` (`VARCHAR(50)`, Unique, Indexed, e.g. `GCAT-ABS-01`)
- `subtest_id` (`BIGINT`, FK -> `gcat_subtests`)
- `title_ar` (`VARCHAR(255)`)
- `prompt_text_ar` (`TEXT`)
- `question_image_url` (`VARCHAR(500)`): Cloudinary HTTPS CDN delivery URL.
- `question_image_public_id` (`VARCHAR(255)`): Cloudinary asset public ID.
- `correct_option_key` (`VARCHAR(5)`): `A` | `B` | `C` | `D` | `E`.
- `difficulty` (`VARCHAR(20)`): `EASY` | `MEDIUM` | `HARD`.
- `exam_mode` (`VARCHAR(20)`): `FULL` | `QUICK` | `BOTH`.
- `is_active` (`BOOLEAN`, default `TRUE`).

### GCAT Options (`gcat_options`)
- `id` (`BIGINT`, PK)
- `question_id` (`BIGINT`, FK -> `gcat_questions`)
- `option_key` (`VARCHAR(5)`): `A`–`E`
- `option_text_ar` (`TEXT`, Nullable)
- `option_image_url` (`VARCHAR(500)`, Nullable)
- `option_image_public_id` (`VARCHAR(255)`, Nullable)
- `is_correct` (`BOOLEAN`)

### SJT Scenarios (`sjt_scenarios`)
- `id` (`BIGINT`, PK)
- `item_code` (`VARCHAR(50)`, Unique, Indexed, e.g. `SJT-DEC-01`)
- `domain_id` (`BIGINT`, FK -> `sjt_domains`)
- `title_ar` (`VARCHAR(255)`)
- `narrative_ar` (`TEXT`)
- `scenario_image_url` (`VARCHAR(500)`, Nullable)
- `best_option_key` (`VARCHAR(5)`): `A`–`D`
- `complexity` (`VARCHAR(20)`): `DIRECT` | `TRADE_OFF` | `ESCALATION`
- `is_active` (`BOOLEAN`, default `TRUE`).

---

## 2. API Endpoints for Image Uploads & Management

| Route | Method | Payload | Handler Class | Description |
|---|---|---|---|---|
| `/api/admin/media/upload` | `POST` | `multipart/form-data` (`file`, optional `folder`) | [`AdminMediaController.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/controller/AdminMediaController.java) | Uploads file to Cloudinary CDN |
| `/api/admin/media` | `DELETE` | `?publicId={publicId}` | [`AdminMediaController.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/controller/AdminMediaController.java) | Deletes asset from Cloudinary |
| `/api/admin/items/cognitive` | `POST` / `PUT` | `GcatQuestionAdminRequest` (includes `questionImageUrl`) | [`AdminGcatItemController.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/controller/AdminGcatItemController.java) | Saves GCAT item with CDN image URL |
| `/api/admin/items/sjt` | `POST` / `PUT` | `SjtScenarioAdminRequest` (includes `scenarioImageUrl`) | [`AdminSjtItemController.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/controller/AdminSjtItemController.java) | Saves SJT scenario with CDN image URL |

---

## 3. Frontend Execution Trace

```
1. Admin opens GCAT or SJT modal in item-bank.html.
2. Selects image file in file input (#gcatImageFileInput / #sjtImageFileInput).
3. Clicks "Upload to CDN" button.
4. JS function uploadMediaFile() in item-bank-management.js:
   - Appends file and folder ("gcat/matrices" or "sjt/scenarios") to FormData.
   - Dispatches authenticated POST request to /api/admin/media/upload.
   - On 201 Created response:
     - Sets #gcatImageUrl / #sjtImageUrl input value to data.secureUrl.
     - Sets #gcatImagePublicId input value to data.publicId.
     - Reveals #gcatImagePreviewContainer / #sjtImagePreviewContainer with live thumbnail.
     - Displays success toast notification.
5. Admin clicks "Save Item":
   - Form submission sends JSON payload containing questionImageUrl to /api/admin/items/*.
   - Item is persisted in MySQL and immediately available for candidate assessments.
```
