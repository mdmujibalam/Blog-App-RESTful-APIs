package com.springboot.blog.services.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
//    @Autowired
//    public PostServiceImpl(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }

    @Override
    public PostDto createPost(PostDto postDto){

      //convert DTO into entity
      Post post=mapToEntity(postDto);

      //sending to repository for saving in db
      Post newPost=postRepository.save(post);

      //convert entity into DTO
      return mapToDTO(newPost);
    }

    @Override
    public List<PostDto> getAllPosts() {
       //received Post Entities from database using findAll query method
       List<Post> posts=postRepository.findAll();

       //converting received entities into DTOs
        List<PostDto> collect = posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        return collect;
        
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id", id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post updatedPost=postRepository.save(post);

        return mapToDTO(updatedPost);

    }

    @Override
    public void deletePostById(Long id) {
        Post post=postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id", id));

        postRepository.delete(post);
    }

    private PostDto mapToDTO(Post post){
      PostDto postDto=new PostDto();
      postDto.setId(post.getId());
      postDto.setTitle(post.getTitle());
      postDto.setDescription(post.getDescription());
      postDto.setContent(post.getContent());

      return postDto;
    }

    private Post mapToEntity(PostDto postDto){
       Post post=new Post();
       post.setTitle(postDto.getTitle());
       post.setContent(postDto.getContent());
       post.setDescription(postDto.getDescription());

       return post;
    }
}
