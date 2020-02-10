/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 Pasqual Koschmieder and Maximilian Kuck.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.switchprojects.controller.printer.octoprint.delete;

import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;
import org.octoprint.api.OctoPrintCommand;
import org.octoprint.api.OctoPrintInstance;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Represents an command for octoprint which deletes a file from the local octoprint storage
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
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
