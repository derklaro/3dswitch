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
package de.switchprojects.controller.printer.web;

import de.switchprojects.controller.printer.PrinterController;
import de.switchprojects.controller.printer.api.GlobalAPI;
import de.switchprojects.controller.printer.user.UserManagement;
import de.switchprojects.controller.printer.user.object.UserType;
import de.switchprojects.controller.printer.util.Validate;
import de.switchprojects.controller.printer.web.handlers.FileHandler;
import de.switchprojects.controller.printer.web.handlers.UserHandler;
import de.switchprojects.controller.printer.web.user.WebUserManagement;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class JavalinWebModule {

    private JavalinWebModule() {
        throw new UnsupportedOperationException();
    }

    private static Javalin javalin;

    public static void init(@NotNull String acceptedToken) {
        javalin = Javalin
                .create(e -> e.showJavalinBanner = false)
                .addHandler(HandlerType.POST, "/api/files", new FileHandler(acceptedToken))
                .addHandler(HandlerType.POST, "/api/user", new UserHandler(acceptedToken))
                .start(7000);

        PrinterController.getInstance().getUserManagements().add(new WebUserManagement());
    }

    public static void close() {
        if (javalin != null) {
            javalin.stop();
            javalin = null;
        }
    }

    public static Long parseHeader(String header) {
        if (header == null) {
            return -1L;
        }

        try {
            return Long.parseLong(header);
        } catch (final Throwable ex) {
            return -1L;
        }
    }

    @NotNull
    public static UserManagement getWebUserManagementOrFail() {
        UserManagement userManagement = GlobalAPI.getUserManagement(UserType.WEB);
        Validate.assertNotNull(userManagement, "Web user management is not registered");
        return userManagement;
    }
}
