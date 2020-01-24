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
package de.switchprojects.controller.printer.console.reader;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.console.TerminalConsole;
import de.switchprojects.controller.printer.console.events.ConsoleLineReadEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the console which handles all console inputs and executes the line in the command
 * manager.
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class TerminalReaderThread extends Thread {

    private static final String PROMPT = System.getProperty(
            "console.prompt",
            "[" + System.getProperty("user.name") + "@" + System.getProperty("user.dir") + " ~]$ "
    );

    public TerminalReaderThread(@NotNull TerminalConsole terminalConsole) {
        this.terminalConsole = terminalConsole;
    }

    private final TerminalConsole terminalConsole;

    @Override
    public void run() {
        String line;

        while (!Thread.interrupted()) {
            try {
                line = terminalConsole.readLine(PROMPT);
                while (!line.trim().isEmpty()) {
                    ConsoleLineReadEvent event = new ConsoleLineReadEvent(line);
                    GlobalAPI.getEventManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }

                    // TODO: Execute command line with command manager
                    line = terminalConsole.readLine(PROMPT);
                }
            } catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
