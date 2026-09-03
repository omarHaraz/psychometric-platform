import { API_BASE } from "../../../../shared/config/api-config.js";

const AdminUI = window.AdminUI;

function getUser() {
    return JSON.parse(localStorage.getItem("user") || "{}");
}

function getAuthHeaders() {
    const user = getUser();
    return {
        "Authorization": `Bearer ${user.token || ""}`,
        "Content-Type": "application/json",
        "Accept": "application/json"
    };
}

// State Management
let currentDimension = "personality"; // 'personality' | 'derailers' | 'cognitive' | 'sjt'
let rawItems = [];
let filteredItems = [];

let taxonomies = {
    competencies: [],
    derailerTypes: [],
    subtests: [
        { code: "ABSTRACT", nameAr: "الاستدلال المجرد" },
        { code: "NUMERICAL", nameAr: "الاستدلال العددي" },
        { code: "VERBAL", nameAr: "الاستدلال اللفظي" }
    ],
    sjtDomains: []
};

// Modals instances
let personalityModal = null;
let derailerModal = null;
let gcatModal = null;
let sjtModal = null;

// Initialize on DOM Ready
async function initItemBank() {
    const user = getUser();
    if (!user || !user.token) {
        window.location.href = "../../auth/login.html";
        return;
    }

    initModals();
    setupEventListeners();
    await loadTaxonomies();
    await loadDimensionData(currentDimension);
}

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initItemBank);
} else {
    initItemBank();
}

function initModals() {
    if (typeof bootstrap !== "undefined") {
        const pEl = document.getElementById("personalityModal");
        if (pEl) personalityModal = new bootstrap.Modal(pEl);

        const dEl = document.getElementById("derailerModal");
        if (dEl) derailerModal = new bootstrap.Modal(dEl);

        const gEl = document.getElementById("gcatModal");
        if (gEl) gcatModal = new bootstrap.Modal(gEl);

        const sEl = document.getElementById("sjtModal");
        if (sEl) sjtModal = new bootstrap.Modal(sEl);
    }
}

function setupEventListeners() {
    // Dimension Tabs
    document.querySelectorAll("#dimensionTabs [data-dimension]").forEach(tab => {
        tab.addEventListener("click", async (e) => {
            e.preventDefault();
            document.querySelectorAll("#dimensionTabs [data-dimension]").forEach(t => t.classList.remove("active"));
            tab.classList.add("active");

            currentDimension = tab.getAttribute("data-dimension");
            await loadDimensionData(currentDimension);
        });
    });

    // Add Item button
    const addBtn = document.getElementById("addItemBtn");
    if (addBtn) {
        addBtn.addEventListener("click", openAddModal);
    }

    // Search and Filters
    const searchInput = document.getElementById("searchInput");
    if (searchInput) searchInput.addEventListener("input", applyFilters);

    const statusFilter = document.getElementById("statusFilter");
    if (statusFilter) statusFilter.addEventListener("change", applyFilters);

    const modeFilter = document.getElementById("modeFilter");
    if (modeFilter) modeFilter.addEventListener("change", applyFilters);

    // Form Submissions
    document.getElementById("personalityForm")?.addEventListener("submit", handlePersonalitySubmit);
    document.getElementById("derailerForm")?.addEventListener("submit", handleDerailerSubmit);
    document.getElementById("gcatForm")?.addEventListener("submit", handleGcatSubmit);
    document.getElementById("sjtForm")?.addEventListener("submit", handleSjtSubmit);

    // Direct Main Image Upload to Cloudinary CDN
    document.getElementById("gcatUploadBtn")?.addEventListener("click", () => {
        uploadMediaFile("gcatImageFileInput", "gcatImageUrl", "gcatImagePublicId", "gcatImagePreviewContainer", "gcatImagePreview", "psychometric/gcat");
    });
    
    document.getElementById("gcatImageFileInput")?.addEventListener("change", (e) => {
        if (e.target.files && e.target.files.length > 0) {
            uploadMediaFile("gcatImageFileInput", "gcatImageUrl", "gcatImagePublicId", "gcatImagePreviewContainer", "gcatImagePreview", "psychometric/gcat");
        }
    });

    document.getElementById("sjtUploadBtn")?.addEventListener("click", () => {
        uploadMediaFile("sjtImageFileInput", "sjtImageUrl", null, "sjtImagePreviewContainer", "sjtImagePreview", "psychometric/sjt");
    });

    document.getElementById("sjtImageFileInput")?.addEventListener("change", (e) => {
        if (e.target.files && e.target.files.length > 0) {
            uploadMediaFile("sjtImageFileInput", "sjtImageUrl", null, "sjtImagePreviewContainer", "sjtImagePreview", "psychometric/sjt");
        }
    });

    // Option Image Upload Listeners (A - E) for GCAT
    document.querySelectorAll(".gcat-opt-upload-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const key = btn.getAttribute("data-key");
            uploadMediaFile(
                `gcatOptFile${key}`,
                `gcatOptImg${key}`,
                null,
                `gcatOptPreviewContainer${key}`,
                `gcatOptPreview${key}`,
                "psychometric/gcat/options"
            );
        });
    });

    // Auto-upload when file selected on option inputs
    ["A", "B", "C", "D", "E"].forEach(k => {
        const fileIn = document.getElementById(`gcatOptFile${k}`);
        if (fileIn) {
            fileIn.addEventListener("change", () => {
                if (fileIn.files && fileIn.files.length > 0) {
                    uploadMediaFile(
                        `gcatOptFile${k}`,
                        `gcatOptImg${k}`,
                        null,
                        `gcatOptPreviewContainer${k}`,
                        `gcatOptPreview${k}`,
                        "psychometric/gcat/options"
                    );
                }
            });
        }
    });

    // Option Image Remove Listeners
    document.querySelectorAll(".gcat-opt-remove-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const key = btn.getAttribute("data-key");
            const imgInput = document.getElementById(`gcatOptImg${key}`);
            if (imgInput) imgInput.value = "";

            const fileInput = document.getElementById(`gcatOptFile${key}`);
            if (fileInput) fileInput.value = "";

            const container = document.getElementById(`gcatOptPreviewContainer${key}`);
            if (container) container.classList.add("d-none");

            const preview = document.getElementById(`gcatOptPreview${key}`);
            if (preview) preview.src = "";
        });
    });
}

