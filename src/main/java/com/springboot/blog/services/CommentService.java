package com.springboot.blog.services;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentbyId(Long postId, Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest);

    void deleteComment(Long postId,Long commentId);


}
