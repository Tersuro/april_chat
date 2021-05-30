package ru.geekbrains.chat_server.auth;

import ru.geekbrains.chat_server.db.ClientsDBService;

import java.sql.SQLException;

public class DBAuthService implements AuthService{
    private ClientsDBService dbService;
    @Override
    public void start() {
        dbService = ClientsDBService.getInstance();
    }

    @Override
    public void stop() {
        dbService.closeConnection();
    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        return dbService.getClientNameByLoginPass(login, password);
    }

    @Override
    public String changeUsername(String oldName, String newName) {
        try {
            return dbService.changeUsername(oldName, newName);
        } catch (SQLException e) {
            throw new RuntimeException("Username change unsuccessful");
        }
    }

    @Override
    public String changePassword(String username, String oldPassword, String newPassword) {
        return null;
    }
}
