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
package de.switchprojects.controller.printer.ticker;

import de.switchprojects.controller.printer.octoprint.OctoPrintHelper;
import de.switchprojects.controller.printer.util.ThreadSupport;
import org.octoprint.api.model.OctoPrintJob;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * The class ticks every minute and prints the current running print job for information parts into the
 * console
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class SystemTicker {

    public static OctoPrintJob runningJob;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss");

    public static void start() {
        while (!Thread.interrupted()) {
            ThreadSupport.sleep(TimeUnit.SECONDS, 30);

            OctoPrintJob currentJob = OctoPrintHelper.getCurrentPrintJob();
            if (currentJob == null) {
                System.out.println("Currently there is no running print job");
                continue;
            }

            if (runningJob == null) {
                runningJob = currentJob;
            }

            OctoPrintJob.JobProgress progress = currentJob.getJobProgress();
            if (progress == null) {
                System.out.println("No job is currently running");
                continue;
            }

            System.out.println("The print job " + currentJob.getName() + " is running!");
            System.out.println("Progress: " + DECIMAL_FORMAT.format(currentJob.getJobProgress().percentComplete()) + "%; " + format(progress));
        }
    }

    private static String format(OctoPrintJob.JobProgress progress) {
        return String.format("Time progressed: %s, Time remaining: %s", DATE_FORMAT.format(TimeUnit.SECONDS.toMillis(progress.elapsedTime())),
                progress.timeRemaining() != null ? DATE_FORMAT.format(TimeUnit.SECONDS.toMillis(progress.timeRemaining())) : "unbekannt");
    }
}
