package com.marcoas.crudCursos.controller;

import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course")
public class CourseController {
    private final CourseService service;

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

    @PostMapping
    private ResponseEntity<?> create( @Valid @RequestBody CourseDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(value = "/{id}")
    private ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CourseDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
