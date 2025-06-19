package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    @Test
    @DisplayName("Конвертация CommentDto в Comment со всеми заполненными полями")
    void testToComment_withAllFieldsFilled() {
        LocalDateTime now = LocalDateTime.now();
        Item item = new Item(1L, "Item", "desc", true, null, null);
        User author = new User(10L, "User", "user@example.com");
        CommentDto dto = new CommentDto("Great item!");

        Comment comment = CommentMapper.INSTANCE.toComment(100L, dto, item, author, now);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(100L);
        assertThat(comment.getText()).isEqualTo(dto.getText());
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getCreated()).isEqualTo(now);
    }

    @Test
    @DisplayName("Конвертация в Comment с null значениями")
    void testToComment_withNullValues() {
        Comment comment = CommentMapper.INSTANCE.toComment(null, null, null, null, null);

        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("Конвертация в CommentDto с null значениями")
    void testToCommentDto_withNullValues() {
        CommentDto commentDto = CommentMapper.INSTANCE.toCommentDto(null);

        assertThat(commentDto).isNull();
    }

    @Test
    @DisplayName("Конвертация в CommentResponseDto с null значениями")
    void testToCommentResponseDto_withNullValues() {
        CommentResponseDto commentDto = CommentMapper.INSTANCE.toCommentResponseDto(null);

        assertThat(commentDto).isNull();
    }

    @Test
    @DisplayName("Конвертация Comment в CommentDto")
    void testToCommentDto_withValidComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Good stuff!");
        comment.setCreated(LocalDateTime.now());

        CommentDto dto = CommentMapper.INSTANCE.toCommentDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getText()).isEqualTo(comment.getText());
    }

    @Test
    @DisplayName("Конвертация Comment в CommentResponseDto с данными")
    void testToCommentResponseDto_withValidData() {
        LocalDateTime now = LocalDateTime.now();
        Item item = new Item(1L, "Item", "desc", true, null, null);
        User author = new User(10L, "AuthorName", "email@example.com");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Nice item!");
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);

        CommentResponseDto dto = CommentMapper.INSTANCE.toCommentResponseDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Nice item!");
        assertThat(dto.getAuthorName()).isEqualTo("AuthorName");
        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getCreated()).isEqualTo(now);
    }

    @Test
    @DisplayName("Конвертация Comment в CommentResponseDto с null ссылками")
    void testToCommentResponseDto_withNullReferences() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Some text");
        comment.setCreated(LocalDateTime.now());
        comment.setItem(null);
        comment.setAuthor(null);

        CommentResponseDto dto = CommentMapper.INSTANCE.toCommentResponseDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Some text");
        assertThat(dto.getAuthorName()).isNull();
        assertThat(dto.getItemId()).isNull();
        assertThat(dto.getCreated()).isEqualTo(comment.getCreated());
    }
}