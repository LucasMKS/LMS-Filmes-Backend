package com.lucasm.lmsfilmes.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.exceptions.MovieServiceException;
import com.lucasm.lmsfilmes.model.Movies;
import com.lucasm.lmsfilmes.model.Series;
import com.lucasm.lmsfilmes.repository.MovieRepository;
import com.lucasm.lmsfilmes.repository.SerieRepository;

/**
 * Serviço para gerenciar avaliações de filmes e séries.
 */
@Service
public class RateService {

    private static final Logger logger = LoggerFactory.getLogger(RateService.class);

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SerieRepository serieRepository;
    
    public Movies rateMovie(String movieId, String email, String rating, String title, String poster_path) {
        try {
            logger.info("Usuário {} avaliando filme {}(id:{}) com nota {}", email, title, movieId, rating);

            Optional<Movies> optionalMovie = movieRepository.findByMovieIdAndEmail(movieId, email);
            if (optionalMovie.isPresent()) {
                Movies existing = optionalMovie.get();
                updateRateMovie(rating, existing);
                logger.warn("Usuário {} já avaliou o filme {}(id:{}). Nota existente: {}", email, title, movieId, existing.getMyVote());
                return existing; // Retorna a nota existente
            }

            // Criar novo registro de avaliação
            Movies movie = new Movies();
            movie.setMovieId(movieId);
            movie.setEmail(email);
            movie.setMyVote(rating);
            movie.setTitle(title);
            movie.setPoster_path(poster_path);
            movie.onCreate();
            movieRepository.save(movie);

            logger.info("Filme {} avaliado com sucesso pelo usuário {}. Nota: {}", movieId, email, rating);
            return movie;

        } catch (Exception e) {
            logger.error("Erro ao salvar avaliação do filme {}: {}", movieId, e.getMessage(), e);
            throw new MovieServiceException("Erro ao salvar avaliação do filme: " + e.getMessage(), e);
        }
    }

    public List<Movies> searchRatedMovies(String email) {
        try {
            List<Movies> result = movieRepository.findAllByEmail(email);
            if (!result.isEmpty()) {
                return result;
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar filmes avaliados para o usuário {}: {}", email, e.getMessage(), e);
        }
        return null;
    }
         
    public Movies updateRateMovie(String rating, Movies existing) {
        try {
            existing.setMyVote(rating);
            movieRepository.save(existing);
            return existing;
        } catch (Exception e) {
            logger.error("Erro ao atualizar avaliação do filme: {}", e.getMessage(), e);
        }
        return null;
    }

    public Series rateSerie(String serieId, String email, String rating, String title, String poster_path) {
        logger.info("Usuário {} avaliando serie {}(id:{}) com nota {}", email, title, serieId, rating);

        try {
            Optional<Series> optionalSerie = serieRepository.findBySerieIdAndEmail(serieId, email);
            if (optionalSerie.isPresent()) {
                Series existing = optionalSerie.get();
                updateRateSerie(rating, existing);
                logger.warn("Usuário {} já avaliou a série {}(id:{}). Nota atualizada: {}", email, title, serieId, existing.getMyVote());
                return existing;
            }

            Series serieModel = new Series();
            serieModel.setName(title);
            serieModel.setSerieId(serieId);
            serieModel.setMyVote(rating);
            serieModel.setEmail(email);
            serieModel.setPoster_path(poster_path);
            serieRepository.save(serieModel);
            return serieModel;
        } catch (Exception e) {
            logger.error("Erro ao avaliar série: {}", e.getMessage(), e);
        }
        return null;
    }

    public Series searchRatedSeries(String email) {
        try {
            List<Series> result = serieRepository.findAllByEmail(email);
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar séries avaliadas para o usuário {}: {}", email, e.getMessage(), e);
        }
        return null;
    }

     public Series updateRateSerie(String rating, Series existing) {
        try {
            existing.setMyVote(rating);
            serieRepository.save(existing);
            return existing;
        } catch (Exception e) {
            logger.error("Erro ao atualizar avaliação da série: {}", e.getMessage(), e);
        }
        return null;
    }
}