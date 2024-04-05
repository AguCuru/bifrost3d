package com.apc.bifrost3d.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.service.AdminService;
import com.apc.bifrost3d.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap model) {
        model.addAttribute("pageTitle", "Dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/productos")
    public String ProductList(ModelMap model) {
        model.addAttribute("pageTitle", "Productos");
        List<ProductEntity> products = productService.productList();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/usuarios")
    public String userList(ModelMap model) {
        model.addAttribute("pageTitle", "Usuarios");
        List<UserEntity> usuarios = adminService.getAllUsers();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/product/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("pageTitle", "Crear Producto");
        return "admin/create-product"; // Nombre de la vista Thymeleaf
    }

    // Este método maneja la solicitud POST para crear un producto
    @PostMapping("/product/create")
    public String createProduct(
            @RequestParam("archivos") MultipartFile[] archivos,
            @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productStock") Integer productStock,
            @RequestParam("productPrice") BigDecimal productPrice,
            Model model) {
        try {
            productService.createProduct(archivos, productName, productDescription, productStock, productPrice);
            model.addAttribute("successMessage", "¡Producto creado correctamente!");
        } catch (MyException e) {
            model.addAttribute("errorMessage", "Error al crear el producto: " + e.getMessage());
        }
        return "redirect:/admin/productos"; // Nombre de la vista Thymeleaf
    }

    @PostMapping("/eliminar/{userId}")
    public String deleteUser(@PathVariable String userId) {
        adminService.deleteUserById(userId);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/producto/eliminar/{productId}")
    public String deleteProduct(@PathVariable String productId) {
        adminService.deleteProductById(productId);
        return "redirect:/admin/productos";
    }
}
