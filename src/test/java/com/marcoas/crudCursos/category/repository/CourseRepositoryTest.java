package com.marcoas.crudCursos.category.repository;

import com.marcoas.crudCursos.dto.PaginateDTO;
import com.marcoas.crudCursos.model.Category;
import com.marcoas.crudCursos.model.Course;
import com.marcoas.crudCursos.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    public void createCourse_WithValidData_ReturnCourse() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = testEntityManager.persistFlushFind(category);
        Course course = new Course(null, "Test Course", savedCategory);
        Course sut = testEntityManager.persistFlushFind(course);
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(course.getName());
        assertThat(sut.getCategory()).isEqualTo(course.getCategory());
    }

    @Test
    public void createCourse_WithInValidName_ThrowsException() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = testEntityManager.persistFlushFind(category);

        Course course = new Course(null, " ", savedCategory);
        assertThatThrownBy(() -> courseRepository.save(course));
    }

    @Test
    public void createCourse_WithEmptyName_ThrowsException() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = testEntityManager.persistFlushFind(category);

        Course course = new Course(null, "", savedCategory);
        assertThatThrownBy(() -> courseRepository.save(course));
    }

    @Test
    public void createCourse_WithNullName_ThrowsException() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = testEntityManager.persistFlushFind(category);

        Course course = new Course(null, null, savedCategory);
        assertThatThrownBy(() -> courseRepository.save(course));
    }

    @Test
    public void createCourse_WithInValidCategoryData_ThrowsException() {
        Category category = new Category();
        category.setName("Test Category");

        Course course = new Course(null, "Valid", null);
        assertThatThrownBy(() -> courseRepository.save(course));
    }

    @Test
    public void createCourse_WithExistingName_ThrowsException() {
        Category category = new Category();
        category.setName("Test Category");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        Course course = new Course(null, "Valid", categorySaved);
        Course courseSaved = testEntityManager.persistFlushFind(course);

        testEntityManager.detach(courseSaved);
        courseSaved.setId(null);

        assertThatThrownBy(() -> courseRepository.save(courseSaved)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findCourseById_WithValidId_ReturnCourse() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);

        Course course = new Course(null, "ValidName", categorySaved);
        Course courseSaved = testEntityManager.persistFlushFind(course);

        Optional<Course> sut = courseRepository.findById(courseSaved.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(courseSaved);
    }

    @Test
    public void findCourseById_WithInValidId_ReturnEmpty() {
        Optional<Course> sut = courseRepository.findById(99l);
        assertThat(sut).isEmpty();
    }

    @Test
    public void findCourseById_WithInNullId_ThrowsException() {
        assertThatThrownBy(() -> courseRepository.findById(null)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findAllCourse_ReturnListOfCourse() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        List<Course> courses = List.of(
                new Course(null, "ValidName1", categorySaved),
                new Course(null, "ValidName2", categorySaved),
                new Course(null, "ValidName3", categorySaved),
                new Course(null, "ValidName4", categorySaved),
                new Course(null, "ValidName5", categorySaved)
        );
        courses.forEach(course -> testEntityManager.persist(course));

        List<Course> sut = courseRepository.findAll();
        assertThat(sut).isNotNull();
        assertThat(sut.size()).isEqualTo(courses.size());
    }

    @Test
    public void findAllPageableCourse_ReturnPageListOfCourse() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        List<Course> courses = List.of(
                new Course(null, "ValidName1", categorySaved),
                new Course(null, "ValidName2", categorySaved),
                new Course(null, "ValidName3", categorySaved),
                new Course(null, "ValidName4", categorySaved),
                new Course(null, "ValidName5", categorySaved)
        );
        courses.forEach(course -> testEntityManager.persist(course));

        Page<Course> sut = courseRepository.findAll(new PaginateDTO(3l, 0l).buildPageable());
        assertThat(sut).isNotNull();
        assertThat(sut.getContent().size()).isLessThanOrEqualTo(courses.size());
    }

    @Test
    public void updateCourse_WithValidData_ReturnCourse() {
        Category category = new Category(null, "ValidName");
        Category newCategory = new Category(null, "ValidName2");
        Category newcategorySaved = testEntityManager.persistFlushFind(newCategory);
        Category categorySaved = testEntityManager.persistFlushFind(category);
        Course course = new Course(null, "ValidName", categorySaved);
        Course courseSaved = testEntityManager.persistFlushFind(course);
        courseSaved.setName("New Name");
        courseSaved.setCategory(newcategorySaved);
        Course sut = courseRepository.save(courseSaved);
        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo("New Name");
        assertThat(sut.getCategory()).isEqualTo(newcategorySaved);
    }

    @Test
    public void updateCourse_WithInValidData_ThrowsException() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        Course course = new Course(null, "ValidName", categorySaved);
        Course courseSaved = testEntityManager.persistFlushFind(course);
        testEntityManager.flush();

        courseSaved.setName(null);
        assertThatThrownBy(() -> courseRepository.saveAndFlush(courseSaved));

        courseSaved.setName("");
        assertThatThrownBy(() -> courseRepository.saveAndFlush(courseSaved));

        courseSaved.setName(" ");
        assertThatThrownBy(() -> courseRepository.saveAndFlush(courseSaved));

        courseSaved.setName("validName");
        courseSaved.setCategory(null);
        assertThatThrownBy(() -> courseRepository.saveAndFlush(courseSaved));
    }

    @Test
    public void deleteCourse_WithValidId_ReturnNoContent() {
        Category category = new Category(null, "ValidName");
        Category categorySaved = testEntityManager.persistFlushFind(category);
        Course course = new Course(null, "ValidName", categorySaved);
        Course courseSaved = testEntityManager.persistFlushFind(course);

        assertThatCode(() -> courseRepository.deleteById(courseSaved.getId())).doesNotThrowAnyException();
        assertThatCode(() -> courseRepository.deleteById(99l)).doesNotThrowAnyException();
    }
    @Test
    public void deleteCourse_WithInValidId_ThrowsException() {
        assertThatThrownBy(() -> courseRepository.deleteById(null)).isInstanceOf(RuntimeException.class);
    }
}
