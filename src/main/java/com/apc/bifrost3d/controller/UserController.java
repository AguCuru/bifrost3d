package com.apc.bifrost3d.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUserById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String getPerfilUsuario(ModelMap model, Principal principal) {
        // Obtener el nombre de usuario del usuario autenticado
        String nombreUsuario = principal.getName();

        // Buscar el usuario en la base de datos utilizando el nombre de usuario
        UserEntity usuario = userService.findByUsername(nombreUsuario);

        // Agregar el usuario al modelo para que esté disponible en la vista
        model.addAttribute("usuario", usuario);

        // Devolver la vista del perfil del usuario
        return "user/perfil";
    }

}
