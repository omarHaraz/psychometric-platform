import { API_BASE } from "../../../../shared/config/api-config.js";
const AdminUI = window.AdminUI;

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

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadCandidates();

    const modalEl = document.getElementById('candidateModal');
    if (modalEl && typeof bootstrap !== 'undefined') {
        bootstrapModalInstance = new bootstrap.Modal(modalEl);
    }

    const formEl = document.getElementById('candidateForm');
    if (formEl) {
        formEl.addEventListener('submit', saveCandidateForm);
    }

    const addBtn = document.getElementById('addCandidateBtn');
    if (addBtn) {
        addBtn.addEventListener('click', openCreateModal);
    }
});

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
                        // Get the latest attempt (index 0 since we ordered by DESC)
                        candidateAttemptsMap[c.id] = attempts[0];
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

            const attempt = candidateAttemptsMap[candidate.id];
            let assessmentHtml = '';

            if (attempt && attempt.state !== 'SCORED') {
                const batteryNames = ['PQ10', 'SJT', 'DERAILERS', 'GCAT'];
                const currentBattery = batteryNames[attempt.currentBatteryIndex] || 'PQ10';
                
                let stateBadgeColor = 'bg-gradient-info';
                if (attempt.state === 'INIT') stateBadgeColor = 'bg-gradient-warning';
                else if (attempt.state === 'ALL_SUBMITTED') stateBadgeColor = 'bg-gradient-success';

                assessmentHtml = `
                    <div class="d-flex flex-column align-items-center">
                        <span class="badge badge-sm ${stateBadgeColor} mb-1">${attempt.state}</span>
                        <small class="text-xxs text-secondary mb-1">Battery ${attempt.currentBatteryIndex + 1}/4 (${currentBattery})</small>
                        ${attempt.state === 'ALL_SUBMITTED' ? `<button class="btn btn-xs bg-gradient-primary mt-1 mb-0 assign-attempt-btn" data-id="${candidate.id}">Re-assign Test</button>` : ''}
                    </div>
                `;
            } else if (attempt && attempt.state === 'SCORED') {
                assessmentHtml = `
                    <div class="text-center">
                        <span class="badge badge-sm bg-gradient-dark">SCORED</span>
                        <br><button class="btn btn-xs bg-gradient-primary mt-1 assign-attempt-btn" data-id="${candidate.id}">Re-assign</button>
                    </div>
                `;
            } else {
                assessmentHtml = `
                    <div class="text-center">
                        ${
                            candidate.enabled
                            ? `<button class="btn btn-xs bg-gradient-primary mb-0 assign-attempt-btn" data-id="${candidate.id}">
                                <i class="material-symbols-rounded text-xs">assignment</i> Assign Test
                               </button>`
                            : `<span class="text-xxs text-muted">Enable account first</span>`
                        }
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

    document.querySelectorAll('.assign-attempt-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const candidateId = Number(e.currentTarget.dataset.id);
            if (!confirm('Assign a new 4-Battery Assessment Attempt to this candidate?')) {
                return;
            }
            try {
                const res = await fetch(`${API_BASE}/api/admin/attempts`, {
                    method: 'POST',
                    headers: authHeader,
                    body: JSON.stringify({ candidateId })
                });
                if (res.ok) {
                    alert('Assessment successfully assigned! The candidate can now log in and take the exam.');
                    loadCandidates();
                } else if (res.status === 409) {
                    alert('Candidate already has an active (non-scored) assessment attempt.');
                } else {
                    const data = await res.json().catch(() => ({}));
                    alert(`Failed to assign assessment: ${data.message || res.statusText}`);
                }
            } catch (err) {
                console.error('Error assigning attempt:', err);
                alert('Error assigning assessment attempt.');
            }
        });
    });
}

function openCreateModal() {
    document.getElementById('candidateId').value = '';
    document.getElementById('candidateName').value = '';
    document.getElementById('candidateEmail').value = '';
    const passInput = document.getElementById('candidatePassword');
    passInput.value = '';
    passInput.required = true;
    passInput.setAttribute('minlength', '6');
    document.getElementById('passwordHelp').classList.add('d-none');
    document.getElementById('candidateModalLabel').textContent = 'Add Candidate';

    if (bootstrapModalInstance) {
        bootstrapModalInstance.show();
    }
}

function openEditModal(id) {
    const candidate = activeCandidatesList.find(c => String(c.id) === String(id));
    if (!candidate) return;

    document.getElementById('candidateId').value = candidate.id;
    document.getElementById('candidateName').value = candidate.name;
    document.getElementById('candidateEmail').value = candidate.email;
    const passInput = document.getElementById('candidatePassword');
    passInput.value = '';
    passInput.required = false;
    passInput.removeAttribute('minlength');
    document.getElementById('passwordHelp').classList.remove('d-none');
    document.getElementById('candidateModalLabel').textContent = 'Edit Candidate';

    if (bootstrapModalInstance) {
        bootstrapModalInstance.show();
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

        if (bootstrapModalInstance) {
            bootstrapModalInstance.hide();
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
