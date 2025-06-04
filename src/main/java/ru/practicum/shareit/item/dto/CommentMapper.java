package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", source = "id")
    Comment toComment(Long id, CommentDto dto, Item item, User author, LocalDateTime created);

    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "itemId", source = "comment.item.id")
    CommentResponseDto toCommentResponseDto(Comment comment);
}