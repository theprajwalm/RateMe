package com.rateme.rateme.DBAccess;

import com.rateme.rateme.model.Image;
import com.rateme.rateme.model.Poi;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class dbaccessimage {
    private final EntityManager entityManager;

    @Autowired
    public dbaccessimage(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public Image findById(long Id){
        return this.entityManager.find(Image.class,Id);
    }

    //getting image from the db
    public Image saveImage(Image image){
        return entityManager.merge(image); //updates if exits sonst inserts
    }
}
