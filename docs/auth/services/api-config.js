const origin = (typeof window !== 'undefined' && window.location && window.location.origin) ? window.location.origin : 'http://localhost:8081';
export const API_BASE = (origin.includes(':8081')) ? origin : 'http://localhost:8081';
