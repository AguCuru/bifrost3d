
package com.apc.bifrost3d.repository;


import com.apc.bifrost3d.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,String>{
    
}
