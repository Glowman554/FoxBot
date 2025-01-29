package de.glowman554.bot.utils.compiler.impl;

import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.Compiler;
import de.glowman554.bot.utils.compiler.Executor;

import java.io.IOException;

@Deprecated
public class FlCompiler implements Compiler {
    @Override
    public boolean isInterpreted() {
        return false;
    }

    @Override
    public TemporaryFile compileAndLink(TemporaryFile file) throws IOException {
        TemporaryFile output = new TemporaryFile("flbb");

        Executor.executeUnsafe(String.format("flc -t bytecode -o %s %s", output.getFile().getPath(), file.getFile().getPath()));

        return output;
    }

    @Override
    public String execute(TemporaryFile file) throws IOException {
        return Executor.execute("flvm " + file.getFile().getAbsolutePath());
    }
}
