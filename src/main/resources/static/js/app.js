function initApp(){
    console.log("App Initializing");
    initAuth();

    const token = localStorage.getItem("authToken");

    if(token){
        initMapModule();
    }
}
document.addEventListener("DOMContentLoaded",initApp);