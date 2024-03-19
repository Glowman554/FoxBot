package de.glowman554.bot.utils.compiler.impl;

import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.Compiler;
import de.glowman554.bot.utils.compiler.Executor;

import java.io.IOException;

public class CCompiler implements Compiler {
    @Override
    public boolean isInterpreted() {
        return false;
    }

    @Override
    public TemporaryFile compileAndLink(TemporaryFile file) throws IOException {
        TemporaryFile output = new TemporaryFile("elf");

        Executor.executeUnsafe(String.format("gcc %s -o %s", file.getFile().getPath(), output.getFile().getPath()));

        return output;
    }

    @Override
    public String execute(TemporaryFile file) throws IOException {
        return Executor.execute(file.getFile().getAbsolutePath());
    }
}
