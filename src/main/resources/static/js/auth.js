// ============================================================
// auth.js - Login functionality
// ============================================================

// ------------------------------------------------------------
// 1. INITIALIZATION
// ------------------------------------------------------------
function initAuth() {
    document.getElementById("btn-open-login").addEventListener("click", showLoginModal);
    document.getElementById('btn-login-cancel').addEventListener('click', hideLoginModal);
    document.getElementById('btn-login-submit').addEventListener('click', handleLogin);
    document.getElementById("btn-logout").addEventListener("click", handleLogout);
    checkAuthStatus();
}

// ------------------------------------------------------------
// 2. MODAL CONTROL
// ------------------------------------------------------------
function showLoginModal() {
    document.getElementById("modal-login").classList.add("w3-show");
    document.getElementById("login-error").classList.add("w3-hide");
    document.getElementById("login-error").textContent = '';
}

function hideLoginModal() {
    document.getElementById("modal-login").classList.remove("w3-show");
    // Clear the fields when cancel clicked
    document.getElementById("login-username").value = "";
    document.getElementById("login-password").value = "";
    document.getElementById("login-error").classList.add("w3-hide");
    document.getElementById("login-error").textContent = '';
}


//HANDLE LOGIN
async function handleLogin() {
    const username = document.getElementById("login-username").value.trim();
    const password = document.getElementById("login-password").value.trim();

    const errorMessage = document.getElementById("login-error");

    // Client-side validation
    if (!username || !password) {
        errorMessage.textContent = "Please enter both username and password";
        errorMessage.classList.remove("w3-hide");
        return;
    }

    try {
        const result = await login(username, password);

        // Store token and user data
        localStorage.setItem("authToken", result.token);
        localStorage.setItem("user", JSON.stringify(result.user));  // ✅ Fixed: changed from authUser to user

        // Hide login modal
        hideLoginModal();

        // Show logged-in interface
        showLoggedInState(result.user);

    } catch (error) {
        errorMessage.textContent = error.message || "Login failed";
        errorMessage.classList.remove("w3-hide");
    } finally {
        // Reset button state
        submitBtn.disabled = false;
        submitBtn.textContent = 'Login';
    }
}

// ------------------------------------------------------------
// 4. UI STATE MANAGEMENT
// ------------------------------------------------------------
function showLoggedInState(user) {
    document.getElementById("app-header").classList.remove("w3-hide");
    document.getElementById("app-main").classList.remove("w3-hide");
    document.getElementById("login-prompt").classList.add("w3-hide");
    document.getElementById("logged-in-username").textContent = user.username;
}

function showLoggedOutState() {
    document.getElementById("app-header").classList.add("w3-hide");
    document.getElementById("app-main").classList.add("w3-hide");
    document.getElementById("login-prompt").classList.remove("w3-hide");
    document.getElementById("logged-in-username").textContent = '';

    // Clear stored data
    localStorage.removeItem("authToken");
    localStorage.removeItem("user");
}

// ------------------------------------------------------------
// 5. HANDLE LOGOUT
// ------------------------------------------------------------
async function handleLogout() {
    const result = await logOut();

    if(!result.success){
        console.warn("logout API Issue:",result.error);
    }

    showLoggedOutState();
}

// ------------------------------------------------------------
// 6. CHECK AUTH STATUS (on page load)
// ------------------------------------------------------------
function checkAuthStatus() {
    const token = localStorage.getItem('authToken');
    const userData = localStorage.getItem('user');  // ✅ Fixed: matches the key used in login

    if (token && userData) {
        try {
            const user = JSON.parse(userData);
            showLoggedInState(user);
        } catch (e) {
            showLoggedOutState();
        }
    } else {
        showLoggedOutState();
    }
}

// ------------------------------------------------------------
// 7. INITIALIZE ON PAGE LOAD
// ------------------------------------------------------------
document.addEventListener("DOMContentLoaded", initAuth);