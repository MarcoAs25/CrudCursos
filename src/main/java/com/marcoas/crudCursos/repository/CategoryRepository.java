package com.marcoas.crudCursos.repository;

import com.marcoas.crudCursos.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where :filter is null or lower(trim(c.name)) like lower(trim(concat('%', :filter, '%')))")
    Page<Category> findByNameContains(String filter, Pageable pageable);
}
