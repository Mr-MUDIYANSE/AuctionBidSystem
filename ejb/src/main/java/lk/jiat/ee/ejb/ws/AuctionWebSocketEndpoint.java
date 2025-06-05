package lk.jiat.ee.ejb.ws;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/auction-ws")
public class AuctionWebSocketEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket closed: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Optional: Echo message or handle custom messages from client
        System.out.println("Received message: " + message);
    }

    // Send message to all connected clients
    public static void broadcast(String message) {
        synchronized (sessions) {
            Set<Session> closedSessions = new HashSet<>();

            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                        System.out.println("ws message: "+message);
                    } catch (IOException | IllegalStateException e) {
                        System.err.println("Error sending message to session " + session.getId() + ": " + e.getMessage());
                        closedSessions.add(session);
                    }
                } else {
                    closedSessions.add(session);
                }
            }
            sessions.removeAll(closedSessions);
        }
    }

}
