package com.marcoas.crudCursos.controller;

import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category", produces = {"application/json"})
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    private ResponseEntity<?> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/pageable")
    private ResponseEntity<?> findAllPageable(@RequestParam(name = "size") Long size, @RequestParam(name = "page") Long page){
        return ResponseEntity.ok(service.findAllPageable(new PaginateDTO(size, page)));
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> create(@Valid @RequestBody CategoryDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
