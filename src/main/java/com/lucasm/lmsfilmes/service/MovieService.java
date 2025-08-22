package com.lucasm.lmsfilmes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasm.lmsfilmes.dto.TmdbDTO;
import com.lucasm.lmsfilmes.exceptions.ResourceNotFoundException;
import com.lucasm.lmsfilmes.model.FavoriteModel;
import com.lucasm.lmsfilmes.repository.FavoriteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.net.URLEncoder;

/**
 * Serviço para gerenciar filmes e favoritos.
 */
@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final FavoriteRepository favoriteRepository;
    private final String tmdbApiUrl;
    private final String apiKey;

    public MovieService(ObjectMapper objectMapper, FavoriteRepository favoriteRepository, @Value("${tmdb.api.url}") String tmdbApiUrl, @Value("${tmdb.api.key}") String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.favoriteRepository = favoriteRepository;
        this.tmdbApiUrl = tmdbApiUrl;
        this.apiKey = apiKey;
    }

    public List<TmdbDTO> searchMovies(String query, int page) {
        logger.info("Buscando filmes para a query '{}' na página {}", query, page);
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/search/movie?query=" + encodedQuery + "&include_adult=false&language=pt-BR&page=" + page))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Status code da busca: {}", response.statusCode());

            if (response.statusCode() == 200) {
                MovieSearchResponse searchResponse = objectMapper.readValue(response.body(), MovieSearchResponse.class);
                logger.info("Encontrados {} filmes para a query '{}'", searchResponse.results().size(), query);
                return searchResponse.results();
            } else {
                logger.warn("Nenhum filme encontrado para a query '{}', status code {}", query, response.statusCode());
                throw new ResourceNotFoundException("Nenhum filme encontrado para a busca: " + query);
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar filmes: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar filmes: " + e.getMessage(), e);
        }
    }

    public TmdbDTO getMoviesDetails(String movieId) {
        logger.info("Buscando detalhes do filme com ID {}", movieId);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/movie/" + movieId + "?language=pt-BR"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Status code detalhes: {}", response.statusCode());

            if (response.statusCode() == 200) {
                logger.info("Detalhes do filme {} obtidos com sucesso", movieId);
                return objectMapper.readValue(response.body(), TmdbDTO.class);
            } else if (response.statusCode() == 404) {
                logger.warn("Filme com ID {} não encontrado", movieId);
                throw new ResourceNotFoundException("Filme não encontrado: " + movieId);
            } else {
                logger.error("Erro ao buscar detalhes do filme {}: status {}", movieId, response.statusCode());
                throw new RuntimeException("Erro ao buscar detalhes do filme: status " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar detalhes do filme {}: {}", movieId, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar detalhes do filme: " + e.getMessage(), e);
        }
    }

    public List<TmdbDTO> moviePopular(int page) {
        logger.info("Buscando filmes populares na página {}", page);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/movie/popular?language=pt-BR&page=" + page))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Status code filmes populares: {}", response.statusCode());

            if (response.statusCode() == 200) {
                MovieSearchResponse searchResponse = objectMapper.readValue(response.body(), MovieSearchResponse.class);
                logger.info("Encontrados {} filmes populares", searchResponse.results().size());
                return searchResponse.results();
            } else {
                logger.error("Erro ao buscar filmes populares: status {}", response.statusCode());
                throw new RuntimeException("Erro ao buscar filmes populares: status " + response.statusCode());
            }

        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar filmes populares: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar filmes populares: " + e.getMessage(), e);
        }
    }

    // public FavoriteDTO getAllFavorites(String nickname) {
    //     try {
    //         List<FavoriteModel> result = favoriteRepository.findAllByNickname(nickname);
    //         List<FavoriteModel> favoriteMovies = result.stream()
    //                 .filter(FavoriteModel::isFavorite)
    //                 .collect(Collectors.toList());
    
    //         if (!favoriteMovies.isEmpty()) {
    //             FavoriteDTO favoriteDTO = new FavoriteDTO();
    //             favoriteDTO.setFavoriteList(favoriteMovies);
    //             favoriteDTO.setStatusCode(200);
    //             favoriteDTO.setMensagem("Filmes favoritados encontrados");
    //             return favoriteDTO;
    //         } else {
    //             return new FavoriteDTO(404, "Nenhum filme favoritado encontrado");
    //         }
    //     } catch (Exception e) {
    //         return new FavoriteDTO(500, "Erro ao buscar filmes favoritados: " + e.getMessage());
    //     }
    // }

    // public void toggleFavorite(FavoriteDTO favorite) {
    //     Optional<FavoriteModel> optionalFavorite = favoriteRepository.findByMovieIdAndNickname(favorite.getMovieId(), favorite.getNickname());
    //     FavoriteModel favoriteMovie = optionalFavorite.orElseGet(() -> new FavoriteModel());
    //     favoriteMovie.setMovieId(favorite.getMovieId());
    //     favoriteMovie.setNickname(favorite.getNickname());
    //     favoriteMovie.setFavorite(favorite.isFavorite());
    //     favoriteMovie.setTitle(favorite.getTitle());
    //     favoriteRepository.save(favoriteMovie);

    // }

    public boolean isFavorite(String movieId, String email) {
        logger.info("Verificando se o filme {} é favorito do usuário {}", movieId, email);

        Optional<FavoriteModel> optionalFavorite = favoriteRepository.findByMovieIdAndEmail(movieId, email);
        boolean result = optionalFavorite.map(FavoriteModel::isFavorite).orElse(false);

        logger.debug("Resultado do favorite: {}", result);
        return result;
    }

    private static record MovieSearchResponse(List<TmdbDTO> results) {}
}
