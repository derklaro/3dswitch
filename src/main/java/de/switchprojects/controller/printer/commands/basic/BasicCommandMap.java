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
package de.switchprojects.controller.printer.commands.basic;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.commands.Command;
import de.switchprojects.controller.printer.commands.CommandMap;
import de.switchprojects.controller.printer.commands.event.CommandPreProcessEvent;
import de.switchprojects.controller.printer.commands.source.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A basic implantation of a command map.
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class BasicCommandMap implements CommandMap {

    private final Collection<Command> commands = new CopyOnWriteArrayList<>();

    @Override
    public void registerCommand(@NotNull Command command) {
        this.commands.add(command);
    }

    @Override
    public void unregisterCommand(@NotNull String commandName) {
        this.commands.stream().filter(e -> e.getCommandName().equals(commandName)).forEach(commands::remove);
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSource source, @NotNull String commandLine) {
        String[] split = commandLine.split(" ");
        Optional<Command> command = findMatching(split[0]);
        if (!command.isPresent()) {
            return false;
        }

        String[] strings = split.length == 1 ? new String[0] : Arrays.copyOfRange(split, 1, split.length);
        CommandPreProcessEvent event = new CommandPreProcessEvent(command.get(), strings);
        GlobalAPI.getEventManager().callEvent(event);
        if (event.isCancelled()) {
            return true;
        }

        command.get().execute(source, commandLine, strings);
        return true;
    }

    @Override
    public @NotNull Collection<Command> getRegisteredCommands() {
        return this.commands;
    }

    private Optional<Command> findMatching(String commandLabel) {
        return this.commands.stream()
                .filter(e -> e.getCommandName().equalsIgnoreCase(commandLabel)
                        || Arrays.stream(e.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(commandLabel)))
                .findFirst();
    }
}
