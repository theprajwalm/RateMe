package com.rateme.rateme.DBAccess;

import com.rateme.rateme.model.Poi;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class dbaccesspoi {
    //Construction Injection
    private final EntityManager entityManager;

    @Autowired
    public dbaccesspoi(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public Poi findById(int Id){
        return this.entityManager.find(Poi.class,Id);
    }

    public List<Poi> findAll(){
        return entityManager.createQuery("SELECT p FROM Pub p",Poi.class).getResultList();
    }
}