package rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Enumeration;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException, SQLException {
        Context context = new InitialContext();

        Enumeration<NameClassPair> e = context.list("rmi://localhost/");
        while (e.hasMoreElements())
            System.out.println(e.nextElement().getName());

        Person person = (Person)context.lookup("rmi://localhost/person");
        String resutl = person.newPerson("Yana", "Lebedeva", "21");
        System.out.println(resutl);
    }
}