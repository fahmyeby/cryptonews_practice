package com.example.crypto_practice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.crypto_practice.model.News;
import com.example.crypto_practice.service.NewsService;

@Controller
@RequestMapping("/api/news")
public class NewsRestController {
    @Autowired NewsService service;

    @GetMapping("")
    public ResponseEntity<List<News>> getArticlesFromAPI(){
        List<News> news = new ArrayList<>();
        news = service.getArticlesFromAPI();
        return ResponseEntity.ok().body(news);
    }
}
