package com.lucasm.lmsfilmes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucasm.lmsfilmes.dto.AuthDTO;
import com.lucasm.lmsfilmes.model.User;
import com.lucasm.lmsfilmes.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository usersRepo;
    
    @Autowired
    private JWTUtils jwtUtils;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Realiza o registro de um novo usuário.
     * Verifica se o email ou nickname já estão cadastrados antes de salvar.
     */
    public String register(AuthDTO registrationRequest) {

            // Verifica se o email já existe
            if (usersRepo.findByEmail(registrationRequest.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

            // Verifica se o nickname já existe
            if (usersRepo.findByNickname(registrationRequest.nickname()).isPresent()) {
                throw new IllegalArgumentException("Username já cadastrado.");
            }

            // Criação de um novo usuário
            User ourUser = new User();
            ourUser.setName(registrationRequest.name());
            ourUser.setEmail(registrationRequest.email());
            ourUser.setNickname(registrationRequest.nickname());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.password()));

            User savedUser = usersRepo.save(ourUser);
            var jwt = jwtUtils.generateToken(savedUser);
        return jwt;
    }

    /**
     * Realiza o login de um usuário, gerando tokens JWT e Refresh.
     */
    public String login(AuthDTO loginRequest) {
        try {
            // Autentica o usuário
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais inválidas.");
        }

        // Busca o usuário no banco
        var user = usersRepo.findByEmail(loginRequest.email())
                            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Gera os tokens de autenticação
        var jwt = jwtUtils.generateToken(user);

        return jwt;
    }

}
