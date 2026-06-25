package com.rateme.rateme.dto;

import com.rateme.rateme.model.Image;
import com.rateme.rateme.model.Rating;

import java.time.LocalDateTime;
import java.util.Base64;

public record RatingDTO(
        int ratingId,
        String username,
        String poi,
        int grade,
        String txt,
        String image,
        LocalDateTime createdAt
) {
    public RatingDTO(Rating rating) {
        this(rating.getId(),
                rating.getUser().getUsername(),
                rating.getPoi().getName(),
                rating.getGrade(),
                rating.getTxt(),
                encodeImage(rating.getImage()),
                rating.getCreatedAt());
    }

    private static String encodeImage(Image image) {
        if (image == null || image.getImg() == null) return null;
        return Base64.getEncoder().encodeToString(image.getImg());
    }
}
