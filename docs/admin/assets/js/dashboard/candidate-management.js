import { API_BASE } from "../../../../shared/config/api-config.js";

const AdminUI = window.AdminUI || {
    clearFormErrors: (form) => {
        if (!form) return;
        form.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
        form.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
    },
    showToast: (msg, type = 'info') => console.log(`[Toast ${type}]:`, msg),
    showConfirm: async (opts) => confirm(typeof opts === 'string' ? opts : (opts && opts.message) ? opts.message : 'Are you sure?')
};

const API_BASE_URL = `${API_BASE}/api/candidate/management`;

const user = JSON.parse(localStorage.getItem("user"));

if (!user || !user.token) {
    window.location.href = "../../auth/login.html";
}

const authHeader = {
    Authorization: `Bearer ${user.token}`,
    "Content-Type": "application/json"
};

let activeCandidatesList = [];
let bootstrapModalInstance = null;
let assignModalInstance = null;

function getCandidateModalInstance() {
    if (!bootstrapModalInstance) {
        const modalEl = document.getElementById('candidateModal');
        if (modalEl && typeof bootstrap !== 'undefined') {
            bootstrapModalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        }
    }
    return bootstrapModalInstance;
}

function getAssignModalInstance() {
    if (!assignModalInstance) {
        const modalEl = document.getElementById('assignAssessmentModal');
        if (modalEl && typeof bootstrap !== 'undefined') {
            assignModalInstance = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        }
    }
    return assignModalInstance;
}

function updateAssignCardStyles(selectedType) {
    const cardPsych = document.getElementById('cardPsychometric');
    const cardEmp = document.getElementById('cardEmployment');
    const radioPsych = document.getElementById('radioPsychometric');
    const radioEmp = document.getElementById('radioEmployment');

    if (selectedType === 'PSYCHOMETRIC') {
        if (radioPsych) radioPsych.checked = true;
        if (cardPsych) {
            cardPsych.style.borderColor = '#344767';
            cardPsych.style.backgroundColor = '#f8f9fa';
        }
        if (cardEmp) {
            cardEmp.style.borderColor = '#e9ecef';
            cardEmp.style.backgroundColor = '#ffffff';
        }
    } else {
        if (radioEmp) radioEmp.checked = true;
        if (cardEmp) {
            cardEmp.style.borderColor = '#2dce89';
            cardEmp.style.backgroundColor = '#f8f9fa';
        }
        if (cardPsych) {
            cardPsych.style.borderColor = '#e9ecef';
            cardPsych.style.backgroundColor = '#ffffff';
        }
    }
}

function initAssignAssessmentModal() {
    const cardPsych = document.getElementById('cardPsychometric');
    const cardEmp = document.getElementById('cardEmployment');
    const radioPsych = document.getElementById('radioPsychometric');
    const radioEmp = document.getElementById('radioEmployment');
    const confirmBtn = document.getElementById('confirmAssignExamBtn');

    if (cardPsych) {
        cardPsych.addEventListener('click', () => updateAssignCardStyles('PSYCHOMETRIC'));
    }
    if (cardEmp) {
        cardEmp.addEventListener('click', () => updateAssignCardStyles('EMPLOYMENT'));
    }
    if (radioPsych) {
        radioPsych.addEventListener('change', () => updateAssignCardStyles('PSYCHOMETRIC'));
    }
    if (radioEmp) {
        radioEmp.addEventListener('change', () => updateAssignCardStyles('EMPLOYMENT'));
    }

    if (confirmBtn) {
        confirmBtn.onclick = async () => {
            const candidateId = document.getElementById('assignCandidateId')?.value;
            const selectedRadio = document.querySelector('input[name="assignExamRadio"]:checked');
            const examType = selectedRadio ? selectedRadio.value : 'PSYCHOMETRIC';

            if (!candidateId) {
                alert('Candidate ID is missing');
                return;
            }

            confirmBtn.disabled = true;
            const originalContent = confirmBtn.innerHTML;
            confirmBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status"></span> Assigning...';

            try {
                const res = await fetch(`${API_BASE}/api/admin/attempts`, {
                    method: 'POST',
                    headers: authHeader,
                    body: JSON.stringify({
                        candidateId: Number(candidateId),
                        examType: examType
                    })
                });

                if (res.ok) {
                    const modal = getAssignModalInstance();
                    if (modal) modal.hide();
                    alert(`Assessment (${examType === 'PSYCHOMETRIC' ? 'التقييم السيكومتري' : 'اختبار التوظيف'}) successfully assigned!`);
                    await loadCandidates();
                } else if (res.status === 409) {
                    alert(`Candidate already has an active (non-scored) attempt for ${examType === 'PSYCHOMETRIC' ? 'التقييم السيكومتري' : 'اختبار التوظيف'}.`);
                } else {
                    const data = await res.json().catch(() => ({}));
                    alert(`Failed to assign assessment: ${data.message || res.statusText}`);
                }
            } catch (err) {
                console.error('Error assigning assessment:', err);
                alert('An error occurred while assigning assessment.');
            } finally {
                confirmBtn.disabled = false;
                confirmBtn.innerHTML = originalContent;
            }
        };
    }
}

