package com.mattcom.demostoreapp.dao;


import com.mattcom.demostoreapp.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDAO {

    private EntityManager entityManager;

    public ImageDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional
    public void save(Product product) {
        entityManager.persist(product);
    }
}
