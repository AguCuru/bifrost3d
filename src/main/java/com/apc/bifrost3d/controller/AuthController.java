package com.apc.bifrost3d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // @Autowired
    // private JavaMailSender mailSender;

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("pageTitle", " Registro");

        return "auth/register";

    }

    @PostMapping("/registro")
    public String registro(
            MultipartFile archivo,
            @RequestParam String nombreUsuario,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,
            String password2,
            ModelMap model) {

        try {
            userService.registrar(archivo, nombreUsuario, nombre, apellido, email, password, password2);
            model.put("exito", "Usuario registrado correctamente!");

            return "auth/login";
        } catch (MyException ex) {

            model.put("error", ex.getMessage());
            model.put("nombreUsuario", nombreUsuario);
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("email", email);
            model.put("archivo", archivo);
            return "auth/register";

        }

    }

    @GetMapping("/login")
    public String login(ModelMap model, @RequestParam(required = false) String error, HttpSession session) {
        model.addAttribute("pageTitle", " Login");

        if (error != null) {

            model.put("error", "Usuario o Contraseña invalidos!");
            session.removeAttribute("usuariosession");
        }
        return "auth/login";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/home")
    public String home(ModelMap model, HttpSession session) {

        UserEntity usuario = (UserEntity) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "home.html";
    }

    /*
     * @GetMapping("/forgot_password")
     * public String recuperarContraseñaForm(Model modelo) {
     * 
     * return "password_forgot_form.html";
     * 
     * }
     * 
     * @PostMapping("/forgot_password")
     * public String forgot_password(@RequestParam String email, ModelMap model,
     * HttpServletRequest request) throws Exception {
     * 
     * String token = RandomString.make(45);
     * 
     * try {
     * usuarioServicio.updateResetPasswordToken(token, email);
     * 
     * String resetPasswordLink = Utility.getSiteURL(request) +
     * "/reset_password?token=" + token;
     * 
     * sendEmail(email, resetPasswordLink);
     * 
     * model.put("exito",
     * "Ya te enviamos a tu mail las instrucciones para recuperar tu contraseña.");
     * } catch (Exception e) {
     * model.put("error", e.getMessage());
     * }
     * 
     * return "password_forgot_form.html";
     * }
     * 
     * private void sendEmail(String email, String resetPasswordLink) throws
     * MessagingException, jakarta.mail.MessagingException,
     * UnsupportedEncodingException {
     * 
     * 
     * MimeMessage message = mailSender.createMimeMessage();
     * 
     * 
     * MimeMessageHelper helper = new MimeMessageHelper(message);
     * 
     * 
     * helper.setFrom("solucionesactivasegg@gmail.com", "Soluciones Activas");
     * helper.setTo(email);
     * 
     * 
     * String subject = "Aquí está el link para restablecer tu contraseña";
     * String content = "<p>Hola,</p>"
     * + "<p>Solicitaste restablecer tu contraseña.</p>"
     * + "<p>Haz click en el link de abajo para cambiar tu contraseña:</p>"
     * + "<p><b><a href=\"" + resetPasswordLink +
     * "\">Restablecer mi contraseña</a><b></p>"
     * +
     * "<p>Ignora este email si recordaste tu contraseña o no solicitaste restablecerla.</p>"
     * ;
     * 
     * 
     * 
     * helper.setSubject(subject);
     * 
     * helper.setText(content, true);
     * 
     * 
     * mailSender.send(message);
     * 
     * }
     */

    /*
     * @GetMapping("/reset_password")
     * public String resetPasswordForm(@Param(value = "token") String token, Model
     * modelo) {
     * 
     * /* Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
     */
    /*
     * modelo.addAttribute("token", token);
     * 
     * return "password_reset_form.html";
     * 
     * }
     * 
     * @PostMapping("/reset_password")
     * public String processResetPassword(HttpServletRequest request, ModelMap
     * model) {
     * 
     * String token = request.getParameter("token");
     * System.out.println("Token recibido" + token);
     * 
     * String password = request.getParameter("password");
     * 
     * Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
     * 
     * model.addAttribute("titulo", "Restablecer contraseña");
     * 
     * if (usuario == null) {
     * model.put("error", "Token inválido");
     * 
     * return "password_mensaje.html";
     * } else {
     * try {
     * usuarioServicio.updatePassword(usuario, password );
     * model.put("exito", "¡Tu contraseña fue cambiada exitosamente!");
     * } catch (MiException ex) {
     * model.put("error", ex.getMessage());
     * }
     * 
     * 
     * }
     * return "password_mensaje.html";
     * }
     */
}