function openAssignModal(candidateId, candidateName, hasActivePsych, hasActiveEmp) {
    const idEl = document.getElementById('assignCandidateId');
    const nameEl = document.getElementById('assignCandidateName');
    if (idEl) idEl.value = candidateId;
    if (nameEl) nameEl.textContent = candidateName || `Candidate #${candidateId}`;

    if (hasActivePsych && !hasActiveEmp) {
        updateAssignCardStyles('EMPLOYMENT');
    } else {
        updateAssignCardStyles('PSYCHOMETRIC');
    }

    const modal = getAssignModalInstance();
    if (modal) {
        modal.show();
    }
}

// Initialize
function initCandidateManagement() {
    loadCandidates();
    getCandidateModalInstance();
    initAssignAssessmentModal();

    const formEl = document.getElementById('candidateForm');
    if (formEl) {
        formEl.removeEventListener('submit', saveCandidateForm);
        formEl.addEventListener('submit', saveCandidateForm);
    }

    const addBtn = document.getElementById('addCandidateBtn');
    if (addBtn) {
        addBtn.removeEventListener('click', openCreateModal);
        addBtn.addEventListener('click', openCreateModal);
    }
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initCandidateManagement);
} else {
    initCandidateManagement();
}

// Load Candidates
async function loadCandidates() {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'GET',
            headers: authHeader
        });

        if (!response.ok) {
            throw new Error('Failed to fetch candidates');
        }

        activeCandidatesList = await response.json();

        const tableBody = document.getElementById('candidates-table-body');
        if (!tableBody) return;

        tableBody.innerHTML = '';

        // Load assessment attempts for all candidates in parallel
        const candidateAttemptsMap = {};
        await Promise.all(activeCandidatesList.map(async (c) => {
            try {
                const attRes = await fetch(`${API_BASE}/api/admin/attempts?candidateId=${c.id}`, {
                    headers: authHeader
                });
                if (attRes.ok) {
                    const attempts = await attRes.json();
                    if (attempts && attempts.length > 0) {
                        const psychometric = attempts.find(a => !a.examType || a.examType === 'PSYCHOMETRIC');
                        const employment = attempts.find(a => a.examType === 'EMPLOYMENT');
                        candidateAttemptsMap[c.id] = { psychometric, employment, all: attempts };
                    }
                }
            } catch (e) {
                console.error("Failed to fetch attempts for candidate", c.id, e);
            }
        }));

        activeCandidatesList.forEach(candidate => {
            const statusBadge = candidate.enabled
                ? '<span class="badge badge-sm bg-gradient-success">Active</span>'
                : '<span class="badge badge-sm bg-gradient-warning">Pending / Deactivated</span>';

            const attemptsInfo = candidateAttemptsMap[candidate.id] || {};
            const psychometricAttempt = attemptsInfo.psychometric;
            const employmentAttempt = attemptsInfo.employment;

            function renderExamBadge(attempt, label, iconClass, colorClass) {
                if (!attempt) return '';
                let stateBadge = 'bg-gradient-secondary';
                if (attempt.state === 'INIT') stateBadge = 'bg-gradient-warning';
                else if (attempt.state === 'IN_PROGRESS') stateBadge = 'bg-gradient-info';
                else if (attempt.state === 'ALL_SUBMITTED') stateBadge = 'bg-gradient-primary';
                else if (attempt.state === 'SCORED') stateBadge = 'bg-gradient-dark';

                const isEmployment = (attempt.examType === 'EMPLOYMENT' || label.includes('توظيف'));
                const examBatteries = isEmployment ? ['PQ10', 'SJT', 'GCAT'] : ['PQ10', 'SJT', 'DERAILERS', 'GCAT'];
                const totalBatteries = examBatteries.length;

                const batteryText = attempt.state === 'SCORED' 
                    ? 'Completed' 
                    : `Bat ${attempt.currentBatteryIndex + 1}/${totalBatteries} (${examBatteries[attempt.currentBatteryIndex] || 'PQ10'})`;

                return `
                    <div class="d-flex align-items-center justify-content-between p-1 px-2 border rounded mb-1 bg-white shadow-xs" style="font-size: 0.72rem; min-width: 220px;">
                        <div class="d-flex align-items-center text-start">
                            <i class="material-symbols-rounded text-xs me-1 ${colorClass}">${iconClass}</i>
                            <div>
                                <span class="font-weight-bold d-block text-xxs text-dark">${label}</span>
                                <span class="text-xxs text-muted">${batteryText}</span>
                            </div>
                        </div>
                        <div class="d-flex align-items-center gap-1 ms-2">
                            <span class="badge badge-xs ${stateBadge}">${attempt.state}</span>
                            ${attempt.state === 'SCORED' ? `
                                <button class="btn btn-xs bg-gradient-info p-1 px-2 mb-0 view-score-btn" data-token="${attempt.attemptToken}" data-name="${candidate.name}" title="View Score">
                                    <i class="material-symbols-rounded" style="font-size: 13px;">analytics</i>
                                </button>
                            ` : ''}
                        </div>
                    </div>
                `;
            }

            let assessmentHtml = '';
            if (!candidate.enabled) {
                assessmentHtml = '<span class="text-xxs text-muted">Enable account first</span>';
            } else {
                const psychHtml = renderExamBadge(psychometricAttempt, 'سيكومتري (Psychometric)', 'psychology', 'text-info');
                const empHtml = renderExamBadge(employmentAttempt, 'توظيف (Employment)', 'work', 'text-success');

                const hasAny = psychHtml || empHtml;
                const buttonText = hasAny ? 'Assign / Add Exam' : 'Assign Exam';
                
                const hasActivePsych = psychometricAttempt && (psychometricAttempt.state === 'INIT' || psychometricAttempt.state === 'IN_PROGRESS');
                const hasActiveEmp = employmentAttempt && (employmentAttempt.state === 'INIT' || employmentAttempt.state === 'IN_PROGRESS');

                assessmentHtml = `
                    <div class="d-flex flex-column align-items-center">
                        ${hasAny ? `<div class="d-flex flex-column w-100 mb-1">${psychHtml}${empHtml}</div>` : ''}
                        <button class="btn btn-xs bg-gradient-primary mb-0 open-assign-modal-btn" 
                                data-id="${candidate.id}" 
                                data-name="${candidate.name}"
                                data-has-active-psych="${hasActivePsych ? 'true' : 'false'}"
                                data-has-active-emp="${hasActiveEmp ? 'true' : 'false'}">
                            <i class="material-symbols-rounded text-xs me-1">assignment_add</i> ${buttonText}
                        </button>
                    </div>
                `;
            }

            const row = `
             <tr>                      
                 <td>
                     <div class="d-flex px-3 py-1 align-items-center">
                         <h6 class="mb-0 text-sm">${candidate.name}</h6>
                     </div>
                 </td>                      
                 <td>
                     <p class="text-xs font-weight-bold mb-0">${candidate.email}</p>
                 </td>
                 <td class="align-middle text-center text-sm">
                     ${statusBadge}
                 </td>
                 <td class="align-middle text-center">
                     ${assessmentHtml}
                 </td>
                 <td class="align-middle text-center">
                     <button class="btn btn-link text-secondary mb-0 edit-btn" data-id="${candidate.id}">
                         <i class="fa fa-ellipsis-v text-xs"></i> Edit
                     </button>
                     ${
                         candidate.enabled
                         ? `<button class="btn btn-link text-warning mb-0 deactivate-btn" data-id="${candidate.id}">Deactivate</button>`
                         : `<button class="btn btn-link text-success mb-0 reactivate-btn" data-id="${candidate.id}">Enable</button>`
                     }
                     <button class="btn btn-link text-danger mb-0 delete-perm-btn" data-id="${candidate.id}">
                         <i class="material-symbols-rounded text-xs">delete</i> Delete
                     </button>
                 </td>
             </tr>
            `;
            tableBody.insertAdjacentHTML('beforeend', row);
        });

        attachActionListeners();

    } catch (err) {
        console.error('Error fetching candidates:', err);
    }
}

