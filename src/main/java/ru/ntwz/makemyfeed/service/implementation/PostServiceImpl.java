package ru.ntwz.makemyfeed.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.mapper.PostMapper;
import ru.ntwz.makemyfeed.dto.request.PostCreateDTO;
import ru.ntwz.makemyfeed.dto.request.PostUpdateDTO;
import ru.ntwz.makemyfeed.dto.response.CommentDTO;
import ru.ntwz.makemyfeed.dto.response.PostDTO;
import ru.ntwz.makemyfeed.exception.NotPostsOwnerException;
import ru.ntwz.makemyfeed.exception.PostAlreadyDeletedException;
import ru.ntwz.makemyfeed.exception.PostHasNoContentAtAllException;
import ru.ntwz.makemyfeed.exception.PostNotFoundException;
import ru.ntwz.makemyfeed.exception.AttachmentNotFoundException;
import ru.ntwz.makemyfeed.exception.TooManyAttachmentsException;
import ru.ntwz.makemyfeed.model.Post;
import ru.ntwz.makemyfeed.model.StorageEntry;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.repository.PostRepository;
import ru.ntwz.makemyfeed.service.PostService;
import ru.ntwz.makemyfeed.service.StorageService;
import ru.ntwz.makemyfeed.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public PostDTO create(User user, PostCreateDTO postCreateDTO) {
        Post post = PostMapper.toPost(postCreateDTO);
        post.setAuthor(user);

        log.info("Creating post: {}", post.getContent());

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        List<Post> comments = postRepository.findTop10CommentsByParentPostId(id, Pageable.ofSize(4)).getContent();

        PostDTO postDTO = PostMapper.toPostDTO(post);
        postDTO.setComments(comments.stream()
                .map(PostMapper::toCommentDTO)
                .toList());

        log.info("Found post: {}", postDTO.getContent());

        return postDTO;
    }

    @Override
    public List<PostDTO> getPostsByUser(long userId, int page, int size) {
        User user = userService.getById(userId);

        log.info("Retrieved posts by user: {}", user.getUsername());

        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByAuthor(user, pageable).getContent().stream().map(PostMapper::toPostDTO).toList();
    }

    @Override
    public PostDTO createComment(User user, PostCreateDTO createDTO, Long parentPostId) {
        Post parentPost = postRepository.findById(parentPostId).orElseThrow(() -> new PostNotFoundException("Post with id " + parentPostId + " not found"));

        Post post = PostMapper.toPost(createDTO);
        post.setParentPost(parentPost);
        post.setAuthor(user);

        log.info("Creating comment on post {}: {}", parentPostId, post.getContent());

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public PostDTO findByUniqueLink(String uniqueLink) {
        Post post = postRepository.findByUniqueLink(uniqueLink)
                .orElseThrow(() -> new PostNotFoundException("Post with unique link '" + uniqueLink + "' not found"));

        log.info("Found post by unique link: {}", post.getContent());

        return PostMapper.toPostDTO(post);
    }

    @Override
    public List<CommentDTO> getComments(Long parentPostId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Post> comments = postRepository.findTopCommentsByParentPostId(parentPostId, pageable).getContent();

        log.info("Retrieved {} comments for post with id {}", comments.size(), parentPostId);

        return comments.stream()
                .map(PostMapper::toCommentDTO)
                .toList();
    }

    @Override
    public PostDTO update(User user, Long id, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        if (!Objects.equals(post.getAuthor().getId(), user.getId())) throw new NotPostsOwnerException("You are not the owner of this post");

        if (postUpdateDTO.getContent() != null && !postUpdateDTO.getContent().isBlank()) {
            post.setContent(postUpdateDTO.getContent());
        }

        log.info("Updating post: {}", post.getContent());

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public void delete(User user, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with id " + id + " not found"));

        if (!Objects.equals(post.getAuthor().getId(), user.getId())) throw new NotPostsOwnerException("You are not the owner of this post");
        if (post.getIsDeleted()) throw new PostAlreadyDeletedException("Post with id " + id + " is already deleted");

        post.setIsDeleted(true);
        post.setContent(null);
        
        List<StorageEntry> attachmentsToDelete = new ArrayList<>(post.getAttachments());
        
        post.getAttachments().clear();
        postRepository.save(post);
        
        attachmentsToDelete.forEach(attachment -> {
            storageService.deleteFile(attachment);
            log.info("Deleted attachment with id: {}", attachment.getId());
        });

        log.info("Deleting post with id: {}", id);
    }

    @Override
    public PostDTO addAttachments(User user, Long postId, List<MultipartFile> attachments) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

        if (!Objects.equals(post.getAuthor().getId(), user.getId())) {
            throw new NotPostsOwnerException("You are not the owner of this post");
        }

        if (post.getIsDeleted()) {
            throw new PostAlreadyDeletedException("Post with id " + postId + " is deleted");
        }

        int maxAttachments = commonConfig.getContent().getMaxAttachments();
        if (post.getAttachments().size() + attachments.size() > maxAttachments) {
            throw new TooManyAttachmentsException("Too many attachments. Current: " + post.getAttachments().size() + 
                    ", trying to add: " + attachments.size() + ", maximum allowed: " + maxAttachments);
        }

        log.info("Adding {} attachments to post: {}", attachments.size(), postId);

        for (MultipartFile attachment : attachments) {
            StorageEntry storageEntry = storageService.uploadFile(attachment, user);
            storageEntry.setPost(post);
            post.getAttachments().add(storageEntry);
        }

        return PostMapper.toPostDTO(postRepository.save(post));
    }

    @Override
    public void deleteAttachment(User user, Long postId, Long attachmentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

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
}
