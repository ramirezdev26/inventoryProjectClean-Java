package co.diegofer.inventoryclean.controller;

import co.diegofer.inventoryclean.model.generic.DomainEvent;
import co.diegofer.inventoryclean.serializer.JSONMapper;
import co.diegofer.inventoryclean.serializer.JSONMapperImpl;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ServerEndpoint("/inventory/{correlationId}")
public class ProductSocketController {

    private static final Logger logger = Logger.getLogger(ProductSocketController.class.getName());
    private static Map<String, Map<String, Session>> sessions;
    private final JSONMapper eventSerializer = new JSONMapperImpl();


    public ProductSocketController() {
        if (Objects.isNull(sessions)) {
            sessions = new ConcurrentHashMap<>();
        }
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("correlationId") String correlationId) {
        logger.info("Connected from: " + correlationId);
        var map = sessions.getOrDefault(correlationId, new HashMap<>());
        map.put(session.getId(), session);
        sessions.put(correlationId, map);
    }

    @OnClose
    public void onClose(Session session, @PathParam("correlationId") String correlationId) {
        sessions.get(correlationId).remove(session.getId());
        logger.info("Disconnected by: " + correlationId);
    }

    @OnError
    public void onError(Session session, @PathParam("correlationId") String correlationId, Throwable throwable) {
        sessions.get(correlationId).remove(session.getId());
        logger.log(Level.SEVERE, throwable.getMessage());
    }


    public void sendBranchCreated(String correlationId, DomainEvent event) {
        String message = eventSerializer.writeToJson(event);
        if (Objects.nonNull(correlationId) && sessions.containsKey(correlationId)) {
            logger.info("Sent from: " + correlationId);
            System.out.println(message);
            sessions
                    .get(correlationId)
                    .values()
                    .forEach(session -> {
                        try {
                            session.getAsyncRemote().sendText(message);
                        } catch (RuntimeException e) {
                            logger.log(Level.SEVERE, e.getMessage(), e);
                        }
                    });
        }
    }

    public void sendProductEventToSocket(String correlationId, DomainEvent model) {
        String message = eventSerializer.writeToJson(model);
        if (Objects.nonNull(correlationId) && sessions.containsKey(correlationId)) {
            logger.info("Sent from: " + correlationId);
            sessions
                    .get(correlationId)
                    .values()
                    .forEach(session -> {
                        try {
                            session.getAsyncRemote().sendText(message);
                        } catch (RuntimeException e) {
                            logger.log(Level.SEVERE, e.getMessage(), e);
                        }
                    });
        }
    }
}