// ---------------------------------------------------------------------------
// 1. Taxonomy Loading
// ---------------------------------------------------------------------------
async function loadTaxonomies() {
    try {
        const headers = getAuthHeaders();

        // 1. Competencies
        const compRes = await fetch(`${API_BASE}/api/admin/taxonomies/competencies`, { headers });
        if (compRes.ok) taxonomies.competencies = await compRes.json();

        // 2. Derailer Types
        const derRes = await fetch(`${API_BASE}/api/admin/items/derailers/taxonomies/types`, { headers });
        if (derRes.ok) taxonomies.derailerTypes = await derRes.json();

        // 3. SJT Domains
        const domRes = await fetch(`${API_BASE}/api/admin/items/sjt/taxonomies/domains`, { headers });
        if (domRes.ok) taxonomies.sjtDomains = await domRes.json();

        populateTaxonomyDropdowns();
    } catch (err) {
        console.error("Error loading taxonomies:", err);
    }
}

function updateCompPillStyle(el, isSelected) {
    const isSD = el.dataset.isSd === "true";
    if (isSelected) {
        if (isSD) {
            el.className = "btn btn-warning text-white btn-sm mb-0 p-comp-drag shadow-xs";
        } else {
            el.className = "btn btn-info text-white btn-sm mb-0 p-comp-drag shadow-xs";
        }
    } else {
        if (isSD) {
            el.className = "btn btn-outline-warning btn-sm mb-0 p-comp-drag";
        } else {
            el.className = "btn btn-outline-info btn-sm mb-0 p-comp-drag";
        }
    }
}

function populateTaxonomyDropdowns() {
    // Personality Competency Dropdown
    const availBox = document.getElementById("availableCompetencies");
    const selBox = document.getElementById("selectedCompetencies");
    if (availBox && selBox) {
        availBox.innerHTML = "";
        selBox.innerHTML = "";
        taxonomies.competencies.forEach(c => {
            const el = document.createElement("div");
            const isSD = (c.code === "SOCIAL_DESIRABILITY" || (c.nameAr && c.nameAr.includes("التظاهر")));
            el.dataset.isSd = isSD ? "true" : "false";
            el.className = isSD ? "btn btn-outline-warning btn-sm mb-0 p-comp-drag" : "btn btn-outline-info btn-sm mb-0 p-comp-drag";
            el.draggable = true;
            el.dataset.id = c.id;
            el.innerText = c.nameAr;
            el.style.cursor = "grab";
            
            // Drag events
            el.addEventListener("dragstart", (e) => {
                e.dataTransfer.setData("text/plain", c.id);
                e.target.style.opacity = "0.5";
            });
            el.addEventListener("dragend", (e) => {
                e.target.style.opacity = "1";
            });
            
            // Click to move (for accessibility / ease of use) - Single selection rule
            el.addEventListener("click", () => {
                if (el.parentElement === availBox) {
                    // Return any previously selected competency back to available
                    Array.from(selBox.children).forEach(existing => {
                        availBox.appendChild(existing);
                        updateCompPillStyle(existing, false);
                    });
                    selBox.appendChild(el);
                    updateCompPillStyle(el, true);
                } else {
                    availBox.appendChild(el);
                    updateCompPillStyle(el, false);
                }
            });
            
            availBox.appendChild(el);
        });

        [availBox, selBox].forEach(box => {
            box.addEventListener("dragover", (e) => {
                e.preventDefault();
                box.style.opacity = "0.8";
            });
            box.addEventListener("dragleave", (e) => {
                box.style.opacity = "1";
            });
            box.addEventListener("drop", (e) => {
                e.preventDefault();
                box.style.opacity = "1";
                const id = e.dataTransfer.getData("text/plain");
                const el = document.querySelector(`.p-comp-drag[data-id="${id}"]`);
                if (el) {
                    if (box === selBox) {
                        // Return any previously selected competency back to available
                        Array.from(selBox.children).forEach(existing => {
                            if (existing !== el) {
                                availBox.appendChild(existing);
                                updateCompPillStyle(existing, false);
                            }
                        });
                    }
                    box.appendChild(el);
                    updateCompPillStyle(el, box === selBox);
                }
            });
        });
    }

    // Derailer Types Dropdown
    const dAvailBox = document.getElementById("availableDerailers");
    const dSelBox = document.getElementById("selectedDerailers");
    if (dAvailBox && dSelBox) {
        dAvailBox.innerHTML = "";
        dSelBox.innerHTML = "";
        taxonomies.derailerTypes.forEach(t => {
            const el = document.createElement("div");
            el.className = "btn btn-outline-warning btn-sm mb-0 d-type-drag";
            el.draggable = true;
            el.dataset.id = t.id;
            el.innerText = t.nameAr;
            el.style.cursor = "grab";
            
            el.addEventListener("dragstart", (e) => {
                e.dataTransfer.setData("text/plain", "d-" + t.id);
                e.target.style.opacity = "0.5";
            });
            el.addEventListener("dragend", (e) => {
                e.target.style.opacity = "1";
            });
            el.addEventListener("click", () => {
                if (el.parentElement === dAvailBox) dSelBox.appendChild(el);
                else dAvailBox.appendChild(el);
            });
            dAvailBox.appendChild(el);
        });

        [dAvailBox, dSelBox].forEach(box => {
            box.addEventListener("dragover", (e) => {
                e.preventDefault();
                box.style.opacity = "0.8";
            });
            box.addEventListener("dragleave", (e) => {
                box.style.opacity = "1";
            });
            box.addEventListener("drop", (e) => {
                e.preventDefault();
                box.style.opacity = "1";
                const data = e.dataTransfer.getData("text/plain");
                if (data.startsWith("d-")) {
                    const id = data.split("-")[1];
                    const el = document.querySelector(`.d-type-drag[data-id="${id}"]`);
                    if (el) box.appendChild(el);
                }
            });
        });
    }

    // SJT Domain Dropdown
    const sDomSelect = document.getElementById("sjtDomainId");
    if (sDomSelect) {
        sDomSelect.innerHTML = taxonomies.sjtDomains.map(d =>
            `<option value="${d.id}">${d.nameAr} (${d.code})</option>`
        ).join("");
    }
}

