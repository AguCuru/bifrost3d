// package com.apc.bifrost3d.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import com.apc.bifrost3d.entity.ProductEntity;
// import com.apc.bifrost3d.exception.MyException;
// import com.apc.bifrost3d.service.ProductService;

// import java.io.IOException;
// import java.math.BigDecimal;
// import java.util.Date;

// @Controller
// @RequestMapping("/productos")
// public class ProductController {

// @Autowired
// private ProductService productService;

// @GetMapping("/crear")
// public String mostrarFormularioCreacion(Model model) {
// model.addAttribute("product", new ProductEntity());
// return "productos/crear-producto";
// }

// @PostMapping("/crear")
// public String crearProducto(MultipartFile[] archivos,
// String productName,
// String productDescription,
// Integer productStock,
// BigDecimal productPrice) throws MyException {

// // Guardar el producto y las imágenes
// productService.createProduct(archivos, productName, productDescription,
// productStock, productPrice);

// return "redirect:/productos";
// }
// }
