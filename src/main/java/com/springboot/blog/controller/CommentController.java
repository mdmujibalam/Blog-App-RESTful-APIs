package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") Long postId,
                                                    @RequestBody CommentDto commentDto){

        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);

    }
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(name = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);

    }
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId){


        return new ResponseEntity<>(commentService.getCommentbyId(postId,commentId), HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,@PathVariable(name = "commentId") Long commentId,
                                                    @RequestBody CommentDto commentRequest){

      CommentDto updatedComment= commentService.updateComment(postId,commentId,commentRequest);

      return new ResponseEntity<>(updatedComment,HttpStatus.OK);
    }
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") Long postId,
                                                @PathVariable(name = "commentId") Long commentId){

        commentService.deleteComment(postId, commentId);

        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);

    }

}
