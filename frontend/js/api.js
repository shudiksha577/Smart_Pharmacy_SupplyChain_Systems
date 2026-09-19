/**
 * ===================================================================
 * Smart Pharmacy Supply Chain System - Frontend API Client & UI Helpers
 * Pure Vanilla JavaScript (Fetch API)
 * ===================================================================
 */

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Generic Fetch Wrapper handling JSON headers and unified ApiResponse format
 */
async function request(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;
  const defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  };

  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers
    }
  };

  try {
    const response = await fetch(url, config);
    const result = await response.json();

    if (!response.ok) {
      const errorMessage = result.message || `Request failed with status ${response.status}`;
      throw new Error(errorMessage);
    }

    return result;
  } catch (error) {
    console.error(`API Error on [${options.method || 'GET'} ${endpoint}]:`, error);
    showToast(error.message, 'error');
    throw error;
  }
}

// REST CRUD Helpers
const API = {
  // Dashboard
  getDashboardStats: () => request('/dashboard/stats'),

  // Medicines
  getMedicines: () => request('/medicines'),
  getMedicine: (id) => request(`/medicines/${id}`),
  createMedicine: (data) => request('/medicines', { method: 'POST', body: JSON.stringify(data) }),
  updateMedicine: (id, data) => request(`/medicines/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteMedicine: (id) => request(`/medicines/${id}`, { method: 'DELETE' }),

  // Suppliers
  getSuppliers: () => request('/suppliers'),
  getSupplier: (id) => request(`/suppliers/${id}`),
  createSupplier: (data) => request('/suppliers', { method: 'POST', body: JSON.stringify(data) }),
  updateSupplier: (id, data) => request(`/suppliers/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteSupplier: (id) => request(`/suppliers/${id}`, { method: 'DELETE' }),

  // Batches
  getBatches: () => request('/batches'),
  getBatch: (id) => request(`/batches/${id}`),
  getAvailableBatches: (medicineId) => request(`/batches/medicine/${medicineId}/available`),
  createBatch: (data) => request('/batches', { method: 'POST', body: JSON.stringify(data) }),
  updateBatch: (id, data) => request(`/batches/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteBatch: (id) => request(`/batches/${id}`, { method: 'DELETE' }),
  updateBatchStatus: (id, status) => request(`/batches/${id}/status?status=${status}`, { method: 'PATCH' }),

  // Batch Verification
  verifyBatch: (data) => request('/batches/verify', { method: 'POST', body: JSON.stringify(data) }),

  // Expiry Alerts
  getExpiryAlerts: (category = '') => {
    const query = category ? `?category=${category}` : '';
    return request(`/batches/expiry-alerts${query}`);
  },

  // FEFO Dispense
  dispenseMedicine: (data) => request('/dispense', { method: 'POST', body: JSON.stringify(data) }),
  getDispenseRecords: () => request('/dispense'),

  // Temperature Logs
  getTemperatureLogs: () => request('/temperature-logs'),
  recordTemperature: (data) => request('/temperature-logs', { method: 'POST', body: JSON.stringify(data) }),
  getTemperatureAlerts: () => request('/temperature-logs/alerts')
};

/**
 * Toast Notification System
 */
function showToast(message, type = 'info') {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;

  let icon = 'ℹ️';
  if (type === 'success') icon = '✅';
  if (type === 'error') icon = '❌';
  if (type === 'warning') icon = '⚠️';

  toast.innerHTML = `
    <span>${icon}</span>
    <div>${message}</div>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease-out';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

/**
 * Modal Dialog Helpers
 */
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add('active');
  }
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove('active');
  }
}

/**
 * Formatting & Badge Helpers
 */
function formatDate(dateStr) {
  if (!dateStr) return '-';
  const parts = dateStr.split('-');
  if (parts.length === 3) {
    const d = new Date(parts[0], parts[1] - 1, parts[2]);
    return d.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }
  return dateStr;
}

function formatDateTime(dtStr) {
  if (!dtStr) return '-';
  const d = new Date(dtStr);
  return d.toLocaleString('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function getVerificationBadge(status) {
  switch (status) {
    case 'VERIFIED':
      return '<span class="badge badge-verified">VERIFIED</span>';
    case 'UNVERIFIED':
      return '<span class="badge badge-unverified">UNVERIFIED</span>';
    case 'REJECTED':
      return '<span class="badge badge-rejected">REJECTED</span>';
    default:
      return `<span class="badge badge-safe">${status || 'UNKNOWN'}</span>`;
  }
}

function getComplianceBadge(status) {
  switch (status) {
    case 'COMPLIANT':
      return '<span class="badge badge-compliant">COMPLIANT</span>';
    case 'OUT_OF_RANGE':
      return '<span class="badge badge-out-of-range">OUT OF RANGE</span>';
    default:
      return `<span class="badge">${status}</span>`;
  }
}

function getAlertBadge(category, daysRemaining) {
  switch (category) {
    case 'EXPIRED':
      return '<span class="badge badge-expired">EXPIRED</span>';
    case 'CRITICAL_30':
      return `<span class="badge badge-critical">${daysRemaining}d Left (Critical)</span>`;
    case 'WARNING_60':
      return `<span class="badge badge-warning">${daysRemaining}d Left (60d)</span>`;
    case 'NOTICE_90':
      return `<span class="badge badge-notice">${daysRemaining}d Left (90d)</span>`;
    case 'SAFE':
      return '<span class="badge badge-safe">Safe (>90d)</span>';
    default:
      return `<span class="badge">${category}</span>`;
  }
}
