package com.lucasm.lmsfilmes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.exceptions.ResourceNotFoundException;
import com.lucasm.lmsfilmes.model.FavoriteMovie;
import com.lucasm.lmsfilmes.repository.FavoriteRepository;

@Service
public class FavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void toggleFavorite(String movieId, String email) {
        Optional<FavoriteMovie> optionalFavorite = favoriteRepository.findByMovieIdAndEmail(movieId, email);
        FavoriteMovie favoriteMovie = optionalFavorite.orElseGet(() -> new FavoriteMovie());
        favoriteMovie.setMovieId(movieId);
        favoriteMovie.setEmail(email);
        favoriteMovie.setFavorite(!favoriteMovie.isFavorite());
        favoriteRepository.save(favoriteMovie);
        logger.info("Filme {} favorito atualizado para {} pelo usuário {}", movieId, favoriteMovie.isFavorite(), email);
    }

    public boolean isFavorite(String movieId, String email) {
        logger.info("Verificando se o filme {} é favorito do usuário {}", movieId, email);

        Optional<FavoriteMovie> optionalFavorite = favoriteRepository.findByMovieIdAndEmail(movieId, email);
        boolean result = optionalFavorite.map(FavoriteMovie::isFavorite).orElse(false);

        logger.debug("Resultado do favorite: {}", result);
        return result;
    }

    public List<FavoriteMovie> getAllFavorites(String email) {
        logger.info("Buscando todos os filmes favoritos do usuário {}", email);
        List<FavoriteMovie> allFavorites = favoriteRepository.findAllByEmail(email)
                .stream()
                .filter(FavoriteMovie::isFavorite)
                .collect(Collectors.toList());

        if (allFavorites.isEmpty()) {
            logger.warn("Nenhum filme favoritado encontrado para o usuário {}", email);
            throw new ResourceNotFoundException("Nenhum filme favoritado encontrado para o usuário: " + email);
        }

        logger.info("Encontrados {} filmes favoritados para o usuário {}", allFavorites.size(), email);
        return allFavorites;
    }
}
