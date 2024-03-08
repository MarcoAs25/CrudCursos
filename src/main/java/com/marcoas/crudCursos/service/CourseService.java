package com.marcoas.crudCursos.service;

import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.repository.CourseRepository;
import com.marcoas.crudCursos.service.events.CategoryUpdatedEvent;
import com.marcoas.crudCursos.service.templates.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseService implements BaseService<Course, CourseDTO> {
    private final CourseRepository repository;
    private final CategoryService categoryService;
    private final CacheManager cacheManager;
    @Transactional
    @Override
    public Course create(CourseDTO dto){
        try {
            if(StringUtils.isBlank(dto.name()) || Objects.isNull(dto.categoryId())) throw new ApiError("Campo(s) Inválido(s).");
            Course course = new Course();
            course.setName(dto.name());
            course.setCategory(categoryService.findById(dto.categoryId()));
            return repository.save(course);
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao criar curso.");
        }
    }
    @CachePut(value = "courses", key = "#id")
    @Transactional
    @Override
    public Course update(Long id, CourseDTO dto){
        try {
            if(StringUtils.isBlank(dto.name()) || Objects.isNull(dto.categoryId())) throw new ApiError("Campo(s) Inválido(s).");

            Course course = findById(id);
            course.setName(dto.name());
            course.setCategory(categoryService.findById(dto.categoryId()));
            return repository.save(course);
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        }  catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao atualizar curso.");
        }
    }
    @EventListener
    public void handleCategoryUpdatedEvent(CategoryUpdatedEvent event) {
        List<Course> courses = repository.findByCategory_Id(event.getCategoryId());
        courses.forEach(course -> cacheManager.getCache("courses").evict(course.getId()));
    }
    @Cacheable("courses")
    @Override
    public Course findById(Long id){
        try {
            return repository.findById(id).orElseThrow(() -> {throw new ApiError("curso não encontrado.", HttpStatusCode.valueOf(404));});
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar curso.");
        }
    }

    @Override
    public List<Course> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar cursos.");
        }
    }

    @Override
    public Page<Course> findAllPageable(PaginateDTO paginateSortDTO){
        try {
            return repository.findAll(paginateSortDTO.buildPageable());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar cursos.");
        }
    }
    @CacheEvict(value = "courses", key = "#id")
    @Transactional
    @Override
    public void delete(Long id){
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao excluir curso.");
        }
    }
}
