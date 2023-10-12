package fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Driver;
import model.Manager;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class LoginWindow {
    @FXML
    public TextField loginField;
    @FXML
    public TextField passwordField;

    public static Manager currentManager;

    public static Driver currentDriver;

    public void login(ActionEvent actionEvent) throws IOException, ClassNotFoundException, SQLException {
        Connection conn = DBMethods.connectTotDB();

        String sql = "SELECT * FROM managers WHERE login = '" + loginField.getText() + "' AND password = '" + passwordField.getText() + "'";
        PreparedStatement statement = conn.prepareStatement(sql);

        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            currentManager = new Manager(rs.getInt("id"),rs.getString("login"), rs.getString("password"), rs.getString("name"),rs.getString("surname"), LocalDate.parse(rs.getString("birth_date")), rs.getString("email"), LocalDate.parse(rs.getString("emp_date")), (rs.getBoolean("is_admin")));
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/MainWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage)loginField.getScene().getWindow();
            stage.setTitle("Management System");
            stage.setScene(scene);
            stage.show();
        }
        else{
            sql = "SELECT * FROM drivers WHERE login = '" + loginField.getText() + "' AND password = '" + passwordField.getText() + "'";
            statement = conn.prepareStatement(sql);

            rs = statement.executeQuery(sql);
            if(rs.next()){
                currentDriver = new Driver(rs.getInt("id"),rs.getString("login"), rs.getString("password"), rs.getString("name"), rs.getString("surname"),LocalDate.parse(rs.getString("birth_date")), LocalDate.parse(rs.getString("med_date")), rs.getString("med_num"), rs.getString("lic_num"));
                FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/DriverWindow.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage)loginField.getScene().getWindow();
                stage.setTitle("Management System");
                stage.setScene(scene);
                stage.show();
                }
            else
                {
                    infoBox("Wrong username or password", "");
                }
            }
    }

    public static void infoBox(String infoMessage, String titleBar) {
        infoBox(infoMessage, titleBar, null);
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
