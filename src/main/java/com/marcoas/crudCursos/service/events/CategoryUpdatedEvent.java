package com.marcoas.crudCursos.service.events;

public class CategoryUpdatedEvent {
    private Long categoryId;

    public CategoryUpdatedEvent(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
