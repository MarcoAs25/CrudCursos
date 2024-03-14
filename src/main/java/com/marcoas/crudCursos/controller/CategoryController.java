package com.marcoas.crudCursos.controller;

import com.marcoas.crudCursos.controller.exception.ErrorDetails;
import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category", produces = {"application/json"})
public class CategoryController {
    private final CategoryService service;

    @Operation(summary = "Obtém uma lista de todas as categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping
    private ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtém uma página de categorias através com base no tamanho(size) e posição da página(page)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página encontrada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping("/pageable")
    private ResponseEntity<Page<Category>> findAllPageable(@RequestParam(name = "size") Long size, @RequestParam(name = "page") Long page, @RequestParam(name = "filter", required = false) String filter) {
        return ResponseEntity.ok(service.findAllPageable(filter, new PaginateDTO(size, page)));
    }

    @Operation(summary = "Obtém uma categoria através do id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping(value = "/{id}")
    private ResponseEntity<Category> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }


    @Operation(summary = "Cria uma nova categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao criar categoria", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<Category> create(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Atualizar uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao atualizar categoria", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PutMapping(value = "/{id}")
    private ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Excluir uma categoria pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída / inexistente no banco de dados"),
            @ApiResponse(responseCode = "400", description = "Requisição Inválida", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "409", description = "Conflito ao excluir categoria", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))})
    })
    @DeleteMapping(value = "/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
