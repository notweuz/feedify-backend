package ru.ntwz.feedify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.feedify.dto.request.PostCreateDto;
import ru.ntwz.feedify.dto.request.PostUpdateDto;
import ru.ntwz.feedify.dto.response.PostDto;
import ru.ntwz.feedify.dto.response.VoteDto;
import ru.ntwz.feedify.service.PostService;
import ru.ntwz.feedify.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final VoteService voteService;

    @Autowired
    public PostController(PostService postService, VoteService voteService) {
        this.postService = postService;
        this.voteService = voteService;
    }

    @PostMapping
    public PostDto create(
            @RequestBody @Valid PostCreateDto postCreateDTO
    ) {
        return postService.create(postCreateDTO);
    }

    @PostMapping("/{id}/comments")
    public PostDto createComment(
            @PathVariable Long id,
            @RequestBody @Valid PostCreateDto postCreateDTO
    ) {
        return postService.createComment(postCreateDTO, id);
    }

    @GetMapping("/{id}/comments")
    public List<PostDto> getComments(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return postService.getComments(id, page, size);
    }

    @GetMapping("/{id}")
    public PostDto getById(@PathVariable long id) {
        return postService.findById(id);
    }

    @GetMapping("/unique/{uniqueLink}")
    public PostDto getByUniqueLink(@PathVariable String uniqueLink) {
        return postService.findByUniqueLink(uniqueLink);
    }

    @PatchMapping("/{id}")
    public PostDto update(
            @PathVariable long id,
            @RequestBody @Valid PostUpdateDto postUpdateDTO
    ) {
        return postService.update(id, postUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable long id
    ) {
        postService.delete(id);
    }

    @PostMapping("/{postId}/vote")
    public VoteDto vote(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "true") boolean upvote
    ) {
        return voteService.vote(postId, upvote);
    }

    @DeleteMapping("/{postId}/attachments/{id}")
    public void deleteAttachment(
            @PathVariable Long postId,
            @PathVariable Long id
    ) {
        postService.deleteAttachment(postId, id);
    }

    @GetMapping("/feed/recommendations")
    public List<PostDto> findUserRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.findUserRecommendations(page, size);
    }

    @GetMapping("/feed/recent")
    public List<PostDto> findRecentPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.findAllRecentPosts(page, size);
    }

    @GetMapping("/feed/popular")
    public List<PostDto> findMonthlyPopularPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.findAllMonthlyPopularPosts(page, size);
    }

    @GetMapping("/{postId}/vote")
    public VoteDto getUserVote(
            @PathVariable Long postId
    ) {
        return voteService.getUserVote(postId);
    }
}