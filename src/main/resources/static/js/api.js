const BASE_URL = "http://localhost:8080";

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

    const token = response.headers.get("Authorization");
    const userData = await response.json();
    localStorage.setItem("authToken",token);
    localStorage.setItem("user",JSON.stringify(userData));

    return { token, user: userData };
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
            return { success: false, error: 'Logout failed on server' };
        }

    } catch (error) {
        // 6. Handle network errors
        console.error('Logout network error:', error);
        return { success: false, error: error.message };
    } finally {
    // clear local storage
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
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
        method:"GET",
        headers: {
            'Authorization': token,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`Failed to load POIs: ${response.status}`);
    }

    return await response.json();
}

// Get POI details with average rating
async function getPoiWithRating(poiId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
        throw new Error('No auth token found');
    }

    const response = await fetch(`${BASE_URL}/pois/${poiId}/with-rating`, {
        method:"GET",
        headers: {
            'Authorization': token
        }
    });

    if (!response.ok) {
        throw new Error(`Failed to load POI details: ${response.status}`);
    }

    return response.json();
}

// Get ratings for a POI
async function getPoiRatings(poiId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
        throw new Error('No auth token found');
    }

    const response = await fetch(`${BASE_URL}/ratings/poi/${poiId}`, {
        headers: {
            'Authorization': token
        }
    });

    if (!response.ok) {
        throw new Error(`Failed to load ratings: ${response.status}`);
    }

    return await response.json();
    }
}