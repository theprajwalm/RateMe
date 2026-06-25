// ============================================================
// auth.js - Login functionality
// ============================================================

// ------------------------------------------------------------
// 1. INITIALIZATION
// ------------------------------------------------------------
function initAuth() {
    //login
    document.getElementById("btn-open-login").addEventListener("click", showLoginModal);
    //cancel inside the login
    document.getElementById('btn-login-cancel').addEventListener('click', hideLoginModal);
    //submit the login details
    document.getElementById('btn-login-submit').addEventListener('click', handleLogin);
    //logout when user is logged in
    document.getElementById("btn-logout").addEventListener("click", handleLogout);
    //registration
    document.getElementById("link-open-register").addEventListener("click",function (event){
        event.preventDefault();
        showRegisterModal();
    })
    //when registertion is clicked
    document.getElementById("btn-reg-submit").addEventListener("click",handleRegister);
    document.getElementById("btn-reg-cancel").addEventListener("click",hideRegisterModal);
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
        // Hide login modal
        hideLoginModal();
        // Show logged-in interface
        showLoggedInState(result.user);
    } catch (error) {
        errorMessage.textContent = error.message || "Login failed";
        errorMessage.classList.remove("w3-hide");
    }
}

//Handle Register
function showRegisterModal(){
    document.getElementById("modal-register").classList.add("w3-show");
}

async function handleRegister() {
    //getting value from the browser
    const username   = document.getElementById("reg-username").value.trim();
    const password   = document.getElementById("reg-password").value.trim();
    const firstname  = document.getElementById("reg-firstname").value.trim();
    const lastname   = document.getElementById("reg-lastname").value.trim();
    const email      = document.getElementById("reg-email").value.trim();
    const street     = document.getElementById("reg-street").value.trim();
    const street_nr  = document.getElementById("reg-housenumber").value.trim();
    const zip        = document.getElementById("reg-zip").value.trim();
    const city       = document.getElementById("reg-city").value.trim();

    const error = document.getElementById("reg-error");

    if (!username || !password || !firstname || !lastname || !email) {
        error.textContent = "Please fill up the details";
        error.classList.remove("w3-hide");
        return;
    }

    //calling the api
    try{
        const result= await register(username,password,firstname,lastname,email,street,street_nr,zip,city);
        //hiding the register form
        hideRegisterModal();
        //user logged in
        showLoggedInState(result.user);
    }catch (err){
        error.textContent="Registertion error";
        error.classList.remove("w3-hide");
    }
}

//HideRegister
function hideRegisterModal(){
    document.getElementById("modal-register").classList.remove("w3-show");

    //when cancel is clicked all the filled info will be empty
    ["reg-username","reg-password","reg-firstname","reg-lastname",
        "reg-email","reg-street","reg-housenumber","reg-zip","reg-city"].forEach(id =>{
            document.getElementById(id).value="";
    })
    document.getElementById("modal-register").classList.remove("w3-show");
}

// ------------------------------------------------------------
// 4. UI STATE MANAGEMENT
// ------------------------------------------------------------
function showLoggedInState(user) {
    document.getElementById("logged-in-username").textContent = user.username;

    document.getElementById("app-header").classList.remove("w3-hide");
    document.getElementById("app-main").classList.remove("w3-hide");
    document.getElementById("login-prompt").classList.add("w3-hide");

    initMapModule();

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
    console.log(result);

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