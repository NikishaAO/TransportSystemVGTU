package fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Cargo;
import model.Delivery;
import model.Truck;
import model.TyreType;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class DriverWindow {
    public ListView driverDeliveriesList;
    public ListView checkpointList;
    public ListView resManagersList;
    public RadioButton longStopRadio;
    public TextField titleField;
    public ListView avaliableDeliveryList;
    public Label startLabel;
    public Label endLabel;
    public Label startLnLabel;
    public Label startLtLabel;
    public Label endLnLabel;
    public Label endLtLabel;
    public Label arrDateLabel;
    public Label depDateLabel;
    public Label cargoLabel;
    public ComboBox truckBox;
    public Label usernameLabel;

    public void initialize() throws SQLException, ClassNotFoundException {
        loadDriverDeliveries();
        loadAvailibleDeliveries();
        loadTrucks();
        usernameLabel.setText(LoginWindow.currentDriver.getLogin());
    }
    public void toForum(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/ForumWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)titleField.getScene().getWindow();
        stage.setTitle("Forum");
        stage.setScene(scene);
        stage.show();
    }

    public void takeDelivery(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "UPDATE deliveries SET in_process = 1, truck_id = ?, driver_id = ? WHERE id =" + ((Delivery)avaliableDeliveryList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,Integer.parseInt(truckBox.getSelectionModel().getSelectedItem().toString()));
        preparedStatement.setInt(2,LoginWindow.currentDriver.getId());
        preparedStatement.execute();
        loadDriverDeliveries();
        loadAvailibleDeliveries();
    }

    public void finishDelivery(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "UPDATE deliveries SET in_process = 0 WHERE id =" + ((Delivery)driverDeliveriesList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        DBMethods.disconnect(connection,preparedStatement);
        loadDriverDeliveries();
    }

    public void addCheckpoint(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "INSERT INTO `checkpoints`(`title`, `del_id`, `long_stop`, `date_arr`) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,titleField.getText());
        preparedStatement.setInt(2, ((Delivery)driverDeliveriesList.getSelectionModel().getSelectedItem()).getId());
        preparedStatement.setBoolean(3,longStopRadio.isSelected());
        preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
        preparedStatement.execute();

        DBMethods.disconnect(connection,preparedStatement);

        loadCheckpoints();
    }

    public void editProfileClick(ActionEvent actionEvent) {
    }

    public void logoutClick(ActionEvent actionEvent) throws IOException {
        LoginWindow.currentManager = null;
        LoginWindow.currentDriver = null;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)titleField.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void loadDriverDeliveries() throws SQLException, ClassNotFoundException {
        Delivery delivery = null;
        driverDeliveriesList.getItems().clear();
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries WHERE in_process = 1 AND driver_id =" + LoginWindow.currentDriver.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            delivery = new Delivery(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getLong(4),
                    rs.getLong(5),rs.getLong(6),rs.getLong(7),rs.getInt(8),rs.getDate(9).toLocalDate(),
                    rs.getDate(10).toLocalDate(),rs.getInt(11),rs.getInt(12),rs.getBoolean(13));
            driverDeliveriesList.getItems().add(delivery);
        }
        DBMethods.disconnect(connection,preparedStatement);
    }
    public void loadAvailibleDeliveries() throws SQLException, ClassNotFoundException {
        Delivery delivery = null;
        avaliableDeliveryList.getItems().clear();
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries WHERE driver_id IS null";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            delivery = new Delivery(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getLong(4),
                    rs.getLong(5),rs.getLong(6),rs.getLong(7),rs.getInt(8),rs.getDate(9).toLocalDate(),
                    rs.getDate(10).toLocalDate(),rs.getInt(11),rs.getInt(12),rs.getBoolean(13));
            avaliableDeliveryList.getItems().add(delivery);
        }
        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadRespManagers() throws SQLException, ClassNotFoundException {
        resManagersList.getItems().clear();

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries_managers WHERE delivery_id =" + ((Delivery)driverDeliveriesList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            sql = "SELECT * FROM managers WHERE id =" + rs.getString("manager_id");
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs2 = preparedStatement.executeQuery();
            while(rs2.next())
            resManagersList.getItems().add(rs2.getString("name") + " " + rs2.getString("surname")+", " + rs2.getString("email"));
        }
        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadCheckpoints() throws SQLException, ClassNotFoundException {
        checkpointList.getItems().clear();

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM checkpoints WHERE del_id =" + ((Delivery)driverDeliveriesList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        String longInfo = "";
        while(rs.next()) {
            if (rs.getBoolean("long_stop"))
                longInfo = ", long";
            checkpointList.getItems().add(rs.getString("title") + ", " + rs.getString("date_arr") + longInfo);
        }
    }

    public void selectDriverDeliveries(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        loadRespManagers();
        loadCheckpoints();

        Delivery delivery = (Delivery) driverDeliveriesList.getSelectionModel().getSelectedItem();
        startLabel.setText("From:" + delivery.getStartPoint());
        endLabel.setText("To:" + delivery.getEndPoint());
        startLnLabel.setText("Ln:"+String.valueOf(delivery.getStartLn()));
        startLtLabel.setText("Lt:"+String.valueOf(delivery.getStartLt()));
        endLnLabel.setText("Ln:"+String.valueOf(delivery.getEndLn()));
        endLtLabel.setText("Lt:"+String.valueOf(delivery.getEndLt()));
        depDateLabel.setText(String.valueOf(delivery.getDateOfDeparture()));
        arrDateLabel.setText(String.valueOf(delivery.getDateofArrival()));
        
        String cargoInfo = "";
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM cargos WHERE id=" + delivery.getCargo();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
            cargoInfo = rs.getString("title") + ", " + rs.getString("weigth") + " kg, " +
                    rs.getString("cargo_type") + "\n"+ rs.getString("description");

        cargoLabel.setText(cargoInfo);

        DBMethods.disconnect(connection,preparedStatement);
    }

    public void selectAvDeliveries(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {
        Delivery delivery = (Delivery) avaliableDeliveryList.getSelectionModel().getSelectedItem();
        startLabel.setText("From:" + delivery.getStartPoint());
        endLabel.setText("To:" + delivery.getEndPoint());
        startLnLabel.setText("Ln:"+String.valueOf(delivery.getStartLn()));
        startLtLabel.setText("Lt:"+String.valueOf(delivery.getStartLt()));
        endLnLabel.setText("Ln:"+String.valueOf(delivery.getEndLn()));
        endLtLabel.setText("Lt:"+String.valueOf(delivery.getEndLt()));
        depDateLabel.setText(String.valueOf(delivery.getDateOfDeparture()));
        arrDateLabel.setText(String.valueOf(delivery.getDateofArrival()));

        String cargoInfo = "";
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM cargos WHERE id=" + delivery.getCargo();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
            cargoInfo = rs.getString("title") + ", " + rs.getString("weigth") + " kg, " +
                    rs.getString("cargo_type") + "\n"+ rs.getString("description");

        cargoLabel.setText(cargoInfo);

        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadTrucks() throws ClassNotFoundException, SQLException {
        Truck truck = null;

        truckBox.getItems().clear();
        Connection conn = DBMethods.connectTotDB();

        String sql = "SELECT * FROM trucks";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            truck = new Truck(rs.getInt("id"), rs.getString("name"), rs.getString("model"), rs.getInt("year"), rs.getDouble("odometer"), rs.getDouble("fuel_cap"), TyreType.valueOf(rs.getString("tyre_type")));
            truckBox.getItems().add(rs.getString("id"));
        }

        DBMethods.disconnect(conn,preparedStatement);
    }

    public void editInfoClick(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/EditDriverProfileWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)titleField.getScene().getWindow();
        stage.setTitle("Account");
        stage.setScene(scene);
        stage.show();
    }
}
