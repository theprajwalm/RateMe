package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccesspoi;
import com.rateme.rateme.DBAccess.dbaccessrating;
import com.rateme.rateme.Security.SecurityManager;
import com.rateme.rateme.dto.RatingDTI;
import com.rateme.rateme.model.Rating;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}")
public class RatingController {

    private final dbaccessrating accessrating;
    private final SecurityManager securityManager;

    public RatingController(dbaccessrating accessrating,SecurityManager securityManager){
        this.accessrating=accessrating;
        this.securityManager=securityManager;
    }

//    @PostMapping("/ratings")
//    public ResponseEntity<Rating> createRating(@RequestHeader("Authorization") String token, RatingDTI ratingDTI){
//        //check the token before validating.
//        securityManager.checkIfTokenIsAccepted(token);
//
//        accessrating.createRating(@PathVariable int userId,)
//    }
}
