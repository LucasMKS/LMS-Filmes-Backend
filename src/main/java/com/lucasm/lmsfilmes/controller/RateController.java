package com.lucasm.lmsfilmes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucasm.lmsfilmes.model.Movies;
import com.lucasm.lmsfilmes.model.Series;
import com.lucasm.lmsfilmes.service.RateService;

/**
 * Controlador de avaliações.
 */
@RestController
@RequestMapping("/rate")
public class RateController {

    @Autowired
    private RateService rateService;

    // Método para avaliar um filme.
    @PostMapping("/m/save")
    public ResponseEntity<Movies> ratingMovies(@RequestParam String movieId, @RequestParam String rating, @RequestParam String title, @RequestParam String poster_path) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(rateService.rateMovie(movieId, email, rating, title, poster_path));
    }

    // // Método para obter as avaliações de um usuário.
    @GetMapping("/m/rated")
    public ResponseEntity<List<Movies>> searchMovies() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Movies> movies = rateService.searchRatedMovies(email);
        return ResponseEntity.ok(movies);
    }

    // Método para avaliar uma série.
    @PostMapping("/s/save")
    public ResponseEntity<Series> ratingSeries(@RequestParam String serieId, @RequestParam String rating, @RequestParam String title, @RequestParam String poster_path) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(rateService.rateSerie(serieId, email, rating, title, poster_path));
    }

    // Método para obter as avaliações de uma série.
    @GetMapping("/s/rated")
    public ResponseEntity<Series> searchRatedSeries() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Series series = rateService.searchRatedSeries(email);
        return ResponseEntity.ok(series);
    }

}