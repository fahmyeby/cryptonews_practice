package com.example.crypto_practice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.crypto_practice.model.News;
import com.example.crypto_practice.service.NewsService;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired NewsService service;

    @GetMapping("")
    public String showList(Model model){
        List<News> news = service.getArticlesFromAPI();
        model.addAttribute("news", news);
        return "list";
    }

    @GetMapping("/articles")
    public String viewSavedArticles(Model model){
        List<News> news = service.getNewsFromRedis();
        model.addAttribute("news", news);
        return "saved";
    }

    @PostMapping("/articles")
    public String saveArticles(@RequestParam("save") List<String> selectedArticles, Model model){
        if (selectedArticles != null){
            service.saveArticles(selectedArticles);
        }
        List<News> news = service.getNewsFromRedis();
        model.addAttribute("news", news);
        return "saved";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        service.delete(id);
        return "redirect:/news/articles";
    }

}
