package com.apc.bifrost3d.service;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.enums.Enumeration.Rol;
import com.apc.bifrost3d.exception.MyException;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.apc.bifrost3d.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepositorio;
    @Autowired
    private ImageService imageService;

    @Transactional
    public void registrar(MultipartFile archivo, String nombreUsuario, String nombre, String apellido,
              String email, String password, String password2)
            throws MyException {

        //validar(nombre, apellido, email, password, password2, dni, telefono);
        UserEntity usuario = new UserEntity();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEstado(true);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        // se guarda la fecha de alta (no modificable)
        Date fechatemp = new Date();
        usuario.setFechaAlta(fechatemp);

        // se guarda la fecha de nacimiento que llega por formulario
 /*       try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNac = formato.parse(fechaNacimiento);
            usuario.setFechaNacimiento(fechaNac);
        } catch (ParseException e) {

            e.printStackTrace();
        }*/

        /*if (!archivo.isEmpty()) {
            ImageEntity imagen = imageService.guardar(archivo);
            usuario.setFotoPerfil(imagen);
        }*/

        usuarioRepositorio.save(usuario);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            System.out.println("ingresa a loaduser");

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = (HttpSession) attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
