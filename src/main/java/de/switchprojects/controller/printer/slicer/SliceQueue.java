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
package de.switchprojects.controller.printer.slicer;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.util.FileUtils;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The queue for objects which are not sliced yet
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class SliceQueue extends Thread {

    public static final BlockingDeque<PrintableObject> QUEUE = new LinkedBlockingDeque<>();

    public static void queue(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot slice null object");
        Validate.assertEquals(object.isSliced(), false);

        GlobalAPI.getDatabase().insert(object);
        QUEUE.offerLast(object);
    }

    public static void queueExisting(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot slice null object");
        Validate.assertEquals(object.isSliced(), false);

        QUEUE.offerLast(object);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                PrintableObject next = QUEUE.takeFirst();
                ensureSlicerExists();
                Slice3rSlicer.slice(next);
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void ensureSlicerExists() {
        File file = new File("slicer");
        if (file.exists()) {
            return;
        }

        System.out.println("Creating non-existing slicer...");
        FileUtils.createDirectories(file.toPath());

        try (InputStream stream = SliceQueue.class.getClassLoader().getResourceAsStream("internal-slicer.zip")) {
            Validate.assertNotNull(stream, "The internal-slicer.zip is not build in. Custom build?");
            FileUtils.unzip(stream, "slicer");
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Unzipped slicer completely");
    }
}
