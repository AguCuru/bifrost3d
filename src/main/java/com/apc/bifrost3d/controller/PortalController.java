package com.apc.bifrost3d.controller;

import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UserService userService;

//    @Autowired
//    private JavaMailSender mailSender;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";

    }

    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombreUsuario,
            @RequestParam String nombre,
            /*@RequestParam*/ String apellido,
            @RequestParam String email,
            @RequestParam String password,
            String password2, ModelMap modelo, MultipartFile archivo) {

        try {
            userService.registrar(archivo, nombreUsuario, nombre, apellido, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MyException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombreUsuario);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("archivo", archivo);
            return "registrar.html";

        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, HttpSession session) {

        if (error != null) {

            modelo.put("error", "Usuario o Contraseña invalidos!");
            session.removeAttribute("usuariosession");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        UserEntity usuario = (UserEntity) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "inicio.html";
    }

    /*    @GetMapping("/forgot_password")
    public String recuperarContraseñaForm(Model modelo) {

        return "password_forgot_form.html";

    }

    @PostMapping("/forgot_password")
    public String forgot_password(@RequestParam String email, ModelMap model, HttpServletRequest request) throws Exception {

        String token = RandomString.make(45);

        try {
            usuarioServicio.updateResetPasswordToken(token, email);
            
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            
            sendEmail(email, resetPasswordLink);
            
            model.put("exito", "Ya te enviamos a tu mail las instrucciones para recuperar tu contraseña.");
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }

         return "password_forgot_form.html";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException,   jakarta.mail.MessagingException,    UnsupportedEncodingException {

        
        MimeMessage message = mailSender.createMimeMessage();
        

        MimeMessageHelper helper = new MimeMessageHelper(message);
        

        helper.setFrom("solucionesactivasegg@gmail.com", "Soluciones Activas");
        helper.setTo(email);
        

        String subject = "Aquí está el link para restablecer tu contraseña";
        String content = "<p>Hola,</p>"
                + "<p>Solicitaste restablecer tu contraseña.</p>"
                + "<p>Haz click en el link de abajo para cambiar tu contraseña:</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Restablecer mi contraseña</a><b></p>"
                + "<p>Ignora este email si recordaste tu contraseña o no solicitaste restablecerla.</p>";
        
        

        helper.setSubject(subject);
        
        helper.setText(content, true);
        

        mailSender.send(message);
        
    }*/

 /*@GetMapping("/reset_password")
    public String resetPasswordForm(@Param(value = "token") String token, Model modelo) {

        /* Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token); */
 /*    modelo.addAttribute("token", token);
        
        return "password_reset_form.html";
        
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, ModelMap model) {
        
        String token = request.getParameter("token");
        System.out.println("Token recibido" + token);
        
        String password = request.getParameter("password");
       
        Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
        
        model.addAttribute("titulo", "Restablecer contraseña");
        
        if (usuario == null) {
            model.put("error", "Token inválido");
            
            return "password_mensaje.html";
        } else {
            try {
                usuarioServicio.updatePassword(usuario, password );
                model.put("exito", "¡Tu contraseña fue cambiada exitosamente!");
            } catch (MiException ex) {
                 model.put("error", ex.getMessage());
            }
           
            
        }
        return "password_mensaje.html";
    }  */
}
