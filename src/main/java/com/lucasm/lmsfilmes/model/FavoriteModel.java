package com.lucasm.lmsfilmes.model;

import lombok.Data;

/**
 * Modelo de dados para filmes favoritos.
 */

@Data
public class FavoriteModel {
    private String id;
    
    private String movieId;

    private String title;
    
    private String email;

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }
}
