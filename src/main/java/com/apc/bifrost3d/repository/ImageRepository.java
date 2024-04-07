
package com.apc.bifrost3d.repository;

import com.apc.bifrost3d.entity.ImageEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    List<ImageEntity> findByProductProductId(String productId);
}
