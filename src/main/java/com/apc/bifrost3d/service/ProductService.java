package com.apc.bifrost3d.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity createProduct(String productName, String productDescription, Integer productStock,
            BigDecimal productPrice, List<ImageEntity> productImages) {
        ProductEntity product = new ProductEntity();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductStock(productStock);
        product.setProductPrice(productPrice);
        product.setProductImages(productImages);
        product.setFechaAlta(new Date());
        product.setProductEstado(true);
        // Establecer la relación inversa en las imágenes
        for (ImageEntity image : productImages) {
            image.setProduct(product);
        }

        // Guardar el producto con las imágenes asociadas
        return productRepository.save(product);
    }

    // Otros métodos de servicio para manejar productos

    @Transactional
    public void updateProduct(
            MultipartFile archivo,
            String productName) {

    }

    public List<ProductEntity> productList() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(String productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null); // Retorna el Usuario si está presente, o null si no lo está
    }
}
