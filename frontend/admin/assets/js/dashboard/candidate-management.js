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
                : '<span class="badge badge-sm bg-gradient-danger">Deactivated</span>';

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
                         : `<button class="btn btn-link text-success mb-0 reactivate-btn" data-id="${candidate.id}">Reactivate</button>`
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

function openEditModal(id) {
    const candidate = activeCandidatesList.find(c => String(c.id) === String(id));
    if (!candidate) return;

    document.getElementById('candidateId').value = candidate.id;
    document.getElementById('candidateName').value = candidate.name;
    document.getElementById('candidateEmail').value = candidate.email;
    document.getElementById('candidatePassword').value = '';

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

    const payload = { name, email };
    if (password) {
        payload.password = password;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: authHeader,
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Failed to update candidate');
        }

        if (bootstrapModalInstance) {
            bootstrapModalInstance.hide();
        }
        await loadCandidates();

    } catch (err) {
        console.error('Error updating candidate:', err);
        alert(err.message || 'Error updating candidate');
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
            throw new Error('Action failed');
        }

        await loadCandidates();

    } catch (err) {
        console.error('Status toggle failed:', err);
        alert(err.message || 'Action failed');
    }
}
