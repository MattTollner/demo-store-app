package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query(nativeQuery = true, value = "WITH RECURSIVE CategoryHierarchy AS (" +
            "  SELECT product_category.id, product_category.category_name, product_category.parent_category_id" +
            "  FROM product_category" +
            "  WHERE product_category.id = ?1" +
            "  UNION ALL" +
            "  SELECT c.id, c.category_name, c.parent_category_id" +
            "  FROM product_category c" +
            "  JOIN CategoryHierarchy ch ON c.parent_category_id = ch.id" +
            ")" +
            "SELECT p.* " +
            "FROM product p " +
            "JOIN CategoryHierarchy ch ON p.product_category = ch.id")
    List<Product> findByProductCategoryRecursive(int id);



    List<Product> findByProductCategory_Id(int id);
}
