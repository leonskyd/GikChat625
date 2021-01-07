package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;

    private class UserData{
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    List<UserData> users;
    public DataBaseAuthService() throws SQLException, ClassNotFoundException {
        users = new ArrayList<>();
        try {
            connect();
            ResultSet set = stmt.executeQuery("SELECT * FROM users");
           // int i = 0;
            while (set.next()) {
                String loginDb = set.getString(2);
                String passwordDb = set.getString(3);
                String nicknameDb = set.getString(4);
                users.add(new UserData(loginDb, passwordDb, nicknameDb));
              //System.out.println(users.size());
                //System.out.println(users.get(i).getLogin());
                //System.out.println(users.get(i).getPassword() );
                //i++;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException f) {
            f.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
    return user.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) throws SQLException {
        for (UserData user : users) {
            if(user.login.equals(login) || user.nickname.equals(nickname)) {
                return false;
            }
        }
        users.add(new UserData(login,password,nickname));
        setStringInDataBase(login, password, nickname);
        return true;
    }

    private void setStringInDataBase(String login, String password, String nickname) throws SQLException {
        psInsert = connection.prepareStatement("INSERT INTO users (login,password,nickname) VALUES (?,?,?)");
        psInsert.setString(1, login);
        psInsert.setString(2, password);
        psInsert.setString(3, nickname);
        psInsert.executeUpdate();
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        stmt = connection.createStatement();
    }

    private static void disconnect() {
        try {
            stmt.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
