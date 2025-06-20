package ru.practicum.shareit.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.utils.Marker;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemCreateDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Поле name должно быть заполнено")
    void testNameIsRequired() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("");
        dto.setDescription("Описание");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<ItemCreateDto> violation : violations) {
            if ("name".equals(violation.getPropertyPath().toString())) {
                assertEquals("name должен быть заполнен", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Поле description должно быть заполнено")
    void testDescriptionIsRequired() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Имя");
        dto.setDescription("");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<ItemCreateDto> violation : violations) {
            if ("description".equals(violation.getPropertyPath().toString())) {
                assertEquals("description должен быть заполнен", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Поле available не может быть null")
    void testAvailableIsRequired() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Имя");
        dto.setDescription("Описание");
        dto.setAvailable(null);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<ItemCreateDto> violation : violations) {
            if ("available".equals(violation.getPropertyPath().toString())) {
                assertEquals("available должен быть заполнен", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Поле name не должно превышать 255 символов")
    void testNameMaxSizeIsRespected() {
        String tooLongName = "A".repeat(256);

        ItemCreateDto dto = new ItemCreateDto();
        dto.setName(tooLongName);
        dto.setDescription("Описание");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto);

        violations.forEach(v -> System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage()));

        assertThat(violations).isNotEmpty();

        boolean foundViolation = false;
        for (ConstraintViolation<ItemCreateDto> violation : violations) {
            if ("name".equals(violation.getPropertyPath().toString())) {
                assertThat(violation.getMessage()).isEqualTo("Длина Имени не должна превышать 255 символов");
                foundViolation = true;
            }
        }

        assertThat(foundViolation).isTrue();
    }


    @Test
    @DisplayName("Успешная валидация")
    void testValidDtoHasNoViolations() {
        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Имя");
        dto.setDescription("Описание");
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Поле description не должно превышать 512 символов")
    void testDescriptionMaxSizeIsRespected() {
        String tooLongDescription = "B".repeat(513);

        ItemCreateDto dto = new ItemCreateDto();
        dto.setName("Имя");
        dto.setDescription(tooLongDescription);
        dto.setAvailable(true);

        Set<ConstraintViolation<ItemCreateDto>> violations = validator.validate(dto);

        violations.forEach(v -> System.out.println("Violation: " + v.getPropertyPath() + " - " + v.getMessage()));

        assertThat(violations).isNotEmpty();

        boolean foundViolation = false;
        for (ConstraintViolation<ItemCreateDto> violation : violations) {
            if ("description".equals(violation.getPropertyPath().toString())) {
                assertThat(violation.getMessage()).isEqualTo("Длина описания не должна превышать 512 символов");
                foundViolation = true;
            }
        }

        assertThat(foundViolation).isTrue();
    }
}