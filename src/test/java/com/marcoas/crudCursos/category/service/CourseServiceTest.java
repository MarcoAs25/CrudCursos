package com.marcoas.crudCursos.category.service;

import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.repository.CourseRepository;
import com.marcoas.crudCursos.service.CategoryService;
import com.marcoas.crudCursos.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static common.CategoryConstants.CATEGORYENTITY;
import static common.CourseConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoryService categoryService;

    @Test
    public void createCourse_WithValidData_ReturnsCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(COURSEENTITY);
        when(categoryService.findById(anyLong())).thenReturn(CATEGORYENTITY);
        Course sut = courseService.create(COURSEDTO);
        assertThat(sut).isEqualTo(COURSEENTITY);
    }

    @Test
    public void createCourse_WithInvalidData1_ThrowsException() {
        assertThatThrownBy(() -> courseService.create(INVALIDCOURSEDTO1))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void createCourse_WithInvalidData2_ThrowsException() {
        assertThatThrownBy(() -> courseService.create(INVALIDCOURSEDTO2))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void createCourse_WithInvalidData3_ThrowsException() {
        assertThatThrownBy(() -> courseService.create(INVALIDCOURSEDTO3))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void createCourse_ThrowsException() {
        when(categoryService.findById(anyLong())).thenThrow(ApiError.class);
        assertThatThrownBy(() -> courseService.create(COURSEDTO))
                .isInstanceOf(ApiError.class);
    }

    @Test
    public void findCourseById_WithValidId_ReturnsCourse() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(COURSEENTITY));
        Course sut = courseService.findById(1L);
        assertThat(sut).isEqualTo(COURSEENTITY);
    }

    @Test
    public void findCourseById_WithInvalidId_ThrowsException() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.findById(1L)).isInstanceOf(ApiError.class);
    }
    @Test
    public void findCourseByPageable_WithValidPaginate_ReturnsPage() {
        Long size = 3l;
        Long page = 0l;

        PaginateDTO paginateDTO = new PaginateDTO(size, page);

        List<Course> course = List.of(
                COURSEENTITY,
                COURSEENTITY,
                COURSEENTITY);
        Page<Course> result = new PageImpl<>(course);
        when(courseRepository.findAll(paginateDTO.buildPageable())).thenReturn(result);

        Page<Course> sut = courseService.findAllPageable(paginateDTO);

        assertThat(sut).isNotNull();
        assertThat(Long.valueOf(sut.getNumber())).isEqualTo(page);
        assertThat(Long.valueOf(sut.getContent().size())).isLessThanOrEqualTo(size);
    }
    @Test
    public void findCourseByPageable_WithInvalidPaginate_ThrowsException() {
        PaginateDTO paginateDTO = new PaginateDTO(10L, 0L);
        when(courseRepository.findAll(paginateDTO.buildPageable())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> courseService.findAllPageable(paginateDTO)).isInstanceOf(ApiError.class);
    }
    @Test
    public void findAllCourse_ReturnsList() {
        List<Course> courses = List.of(
                COURSEENTITY,
                COURSEENTITY,
                COURSEENTITY);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> sut = courseService.findAll();

        assertThat(sut).isEqualTo(courses);
    }

    @Test
    public void findAllCourse_ReturnsListEmpty() {

        List<Course> courses = List.of();
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> sut = courseService.findAll();

        assertThat(sut).isEqualTo(courses);
    }

    @Test
    public void findAllCourse_ThrowsException() {
        when(courseRepository.findAll()).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> courseService.findAll()).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCourse_WithValidData_ReturnsUpdatedCourse() {
        Long courseId = 1L;
        CourseDTO dto = new CourseDTO("New Course Name",2L);
        Category newCategory = new Category(2L, "new CategoryName");
        Category existingCategory = new Category(1l, "Old Category Name");
        Course existingCourse = new Course(courseId, "Angular", existingCategory);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(categoryService.findById(2L)).thenReturn(newCategory);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Course updatedCourse = courseService.update(1L, dto);

        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getCategory()).isEqualTo(newCategory);
        assertThat(updatedCourse.getName()).isEqualTo(dto.name());
    }

    @Test
    public void updateCourse_WithInvalidData1_ThrowsException() {
        CourseDTO dto = new CourseDTO(null,2L);
        assertThatThrownBy(() -> courseService.update(1l, dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCourse_WithInvalidData2_ThrowsException() {
        CourseDTO dto = new CourseDTO(" ",2L);
        assertThatThrownBy(() -> courseService.update(1l, dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCourse_WithInvalidData3_ThrowsException() {
        CourseDTO dto = new CourseDTO(" ",null);
        assertThatThrownBy(() -> courseService.update(1l, dto)).isInstanceOf(ApiError.class);
    }

    @Test
    public void updateCourse_ThrowsException() {
        CourseDTO dto = new CourseDTO("valid",2l);
        when(courseRepository.findById(anyLong())).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> courseService.update(1l, dto)).isInstanceOf(ApiError.class);
    }
    @Test
    public void deleteCourse_ThrowsException() {
        doThrow(RuntimeException.class).when(courseRepository).deleteById(anyLong());
        assertThatThrownBy(() -> courseService.delete(anyLong()))
                .isInstanceOf(ApiError.class);
    }
}
