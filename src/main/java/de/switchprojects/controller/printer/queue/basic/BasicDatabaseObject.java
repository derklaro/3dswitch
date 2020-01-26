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
package de.switchprojects.controller.printer.queue.basic;

import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.user.User;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Represents a basic implementation of a print job
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class BasicDatabaseObject extends PrintableObject {

    public BasicDatabaseObject(Long userID, Long time, User user, String path) {
        super(time == null ? System.currentTimeMillis() : time);
        this.userID = userID;
        this.user = user;
        this.path = path;
    }

    private final Long userID;

    private final User user;

    private final String path;

    @Override
    public @NotNull User getUser() {
        return this.user;
    }

    @Override
    public @NotNull String getPath() {
        return this.path;
    }

    @Override
    public @NotNull String getKey() {
        return Long.toString(super.getRequestTime());
    }

    @Override
    public @NotNull String getTable() {
        return "jobs";
    }

    @Override
    public @NotNull byte[] serialize() {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {
            objectOutputStream.writeObject(this.user);
            objectOutputStream.writeLong(this.userID);
            objectOutputStream.writeLong(getRequestTime());
            objectOutputStream.writeUTF(this.path);

            return stream.toByteArray();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }
}
