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
package de.switchprojects.controller.printer.user;

import de.switchprojects.controller.printer.user.object.User;
import de.switchprojects.controller.printer.user.object.UserType;
import de.switchprojects.controller.printer.user.util.NotifyType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the management for the {@link User}s
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public interface UserManagement {

    /**
     * Retrieves or creates a user by the the unique id of him
     *
     * @param uniqueID The unique id of the user
     * @return The user of the database or a newly created user
     */
    @NotNull
    User getUserByID(@NotNull Long uniqueID);

    /**
     * Updates a specific user in the database
     *
     * @param user The user which should get updated
     */
    void updateUser(@NotNull User user);

    /**
     * Deletes a user completely
     *
     * @param userID The unique id of the user
     */
    void deleteUser(@NotNull Long userID);

    /**
     * Notifies the user that the current print job is done
     *
     * @param notifyType The type of the current notify message which should get sent to the user
     * @param fileName   The name of the printed file
     * @param userID     The id of the user which gave the task
     */
    void notify(@NotNull NotifyType notifyType, @NotNull String fileName, long userID);

    /**
     * @return The type of user which this manager is handling
     */
    @NotNull
    UserType getHandlingType();
}
