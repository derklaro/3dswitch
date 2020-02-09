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
package de.switchprojects.controller.printer.api;

import de.switchprojects.controller.printer.commands.CommandMap;
import de.switchprojects.controller.printer.database.DatabaseDriver;
import de.switchprojects.controller.printer.events.EventManager;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.UserType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The global api which represents the full system functionality.
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public interface ExecutorAPI {

    /**
     * @return The current event manager of the cloud system
     */
    @NotNull EventManager getEventManager();

    /**
     * @return The command map used by the system
     */
    @NotNull CommandMap getCommandMap();

    /**
     * @return The current in-use database
     */
    @NotNull DatabaseDriver getDatabase();

    /**
     * Get the user management which is assigned to the user type
     *
     * @param userType The type of the user management
     * @return The user management which handles the specified type or {@code null} if the type is not handled
     */
    @Nullable UserManagement getUserManagement(@NotNull UserType userType);

    /**
     * @return If the printer is ready and cleaned up to print the next object
     */
    boolean isReadyForNext();

    /**
     * Sets the status if the printer is cleaned up and ready for the next job
     *
     * @param ready If the printer is ready
     */
    void setIsReadyForNext(boolean ready);
}
