package de.glowman554.bot.database;

public record Migration(String id, Apply apply) {
    public interface Apply {
        void execute(IDatabaseInterface db);
    }
}