function attachActionListeners() {
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const id = e.currentTarget.dataset.id;
            openEditModal(id);
        });
    });

    document.querySelectorAll('.deactivate-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const id = e.currentTarget.dataset.id;
            if (confirm('Are you sure you want to deactivate this candidate?')) {
                await toggleCandidateStatus(id, 'DELETE');
            }
        });
    });

    document.querySelectorAll('.reactivate-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const id = e.currentTarget.dataset.id;
            await toggleCandidateStatus(id, 'PATCH');
        });
    });

    document.querySelectorAll('.delete-perm-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const id = e.currentTarget.dataset.id;
            if (confirm('Are you sure you want to PERMANENTLY delete this candidate account and all associated test data? This action cannot be undone.')) {
                try {
                    const res = await fetch(`${API_BASE_URL}/${id}/permanent`, {
                        method: 'DELETE',
                        headers: authHeader
                    });
                    if (res.ok || res.status === 204) {
                        alert('Candidate permanently deleted successfully.');
                        loadCandidates();
                    } else {
                        const err = await res.json().catch(() => ({}));
                        alert(`Failed to delete candidate: ${err.message || res.statusText}`);
                    }
                } catch (err) {
                    console.error('Error deleting candidate:', err);
                    alert('Error deleting candidate permanently.');
                }
            }
        });
    });

    document.querySelectorAll('.view-score-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const token = e.currentTarget.dataset.token;
            const name = e.currentTarget.dataset.name;
            await openAdminScoreModal(token, name);
        });
    });

    document.querySelectorAll('.open-assign-modal-btn, .assign-attempt-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const candidateId = e.currentTarget.dataset.id;
            const candidateName = e.currentTarget.dataset.name;
            const hasActivePsych = e.currentTarget.dataset.hasActivePsych === 'true';
            const hasActiveEmp = e.currentTarget.dataset.hasActiveEmp === 'true';
            openAssignModal(candidateId, candidateName, hasActivePsych, hasActiveEmp);
        });
    });
}

