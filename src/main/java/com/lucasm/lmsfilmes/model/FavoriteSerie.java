package com.lucasm.lmsfilmes.model;

import lombok.Data;

/**
 * Modelo de dados para séries favoritas.
 */
@Data
public class FavoriteSerie {
    private String id;
    
    private String serieId;

    private String email;

    private boolean favorite;
    
}
