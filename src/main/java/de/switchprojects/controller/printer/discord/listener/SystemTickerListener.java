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
package de.switchprojects.controller.printer.discord.listener;

import de.switchprojects.controller.printer.discord.DiscordModule;
import de.switchprojects.controller.printer.events.annotations.Subscribe;
import de.switchprojects.controller.printer.ticker.event.SystemTickEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Objects;

import static de.switchprojects.controller.printer.ticker.SystemTicker.DECIMAL_FORMAT;

/**
 * Handles the tick of the console print job check and gives it to the console
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class SystemTickerListener {

    public SystemTickerListener() {
        Objects.requireNonNull(DiscordModule.getGuild().getTextChannelById("676329808535355392"), "The status channel can't get located")
                .retrievePinnedMessages()
                .queue(e -> {
                    e.stream()
                            .filter(message -> !message.getAuthor().getId().equals(DiscordModule.getJda().getSelfUser().getId()))
                            .forEach(message -> message.delete().queue());

                    if (e.isEmpty()) {
                        return;
                    }

                    if (e.size() == 1) {
                        lastMessage = e.get(0);
                        return;
                    }

                    for (int i = 1; i < e.size(); i++) {
                        e.get(i).delete().queue();
                    }

                    lastMessage = e.get(0);
                });
    }

    private Message lastMessage;

    @Subscribe
    public void handleTick(final SystemTickEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor("SwitchProjects - Status")
                .setColor(Color.GREEN)
                .setTitle("Status call")
                .addField(
                        "Running:",
                        event.getCurrentJob().getName() + " (" + DECIMAL_FORMAT.format(event.getCurrentJob().getJobProgress().percentComplete()) + "%)",
                        false
                ).build();
        if (lastMessage != null) {
            lastMessage.editMessage(embed).queue();
            return;
        }

        Objects.requireNonNull(DiscordModule.getGuild().getTextChannelById("676329808535355392"), "The status channel can't get located")
                .sendMessage(embed)
                .queue(e -> {
                    lastMessage = e;
                    lastMessage.pin().queue();
                });
    }
}
