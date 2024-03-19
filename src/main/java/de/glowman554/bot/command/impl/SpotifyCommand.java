package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.api.spotify.Song;
import de.glowman554.bot.utils.api.spotify.SpotifyApi;

import java.util.List;

public class SpotifyCommand extends Command {
    private final SpotifyApi spotifyApi = new SpotifyApi(Main.config.getSpotify().getClientId(), Main.config.getSpotify().getClientSecret(), Main.config.getSpotify().getRedirectUrl());

    public SpotifyCommand() {
        super("Search spotify for songs.", "Usage: <command> [query]", null, Group.TOOLS);
    }


    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        if (arguments.length == 0) {
            message.reply("Invalid arguments");
        } else {
            List<Song> songs = spotifyApi.searchSpotifySongs(String.join(" ", arguments), 5);
            for (Song song : songs) {
                if (song.preview() == null || song.preview().isEmpty() || song.preview().equals("null")) {
                    continue;
                }
                try (TemporaryFile temporaryFile = new TemporaryFile("mp3")) {
                    HttpClient.download(temporaryFile.getFile(), song.preview());
                    message.replyFile(temporaryFile.getFile(), Message.Type.AUDIO, false, "Maybe you like " + message.formatBold(song.title()));
                }
            }
        }
    }
}
