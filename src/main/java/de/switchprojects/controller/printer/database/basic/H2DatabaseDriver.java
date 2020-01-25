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
package de.switchprojects.controller.printer.database.basic;

import de.switchprojects.controller.printer.database.DatabaseDriver;
import de.switchprojects.controller.printer.database.config.DatabaseConfig;
import de.switchprojects.controller.printer.database.object.DatabaseObject;
import de.switchprojects.controller.printer.database.object.DatabaseObjectToken;
import org.h2.Driver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;

/**
 * A default implementation of a database driver
 *
 * @author Pasqual Koschmieder
 * @since 1.0
 */
public class H2DatabaseDriver implements DatabaseDriver {

    private Connection connection;

    @Override
    public boolean connect(@NotNull DatabaseConfig config) {
        try {
            Driver.load();
            this.connection = DriverManager.getConnection(config.formatConnectionString());

            return true;
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public void createTable(@NotNull String table) {
        try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table
                + " (`key` TEXT, `value` LONGBLOB)")) {
            statement.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteTable(@NotNull String table) {
        try (PreparedStatement statement = connection.prepareStatement("DROP TABLE IF EXISTS " + table)) {
            statement.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void insert(@NotNull DatabaseObject object) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ? (`key`, `value`) VALUES (?, ?)")) {
            statement.setString(1, object.getTable());
            statement.setString(2, object.getKey());
            statement.setBytes(3, object.serialize());

            statement.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull DatabaseObject object) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE ? SET `value` = ? WHERE `key` = ?")) {
            statement.setString(1, object.getTable());
            statement.setBytes(2, object.serialize());
            statement.setString(3, object.getKey());

            statement.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public <T> @Nullable T get(@NotNull DatabaseObjectToken<T> databaseObjectToken) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT `value` FROM ? WHERE `key` = ?")) {
            statement.setString(1, databaseObjectToken.getTable());
            statement.setString(2, databaseObjectToken.getKey());

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(resultSet.getBytes("value")))) {
                return databaseObjectToken.deserialize(stream);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteFromTable(@NotNull String table, @NotNull String key) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ? WHERE `key` = ?")) {
            statement.setString(1, table);
            statement.setString(2, key);

            statement.executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }
}
