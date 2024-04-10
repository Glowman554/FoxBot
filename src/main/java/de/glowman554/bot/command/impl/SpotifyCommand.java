package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.api.spotify.Song;
import de.glowman554.bot.utils.api.spotify.SpotifyApi;

import java.util.List;

public class SpotifyCommand extends SchemaCommand {
    private final SpotifyApi spotifyApi = new SpotifyApi(Main.config.getSpotify().getClientId(), Main.config.getSpotify().getClientSecret(), Main.config.getSpotify().getRedirectUrl());

    public SpotifyCommand() {
        super("Search spotify for songs.", "Usage: <command> [query]", null, Group.TOOLS);
    }


    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Invalid arguments");
        } else {
            doSend(message, String.join(" ", arguments));
        }
    }

    private void doSend(Reply reply, String query) throws Exception {
        List<Song> songs = spotifyApi.searchSpotifySongs(query, 5);
        for (Song song : songs) {
            if (song.preview() == null || song.preview().isEmpty() || song.preview().equals("null")) {
                continue;
            }
            try (StreamedFile file = song.stream()) {
                reply.replyFile(file, MediaType.AUDIO, false, "Maybe you like " + reply.formatBold(song.title()));
            }
        }
    }

    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "query", "Search query", false).register();
    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doSend(commandContext, commandContext.get("query").asString());
    }
}
