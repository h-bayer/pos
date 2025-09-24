package de.bayer.pharmacy.common.commandhandling;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandBus {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    // Spring injects all CommandHandler beans found on the classpath
    public CommandBus(Map<String, CommandHandler<?, ?>> handlerBeans) {
        handlerBeans.values().forEach(h -> handlers.put(h.commandType(), h));
    }

    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> R dispatch(C command) {
        var handler = (CommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler for command: " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
