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
import de.switchprojects.controller.printer.queue.PrintQueue;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.util.FileUtils;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Represents the slicer which slices the {@code stl} or {@code obj} files into the {@code gcode} format
 * using Slice3r
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class Slice3rSlicer {

    private Slice3rSlicer() {
        throw new UnsupportedOperationException();
    }

    public static void slice(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot slice null object");
        Validate.assertEquals(object.isSliced(), false);

        String objectPath = Paths.get(object.getPath()).toFile().getAbsolutePath();
        File file = new File("slicer/prusa-slicer-console.exe").getAbsoluteFile();

        String[] command = new String[]{
                file.getAbsolutePath(),
                "-g",
                "--load",
                "switch.ini",
                "-o",
                objectPath + ".gcode",
                objectPath
        };

        try {
            Process process = new ProcessBuilder()
                    .inheritIO()
                    .directory(new File("slicer").getAbsoluteFile())
                    .command(command)
                    .start();
            process.waitFor();

            FileUtils.deleteIfExists(object.getPath());

            object.setPath(object.getPath() + ".gcode");
            object.setSliced(true);
            GlobalAPI.getDatabase().update(object);

            PrintQueue.queue(object);
        } catch (final IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
