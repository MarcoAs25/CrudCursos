package com.marcoas.crudCursos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "Course")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CouId")
    private Long id;

    @NotBlank
    @Basic(optional = false)
    @Column(name = "CouName", unique = true)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CouCategoryId", referencedColumnName = "CatId")
    private Category category;
}
