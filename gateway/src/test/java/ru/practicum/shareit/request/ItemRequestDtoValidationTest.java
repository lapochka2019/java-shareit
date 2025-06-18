package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Поле description не должно быть пустым")
    void testDescriptionIsRequired() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("   ");

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<ItemRequestDto> violation : violations) {
            if ("description".equals(violation.getPropertyPath().toString())) {
                assertEquals("description должен быть заполнен", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Поле description не должно превышать 512 символов")
    void testDescriptionMaxSizeIsRespected() {
        String tooLongDescription = "A".repeat(513);

        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(tooLongDescription);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<ItemRequestDto> violation : violations) {
            if ("description".equals(violation.getPropertyPath().toString())) {
                assertEquals("Длина описания не должна превышать 512 символов", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Валидный запрос")
    void testValidDtoHasNoViolations() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Описание запроса");

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}