package com.mattcom.demostoreapp.config;


import com.mattcom.demostoreapp.entity.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private String theAllowedOrigins = "*";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.PUT};
//        String[] theSupportedActions = {"GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"};
        config.exposeIdsFor(StoreUser.class);
        config.exposeIdsFor(ProductCategory.class);
        config.exposeIdsFor(Product.class);
        config.exposeIdsFor(Role.class);
        config.exposeIdsFor(Image.class);

//        disableHttpMethods(User.class, config, theUnsupportedActions);
//        //disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
//        disableHttpMethods(Role.class, config, theUnsupportedActions);

        System.out.println("LOGGGGG _+_+_+__+_+_+_+ " + config.getBasePath());
       // cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins).allowedMethods("*").allowedHeaders("*");
    }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/api/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions){
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metadata,httpMethods) ->
                        httpMethods.disable((theUnsupportedActions)))
                .withCollectionExposure(((metadata, httpMethods) ->
                        httpMethods.disable(theUnsupportedActions)));
    }

}
