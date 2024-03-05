package com.marcoas.crudCursos.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PaginateDTO(Long size, Long page) {

    public Pageable buildPageable(){
        int pageSize = size != null ? size.intValue() : 10;
        int pageNumber = page != null ? page.intValue() : 0;
        return PageRequest.of(pageNumber, pageSize);
    }

}

