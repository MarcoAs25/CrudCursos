package com.marcoas.crudCursos.repository;

import com.marcoas.crudCursos.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByCategory_Id(Long id);

    @Query("select c from Course c where :filter is null or lower(trim(c.name)) like lower(trim( concat('%', :filter, '%'))) or lower(trim(c.category.name)) like lower(trim(concat('%', :filter, '%')))")
    Page<Course> findByNameContainsOrCategory_NameContains(String filter, Pageable pageable);
}
