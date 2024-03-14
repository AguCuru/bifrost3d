package com.apc.bifrost3d.repository;

import com.apc.bifrost3d.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    public UserEntity buscarPorEmail(@Param("email") String email);

    void deleteById(@SuppressWarnings("null") String userId);

}