// ---------------------------------------------------------------------------
// 2. Load Dimension Data
// ---------------------------------------------------------------------------
async function loadDimensionData(dim) {
    const tableBody = document.getElementById("itemsTableBody");
    if (tableBody) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center py-5 text-secondary">
                    <div class="spinner-border spinner-border-sm text-dark me-2" role="status"></div>
                    <span>Loading ${dim} items...</span>
                </td>
            </tr>
        `;
    }

    updateTableHeaders(dim);

    try {
        let endpoint = "";
        if (dim === "personality") endpoint = `${API_BASE}/api/admin/items/personality`;
        else if (dim === "derailers") endpoint = `${API_BASE}/api/admin/items/derailers`;
        else if (dim === "cognitive") endpoint = `${API_BASE}/api/admin/items/cognitive`;
        else if (dim === "sjt") endpoint = `${API_BASE}/api/admin/items/sjt`;

        const res = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!res.ok) throw new Error(`HTTP Error ${res.status}`);

        rawItems = await res.json();
        updateStats(dim);
        applyFilters();
    } catch (err) {
        console.error(`Failed to load ${dim} items:`, err);
        if (tableBody) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center py-4 text-danger font-weight-bold">
                        Failed to load items. ${err.message}
                    </td>
                </tr>
            `;
        }
    }
}

function updateTableHeaders(dim) {
    const headersEl = document.getElementById("tableHeaders");
    if (!headersEl) return;

    if (dim === "personality") {
        headersEl.innerHTML = `
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-3" style="width: 50px;">#</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Competency (الجدارة)</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Statement (نص العبارة)</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Ideal Target</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Exam Mode</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Status</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3">Actions</th>
        `;
    } else if (dim === "derailers") {
        headersEl.innerHTML = `
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-3" style="width: 50px;">#</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Derailer Type (نوع السلوك المعطل)</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Statement (نص العبارة)</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Benchmark Target</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Exam Mode</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Status</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3">Actions</th>
        `;
    } else if (dim === "cognitive") {
        headersEl.innerHTML = `
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-3">Code / Subtest</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Title & Prompt (المسألة)</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Difficulty</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Correct Key</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Exam Mode</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Status</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3">Actions</th>
        `;
    } else if (dim === "sjt") {
        headersEl.innerHTML = `
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-3">Code / Domain</th>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">Scenario Narrative (الموقف الإداري)</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Complexity</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Best Key</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Exam Mode</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Status</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 pe-3">Actions</th>
        `;
    }
}

function updateStats(dim) {
    const total = rawItems.length;
    const active = rawItems.filter(i => i.active).length;
    const disabled = total - active;

    let taxCount = 0;
    let summaryLines = [];

    if (dim === "personality") {
        taxCount = taxonomies.competencies.length;
        const counts = {};
        taxonomies.competencies.forEach(c => counts[c.nameAr] = 0);
        rawItems.forEach(item => {
            if (item.competencyNamesAr) {
                item.competencyNamesAr.forEach(name => {
                    if (counts[name] !== undefined) counts[name]++;
                    else counts[name] = 1;
                });
            }
        });
        summaryLines = Object.entries(counts).map(([name, count]) => `${name}: ${count} سؤال`);
    } 
    else if (dim === "derailers") {
        taxCount = taxonomies.derailerTypes.length;
        const counts = {};
        taxonomies.derailerTypes.forEach(t => counts[t.nameAr] = 0);
        rawItems.forEach(item => {
            if (item.derailerTypeNamesAr) {
                item.derailerTypeNamesAr.forEach(name => {
                    if (counts[name] !== undefined) counts[name]++;
                    else counts[name] = 1;
                });
            }
        });
        summaryLines = Object.entries(counts).map(([name, count]) => `${name}: ${count} سؤال`);
    } 
    else if (dim === "cognitive") {
        taxCount = taxonomies.subtests.length;
        const counts = {};
        taxonomies.subtests.forEach(s => counts[s.nameAr] = { EASY: 0, MEDIUM: 0, HARD: 0 });
        
        rawItems.forEach(item => {
            const subtestObj = taxonomies.subtests.find(s => s.code === item.subtestCode);
            if (subtestObj) {
                const diff = item.difficulty || "MEDIUM";
                if (counts[subtestObj.nameAr][diff] !== undefined) {
                    counts[subtestObj.nameAr][diff]++;
                }
            }
        });
        
        summaryLines = Object.entries(counts).map(([name, diffs]) => {
            return `${name}: ${diffs.EASY} سهل، ${diffs.MEDIUM} متوسط، ${diffs.HARD} صعب`;
        });
    } 
    else if (dim === "sjt") {
        taxCount = taxonomies.sjtDomains.length;
        const counts = {};
        taxonomies.sjtDomains.forEach(d => counts[d.nameAr] = 0);
        rawItems.forEach(item => {
            if (item.domainNameAr) {
                if (counts[item.domainNameAr] !== undefined) counts[item.domainNameAr]++;
                else counts[item.domainNameAr] = 1;
            }
        });
        summaryLines = Object.entries(counts).map(([name, count]) => `${name}: ${count} سؤال`);
    }

    document.getElementById("statTotalItems").innerText = total;
    document.getElementById("statActiveItems").innerText = active;
    document.getElementById("statDisabledItems").innerText = disabled;
    
    const taxElement = document.getElementById("statTaxonomies");
    taxElement.innerText = taxCount;
    
    const cardElement = taxElement.closest('.card');
    if (cardElement && typeof bootstrap !== "undefined") {
        // Destroy existing tooltip if any
        let existingTooltip = bootstrap.Tooltip.getInstance(cardElement);
        if (existingTooltip) {
            existingTooltip.dispose();
        }

        if (summaryLines.length > 0) {
            cardElement.setAttribute('data-bs-toggle', 'tooltip');
            cardElement.setAttribute('data-bs-html', 'true');
            cardElement.setAttribute('data-bs-placement', 'bottom');
            cardElement.style.cursor = "help";
            
            // Ensure custom CSS exists for tooltip to prevent wrapping
            if (!document.getElementById("tax-tooltip-style")) {
                const style = document.createElement("style");
                style.id = "tax-tooltip-style";
                style.innerHTML = ".taxonomy-tooltip .tooltip-inner { max-width: none !important; text-align: right !important; }";
                document.head.appendChild(style);
            }

            // Format HTML for the tooltip
            const htmlContent = `
                <div class="p-1 arabic-text" style="line-height: 1.6; text-align: right; direction: rtl;">
                    <strong class="d-block mb-2 border-bottom border-light pb-1 text-white text-center">التوزيع حسب الفئة</strong>
                    ${summaryLines.map(line => `<div style="white-space: nowrap !important; margin-bottom: 2px;">${line}</div>`).join("")}
                </div>
            `;
            cardElement.setAttribute('title', htmlContent);
            
            // Re-initialize tooltip with customClass
            new bootstrap.Tooltip(cardElement, {
                customClass: 'taxonomy-tooltip'
            });
        } else {
            cardElement.removeAttribute("title");
            cardElement.removeAttribute("data-bs-toggle");
            cardElement.style.cursor = "default";
        }
    }
}

