package com.marcoas.crudCursos.service;

import com.marcoas.crudCursos.controller.exception.ApiError;
import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.repository.CategoryRepository;
import com.marcoas.crudCursos.service.events.CategoryUpdatedEvent;
import com.marcoas.crudCursos.service.templates.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements BaseService<Category, CategoryDTO> {
    private final CategoryRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    @Transactional
    @Override
    public Category create(CategoryDTO dto){
        try {
            if(StringUtils.isBlank(dto.name())) throw new ApiError("Campo(s) Inválido(s).");
            Category categoria = new Category();
            categoria.setName(dto.name());
            return repository.save(categoria);
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao criar Categoria");
        }
    }
    @CachePut(value = "categoryes", key = "#id")
    @Transactional
    @Override
    public Category update(Long id, CategoryDTO dto){
        try {
            if(StringUtils.isBlank(dto.name())) throw new ApiError("Campo(s) Inválido(s).");

            Category categoria = findById(id);
            categoria.setName(dto.name());
            categoria = repository.save(categoria);
            eventPublisher.publishEvent(new CategoryUpdatedEvent(id));
            return categoria;
        }  catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao atualizar Categoria");
        }
    }
    @Cacheable("categoryes")
    @Override
    public Category findById(Long id){
        try {
            return repository.findById(id).orElseThrow(() -> {throw new ApiError("Categoria não encontrada.", HttpStatusCode.valueOf(404));});
        } catch (ApiError e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar categoria.");
        }
    }

    @Override
    public List<Category> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar categorias.");
        }
    }

    @Override
    public Page<Category> findAllPageable(PaginateDTO paginateSortDTO){
        try {
            return repository.findAll(paginateSortDTO.buildPageable());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiError("Erro ao buscar categorias.");
        }
    }
    @CacheEvict(value = "categoryes", key = "#id")
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
            throw new ApiError("Erro ao excluir categoria.");
        }
    }
}
