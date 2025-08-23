package com.lucasm.lmsfilmes.repository;

import com.lucasm.lmsfilmes.model.FavoriteMovie;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso a dados de filmes favoritos.
 */
@Repository
public interface FavoriteRepository extends MongoRepository<FavoriteMovie, String> {

    // Busca todos os filmes favoritos associados a um nickname.
    List<FavoriteMovie> findAllByEmail(String email);

    // Busca um filme favorito específico por ID e nickname.
    Optional<FavoriteMovie> findByMovieIdAndEmail(String movieId, String email);

    // Busca um filme favorito específico por email e ID.
    Optional<FavoriteMovie> findByEmailAndMovieId(String email, String movieId);

    // Busca todos os filmes favoritos associados a um email e um estado de favorito.
    List<FavoriteMovie> findByEmailAndFavorite(String email, boolean favorite);
}