// ---------------------------------------------------------------------------
// 3. Search & Filters
// ---------------------------------------------------------------------------
function applyFilters() {
    const query = (document.getElementById("searchInput")?.value || "").toLowerCase().trim();
    const status = document.getElementById("statusFilter")?.value || "ALL";
    const mode = document.getElementById("modeFilter")?.value || "ALL";

    filteredItems = rawItems.filter(item => {
        // Status filter
        if (status === "ACTIVE" && !item.active) return false;
        if (status === "DISABLED" && item.active) return false;

        // Exam Mode filter
        if (mode !== "ALL" && item.examMode !== mode) return false;

        // Search query
        if (query) {
            const statement = (item.statementAr || item.titleAr || item.narrativeAr || item.promptTextAr || "").toLowerCase();
            const code = (item.itemCode || "").toLowerCase();
            const pCompNames = (item.competencyNamesAr || []).join(" ");
            const dTypeNames = (item.derailerTypeNamesAr || []).join(" ");
            const cat = (pCompNames || dTypeNames || item.subtestNameAr || item.domainNameAr || "").toLowerCase();
            if (!statement.includes(query) && !code.includes(query) && !cat.includes(query)) {
                return false;
            }
        }
        return true;
    });

    renderTable();
}

// ---------------------------------------------------------------------------
// 4. Render Table Rows
// ---------------------------------------------------------------------------
function renderTable() {
    const tableBody = document.getElementById("itemsTableBody");
    const countDisplay = document.getElementById("itemCountDisplay");
    if (!tableBody) return;

    if (countDisplay) {
        countDisplay.innerText = `Showing ${filteredItems.length} of ${rawItems.length} items`;
    }

    if (filteredItems.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center py-5 text-secondary">
                    <i class="material-symbols-rounded fs-1 opacity-5">search_off</i>
                    <p class="mb-0 text-sm">No items found matching the selected filters.</p>
                </td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = filteredItems.map((item, index) => {
        const statusBadge = item.active
            ? `<span class="badge badge-sm bg-gradient-success">Active</span>`
            : `<span class="badge badge-sm bg-gradient-secondary">Disabled</span>`;

        const toggleActionBtn = item.active
            ? `<button class="btn btn-link text-warning p-1 mb-0 action-toggle" data-id="${item.id}" data-action="disable" title="Disable item">
                 <i class="material-symbols-rounded text-sm">pause_circle</i>
               </button>`
            : `<button class="btn btn-link text-success p-1 mb-0 action-toggle" data-id="${item.id}" data-action="enable" title="Enable item">
                 <i class="material-symbols-rounded text-sm">play_circle</i>
               </button>`;

        const deleteActionBtn = `<button class="btn btn-link text-danger p-1 mb-0 action-delete" data-id="${item.id}" title="Delete item">
                 <i class="material-symbols-rounded text-sm">delete</i>
               </button>`;

        let col1 = "", col2 = "", col3 = "", col4 = "";

        if (currentDimension === "personality") {
            let compNamesHtml = "N/A";
            if (item.competencyNamesAr && item.competencyNamesAr.length > 0) {
                compNamesHtml = item.competencyNamesAr.map(name => {
                    if (name.includes("التظاهر")) {
                        return `<span class="badge badge-sm bg-gradient-warning text-white me-1">${name}</span>`;
                    }
                    return `<span class="badge badge-sm bg-gradient-info text-white me-1">${name}</span>`;
                }).join(" ");
            }
            col1 = `<div class="d-flex flex-wrap gap-1">${compNamesHtml}</div>`;
            col2 = `<p class="text-xs arabic-text mb-0 truncate-2-lines">${item.statementAr}</p>`;
            col3 = `<span class="badge badge-sm bg-gradient-info">${item.idealTarget} / 5</span>`;
            col4 = "";
        } else if (currentDimension === "derailers") {
            const typeNames = (item.derailerTypeNamesAr && item.derailerTypeNamesAr.length > 0) ? item.derailerTypeNamesAr.join(" • ") : "N/A";
            col1 = `<span class="text-xs font-weight-bold text-dark">${typeNames}</span>`;
            col2 = `<p class="text-xs arabic-text mb-0 truncate-2-lines">${item.statementAr}</p>`;
            col3 = `<span class="badge badge-sm bg-gradient-warning">Target: ${item.idealTarget}</span>`;
            col4 = "";
        } else if (currentDimension === "cognitive") {
            col1 = `<span class="text-xs font-weight-bold text-dark">${item.itemCode}</span><br><small class="text-xxs text-secondary">${item.subtestCode}</small>`;
            col2 = `<h6 class="text-xs arabic-text mb-0 font-weight-bold">${item.titleAr}</h6><small class="text-xxs text-secondary arabic-text">${item.promptTextAr || ""}</small>`;
            col3 = `<span class="badge badge-sm bg-light text-dark border">${item.difficulty}</span>`;
            col4 = `<span class="badge badge-sm bg-gradient-primary">Key: ${item.correctOptionKey}</span>`;
        } else if (currentDimension === "sjt") {
            col1 = `<span class="text-xs font-weight-bold text-dark">${item.itemCode}</span><br><small class="text-xxs text-secondary">${item.domainNameAr || ""}</small>`;
            col2 = `<h6 class="text-xs arabic-text mb-0 font-weight-bold">${item.titleAr}</h6><p class="text-xxs arabic-text text-secondary mb-0 truncate-2-lines">${item.narrativeAr}</p>`;
            col3 = `<span class="badge badge-sm bg-light text-dark border">${item.complexity}</span>`;
            col4 = `<span class="badge badge-sm bg-gradient-success">Best: ${item.bestOptionKey}</span>`;
        }

        let rowHtml = "";
        if (currentDimension === "personality" || currentDimension === "derailers") {
            rowHtml = `
            <tr>
                <td class="align-middle text-center"><span class="text-xs font-weight-bold text-secondary">${index + 1}</span></td>
                <td class="align-middle">${col1}</td>
                <td class="align-middle">${col2}</td>
                <td class="align-middle text-center">${col3}</td>
                <td class="align-middle text-center"><span class="text-secondary text-xs font-weight-bold">${item.examMode || 'BOTH'}</span></td>
                <td class="align-middle text-center">${statusBadge}</td>
                <td class="align-middle text-center pe-3">
                  <button class="btn btn-link text-dark p-1 mb-0 action-edit" data-id="${item.id}" title="Edit item">
                    <i class="material-symbols-rounded text-sm">edit</i>
                  </button>
                  ${toggleActionBtn}
                  ${deleteActionBtn}
                </td>
            </tr>`;
        } else {
            rowHtml = `
            <tr>
                <td class="align-middle text-center">${col1}</td>
                <td class="align-middle">${col2}</td>
                <td class="align-middle">${col3}</td>
                <td class="align-middle text-center">${col4}</td>
                <td class="align-middle text-center"><span class="text-secondary text-xs font-weight-bold">${item.examMode || 'BOTH'}</span></td>
                <td class="align-middle text-center">${statusBadge}</td>
                <td class="align-middle text-center pe-3">
                  <button class="btn btn-link text-dark p-1 mb-0 action-edit" data-id="${item.id}" title="Edit item">
                    <i class="material-symbols-rounded text-sm">edit</i>
                  </button>
                  ${toggleActionBtn}
                  ${deleteActionBtn}
                </td>
            </tr>`;
        }
        return rowHtml;
    }).join("");

    attachActionHandlers();
}

function attachActionHandlers() {
    // Edit Item
    document.querySelectorAll(".action-edit").forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.getAttribute("data-id");
            openEditModal(id);
        });
    });

    // Toggle Enable / Disable
    document.querySelectorAll(".action-toggle").forEach(btn => {
        btn.addEventListener("click", async () => {
            const id = btn.getAttribute("data-id");
            const action = btn.getAttribute("data-action"); // 'enable' | 'disable'
            await toggleItemStatus(id, action);
        });
    });

    // Delete / Soft Delete
    document.querySelectorAll(".action-delete").forEach(btn => {
        btn.addEventListener("click", async () => {
            const id = btn.getAttribute("data-id");
            await softDeleteItem(id);
        });
    });
}

