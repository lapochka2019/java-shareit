package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
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

    private final CommentMapper mapper = CommentMapper.INSTANCE;

    private Item item;
    private User author;
    private Comment comment;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);

        author = new User();
        author.setId(2L);
        author.setName("Alice");

        comment = new Comment();
        comment.setId(100L);
        comment.setText("Great item!");
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Преобразование Comment в CommentDto")
    void toCommentDto_ShouldMapTextAndIgnoreOtherFields() {
        CommentDto dto = mapper.toCommentDto(comment);

        assertThat(dto).isNotNull();
        assertThat(dto.getText()).isEqualTo("Great item!");
    }

    @Test
    @DisplayName("Преобразование Comment в CommentResponseDto")
    void toCommentResponseDto_ShouldMapAllRequiredFields() {
        CommentResponseDto responseDto = mapper.toCommentResponseDto(comment);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(100L);
        assertThat(responseDto.getText()).isEqualTo("Great item!");
        assertThat(responseDto.getAuthorName()).isEqualTo("Alice");
        assertThat(responseDto.getItemId()).isEqualTo(1L);
        assertThat(responseDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Создание Comment из DTO с дополнительными параметрами")
    void toComment_ShouldCreateCommentWithGivenFields() {
        CommentDto dto = new CommentDto();
        dto.setText("Updated text");

        LocalDateTime now = LocalDateTime.now();
        Comment createdComment = mapper.toComment(99L, dto, item, author, now);

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isEqualTo(99L);
        assertThat(createdComment.getText()).isEqualTo("Updated text");
        assertThat(createdComment.getItem()).isEqualTo(item);
        assertThat(createdComment.getAuthor()).isEqualTo(author);
        assertThat(createdComment.getCreated()).isEqualTo(now);
    }
}