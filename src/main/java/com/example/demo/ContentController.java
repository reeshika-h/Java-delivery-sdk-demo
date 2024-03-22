package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class ContentController {

    private final ContentFetcher contentFetcher;

    public ContentController(ContentFetcher contentFetcher) {
        this.contentFetcher = contentFetcher;
    }

    @GetMapping("/content-types")
    public String fetchAllContentTypes(Model model) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        contentFetcher.fetchAllContent(contentTypes -> future.complete(contentTypes));
        model.addAttribute("contentTypes", future.join());
        return "index";
    }
    @GetMapping("/home")
    public String displayHome(Model model){
//        CompletableFuture<List<String>> future = new CompletableFuture<>();
//        contentFetcher.fetchAll("blog_post","title",titles -> future.complete(titles));
//
//        CompletableFuture<List<String>> bodyfuture = new CompletableFuture<>();
//        contentFetcher.fetchAll("blog_post","body",titles -> bodyfuture.complete(titles));
//
//        CompletableFuture<List<String>> imgFuture = new CompletableFuture<>();
//        contentFetcher.fetchAllImg("blog_post","featured_image", authorImg -> imgFuture.complete(authorImg));
//
//        model.addAttribute("titles", future.join());
//        model.addAttribute("authorImg",imgFuture.join());
//        model.addAttribute("titles",bodyfuture.join());
//        return "testIndex";

        CompletableFuture<List<String>> titleFuture = new CompletableFuture<>();
        contentFetcher.fetchAll("blog_post", "title", titles -> titleFuture.complete(titles));

        CompletableFuture<List<String>> imgFuture = new CompletableFuture<>();
        contentFetcher.fetchAllImg("blog_post", "featured_image",  authorImg -> imgFuture.complete(authorImg));

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(titleFuture,imgFuture);

        allOfFuture.thenRun(() -> {
            model.addAttribute("titles", titleFuture.join());
            model.addAttribute("authorImg", imgFuture.join());
        }).join();

        return "testIndex";
    }

    @GetMapping("/home/authors")
    public String displayAuthors(Model model){

        CompletableFuture<List<String>> future = new CompletableFuture<>();
        contentFetcher.fetchAll("author","title", titles -> future.complete(titles));

        CompletableFuture<List<String>> imgFuture = new CompletableFuture<>();
        contentFetcher.fetchAllImg("author","picture", authorImg -> imgFuture.complete(authorImg));

        model.addAttribute("titles", future.join());
        model.addAttribute("authorImg", imgFuture.join());
        return "authors";
    }
}
