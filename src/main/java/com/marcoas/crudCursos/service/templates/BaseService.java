package com.marcoas.crudCursos.service.templates;

import com.marcoas.crudCursos.dto.PaginateDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<T, DTO> {
    T create(DTO dto);
    T update(Long id, DTO dto);
    T findById(Long id);
    List<T> findAll();
    Page<T> findAllPageable(String filter, PaginateDTO paginateSortDTO);
    void delete(Long id);
}