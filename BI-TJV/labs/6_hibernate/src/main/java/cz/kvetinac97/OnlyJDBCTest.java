package cz.kvetinac97;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class OnlyJDBCTest {

    private Connection connection;
    private Map<StatementType, PreparedStatement> statements;

    private void createConnection () throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:/Users/kvetinac97/FIT_CTU/ZS 2020-2021/BI-TJV/cv06/cv06.db", null, null);
    }

    private void prepareStatementTable () throws SQLException {
        statements = new HashMap<StatementType, PreparedStatement>() {{
            put( StatementType.INSERT, connection.prepareStatement("INSERT INTO `author` (`id`, `first_name`, `last_name`) VALUES (?, ?, ?)") );
            put( StatementType.UPDATE, connection.prepareStatement("UPDATE `author` SET `first_name` = ? AND `last_name` = ? WHERE `id` = ?") );
            put( StatementType.FIND_ALL, connection.prepareStatement("SELECT * FROM `author`") );
            put( StatementType.GET_ID, connection.prepareStatement("SELECT * FROM `author` WHERE `id` = ?") );
            put( StatementType.DELETE, connection.prepareStatement("DELETE FROM `author` WHERE `id` = ?") );
        }};
    }

    public boolean insert ( int id, String firstName, String lastName ) throws SQLException {
        PreparedStatement statement = statements.get(StatementType.INSERT);
        statement.setInt(1, id);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.execute();
        return statement.getUpdateCount() > 0;
    }

    public boolean update ( int id, String firstName, String lastName ) throws SQLException {
        PreparedStatement statement = statements.get(StatementType.UPDATE);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, id);
        statement.execute();
        return statement.getUpdateCount() > 0;
    }

    public boolean delete ( int id ) throws SQLException {
        PreparedStatement statement = statements.get(StatementType.DELETE);
        statement.setInt(1, id);
        statement.execute();
        return statement.getUpdateCount() > 0;
    }

    public void getByID ( int id ) throws SQLException {
        PreparedStatement statement = statements.get(StatementType.GET_ID);
        statement.setInt(1, id);

        ResultSet result = statement.executeQuery();
        if ( !result.next() )
            System.out.println("null");
        else
            System.out.println(result.getInt("id") + " " + result.getString("first_name") + " " +
                    result.getString("last_name"));
    }

    public void findAll () throws SQLException {
        PreparedStatement statement = statements.get(StatementType.FIND_ALL);
        ResultSet result = statement.executeQuery();

        while ( result.next() )
            System.out.println(result.getInt("id") + " " + result.getString("first_name") + " " +
                    result.getString("last_name"));
    }

    public void execute () {
        try {
            createConnection();
            prepareStatementTable();

            System.out.println(insert(1, "Josef", "Novák"));
            System.out.println(insert(2, "Palo", "Escobar"));
            System.out.println(insert(3, "John", "Cena"));
            System.out.println();

            findAll();
            getByID(1);
            System.out.println();

            System.out.println(update(2, "Palos", "Ecobar"));
            getByID(2);
            System.out.println();

            System.out.println(delete(1));
            System.out.println(delete(3));
            findAll();
            System.out.println();

            System.out.println(delete(2));
            findAll();
            System.out.println();

            connection.close();
        }
        catch ( SQLException exception) { exception.printStackTrace(); }
    }

    enum StatementType {
        INSERT,
        UPDATE,
        FIND_ALL,
        GET_ID,
        DELETE
    }

}
