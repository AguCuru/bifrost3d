package com.apc.bifrost3d.service;

import com.apc.bifrost3d.entity.ImageEntity;
import com.apc.bifrost3d.entity.ProductEntity;
import com.apc.bifrost3d.exception.MyException;
import com.apc.bifrost3d.repository.ImageRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    /* Guarda imagen de perfil usuario */
    public ImageEntity guardar(MultipartFile archivo) throws MyException {
        if (archivo != null) {
            try {
                ImageEntity imagen = new ImageEntity();
                imagen.setMime(archivo.getContentType());
                imagen.setImageName(archivo.getOriginalFilename());
                imagen.setContenido(archivo.getBytes());
                return imageRepository.save(imagen);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    /* Guarda lista de imagenes de un Producto */
    public List<ImageEntity> guardarImageProduct(String productId, List<MultipartFile> archivos) throws MyException {
        List<ImageEntity> imagenesGuardadas = new ArrayList<>();
        try {
            for (MultipartFile archivo : archivos) {
                ImageEntity imagen = new ImageEntity();
                imagen.setMime(archivo.getContentType());
                imagen.setImageName(archivo.getOriginalFilename());
                imagen.setContenido(archivo.getBytes());

                // Guardar la imagen en la base de datos
                imagenesGuardadas.add(imageRepository.save(imagen));
            }
        } catch (IOException e) {
            throw new MyException("Error al guardar las imágenes", e);
        }
        return imagenesGuardadas;
    }

    public ImageEntity actualizar(MultipartFile archivo, String idImagen) throws MyException {
        if (archivo != null) {
            try {
                ImageEntity imagen = new ImageEntity();
                if (idImagen != null) {
                    Optional<ImageEntity> respuesta = imageRepository.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setImageName(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imageRepository.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ImageEntity> listarTodos() {
        return imageRepository.findAll();
    }

}
