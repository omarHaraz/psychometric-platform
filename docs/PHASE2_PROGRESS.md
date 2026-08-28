# Phase 2 Progress - Assessment Session Rebuild

**Date:** 2026-08-28

## What Was Replaced
The old flat `TestSession`/`examMode` model was completely replaced with a parent/child forced-sequential model (`AssessmentAttempt` -> `BatterySession`).
This was done because:
1. **Admin-Gated Access:** Candidates cannot start assessments on their own. Admins must explicitly assign them an attempt.
2. **Forced Sequence:** Candidates must take the 4 batteries in a strict order and cannot skip or re-enter completed batteries.
3. **Independent Reporting:** We need exactly ONE combined report per attempt, generated only after all batteries are completed.

## Entities Added
*   `AssessmentAttempt` (Parent entity storing attemptToken, candidate, admin, current battery index)
*   `BatterySession` (Child entity for each of the 4 test batteries, tracking sequence order, time limit, and individual session state)
*   `CandidateResponse` (Unified response entity storing Likert, Ranking, or MCQ responses linked to a specific BatterySession)

## Endpoints Added

### Admin Endpoints
*   `POST /api/admin/attempts`: Creates a new AssessmentAttempt + 4 locked BatterySession rows. Assigns it to the candidate. Returns `409` if the candidate already has an active attempt.
*   `GET /api/admin/attempts?candidateId=X`: Fetches all attempts and their progress for a specific candidate.

### Candidate Endpoints
*   `GET /api/attempts/me/pending`: Retrieves the candidate's active (non-scored) attempt.
*   `GET /api/attempts/{token}`: Retrieves the state and current battery info for the attempt. Enforces `403` if candidate ID doesn't match authenticated user.
*   `POST /api/attempts/{token}/start`: Transitions attempt state to `IN_PROGRESS` and starts the first battery. Sets the server-side Redis timer.
*   `POST /api/attempts/battery-sessions/{id}/heartbeat`: Autosaves Candidate responses into the Redis buffer and computes remaining time.
*   `POST /api/attempts/battery-sessions/{id}/submit`: Flushes Redis buffer to `candidate_responses`, transitions battery to `SUBMITTED`, and unlocks the next battery in the sequence.

### Reporting
*   `GET /api/attempts/{token}/report`: Endpoint stubbed. 
> *Note: Scoring and report generation are currently stubbed in this pass and will be implemented in a separate phase.*

## Battery Order and Time Limits (Confirmed & Hardcoded)
1. **PQ10**: 2400 seconds (40 minutes)
2. **SJT**: 2700 seconds (45 minutes)
3. **DERAILERS**: 1200 seconds (20 minutes)
4. **GCAT**: 1200 seconds (20 minutes)

## Security Rules Enforced
*   **Authorization:** JWT must map to a user ID that matches the `candidate_id` on the `AssessmentAttempt`.
*   **Sequence Lock:** `batterySession.sequenceOrder` must equal `attempt.currentBatteryIndex`.
*   **State Lock:** Battery actions (heartbeat/submit) are only accepted when the battery state is `IN_PROGRESS`.
*   **Server-Side Timing:** The candidate's remaining time is computed server-side via a Redis `battery_session:{id}:timer` key. Client-provided elapsed times are ignored.
