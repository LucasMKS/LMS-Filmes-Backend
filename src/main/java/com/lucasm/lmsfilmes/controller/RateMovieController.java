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
import com.lucasm.lmsfilmes.service.RateMovieService;

/**
 * Controlador de avaliações.
 */
@RestController
@RequestMapping("/rate/movies")
public class RateMovieController {

    @Autowired
    private RateMovieService rateService;

    // Método para avaliar um filme.
    @PostMapping("")
    public ResponseEntity<Movies> ratingMovies(
            @RequestParam String movieId, 
            @RequestParam String rating, 
            @RequestParam String title, 
            @RequestParam String poster_path,
            @RequestParam(required = false) String comment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(rateService.rateMovie(movieId, email, rating, title, poster_path, comment));
    }

    // // Método para obter as avaliações de um usuário.
    @GetMapping("")
    public ResponseEntity<List<Movies>> searchMovies() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Movies> movies = rateService.searchRatedMovies(email);
        return ResponseEntity.ok(movies);
    }
}