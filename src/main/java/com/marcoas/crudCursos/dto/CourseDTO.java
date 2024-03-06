package com.marcoas.crudCursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseDTO(@NotBlank(message = "O nome do curso deve ser válido.") String name, @NotNull(message = "O id da Categoria deve ser informado.") Long categoryId) {
}
