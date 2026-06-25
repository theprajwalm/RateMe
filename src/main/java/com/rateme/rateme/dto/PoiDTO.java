package com.rateme.rateme.dto;

import com.rateme.rateme.model.Image;
import com.rateme.rateme.model.Poi;
import com.rateme.rateme.model.Rating;

import java.time.LocalDateTime;
import java.util.List;

// PoiDTO - ONLY POI data
public record PoiDTO(
        long id,
        String name,
        String type,              // Art der Location
        String phone,             // Telefonnummer
        String opening_hours,
        String addrHouseNumber,
        String addrPostcode,
        String addrStreet,
        double lat,
        double lon,
        double averageRating      // Only the AVERAGE, not all ratings!
) {
    public PoiDTO(Poi poi) {
        this(poi.getId(),
                poi.getName(),
                poi.getType(),
                poi.getPhone(),
                poi.getOpeningHours(),
                poi.getAddrHousenumber(),
                poi.getAddrPostcode(),
                poi.getAddrStreet(),
                poi.getLat(),
                poi.getLon(),
                0.0
        );
    }

    public PoiDTO(Poi poi, double averageRating) {
        this(poi.getId(),
                poi.getName(),
                poi.getType(),
                poi.getPhone(),
                poi.getOpeningHours(),
                poi.getAddrHousenumber(),
                poi.getAddrPostcode(),
                poi.getAddrStreet(),
                poi.getLat(),
                poi.getLon(),
                averageRating
        );
    }
}
