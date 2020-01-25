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
package de.switchprojects.controller.printer.database;

import de.switchprojects.controller.printer.database.config.DatabaseConfig;
import de.switchprojects.controller.printer.database.object.DatabaseObject;
import de.switchprojects.controller.printer.database.object.DatabaseObjectToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a database connection
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public interface DatabaseDriver {

    /**
     * Connects to the database
     *
     * @param config The config file for the database config
     * @return If the connection was successful
     */
    boolean connect(@NotNull DatabaseConfig config);

    /**
     * Creates a new table in the database
     *
     * @param table The name of the new table in the database
     */
    void createTable(@NotNull String table);

    /**
     * Deletes a table from the database
     *
     * @param table The name of the table which should get deleted
     */
    void deleteTable(@NotNull String table);

    /**
     * Inserts a database object into the database
     *
     * @param object The object which should get inserted
     */
    void insert(@NotNull DatabaseObject object);

    /**
     * Updates an object in the database
     *
     * @param object The object which should get updated
     */
    void update(@NotNull DatabaseObject object);

    /**
     * Gets an object from the database
     *
     * @param databaseObjectToken The token to deserialize the object from the database
     * @param <T> The type of the object which gets deserialize from the database
     * @return The deserialize object from the database
     */
    @Nullable
    <T> T get(@NotNull DatabaseObjectToken<T> databaseObjectToken);

    /**
     * Deletes an object from the database
     *
     * @param table The table name from which the object should get deleted
     * @param key The key of the database which should get deleted
     */
    void deleteFromTable(@NotNull String table, @NotNull String key);
}
