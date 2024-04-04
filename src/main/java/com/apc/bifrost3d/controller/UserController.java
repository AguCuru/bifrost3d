package com.apc.bifrost3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.repository.UserRepository;
import com.apc.bifrost3d.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; // Inyecta UserRepository

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUserById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String getPerfilUsuario(ModelMap model, HttpSession session) {
        model.addAttribute("pageTitle", "Perfil");
        UserEntity usuario = (UserEntity) session.getAttribute("usuariosession");
        model.addAttribute("usuario", userService.getOne(usuario.getUserId()));
        model.put("usuario", usuario);
        return "user/perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/actualizar/{userId}")
    public String updateUser(
            MultipartFile archivo,
            @RequestParam String nombreUsuario,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,
            String password2,
            String userId,
            ModelMap model,
            HttpSession session) throws MyException {

        try {
            userService.updateUser(archivo, nombreUsuario, nombre, apellido, email, password, password2, userId);
            // Obtener el nombre de usuario actual
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            String userEmail = userDetails.getUsername();

            // Obtener los datos actualizados del usuario
            UserEntity usuario = userRepository.buscarPorEmail(userEmail);

            // Actualizar los datos del usuario en la sesión
            session.setAttribute("usuariosession", usuario);

            model.put("exito", "Usuario actualizado correctamente!");

            return "index.html";
        } catch (MyException ex) {

            model.put("error", ex.getMessage());
            model.put("nombreUsuario", nombreUsuario);
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("email", email);
            model.put("archivo", archivo);
            return "user/perfil";

        }

    }

}