// ---------------------------------------------------------------------------
// 5. Actions (Add, Edit, Toggle, Delete)
// ---------------------------------------------------------------------------
function openAddModal() {
    if (currentDimension === "personality") {
        document.getElementById("personalityForm").reset();
        document.getElementById("pItemId").value = "";
        
        // Reset pills to available
        const availBox = document.getElementById("availableCompetencies");
        document.querySelectorAll(".p-comp-drag").forEach(pill => {
            availBox.appendChild(pill);
            updateCompPillStyle(pill, false);
        });
        document.getElementById("personalityModalTitle").innerText = "Add Personality Item (PQ10)";
        personalityModal?.show();
    } else if (currentDimension === "derailers") {
        document.getElementById("derailerForm").reset();
        document.getElementById("dItemId").value = "";
        
        // Reset pills to available
        const availBox = document.getElementById("availableDerailers");
        document.querySelectorAll(".d-type-drag").forEach(pill => {
            availBox.appendChild(pill);
            pill.classList.add("btn-outline-warning");
            pill.classList.remove("btn-warning", "text-white");
        });
        
        document.getElementById("derailerModalTitle").innerText = "Add Derailer Item (Dark Traits)";
        derailerModal?.show();
    } else if (currentDimension === "cognitive") {
        document.getElementById("gcatForm").reset();
        document.getElementById("gcatId").value = "";
        document.getElementById("gcatImagePreviewContainer").classList.add("d-none");
        document.getElementById("gcatImagePreview").src = "";
        
        // Reset 5 options texts, files, hidden URLs, and previews
        ["A", "B", "C", "D", "E"].forEach(k => {
            const txt = document.getElementById(`gcatOptText${k}`);
            if (txt) txt.value = "";
            const file = document.getElementById(`gcatOptFile${k}`);
            if (file) file.value = "";
            const img = document.getElementById(`gcatOptImg${k}`);
            if (img) img.value = "";
            const container = document.getElementById(`gcatOptPreviewContainer${k}`);
            if (container) container.classList.add("d-none");
            const prev = document.getElementById(`gcatOptPreview${k}`);
            if (prev) prev.src = "";
        });

        document.getElementById("gcatModalTitle").innerText = "Add Cognitive Question (GCAT)";
        gcatModal?.show();
    } else if (currentDimension === "sjt") {
        document.getElementById("sjtForm").reset();
        document.getElementById("sjtId").value = "";
        document.getElementById("sjtImagePreviewContainer").classList.add("d-none");
        document.getElementById("sjtImagePreview").src = "";

        // Reset 4 options
        const defaultScores = { A: 4, B: 3, C: 2, D: 1 };
        ["A", "B", "C", "D"].forEach(k => {
            const txt = document.getElementById(`sjtOptText${k}`);
            if (txt) txt.value = "";
            const score = document.getElementById(`sjtOptScore${k}`);
            if (score) score.value = defaultScores[k] || 1;
            const rat = document.getElementById(`sjtOptRationale${k}`);
            if (rat) rat.value = "";
        });

        document.getElementById("sjtModalTitle").innerText = "Add Situational Judgment Scenario (SJT)";
        sjtModal?.show();
    }
}

