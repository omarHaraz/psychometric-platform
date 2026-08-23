const origin = (typeof window !== 'undefined' && window.location && window.location.origin) ? window.location.origin : 'http://localhost:8081';
export const API_BASE = (origin.includes('5500') || origin.includes('63342') || origin.includes('8085') || origin.startsWith('file')) ? 'http://localhost:8081' : origin;
