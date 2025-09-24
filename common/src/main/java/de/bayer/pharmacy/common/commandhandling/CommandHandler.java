package de.bayer.pharmacy.common.commandhandling;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
    Class<C> commandType();
}