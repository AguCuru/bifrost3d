package com.apc.bifrost3d.repository;

import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query("SELECT p FROM ProductEntity p WHERE p.productName = :productName")
    public UserEntity buscarPoductoXnombre(@Param("productName") String productName);

    void deleteById(@SuppressWarnings("null") String productId);

    void findByProductId(String productId);

    // public List<ProductEntity> findById();

}
