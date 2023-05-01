package org.example.controllers;

import org.example.domain.Book;
import org.example.domain.Review;
import org.example.storage.FirestoreService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
public class BookController {
    private FirestoreService firestoreService = new FirestoreService();

    public BookController() throws IOException {
    }

    @PutMapping("/books")
    public Book createBook(@RequestBody Book book) throws ExecutionException, InterruptedException {
        book.id = UUID.randomUUID().toString();
        firestoreService.createBook(book);
        return book;
    }

    @GetMapping("/books")
    public List<Book> listBooks() throws ExecutionException, InterruptedException {
        return firestoreService.listBooks();
    }

    @DeleteMapping("/books/{bookId}")
    public void deleteBook(@PathVariable String bookId) throws ExecutionException, InterruptedException {
        firestoreService.deleteBook(bookId);
    }

    @PostMapping("/books/{bookId}/reviews")
    public void createReview(@PathVariable String bookId, @RequestBody Review review) {
        review.id = UUID.randomUUID().toString();
        review.bookId = bookId;
        firestoreService.createReview(bookId, review);
    }

    @PostMapping("/books")
    public List<Book> createBooks(@RequestBody List<Book> rs) throws ExecutionException, InterruptedException {
        for (var r : rs) {
            r.id = UUID.randomUUID().toString();
        }
        firestoreService.createBooks(rs);
        return rs;
    }
}