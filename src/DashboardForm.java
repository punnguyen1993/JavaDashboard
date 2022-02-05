import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistationForm registationForm = new RegistationForm(DashboardForm.this);
                User user = registationForm.user;

                if(user != null){
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }


    private boolean connectToDatabse() {
        boolean hasRegisteredUsers = false;
        String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        String DB_URL = "jdbc:mysql://localhost/MyStore";// ở đây set time zone(?serverTimezone=UTC) phía sau
                                                                // MyStore, chưa tìm hiểu nên bỏ.
        String USERNAME = "root";
        String PASSWORD = "th!nhnguyen";

        try {
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS MyStore";
            statement.executeUpdate(sql);
            conn.close();
            statement.close();

            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS users(" +
                    "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(200) NOT NULL" +
                    "email VARCHAR(200) NOT NULL UNIQUE," +
                    "phone VARCHAR(200)," +
                    "address VARCHAR(200)," +
                    "password VARCHAR(200) NOT NULL" +
                    ")";
            statement.executeUpdate(sql);

            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users;");

            if(resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if(numUsers>0){
                    hasRegisteredUsers = true;
                }
            }
            conn.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    public static void main(String[] args) {
        DashboardForm dashboardForm = new DashboardForm();
    }
}
