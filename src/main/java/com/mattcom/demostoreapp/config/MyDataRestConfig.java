package com.mattcom.demostoreapp.config;


import com.mattcom.demostoreapp.product.Product;
import com.mattcom.demostoreapp.product.category.ProductCategory;
import com.mattcom.demostoreapp.product.image.Image;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.role.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(StoreUser.class);
        config.exposeIdsFor(ProductCategory.class);
        config.exposeIdsFor(Product.class);
        config.exposeIdsFor(Role.class);
        config.exposeIdsFor(Image.class);
    }



}
