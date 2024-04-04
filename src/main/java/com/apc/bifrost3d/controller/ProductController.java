package com.apc.bifrost3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.service.ProductService;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("product", new ProductEntity());
        return "productos/crear-producto";
    }

    @PostMapping("/crear")
    public String crearProducto(@ModelAttribute("product") ProductEntity product,
            @RequestParam("imagenes") MultipartFile[] imagenes) throws IOException {
        // Establecer la fecha de alta
        product.setFechaAlta(new Date());

        // Guardar el producto y las imágenes
        productService.crearProducto(product, imagenes);

        return "redirect:/productos";
    }
}
