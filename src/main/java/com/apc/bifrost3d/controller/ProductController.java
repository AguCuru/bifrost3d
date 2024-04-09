package com.apc.bifrost3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{productId}")
    public String showProductDetail(@PathVariable("productId") String productId, Model model) {
        model.addAttribute("pageTitle", "Producto");

        // Obtener el producto y sus imágenes
        ProductEntity product = productService.getProductById(productId);
        List<ImageEntity> productImages = productService.getProductImages(productId);

        model.addAttribute("product", product);
        model.addAttribute("productImages", productImages);

        return "productos/product_detail"; // Nombre de la vista Thymeleaf
    }

}
