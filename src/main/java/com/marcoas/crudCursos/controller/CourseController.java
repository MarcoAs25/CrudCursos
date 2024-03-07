package com.marcoas.crudCursos.controller;

import com.marcoas.crudCursos.controller.exception.ErrorDetails;
import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/course", produces = {"application/json"})
public class CourseController {
    private final CourseService service;

    @Operation(summary = "Obtém uma lista de todos os cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping
    private ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtém uma página de cursos através com base no tamanho(size) e posição da página(page)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página encontrada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping("/pageable")
    private ResponseEntity<Page<Course>> findAllPageable(@RequestParam(name = "size") Long size, @RequestParam(name = "page") Long page) {
        return ResponseEntity.ok(service.findAllPageable(new PaginateDTO(size, page)));
    }

    @Operation(summary = "Obtém um curso através do id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping(value = "/{id}")
    private ResponseEntity<Course> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Cria um novo curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao criar curso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PostMapping
    private ResponseEntity<Course> create(@Valid @RequestBody CourseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Atualizar um curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar curso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PutMapping(value = "/{id}")
    private ResponseEntity<Course> update(@PathVariable Long id, @Valid @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Excluir um curso pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso excluído / inexistente no banco de dados"),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao excluir curso", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @DeleteMapping(value = "/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
