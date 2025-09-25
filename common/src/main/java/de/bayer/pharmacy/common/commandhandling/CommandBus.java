package de.bayer.pharmacy.common.commandhandling;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandBus {

    private final Map<Class<?>, ICommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    // Spring injects all CommandHandler beans found on the classpath
    public CommandBus(Map<String, ICommandHandler<?, ?>> handlerBeans) {
        handlerBeans.values().forEach(h -> handlers.put(h.commandType(), h));
    }

    @SuppressWarnings("unchecked")
    public <R, C extends ICommand<R>> R dispatch(C command) {
        var handler = (ICommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler for command: " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
