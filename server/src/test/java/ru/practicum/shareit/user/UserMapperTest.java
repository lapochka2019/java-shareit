package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    @Test
    @DisplayName("Конвертация UserDto в User со всеми заполненными полями")
    void testToEntity_withAllFieldsFilled() {
        UserDto dto = new UserDto(1L, "John Doe", "john@example.com");

        User user = UserMapper.INSTANCE.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(dto.getId());
        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    @DisplayName("Конвертация null в User")
    void testToEntity_withNullValues() {
        User user = UserMapper.INSTANCE.toEntity(null);

        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Конвертация UserDto в User с пустыми строками")
    void testToEntity_withEmptyStrings() {
        UserDto dto = new UserDto(1L, "", "");

        User user = UserMapper.INSTANCE.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("");
        assertThat(user.getEmail()).isEqualTo("");
    }

    @Test
    @DisplayName("Конвертация User в UserDto со всеми заполненными полями")
    void testToDto_withAllFieldsFilled() {
        User user = new User(1L, "Alice Smith", "alice@example.com");

        UserDto dto = UserMapper.INSTANCE.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getName()).isEqualTo(user.getName());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Конвертация null в UserDto")
    void testToDto_withNullValues() {
        UserDto dto = UserMapper.INSTANCE.toDto(null);

        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Конвертация User в UserDto с пустыми строками")
    void testToDto_withEmptyStrings() {
        User user = new User(1L, "", "");

        UserDto dto = UserMapper.INSTANCE.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("");
        assertThat(dto.getEmail()).isEqualTo("");
    }

    @Test
    @DisplayName("Конвертация User с дублированным email, но уникальность не проверяется в маппере")
    void testToDto_withDuplicateEmail() {
        User user1 = new User(1L, "User One", "same@example.com");
        User user2 = new User(2L, "User Two", "same@example.com");

        UserDto dto1 = UserMapper.INSTANCE.toDto(user1);
        UserDto dto2 = UserMapper.INSTANCE.toDto(user2);

        assertThat(dto1.getEmail()).isEqualTo(dto2.getEmail());
        assertThat(dto1.getId()).isNotEqualTo(dto2.getId());
    }
}