function openCreateModal() {
    const idEl = document.getElementById('candidateId');
    if (idEl) idEl.value = '';
    const nameEl = document.getElementById('candidateName');
    if (nameEl) nameEl.value = '';
    const emailEl = document.getElementById('candidateEmail');
    if (emailEl) emailEl.value = '';
    const passInput = document.getElementById('candidatePassword');
    if (passInput) {
        passInput.value = '';
        passInput.required = true;
        passInput.setAttribute('minlength', '6');
    }
    const helpEl = document.getElementById('passwordHelp');
    if (helpEl) helpEl.classList.add('d-none');
    const labelEl = document.getElementById('candidateModalLabel');
    if (labelEl) labelEl.textContent = 'Add Candidate';

    const instance = getCandidateModalInstance();
    if (instance) {
        instance.show();
    }
}

function openEditModal(id) {
    const candidate = activeCandidatesList.find(c => String(c.id) === String(id));
    if (!candidate) return;

    const idEl = document.getElementById('candidateId');
    if (idEl) idEl.value = candidate.id;
    const nameEl = document.getElementById('candidateName');
    if (nameEl) nameEl.value = candidate.name;
    const emailEl = document.getElementById('candidateEmail');
    if (emailEl) emailEl.value = candidate.email;
    const passInput = document.getElementById('candidatePassword');
    if (passInput) {
        passInput.value = '';
        passInput.required = false;
        passInput.removeAttribute('minlength');
    }
    const helpEl = document.getElementById('passwordHelp');
    if (helpEl) helpEl.classList.remove('d-none');
    const labelEl = document.getElementById('candidateModalLabel');
    if (labelEl) labelEl.textContent = 'Edit Candidate';

    const instance = getCandidateModalInstance();
    if (instance) {
        instance.show();
    }
}

