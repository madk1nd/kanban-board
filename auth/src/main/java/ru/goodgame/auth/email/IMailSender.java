package ru.goodgame.auth.email;

import javax.annotation.Nonnull;

public interface IMailSender {
    void send(@Nonnull String address, @Nonnull String link);
}
