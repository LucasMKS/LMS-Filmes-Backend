package com.lucasm.lmsfilmes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lucasm.lmsfilmes.model.User;

/**
 * Repositório para operações com a entidade UserModel.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Busca usuário pelo e-mail
    Optional<User> findByEmail(String email);

    // Busca usuário pelo nickname
    Optional<User> findByNickname(String nickname);

    // Lista todos os usuários
    List<User> findAll();
}
