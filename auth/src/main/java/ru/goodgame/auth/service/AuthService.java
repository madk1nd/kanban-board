package ru.goodgame.auth.service;

import org.springframework.stereotype.Service;
import ru.goodgame.auth.repository.IAuthRepository;

import javax.annotation.Nonnull;

@Service
public class AuthService implements IAuthService {

    @Nonnull private final IAuthRepository repository;

    public AuthService(@Nonnull IAuthRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getUser() {
        return repository.getUser();
    }
}
