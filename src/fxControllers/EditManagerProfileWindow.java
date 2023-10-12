package fxControllers;

import com.mysql.cj.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditManagerProfileWindow {
    public TextField passwordField;
    public Label loginLabel;
    public TextField nameField;
    public TextField surnameField;
    public TextField emailField;
    public DatePicker birthDateField;

    public void initialize() {
        if (LoginWindow.currentManager != null){
            loginLabel.setText(LoginWindow.currentManager.getLogin());
        passwordField.setText(LoginWindow.currentManager.getPassword());
        nameField.setText(LoginWindow.currentManager.getName());
        surnameField.setText(LoginWindow.currentManager.getSurname());
        birthDateField.setValue(LoginWindow.currentManager.getBirthDate());
        emailField.setText(LoginWindow.currentManager.getEmail());
    }
        else
            loginLabel.setText(LoginWindow.currentDriver.getLogin());
    }

    public void returnButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/DriverWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)passwordField.getScene().getWindow();
        stage.setTitle("Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void updatePersonalData(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "UPDATE `managers` SET `password`=?,`name`=?,`surname`=?,`birth_date`=?,`email`=? WHERE id =" + LoginWindow.currentManager.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,passwordField.getText());
        preparedStatement.setString(2,nameField.getText());
        preparedStatement.setString(3,surnameField.getText());
        preparedStatement.setString(4,String.valueOf(birthDateField.getValue()));
        preparedStatement.setString(5,emailField.getText());
        LoginWindow.currentManager.setPassword(passwordField.getText());
        LoginWindow.currentManager.setName(nameField.getText());
        LoginWindow.currentManager.setSurname(surnameField.getText());
        LoginWindow.currentManager.setBirthDate(birthDateField.getValue());
        LoginWindow.currentManager.setEmail(emailField.getText());

        preparedStatement.execute();
        DBMethods.disconnect(connection,preparedStatement);

    }
}
