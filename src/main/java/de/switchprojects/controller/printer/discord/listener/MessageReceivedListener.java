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

import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.discord.DiscordModule;
import de.switchprojects.controller.printer.queue.basic.BasicPrintableObject;
import de.switchprojects.controller.printer.queue.object.PrintableObject;
import de.switchprojects.controller.printer.slicer.SliceQueue;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.User;
import de.switchprojects.controller.printer.user.object.UserType;
import de.switchprojects.controller.printer.util.FileUtils;
import de.switchprojects.controller.printer.util.Validate;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

/**
 * Sets the listener which is handling the file upload to the discord bot private channel
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class MessageReceivedListener extends ListenerAdapter {

    private static final Collection<String> ALLOWED_EXTENSIONS = Arrays.asList("obj", "stl");

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if (event.getMessage().getAttachments().size() != 1) {
            event.getChannel().sendMessage("Please add one attachment which is an unsliced 3D-File (allowed: \"*.stl\" or \"*.obj\"").queue();
            return;
        }

        Message.Attachment attachment = event.getMessage().getAttachments().get(0);
        String attachmentExtension = attachment.getFileExtension();
        if (attachmentExtension == null) {
            event.getChannel().sendMessage("Please add one attachment which is an unsliced 3D-File (allowed: \"*.stl\" or \"*.obj\"").queue();
            return;
        }

        if (ALLOWED_EXTENSIONS.stream().noneMatch(attachmentExtension::equals)) {
            event.getChannel().sendMessage("Please add one attachment which is an unsliced 3D-File (allowed: \"*.stl\" or \"*.obj\"").queue();
            return;
        }

        if (!DiscordModule.hasAccess(event.getAuthor())) {
            event.getChannel().sendMessage("I'm sorry but you do not have permission to upload a file. " +
                    "Please contact an administrator if you believe this is an error").queue();
            return;
        }

        event.getChannel().sendMessage("Uploading file, please wait...").queue();

        User user = getDiscordUserManagementOrFail().getUserByID(event.getAuthor().getIdLong());
        attachment.retrieveInputStream().thenAccept(stream -> {
            String path = "files/unsliced/" + System.currentTimeMillis() + "-" + attachment.getFileName();

            FileUtils.copy(stream, Paths.get(path));
            PrintableObject object = new BasicPrintableObject(user.getUniqueID(), null, user, path);

            SliceQueue.queue(object);

            event.getChannel().sendMessage("Received file successfully and added it to slice queue").queue();
        });
    }

    @NotNull
    private static UserManagement getDiscordUserManagementOrFail() {
        UserManagement management = GlobalAPI.getUserManagement(UserType.DISCORD);
        Validate.assertNotNull(management, "User handler for discord is not registered");
        return management;
    }
}
