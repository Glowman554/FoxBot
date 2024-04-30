package de.glowman554.bot.utils.compiler;

import de.glowman554.bot.logging.Logger;

import java.io.*;

public class Executor {
    private static final String[] nsjail = new String[]{"-l /tmp/nsjail.log", "-Mo", "--user 0", "--group 99999",
            "--chroot /", "-T /boot/", "-T /dev/", "-T /mnt/", "-T /media/", "-t 600", "-T /proc/", "--keep_caps"};
    private static final String[] filter = new String[]{"$", "(", ")", "'", "\"", "|", "<", ">", "`", "\\"};
    private static final File nsjailExecutable = new File("./nsjail.elf");
    private static boolean secure = true;

    @Deprecated
    public static String execute(String command) throws IOException {
        if (secure) {
            // Check platform
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                throw new IOException("Windows is not supported for save execution mode!");
            } else {
                ensureNsjailInstalled();
                // check if command contains any of the filter characters
                for (String f : filter) {
                    if (command.contains(f)) {
                        throw new IOException("Command contains filter character: " + f);
                    }
                }

                String actualCommand = "echo '" + command + "' | " + new File(".").getAbsolutePath() + "/nsjail.elf "
                        + String.join(" ", nsjail) + " -- /bin/bash";

                try (FileWriter fileWriter = new FileWriter("/tmp/tmp.sh")) {
                    fileWriter.write(actualCommand);
                }

                return executeUnsafe("bash /tmp/tmp.sh");

            }
        } else {
            return executeUnsafe(command);
        }
    }

    private static void ensureNsjailInstalled() throws IOException {
        if (!nsjailExecutable.exists()) {
            Logger.log("Trying to download and install nsjail...");

            try (FileWriter fileWriter = new FileWriter("/tmp/nsjail.sh")) {
                fileWriter.write("""
                            (
                            git clone https://github.com/google/nsjail.git /tmp/nsjail
                            cd /tmp/nsjail
                            make
                            )
                            cp /tmp/nsjail/nsjail nsjail.elf
                            rm -rf /tmp/nsjail
                        """);
            }

            executeUnsafe("bash /tmp/nsjail.sh");

            if (!nsjailExecutable.exists()) {
                throw new RuntimeException("Could not install nsjail.");
            }

            Logger.log("Downloaded and installed nsjail!");
        }
    }

    public static String executeUnsafe(String command) throws IOException {
        Logger.log("Executing command '%s'", command);
        try {
            Process process = Runtime.getRuntime().exec(command);

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder outputBuilder = new StringBuilder();

            String output;
            while ((output = bufferedReader.readLine()) != null) {
                outputBuilder.append(output).append("\n");
                Logger.log("%s", output);
            }

            return outputBuilder.toString();
        } catch (Exception e) {
            throw new IOException("Error while executing command: " + command);
        }
    }

    public static void setSecure(boolean secure) {
        Executor.secure = secure;
    }
}
