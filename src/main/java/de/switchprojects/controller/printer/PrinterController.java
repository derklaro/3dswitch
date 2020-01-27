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
package de.switchprojects.controller.printer;

import de.switchprojects.controller.printer.api.ExecutorAPI;
import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.commands.CommandMap;
import de.switchprojects.controller.printer.commands.basic.BasicCommandMap;
import de.switchprojects.controller.printer.console.TerminalConsole;
import de.switchprojects.controller.printer.console.basic.BasicTerminalConsole;
import de.switchprojects.controller.printer.console.reader.TerminalReaderThread;
import de.switchprojects.controller.printer.database.DatabaseDriver;
import de.switchprojects.controller.printer.database.basic.H2DatabaseConfig;
import de.switchprojects.controller.printer.database.basic.H2DatabaseDriver;
import de.switchprojects.controller.printer.discord.DiscordModule;
import de.switchprojects.controller.printer.events.EventManager;
import de.switchprojects.controller.printer.events.basic.BasicEventManager;
import de.switchprojects.controller.printer.octoprint.OctoPrintHelper;
import de.switchprojects.controller.printer.queue.PrintQueue;
import de.switchprojects.controller.printer.slicer.SliceQueue;
import de.switchprojects.controller.printer.ticker.SystemTicker;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.UserType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PrinterController implements ExecutorAPI {

    public static synchronized void main(@NotNull String[] args) {
        new PrinterController();
    }

    private static PrinterController instance;

    public static PrinterController getInstance() {
        return instance;
    }

    private PrinterController() {
        instance = this;
        GlobalAPI.setExecutorAPI(this);

        try {
            this.terminalConsole = new BasicTerminalConsole();
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Starting printer controller...");
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));

        System.out.println("Connecting to H2 database...");
        this.databaseDriver.connect(new H2DatabaseConfig());
        System.out.println("Connected to H2 database");

        System.out.println("Loading discord module...");
        DiscordModule.init(System.getProperty("module.discord.bot.token"));
        System.out.println("Loaded discord module successfully and established connection to discord");

        System.out.println("Opening connection to octoprint...");
        OctoPrintHelper.connect(
                System.getProperty("printer.host"),
                Integer.parseInt(System.getProperty("printer.port")),
                System.getProperty("printer.api.key")
        );
        System.out.println("Opened connection to octoprint successfully");

        System.out.println("Starting terminal console");
        new TerminalReaderThread(this.terminalConsole).start();
        System.out.println("Started terminal console successfully");

        System.out.println("Starting up queue threads...");
        new PrintQueue().start();
        new SliceQueue().start();
        System.out.println("Started up queue threads successfully");

        System.out.println("Starting system ticker");
        SystemTicker.start();
    }

    private final EventManager eventManager = new BasicEventManager();

    private final CommandMap commandMap = new BasicCommandMap();

    private final DatabaseDriver databaseDriver = new H2DatabaseDriver();

    private final Collection<UserManagement> userManagements = new CopyOnWriteArrayList<>();

    private final TerminalConsole terminalConsole;

    @Override
    public @NotNull EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        return this.commandMap;
    }

    @Override
    public @NotNull DatabaseDriver getDatabase() {
        return this.databaseDriver;
    }

    @Override
    public @Nullable UserManagement getUserManagement(@NotNull UserType userType) {
        return this.userManagements.stream().filter(e -> e.getHandlingType().equals(userType)).findFirst().orElse(null);
    }

    public Collection<UserManagement> getUserManagements() {
        return userManagements;
    }

    private void close() {
        this.terminalConsole.close();
        DiscordModule.close();
        this.databaseDriver.close();
    }
}
