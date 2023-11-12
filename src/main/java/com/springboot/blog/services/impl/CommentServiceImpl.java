package com.springboot.blog.services.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment=new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());

        return comment;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        //Convert DTO into entity
        Comment comment=mapToEntity(commentDto);

        //Find that post from database whose id is equal to postId
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));

        //Save received post into our comment entity
        comment.setPost(post);

        //Save comment entity into database
        Comment newComment=commentRepository.save(comment);

        //Return DTO after converting entity
        return mapToDTO(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        //Retrieve list of comments(entities) for a specific post by its id
        List<Comment> comments=commentRepository.findByPostId(postId);

        //convert received list of entities into list of DTOs
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentbyId(Long postId, Long commentId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));

        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","commentId", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_GATEWAY,"Comment does not exist for this post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));

        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","commentId", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_GATEWAY,"Comment does not belong to this post");
        }


        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());

        Comment updatedComment=commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post=postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "id", postId));

        Comment comment=commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("comment","commentId", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_GATEWAY,"Comment does not belong to this post");
        }

        commentRepository.delete(comment);
    }
}
