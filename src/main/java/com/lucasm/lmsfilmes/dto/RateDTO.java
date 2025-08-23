package com.lucasm.lmsfilmes.dto;

import java.util.List;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lucasm.lmsfilmes.model.Movies;
import com.lucasm.lmsfilmes.model.Series;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferir informações sobre avaliações de filmes e séries.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RateDTO {

    private String title;            // Título do filme
    private String name;             // Nome da série
    private String rating;           // Nota atribuída
    private String movieId;          // ID do filme
    private String serieId;          // ID da série
    private String email;         // Nickname do usuário
    private String poster_path;      // Caminho do pôster
    private Date created_at;         // Data de criação
    private Movies movieModel;   // Modelo de filme
    private List<Movies> movieList; // Lista de filmes
    private Series seriemodel;   // Modelo de série
    private List<Series> serieList; // Lista de séries

    // Método para converter RateDTO em MovieModel
    public Movies toModel() {
        Movies movieModel = new Movies();
        movieModel.setTitle(this.title);
        movieModel.setMovieId(this.movieId);
        movieModel.setMyVote(this.rating);
        movieModel.setEmail(this.email);
        movieModel.setPoster_path(this.poster_path);
        return movieModel;
    }
}
