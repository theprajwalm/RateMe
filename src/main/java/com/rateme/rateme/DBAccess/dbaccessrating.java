package com.rateme.rateme.DBAccess;

import com.rateme.rateme.model.Image;
import com.rateme.rateme.model.Poi;
import com.rateme.rateme.model.Rating;
import com.rateme.rateme.model.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class dbaccessrating {
    private final EntityManager entityManager;
    private final dbaccessuser accessuser;
    private final dbaccesspoi accesspoi;

    @Autowired
    public dbaccessrating(EntityManager entityManager,dbaccessuser accessuser,dbaccesspoi accesspoi){
        this.entityManager=entityManager;
        this.accessuser=accessuser;
        this.accesspoi=accesspoi;
    }

    //find rating by Id
    public Rating findRatingById(long id){
        return entityManager.find(Rating.class,id);
    }

    //create Rating
    public Rating createRating(int userId, int poiId, String txt, int grade, Image image){
        //check if user exits or not
        User user = accessuser.findUserById(userId);
        if(user==null){
            throw new IllegalArgumentException("User doesnt exits");
        }

        //checking if poi exits or not
        Poi poi = accesspoi.findById(poiId);
        if(poi==null){
            throw new IllegalArgumentException("Poi not found");
        }

        Rating rating =new Rating();
        rating.setTxt(txt);
        rating.setGrade(grade);
        rating.setPoi(poi);
        rating.setUser(user);

        //if image is uploaded
        if (image != null) {
            entityManager.persist(image);
            rating.setImage(image);
        }


        //saving rating in database
        entityManager.persist(rating);
        return rating;
    }

    //delete Rating
    public void deleteRating(Rating rating){
        entityManager.remove(rating);
    }

    //delete Rating by ID
    public Rating deleteRatingById(int Id){
        //check if rating exists or not
        Rating rating = this.findRatingById(Id);
        if(rating==null){
            throw new IllegalArgumentException("No Rating found");
        }
        this.deleteRating(rating);
        return rating;
    }

    //All the rating from the user
    public List<Rating> allRatingFromUser(int userId){
        User user =accessuser.findUserById(userId);
        if(user==null){
            throw new IllegalArgumentException("No User found");
        }
        return user.getRatings();
    }

    //All thea rating of the poi
    public List<Rating> allRatingOfPoi(int poiId){
        Poi poi = accesspoi.findById(poiId);
        if(poi==null){
            throw new IllegalArgumentException("No poi found");
        }
        return poi.getRatings();
    }

    //Getting average rating of the poi
    public double averageRatings(int poiId){
        Poi poi = accesspoi.findById(poiId);
        if(poi==null){
            throw new IllegalArgumentException("No poi found");
        }

        Double avg =entityManager.createQuery(
                "SELECT AVG(r.grade)FROM Rating r WHERE r.poi.id =:poiId"
                ,Double.class).setParameter("poiId",poiId).getSingleResult();

        return avg !=null?avg:0.0; //return avg if not null else equal then 0.0
    }


    //Editing rating
    public Rating editRatings(int ratingId,String txt, int grade, Image image){
        //check if rating exists or not
        Rating rating = this.findRatingById(ratingId);
        if(rating==null){
            throw new IllegalArgumentException("No Rating found");
        }
        rating.setTxt(txt);
        rating.setGrade(grade);
        rating.setImage(image);
        return entityManager.merge(rating);
    }

    //time when rating was created
    public LocalDateTime ratingTime(Rating rating){
        return rating.getCreatedAt();
    }
}