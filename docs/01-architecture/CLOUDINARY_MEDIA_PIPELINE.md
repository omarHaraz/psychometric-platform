# Cloudinary Media Pipeline Architecture & Codebase Trace

## 1. Overview & Goal

The Psychometric Assessment Platform supports multi-modal assessment items:
1. **GCAT Cognitive Matrix Tests**: High-resolution abstract logic matrices (e.g. 3x3 visual grids) and option tiles (A–E).
2. **SJT Situational Judgment Tests**: Organizational charts, scenario infographics, and workplace dilemmas.

Rather than storing binary images directly in MySQL or requiring manual URL pasting, this pipeline allows direct file uploads (`PNG`, `JPEG`, `WEBP`, `SVG`, `GIF`) to **Cloudinary CDN** with automatic thumbnailing, CDN URL generation, and entity mapping.

---

## 2. End-to-End Flow Diagram

```
+-----------------------------------------------------------------------------------------+
|                                    ADMIN BROWSER                                        |
|  1. User selects image file in GCAT / SJT Modal                                         |
|  2. JS constructs Multipart FormData                                                    |
|  3. POST /api/admin/media/upload?folder=gcat/matrices                                   |
+--------------------------------------------+--------------------------------------------+
                                             |
                                             v
+-----------------------------------------------------------------------------------------+
|                              SPRING BOOT BACKEND CONTROLLER                             |
|  File: AdminMediaController.java                                                        |
|  Endpoint: POST /api/admin/media/upload                                                 |
|  Security: @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")           |
+--------------------------------------------+--------------------------------------------+
                                             |
                                             v
+-----------------------------------------------------------------------------------------+
|                                    SERVICE LAYER                                        |
|  File: CloudinaryService.java                                                           |
|  - Validates non-empty file & MIME type (image/*)                                       |
|  - Injects Cloudinary bean from CloudinaryConfig.java                                   |
|  - Uploads to CDN folder (`psychometric-assessment/gcat/matrices`)                      |
|  - Returns: secureUrl, publicId, format, bytes, width, height                           |
+--------------------------------------------+--------------------------------------------+
                                             |
                                             v
+-----------------------------------------------------------------------------------------+
|                                CLOUDINARY CDN (CLOUD)                                   |
|  - Stores asset with unique public_id                                                   |
|  - Serves fast optimized HTTPS URLs to candidates worldwide                             |
+-----------------------------------------------------------------------------------------+
```

---

## 3. Configuration & Environment Variables

Configured in [`application.properties`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/resources/application.properties):

```properties
# Cloudinary CDN Configuration
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME:psychometric-platform}
cloudinary.api-key=${CLOUDINARY_API_KEY:placeholder_api_key}
cloudinary.api-secret=${CLOUDINARY_API_SECRET:placeholder_api_secret}
cloudinary.folder=${CLOUDINARY_FOLDER:psychometric-assessment}
```

### Fallback / Offline Developer Mode
When `CLOUDINARY_API_KEY` is not supplied or is set to `"placeholder_api_key"`, `CloudinaryService.java` automatically falls back to generating deterministic mock CDN URLs (`https://res.cloudinary.com/...`). This ensures tests, CI/CD pipelines, and local development never fail on connection errors when live cloud keys are absent.

---

## 4. API Reference

### 1. Upload Image Asset
- **Method**: `POST`
- **Path**: `/api/admin/media/upload`
- **Content-Type**: `multipart/form-data`
- **Request Parameters**:
  - `file` (MultipartFile, required): Image file.
  - `folder` (String, optional): Target subfolder (e.g. `gcat/matrices`, `sjt/scenarios`).
- **Response** (`201 Created`):
```json
{
  "secureUrl": "https://res.cloudinary.com/psychometric-platform/image/upload/v1700000000/psychometric-assessment/gcat/matrices/mat_01.png",
  "publicId": "psychometric-assessment/gcat/matrices/mat_01",
  "format": "png",
  "bytes": 145220,
  "width": 800,
  "height": 600
}
```

### 2. Delete Image Asset
- **Method**: `DELETE`
- **Path**: `/api/admin/media?publicId={publicId}`
- **Response**: `204 No Content`

---

## 5. Codebase Trace Guide for Debugging

| Step | Action | Code Location / Symbol | Key Checks |
|---|---|---|---|
| **1. UI File Select** | User selects file | [`item-bank.html`](file:///c:/Users/Logo/Desktop/Psychometric/frontend/admin/pages/item-bank.html) (`#gcatImageFileInput`) | Check `accept="image/*"` |
| **2. JS Upload Trigger** | Click "Upload to CDN" | [`item-bank-management.js`](file:///c:/Users/Logo/Desktop/Psychometric/frontend/admin/assets/js/dashboard/item-bank-management.js#L484-L545) (`uploadMediaFile`) | Check `FormData` appended with `file` & `folder` |
| **3. Security Filter** | JWT Verification | [`JwtAuthenticationFilter.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/common/security/JwtAuthenticationFilter.java) | Token in `Authorization: Bearer <token>` |
| **4. Controller Entry** | REST Handler | [`AdminMediaController.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/controller/AdminMediaController.java#L24-L32) | Consumes `MULTIPART_FORM_DATA_VALUE` |
| **5. Service Execution** | Cloudinary API Call | [`CloudinaryService.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/service/CloudinaryService.java#L41-L87) (`uploadImage`) | Check MIME type validation & upload parameters |
| **6. Form Auto-Fill** | URL updated in modal | [`item-bank-management.js`](file:///c:/Users/Logo/Desktop/Psychometric/frontend/admin/assets/js/dashboard/item-bank-management.js) | Sets `#gcatImageUrl` & displays `#gcatImagePreview` |
| **7. Persistence** | Item save | [`AdminGcatItemService.java`](file:///c:/Users/Logo/Desktop/Psychometric/backend/src/main/java/com/psychometric/platform/features/itembank/admin/service/AdminGcatItemService.java) | Stores `questionImageUrl` on `GcatQuestion` |