async function saveCandidateForm(e) {
    e.preventDefault();

    const id = document.getElementById('candidateId').value;
    const name = document.getElementById('candidateName').value.trim();
    const email = document.getElementById('candidateEmail').value.trim();
    const password = document.getElementById('candidatePassword').value;

    try {
        let response;
        if (id) {
            // Edit existing candidate
            const payload = { name, email };
            if (password) {
                payload.password = password;
            }
            response = await fetch(`${API_BASE_URL}/${id}`, {
                method: 'PUT',
                headers: authHeader,
                body: JSON.stringify(payload)
            });
        } else {
            // Create new candidate from Admin Panel (default enabled = true)
            if (!password || password.length < 6) {
                alert('Password must be at least 6 characters.');
                return;
            }
            response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: authHeader,
                body: JSON.stringify({ name, email, password })
            });
        }

        if (!response.ok) {
            const errData = await response.json().catch(() => ({}));
            throw new Error(errData.message || (id ? 'Failed to update candidate' : 'Failed to create candidate'));
        }

        const instance = getCandidateModalInstance();
        if (instance) {
            instance.hide();
        }
        await loadCandidates();

    } catch (err) {
        console.error('Error saving candidate:', err);
        alert(err.message || 'Error saving candidate');
    }
}

async function toggleCandidateStatus(id, method) {
    const url = method === 'PATCH' ? `${API_BASE_URL}/${id}/reactivate` : `${API_BASE_URL}/${id}`;
    try {
        const response = await fetch(url, {
            method: method,
            headers: authHeader
        });

        if (!response.ok) {
            const errData = await response.json().catch(() => ({}));
            throw new Error(errData.message || 'Action failed');
        }

        await loadCandidates();

    } catch (err) {
        console.error('Status toggle failed:', err);
        alert(err.message || 'Action failed');
    }
}

