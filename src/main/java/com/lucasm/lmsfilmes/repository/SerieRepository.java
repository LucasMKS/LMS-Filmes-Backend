package com.lucasm.lmsfilmes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.lucasm.lmsfilmes.model.Series;

/**
 * Repositório para acesso a dados de séries.
 */
@Repository
public interface SerieRepository extends MongoRepository<Series, String>  {

    // Busca todas as séries associadas a um email.
    List<Series> findAllByEmail(String email);

    // Busca uma série específica por ID e email.
    Optional<Series> findBySerieIdAndEmail(String serieId, String email);
}
