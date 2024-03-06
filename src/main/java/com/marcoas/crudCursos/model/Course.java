package com.marcoas.crudCursos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Course")
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CouId")
    private Long id;

    @Basic(optional = false)
    @Column(name = "CouNome", unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CouCategoriaId", referencedColumnName = "CatId")
    private Category category;
}
