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
package de.switchprojects.controller.printer.queue.object;

import de.switchprojects.controller.printer.database.object.DatabaseObject;
import de.switchprojects.controller.printer.queue.basic.BasicPrintableObject;
import de.switchprojects.controller.printer.user.object.User;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.function.Function;

/**
 * Represents an object which can be printed and written into the database
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public abstract class PrintableObject implements DatabaseObject {

    public static final Function<byte[], PrintableObject> MAPPER = bytes -> {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(stream)) {
            Long userID = inputStream.readLong();
            Long requestTime = inputStream.readLong();
            String path = inputStream.readUTF();
            Boolean sliced = inputStream.readBoolean();
            String realName = inputStream.readUTF();

            User user = (User) inputStream.readObject();

            return new BasicPrintableObject(requestTime, userID, user, sliced, path, realName);
        } catch (final IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    };

    public PrintableObject(Long requestTime) {
        this.requestTime = requestTime;
    }

    private final long requestTime;

    /**
     * @return The user which has requested the print
     */
    @NotNull
    public abstract User getUser();

    /**
     * @return The path were the file to print is located
     */
    @NotNull
    public abstract String getPath();

    /**
     * @return The file which the user gave before the rename for the system internal stuff
     */
    @NotNull
    public abstract String getRealFileName();

    /**
     * Changes the path of the file location (for example after the slice)
     *
     * @param path The new path where the file is located
     */
    public abstract void setPath(@NotNull String path);

    /**
     * @return If the current object is sliced
     */
    public abstract boolean isSliced();

    /**
     * Sets the sliced status of the file
     *
     * @param sliced If the current object is sliced
     */
    public abstract void setSliced(boolean sliced);

    /**
     * @return The when the print got requested
     */
    public final long getRequestTime() {
        return requestTime;
    }
}
