package com.apc.bifrost3d.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor        
public class ImageEntity {
        @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String mime;
    private String imageName;

    
    
    //lob = que puede ser pesado  // basic lazy puede demorar mucho tiempo y solo lo cargara cuando lo solicit explictamente
    @Lob 
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition="LONGBLOB")
    private byte[] contenido;
    
   
    
}
