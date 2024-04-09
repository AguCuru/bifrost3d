package com.apc.bifrost3d.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.service.ProductService;
import com.apc.bifrost3d.service.UserService;

@Controller
@RequestMapping("/imagen")
public class ImageController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/perfil/{userId}")
    public ResponseEntity<byte[]> imagEntity(@PathVariable String userId) {
        UserEntity usuario = (UserEntity) userService.getOne(userId);
        byte[] imagen = usuario.getFotoPerfil().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    @GetMapping("/producto/{productId}/imagen/{imageId}")
    public ResponseEntity<byte[]> getImageForProduct(@PathVariable String productId, @PathVariable String imageId) {
        ProductEntity product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Buscar la imagen por su ID
        ImageEntity image = productService.getImageById(imageId);
        if (image == null || !product.getProductImages().contains(image)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] imageContent = image.getContenido();

        if (imageContent == null || imageContent.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        String contentType = image.getMime();
        if (contentType != null && !contentType.isEmpty()) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

}