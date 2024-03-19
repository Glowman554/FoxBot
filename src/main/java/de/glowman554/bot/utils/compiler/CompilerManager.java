package de.glowman554.bot.utils.compiler;

import de.glowman554.bot.registry.Registry;
import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.TemporaryFile;
import de.glowman554.bot.utils.compiler.impl.CCompiler;
import de.glowman554.bot.utils.compiler.impl.CppCompiler;
import de.glowman554.bot.utils.compiler.impl.FlCompiler;
import de.glowman554.bot.utils.compiler.impl.ShInterpreter;

public class CompilerManager {
    public static Registry<String, Compiler> BY_FILE_EXTENSION = new Registry<>((string, compiler) -> {
    });

    static {
        register("sh", new ShInterpreter());
        register("c", new CCompiler());
        register("cpp", new CppCompiler());
        register("fl", new FlCompiler());
    }

    private static void register(String fileExtension, Compiler compiler) {
        BY_FILE_EXTENSION.register(fileExtension, compiler);
    }

    public static String run(TemporaryFile file) throws Exception {
        Compiler compiler = BY_FILE_EXTENSION.get(FileUtils.getFileExtension(file.getFile().getName()));

        if (compiler.isInterpreted()) {
            return compiler.execute(file);
        } else {
            try (TemporaryFile output = compiler.compileAndLink(file)) {
                return compiler.execute(output);
            }
        }
    }
}
