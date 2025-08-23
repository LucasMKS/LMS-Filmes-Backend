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

import com.lucasm.lmsfilmes.model.FavoriteSerie;
import com.lucasm.lmsfilmes.service.FavoriteSerieService;

@RestController
@RequestMapping("/favorite/serie")
public class FavoriteSerieController {

    @Autowired
    private FavoriteSerieService favoriteService;

    // Método para adicionar/remover uma série dos favoritos.
    @PostMapping("/")
    public ResponseEntity<String> toggleFavoriteSerie(@RequestParam String serieId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        favoriteService.toggleFavoriteSerie(serieId, email);
        return ResponseEntity.ok("Favorite status updated");
    }

    // Método para verificar se uma série é favorita.
    @GetMapping("/status")
    public ResponseEntity<Boolean> getFavoriteStatusSeries(@RequestParam String serieId) {
        // Pega o email do usuário logado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isFavorite = favoriteService.isFavoriteSerie(serieId, email);
        return ResponseEntity.ok(isFavorite);
    }

    // Método para obter todas as séries favoritas de um usuário.
    @GetMapping("/")
    public ResponseEntity<List<FavoriteSerie>> getAllFavoritesSeries() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FavoriteSerie> series = favoriteService.getAllFavoritesSeries(email);
        return ResponseEntity.ok(series);
    }

    
}
