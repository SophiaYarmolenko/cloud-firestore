package org.example.controllers;

import org.example.domain.Review;
import org.example.domain.User;
import org.example.storage.FirestoreService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class UsersController {
    private FirestoreService firestoreService = new FirestoreService();

    public UsersController() throws IOException {
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.id = UUID.randomUUID().toString();
        firestoreService.createUser(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> listUsers() throws ExecutionException, InterruptedException {
        return firestoreService.listUsers();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable String userId) throws ExecutionException, InterruptedException {
        firestoreService.deleteUser(userId);
    }

    @GetMapping("/users/{userId}/reviews")
    public List<Review> getReviews(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return firestoreService.getUserReviews(userId);
    }
}