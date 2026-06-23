package com.rateme.rateme.dto;

import com.rateme.rateme.model.Poi;

public record PoiDTO(long id,
                     String name,
                     String opening_hours,
                     String addrHouseNumber,
                     String addrPostcode,
                     String addrStreet,
                     double lat,
                     double lon,
                     double averageRating
                     ) {

    //Constructor without average rating
    public PoiDTO(Poi poi){
        this(poi.getId(),
                poi.getName(),
                poi.getOpeningHours(),
                poi.getAddrHousenumber(),
                poi.getAddrPostcode(),
                poi.getAddrStreet(),
                poi.getLat(),
                poi.getLon(),
                0);
    }

    //Constructor with average rating
    public PoiDTO(Poi poi,double averageRating){
        this(poi.getId(),
                poi.getName(),
                poi.getOpeningHours(),
                poi.getAddrHousenumber(),
                poi.getAddrPostcode(),
                poi.getAddrStreet(),
                poi.getLat(),
                poi.getLon(),
                averageRating);
    }
}