package repositories;

import dtos.DTOBase;
import repositories.interfaces.IRepository;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public abstract class Repository<T extends DTOBase> implements IRepository<T> {


    private final static String DBURL = "jdbc:mysql://localhost:3306/utp";
    private final static String DBUSER = "root";
    private final static String DBPASS = "rozciąganie";
    private final static String DBDRIVER = "com.mysql.cj.jdbc.Driver";
    protected static Connection _connection;

    @Override
    public Connection getConnection() throws SQLException {

        try {
            Class.forName(DBDRIVER).getDeclaredConstructor().newInstance();

        }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return DriverManager.getConnection(DBURL, DBUSER, DBPASS);
    }

    @Override
    public void beginTransaction() {
        if (_connection != null){
            System.out.println("Transaction already pending");
        }

        performQueryWithStatement("START TRANSACTION;");
    }

    @Override
    public void commitTransaction() {
        if (_connection != null){
            System.out.println("No connection");
        }

        try {
            performQueryWithStatement("COMMIT;");
            _connection.close();
            _connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollbackTransaction() {
        if (_connection == null) {
            System.out.println("No transaction to rollback");
            return;
        }

        try {
            Statement statement = _connection.createStatement();

            statement.executeQuery("rollback");

            statement.close();
        } catch (Exception e) {
            if (_connection != null)
                e.printStackTrace();
        }
    }

    protected void performQueryWithStatement(String query){
        try{
            if(_connection == null) _connection = getConnection();

            Statement statement = _connection.createStatement();
            statement.executeUpdate(query);
            statement.executeUpdate("Commit;");
            statement.close();

        }catch (SQLException e){
            if (_connection != null)
                e.printStackTrace();
        }
    }

    protected ResultSet performQueryWithPreparedStatement(String query){

        try{
            if(_connection == null) _connection = getConnection();

            PreparedStatement statement = _connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.executeQuery();
            return result;

        }catch (SQLException e){
            if (_connection != null)
                e.printStackTrace();
            return null;
        }


    }
}
