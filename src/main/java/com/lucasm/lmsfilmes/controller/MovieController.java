package com.lucasm.lmsfilmes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lucasm.lmsfilmes.dto.TmdbDTO;
import com.lucasm.lmsfilmes.service.MovieService;

/**
 * Controlador de filmes.
 */
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Método para buscar filmes por query.
    @GetMapping("/search")
    public ResponseEntity<List<TmdbDTO>> searchMovies(@RequestParam String query,
            @RequestParam(defaultValue = "1") int page) {
        List<TmdbDTO> movies = movieService.searchMovies(query, page);
        return ResponseEntity.ok(movies);
    }

    // Método para obter detalhes de um filme.
    @GetMapping("/{movieId}")
    public ResponseEntity<TmdbDTO> getMoviesDetails(@PathVariable String movieId) {
        TmdbDTO movie = movieService.getMoviesDetails(movieId);
        return ResponseEntity.ok(movie);
    }

    // Método para obter filmes populares.
    @GetMapping("/popular")
    public ResponseEntity<List<TmdbDTO>> moviePopular(@RequestParam(defaultValue = "1") int page) {
        List<TmdbDTO> movies = movieService.moviePopular(page);
        return ResponseEntity.ok(movies);
    }
}