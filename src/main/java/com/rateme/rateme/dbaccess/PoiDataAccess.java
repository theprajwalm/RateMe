package com.rateme.rateme.dbaccess;

import com.rateme.rateme.model.Poi;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class PoiDataAccess {
    //Construction Injection
    private final EntityManager entityManager;

    @Autowired
    public PoiDataAccess(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public Poi findById(long Id){
        return this.entityManager.find(Poi.class,Id);
    }

    public List<Poi> findAll(){
        return entityManager.createQuery("SELECT p FROM Poi p",Poi.class).getResultList();
    }
}