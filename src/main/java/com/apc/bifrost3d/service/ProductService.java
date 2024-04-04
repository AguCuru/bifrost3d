package com.apc.bifrost3d.service;

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
            MultipartFile archivo,
            String productName,
            String productDescription,
            Integer productStock)
            throws MyException {

        ProductEntity product = new ProductEntity();
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductStock(productStock);
        product.setProductEstado(true);
        Date fechatemp = new Date();
        product.setFechaAlta(fechatemp);

        try {
            // Guardar el producto primero para obtener su ID
            ProductEntity savedProduct = productRepository.save(product);

            // Si el producto se guardó correctamente y hay un archivo adjunto, guardar la
            // imagen
            if (savedProduct != null && !archivo.isEmpty()) {
                ImageEntity imagen = imageService.guardar(archivo);

                // Asociar la imagen con el producto utilizando el ID del producto obtenido
                imagen.setProduct(savedProduct);

            }
        } catch (Exception e) {
            throw new MyException("Error al guardar el producto", e);
        }
    }

    public List<ProductEntity> productList() {
        return productRepository.findAll();
    }

    @SuppressWarnings("null")
    public ProductEntity getProductById(String productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null); // Retorna el Usuario si está presente, o null si no lo está
    }
}
