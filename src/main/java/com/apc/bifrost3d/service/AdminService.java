package com.apc.bifrost3d.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.enums.Enumeration.Rol;
import com.apc.bifrost3d.repository.ProductRepository;
import com.apc.bifrost3d.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findByRolNot(Rol.ADMIN);
    }

    @Transactional
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    public void deleteProductById(String productId) {
        productRepository.deleteById(productId);

    }
}
