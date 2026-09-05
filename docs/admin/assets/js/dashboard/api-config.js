const isLocal = typeof window !== 'undefined' && (
    window.location.hostname === 'localhost' ||
    window.location.hostname === '127.0.0.1' ||
    window.location.protocol === 'file:'
);

export const API_BASE = isLocal 
    ? 'http://localhost:8081' 
    : 'https://revised-port-means-musicians.trycloudflare.com';
