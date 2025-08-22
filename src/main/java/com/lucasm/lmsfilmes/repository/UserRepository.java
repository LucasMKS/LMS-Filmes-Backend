package com.lucasm.lmsfilmes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lucasm.lmsfilmes.model.UserModel;

/**
 * Repositório para operações com a entidade UserModel.
 */
@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    // Busca usuário pelo e-mail
    Optional<UserModel> findByEmail(String email);

    // Busca usuário pelo nickname
    Optional<UserModel> findByNickname(String nickname);

    // Lista todos os usuários
    List<UserModel> findAll();
}
