package com.apc.bifrost3d.entity;

import com.apc.bifrost3d.enums.Enumeration.Rol;
import jakarta.persistence.Entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
//@Table(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String userId;
    protected String nombreUsuario;
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String password;
    protected boolean estado;
    @Temporal(TemporalType.DATE)
    protected Date fechaAlta;
    @Enumerated(EnumType.STRING)
    protected Rol rol;
    @OneToOne
    protected ImageEntity fotoPerfil;
}
