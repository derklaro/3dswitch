package de.switchprojects.controller.printer.octoprint.delete;

import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;
import org.octoprint.api.OctoPrintCommand;
import org.octoprint.api.OctoPrintInstance;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDeleteCommand extends OctoPrintCommand {

    public FileDeleteCommand(OctoPrintInstance requestor) {
        super(requestor, "files");
    }

    public void deleteFile(@NotNull String name) {
        Validate.assertNotNull(name, "Invalid file name provided");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.g_comm.getM_url() + "/api/files/local/" + name).openConnection();
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
            );
            connection.setRequestProperty("X-Api-Key", this.g_comm.getM_key());
            connection.setRequestMethod("DELETE");
            connection.setUseCaches(false);

            connection.connect();

            Validate.assertEquals(connection.getResponseCode(), 204);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