function openEditModal(id) {
    const item = rawItems.find(i => String(i.id) === String(id));
    if (!item) return;

    if (currentDimension === "personality") {
        document.getElementById("pItemId").value = item.id;
        document.getElementById("pStatementAr").value = item.statementAr || "";
        document.getElementById("pJustificationAr").value = item.justificationAr || "";
        const availBox = document.getElementById("availableCompetencies");
        const selBox = document.getElementById("selectedCompetencies");
        const allPills = document.querySelectorAll(".p-comp-drag");
        
        // Reset all pills to available
        allPills.forEach(pill => {
            availBox.appendChild(pill);
            updateCompPillStyle(pill, false);
        });

        // Select strictly one competency (the first one if legacy data had multiple)
        const primaryCompId = (item.competencyIds && item.competencyIds.length > 0) ? Number(item.competencyIds[0]) : null;
        if (primaryCompId) {
            const selectedPill = document.querySelector(`.p-comp-drag[data-id="${primaryCompId}"]`);
            if (selectedPill) {
                selBox.appendChild(selectedPill);
                updateCompPillStyle(selectedPill, true);
            }
        }

        document.getElementById("pIdealTarget").value = item.idealTarget;
        document.getElementById("pExamMode").value = item.examMode || "FULL";
        document.getElementById("personalityModalTitle").innerText = `Edit Personality Item #${item.id}`;
        personalityModal?.show();
    } else if (currentDimension === "derailers") {
        document.getElementById("dItemId").value = item.id;
        document.getElementById("dStatementAr").value = item.statementAr || "";
        document.getElementById("dJustificationAr").value = item.justificationAr || "";
        
        const availBox = document.getElementById("availableDerailers");
        const selBox = document.getElementById("selectedDerailers");
        const allPills = document.querySelectorAll(".d-type-drag");
        const selectedIds = item.derailerTypeIds || [];
        
        allPills.forEach(pill => {
            const id = Number(pill.dataset.id);
            if (selectedIds.includes(id)) {
                selBox.appendChild(pill);
                pill.classList.remove("btn-outline-warning");
                pill.classList.add("btn-warning", "text-white");
            } else {
                availBox.appendChild(pill);
                pill.classList.add("btn-outline-warning");
                pill.classList.remove("btn-warning", "text-white");
            }
        });
        
        document.getElementById("dIdealTarget").value = item.idealTarget;
        document.getElementById("dExamMode").value = item.examMode || "FULL";
        document.getElementById("derailerModalTitle").innerText = `Edit Derailer Item #${item.id}`;
        derailerModal?.show();
    } else if (currentDimension === "cognitive") {
        document.getElementById("gcatId").value = item.id;
        document.getElementById("gcatItemCode").value = item.itemCode || "";
        document.getElementById("gcatSubtestCode").value = item.subtestCode || "ABSTRACT";
        document.getElementById("gcatDifficulty").value = item.difficulty || "MEDIUM";
        document.getElementById("gcatExamMode").value = item.examMode || "FULL";
        document.getElementById("gcatTitleAr").value = item.titleAr || "";
        document.getElementById("gcatPatternTypeAr").value = item.patternTypeAr || "";
        document.getElementById("gcatPromptTextAr").value = item.promptTextAr || "";
        document.getElementById("gcatImageUrl").value = item.questionImageUrl || "";
        document.getElementById("gcatObservationAr").value = item.observationAr || "";
        document.getElementById("gcatRuleAr").value = item.ruleAr || "";
        document.getElementById("gcatApplicationAr").value = item.applicationAr || "";
        document.getElementById("gcatCorrectOptionKey").value = item.correctOptionKey || "A";
        
        const previewContainer = document.getElementById("gcatImagePreviewContainer");
        const previewImg = document.getElementById("gcatImagePreview");
        if (item.questionImageUrl) {
            previewImg.src = item.questionImageUrl;
            previewContainer.classList.remove("d-none");
        } else {
            previewContainer.classList.add("d-none");
            previewImg.src = "";
        }

        // Populate options A - E
        ["A", "B", "C", "D", "E"].forEach(k => {
            const opt = (item.options || []).find(o => o.optionKey === k);
            const txt = document.getElementById(`gcatOptText${k}`);
            if (txt) txt.value = opt ? (opt.optionTextAr || "") : "";

            const imgVal = opt ? (opt.optionImageUrl || "") : "";
            const imgInput = document.getElementById(`gcatOptImg${k}`);
            if (imgInput) imgInput.value = imgVal;

            const optContainer = document.getElementById(`gcatOptPreviewContainer${k}`);
            const optPrev = document.getElementById(`gcatOptPreview${k}`);
            if (imgVal && optContainer && optPrev) {
                optPrev.src = imgVal;
                optContainer.classList.remove("d-none");
            } else if (optContainer && optPrev) {
                optContainer.classList.add("d-none");
                optPrev.src = "";
            }
        });

        document.getElementById("gcatModalTitle").innerText = `Edit Cognitive Question (${item.itemCode})`;
        gcatModal?.show();
    } else if (currentDimension === "sjt") {
        document.getElementById("sjtId").value = item.id;
        document.getElementById("sjtItemCode").value = item.itemCode || "";
        document.getElementById("sjtDomainId").value = item.domainId;
        document.getElementById("sjtComplexity").value = item.complexity || "TRADE_OFF";
        document.getElementById("sjtExamMode").value = item.examMode || "FULL";
        document.getElementById("sjtTitleAr").value = item.titleAr || "";
        document.getElementById("sjtNarrativeAr").value = item.narrativeAr || "";
        document.getElementById("sjtBestOptionKey").value = item.bestOptionKey || "A";
        document.getElementById("sjtRationaleAr").value = item.rationaleAr || "";
        document.getElementById("sjtCommonMistakeAr").value = item.commonMistakeAr || "";
        document.getElementById("sjtCoachingNoteAr").value = item.coachingNoteAr || "";
        document.getElementById("sjtImageUrl").value = item.scenarioImageUrl || "";

        const previewContainer = document.getElementById("sjtImagePreviewContainer");
        const previewImg = document.getElementById("sjtImagePreview");
        if (item.scenarioImageUrl) {
            previewImg.src = item.scenarioImageUrl;
            previewContainer.classList.remove("d-none");
        } else {
            previewContainer.classList.add("d-none");
            previewImg.src = "";
        }

        // Populate options A - D
        const defaultScores = { A: 4, B: 3, C: 2, D: 1 };
        ["A", "B", "C", "D"].forEach(k => {
            const opt = (item.options || []).find(o => o.optionKey === k);
            const txt = document.getElementById(`sjtOptText${k}`);
            if (txt) txt.value = opt ? (opt.actionTextAr || "") : "";
            const score = document.getElementById(`sjtOptScore${k}`);
            if (score) score.value = opt ? opt.effectivenessScore : (defaultScores[k] || 1);
            const rat = document.getElementById(`sjtOptRationale${k}`);
            if (rat) rat.value = opt ? (opt.scoringRationaleAr || "") : "";
        });

        document.getElementById("sjtModalTitle").innerText = `Edit SJT Scenario (${item.itemCode})`;
        sjtModal?.show();
    }
}

