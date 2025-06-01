package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getText());
    }

    public static Comment toComment(Long id, CommentDto dto, Item item, User user, LocalDateTime dataTime) {
        return new Comment(id,
                dto.getText(),
                item,
                user,
                dataTime);
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated(),
                comment.getItem().getId()
        );
    }
}
