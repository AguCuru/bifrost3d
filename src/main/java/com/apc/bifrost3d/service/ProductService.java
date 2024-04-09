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
import com.apc.bifrost3d.repository.ImageRepository;
import com.apc.bifrost3d.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    public ProductEntity createProduct(
            String productName,
            String productDescription,
            Integer productStock,
            BigDecimal productPrice,
            List<ImageEntity> productImages) {
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

    /* Actualiza datos usuario */
    public ProductEntity updateProduct(
            String productId,
            String productName,
            String productDescription,
            Integer productStock,
            BigDecimal productPrice,
            List<ImageEntity> productImages) throws MyException {
        // Verificar si el producto existe
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new MyException("El producto con el ID " + productId + " no se encontró.", null);
        }

        // Actualizar los campos del producto con los nuevos valores
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductStock(productStock);
        product.setProductPrice(productPrice);
        product.setProductImages(productImages);
        // Establecer la fecha de modificación
        product.setFechaModificacion(new Date());

        // Actualizar la relación inversa en las imágenes
        for (ImageEntity image : productImages) {
            image.setProduct(product);
        }

        // Guardar los cambios en el producto
        return productRepository.save(product);
    }

    public List<ProductEntity> productList() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(String productId) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null); // Retorna el Usuario si está presente, o null si no lo está
    }

    public List<ImageEntity> getProductImages(String productId) {
        // Implementación para recuperar las imágenes del producto según su ID
        return imageRepository.findByProductProductId(productId); // Suponiendo que tengas un método en el repositorio
                                                                  // para
        // buscar imágenes por ID de producto
    }

    public ImageEntity getImageById(String imageId) {
        // Implementa la lógica para buscar la imagen por su ID en la base de datos
        return imageRepository.findById(imageId).orElse(null);
    }

}
