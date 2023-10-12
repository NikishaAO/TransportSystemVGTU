package fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditDriverProfileWindow {
    public TextField passwordField;
    public TextField nameField;
    public TextField surnameField;
    public TextField medNumField;
    public TextField licenseNumField;
    public DatePicker birthDateField;
    public DatePicker medDateField;
    public Label loginLabel;

    public void initialize(){
        if(LoginWindow.currentManager !=null)
        loginLabel.setText(LoginWindow.currentDriver.getLogin());
        passwordField.setText(LoginWindow.currentDriver.getPassword());
        nameField.setText(LoginWindow.currentDriver.getName());
        surnameField.setText(LoginWindow.currentDriver.getSurname());
        medDateField.setValue(LoginWindow.currentDriver.getMedicalCertificateDate());
        birthDateField.setValue(LoginWindow.currentDriver.getBirthDate());
        medNumField.setText(LoginWindow.currentDriver.getMedicalCertificationNumber());
        licenseNumField.setText(LoginWindow.currentDriver.getDriverLicense());
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/DriverWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)passwordField.getScene().getWindow();
        stage.setTitle("Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void updateInfo(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "UPDATE `drivers` SET `password`=?,`name`=?,`surname`=?,`birth_date`=?,`med_date`=?,`med_num`=?,`lic_num`=? WHERE id = " + LoginWindow.currentDriver.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,passwordField.getText());
        preparedStatement.setString(2,nameField.getText());
        preparedStatement.setString(3,surnameField.getText());
        preparedStatement.setDate(4, Date.valueOf(birthDateField.getValue()));
        preparedStatement.setDate(5,Date.valueOf(medDateField.getValue()));
        preparedStatement.setString(6,medNumField.getText());
        preparedStatement.setString(7,licenseNumField.getText());
        preparedStatement.execute();

        DBMethods.disconnect(connection,preparedStatement);

    }
}
