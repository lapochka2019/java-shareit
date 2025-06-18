package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    @DisplayName("Все поля ДТО заполнены")
    @Test
    void toEntityFullReturnsMappedObjectTest() {
        UserDto userDto = new UserDto(1L,"Test", "test@test.ru");
        User mappedUser = UserMapper.INSTANCE.toEntity(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertEquals(mappedUser.getEmail(), userDto.getEmail());
    }

    @DisplayName("Все поля ДТО равны null")
    @Test
    void toEntityNullUserDtoReturnsUserWithNullFields() {
        UserDto userDto = new UserDto(null,null, null);
        User mappedUser = UserMapper.INSTANCE.toEntity(userDto);

        assertNull(mappedUser.getId());
        assertNull(mappedUser.getName());
        assertNull(mappedUser.getEmail());
    }

    @DisplayName("ДТО частично заполнено. Email = null и пустое Имя")
    @Test
    void toEntityReturnsMappedObjectWithNullFieldsTest() {
        UserDto userDto = new UserDto(1L,"", null);
        User mappedUser = UserMapper.INSTANCE.toEntity(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals("", mappedUser.getName());
        assertNull(mappedUser.getEmail());
    }

    @DisplayName("ДТО частично заполнено. Email = null")
    @Test
    void toEntityReturnsMappedObjectWithPartialDataTest() {
        UserDto userDto = new UserDto(1L,"Test", null);
        User mappedUser = UserMapper.INSTANCE.toEntity(userDto);

        assertEquals(mappedUser.getId(), userDto.getId());
        assertEquals(mappedUser.getName(), userDto.getName());
        assertNull(mappedUser.getEmail());
    }

    @Test
    @DisplayName("Все поля сущности заполнены")
    void testToDto() {
        User user = new User(2L, "Bob", "bob@example.com");
        UserDto dto = UserMapper.INSTANCE.toDto(user);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }
}
