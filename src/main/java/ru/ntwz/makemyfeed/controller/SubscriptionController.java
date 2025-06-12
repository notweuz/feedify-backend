package ru.ntwz.makemyfeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ntwz.makemyfeed.constant.AttributesConstants;
import ru.ntwz.makemyfeed.dto.response.SubscriptionDTO;
import ru.ntwz.makemyfeed.model.User;
import ru.ntwz.makemyfeed.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{username}")
    public SubscriptionDTO subscribe(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        return subscriptionService.subscribe(user, username);
    }

    @DeleteMapping("/{username}")
    public void unsubscribe(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        subscriptionService.unsubscribe(user, username);
    }

    @GetMapping("/followers/{username}")
    public List<SubscriptionDTO> getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return subscriptionService.getFollowers(username, page, size);
    }

    @GetMapping("/following/{username}")
    public List<SubscriptionDTO> getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return subscriptionService.getFollowing(username, page, size);
    }

    @GetMapping("/check/{username}")
    public boolean isFollowing(
            @RequestAttribute(AttributesConstants.USER) User user,
            @PathVariable String username
    ) {
        return subscriptionService.isFollowing(user, username);
    }
} 