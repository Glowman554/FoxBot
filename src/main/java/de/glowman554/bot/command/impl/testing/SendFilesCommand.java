package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.FileUtils;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.TemporaryFile;

public class SendFilesCommand extends Command {

    public SendFilesCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        doTest("https://filesamples.com/samples/audio/mp3/sample3.mp3", Message.Type.AUDIO, message);
        doTest("https://filesamples.com/samples/video/mp4/sample_960x540.mp4", Message.Type.VIDEO, message);
        doTest("https://filesamples.com/samples/image/jpg/sample_1920%C3%971280.jpg", Message.Type.IMAGE, message);
        doTest("https://filesamples.com/samples/document/csv/sample2.csv", Message.Type.DOCUMENT, message);
    }

    public void doTest(String url, Message.Type mediaType, Message message) {
        try (TemporaryFile testFile = new TemporaryFile(FileUtils.getFileExtension(url))) {
            HttpClient.download(testFile.getFile(), url);
            message.replyFile(testFile.getFile(), mediaType, true);
            message.replyFile(testFile.getFile(), mediaType, false);
            message.replyFile(testFile.getFile(), mediaType, false, "test123");
            message.replyFile(testFile.getFile(), mediaType, true, "test123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
