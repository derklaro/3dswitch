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

import de.switchprojects.controller.printer.events.EventManager;
import org.jetbrains.annotations.NotNull;

/**
 * The global api of the system
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class GlobalAPI {

    /**
     * We let nobody see the constructor.
     *
     * @throws UnsupportedOperationException If someone tries to instantiate this class using reflections
     */
    private GlobalAPI() {
        throw new UnsupportedOperationException();
    }

    public static void setExecutorAPI(ExecutorAPI executor) {
        if (executorAPI != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton api");
        }

        executorAPI = executor;
    }

    private static ExecutorAPI executorAPI;

    @NotNull
    public static EventManager getEventManager() {
        return executorAPI.getEventManager();
    }
}
