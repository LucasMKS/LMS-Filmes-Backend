package com.lucasm.lmsfilmes.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.exceptions.MovieServiceException;
import com.lucasm.lmsfilmes.model.Series;
import com.lucasm.lmsfilmes.repository.SerieRepository;

/**
 * Serviço para gerenciar avaliações de filmes e séries.
 */
@Service
public class RateSerieService {

    private static final Logger logger = LoggerFactory.getLogger(RateSerieService.class);

    @Autowired
    private SerieRepository serieRepository;

    @CacheEvict(value = "userRatedSeries", key = "#email")
    public Series rateSerie(String serieId, String email, String rating, String title, String poster_path, String comment) {
        try {
            logger.info("Usuário {} avaliando série {}(id:{}) com nota {} e comentário: {}", email, title, serieId, rating, comment != null ? "sim" : "não");

            Optional<Series> optionalSerie = serieRepository.findBySerieIdAndEmail(serieId, email);
            if (optionalSerie.isPresent()) {
                Series existing = optionalSerie.get();
                updateRateSerie(rating, comment, existing);
                logger.warn("Usuário {} já avaliou a série {}(id:{}). Nota existente: {}", email, title, serieId, existing.getMyVote());
                return existing; // Retorna a nota existente
            }

            // Criar novo registro de avaliação
            Series serie = new Series();
            serie.setSerieId(serieId);
            serie.setEmail(email);
            serie.setMyVote(rating);
            serie.setComment(comment);
            serie.setTitle(title);
            serie.setPoster_path(poster_path);
            serie.onCreate();
            serieRepository.save(serie);

            logger.info("Série {} avaliada com sucesso pelo usuário {}. Nota: {}", serieId, email, rating);
            return serie;

        } catch (Exception e) {
            logger.error("Erro ao salvar avaliação da série {}: {}", serieId, e.getMessage(), e);
            throw new MovieServiceException("Erro ao salvar avaliação da série: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "userRatedSeries", key = "#email")
    public List<Series> searchRatedSeries(String email) {
        try {
            List<Series> result = serieRepository.findAllByEmail(email);
            if (!result.isEmpty()) {
                return result;
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar séries avaliadas para o usuário {}: {}", email, e.getMessage(), e);
        }
        return null;
    }

    @CacheEvict(value = "userRatedSeries", key = "#existing.email")
    public Series updateRateSerie(String rating, String comment, Series existing) {
        try {
            existing.setMyVote(rating);
            existing.setComment(comment);
            serieRepository.save(existing);
            return existing;
        } catch (Exception e) {
            logger.error("Erro ao atualizar avaliação da série: {}", e.getMessage(), e);
        }
        return null;
    }
}