async function uploadMediaFile(fileInputId, urlInputId, publicIdInputId, previewContainerId, previewImgId, folder) {
    const fileInput = document.getElementById(fileInputId);
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast("Please select an image file first.", "warning");
        } else {
            alert("Please select an image file first.");
        }
        return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);
    if (folder) {
        formData.append("folder", folder);
    }

    try {
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast("Uploading asset to Cloudinary CDN...", "info");
        }

        const user = getUser();
        const res = await fetch(`${API_BASE}/api/admin/media/upload`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${user.token || ""}`
            },
            body: formData
        });

        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.message || "Failed to upload image to CDN.");
        }

        const data = await res.json();
        const mediaUrl = data.url || data.secureUrl || "";
        const urlInput = document.getElementById(urlInputId);
        if (urlInput) urlInput.value = mediaUrl;

        if (publicIdInputId) {
            const pubInput = document.getElementById(publicIdInputId);
            if (pubInput) pubInput.value = data.publicId || "";
        }

        const previewContainer = document.getElementById(previewContainerId);
        const previewImg = document.getElementById(previewImgId);
        if (previewImg) previewImg.src = mediaUrl;
        if (previewContainer) previewContainer.classList.remove("d-none");

        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast("Image uploaded to Cloudinary successfully!", "success");
        }
    } catch (err) {
        console.error("Cloudinary upload error:", err);
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(err.message, "danger");
        } else {
            alert(err.message);
        }
    }
}

async function toggleItemStatus(id, action) {
    const endpoint = getEndpoint(currentDimension);
    try {
        const res = await fetch(`${endpoint}/${id}/${action}`, {
            method: "PATCH",
            headers: getAuthHeaders()
        });

        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.message || `Failed to ${action} item`);
        }

        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(`Item #${id} successfully ${action}d.`, "success");
        }
        await loadDimensionData(currentDimension);
    } catch (err) {
        console.error(`Toggle error:`, err);
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(err.message, "danger");
        } else {
            alert(err.message);
        }
    }
}

async function softDeleteItem(id) {
    const item = rawItems.find(i => String(i.id) === String(id));
    const isAlreadyDisabled = item && !item.active;

    const promptText = isAlreadyDisabled
        ? `This disabled item (#${id}) will be PERMANENTLY deleted from the item bank. Proceed?`
        : `Are you sure you want to deactivate item #${id}? It will be removed from live rotation.`;

    const confirmed = confirm(promptText);
    if (!confirmed) return;

    const endpoint = getEndpoint(currentDimension);
    try {
        const res = await fetch(`${endpoint}/${id}`, {
            method: "DELETE",
            headers: getAuthHeaders()
        });

        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.message || "Failed to delete item");
        }

        const successMsg = isAlreadyDisabled
            ? `Item #${id} permanently deleted from database.`
            : `Item #${id} deactivated and removed from active rotation.`;

        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(successMsg, "success");
        }
        await loadDimensionData(currentDimension);
    } catch (err) {
        console.error(`Delete error:`, err);
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(err.message, "danger");
        } else {
            alert(err.message);
        }
    }
}

function getEndpoint(dim) {
    if (dim === "personality") return `${API_BASE}/api/admin/items/personality`;
    if (dim === "derailers") return `${API_BASE}/api/admin/items/derailers`;
    if (dim === "cognitive") return `${API_BASE}/api/admin/items/cognitive`;
    if (dim === "sjt") return `${API_BASE}/api/admin/items/sjt`;
    return "";
}

// ---------------------------------------------------------------------------
// 6. Form Submit Handlers
// ---------------------------------------------------------------------------
async function handlePersonalitySubmit(e) {
    e.preventDefault();
    const id = document.getElementById("pItemId").value;
    const selectedPills = document.querySelectorAll("#selectedCompetencies .p-comp-drag");
    const selectedCompetencyIds = Array.from(selectedPills).map(pill => Number(pill.dataset.id));
    
    if (selectedCompetencyIds.length === 0) {
        alert("Please select one competency for this question (drag or click a competency).");
        return;
    }

    // Strictly assign exactly one competency
    const body = {
        statementAr: document.getElementById("pStatementAr").value,
        justificationAr: document.getElementById("pJustificationAr").value,
        competencyIds: [selectedCompetencyIds[0]],
        idealTarget: Number(document.getElementById("pIdealTarget").value),
        examMode: document.getElementById("pExamMode").value
    };

    const isEdit = Boolean(id);
    const url = isEdit ? `${API_BASE}/api/admin/items/personality/${id}` : `${API_BASE}/api/admin/items/personality`;
    const method = isEdit ? "PUT" : "POST";

    await submitItemForm(url, method, body, personalityModal);
}

