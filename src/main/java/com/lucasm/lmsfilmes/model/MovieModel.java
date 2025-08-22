package com.lucasm.lmsfilmes.model;

import java.util.Date;

import lombok.Data;

/**
 * Modelo de dados para filmes.
 */
@Data
public class MovieModel {

    private String id;

    private String title;

    private String movieId;

    private String myVote;

    private String nickname;

    private String poster_path;

    private Date created_at;

    protected void onCreate() {
        this.created_at = new Date();
    }
}
