package ru.goodgame.auth.repository;

import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.QValidation;

import javax.annotation.Nonnull;
import java.util.Optional;

@Service
public class ValidationRepository implements IValidationRepository {

    @Nonnull private static final QValidation VALIDATION = QValidation.validation;

    @Nonnull private final SQLQueryFactory factory;

    public ValidationRepository(@Nonnull final SQLQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public void persistToken(@Nonnull final String token,
                             @Nonnull final String username) {
        factory.insert(VALIDATION)
                .columns(VALIDATION.token, VALIDATION.username)
                .values(token, username)
                .execute();
    }

    @Nonnull
    @Override
    public Optional<String> getBy(@Nonnull final String token) {
        return Optional.ofNullable(
                factory.select(VALIDATION.username)
                        .from(VALIDATION)
                        .where(VALIDATION.token.eq(token))
                        .fetchOne()
        );
    }

    @Override
    public void deleteBy(@Nonnull final String token) {
        factory.delete(VALIDATION)
                .where(VALIDATION.token.eq(token))
                .execute();
    }

}
