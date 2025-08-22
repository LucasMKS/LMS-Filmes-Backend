package com.lucasm.lmsfilmes.model;

import lombok.Data;

/**
 * Modelo de dados para séries favoritas.
 */

@Data
public class FavoriteSerieModel {
    private String id;
    
    private String serieId;

    private String name;
    
    private String nickname;

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }
}
