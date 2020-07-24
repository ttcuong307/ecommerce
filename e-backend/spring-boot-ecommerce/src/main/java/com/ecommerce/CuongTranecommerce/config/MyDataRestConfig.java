package com.ecommerce.CuongTranecommerce.config;

import com.ecommerce.CuongTranecommerce.Entity.Product;
import com.ecommerce.CuongTranecommerce.Entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        // khóa HTTP methods bảng Product: PUT,POST,DELETE

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // khóa HTTP methods bảng ProductCategory: PUT,POST,DELETE

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // goi internal helper method de expose id
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // expose entity ids

        // lấy list trả về của tất cả các class từ entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // tạo array list cho entity  types
        List<Class> entityClasses = new ArrayList<>();

        // lấy entity types
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose entity ids cho array entity
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
