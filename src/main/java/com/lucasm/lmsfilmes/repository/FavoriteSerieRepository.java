package com.lucasm.lmsfilmes.repository;

import com.lucasm.lmsfilmes.model.FavoriteSerieModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso a dados de séries favoritas.
 */
@Repository
public interface FavoriteSerieRepository extends MongoRepository<FavoriteSerieModel, String> {

    // Busca todas as séries favoritas associadas a um nickname.
    List<FavoriteSerieModel> findAllByNickname(String nickname);

    // Busca uma série favorita específica por ID e nickname.
    Optional<FavoriteSerieModel> findBySerieIdAndNickname(String serieId, String nickname);

    // Busca todas as séries favoritas associadas a um nickname e um estado de favorito.
    List<FavoriteSerieModel> findByNicknameAndFavorite(String nickname, boolean favorite);
}
