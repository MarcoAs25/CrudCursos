package com.marcoas.crudCursos.category.repository;

import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static common.CategoryConstants.CATEGORYENTITYTOSAVE;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createCategory_WithValidData_ReturnsCategory() {
        Category category = categoryRepository.save(CATEGORYENTITYTOSAVE);
        Category sut = testEntityManager.find(Category.class, category.getId());
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(CATEGORYENTITYTOSAVE.getName());
    }

    @Test
    public void createCategory_WithInValidData_ThrowsException() {
        Category emptyCategory = new Category();
        Category invalidCategory = new Category();
        invalidCategory.setName("  ");
        assertThatThrownBy(() -> categoryRepository.save(emptyCategory)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> categoryRepository.save(invalidCategory)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createCategory_WithExistingName_ThrowsException() {
        Category category = new Category(null, "ValidName");

        Category categorySaved = testEntityManager.persistFlushFind(category);
        testEntityManager.detach(categorySaved);
        categorySaved.setId(null);

        assertThatThrownBy(() -> categoryRepository.save(categorySaved)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findCategoryById_WithValidId_ReturnCategory() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        testEntityManager.detach(categorySaved);

        Optional<Category> sut = categoryRepository.findById(categorySaved.getId());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(categorySaved);
    }

    @Test
    public void findCategoryById_WithInValidId_ReturnEmpty() {
        Optional<Category> sut = categoryRepository.findById(99l);
        assertThat(sut).isEmpty();
    }

    @Test
    public void findCategoryById_WithInNullId_ThrowsException() {
        assertThatThrownBy(() -> categoryRepository.findById(null)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findAllCategory_ReturnListOfCategory() {
        List<Category> categories = List.of(
                new Category(null, "ValidName1"),
                new Category(null, "ValidName2"),
                new Category(null, "ValidName3"),
                new Category(null, "ValidName4"),
                new Category(null, "ValidName5")
        );
        categories.forEach(category -> testEntityManager.persist(category));

        List<Category> sut = categoryRepository.findAll();
        assertThat(sut).isNotNull();
        assertThat(sut.size()).isEqualTo(categories.size());
    }

    @Test
    public void findAllPageableCategory_ReturnPageListOfCategory() {
        List<Category> categories = List.of(
                new Category(null, "ValidName1"),
                new Category(null, "ValidName2"),
                new Category(null, "ValidName3"),
                new Category(null, "ValidName4"),
                new Category(null, "ValidName5")
        );
        categories.forEach(category -> testEntityManager.persist(category));

        Page<Category> sut = categoryRepository.findAll(new PaginateDTO(3l, 0l).buildPageable());
        assertThat(sut).isNotNull();
        assertThat(sut.getContent().size()).isLessThanOrEqualTo(categories.size());
    }

    @Test
    public void updateCategory_WithValidData_ReturnCategory() {
        Category category = new Category(null, "ValidName1");
        Category categoryCreated = testEntityManager.persistFlushFind(category);
        categoryCreated.setName("ValidName2");
        Category sut = categoryRepository.save(categoryCreated);
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo("ValidName2");
    }

    @Test
    public void updateCategory_WithInValidData_ThrowsException() {
        Category category = new Category(null, "ValidName1");
        Category categoryCreated = testEntityManager.persistFlushFind(category);
        categoryCreated.setName(null);
        assertThatThrownBy(() -> categoryRepository.saveAndFlush(categoryCreated));
        categoryCreated.setName("");
        assertThatThrownBy(() -> categoryRepository.saveAndFlush(categoryCreated));
        categoryCreated.setName(" ");
        assertThatThrownBy(() -> categoryRepository.saveAndFlush(categoryCreated));
    }

    @Test
    public void deleteCategory_WithValidId_ReturnNoContent() {
        Category category = new Category(null, "ValidName1");
        Category categoryCreated = testEntityManager.persistFlushFind(category);
        assertThatCode(() -> categoryRepository.deleteById(categoryCreated.getId())).doesNotThrowAnyException();
        assertThat(testEntityManager.find(Category.class, categoryCreated.getId())).isNull();
    }

    @Test
    public void deleteCategory_WithInValidId_ThrowsException() {
        assertThatThrownBy(() -> categoryRepository.deleteById(null)).isInstanceOf(RuntimeException.class);
    }
}
