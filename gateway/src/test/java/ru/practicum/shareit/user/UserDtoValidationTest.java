package ru.practicum.shareit.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.utils.Marker;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Создание пользователя с пустым name")
    void createUserWithEmptyName_ReturnsValidationError() {
        UserDto dto = new UserDto(1L, "", "valid@example.com");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<UserDto> violation : violations) {
            if ("name".equals(violation.getPropertyPath().toString())) {
                assertEquals("Имя пользователя должно быть заполнено", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Создание пользователя с name = null")
    void createUserWithNullName_ReturnsValidationError() {
        UserDto dto = new UserDto(1L, null, "valid@example.com");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<UserDto> violation : violations) {
            if ("name".equals(violation.getPropertyPath().toString())) {
                assertEquals("Имя пользователя должно быть заполнено", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Создание пользователя с пустым email")
    void createUserWithEmptyEmail_ReturnsValidationError() {
        UserDto dto = new UserDto(1L, "Valid Name", "");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<UserDto> violation : violations) {
            if ("email".equals(violation.getPropertyPath().toString())) {
                assertEquals("Поле email должно быть заполнено", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Создание пользователя с некорректным email")
    void createUserWithInvalidEmail_ReturnsValidationError() {
        UserDto dto = new UserDto(1L, "Valid Name", "invalid-email");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        boolean foundViolation = false;
        for (ConstraintViolation<UserDto> violation : violations) {
            if ("email".equals(violation.getPropertyPath().toString())) {
                assertEquals("Это не похоже на email", violation.getMessage());
                foundViolation = true;
            }
        }
        assertThat(foundViolation).isTrue();
    }

    @Test
    @DisplayName("Создание пользователя с корректными данными")
    void createUserWithValidData_ReturnsNoViolations() {
        UserDto dto = new UserDto(1L, "Valid Name", "valid@example.com");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, Marker.OnCreate.class);

        assertThat(violations).isEmpty();
    }
}