package com.orbitmines.api;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Database {

    public static String NAME = "OrbitMines";

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

        setupTables();
    }

    public Connection getConnection() {
        return connection;
    }

    private void setupTables() {
        for (Table table : Table.values()) {
            StringBuilder tableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + table.toString() + "` (");
            for (Column column : table.getColumns()) {
                tableQuery.append("`" + column.toString() + "` " + column.getType().toString(column.getChars()) + ", ");
            }

            String query = tableQuery.toString().substring(0, tableQuery.length() -2) + ");";

            try {
                checkConnection();

                PreparedStatement ps = connection.prepareStatement(query);
                ps.execute();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean contains(Table table, Column column, Where where) {
        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString() + ";";

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
        String query = "INSERT INTO `" + table.toString() + "` (`" + table.columns() + "`) VALUES ('" + values + "');";

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
            s.executeUpdate("DELETE FROM `" + table.toString() + "` WHERE " + where.toString() + ";");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Map<Column, String> getValues(Table table, Where where, Column... columns) {
        Map<Column, String> values = new HashMap<>();

        String query = "SELECT `" + colums(columns) + "` FROM `" + table.toString() + "` WHERE " + where.toString() + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                for (Column column : columns) {
                    values.put(column, rs.getString(column.toString()));
                }
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    public List<Map<Column, String>> getEntries(Table table) {
        return getEntries(table, table.getColumns());
    }

    public List<Map<Column, String>> getEntries(Table table, Column... columns) {
        List<Map<Column, String>> values = new ArrayList<>();

        String query = "SELECT `" + colums(columns) + "` FROM `" + table.toString() + "`;";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                Map<Column, String> entry = new HashMap<>();
                for (Column column : columns) {
                    entry.put(column, rs.getString(column.toString()));
                }
                values.add(entry);
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    public List<Map<Column, String>> getEntries(Table table, Where where, Column... columns) {
        List<Map<Column, String>> values = new ArrayList<>();

        String query = "SELECT `" + colums(columns) + "` FROM `" + table.toString() + "` WHERE " + where.toString() + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                Map<Column, String> entry = new HashMap<>();
                for (Column column : columns) {
                    entry.put(column, rs.getString(column.toString()));
                }
                values.add(entry);
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    public int getInt(Table table, Column column, Where where) {
        int integer = 0;

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString() + ";";

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

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "` WHERE " + where.toString() + ";";

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

        String query = "SELECT `" + where(columnKey.toString(), columnValue.toString()) + "` FROM `" + table.toString() + "`;";

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

        String query = "SELECT `" + where(columnKey.toString(), columnValue.toString()) + "` FROM `" + table.toString() + "`;";

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
            s.executeUpdate("UPDATE `" + table.toString() + "` SET " + sets(sets) + " WHERE " + where.toString() + ";");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void openConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException ex) {
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
        return stringBuilder.toString().substring(0, stringBuilder.length() - 3);
    }

    public String colums(Column... columns) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Column column : columns) {
            stringBuilder.append(column.toString());
            stringBuilder.append("`,`");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 3);
    }

    public String values(String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append("','");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - 3);
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

        PLAYERS(Column.UUID, Column.NAME, Column.STAFFRANK, Column.VIPRANK, Column.LANGUAGE, Column.SILENT, Column.MONTHLYBONUS, Column.VOTES, Column.CACHEDVOTES, Column.STATS),
        SERVERS(Column.NAME, Column.STATUS, Column.MAXPLAYERS),
        BUNGEE(Column.TYPE, Column.DATA),
        IPS(Column.UUID, Column.LASTIP, Column.LASTLOGIN, Column.HISTORY),
        BANS(Column.BANNED, Column.ACTIVE, Column.BANNEDBY, Column.BANNEDON, Column.BANNEDUNTIL, Column.REASON),
        REPORTS(Column.REPORTED, Column.REPORTEDBY, Column.REPORTEDON, Column.SERVER, Column.REASON),
        HOLOGRAMS(Column.SERVER, Column.HOLOGRAMNAME, Column.DATA);

        private final Column[] columns;

        Table(Column... columns) {
            this.columns = columns;
        }

        public Column[] getColumns() {
            return columns;
        }

        public String columns() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Column column : columns) {
                stringBuilder.append(column.toString());
                stringBuilder.append("`,`");
            }
            return stringBuilder.toString().substring(0, stringBuilder.length() - 3);
        }
    }

    public enum Column {

        UUID(Type.VARCHAR, 36),
        NAME(Type.VARCHAR, 16),
        STAFFRANK(Type.VARCHAR, 30),
        VIPRANK(Type.VARCHAR, 30),
        LANGUAGE(Type.VARCHAR, 30),
        SILENT(Type.VARCHAR, 5),
        MONTHLYBONUS(Type.VARCHAR, 5),
        VOTES(Type.INT, 10),
        CACHEDVOTES(Type.VARCHAR, 500),
        STATS(Type.VARCHAR, 20000),

        STATUS(Type.VARCHAR, 30),
        MAXPLAYERS(Type.INT, 10),

        TYPE(Type.VARCHAR, 30),
        DATA(Type.VARCHAR, 1000),

        LASTIP(Type.VARCHAR, 30),
        LASTLOGIN(Type.VARCHAR, 30),
        HISTORY(Type.VARCHAR, 500),

        BANNED(Type.VARCHAR, 36),
        ACTIVE(Type.VARCHAR, 5),
        BANNEDBY(Type.VARCHAR, 36),
        BANNEDON(Type.VARCHAR, 30),
        BANNEDUNTIL(Type.VARCHAR, 30),
        REASON(Type.VARCHAR, 100),

        REPORTED(Type.VARCHAR, 36),
        REPORTEDBY(Type.VARCHAR, 36),
        REPORTEDON(Type.VARCHAR, 30),
        SERVER(Type.VARCHAR, 30),

        HOLOGRAMNAME(Type.VARCHAR, 50);

        private final Type type;
        private final int chars;

        Column(Type type, int chars) {
            this.type = type;
            this.chars = chars;
        }

        public Type getType() {
            return type;
        }

        public int getChars() {
            return chars;
        }

        @Override
        public String toString() {
            return super.toString();
        }

        public enum Type {

            VARCHAR,
            INT;

            public String toString(int chars) {
                return super.toString() + "(" + chars + ")";
            }
        }
    }
}
