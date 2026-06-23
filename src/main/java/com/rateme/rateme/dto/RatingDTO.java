package com.rateme.rateme.dto;

import com.rateme.rateme.model.Image;
import com.rateme.rateme.model.Rating;

import java.time.LocalDateTime;

public record RatingDTO(
        int ratingId,
        String username,
        String poi,
        int grade,
        String txt,
        Image image,
        LocalDateTime createdAt
) {
    //constructor to change the rating to rating DTO
    public RatingDTO(Rating rating){
         this(rating.getId(),
                rating.getUser().getUsername(),
                rating.getPoi().getName(),
                rating.getGrade(),
                rating.getTxt(),
                rating.getImage(),
                rating.getCreatedAt());
    }
}
