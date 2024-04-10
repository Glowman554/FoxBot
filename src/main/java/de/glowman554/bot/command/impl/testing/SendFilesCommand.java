package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.*;
import de.glowman554.bot.command.impl.Reply;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;

public class SendFilesCommand extends SchemaCommand {

    public SendFilesCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        doTest("https://filesamples.com/samples/audio/mp3/sample3.mp3", MediaType.AUDIO, message);
        doTest("https://filesamples.com/samples/video/mp4/sample_960x540.mp4", MediaType.VIDEO, message);
        doTest("https://filesamples.com/samples/image/webp/sample1.webp", MediaType.IMAGE, message);
        doTest("https://filesamples.com/samples/document/csv/sample2.csv", MediaType.DOCUMENT, message);
    }

    public void doTest(String url, MediaType mediaType, Reply message) {
        try (StreamedFile file = HttpClient.download(url)) {
            message.replyFile(file, mediaType, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (StreamedFile file = HttpClient.download(url)) {
            message.replyFile(file, mediaType, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (StreamedFile file = HttpClient.download(url)) {
            message.replyFile(file, mediaType, false, "test123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (StreamedFile file = HttpClient.download(url)) {
            message.replyFile(file, mediaType, true, "test123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadSchema(Schema schema) {

    }

    @Override
    public void execute(CommandContext commandContext) throws Exception {
        doTest("https://filesamples.com/samples/audio/mp3/sample3.mp3", MediaType.AUDIO, commandContext);
        doTest("https://filesamples.com/samples/video/mp4/sample_960x540.mp4", MediaType.VIDEO, commandContext);
        doTest("https://filesamples.com/samples/image/webp/sample1.webp", MediaType.IMAGE, commandContext);
        doTest("https://filesamples.com/samples/document/csv/sample2.csv", MediaType.DOCUMENT, commandContext);
    }
}
