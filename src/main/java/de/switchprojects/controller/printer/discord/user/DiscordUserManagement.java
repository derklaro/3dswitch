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
package de.switchprojects.controller.printer.discord.user;

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.database.object.DatabaseObjectToken;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.User;
import de.switchprojects.controller.printer.user.object.UserType;
import org.jetbrains.annotations.NotNull;

/**
 * Manges the user which are connected via discord
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class DiscordUserManagement implements UserManagement {

    public static final String TABLE_NAME = "discord_users";

    public DiscordUserManagement() {
        GlobalAPI.getDatabase().createTable(TABLE_NAME);
    }

    @Override
    public @NotNull User getUserByID(@NotNull Long uniqueID) {
        User user = GlobalAPI.getDatabase().getOrDefault(DatabaseObjectToken.newToken(DiscordUser.MAPPER,
                Long.toString(uniqueID), TABLE_NAME), new DiscordUser(uniqueID));
        return user == null /* should never happen */ ? new DiscordUser(uniqueID) : user;
    }

    @Override
    public void updateUser(@NotNull User user) {
        GlobalAPI.getDatabase().update(user);
    }

    @Override
    public void deleteUser(@NotNull Long userID) {
        GlobalAPI.getDatabase().deleteFromTable(TABLE_NAME, Long.toString(userID));
    }

    @Override
    public @NotNull UserType getHandlingType() {
        return UserType.DISCORD;
    }
}
