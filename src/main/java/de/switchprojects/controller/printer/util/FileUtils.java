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
package de.switchprojects.controller.printer.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

/**
 * The basic file utils for the system to run simple file stuff
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Copies a file from the target to the destination path
     *
     * @param from The path from which the file is copied
     * @param to   The target location of the file
     */
    public static void copy(@NotNull Path from, @NotNull Path to) {
        Validate.assertNotNull(from, "Cannot copy from null path");
        Validate.assertNotNull(to, "Cannot copy to null path");

        try (InputStream inputStream = Files.newInputStream(from)) {
            copy(inputStream, to);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Copies the given bytes to a target file
     *
     * @param target The target path
     * @param bytes The byte array which should get copied to the file
     */
    public static void copy(@NotNull Path target, @NotNull byte[] bytes) {
        try {
            Files.write(target, bytes, StandardOpenOption.CREATE_NEW);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Copies an input stream from the source stream to a path
     *
     * @param source The stream of the input file
     * @param to     The destination where to copy the file to
     */
    public static void copy(@NotNull InputStream source, @NotNull Path to) {
        Validate.assertNotNull(source, "Cannot copy from null stream");
        Validate.assertNotNull(to, "Cannot copy to null path");

        createDirectories(to.getParent());

        try (OutputStream outputStream = Files.newOutputStream(to, StandardOpenOption.CREATE_NEW)) {
            pipeStreams(source, outputStream);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new directory
     *
     * @param path The path of the new directory
     */
    public static void createDirectories(@NotNull Path path) {
        Validate.assertNotNull(path, "Cannot create directory from null path");

        try {
            Files.createDirectories(path);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes a file if it's exists
     *
     * @param path The path to the file which should get deleted if it exists
     */
    public static void deleteIfExists(@NotNull String path) {
        Validate.assertNotNull(path, "Cannot delete null path");

        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Writes all bytes from inputStream to outputStream
     *
     * @param source      The source file stream from which the file is read
     * @param destination The target stream to which the file is written
     * @throws IOException If an i/o error occurs during the stream copy
     */
    private static void pipeStreams(InputStream source, OutputStream destination) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = source.read(buffer)) != -1) {
            destination.write(buffer, 0, read);
            destination.flush();
        }
    }
}
