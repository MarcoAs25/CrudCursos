package com.marcoas.crudCursos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "Category")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CatId")
    private Long id;

    @NotBlank
    @Basic(optional = false)
    @Column(name = "CatName", unique = true)
    private String name;

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}
