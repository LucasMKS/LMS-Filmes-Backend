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

    // Busca todas as séries associadas a um nickname.
    List<Series> findAllByNickname(String nickname);

    // Busca uma série específica por ID e nickname.
    Optional<Series> findBySerieIdAndNickname(String serieId, String nickname);
}
