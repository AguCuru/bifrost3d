package com.apc.bifrost3d.service;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.UserEntity;
import com.apc.bifrost3d.enums.Enumeration.Rol;
import com.apc.bifrost3d.exception.MyException;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.apc.bifrost3d.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    /* Registra un usuario */
    @Transactional
    public void registrar(
            MultipartFile archivo,
            String nombreUsuario,
            String nombre,
            String apellido,
            String email,
            String password,
            String password2)
            throws MyException {

        validar(nombreUsuario, nombre, apellido, email, password, password2);
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
        /*
         * try {
         * SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
         * Date fechaNac = formato.parse(fechaNacimiento);
         * usuario.setFechaNacimiento(fechaNac);
         * } catch (ParseException e) {
         * 
         * e.printStackTrace();
         * }
         */

        if (!archivo.isEmpty()) {
            ImageEntity imagen = imageService.guardar(archivo);
            usuario.setFotoPerfil(imagen);
        }

        userRepository.save(usuario);

    }

    /* Actualiza datos usuario */
    @Transactional
    public void updateUser(
            MultipartFile archivo,
            String nombreUsuario,
            String nombre,
            String apellido,
            String email,
            String password,
            String password2,
            String userId)
            throws MyException {
        @SuppressWarnings("null")
        Optional<UserEntity> respuesta = userRepository.findById(userId);
        if (respuesta.isPresent()) {

            UserEntity usuario = respuesta.get();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);

            if (usuario.getFotoPerfil() != null && !archivo.isEmpty()) {
                String idImagen = usuario.getFotoPerfil().getId();
                ImageEntity imagen = imageService.actualizar(archivo, idImagen);
                usuario.setFotoPerfil(imagen);

            } else if (usuario.getFotoPerfil() == null && !archivo.isEmpty()) {

                ImageEntity imagen = imageService.guardar(archivo);
                usuario.setFotoPerfil(imagen);
            }

            userRepository.save(usuario);
        }

    }

    private void validar(
            String nombreUsuario,
            String nombre,
            String apellido,
            String email,
            String password,
            String password2) throws MyException {

        if (nombreUsuario.isEmpty()) {
            throw new MyException(" el username no puede ser nulo o estar vacío", null);
        }

        if (nombre.isEmpty()) {
            throw new MyException(" el nombre no puede ser nulo o estar vacío", null);
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new MyException(" el apellido no puede ser nulo o estar vacío", null);
        }

        if (email.isEmpty() || email == null) {
            throw new MyException(" el email no puede ser nulo o estar vacio", null);
        }
        UserEntity respuesta = userRepository.buscarPorEmail(email);
        if (respuesta != null) {
            throw new MyException(" email ya registrado.", null);
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MyException(" La contraseña no puede estar vacía, y debe tener más de 5 dígitos", null);
        }

        if (!password.equals(password2)) {
            throw new MyException(" Las contraseñas ingresadas deben ser iguales", null);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity usuario = userRepository.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

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

    @Transactional
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    @SuppressWarnings("null")
    public UserEntity getOne(String userId) {
        Optional<UserEntity> optionalUsuario = userRepository.findById(userId);
        return optionalUsuario.orElse(null); // Retorna el Usuario si está presente, o null si no lo está
    }

    public UserEntity findByUsername(String nombreUsuario) {
        return userRepository.findByUsername(nombreUsuario);
    }

    public UserEntity buscarPorEmail(String email) {
        return userRepository.buscarPorEmail(email);
    }

}
