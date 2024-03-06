package com.marcoas.crudCursos.category.service;

import static common.CategoryConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.repository.CategoryRepository;
import com.marcoas.crudCursos.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void createCategory_WithValidData_ReturnsCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(CATEGORYENTITY);
        Category sut = categoryService.create(CATEGORYDTO);
        assertThat(sut).isEqualTo(CATEGORYENTITY);
    }

    @Test
    public void createCategory_WithInvalidData1_ThrowsException() {
        assertThatThrownBy(() -> categoryService.create(INVALIDCATEGORYDTO1))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void createCategory_WithInvalidData2_ThrowsException() {
        assertThatThrownBy(() -> categoryService.create(INVALIDCATEGORYDTO2))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void createCategory_WithInvalidData3_ThrowsException() {
        assertThatThrownBy(() -> categoryService.create(INVALIDCATEGORYDTO3))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void findCategoryById_WithValidId_ReturnsCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(CATEGORYENTITY));
        Category sut = categoryService.findById(1L);
        assertThat(sut).isEqualTo(CATEGORYENTITY);
    }

    @Test
    public void findCategoryById_WithInvalidId_ThrowsException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.findById(1L)).isInstanceOf(ApiError.class);
    }

    @Test
    public void findCategoryByPageable_WithValidPaginate_ReturnsPage() {
        Long size = 3l;
        Long page = 0l;

        PaginateDTO paginateDTO = new PaginateDTO(size, page);

        List<Category> cat = List.of(
                new Category(1l, "1"),
                new Category(2l, "2"),
                new Category(3l, "3"));
        Page<Category> result = new PageImpl<>(cat);
        when(categoryRepository.findAll(paginateDTO.buildPageable())).thenReturn(result);

        Page<Category> resultPage = categoryService.findAllPageable(paginateDTO);

        assertThat(resultPage).isNotNull();
        assertThat(Long.valueOf(resultPage.getNumber())).isEqualTo(page);
        assertThat(Long.valueOf(resultPage.getContent().size())).isLessThanOrEqualTo(size);
    }

    @Test
    public void findCategoryByPageable_WithInvalidPaginate_ThrowsException() {
        PaginateDTO paginateDTO = new PaginateDTO(10L, 0L);
        when(categoryRepository.findAll(paginateDTO.buildPageable())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> categoryService.findAllPageable(paginateDTO)).isInstanceOf(ApiError.class);
    }

    @Test
    public void findAllCategory_ReturnsList() {
        List<Category> cat = List.of(
                CATEGORYENTITY,
                CATEGORYENTITY,
                CATEGORYENTITY);
        when(categoryRepository.findAll()).thenReturn(cat);

        List<Category> result = categoryService.findAll();

        assertThat(result).isEqualTo(cat);
    }

    @Test
    public void findAllCategory_ReturnsListEmpty() {

        List<Category> cat = List.of();
        when(categoryRepository.findAll()).thenReturn(cat);

        List<Category> result = categoryService.findAll();

        assertThat(result).isEqualTo(cat);
    }

    @Test
    public void findAllCategory_ThrowsException() {
        when(categoryRepository.findAll()).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> categoryService.findAll()).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCategory_WithValidData_ReturnsUpdatedCategory() {
        Long categoryId = 1L;
        CategoryDTO dto = new CategoryDTO("New Category Name");
        Category existingCategory = new Category(categoryId, "Old Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Category updatedCategory = categoryService.update(categoryId, dto);

        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo(dto.name());
    }

    @Test
    public void updateCategory_WithInvalidData1_ThrowsException() {
        Long categoryId = 1L;
        CategoryDTO dto = new CategoryDTO(" ");
        assertThatThrownBy(() -> categoryService.update(categoryId, dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCategory_WithInvalidData2_ThrowsException() {
        CategoryDTO dto = new CategoryDTO(" ");
        assertThatThrownBy(() -> categoryService.update(null, dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCategory_ThrowsException() {
        CategoryDTO dto = new CategoryDTO("valid");
        when(categoryRepository.findById(anyLong())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> categoryService.update(anyLong(), dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void deleteCategory_ReturnsNoContent() {
        assertThatCode(() -> categoryService.delete(anyLong())).doesNotThrowAnyException();
    }

    @Test
    public void deleteCategory_ThrowsException() {
        doThrow(RuntimeException.class).when(categoryRepository).deleteById(anyLong());
        assertThatThrownBy(() -> categoryService.delete(anyLong()))
                .isInstanceOf(ApiError.class);
    }

}
