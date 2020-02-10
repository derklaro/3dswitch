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
package de.switchprojects.controller.printer.queue;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.octoprint.OctoPrintHelper;
import de.switchprojects.controller.printer.progressed.ProgressedDatabaseHelper;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.ticker.SystemTicker;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.UserType;
import de.switchprojects.controller.printer.util.ThreadSupport;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Represents the current loaded queue of all objects which are going to get printed
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class PrintQueue extends Thread {

    public static final BlockingDeque<PrintableObject> QUEUE = new LinkedBlockingDeque<>();

    public static void queue(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot print null object");
        Validate.assertEquals(object.isSliced(), true);

        QUEUE.offerLast(object);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                PrintableObject next = QUEUE.takeFirst();

                if (OctoPrintHelper.isPrintJobRunning()) {
                    QUEUE.addFirst(next);
                    ThreadSupport.sleep(TimeUnit.SECONDS, 30);
                    continue;
                } else if (!OctoPrintHelper.isPrintJobRunning() && SystemTicker.runningJob != null) {
                    String fileName = SystemTicker.runningJob.getName();
                    ProgressedDatabaseHelper.getProgressedObjectAndRemove(fileName).ifPresent(object -> {
                        UserType userType = object.getUserType();
                        if (userType == null) {
                            return;
                        }

                        UserManagement target = GlobalAPI.getUserManagement(userType);
                        if (target == null) {
                            return;
                        }

                        target.notifyPrintFinished(object.getKey(), object.getUserID());
                    });

                    OctoPrintHelper.deleteFile(SystemTicker.runningJob.getName());
                    SystemTicker.runningJob = null;
                }

                if (!GlobalAPI.isReadyForNext()) {
                    QUEUE.addFirst(next);
                    ThreadSupport.sleep(TimeUnit.SECONDS, 10);
                    continue;
                }

                System.out.println("Next object polled from queue and ready to print: " + next.getKey());

                OctoPrintHelper.print(next);
                GlobalAPI.setIsReadyForNext(false);

                GlobalAPI.getDatabase().deleteFromTable(next.getTable(), next.getKey());
                ProgressedDatabaseHelper.handlePrintStart(next);

                System.out.println("Started print job successfully");
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
