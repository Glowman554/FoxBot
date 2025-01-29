package de.glowman554.bot.command.impl;

import de.glowman554.bot.Main;
import de.glowman554.bot.command.*;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;
import de.glowman554.bot.utils.api.openai.ImageGenerator;

public class ImageCommand extends SchemaCommand {
    private final ImageGenerator generator = new ImageGenerator(Main.config.getOpenAI().getToken());

    public ImageCommand() {
        super("Generate an image based on a prompt", "Usage: <command> [prompt]", null, Group.FUN);
    }


    @Override
    public void loadSchema(Schema schema) {
        schema.addArgument(Schema.Argument.Type.STRING, "prompt", "Prompt for the image", false).register();
    }

    private void common(IContext context, String prompt) {
        context.reply("Generating image please wait...");
        String url = generator.requestImage(prompt);

        try (StreamedFile file = HttpClient.download(url)) {
            file.setName("image.png");
            context.replyFile(file, MediaType.IMAGE, false);
        } catch (Exception e) {
            context.reply("Failed to generate image: " + e.getMessage());
        }
    }

    @Override
    public void execute(SchemaCommandContext commandContext) throws Exception {
        common(commandContext, commandContext.get("prompt").asString());
    }

    @Override
    public void execute(LegacyCommandContext commandContext, String[] arguments) throws Exception {
        if (arguments.length < 1) {
            commandContext.reply("Expected at least 1 argument!");
        } else {
            common(commandContext, String.join(" ", arguments));
        }
    }
}
