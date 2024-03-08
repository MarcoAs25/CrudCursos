package com.marcoas.crudCursos.repository;

import com.marcoas.crudCursos.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByCategory_Id(Long id);
}
