package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.*;
import de.glowman554.bot.command.Schema.Argument.Type;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.api.TelegramSticker;

public class StickerCommand extends SchemaCommand {
    private TelegramSticker sticker = new TelegramSticker(Main.config.getTelegramToken());

    public StickerCommand() {
        super("Download a telegram sticker pack.", "Usage: <command> [name]", null, Group.TOOLS);
    }

    private void doSend(IReply reply, String name) {
        reply.reply("Downloading please wait...");

        String strip = "https://t.me/addstickers/";
        if (name.startsWith(strip)) {
            name = name.substring(strip.length());
        }

        for (String url : sticker.fetchPack(name)) {
            try (StreamedFile file = HttpClient.download(url)) {
                reply.replyFile(file, MediaType.IMAGE, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Type.STRING, "name", "Name of the sticker pack", false).register();
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        doSend(commandContext, commandContext.get("name").asString());
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length != 1) {
            commandContext.reply("Missing or invalid name argument");
        } else {
            doSend(commandContext, arguments[0]);
        }
    }

}
