package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "categories")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {


}
