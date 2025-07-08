package ru.ntwz.feedify.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.mapper.PostMapper;
import ru.ntwz.feedify.dto.request.PostCreateDto;
import ru.ntwz.feedify.dto.request.PostUpdateDto;
import ru.ntwz.feedify.dto.response.PostDto;
import ru.ntwz.feedify.exception.*;
import ru.ntwz.feedify.model.Post;
import ru.ntwz.feedify.model.StorageEntry;
import ru.ntwz.feedify.model.User;
import ru.ntwz.feedify.repository.PostRepository;
import ru.ntwz.feedify.service.PostService;
import ru.ntwz.feedify.service.StorageService;
import ru.ntwz.feedify.service.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final StorageService storageService;
    private final CommonConfig commonConfig;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService, StorageService storageService, CommonConfig commonConfig) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.storageService = storageService;
        this.commonConfig = commonConfig;
    }

    private List<StorageEntry> validateTempFiles(User user, PostCreateDto postCreateDTO) {
        List<StorageEntry> temporaryFiles = new ArrayList<>();

        if (postCreateDTO.getAttachments() != null && !postCreateDTO.getAttachments().isEmpty()) {
            log.info("Validating {} attachments for post", postCreateDTO.getAttachments().size());

            temporaryFiles = storageService.getTemporaryFilesByIds(postCreateDTO.getAttachments(), user);

            log.info("Validated {} attachments for post", temporaryFiles.size());

            int maxAttachments = commonConfig.getContent().getMaxAttachments();
            if (temporaryFiles.size() > maxAttachments) {
                throw new TooManyAttachmentsException("Too many attachments. Trying to add: " + temporaryFiles.size() +
                        ", maximum allowed: " + maxAttachments);
            }
        }

        return temporaryFiles;
    }

    @Override
    @CachePut(value = "posts", key = "#result.id")
    public PostDto create(User user, PostCreateDto postCreateDTO) {
        List<StorageEntry> temporaryFiles = new ArrayList<>();

        try {
            temporaryFiles = validateTempFiles(user, postCreateDTO);

            Post post = PostMapper.toPost(postCreateDTO);
            post.setAuthor(user);

            Post savedPost = postRepository.save(post);

            if (!temporaryFiles.isEmpty()) {
                storageService.attachFilesToPost(temporaryFiles, savedPost);
                savedPost = postRepository.findById(savedPost.getId()).orElse(savedPost);
            }

            log.info("Created post with {} attachments: {}", temporaryFiles.size(), savedPost.getContent());

            return PostMapper.toPostDTO(savedPost);

        } catch (Exception e) {
            if (!temporaryFiles.isEmpty()) {
                storageService.deleteTemporaryFiles(temporaryFiles);
                log.warn("Deleted temporary files due to error: {}", e.getMessage());
            }
            throw e;
        }
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    public PostDto findById(Long id) {
        Post post = getPostOrThrow(id);

        PostDto postDTO = PostMapper.toPostDTO(post);

        log.info("Found post: {}", postDTO.getContent());

        return postDTO;
    }

    @Override
    public List<PostDto> getPostsByUser(long userId, int page, int size) {
        User user = userService.getById(userId);

        log.info("Retrieved posts by user: {}", user.getUsername());

        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByAuthor(user, pageable).getContent().stream().map(PostMapper::toPostDTO).toList();
    }

    @Override
    @CachePut(value = "posts", key = "#result.id")
    public PostDto createComment(User user, PostCreateDto createDTO, Long parentPostId) {
        Post parentPost = getPostOrThrow(parentPostId);
        List<StorageEntry> temporaryFiles = new ArrayList<>();

        try {
            temporaryFiles = validateTempFiles(user, createDTO);

            Post post = PostMapper.toPost(createDTO);
            post.setParentPost(parentPost);
            post.setAuthor(user);

            Post savedPost = postRepository.save(post);

            if (!temporaryFiles.isEmpty()) {
                storageService.attachFilesToPost(temporaryFiles, savedPost);
                savedPost = postRepository.findById(savedPost.getId()).orElse(savedPost);
            }

            log.info("Created comment with {} attachments on post {}: {}",
                    temporaryFiles.size(), parentPostId, savedPost.getContent());

            return PostMapper.toPostDTO(savedPost);

        } catch (Exception e) {
            if (!temporaryFiles.isEmpty()) {
                storageService.deleteTemporaryFiles(temporaryFiles);
                log.warn("Deleted temporary files due to error: {}", e.getMessage());
            }
            throw e;
        }
    }

    @Override
    @Cacheable(value = "posts", key = "#uniqueLink")
    public PostDto findByUniqueLink(String uniqueLink) {
        Post post = postRepository.findByUniqueLink(uniqueLink)
                .orElseThrow(() -> new PostNotFoundException("Post with unique link '" + uniqueLink + "' not found"));

        log.info("Found post by unique link: {}", post.getContent());

        return PostMapper.toPostDTO(post);
    }

    @Override
    public List<PostDto> getComments(Long parentPostId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> comments = postRepository.findTopCommentsByParentPostId(parentPostId, pageable).getContent();

        log.info("Retrieved {} comments for post with id {}", comments.size(), parentPostId);

        return comments.stream()
                .map(PostMapper::toPostDTO)
                .toList();
    }

    @Override
    @CachePut(value = "posts", key = "#id")
    public PostDto update(User user, Long id, PostUpdateDto postUpdateDTO) {
        Post post = getPostOrThrow(id);

        if (!Objects.equals(post.getAuthor().getId(), user.getId()))
            throw new NotPostsOwnerException("You are not the owner of this post");

        if (postUpdateDTO.getContent() != null && !postUpdateDTO.getContent().isBlank()) {
            post.setContent(postUpdateDTO.getContent());
        }

        log.info("Updating post: {}", post.getContent());

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    @CacheEvict(value = "posts", key = "#id")
    public void delete(User user, Long id) {
        Post post = getPostOrThrow(id);

        if (!Objects.equals(post.getAuthor().getId(), user.getId()))
            throw new NotPostsOwnerException("You are not the owner of this post");
        if (post.getIsDeleted()) throw new PostAlreadyDeletedException("Post with id " + id + " is already deleted");

        post.setIsDeleted(true);
        post.setContent(null);

        List<StorageEntry> attachmentsToDelete = new ArrayList<>(post.getAttachments());

        post.getAttachments().clear();
        postRepository.save(post);

        storageService.deleteFiles(attachmentsToDelete);

        log.info("Deleting post with id: {}", id);
    }

    @Override
    @CacheEvict(value = "posts", key = "#postId")
    public void deleteAttachment(User user, Long postId, Long attachmentId) {
        Post post = getPostOrThrow(postId);

        if (!Objects.equals(post.getAuthor().getId(), user.getId())) {
            throw new NotPostsOwnerException("You are not the owner of this post");
        }

        if (post.getIsDeleted()) {
            throw new PostAlreadyDeletedException("Post with id " + postId + " is deleted");
        }

        StorageEntry attachment = post.getAttachments().stream()
                .filter(att -> Objects.equals(att.getId(), attachmentId))
                .findFirst()
                .orElseThrow(() -> new AttachmentNotFoundException("Attachment with id " + attachmentId + " not found"));

        log.info("Deleting attachment with id: {} from post: {}", attachmentId, postId);

        post.getAttachments().remove(attachment);
        postRepository.save(post);
        storageService.deleteFile(attachment);
    }

    @Override
    public List<PostDto> findUserRecommendations(User user, int page, int size) {
        log.info("Finding user recommendations for user: {}, page: {}, size: {}", user.getUsername(), page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> followingAndTheirFollowings = postRepository.findPostsByFollowingAndTheirFollowings(user, pageable);
        Page<Post> topPostsByRatingAndCommentsMonthly = postRepository.findTopPostsByRatingAndCommentsMonthly(
                getThirtyOneDaysAgo(), pageable);
        Page<Post> postsLikedByFollowings = postRepository.findPostsLikedByFollowedUsers(user, pageable);

        Set<Post> recommendedPosts = new HashSet<>(followingAndTheirFollowings.getContent());
        recommendedPosts.addAll(new HashSet<>(topPostsByRatingAndCommentsMonthly.getContent()));
        recommendedPosts.addAll(new HashSet<>(postsLikedByFollowings.getContent()));

        List<PostDto> result = recommendedPosts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .filter(post -> !post.getAuthor().getId().equals(user.getId()))
                .map(PostMapper::toPostDTO)
                .toList();

        log.info("Found {} recommended posts for user: {}", result.size(), user.getUsername());
        return result;
    }

    @Override
    public List<PostDto> findAllRecentPosts(int page, int size) {
        log.info("Finding all recent posts, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> allRecentPosts = postRepository.findAllActivePosts(pageable);
        List<PostDto> result = allRecentPosts.getContent().stream().map(PostMapper::toPostDTO).toList();

        log.info("Found {} recent posts", result.size());
        return result;
    }

    @Override
    public List<PostDto> findAllMonthlyPopularPosts(int page, int size) {
        log.info("Finding all monthly popular posts, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> allPopularMonthlyPosts = postRepository.findTopPostsByRatingAndCommentsMonthly(
                getThirtyOneDaysAgo(), pageable);
        List<PostDto> result = allPopularMonthlyPosts.getContent().stream().map(PostMapper::toPostDTO).toList();

        log.info("Found {} monthly popular posts", result.size());
        return result;
    }

    public Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
    }

    private Instant getThirtyOneDaysAgo() {
        return Instant.now().minus(Duration.ofDays(31));
    }
}
