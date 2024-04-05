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

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createProduct(
            MultipartFile[] archivos,
            String productName,
            String productDescription,
            Integer productStock,
            BigDecimal productPrice)
            throws MyException {

        ProductEntity product = new ProductEntity();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductStock(productStock);
        product.setProductPrice(productPrice);
        product.setProductEstado(true);
        Date fechatemp = new Date();
        product.setFechaAlta(fechatemp);

        try {
            // Guardar el producto primero para obtener su ID
            ProductEntity savedProduct = productRepository.save(product);

            // Guardar las imágenes asociadas al producto
            for (MultipartFile archivo : archivos) {
                if (!archivo.isEmpty()) {
                    // Guardar la imagen y asociarla con el producto
                    ImageEntity imagen = imageService.guardar(archivo);
                    imagen.setProduct(savedProduct);

                }
            }
        } catch (Exception e) {
            throw new MyException("Error al guardar el producto", e);
        }
    }

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
