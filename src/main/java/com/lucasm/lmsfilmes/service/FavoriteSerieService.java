package com.lucasm.lmsfilmes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.model.FavoriteSerie;
import com.lucasm.lmsfilmes.repository.FavoriteSerieRepository;

@Service
public class FavoriteSerieService {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteSerieService.class);

    private final FavoriteSerieRepository favoriteRepository;

    public FavoriteSerieService(FavoriteSerieRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void toggleFavoriteSerie(String serieId, String email) {
        logger.info("SÉRIE: Tentando alterar favorito - SerieID: {}, Email: {}", serieId, email);
        
        Optional<FavoriteSerie> optionalFavorite = favoriteRepository.findBySerieIdAndEmail(serieId, email);
        FavoriteSerie favoriteSerie = optionalFavorite.orElseGet(() -> {
            logger.info("SÉRIE: Criando novo favorito para SerieID: {}", serieId);
            return new FavoriteSerie();
        });
        
        favoriteSerie.setSerieId(serieId);
        favoriteSerie.setEmail(email);
        boolean newStatus = !favoriteSerie.isFavorite();
        favoriteSerie.setFavorite(newStatus);
        
        FavoriteSerie saved = favoriteRepository.save(favoriteSerie);
        logger.info("SÉRIE: Favorito salvo - ID: {}, SerieID: {}, Status: {}, Collection: favorite_series", 
                   saved.getId(), saved.getSerieId(), saved.isFavorite());
    }

    public boolean isFavoriteSerie(String serieId, String email) {
        logger.info("Verificando se a série {} é favorita do usuário {}", serieId, email);

        Optional<FavoriteSerie> optionalFavorite = favoriteRepository.findBySerieIdAndEmail(serieId, email);
        boolean result = optionalFavorite.map(FavoriteSerie::isFavorite).orElse(false);

        logger.debug("Resultado do favorite: {}", result);
        return result;
    }

    public List<FavoriteSerie> getAllFavoritesSeries(String email) {
        logger.info("Buscando todas as séries favoritas do usuário {}", email);
        List<FavoriteSerie> allFavorites = favoriteRepository.findAllByEmail(email)
                .stream()
                .filter(FavoriteSerie::isFavorite)
                .collect(Collectors.toList());

        if (allFavorites.isEmpty()) {
            logger.info("Nenhuma série favoritada encontrada para o usuário {}", email);
        } else {
            logger.info("Encontrados {} séries favoritas para o usuário {}", allFavorites.size(), email);
        }

        return allFavorites;
    }
}
