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
package de.switchprojects.controller.printer.console.formatter.basic;

import de.switchprojects.controller.printer.console.TerminalConsole;
import de.switchprojects.controller.printer.console.formatter.AbstractFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;

/**
 * Represents a basic implementation of a formatter.
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class BasicLogFormatter extends AbstractFormatter {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");

    public BasicLogFormatter(TerminalConsole terminalConsole) {
        super(terminalConsole);
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[")
                .append(DATE_FORMAT.format(record.getMillis()))
                .append(" ")
                .append(record.getLevel().getLocalizedName())
                .append("] ")
                .append(formatMessage(record))
                .append("\n");
        if (record.getThrown() != null) {
            stringBuilder.append(format(record.getThrown()));
        }

        return stringBuilder.toString();
    }
}
