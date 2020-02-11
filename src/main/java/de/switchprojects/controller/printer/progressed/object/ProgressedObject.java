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
package de.switchprojects.controller.printer.progressed.object;

import de.switchprojects.controller.printer.database.object.DatabaseObject;
import de.switchprojects.controller.printer.progressed.ProgressedDatabaseHelper;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.user.object.UserType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class ProgressedObject implements DatabaseObject {

    public static final Function<ObjectInputStream, ProgressedObject> MAPPER = stream -> {
        try {
            String realFileName = stream.readUTF();
            String userType = stream.readUTF();
            long id = stream.readLong();

            return new ProgressedObject("empty", realFileName, id, userType);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return null;
    };

    public ProgressedObject(@NotNull PrintableObject parent) {
        this.key = new File(parent.getPath()).getName();
        this.realFileName = parent.getRealFileName();
        this.userID = parent.getUser().getUniqueID();
        this.userType = parent.getUser().getUserType().name();
    }

    private ProgressedObject(String key, String realFileName, long userID, String userType) {
        this.key = key;
        this.realFileName = realFileName;
        this.userID = userID;
        this.userType = userType;
    }

    private final String key;

    private final String realFileName;

    private final long userID;

    private final String userType;

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull String getTable() {
        return ProgressedDatabaseHelper.DB_NAME;
    }

    public long getUserID() {
        return userID;
    }

    @Nullable
    public UserType getUserType() {
        return Arrays.stream(UserType.values()).filter(e -> e.name().equalsIgnoreCase(this.userType)).findFirst().orElse(null);
    }

    @NotNull
    public String getRealFileName() {
        return realFileName;
    }

    @Override
    public @NotNull byte[] serialize() {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {
            objectOutputStream.writeUTF(this.realFileName);
            objectOutputStream.writeUTF(this.userType);
            objectOutputStream.writeLong(this.userID);

            objectOutputStream.writeObject(null);

            return stream.toByteArray();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }
}
