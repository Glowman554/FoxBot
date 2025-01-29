package de.glowman554.bot.platform.web;

import de.glowman554.bot.Main;
import de.glowman554.bot.Platform;
import de.glowman554.bot.command.*;
import de.glowman554.bot.event.EventManager;
import de.glowman554.bot.event.EventTarget;
import de.glowman554.bot.logging.Logger;
import de.glowman554.bot.registry.Registries;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;

@WebSocket
public class WebManager {
    private final HashMap<Session, WebInstance> connections = new HashMap<>();
    private final JsonNode schemas;

    public WebManager(JsonNode schemas) {
        EventManager.register(this);
        this.schemas = schemas;
    }

    @EventTarget
    public void onMessage(LegacyCommandContext commandContext) {
        for (Session session : connections.keySet()) {
            WebInstance instance = connections.get(session);
            if (commandContext.getUserId().equals(instance.getUserId())
                    && commandContext.getMessage().equals(instance.getAuthenticationString())) {
                Logger.log("User %s authenticated", commandContext.getUserId());
                instance.setAuthenticated(true);
            }
        }
    }

    public void onClose(WsCloseContext wsCloseContext) {
        connections.remove(wsCloseContext.session);
    }

    public void onConnect(WsConnectContext wsConnectContext) {
        connections.put(wsConnectContext.session, new WebInstance());

        JsonNode root = JsonNode.object();
        root.set("type", "info");
        root.set("prefix", Main.config.getPrefix());
        root.set("schemas", schemas);

        wsConnectContext.send(Json.json().serialize(root));
    }

    public void onMessage(WsMessageContext wsMessageContext) {
        Platform.call(wsMessageContext);
        WebInstance instance = connections.get(wsMessageContext.session);
        try {
            if (wsMessageContext.message().equals("ping")) {
                wsMessageContext.send("pong");
                return;
            }

            JsonNode root = Json.json().parse(wsMessageContext.message());

            switch (root.get("type").asString()) {
                case "authenticate":
                    instance.setUserId(root.get("user").asString());
                    JsonNode reply = JsonNode.object();
                    reply.set("type", "authenticate");
                    reply.set("string", instance.getAuthenticationString());
                    wsMessageContext.send(Json.json().serialize(reply));
                    break;
                case "message":
                    ArrayList<Attachment> attachments = new ArrayList<>();
                    for (JsonNode attachmentNode : root.get("files")) {
                        attachments.add(new WebAttachment(attachmentNode.get("name").asString(),
                                attachmentNode.get("file").asString()));
                    }
                    if (instance.isAuthenticated()) {
                        new WebLegacyCommandContext(root.get("message").asString(), instance.getUserId(), "Web",
                                wsMessageContext, attachments).call(LegacyCommandContext.class);
                    } else {
                        new WebLegacyCommandContext(root.get("message").asString(), "web", "Web", wsMessageContext,
                                attachments).call(LegacyCommandContext.class);
                    }
                    break;
                case "schemaMessage":
                    LegacyCommand command = Registries.COMMANDS.get(root.get("schemaCommandName").asString());
                    if (command instanceof SchemaCommand schemaCommand) {
                        SchemaCommandContext context = new WebSchemaCommandContext(
                                instance.isAuthenticated() ? instance.getUserId() : "web", "Web", wsMessageContext,
                                root);
                        schemaCommand.loadSchema(context);
                        Main.commandManager.execute(root.get("schemaCommandName").asString(), schemaCommand, context);
                    }
                    break;
            }

        } catch (Exception e) {
            Logger.exception(e);
            wsMessageContext.send("Something went wrong.");
        }
    }

    public void bind(WsConfig wsConfig) {
        wsConfig.onClose(this::onClose);
        wsConfig.onConnect(this::onConnect);
        wsConfig.onMessage(this::onMessage);
    }
}
