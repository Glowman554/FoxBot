package de.glowman554.bot.platform.web;

import de.glowman554.bot.command.Message;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Random;

@WebSocket
public class WebInstance {
    private String userId;
    private String displayName = "web";

    private String authenticationString;
    private boolean authenticated = false;

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        EventManager.register(this);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        authenticated = false;
        userId = null;
        EventManager.unregister(this);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            JsonNode root = Json.json().parse(message);

            switch (root.get("type").asString()) {
                case "authenticate":
                    userId = root.get("user").asString();
                    authenticationString = String.valueOf(new Random().nextInt(9999999));
                    JsonNode reply = JsonNode.object();
                    reply.set("type", "authenticate");
                    reply.set("string", authenticationString);
                    session.getRemote().sendString(Json.json().serialize(reply));
                    break;
                case "displayName":
                    displayName = root.get("name").asString();
                    break;
                case "message":
                    if (authenticated) {
                        new WebMessage(root.get("message").asString(), userId, displayName, session).call(Message.class);
                    } else {
                        new WebMessage(root.get("message").asString(), "web", displayName, session).call(Message.class);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.getRemote().sendString("Error");
        }
    }

    @EventTarget
    public void onMessage(Message message) {
        if (message.getUserId().equals(userId) && message.getMessage().equals(authenticationString)) {
            authenticated = true;
        }
    }
}
