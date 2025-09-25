package de.bayer.pharmacy.common.commandhandling;

public interface ICommandHandler<C extends ICommand<R>, R> {
    R handle(C command);
    Class<C> commandType();
}