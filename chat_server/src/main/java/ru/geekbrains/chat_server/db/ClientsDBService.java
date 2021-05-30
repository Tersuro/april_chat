package ru.geekbrains.chat_server.db;

import java.sql.*;

public class ClientsDBService {
    private static ClientsDBService instance;
    private final String DRIVER = "org.sqlite.JDBC";
    private final String CONNECTION = "jdbc:sqlite:chat_server/db/users.db";
    private final String GET_USERNAME = "select username from users where login = ? and password = ?;";
    private final String CHANGE_USERNAME = "update users set username = ? where username = ?;";
    private final String CREATE_DB = "create table if not exists users (id integer primary key autoincrement, login text unique not null, password text not null, username text unique not null);";
    private Connection connection;

    private ClientsDBService() {
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        //createDb();
    }

    public static ClientsDBService getInstance() {
        if (instance != null) return instance;
        instance = new ClientsDBService();
        return instance;
    }

    public String changeUsername(String oldName, String newName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(CHANGE_USERNAME)) {
            ps.setString(1, newName);
            ps.setString(2, oldName);
            if (ps.executeUpdate() > 0) return newName;
        }
        return oldName;
    }

    public String getClientNameByLoginPass(String login, String pass) {
        try (PreparedStatement ps = connection.prepareStatement(GET_USERNAME)) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String result = rs.getString("username");
                rs.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(CONNECTION);
        System.out.println("Подключены к базе данных");
    }

    private void createDb() {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_DB);
            st.execute("insert into users (login, password, username) values ('log1', 'pass1', 'user1'), ('log2', 'pass2', 'user2'), ('log3', 'pass3', 'user3');");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) connection.close();
            System.out.println("Отключены от базы данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
