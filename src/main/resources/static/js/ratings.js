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

        ratings.forEach(ratings =>{
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
                img.src = rating.image;
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

function initRatingsModule() {
    initStarRating();
    document.getElementById('btn-submit-rating').addEventListener('click', handleSubmitRating);
}