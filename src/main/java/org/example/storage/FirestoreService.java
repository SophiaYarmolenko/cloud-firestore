package org.example.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.example.domain.Book;
import org.example.domain.Review;
import org.example.domain.User;

import java.io.FileInputStream;

import com.google.cloud.firestore.Firestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreService {

    private FirestoreOptions firestoreOptions =
            FirestoreOptions.newBuilder()
                    .setProjectId("test-firestore-7475f")
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream("/home/syarmolenko/IdeaProjects/cloud-firestore/src/main/resources/firestore_config.json")))
                    .build();
    private Firestore db = firestoreOptions.getService();

    public FirestoreService() throws IOException {
    }

    public void createBook(Book r) throws ExecutionException, InterruptedException {
        var doc = db.collection("books").document(r.id);
        Map<String, String> data = new HashMap<>();
        data.put("name", r.name);
        data.put("author", r.author);
        doc.set(data);
    }

    public List<Book> listBooks() throws ExecutionException, InterruptedException {
        var q = db.collection("books").get();
        List<Book> rs = new LinkedList<>();

        for (var d : q.get().getDocuments()) {
            Book r = new Book();
            r.id = d.getId();
            r.author = d.getString("author");
            r.name = d.getString("name");
            rs.add(r);
        }

        return rs;
    }

    public void deleteBook(String bookId) throws ExecutionException, InterruptedException {
        db.collection("books").document(bookId).get().get().getReference().delete();
    }

    public void createUser(User user) {
        var doc = db.collection("users").document(user.id);
        HashMap<String, String> data = new HashMap<>();
        data.put("username", user.username);
        data.put("firstName", user.firstName);
        data.put("lastName", user.lastName);
        doc.set(data);
    }

    public List<User> listUsers() throws ExecutionException, InterruptedException {
        var q = db.collection("users").get();
        List<User> rs = new LinkedList<>();

        for (var d : q.get().getDocuments()) {
            User r = new User();
            r.id = d.getId();
            r.firstName = d.getString("firstName");
            r.lastName = d.getString("lastName");
            r.username = d.getString("username");
            rs.add(r);
        }

        return rs;
    }

    public void deleteUser(String userId) throws ExecutionException, InterruptedException {
        db.collection("users").document(userId).get().get().getReference().delete();
    }

    public List<Review> getUserReviews(String userId) throws ExecutionException, InterruptedException {
        var docs = db.collection("reviews").whereEqualTo("userId", userId).get().get().getDocuments();
        List<Review> rs = new LinkedList<>();
        for (var d : docs) {
            rs.add(fromDocument(d));
        }
        return rs;
    }

    public void createReview(String bookId, Review r) {
        var doc = db.collection("reviews").document(r.id);
        HashMap<String, String> data = new HashMap<>();
        data.put("bookId", r.bookId);
        data.put("text", r.text);
        data.put("userId", r.userId);
        doc.set(data);
    }

    public void createBooks(List<Book> rs) throws ExecutionException, InterruptedException {
        db.collection("books");

        var batch = db.batch();

        for (var r : rs) {
            var doc = db.collection("books").document(r.id);
            HashMap<String, String> data = new HashMap<>();
            data.put("name", r.name);
            data.put("author", r.author);
            doc.set(data);
        }

        batch.commit();
    }

    private Review fromDocument(QueryDocumentSnapshot d) throws ExecutionException, InterruptedException {
        var r = new Review();
        r.id = d.getId();
        r.bookId = d.getString("bookId");
        r.userId = d.getString("userId");
        r.text = d.getString("text");
        r.username = d.getString("username");

        var user = db.collection("users").document(r.userId).get().get();
        r.username = user.getString("username");

        return r;
    }

}