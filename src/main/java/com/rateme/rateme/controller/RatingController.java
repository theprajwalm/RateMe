package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccesspoi;
import com.rateme.rateme.DBAccess.dbaccessrating;
import com.rateme.rateme.Security.SecurityManager;
import com.rateme.rateme.dto.RatingDTI;
import com.rateme.rateme.dto.RatingDTO;
import com.rateme.rateme.model.Poi;
import com.rateme.rateme.model.Rating;
import com.rateme.rateme.model.User;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final dbaccessrating accessrating;
    private final SecurityManager securityManager;

    public RatingController(dbaccessrating accessrating,SecurityManager securityManager){
        this.accessrating=accessrating;
        this.securityManager=securityManager;
    }

    @PostMapping
    public ResponseEntity<RatingDTO> createRating(@RequestHeader("Authorization") String token, @RequestBody RatingDTI ratingDTI){
        //check the token before validating.
        securityManager.checkIfTokenIsAccepted(token);

        //getting user from the token
        User user = securityManager.getUser(token);

        //is grade valid?
        if(ratingDTI.grade() <0 || ratingDTI.grade()>5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Grade should be inbetween 0 to 5!");
        }

        //create a Rating for a poi
        Rating rating =accessrating.createRating(user.getId(),ratingDTI.poiId(),ratingDTI.txt(), ratingDTI.grade(), ratingDTI.image());

        RatingDTO ratingDTO = new RatingDTO(rating);

        return ResponseEntity.ok().header("Authorization",token).body(ratingDTO);
    }


    //getting all the ratings for the current user
    @GetMapping("/user")
    public ResponseEntity<List<RatingDTO>> getAllRatings(@RequestHeader("Authorization") String token){

        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        // Get user from token
        User user = securityManager.getUser(token);

        // Get all ratings from user
        List<Rating> ratings = accessrating.allRatingFromUser(user.getId());

        List<RatingDTO> ratingDTOs=ratings.
                stream().map(
                        rating -> new RatingDTO(rating)).collect(Collectors.toList());

        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(ratingDTOs);
    }

    //getting all the poi ratings
    @GetMapping("poi/{poiId}")
    public ResponseEntity<List<RatingDTO>> getAllPoiRatings(@RequestHeader("Authorization") String token,@PathVariable int poiId){
        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        // Get all ratings for the POI
        List<Rating> ratings = accessrating.allRatingOfPoi(poiId);

        // Convert to DTOs using the constructor
        List<RatingDTO> ratingDTOs = ratings.stream()
                .map(rating -> new RatingDTO(rating))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(ratingDTOs);
    }

    //edit ratings
    @PutMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> editRatings(@RequestHeader("Authorization")String token,
                                                 @PathVariable int ratingId,
                                                 @RequestBody RatingDTI ratingDTI){
        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        //getting user from the token
        User existingUser = securityManager.getUser(token);

        //User who wrote the rating
        User user = accessrating.findRatingById(ratingId).getUser();

        //check if the user is trying to edit others rating
        if(existingUser.getId() != user.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access denied");
        }

        // Validate grade
        if (ratingDTI.grade() < 0 || ratingDTI.grade() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Grade must be between 0 and 5");
        }

        Rating rating =accessrating.editRatings(ratingId, ratingDTI.txt(), ratingDTI.grade(), ratingDTI.image());

        RatingDTO  ratingDTO = new RatingDTO(rating);

        return ResponseEntity.ok().header("Authorization",token).body(ratingDTO);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@RequestHeader("Authorization") String token, @PathVariable int ratingId){
            securityManager.checkIfTokenIsAccepted(token);

            User existingUser = securityManager.getUser(token);

            //User who wrote the rating
            User user = accessrating.findRatingById(ratingId).getUser();

            //check if the user is trying to edit others rating
            if(existingUser != user){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access denied");
            }

            //delete the ratings
            accessrating.deleteRatingById(ratingId);

            return ResponseEntity.ok().header("Authorization", token).body("Delete sucessfully");

    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> getRatingById(
            @RequestHeader("Authorization") String token,
            @PathVariable long ratingId) {

        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        // Get the rating
        Rating rating = accessrating.findRatingById(ratingId);
        if (rating == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found");
        }

        // Convert to DTO using the constructor
        RatingDTO ratingDTO = new RatingDTO(rating);

        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(ratingDTO);
    }

}