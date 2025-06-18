package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookingDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Поле itemId должно быть заполнено")
    void testItemIdIsRequired() {
        BookingDto dto = new BookingDto();
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        for (ConstraintViolation<BookingDto> violation : violations) {
            if ("itemId".equals(violation.getPropertyPath().toString())) {
                assertEquals("Поле itemId должно быть заполнено", violation.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Поле start должно быть заполнено")
    void testStartIsRequired() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        for (ConstraintViolation<BookingDto> violation : violations) {
            if ("start".equals(violation.getPropertyPath().toString())) {
                assertEquals("Время начала бронирования должно быть заполнено", violation.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Поле end должно быть заполнено")
    void testEndIsRequired() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        for (ConstraintViolation<BookingDto> violation : violations) {
            if ("end".equals(violation.getPropertyPath().toString())) {
                assertEquals("Время окончания бронирования должно быть заполнено", violation.getMessage());
            }
        }
    }

    @Test
    @DisplayName("start из прошлого")
    void testStartMustBeInFuture() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().minusDays(1)); // start в прошлом
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        for (ConstraintViolation<BookingDto> violation : violations) {
            if ("start".equals(violation.getPropertyPath().toString())) {
                assertEquals("Время начала бронирования должно быть в настоящем или будущем", violation.getMessage());
            }
        }
    }

    @Test
    @DisplayName("end из прошлого")
    void testEndMustBeInFuture() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().minusDays(1)); // end в прошлом

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        for (ConstraintViolation<BookingDto> violation : violations) {
            if ("end".equals(violation.getPropertyPath().toString())) {
                assertEquals("Время окончания бронирования должно быть в будущем", violation.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Корректное бронирование")
    void testValidBookingPassesValidation() {
        BookingDto dto = new BookingDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}