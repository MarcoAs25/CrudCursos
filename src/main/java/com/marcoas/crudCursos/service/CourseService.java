package com.marcoas.crudCursos.service;

import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.repository.CourseRepository;
import com.marcoas.crudCursos.service.templates.BaseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseService implements BaseService<Course, CourseDTO> {
    private final CourseRepository repository;
    private final CategoryService categoryService;
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao criar curso.");
        }
    }

    @Override
    public Course update(Long id, CourseDTO dto){
        try {
            if(StringUtils.isBlank(dto.name()) || Objects.isNull(dto.categoryId())) throw new ApiError("Campo(s) Inválido(s).");

            Course course = repository.findById(id).orElse(null);
            if(course != null){
                course.setName(dto.name());
                course.setCategory(categoryService.findById(dto.categoryId()));
                return repository.save(course);
            }
            throw new ApiError("Erro ao buscar curso.");
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao atualizar curso.");
        }
    }

    @Override
    public Course findById(Long id){
        try {
            return repository.findById(id).orElseThrow(() -> {throw new ApiError("curso não encontrado.");});
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
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar cursos.");
        }
    }

    @Override
    public Page<Course> findAllPageable(PaginateDTO paginateSortDTO){
        try {
            return repository.findAll(paginateSortDTO.buildPageable());
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar cursos.");
        }
    }

    @Override
    public void delete(Long id){
        try {
            Course course = repository.findById(id).orElseThrow(() -> {
                throw new ApiError("Curso não encontrado.");
            });
            repository.delete(course);
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao excluir curso.");
        }
    }
}