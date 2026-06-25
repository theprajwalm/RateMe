package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccesspoi;
import com.rateme.rateme.DBAccess.dbaccessrating;
import com.rateme.rateme.Security.SecurityManager;
import com.rateme.rateme.dto.PoiDTO;
import com.rateme.rateme.dto.RatingDTO;
import com.rateme.rateme.model.Poi;
import com.rateme.rateme.model.Rating;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pois")
public class PoiController {

    private final dbaccesspoi accesspoi;
    private final dbaccessrating accessrating;
    private final SecurityManager securityManager;

    public PoiController(dbaccesspoi accesspoi, dbaccessrating accessrating, SecurityManager securityManager) {
        this.accesspoi = accesspoi;
        this.accessrating = accessrating;
        this.securityManager = securityManager;
    }

    //getting all the poiss
    @GetMapping
    public ResponseEntity<List<PoiDTO>> getAllPois(@RequestHeader("Authorization") String token) {

        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        // Get all POIs from database
        List<Poi> pois = accesspoi.findAll();

        List<PoiDTO> poiDTOs=pois.stream().map(poi -> new PoiDTO(poi)).collect(Collectors.toList());

        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(poiDTOs);
    }

    //getting poi with its average rating
    @GetMapping("/{poiId}/with-rating")
    public ResponseEntity<PoiDTO> getPoiWithRating(
            @RequestHeader("Authorization") String token,
            @PathVariable long poiId) {

        // Check if token is valid
        securityManager.checkIfTokenIsAccepted(token);

        Poi poi = accesspoi.findById(poiId);
        if (poi == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POI not found");
        }

        double averageRating = accessrating.averageRatings(poiId);

        // Convert to DTO with average rating
        PoiDTO poiDTO = new PoiDTO(poi,averageRating);

        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(poiDTO);
    }
}