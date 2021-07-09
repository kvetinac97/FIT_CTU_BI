package kvetinac97;

import java.sql.*;

public class Main {

    public static void main ( String[] args ) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:pjv.db", "pjv", "Pjv2020!");
        Statement statement = connection.createStatement();

        int res = statement.executeUpdate("create table if not exists testpjv\n" +
                "(\n" +
                "\tid integer\n" +
                "\t\tconstraint testpjv_pk\n" +
                "\t\t\tprimary key autoincrement,\n" +
                "\tdescription text\n" +
                ");\n" +
                "\n");

        int res2 = statement.executeUpdate("DELETE FROM testpjv WHERE id % 2 = 0");

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO testpjv (description) VALUES (?)");
        preparedStatement.setString(1, "test: " + System.currentTimeMillis());
        int res3 = preparedStatement.executeUpdate();

        ResultSet set = statement.executeQuery("SELECT * FROM testpjv");
        while ( set.next() ) {
            System.out.println( "ID: " + set.getInt("id") + " desc: " + set.getString("description") );
        }

        System.out.println("Res: " + res + " " + res2 + " " + res3);

        preparedStatement.close();
        statement.close();
        connection.close();
    }

}
