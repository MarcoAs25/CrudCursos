package com.marcoas.crudCursos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.service.CourseService;
import common.CategoryConstants;
import common.CourseConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(CourseController.class)
@ActiveProfiles("test")
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @Test
    public void createCategory_WithValidData_ReturnsCreated() throws Exception {
        when(courseService.create(CourseConstants.COURSEDTO)).thenReturn(CourseConstants.COURSEENTITY);

        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(CourseConstants.COURSEDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(CourseConstants.COURSEENTITY.getId()))
                .andExpect(jsonPath("$.name").value(CourseConstants.COURSEENTITY.getName()))
                .andExpect(jsonPath("$.category.id").value(CourseConstants.COURSEENTITY.getCategory().getId()))
                .andExpect(jsonPath("$.category.name").value(CourseConstants.COURSEENTITY.getCategory().getName()));
    }


    @Test
    public void createCategory_WithInValidData_ReturnsBadRequest() throws Exception {
        CourseDTO course = new CourseDTO(null, CategoryConstants.CATEGORYENTITY.getId());
        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        course = new CourseDTO("", CategoryConstants.CATEGORYENTITY.getId());
        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        course = new CourseDTO(" ", CategoryConstants.CATEGORYENTITY.getId());
        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        course = new CourseDTO("valid", null);
        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(course))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsBadRequest() throws Exception {
        when(courseService.create(any(CourseDTO.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/api/course")
                        .content(objectMapper.writeValueAsString(CourseConstants.COURSEDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void findCategoryById_WithValidId_ReturnsCategory() throws Exception {
        when(courseService.findById(anyLong())).thenReturn(CourseConstants.COURSEENTITY);
        mockMvc.perform(get("/api/course/" + anyLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CourseConstants.COURSEENTITY.getId()))
                .andExpect(jsonPath("$.name").value(CourseConstants.COURSEENTITY.getName()))
                .andExpect(jsonPath("$.category.id").value(CourseConstants.COURSEENTITY.getCategory().getId()))
                .andExpect(jsonPath("$.category.name").value(CourseConstants.COURSEENTITY.getCategory().getName()));
    }

    @Test
    public void findCategoryById_WithUnexistingId_ReturnsNotFound() throws Exception {
        when(courseService.findById(anyLong())).thenThrow(new ApiError("message", HttpStatusCode.valueOf(404)));
        mockMvc.perform(get("/api/course/" + anyLong()))
                .andExpect(status().isNotFound());
    }
    @Test
    public void findAllCategory_ReturnListOfCategory() throws Exception {
        List<Course> categories = List.of(
                new Course(null, "ValidName1", CategoryConstants.CATEGORYENTITY),
                new Course(null, "ValidName2", CategoryConstants.CATEGORYENTITY),
                new Course(null, "ValidName3", CategoryConstants.CATEGORYENTITY),
                new Course(null, "ValidName4", CategoryConstants.CATEGORYENTITY),
                new Course(null, "ValidName5", CategoryConstants.CATEGORYENTITY)
        );
        when(courseService.findAll()).thenReturn(categories);
        mockMvc.perform(get("/api/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)));

        when(courseService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    public void findAllCoursePageable_ReturnPageListOfCourse() throws Exception {
        List<Course> courses = List.of(
                new Course(1L, "ValidName1", CategoryConstants.CATEGORYENTITY),
                new Course(2L, "ValidName2", CategoryConstants.CATEGORYENTITY),
                new Course(3L, "ValidName3", CategoryConstants.CATEGORYENTITY)
        );
        Page<Course> coursePages = new PageImpl<>(courses);

        when(courseService.findAllPageable(any(PaginateDTO.class))).thenReturn(coursePages);

        mockMvc.perform(get("/api/course/pageable")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(courses.size()));

        when(courseService.findAllPageable(any(PaginateDTO.class))).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/course/pageable")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void updateCategory_WithValidData_ReturnsCategory() throws Exception {
        when(courseService.update(1l, CourseConstants.COURSEDTO)).thenReturn(CourseConstants.COURSEENTITY);
        mockMvc.perform(put("/api/course/1")
                        .content(objectMapper.writeValueAsString(CourseConstants.COURSEDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCategory_WithInValidData_ThrowsException() throws Exception {
        when(courseService.update(1l, CourseConstants.COURSEDTO)).thenThrow(new ApiError("Erro"));
        mockMvc.perform(put("/api/course/1")
                        .content(objectMapper.writeValueAsString(CourseConstants.COURSEDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCategoryById_WithId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/course/" + anyLong()))
                .andExpect(status().isNoContent());
    }
}
