let mapInstance =null;

function initMap(){
    console.log("initMap called");

    const mapContainer=document.getElementById("map");
    if(!mapContainer){
        console.warn("Map not found");
        return;
    }
    console.log("Map container found");

    //Create Map on Center Zweibrucken

    mapInstance = L.map("map").setView([49.25,7.36],13); //Center coodrinates and zoom level

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(mapInstance);

    console.log('Map created successfully!');
}

//LOADING POIS
async function loadPois(){
    console.log("Pois called");
    try{
        const token = localStorage.getItem("authToken");
        if(!token){
            console.error("No token found! access denied!");
            return;
        }
        console.log("Fetching pois");
        const response = await fetch(`${BASE_URL}/pois`,{
            headers:{
                "Authorization":token

            }
        });

        if(!response.ok){
            throw new Error("Failed to fetch the pois!",response.status);
        }
        const pois = await response.json();
        console.log("Pois length",pois.length);
        console.log(pois);

        pois.forEach(poi=>{
            const lat = poi.lat;
            const lng = poi.lon;

            const marker =L.marker([lat, lng])
                .addTo(mapInstance)
                .bindPopup(poi.name || 'Unknown POI');

            //when the blue marker is clicked!
            marker.on("click",function(){
                console.log("Marker clicked!",poi.id,poi.name);
                handleMarkerclick(poi.id);
            })
        });
    }catch (error) {
        console.error('Error loading POIs:', error);
    }
}
async function handleMarkerclick(id){
    const token = localStorage.getItem("authToken");
    if(!token){
        console.log("Error getting the token");
        return;
    }

}

function initMapModule(){
    console.log("Init Map Module called");
    initMap();
    loadPois();
}