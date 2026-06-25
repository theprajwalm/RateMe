let mapInstance =null;
let currentPoi=null;
let mapInitialized = false;

function initMap(){
    const mapContainer=document.getElementById("map");
    if(!mapContainer){
        console.warn("Map not found");
        return;
    }

    //Create Map on Center Zweibrucken
    mapInstance = L.map("map").setView([49.25,7.36],13); //Center coodrinates and zoom level

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(mapInstance);

    loadPois();

}

//LOADING and DISPLAYING POIS
async function loadPois(){

        if (!mapInstance) {
        console.warn("Map not initialized");
        return;
    }
        try{
            const pois = await getPois();

            pois.forEach(poi=>{
                const lat = poi.lat;
                const lng = poi.lon;

                const marker =L.marker([lat, lng])
                    .addTo(mapInstance)
                    .bindPopup(poi.name || 'Unknown POI');

                //when the blue marker is clicked!
                marker.on("click",function(){
                    handleMarkerclick(poi.id);
                })
            });
        }catch (e) {
            console.error("Error loading POIs:", e.message);
        }

}


async function handleMarkerclick(poiId){
    currentPoi = poiId;

    try{
        //Showing the loading ui before rendering
        showPoiInfo();

        const token = localStorage.getItem("authToken");

        //getting poi details with average ratings
        const pois = await getPoiWithRating(poiId);
        console.log(pois);

        showPoiDetails(pois);

    }catch (e) {
        console.warn("Error occured");
    }
}

function showPoiInfo(){
    document.getElementById("poi-placeholder").classList.add("w3-hide");
    document.getElementById("poi-info").classList.remove("w3-hide");
    document.getElementById("poi-name").textContent="Loading.. Please wait";
    document.getElementById("poi-attributes").innerHTML="<li>Loading...</li>";
}

function showPoiDetails(poi) {
    document.getElementById("poi-placeholder").classList.add("w3-hide");
    document.getElementById("poi-info").classList.remove("w3-hide");
    document.getElementById("poi-name").textContent = poi.name || "Unbekannt";
    const ul = document.getElementById("poi-attributes");
    ul.innerHTML = '';
    if (poi.phone) ul.innerHTML += `<li>Telefon: ${poi.phone}</li>`;
    if (poi.type)  ul.innerHTML += `<li>Locationart: ${poi.type}</li>`;
    loadPoiRatings(poi.id);
}
async function loadPoiRatings(poiId) {
    const tbody = document.getElementById("ratings-tbody");
    tbody.innerHTML = '<tr><td colspan="5">Loading...</td></tr>';
    try {
        const ratings = await getPoiRatings(poiId);
        tbody.innerHTML = '';
        if (!ratings || ratings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5">Noch keine Bewertungen</td></tr>';
            return;
        }
        ratings.forEach(r => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${formatDate(r.createdAt)}</td>
                <td>${r.username}</td>
                <td>${r.txt}</td>
                <td>${getStarDisplay(r.grade)}</td>
                <td>${r.image ? '<img src="data:image/jpeg;base64,' + r.image + '" style="max-width:60px">' : '-'}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        tbody.innerHTML = '<tr><td colspan="5">Fehler beim Laden</td></tr>';
    }
}

function initTabs(){
    //getting both tab buttons
    const tab1=document.querySelector('[data-tab="tab-bewerten"]');
    const tab2=document.querySelector('[data-tab="tab-meine"]');

    //when tab1 is clicked
    tab1.addEventListener("click",function(){
        document.getElementById("tab-meine").classList.add("w3-hide");
        document.getElementById("tab-bewerten").classList.remove("w3-hide");

        //selected tab is red
        tab1.classList.add("w3-red");
        tab2.classList.remove("w3-red");
    })

    //when tab2 is selected
    tab2.addEventListener("click",function(){
        document.getElementById("tab-meine").classList.remove("w3-hide");
        document.getElementById("tab-bewerten").classList.add("w3-hide");

        //selected tab is red
        tab1.classList.remove("w3-red");
        tab2.classList.add("w3-red");

        //loading user's ratings
        loadMyRatings();
    })
}

function initMapModule(){
    if(mapInitialized){
        console.log(mapInitialized);
        return;
    }
    initMap();
    mapInitialized=true;
}