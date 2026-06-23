package com.rateme.rateme.dto;

import com.rateme.rateme.model.Image;

public record RatingDTI(int poiId,String txt,int grade, Image image) {
}
