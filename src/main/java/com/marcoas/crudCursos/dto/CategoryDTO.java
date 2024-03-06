package com.marcoas.crudCursos.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank(message = "O nome da categoria deve ser válido.") String name) {
}
