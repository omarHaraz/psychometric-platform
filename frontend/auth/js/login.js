import AuthService from '../services/AuthService.js';

const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const formError = document.getElementById('formError');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');

const errorEl = document.getElementById("loginErrorMsg");

const setFieldError = (field, message) => {
    if (field) field.textContent = message;
};

const clearErrors = () => {
    if (formError) {
        formError.hidden = true;
        formError.textContent = '';
    }
    if (errorEl) {
        errorEl.classList.add("hidden");
    }
    setFieldError(emailError, '');
    setFieldError(passwordError, '');
};

const validateForm = () => {
    const email = emailInput.value.trim();
    const password = passwordInput.value;
    const isArabic = (document.documentElement.getAttribute("dir") || document.documentElement.dir || "ltr") === "rtl" || document.documentElement.getAttribute("lang") === "ar";
    let isValid = true;

    if (!email) {
        setFieldError(emailError, isArabic ? 'يرجى إدخال البريد الإلكتروني.' : 'Email is required.');
        isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        setFieldError(emailError, isArabic ? 'يرجى إدخال بريد إلكتروني صحيح (مثال: user@example.com).' : 'Please enter a valid email address (e.g. user@example.com).');
        isValid = false;
    }

    if (!password) {
        setFieldError(passwordError, isArabic ? 'يرجى إدخال كلمة المرور.' : 'Password is required.');
        isValid = false;
    } else if (password.length < 6) {
        setFieldError(passwordError, isArabic ? 'يجب أن تتكون كلمة المرور من 6 أحرف على الأقل.' : 'Password must be at least 6 characters.');
        isValid = false;
    }

    return isValid;
};

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearErrors();

    const isArabic = (document.documentElement.getAttribute("dir") || document.documentElement.dir || "ltr") === "rtl" || document.documentElement.getAttribute("lang") === "ar";

    if (!validateForm()) {
        return;
    }

    const email = emailInput.value.trim();
    const password = passwordInput.value;

    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.disabled = true;
        loginBtn.classList.add("opacity-75", "cursor-not-allowed");
    }

    try {
        const user = await AuthService.login(email, password);
        const payload = JSON.parse(atob(user.token.split('.')[1]));
        const roles = payload.roles || [];
      
        const repoPrefix = window.location.pathname.includes("/customer/")
            ? window.location.pathname.split("/customer/")[0]
            : window.location.pathname.includes("/auth/")
                ? window.location.pathname.split("/auth/")[0]
                : window.location.pathname.includes("/admin/")
                    ? window.location.pathname.split("/admin/")[0]
                    : "";

        if (roles.includes("ROLE_ADMIN") || roles.includes("ROLE_SUPER_ADMIN")) {
            window.location.href = `${repoPrefix}/admin/pages/dashboard.html`;
        } else {
            window.location.href = `${repoPrefix}/candidate/index.html`;
        }
    } catch (error) {
        if (loginBtn) {
            loginBtn.disabled = false;
            loginBtn.classList.remove("opacity-75", "cursor-not-allowed");
        }

        if (errorEl) {
            errorEl.classList.remove("hidden");
            const status = error.response?.status;
            if (status === 401 || status === 400 || error.message === "INVALID_CREDENTIALS" || (error.message && error.message.toLowerCase().includes("bad credentials"))) {
                errorEl.textContent = isArabic ? "البريد الإلكتروني أو كلمة المرور غير صحيحة." : "Invalid email or password.";
            } else {
                errorEl.textContent = isArabic ? "حدث خطأ في النظام. يرجى المحاولة لاحقاً." : "A system error occurred. Please try again later.";
            }
        } else if (formError) {
            formError.hidden = false;
            formError.textContent = error.message || 'Login failed. Please check your credentials.';
        }
    }
});

[emailInput, passwordInput].forEach((input) => {
    input.addEventListener('input', clearErrors);
});

const togglePassword = document.getElementById('togglePassword');
if (togglePassword) {
    togglePassword.addEventListener('click', () => {
        const iconSpan = togglePassword.querySelector('.material-symbols-outlined');
        const iconI = togglePassword.querySelector('i');
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            if (iconSpan) iconSpan.textContent = 'visibility_off';
            if (iconI) {
                iconI.classList.remove('fa-eye');
                iconI.classList.add('fa-eye-slash');
            }
            togglePassword.setAttribute('aria-label', 'Hide password');
        } else {
            passwordInput.type = 'password';
            if (iconSpan) iconSpan.textContent = 'visibility';
            if (iconI) {
                iconI.classList.remove('fa-eye-slash');
                iconI.classList.add('fa-eye');
            }
            togglePassword.setAttribute('aria-label', 'Show password');
        }
    });
}