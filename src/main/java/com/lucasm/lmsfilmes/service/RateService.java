package com.lucasm.lmsfilmes.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.dto.RateDTO;
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
                updateRate(rating, existing);
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

    public Movies ratedContent(String email) {
        try {
            List<Movies> result = movieRepository.findAllByEmail(email);
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar filmes avaliados para o usuário {}: {}", email, e.getMessage(), e);
        }
        return null;
    }
         
    public Movies updateRate(String rating, Movies existing) {
        try {
            existing.setMyVote(rating);
            movieRepository.save(existing);
            return existing;
        } catch (Exception e) {
            logger.error("Erro ao atualizar avaliação do filme: {}", e.getMessage(), e);
        }
        return null;
    }

    // public RateDTO ratingSeries(RateDTO ratingDTO) {
    //     RateDTO rateDTO = new RateDTO();
    //     try {
    //         if (serieRepository.findBySerieIdAndNickname(ratingDTO.getSerieId(), ratingDTO.getNickname()).isPresent()) {
    //             rateDTO.setStatusCode(400);
    //             rateDTO.setError("Você já avaliou esta série.");
    //             return rateDTO;
    //         }

    //         Series serieModel = new Series();
    //         serieModel.setName(ratingDTO.getName());
    //         serieModel.setSerieId(ratingDTO.getSerieId());
    //         serieModel.setMyVote(ratingDTO.getRating());
    //         serieModel.setNickname(ratingDTO.getNickname());
    //         serieModel.setPoster_path(ratingDTO.getPoster_path());

    //         // Salvar no banco de dados
    //         serieRepository.save(serieModel);

    //         rateDTO.setStatusCode(200);
    //         rateDTO.setMensagem("Série avaliada com sucesso. Nota: " + ratingDTO.getRating());
    //         return rateDTO;
    //     } catch (Exception e) {
    //         rateDTO.setStatusCode(500);
    //         rateDTO.setError("Erro ao salvar avaliação: " + e.getMessage());
    //         return rateDTO;
    //     }
    // }
    
    // public RateDTO searchRatedSeries(String nickname) {
    //     RateDTO rateDTO = new RateDTO();
    //     try {
    //         List<Series> result = serieRepository.findAllByNickname(nickname);
    //         if (!result.isEmpty()) {
    //             rateDTO.setSerieList(result);
    //             rateDTO.setStatusCode(200);
    //             rateDTO.setMensagem("Séries avaliadas encontradas para o nickname: " + nickname);
    //         } else {
    //             rateDTO.setStatusCode(404);
    //             rateDTO.setError("Nenhuma série avaliada encontrada para o nickname: " + nickname);
    //         }
    //     } catch (Exception e) {
    //         rateDTO.setStatusCode(500);
    //         rateDTO.setError("Erro ao buscar séries avaliadas: " + e.getMessage());
    //     }
    //     return rateDTO;
    // }

    // public RateDTO updateRatingSeries(RateDTO ratingDTO) {
    //     RateDTO rateDTO = new RateDTO();
    //     try {
    //         Optional<Series> serieOptional = serieRepository.findBySerieIdAndNickname(ratingDTO.getSerieId(), ratingDTO.getNickname());
    //         if (serieOptional.isPresent()) {
    //             Series serieModel = serieOptional.get();
    //             serieModel.setMyVote(ratingDTO.getRating());
    //             serieRepository.save(serieModel);

    //             rateDTO.setStatusCode(200);
    //             rateDTO.setMensagem("Série atualizada com sucesso. Nova nota: " + ratingDTO.getRating());
    //         } else {
    //             rateDTO.setStatusCode(404);
    //             rateDTO.setError("Série não encontrada para atualização.");
    //         }
    //     } catch (Exception e) {
    //         rateDTO.setStatusCode(500);
    //         rateDTO.setError("Erro ao atualizar avaliação: " + e.getMessage());
    //     }
    //     return rateDTO;
    // }
}