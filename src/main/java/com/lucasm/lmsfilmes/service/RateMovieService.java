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
import com.lucasm.lmsfilmes.model.Movies;
import com.lucasm.lmsfilmes.repository.MovieRepository;

/**
 * Serviço para gerenciar avaliações de filmes e séries.
 */
@Service
public class RateMovieService {

    private static final Logger logger = LoggerFactory.getLogger(RateMovieService.class);

    @Autowired
    private MovieRepository movieRepository;
    
    @CacheEvict(value = "userRatedMovies", key = "#email")
    public Movies rateMovie(String movieId, String email, String rating, String title, String poster_path, String comment) {
        try {
            logger.info("Usuário {} avaliando filme {}(id:{}) com nota {} e comentário: {}", email, title, movieId, rating, comment != null ? "sim" : "não");

            Optional<Movies> optionalMovie = movieRepository.findByMovieIdAndEmail(movieId, email);
            if (optionalMovie.isPresent()) {
                Movies existing = optionalMovie.get();
                updateRateMovie(rating, comment, existing);
                logger.warn("Usuário {} já avaliou o filme {}(id:{}). Nota existente: {}", email, title, movieId, existing.getMyVote());
                return existing; // Retorna a nota existente
            }

            // Criar novo registro de avaliação
            Movies movie = new Movies();
            movie.setMovieId(movieId);
            movie.setEmail(email);
            movie.setMyVote(rating);
            movie.setComment(comment);
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

    @Cacheable(value = "userRatedMovies", key = "#email")
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
         
    @CacheEvict(value = "userRatedMovies", key = "#existing.email")
    public Movies updateRateMovie(String rating, String comment, Movies existing) {
        try {
            existing.setMyVote(rating);
            existing.setComment(comment);
            movieRepository.save(existing);
            return existing;
        } catch (Exception e) {
            logger.error("Erro ao atualizar avaliação do filme: {}", e.getMessage(), e);
        }
        return null;
    }
}