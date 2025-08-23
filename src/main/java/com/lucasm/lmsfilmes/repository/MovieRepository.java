package com.lucasm.lmsfilmes.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lucasm.lmsfilmes.model.Movies;

/**
 * Repositório para acesso a dados de filmes.
 */
@Repository
public interface MovieRepository extends MongoRepository<Movies, String> {

    // Busca todos os filmes associados a um email.
    List<Movies> findAllByEmail(String email);

    // Busca um filme específico por ID e email.
    Optional<Movies> findByMovieIdAndEmail(String movieId, String email);

}
