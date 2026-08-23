import { API_BASE } from "../config/api-config.js";

const axios = {
    async post(url, data, config = {}) {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...(config.headers || {}) },
            body: JSON.stringify(data),
            credentials: config.withCredentials ? 'include' : 'same-origin'
        });
        const responseData = await res.json().catch(() => ({}));
        if (!res.ok) {
            const err = new Error(responseData.message || `HTTP ${res.status}`);
            err.response = { status: res.status, data: responseData };
            throw err;
        }
        return { data: responseData };
    },
    async get(url, config = {}) {
        const res = await fetch(url, {
            method: 'GET',
            headers: { ...(config.headers || {}) },
            credentials: config.withCredentials ? 'include' : 'same-origin'
        });
        const responseData = await res.json().catch(() => ({}));
        if (!res.ok) {
            const err = new Error(responseData.message || `HTTP ${res.status}`);
            err.response = { status: res.status, data: responseData };
            throw err;
        }
        return { data: responseData };
    }
};

const API_URL = `${API_BASE}/api/auth/`;

class AuthService {

    // ==========================
    // Authentication
    // ==========================

    async login(email, password) {

        const response = await axios.post(API_URL + "login", {
            email,
            password
        }, {
            withCredentials: true
        });

        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
    }

    logout() {
        localStorage.removeItem("user");
    }

    // ==========================
    // Signup
    // ==========================

    async requestOtp(signupData) {

        const response = await axios.post(
            API_URL + "request-otp",
            signupData,
            { withCredentials: true }
        );

        return response.data;
    }

    async verifyOtp(email, code) {

        const response = await axios.post(
            API_URL + "verify-otp",
            {
                email,
                code
            },
            { withCredentials: true }
        );

        if (response.data.token) {
            localStorage.setItem("user", JSON.stringify(response.data));
        }

        return response.data;
    }

    async resendOtp(email) {

        const response = await axios.post(
            API_URL + "resend-otp",
            {
                email
            },
            { withCredentials: true }
        );

        return response.data;
    }

    // ==========================
    // User
    // ==========================

    getCurrentUser() {

        const user = localStorage.getItem("user");

        return user ? JSON.parse(user) : null;
    }

    getToken() {
        const userObj = this.getCurrentUser();
        if (userObj && userObj.token) return userObj.token;
        return localStorage.getItem("jwt_token") || sessionStorage.getItem("jwt_token") || localStorage.getItem("token") || sessionStorage.getItem("token") || null;
    }

    getAuthHeader() {

        const token = this.getToken();

        return token
            ? {
                  Authorization: `Bearer ${token}`
              }
            : {};
    }

    isLoggedIn() {
        return this.getToken() !== null;
    }

    async validateSession() {

        const token = this.getToken();

        if (!token) {
            return null;
        }

        try {

            const response = await axios.get(
                API_URL + "me",
                {
                    headers: this.getAuthHeader(),
                    withCredentials: true
                }
            );

            return response.data;

        } catch (e) {

            this.logout();

            return null;
        }
    }


    async forgotPassword(email) {
    
        const response = await axios.post(
            API_URL + "forgot-password",
            {
                email
            },
            { withCredentials: true }
        );
    
        return response.data;
    }
    
    async verifyResetCode(email, code) {
    
        const response = await axios.post(
            API_URL + "verify-reset-code",
            {
                email,
                code
            },
            { withCredentials: true }
        );
    
        return response.data;
    }
    
    async resendResetCode(email) {
    
        const response = await axios.post(
            API_URL + "resend-reset-code",
            {
                email
            },
            { withCredentials: true }
        );
    
        return response.data;
    }
    
    async resetPassword(email, code, password) {
    
        const response = await axios.post(
            API_URL + "reset-password",
            {
                email,
                code,
                password
            },
            { withCredentials: true }
        );
    
        return response.data;
    }


}








export default new AuthService();