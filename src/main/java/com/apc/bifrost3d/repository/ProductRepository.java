package com.apc.bifrost3d.repository;

import com.apc.bifrost3d.entity.ProductEntity;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductName(String productName);

    Optional<ProductEntity> findByProductId(String productId);

    void deleteByProductId(String productId);
}
