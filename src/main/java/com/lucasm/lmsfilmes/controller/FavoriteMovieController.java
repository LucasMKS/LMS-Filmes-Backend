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

import com.lucasm.lmsfilmes.model.FavoriteMovie;
import com.lucasm.lmsfilmes.service.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteMovieController {

    @Autowired
    private FavoriteService favoriteService;

    // Método para adicionar/remover um filme dos favoritos.
    @PostMapping("/")
    public ResponseEntity<String> toggleFavorite(@RequestParam String movieId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        favoriteService.toggleFavorite(movieId, email);
        return ResponseEntity.ok("Favorite status updated");
    }

    // Método para verificar se um filme é favorito.
    @GetMapping("/status")
    public ResponseEntity<Boolean> getFavoriteStatus(@RequestParam String movieId) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isFavorite = favoriteService.isFavorite(movieId, email);
        return ResponseEntity.ok(isFavorite);
    }

    // Método para obter todos os filmes favoritos de um usuário.
    @GetMapping("/")
    public ResponseEntity<List<FavoriteMovie>> getAllFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FavoriteMovie> movies = favoriteService.getAllFavorites(email);
        return ResponseEntity.ok(movies);
    }

    
}
