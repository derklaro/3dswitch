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
package de.switchprojects.controller.printer.progressed;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.database.object.DatabaseObjectToken;
import de.switchprojects.controller.printer.progressed.object.ProgressedObject;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.util.Optional;

/**
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class ProgressedDatabaseHelper {

    private ProgressedDatabaseHelper() {
        throw new UnsupportedOperationException();
    }

    static {
        GlobalAPI.getDatabase().createTable(DB_NAME = "progressed");
    }

    public static final String DB_NAME;

    public static void handlePrintStart(@NotNull PrintableObject object) {
        Validate.assertNotNull(object, "Cannot handle null print object");

        ProgressedObject progressedObject = new ProgressedObject(object);
        GlobalAPI.getDatabase().insert(progressedObject);
    }

    @NotNull
    public static Optional<ProgressedObject> getProgressedObjectAndRemove(@NotNull String fileName) {
        Validate.assertNotNull(fileName, "Cannot use null as file name");

        ProgressedObject object = GlobalAPI.getDatabase().get(new DatabaseObjectToken<ProgressedObject>() {
            @NotNull
            @Override
            public ProgressedObject deserialize(@NotNull ObjectInputStream stream) {
                return ProgressedObject.MAPPER.apply(stream);
            }

            @Override
            public @NotNull String getTable() {
                return ProgressedDatabaseHelper.DB_NAME;
            }

            @Override
            public @NotNull String getKey() {
                return fileName;
            }
        });

        if (object != null) {
            GlobalAPI.getDatabase().deleteFromTable(DB_NAME, object.getKey());
        }

        return Optional.ofNullable(object);
    }
}
