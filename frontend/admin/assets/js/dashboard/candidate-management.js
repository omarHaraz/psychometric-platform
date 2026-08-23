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

        activeCandidatesList.forEach(candidate => {
            const statusBadge = candidate.enabled
                ? '<span class="badge badge-sm bg-gradient-success">Active</span>'
                : '<span class="badge badge-sm bg-gradient-warning">Pending / Deactivated</span>';

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
                     <button class="btn btn-link text-secondary mb-0 edit-btn" data-id="${candidate.id}">
                         <i class="fa fa-ellipsis-v text-xs"></i> Edit
                     </button>
                     ${
                         candidate.enabled
                         ? `<button class="btn btn-link text-danger mb-0 deactivate-btn" data-id="${candidate.id}">Deactivate</button>`
                         : `<button class="btn btn-link text-success mb-0 reactivate-btn" data-id="${candidate.id}">Enable / Activate</button>`
                     }
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
