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
package de.switchprojects.controller.printer.commands.basic.commands;

import de.switchprojects.controller.printer.commands.basic.BasicCommand;
import de.switchprojects.controller.printer.commands.source.CommandSource;
import de.switchprojects.controller.printer.queue.PrintQueue;
import de.switchprojects.controller.printer.slicer.SliceQueue;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class QueueCommand extends BasicCommand {

    public QueueCommand() {
        super("queue", new String[0], "Lists all entries of the print or slice queue");
    }

    @Override
    public void execute(@NotNull CommandSource source, @NotNull String commandLine, @NotNull String[] strings) {
        if (strings.length != 1) {
            source.sendMessage("queue <slicer/print>");
            return;
        }

        if (strings[0].equalsIgnoreCase("slicer")) {
            source.sendMessage("Slice-Warteschlange (" + SliceQueue.QUEUE.size() + "):");
            source.sendMessage("\n" + SliceQueue.QUEUE
                    .stream()
                    .map(entry -> String.format("%s of %d", entry.getPath(), entry.getUser().getUniqueID()))
                    .collect(Collectors.joining("\n")));
            return;
        }

        if (strings[0].equalsIgnoreCase("printer")) {
            source.sendMessage("Druck-Warteschlange (" + PrintQueue.QUEUE.size() + "):");
            source.sendMessage("\n" + PrintQueue.QUEUE
                    .stream()
                    .map(entry -> String.format("%s of %d", entry.getPath(), entry.getUser().getUniqueID()))
                    .collect(Collectors.joining("\n")));
            return;
        }

        source.sendMessage("queue <slicer/print>");
    }
}
