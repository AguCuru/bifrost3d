package com.apc.bifrost3d.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.repository.ProductRepository;
import com.apc.bifrost3d.service.AdminService;
import com.apc.bifrost3d.service.ImageService;
import com.apc.bifrost3d.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;

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
    // @PostMapping("/product/create")
    // public String createProduct(String productId,
    // @RequestParam("archivos") MultipartFile[] archivos,
    // @RequestParam("productName") String productName,
    // @RequestParam("productDescription") String productDescription,
    // @RequestParam("productStock") Integer productStock,
    // @RequestParam("productPrice") BigDecimal productPrice,
    // Model model) {
    // try {
    // List<ImageEntity> imagenesGuardadas =
    // imageService.guardarImageProduct(productId, Arrays.asList(archivos));

    // // Crear el producto y asociar las imágenes
    // productService.createProduct(productName, productDescription, productStock,
    // productPrice,
    // imagenesGuardadas);

    // model.addAttribute("successMessage", "¡Producto creado correctamente!");
    // } catch (MyException e) {
    // model.addAttribute("errorMessage", "Error al crear el producto: " +
    // e.getMessage());
    // }
    // return "redirect:/admin/productos"; // Nombre de la vista Thymeleaf
    // }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productStock") Integer productStock,
            @RequestParam("productPrice") BigDecimal productPrice,
            @RequestParam("archivos") MultipartFile[] archivos,
            Model model) {

        try {
            List<ImageEntity> imagenesGuardadas = imageService.guardarImagenes(Arrays.asList(archivos));
            // Crear el producto y asociar las imágenes
            productService.createProduct(productName, productDescription, productStock, productPrice,
                    imagenesGuardadas);

            model.addAttribute("successMessage", "¡Producto creado correctamente!");
        } catch (MyException e) {
            model.addAttribute("errorMessage", "Error al crear el producto: " + e.getMessage());
        }
        return "redirect:/admin/productos"; // Nombre de la vista Thymeleaf
    }

    @GetMapping("/product/{productId}")
    public String updateProduct(ModelMap model, @PathVariable String productId) {
        try {
            ProductEntity product = productService.getProductById(productId);

            model.put("product", product);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error, producto no encontrado: " + e.getMessage());

        }

        return "admin/update-product";
    }

    @PostMapping("/product/update/{productId}")
    public String updateProduct(@PathVariable String productId,
            @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productStock") Integer productStock,
            @RequestParam("productPrice") BigDecimal productPrice,
            @RequestParam("archivos") MultipartFile[] archivos,
            Model model) throws IOException {
        try {

            // Convertir los MultipartFiles a ImageEntity
            List<ImageEntity> productImages = imageService.convertToImageEntities(archivos);

            // Procesar la solicitud de actualización del producto
            productService.updateProduct(productId, productName, productDescription, productStock, productPrice,
                    productImages);

            model.addAttribute("successMessage", "¡Producto actualizado correctamente!");
        } catch (MyException e) {
            model.addAttribute("errorMessage", "Error al actualizar el producto: " + e.getMessage());
        }
        return "redirect:/admin/productos"; // Redirigir a la página de administración de productos
    }

    @PostMapping("/eliminar/{userId}")
    public String deleteUser(@PathVariable String userId) {
        adminService.deleteUserById(userId);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/product/eliminar/{productId}")
    public String deleteProduct(@PathVariable String productId) {
        adminService.deleteProductById(productId);
        return "redirect:/admin/productos";
    }
}
