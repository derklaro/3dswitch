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
package de.switchprojects.controller.printer.discord.user;

import de.switchprojects.controller.printer.user.object.User;
import de.switchprojects.controller.printer.user.object.UserType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.function.Function;

/**
 * Represents a default discord user
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class DiscordUser implements User {

    public static final Function<ObjectInputStream, User> MAPPER = stream -> {
        try {
            return new DiscordUser(stream.readLong());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return null;
    };

    public DiscordUser(long id) {
        this.id = id;
    }

    private final long id;

    @Override
    public UserType getUserType() {
        return UserType.DISCORD;
    }

    @Override
    public Long getUniqueID() {
        return this.id;
    }

    @Override
    public @NotNull String getKey() {
        return Long.toString(getUniqueID());
    }

    @Override
    public @NotNull String getTable() {
        return DiscordUserManagement.TABLE_NAME;
    }

    @Override
    public @NotNull byte[] serialize() {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream)) {
            objectOutputStream.writeLong(this.id);
            return stream.toByteArray();
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }
}
