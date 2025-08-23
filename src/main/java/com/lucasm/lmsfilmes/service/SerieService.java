package com.lucasm.lmsfilmes.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasm.lmsfilmes.dto.SeriesDTO;
import com.lucasm.lmsfilmes.exceptions.ResourceNotFoundException;

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
import java.net.URLEncoder;

/**
 * Serviço para buscar e manipular séries, incluindo favoritos.
 */
@Service
public class SerieService {

    private static final Logger logger = LoggerFactory.getLogger(SerieService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${tmdb.api.url}")
    private String tmdbApiUrl;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public SerieService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public List<SeriesDTO> searchSeries(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/search/tv?query=" + encodedQuery + "&include_adult=false&language=pt-BR"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                SerieSearchResponse searchResponse = objectMapper.readValue(response.body(), SerieSearchResponse.class);
                logger.info("Encontrados {} séries para a query '{}'", searchResponse.results().size(), query);
                return searchResponse.results();
            } else {
                logger.warn("Nenhuma série encontrada para a query '{}', status code {}", query, response.statusCode());
                throw new ResourceNotFoundException("Nenhuma série encontrada para a busca: " + query);
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar séries: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar séries: " + e.getMessage(), e);
        }
    }

    public SeriesDTO getSeriesDetails(String serieId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/tv/" + serieId + "?language=pt-BR"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                logger.info("Detalhes da série {} obtidos com sucesso", serieId);
                return objectMapper.readValue(response.body(), SeriesDTO.class);
            } else if (response.statusCode() == 404) {
                logger.warn("Série com ID {} não encontrada", serieId);
                throw new ResourceNotFoundException("Série não encontrada: " + serieId);
            } else {
                logger.error("Erro ao buscar detalhes da série {}: status {}", serieId, response.statusCode());
                throw new RuntimeException("Erro ao buscar detalhes da série: status " + response.statusCode());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar detalhes da série {}: {}", serieId, e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar detalhes da série: " + e.getMessage(), e);
        }
    }

    public List<SeriesDTO> seriePopular(int page) {
        logger.info("Buscando séries populares na página {}", page);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(tmdbApiUrl + "/trending/tv/week?language=pt-BR&page=" + page))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("Status code séries populares: {}", response.statusCode());

            if (response.statusCode() == 200) {
                SerieSearchResponse searchResponse = objectMapper.readValue(response.body(), SerieSearchResponse.class);
                logger.info("Encontrados {} séries populares", searchResponse.results().size());
                return searchResponse.results();
            } else {
                logger.error("Erro ao buscar séries populares: {}", response.body());
                throw new RuntimeException("Erro ao buscar séries populares: " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            logger.error("Erro ao buscar séries populares: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar séries populares: " + e.getMessage(), e);
        }
    }

    private static record SerieSearchResponse(List<SeriesDTO> results) {}
}