async function openAdminScoreModal(token, candidateName) {
    try {
        const modalEl = document.getElementById('adminScoreModal');
        const modalBody = document.getElementById('adminScoreModalBody');
        const printBtn = document.getElementById('adminPrintReportBtn');

        if (!modalEl || !modalBody) return;

        modalBody.innerHTML = '<div class="text-center p-4"><div class="spinner-border text-primary" role="status"></div><p class="mt-2 text-sm text-secondary">Loading score report...</p></div>';
        
        const modalInstance = new bootstrap.Modal(modalEl);
        modalInstance.show();

        const res = await fetch(`${API_BASE}/api/admin/attempts/${token}/score`, {
            headers: authHeader
        });

        if (!res.ok) {
            throw new Error('Failed to load assessment score details.');
        }

        const score = await res.json();
        const isSdElevated = !!score.elevatedImpressionManagement;
        const sdRisk = score.socialDesirabilityRiskPct || 0;
        const isCtElevated = !!score.elevatedCentralTendency;
        const ctRate = score.centralTendencyRatePct || 0;

        let traitsHtml = (score.traitScores || []).map(t => {
            const maxScoreText = score.examType === 'EMPLOYMENT' ? '' : ' / 68.0';
            return `
            <div class="col-md-6 mb-2">
                <div class="p-2 border rounded bg-white">
                    <div class="d-flex justify-content-between text-xs font-weight-bold">
                        <span>${t.nameAr || t.traitCode}</span>
                        <span class="text-teal text-gradient">${t.scorePct}%</span>
                    </div>
                    <small class="text-xxs text-secondary">Raw: ${t.rawScore}${maxScoreText}</small>
                    <div class="progress progress-xs mt-1">
                        <div class="progress-bar bg-gradient-info" style="width: ${Math.min(100, Math.max(0, t.scorePct))}%;"></div>
                    </div>
                </div>
            </div>
            `;
        }).join('');

        let derailersHtml = (score.derailerCategoryScores || []).map(d => `
            <div class="col-md-4 mb-2">
                <div class="p-2 border rounded bg-white">
                    <div class="d-flex justify-content-between text-xs font-weight-bold">
                        <span>${d.nameAr || d.categoryCode}</span>
                        <span class="text-warning">${d.scorePct}%</span>
                    </div>
                    <small class="text-xxs text-secondary">Raw: ${d.rawScore} / 40.0</small>
                    <div class="progress progress-xs mt-1">
                        <div class="progress-bar bg-gradient-warning" style="width: ${Math.min(100, Math.max(0, d.scorePct))}%;"></div>
                    </div>
                </div>
            </div>
        `).join('');

        let gcatHtml = (score.gcatSubtestScores || []).map(g => {
            const totalItems = g.totalCount || (score.examType === 'EMPLOYMENT' ? 10 : 14);
            const weightLabel = score.examType === 'EMPLOYMENT' ? (g.subtest && g.subtest.includes('المجرد') ? ' (40%)' : ' (30%)') : '';
            return `
            <div class="col-md-4 mb-2">
                <div class="p-2 border rounded bg-white text-center">
                    <div class="text-xs font-weight-bold mb-1">${g.subtest}${weightLabel}</div>
                    <h6 class="mb-0 text-info">${g.scorePct}%</h6>
                    <small class="text-xxs text-secondary">${g.correctCount} / ${totalItems} correct</small>
                </div>
            </div>
            `;
        }).join('');

        modalBody.innerHTML = `
            <div class="text-center mb-4">
                <div class="d-flex justify-content-center align-items-center gap-2 mb-1">
                    <h5 class="mb-0">${candidateName || score.candidateName || 'Candidate'}</h5>
                    <span class="badge ${score.examType === 'EMPLOYMENT' ? 'bg-gradient-success' : 'bg-gradient-primary'} text-xxs">
                        ${score.examType === 'EMPLOYMENT' ? 'اختبار التوظيف (Employment)' : 'التقييم السيكومتري (Psychometric)'}
                    </span>
                </div>
                <p class="text-xs text-secondary mb-0">Token: <code>${token}</code> &bull; Scored At: ${new Date(score.scoredAt).toLocaleString()}</p>
            </div>

            <!-- Composite Card -->
            <div class="card bg-gradient-dark text-white p-3 mb-4 shadow-sm">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="text-xs text-white-50 text-uppercase">
                            ${score.examType === 'EMPLOYMENT' ? 'Overall Employment Composite Score' : 'Composite Aptitude Score'}
                        </span>
                        <h2 class="text-white mb-0 font-weight-bolder">${score.compositeScore}%</h2>
                        <small class="text-xs text-white-50">Percentile: P${score.percentile} &bull; Band: ${score.readinessBandLabelAr || score.readinessBand}</small>
                        ${score.cappedPenaltyPct && score.cappedPenaltyPct > 0 ? `
                            <div class="mt-2 text-xxs text-warning">
                                <i class="material-symbols-rounded text-xs align-middle">warning</i>
                                <strong>Validity Deduction:</strong> -${score.cappedPenaltyPct}% (Raw: ${score.rawCompositeScore}% &bull; Final: ${score.compositeScore}%)
                            </div>
                        ` : ''}
                    </div>
                    <div class="text-end">
                        <span class="badge bg-success">${score.readinessBandLabelAr ? score.readinessBandLabelAr + ' (' + score.readinessBand + ')' : score.readinessBand}</span>
                    </div>
                </div>
            </div>

            <!-- Response Validity Panel -->
            <div class="card p-3 mb-4 border border-light shadow-2xs">
                <h6 class="text-xs font-weight-bold text-dark mb-2">
                    <i class="material-symbols-rounded text-sm align-middle me-1">verified_user</i>
                    مؤشرات صدق وجودة الاستجابة (Response Validity)
                </h6>
                <div class="row g-2">
                    <!-- Social Desirability -->
                    <div class="col-md-6">
                        <div class="p-2 border rounded ${isSdElevated ? 'bg-amber-light border-warning' : 'bg-light'} d-flex justify-content-between align-items-center">
                            <div>
                                <div class="text-xs font-weight-bold ${isSdElevated ? 'text-warning' : 'text-dark'}">
                                    مقياس التظاهر الاجتماعي
                                </div>
                                <small class="text-xxs text-secondary">${isSdElevated ? 'ميل مرتفع لإظهار صورة مثالية' : 'استجابات واقعية وتلقائية'}</small>
                            </div>
                            <span class="badge ${isSdElevated ? 'bg-gradient-warning' : 'bg-gradient-success'}">${sdRisk}%</span>
                        </div>
                    </div>
                    <!-- Central Tendency -->
                    <div class="col-md-6">
                        <div class="p-2 border rounded ${isCtElevated ? 'bg-amber-light border-warning' : 'bg-light'} d-flex justify-content-between align-items-center">
                            <div>
                                <div class="text-xs font-weight-bold ${isCtElevated ? 'text-warning' : 'text-dark'}">
                                    مؤشر الوسطية (Central Tendency)
                                </div>
                                <small class="text-xxs text-secondary">${isCtElevated ? 'نزعة مرتفعة نحو الحياد' : 'استجابات متوازنة ومتمايزة'}</small>
                            </div>
                            <span class="badge ${isCtElevated ? 'bg-gradient-warning' : 'bg-gradient-success'}">${ctRate}%</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Battery Overall Summary -->
            <h6 class="text-xs font-weight-bold text-dark mb-2">Battery Scores Overview</h6>
            <div class="row g-2 mb-4">
                <div class="${score.examType === 'EMPLOYMENT' ? 'col-4' : 'col-3'} text-center p-2 border rounded">
                    <small class="text-xxs text-muted d-block">${score.examType === 'EMPLOYMENT' ? '01 • Competencies (30%)' : '01 • Personality (28%)'}</small>
                    <strong class="text-sm">${score.personalityScorePct}%</strong>
                    <span class="d-block text-xxs text-secondary">${score.examType === 'EMPLOYMENT' ? '40 items' : '140 items'}</span>
                </div>
                <div class="${score.examType === 'EMPLOYMENT' ? 'col-4' : 'col-3'} text-center p-2 border rounded">
                    <small class="text-xxs text-muted d-block">${score.examType === 'EMPLOYMENT' ? '02 • SJT (30%)' : '02 • SJT (22%)'}</small>
                    <strong class="text-sm">${score.sjtScorePct}%</strong>
                    <span class="d-block text-xxs text-secondary">${score.examType === 'EMPLOYMENT' ? '10 scenarios' : '16 scenarios'}</span>
                </div>
                ${score.examType === 'EMPLOYMENT' ? '' : `
                <div class="col-3 text-center p-2 border rounded">
                    <small class="text-xxs text-muted d-block">03 • Derailers (20%)</small>
                    <strong class="text-sm">${score.derailersEffectiveScorePct}%</strong>
                    <span class="d-block text-xxs text-secondary">60 items</span>
                </div>
                `}
                <div class="${score.examType === 'EMPLOYMENT' ? 'col-4' : 'col-3'} text-center p-2 border rounded">
                    <small class="text-xxs text-muted d-block">${score.examType === 'EMPLOYMENT' ? '03 • GCAT (40%)' : '04 • GCAT (30%)'}</small>
                    <strong class="text-sm">${score.cognitiveScorePct}%</strong>
                    <span class="d-block text-xxs text-secondary">${score.examType === 'EMPLOYMENT' ? '30 items' : '42 items'}</span>
                </div>
            </div>

            <!-- PQ10 / Employment Competencies -->
            <h6 class="text-xs font-weight-bold text-dark mb-2">
                ${score.examType === 'EMPLOYMENT' ? 'Employment Competencies (12 Competencies)' : 'PQ10 Leadership Competencies (8 Traits + Social Desirability)'}
            </h6>
            <div class="row g-2 mb-3">
                ${traitsHtml}
                ${score.examType === 'EMPLOYMENT' ? '' : `
                <!-- 9th card: Social Desirability -->
                <div class="col-12">
                    <div class="p-2 border rounded ${isSdElevated ? 'bg-amber-light border-warning' : 'bg-light'}">
                        <div class="d-flex justify-content-between text-xs font-weight-bold">
                            <span>التظاهر الاجتماعي (مقياس التظاهر الاجتماعي)</span>
                            <span class="${isSdElevated ? 'text-warning' : 'text-success'}">${sdRisk}% مخاطرة</span>
                        </div>
                        <small class="text-xxs text-secondary">SOCIAL_DESIRABILITY &bull; 4 items validity scale</small>
                        <div class="progress progress-xs mt-1">
                            <div class="progress-bar ${isSdElevated ? 'bg-gradient-warning' : 'bg-gradient-success'}" style="width: ${Math.min(100, Math.max(0, sdRisk))}%;"></div>
                        </div>
                    </div>
                </div>
                `}
            </div>

            ${score.examType === 'EMPLOYMENT' ? '' : `
            <!-- Derailers -->
            <h6 class="text-xs font-weight-bold text-dark mb-2">Derailer Risk Categories (6 Drivers)</h6>
            <div class="row g-2 mb-3">
                ${derailersHtml}
            </div>
            `}

            <!-- GCAT -->
            <h6 class="text-xs font-weight-bold text-dark mb-2">
                ${score.examType === 'EMPLOYMENT' ? 'GCAT Cognitive Subtests (30% Verbal, 30% Numerical, 40% Abstract)' : 'GCAT Cognitive Subtests'}
            </h6>
            <div class="row g-2 mb-4">
                ${gcatHtml}
            </div>

            <!-- PDF Download Action Block -->
            <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                <a href="${API_BASE}/api/assessments/${encodeURIComponent(token)}/report/pdf" target="_blank" class="btn btn-sm btn-primary mb-0 d-flex align-items-center gap-1">
                    <i class="material-symbols-rounded text-sm">picture_as_pdf</i>
                    <span>Download Official PDF Report</span>
                </a>
            </div>
        `;

        if (printBtn) {
            printBtn.onclick = () => {
                const printWin = window.open('', '_blank');
                printWin.document.write(`
                    <html>
                    <head><title>Candidate Assessment Score - ${candidateName}</title></head>
                    <body style="font-family: sans-serif; padding: 20px;">
                        ${modalBody.innerHTML}
                    </body>
                    </html>
                `);
                printWin.document.close();
                printWin.print();
            };
        }

    } catch (err) {
        console.error('Failed to open score modal:', err);
        alert(err.message || 'Error loading score details.');
    }
}

Object.assign(window, {
    openCreateModal,
    openEditModal,
    openAdminScoreModal,
    openAssignModal,
    loadCandidates
});

