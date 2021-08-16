package com.programming.blog.service;

import com.programming.blog.dto.PostRequest;
import com.programming.blog.exception.PostNotFoundException;
import com.programming.blog.model.Post;
import com.programming.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    @Transactional
    public void createPost(PostRequest postRequest) {
        Post post = mapFromDtoToPost(postRequest);
        postRepository.save(post);
    }

    @Transactional
    public PostRequest getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    @Transactional
    public List<PostRequest> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    //maps a Post to a PostRequest DTO
    private PostRequest mapFromPostToDto(Post post) {
        PostRequest postRequest = new PostRequest();

        postRequest.setId(post.getId());
        postRequest.setTitle(post.getTitle());
        postRequest.setContent(post.getContent());
        postRequest.setUsername(post.getUsername());

        return postRequest;
    }

    //maps a PostRequest DTO to a Post
    private Post mapFromDtoToPost(PostRequest postRequest) {
        Post post = new Post();
        User loggedInUser = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User not found"));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCreatedOn(Instant.now());
        post.setUsername(loggedInUser.getUsername());
        post.setUpdatedOn(Instant.now());

        return post;
    }
}
