package com.rateme.rateme.dto;

public record PoiDTO(Long id,
                     String name,
                     String opening_hours,
                     String addrHouseNumber,
                     String addrPostcode,
                     String addrStreet) {
}