package server;

public class Start {
    public static void main(String[] args) {
        try {
            new Server();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
