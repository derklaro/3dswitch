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
package de.switchprojects.controller.printer.commands;

import de.switchprojects.controller.printer.commands.source.CommandSource;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command which can get executed from the console or any other source.
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public interface Command {

    /**
     * @return The main name of the command
     */
    @NotNull
    String getCommandName();

    /**
     * @return All aliases of the command which can get used
     */
    @NotNull
    String[] getAliases();

    /**
     * @return The description of the command
     */
    @NotNull
    String getDescription();

    /**
     * Executes the command
     *
     * @param source      The source from where the command got called
     * @param commandLine The complete command line typed by the user
     * @param strings     All parsed arguments from the command line
     */
    void execute(@NotNull CommandSource source, @NotNull String commandLine, @NotNull String[] strings);
}