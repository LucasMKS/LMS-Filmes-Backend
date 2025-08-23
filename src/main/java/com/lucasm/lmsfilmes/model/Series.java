package com.lucasm.lmsfilmes.model;

import java.util.Date;

import lombok.Data;

/**
 * Modelo de dados para séries.
 */
@Data
public class Series {

    private String id;

    private String name;

    private String serieId;

    private String myVote;

    private String email;

    private String poster_path;

    private Date created_at;

    protected void onCreate() {
        this.created_at = new Date();
    }
}
