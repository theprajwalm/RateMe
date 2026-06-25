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
            console.log(pois);

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
            console.log("Error to load POIS");
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

function showPoiDetails(poi){
    console.log("show");
    document.getElementById("poi-placeholder").classList.add("w3-hide");
    document.getElementById("poi-info").classList.remove("w3-hide");

    document.getElementById("poi-name").textContent=poi.name || "unknown location";
}

function initMapModule(){
    if(mapInitialized){
        console.log(mapInitialized);
        return;
    }
    initMap();
    mapInitialized=true;
}