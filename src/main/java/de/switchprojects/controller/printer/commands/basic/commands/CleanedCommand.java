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

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.commands.basic.BasicCommand;
import de.switchprojects.controller.printer.commands.source.CommandSource;
import de.switchprojects.controller.printer.octoprint.OctoPrintHelper;
import org.jetbrains.annotations.NotNull;

/**
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class CleanedCommand extends BasicCommand {

    public CleanedCommand() {
        super("cleaned", new String[]{"ready"}, "Sets the status if the printer is ready to print");
    }

    @Override
    public void execute(@NotNull CommandSource source, @NotNull String commandLine, @NotNull String[] strings) {
        if (GlobalAPI.isReadyForNext()) {
            source.sendMessage("Der Drucker ist bereits bereit.");
            return;
        }

        if (OctoPrintHelper.isPrintJobRunning()) {
            source.sendMessage("Es ist momentan ein Druck am laufen.");
            return;
        }

        GlobalAPI.setIsReadyForNext(true);
        source.sendMessage("Der Drucker ist nun bereit");
    }
}
