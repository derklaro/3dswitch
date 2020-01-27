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

import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The queue for objects which are not sliced yet
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class SliceQueue extends Thread {

    private static final BlockingDeque<PrintableObject> QUEUE = new LinkedBlockingDeque<>();

    public static void queue(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot slice null object");
        Validate.assertEquals(object.isSliced(), false);

        QUEUE.offerLast(object);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                PrintableObject next = QUEUE.takeFirst();
                Slice3rSlicer.slice(next);
            } catch (final InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
