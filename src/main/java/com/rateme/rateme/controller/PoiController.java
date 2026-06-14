package com.rateme.rateme.controller;

import com.rateme.rateme.DBAccess.dbaccesspoi;
import com.rateme.rateme.model.Poi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{PoiId}")
public class PoiController {
    private final dbaccesspoi accesspoi;

    @Autowired
    public PoiController(dbaccesspoi accesspoi){
        this.accesspoi=accesspoi;
    }

//    @GetMapping("/")
}