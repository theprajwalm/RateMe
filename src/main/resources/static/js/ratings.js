let selectedRating = 0;
//star to give when rating
function initStarRating() {
    const stars = document.querySelectorAll('.star');

    // Click to select
    stars.forEach(star => {
        star.addEventListener('click', function() {
            const value = parseInt(this.dataset.value);
            setRating(value);
        });

        // Hover effect
        star.addEventListener('mouseenter', function() {
            const value = parseInt(this.dataset.value);
            highlightStars(value);
        });

        star.addEventListener('mouseleave', function() {
            highlightStars(selectedRating);
        });
    });
}

function setRating(value) {
    selectedRating = value;
    document.getElementById('rating-stars').value = value;
    highlightStars(value);
}

function highlightStars(count) {
    const stars = document.querySelectorAll('.star');
    stars.forEach((star, index) => {
        if (index < count) {
            star.classList.add('active');
        } else {
            star.classList.remove('active');
        }
    });
}

async function loadMyRatings(){
    const tbody = document.getElementById("my-ratings-tbody");
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Loading...</td></tr>';

    try{
        //getting all the ratings of the user
        const ratings = await getMyRatings();

        tbody.innerHTML = '';

        //when rating is empty
        if(!ratings|| ratings.length===0){
            tbody.innerHTML='<tr><td colspan="5" style="text-align:center;color:#999;">No ratings yet</td></tr>';
            return;
        }

        ratings.sort((a,b)=>new Date(b.createdAt)-new Date(a.createdAt));

        ratings.forEach(rating =>{
            const row = document.createElement('tr');

            //Date
            const dateBox = document.createElement("td");
            dateBox.textContent=formatDate(rating.createdAt);
            row.appendChild(dateBox);

            //location
            const locationCell = document.createElement('td');
            locationCell.textContent = rating.poi || 'Unknown';
            row.appendChild(locationCell);

            // Rating (stars)
            const ratingCell = document.createElement('td');
            ratingCell.textContent = getStarDisplay(rating.grade);
            row.appendChild(ratingCell);

            // Image
            const imageCell = document.createElement('td');
            if (rating.image) {
                const img = document.createElement('img');
                img.src = 'data:image/jpeg;base64,' + rating.image;
                img.alt = 'Rating image';
                img.className = 'rating-image-preview';
                img.style.maxWidth = '80px';
                img.style.maxHeight = '80px';
                imageCell.appendChild(img);
            } else {
                imageCell.textContent = '-';
            }
            row.appendChild(imageCell);

            tbody.appendChild(row);
        });
    }catch (e) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#999;">Failed to load</td></tr>';
    }
}

function formatDate(dateStr) {
    const d = new Date(dateStr);
    return d.toLocaleDateString('de-DE') + ', ' + d.toLocaleTimeString('de-DE', {hour:'2-digit', minute:'2-digit'});
}
function getStarDisplay(grade) {
    return '★'.repeat(grade) + '☆'.repeat(5 - grade);
}

async function handleSubmitRating() {
    const comment = document.getElementById('rating-comment').value.trim();
    const grade = parseInt(document.getElementById('rating-stars').value);
    const imageFile = document.getElementById('rating-image').files[0];
    if (!currentPoi) return;
    if (grade < 1) { alert('Bitte eine Bewertung auswählen'); return; }
    try {
        await submitRating(currentPoi, comment, grade, imageFile);
        // reset form
        document.getElementById('rating-comment').value = '';
        selectedRating = 0;
        highlightStars(0);
        document.getElementById('rating-stars').value = 0;
        document.getElementById('rating-image').value = '';
        // refresh ratings table
        await loadPoiRatings(currentPoi);
    } catch (e) {
        alert('Fehler beim Absenden: ' + e.message);
    }
}

function initRatingsModule() {
    initStarRating();
    document.getElementById('btn-submit-rating').addEventListener('click', handleSubmitRating);
}