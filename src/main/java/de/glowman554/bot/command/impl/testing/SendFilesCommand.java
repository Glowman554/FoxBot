package de.glowman554.bot.command.impl.testing;

import de.glowman554.bot.command.Command;
import de.glowman554.bot.command.Constants;
import de.glowman554.bot.command.Message;
import de.glowman554.bot.utils.HttpClient;
import de.glowman554.bot.utils.StreamedFile;

public class SendFilesCommand extends Command {

    public SendFilesCommand() {
        super(Constants.TESTING, Constants.TESTING, "testing", Group.TESTING);
    }

    @Override
    public void execute(Message message, String[] arguments) throws Exception {
        doTest("https://filesamples.com/samples/audio/mp3/sample3.mp3", Message.Type.AUDIO, message);
        doTest("https://filesamples.com/samples/video/mp4/sample_960x540.mp4", Message.Type.VIDEO, message);
        doTest("https://filesamples.com/samples/image/webp/sample1.webp", Message.Type.IMAGE, message);
        doTest("https://filesamples.com/samples/document/csv/sample2.csv", Message.Type.DOCUMENT, message);
    }

    public void doTest(String url, Message.Type mediaType, Message message) {
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
}
