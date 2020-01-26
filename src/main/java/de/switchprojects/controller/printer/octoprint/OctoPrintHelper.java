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
package de.switchprojects.controller.printer.octoprint;

import de.switchprojects.controller.printer.octoprint.upload.FileUploadCommand;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.octoprint.api.FileCommand;
import org.octoprint.api.JobCommand;
import org.octoprint.api.OctoPrintInstance;
import org.octoprint.api.PrinterCommand;
import org.octoprint.api.model.OctoPrintJob;
import org.octoprint.api.model.PrinterState;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Another util class for the connection between the printer and the java implementation
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class OctoPrintHelper {

    private OctoPrintHelper() {
        throw new UnsupportedOperationException();
    }

    private static OctoPrintInstance octoPrintInstance;

    public static void connect(@NotNull String host, int port, @NotNull String apiKey) {
        Validate.assertNotNull(host, "Cannot connect to null host");
        Validate.assertNotNull(apiKey, "Cannot use null api key");
        Validate.assertBigger(port, 0);

        try {
            octoPrintInstance = new OctoPrintInstance(host, port, apiKey);
        } catch (final MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void print(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot print null-object");
        Validate.assertNotNull(octoPrintInstance, "Not connected to printer!");

        File file = new File(object.getPath());
        Validate.assertEquals(file.exists(), true);
        Validate.assertEquals(file.isDirectory(), false);

        FileUploadCommand fileUploadCommand = new FileUploadCommand(octoPrintInstance);
        fileUploadCommand.uploadFile(file);

        FileCommand fileCommand = new FileCommand(octoPrintInstance);
        Validate.assertEquals(fileCommand.printFile(file.getName()), true);
    }

    @Nullable
    public static OctoPrintJob getCurrentPrintJob() {
        Validate.assertNotNull(octoPrintInstance, "Not connected to printer!");
        return new JobCommand(octoPrintInstance).getJobDetails();
    }

    public static boolean isPrintJobRunning() {
        Validate.assertNotNull(octoPrintInstance, "Not connected to printer!");

        PrinterCommand printerCommand = new PrinterCommand(octoPrintInstance);
        return !isReadyToPrint(printerCommand.getCurrentState());
    }

    private static boolean isReadyToPrint(PrinterState state) {
        return state.isConnected() && state.isReady() && !state.isPrinting() && !state.isPaused();
    }
}
