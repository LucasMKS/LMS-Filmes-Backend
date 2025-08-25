package com.lucasm.lmsfilmes.model;

import lombok.Data;

/**
 * Modelo de dados para filmes favoritos.
 */
@Data
public class FavoriteMovie {
    private String id;
    
    private String movieId;
    
    private String email;

    private boolean favorite;

}
