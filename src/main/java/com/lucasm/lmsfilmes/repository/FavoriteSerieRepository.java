package com.lucasm.lmsfilmes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lucasm.lmsfilmes.model.FavoriteSerie;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso a dados de séries favoritas.
 */
@Repository
public interface FavoriteSerieRepository extends MongoRepository<FavoriteSerie, String> {

    // Busca todas as séries favoritas associadas a um email.
    List<FavoriteSerie> findAllByEmail(String email);

    // Busca uma série favorita específica por ID e email.
    Optional<FavoriteSerie> findBySerieIdAndEmail(String serieId, String email);

    // Busca todas as séries favoritas associadas a um email e um estado de favorito.
    List<FavoriteSerie> findByEmailAndFavorite(String email, boolean favorite);
}
