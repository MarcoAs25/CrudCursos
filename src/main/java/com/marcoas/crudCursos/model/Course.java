package com.marcoas.crudCursos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Course")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CouId")
    private Long id;

    @NotBlank
    @Basic(optional = false)
    @Column(name = "CouNome", unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CouCategoriaId", referencedColumnName = "CatId")
    private Category category;
}
