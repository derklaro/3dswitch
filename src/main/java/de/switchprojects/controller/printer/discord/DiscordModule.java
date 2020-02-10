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
package de.switchprojects.controller.printer.discord;

import de.switchprojects.controller.printer.PrinterController;
import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.discord.listener.MessageReceivedListener;
import de.switchprojects.controller.printer.discord.listener.SystemTickerListener;
import de.switchprojects.controller.printer.discord.user.DiscordUserManagement;
import de.switchprojects.controller.printer.util.Validate;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Optional;

/**
 * Represents the discord module which can download the templates from the discord server
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class DiscordModule {

    private static JDA jda;

    private static Guild guild;

    public static void init(@NotNull String botToken) {
        Validate.assertNotNull(botToken, "Cannot use null bot token");
        Validate.assertNull(jda, "JDA already initialized");

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(botToken)
                    .setActivity(Activity.playing("3DSwitch"))
                    .setAutoReconnect(true)
                    .addEventListeners(new MessageReceivedListener())
                    .build()
                    .awaitReady();

            guild = jda.getGuildById("670310948757831690");
            Validate.assertNotNull(guild, "The bot is not in guild 670310948757831690");

            PrinterController.getInstance().getUserManagements().add(new DiscordUserManagement());
            GlobalAPI.getEventManager().registerListener(new SystemTickerListener());
        } catch (final LoginException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean hasAccess(@NotNull User user) {
        Validate.assertNotNull(jda, "The JDA is not initialized correctly");
        Validate.assertNotNull(guild, "The bot is not on the main guild");
        Validate.assertNotNull(user, "The provided user is invalid");

        Member member = guild.getMemberById(user.getId());
        if (member == null) {
            return false;
        }

        return member.getRoles().stream().anyMatch(e -> e.getId().equals("671265328474357780"));
    }

    @NotNull
    public static Optional<Member> getMember(long id) {
        Member member = guild.getMemberById(id);
        return Optional.ofNullable(member);
    }

    public static void close() {
        if (jda != null) {
            jda.shutdownNow();
            jda = null;
        }
    }

    public static Guild getGuild() {
        return guild;
    }

    public static JDA getJda() {
        return jda;
    }
}
