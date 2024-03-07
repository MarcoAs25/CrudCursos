package com.marcoas.crudCursos.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcoas.crudCursos.controller.CategoryController;
import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.service.CategoryService;
import common.CategoryConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void createCategory_WithValidData_ReturnsCreated() throws Exception {
        when(categoryService.create(CategoryConstants.CATEGORYDTO)).thenReturn(CategoryConstants.CATEGORYENTITY);
        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(CategoryConstants.CATEGORYDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(CategoryConstants.CATEGORYENTITY));
    }

    @Test
    public void createCategory_WithInValidData_ReturnsBadRequest() throws Exception {
        CategoryDTO category = new CategoryDTO(null);
        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        category = new CategoryDTO("");
        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        category = new CategoryDTO(" ");
        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(category))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsBadRequest() throws Exception {
        when(categoryService.create(any(CategoryDTO.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(CategoryConstants.CATEGORYDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void findCategoryById_WithValidId_ReturnsCategory() throws Exception {
        when(categoryService.findById(anyLong())).thenReturn(CategoryConstants.CATEGORYENTITY);
        mockMvc.perform(get("/api/category/" + anyLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(CategoryConstants.CATEGORYENTITY));
    }

    @Test
    public void findCategoryById_WithUnexistingId_ReturnsNotFound() throws Exception {
        when(categoryService.findById(anyLong())).thenThrow(new ApiError("message", HttpStatusCode.valueOf(404)));
        mockMvc.perform(get("/api/category/" + anyLong()))
                .andExpect(status().isNotFound());
    }
    @Test
    public void findAllCategory_ReturnListOfCategory() throws Exception {
        List<Category> categories = List.of(
                new Category(null, "ValidName1"),
                new Category(null, "ValidName2"),
                new Category(null, "ValidName3"),
                new Category(null, "ValidName4"),
                new Category(null, "ValidName5")
        );
        when(categoryService.findAll()).thenReturn(categories);
        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)));

        when(categoryService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void findAllCategoryPageable_ReturnPageListOfCategory() throws Exception {
        List<Category> categories = List.of(
                new Category(1L, "ValidName1"),
                new Category(2L, "ValidName2"),
                new Category(3L, "ValidName3")
        );
        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryService.findAllPageable(any(PaginateDTO.class))).thenReturn(categoryPage);

        mockMvc.perform(get("/api/category/pageable")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(categories.size()));

        when(categoryService.findAllPageable(any(PaginateDTO.class))).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/category/pageable")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void updateCategory_WithValidData_ReturnsCategory() throws Exception {
        when(categoryService.update(1l, CategoryConstants.CATEGORYDTO)).thenReturn(CategoryConstants.CATEGORYENTITY);
        mockMvc.perform(put("/api/category/1")
                        .content(objectMapper.writeValueAsString(CategoryConstants.CATEGORYDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCategory_WithInValidData_ThrowsException() throws Exception {
        when(categoryService.update(1l, CategoryConstants.CATEGORYDTO)).thenThrow(new ApiError("Erro"));
        mockMvc.perform(put("/api/category/1")
                        .content(objectMapper.writeValueAsString(CategoryConstants.CATEGORYDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCategoryById_WithId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/category/" + anyLong()))
                .andExpect(status().isNoContent());
    }
}
