const BASE_URL = "http://localhost:8080";

function handleApiError(response) {
    if (response.status === 403 || response.status === 401) {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        location.reload();
    }
    throw new Error(`Request failed: ${response.status}`);
}

async function register(username,password,firstname,lastname,email,street,street_nr,zip,city){
    const response = await fetch(`${BASE_URL}/user/register`,{
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify({username,password,firstname,lastname,email,street,street_nr,zip,city})
    });

    if(!response.ok){
        throw new Error(await response.text() || "Error in Registeration");
    }

    const userData = await response.json();
    localStorage.setItem("authToken", userData.token);
    localStorage.setItem("user", JSON.stringify(userData));
    return { token: userData.token, user: userData };

}
async function login(username, password){
    const response = await fetch(`${BASE_URL}/user/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({username, password})
    });

    if(!response.ok){
        const errorData = await response.json();
        throw new Error(errorData.message || "Login Failed");
    }

    const userData = await response.json();
    localStorage.setItem("authToken", userData.token);
    localStorage.setItem("user", JSON.stringify(userData));

    return { token: userData.token, user: userData };
}

async function logOut() {

    const token = localStorage.getItem("authToken");

    if (!token) {
        return {
            success: true,
            message: "Already logged out"
        };
    }
    try {
        // 3. Send logout request to backend
        const response = await fetch(`${BASE_URL}/user/logout`, {
            method: 'POST',
            headers: {
                'Authorization': token  // Send the token
            }
        });

        // 4. Check if the request was successful
        if (!response.ok) {
            // Even if backend fails, we should still clear local state
            console.warn('Logout API returned error:', response.status);
            return {success: false, error: 'Logout failed on server'};
        }
        return{success:true};

    } catch (error) {
        // 6. Handle network errors
        console.error('Logout network error:', error);
        return {success: false, error: error.message};
    } finally {
        // clear local storage
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
    }
}
// ============================================================
// api.js - Add POI functions
// ============================================================

// Get all POIs
async function getPois() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        throw new Error('No auth token found');
    }

    const response = await fetch(`${BASE_URL}/pois`, {
        method: "GET",
        headers: {
            'Authorization': token,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) handleApiError(response);

    return await response.json();
}

// Get POI details with average rating
async function getPoiWithRating(poiId) {
    const token = localStorage.getItem('authToken');
    if (!token) throw new Error('No auth token found');

    const response = await fetch(`${BASE_URL}/pois/${poiId}/with-rating`, {
        method: "GET",
        headers: { 'Authorization': token }
    });

    if (!response.ok) handleApiError(response);

    return response.json();
}

// Get ratings for a POI
async function getPoiRatings(poiId) {
    const token = localStorage.getItem('authToken');
    if (!token) throw new Error('No auth token found');

    const response = await fetch(`${BASE_URL}/ratings/poi/${poiId}`, {
        headers: { 'Authorization': token }
    });

    if (!response.ok) handleApiError(response);

    return await response.json();
}

async function getMyRatings() {
    const token = localStorage.getItem('authToken');
    if (!token) throw new Error('No auth token found');

    const response = await fetch(`${BASE_URL}/ratings/user`, {
        headers: { 'Authorization': token }
    });

    if (!response.ok) handleApiError(response);

    return await response.json();
}

async function submitRating(poiId, txt, grade, imageFile) {
    const token = localStorage.getItem("authToken");
    if (!token) throw new Error("No auth token found");
    let imageBase64 = null;
    if (imageFile) {
        imageBase64 = await toBase64(imageFile);
    }
    const response = await fetch(`${BASE_URL}/ratings`, {
        method: "POST",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ poiId, txt, grade, image: imageBase64 })
    });
    if (!response.ok) {
        const err = await response.json();
        throw new Error(err.message || `Failed: ${response.status}`);
    }
    return await response.json();
}

function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}