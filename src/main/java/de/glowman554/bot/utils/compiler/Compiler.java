package de.glowman554.bot.utils.compiler;

import de.glowman554.bot.utils.TemporaryFile;

import java.io.IOException;

public interface Compiler {
    boolean isInterpreted();

    default TemporaryFile compileAndLink(TemporaryFile file) throws IOException {
        return null;
    }

    String execute(TemporaryFile file) throws IOException;
}
