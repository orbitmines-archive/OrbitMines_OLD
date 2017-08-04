package com.orbitmines.api;

import com.orbitmines.api.spigot.utils.ConsoleUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Database {

    private static Database database;

    private Connection connection;
    private String hostName;
    private int port;
    private String databaseName;
    private String username;
    private String password;

    public Database(String hostName, int port, String databaseName, String username, String password) {
        database = this;

        this.hostName = hostName;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;

        openConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean contains(Table table, Column column, Where where) {
        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString();

        try {
            checkConnection();

            ResultSet rs = getConnection().prepareStatement(query).executeQuery();

            boolean bl = rs.next();
            rs.close();

            return bl;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void insert(Table table, String values) {
        String query = "INSERT INTO `" + table.toString() + "` (`" + table.columns() + "`) VALUES ('" + values + "')";

        try {
            checkConnection();

            PreparedStatement ps = getConnection().prepareStatement(query);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Table table, Where where) {
        try {
            checkConnection();

            Statement s = getConnection().createStatement();
            s.executeUpdate("DELETE FROM `" + table.toString() + "` WHERE " + where.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getInt(Table table, Column column, Where where) {
        int integer = 0;

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString();

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                integer = rs.getInt(column.toString());
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return integer;
    }

    public String getString(Table table, Column column, Where where) {
        String string = "";

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString();

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                string = rs.getString(column.toString());
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        string = string.replace("`", "'");
        return string;
    }

    public Map<String, Integer> getIntEntries(Table table, Column columnKey, Column columnValue) {
        Map<String, Integer> entries = new HashMap<>();

        String query = "SELECT `" + where(columnKey.toString(), columnValue.toString()) + "` FROM `" + table.toString() + "`";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                entries.put(rs.getString(columnKey.toString()), rs.getInt(columnValue.toString()));
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return entries;
    }

    public Map<String, String> getStringEntries(Table table, Column columnKey, Column columnValue) {
        Map<String, String> entries = new HashMap<>();

        String query = "SELECT `" + where(columnKey.toString(), columnValue.toString()) + "` FROM `" + table.toString() + "`";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                entries.put(rs.getString(columnKey.toString()), rs.getString(columnValue.toString()));
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return entries;
    }

    public void update(Table table, Where where, Set... sets) {
        try {
            checkConnection();

            Statement s = connection.createStatement();
            s.executeUpdate("UPDATE `" + table.toString() + "` SET " + sets(sets) + " WHERE " + where.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void openConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException ex) {
            ConsoleUtils.warn("Cannot connect to the database.");
            ConsoleUtils.warn("Error:");
            ex.printStackTrace();
        }
    }

    private void checkConnection() throws SQLException {
        if (connection.isClosed())
            openConnection();
    }

    public String where(String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append("`,`");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
    }

    public String values(String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append("','");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
    }

    private String sets(Set... sets) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Set set : sets) {
            stringBuilder.append(set.toString());
            stringBuilder.append(",");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    public static Database get() {
        return database;
    }

    public static class Set {

        private Column column;
        private String value;

        public Set(Column column, String value) {
            this.column = column;
            this.value = value;
        }

        public Column getColumn() {
            return column;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "`" + column + "` = '" + value.replace("'", "`") + "'";
        }
    }

    public static class Where extends Set {

        public Where(Column column, String value) {
            super(column, value);
        }

    }

    public enum Table {

        PLAYERS("Players", Column.UUID, Column.NAME, Column.STAFFRANK, Column.VIPRANK, Column.MONTHLYBONUS, Column.VOTES, Column.CACHEDVOTES, Column.STATS),
        SERVERS("Servers", Column.NAME, Column.STATUS, Column.MAXPLAYERS);

        private final String table;
        private final Column[] columns;

        Table(String table, Column... columns) {
            this.table = table;
            this.columns = columns;
        }

        public Column[] getColumns() {
            return columns;
        }

        @Override
        public String toString() {
            return table;
        }

        public String columns() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Column column : columns) {
                stringBuilder.append(column.toString());
                stringBuilder.append("`,`");
            }
            return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
        }
    }

    public enum Column {

        UUID,
        NAME,
        STAFFRANK,
        VIPRANK,
        MONTHLYBONUS,
        VOTES,
        CACHEDVOTES,
        STATS,

        STATUS,
        MAXPLAYERS;

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
