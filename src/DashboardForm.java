import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JButton btnRegister;
    private JPanel dashboardPanel;
    private JLabel lbAdmin;

    public DashboardForm(){
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 450));
        setSize(700, 500);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabse();
        if(hasRegisteredUsers){
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if(user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else{
                dispose();
            }
        }else{
            //Show Registration form
            RegistationForm registationForm = new RegistationForm(this);
            User user = registationForm.user;

            if(user != null){
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }else{
                dispose();
            }
        }
    }

    private boolean connectToDatabse() {
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhoset/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "th!nhnguyen93";
        try{
            //First, connect to MYSQL and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            statement.close();
            conn.close();

            //Second, connect to the tdatabse and create the table "users" if not created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users(" +
                    "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(200) NOT NULL," +
                    "email VARCHAR(200) NOT NULL UNIQUE," +
                    "phone VARCHAR(200)," +
                    "address VARCHAR(200)," +
                    "password VARCHAR(200) NOT NULL" +
                    ")";
            statement.executeUpdate(sql);

            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if(resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if(numUsers > 0){
                    hasRegisteredUsers = true;
                }
            }
            statement.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    public static void main(String[] args) {
        DashboardForm dashboardForm = new DashboardForm();
    }
}
