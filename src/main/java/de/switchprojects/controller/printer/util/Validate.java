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
package de.switchprojects.controller.printer.util;

import org.jetbrains.annotations.NotNull;

/**
 * A simple class to validate some things to ensure the system running correctly
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public final class Validate {

    private Validate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validates that the given long is bigger than the required minimum
     *
     * @param current The current long which should be greater than the minimum
     * @param min     The minimum length of the long
     * @throws AssertionError If the current long is smaller or equal to the minimum
     */
    public static void assertBigger(long current, long min) {
        if (current < min) {
            throw new AssertionError("Current value " + current + " is not bigger than " + min);
        }
    }

    /**
     * Asserts that the given object is non null
     *
     * @param o       The object which should get checked
     * @param message The message which should be in the exception as detail message
     * @throws AssertionError If the given object is null
     */
    public static void assertNotNull(Object o, @NotNull String message) {
        if (o == null) {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that the given object is null
     *
     * @param o       The object which should get checked
     * @param message The message which should be in the exception as detail message
     * @throws AssertionError If the given object is non-null
     */
    public static void assertNull(Object o, @NotNull String message) {
        if (o != null) {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that the given long equals to the expected one
     *
     * @param current  The current long which should equal to the expected
     * @param expected The expected long
     * @throws AssertionError If the current long does not equal to the expected one
     */
    public static void assertEquals(long current, long expected) {
        if (current != expected) {
            throw new AssertionError("Expected " + expected + " but received " + current);
        }
    }

    /**
     * Asserts that the given boolean equals to the expected one
     *
     * @param current  The current boolean which should equal to the expected
     * @param expected The expected boolean
     * @throws AssertionError If the current boolean does not equal to the expected one
     */
    public static void assertEquals(boolean current, boolean expected) {
        if (current != expected) {
            throw new AssertionError("Expected " + expected + " but received " + current);
        }
    }
}
