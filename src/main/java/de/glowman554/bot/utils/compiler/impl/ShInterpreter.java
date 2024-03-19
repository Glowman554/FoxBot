package de.glowman554.bot.utils.compiler.impl;

import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.Compiler;
import de.glowman554.bot.utils.compiler.Executor;

import java.io.IOException;

public class ShInterpreter implements Compiler {
    @Override
    public boolean isInterpreted() {
        return true;
    }

    @Override
    public String execute(TemporaryFile file) throws IOException {
        return Executor.execute("bash " + file.getFile().getAbsolutePath());
    }
}
