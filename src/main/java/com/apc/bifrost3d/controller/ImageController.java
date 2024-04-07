package com.apc.bifrost3d.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/producto/{productId}/imagen")
    public ResponseEntity<byte[]> getImageForProduct(@PathVariable String productId) {
        ProductEntity product = productService.getProductById(productId);
        if (product == null || product.getProductImages().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ImageEntity> images = product.getProductImages();

        // Verifica la existencia de imágenes asociadas al producto
        if (images == null || images.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtén la primera imagen del producto
        ImageEntity image = images.get(0);
        byte[] imageContent = image.getContenido();

        // Verifica si el contenido de la imagen es nulo o vacío
        if (imageContent == null || imageContent.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Prepara los encabezados de respuesta
        HttpHeaders headers = new HttpHeaders();
        // Establece el tipo de contenido basado en el tipo de imagen
        String contentType = image.getMime();
        if (contentType != null && !contentType.isEmpty()) {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } else {
            // Si no se encuentra el tipo de contenido, establece un tipo de contenido
            // genérico
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        // Devuelve la imagen con los encabezados y el estado HTTP adecuados
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

}