async function handleDerailerSubmit(e) {
    e.preventDefault();
    const id = document.getElementById("dItemId").value;
    const selectedPills = document.querySelectorAll("#selectedDerailers .d-type-drag");
    const selectedTypeIds = Array.from(selectedPills).map(pill => Number(pill.dataset.id));
    
    if (selectedTypeIds.length === 0) {
        alert("Please select at least one derailer type.");
        return;
    }

    const body = {
        statementAr: document.getElementById("dStatementAr").value,
        justificationAr: document.getElementById("dJustificationAr").value,
        derailerTypeIds: selectedTypeIds,
        idealTarget: Number(document.getElementById("dIdealTarget").value),
        responseScaleType: "FREQUENCY",
        examMode: document.getElementById("dExamMode").value
    };

    const isEdit = Boolean(id);
    const url = isEdit ? `${API_BASE}/api/admin/items/derailers/${id}` : `${API_BASE}/api/admin/items/derailers`;
    const method = isEdit ? "PUT" : "POST";

    await submitItemForm(url, method, body, derailerModal);
}

async function handleGcatSubmit(e) {
    e.preventDefault();
    const id = document.getElementById("gcatId").value;
    const correctOptionKey = document.getElementById("gcatCorrectOptionKey").value;

    const rawOptions = ["A", "B", "C", "D", "E"].map(k => ({
        optionKey: k,
        optionTextAr: (document.getElementById(`gcatOptText${k}`)?.value || "").trim(),
        optionImageUrl: (document.getElementById(`gcatOptImg${k}`)?.value || "").trim() || null,
        isCorrect: k === correctOptionKey
    }));

    // Filter out Option E if empty (A-D are primary, E is optional)
    const options = rawOptions.filter(opt => opt.optionKey !== "E" || opt.optionTextAr || opt.optionImageUrl);

    const body = {
        itemCode: document.getElementById("gcatItemCode").value.trim(),
        subtestCode: document.getElementById("gcatSubtestCode").value,
        titleAr: document.getElementById("gcatTitleAr").value.trim(),
        promptTextAr: document.getElementById("gcatPromptTextAr").value.trim(),
        questionImageUrl: document.getElementById("gcatImageUrl").value.trim() || null,
        questionImagePublicId: document.getElementById("gcatImagePublicId")?.value.trim() || null,
        difficulty: document.getElementById("gcatDifficulty").value,
        patternTypeAr: document.getElementById("gcatPatternTypeAr").value.trim() || null,
        observationAr: document.getElementById("gcatObservationAr").value.trim() || null,
        ruleAr: document.getElementById("gcatRuleAr").value.trim() || null,
        applicationAr: document.getElementById("gcatApplicationAr").value.trim() || null,
        correctOptionKey: correctOptionKey,
        examMode: document.getElementById("gcatExamMode").value,
        options: options
    };

    const isEdit = Boolean(id);
    const url = isEdit ? `${API_BASE}/api/admin/items/cognitive/${id}` : `${API_BASE}/api/admin/items/cognitive`;
    const method = isEdit ? "PUT" : "POST";

    await submitItemForm(url, method, body, gcatModal);
}

async function handleSjtSubmit(e) {
    e.preventDefault();
    const id = document.getElementById("sjtId").value;
    const bestOptionKey = document.getElementById("sjtBestOptionKey").value;

    const options = ["A", "B", "C", "D"].map(k => ({
        optionKey: k,
        actionTextAr: (document.getElementById(`sjtOptText${k}`)?.value || "").trim(),
        effectivenessScore: Number(document.getElementById(`sjtOptScore${k}`)?.value || 1),
        scoringRationaleAr: (document.getElementById(`sjtOptRationale${k}`)?.value || "").trim() || null,
        isBestAction: k === bestOptionKey
    }));

    const body = {
        itemCode: document.getElementById("sjtItemCode").value.trim(),
        domainId: Number(document.getElementById("sjtDomainId").value),
        titleAr: document.getElementById("sjtTitleAr").value.trim(),
        narrativeAr: document.getElementById("sjtNarrativeAr").value.trim(),
        complexity: document.getElementById("sjtComplexity").value,
        scenarioImageUrl: document.getElementById("sjtImageUrl")?.value.trim() || null,
        bestOptionKey: bestOptionKey,
        rationaleAr: document.getElementById("sjtRationaleAr").value.trim() || null,
        commonMistakeAr: document.getElementById("sjtCommonMistakeAr").value.trim() || null,
        coachingNoteAr: document.getElementById("sjtCoachingNoteAr").value.trim() || null,
        examMode: document.getElementById("sjtExamMode").value,
        options: options
    };

    const isEdit = Boolean(id);
    const url = isEdit ? `${API_BASE}/api/admin/items/sjt/${id}` : `${API_BASE}/api/admin/items/sjt`;
    const method = isEdit ? "PUT" : "POST";

    await submitItemForm(url, method, body, sjtModal);
}

async function submitItemForm(url, method, body, modalInstance) {
    try {
        const res = await fetch(url, {
            method: method,
            headers: getAuthHeaders(),
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            let errorMsg = "Failed to save item.";
            try {
                const err = await res.json();
                errorMsg = err.message || errorMsg;
                if (err.fieldErrors) {
                    const fields = Object.entries(err.fieldErrors).map(([k, v]) => `${k}: ${v}`).join(", ");
                    errorMsg += ` (${fields})`;
                }
            } catch {
                errorMsg = `Server error ${res.status}: ${res.statusText}`;
            }
            throw new Error(errorMsg);
        }

        modalInstance?.hide();
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast("Item saved successfully!", "success");
        }
        await loadDimensionData(currentDimension);
    } catch (err) {
        console.error("Form error:", err);
        if (AdminUI && AdminUI.showToast) {
            AdminUI.showToast(err.message, "danger");
        } else {
            alert(err.message);
        }
    }
}
