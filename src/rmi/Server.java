package rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public static void main(String[] args) throws RemoteException, NamingException, MalformedURLException, AlreadyBoundException {
        Context context = new InitialContext();
        context.bind("rmi://localhost/person", new PersonImpl());
    }
}

interface Person extends Remote {
    String newPerson(String name, String surname, String age) throws RemoteException, SQLException;
}

class PersonImpl extends UnicastRemoteObject implements Person {

    protected PersonImpl() throws RemoteException {
    }

    private Connection connection;
    private void Connect() throws SQLException {
        String db = "jdbc:mysql://localhost/rmi";
        String user = "root";
        String pass = "root";
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            connection = (Connection) DriverManager.getConnection(db, user, pass);
            if (connection != null) {
                System.out.println("Success connection");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String newPerson(String name, String surname, String age) throws RemoteException, SQLException {
        Connect();
        String data = "";
        String sql = "INSERT INTO user VALUES (null, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, age);
            int num = ps.executeUpdate();
            if (num>0) {
                data = "Success";
            }
            else {
                data = "Error";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
}