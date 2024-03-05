package com.marcoas.crudCursos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CatId")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CatNome", unique = true)
    private String name;
}
