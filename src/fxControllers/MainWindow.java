package fxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;
import model.Driver;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class MainWindow {
    @FXML
    public ListView<Truck> trucksList;
    @FXML
    public TextField makeField;
    @FXML
    public TextField modelField;
    @FXML
    public TextField yearField;
    @FXML
    public TextField odometerField;
    @FXML
    public TextField fuelCapacityField;
    @FXML
    public ComboBox<TyreType> tyreTypeField;
    @FXML
    public ListView cargoList;
    @FXML
    public TextField titleField;
    @FXML
    public TextField weightField;
    @FXML
    public ComboBox cargoTypeBox;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField customerField;
    public RadioButton driverRadio;
    public RadioButton managerRadio;
    public TextField loginField;
    public TextField nameField;
    public TextField surnameField;
    public DatePicker birthDateField;
    public DatePicker medCertDateField;
    public TextField medCertNumField;
    public TextField licenceNumberField;
    public TextField emailField;
    public DatePicker empDateField;
    public RadioButton adminRadio;
    public ListView driverList;
    public ListView managerList;
    public TextField passwordField;
    public Label currentUsername;
    public TextField startPointField;
    public TextField startLnField;
    public TextField startLtField;
    public TextField endPointField;
    public TextField endLnField;
    public TextField endLtField;
    public ComboBox driverBox;
    public ComboBox truckBox;
    public DatePicker departureDateField;
    public DatePicker arrivalDateField;
    public ComboBox cargoBox;
    public ComboBox managerBox;
    public Button addManagerButton;
    public RadioButton processRadio;
    public ListView deliveryList;
    public ListView responsibleManagersList;
    public Button deleteManagerButton;
    public ListView checkpointList;
    public Button registerButton;
    public Button updateUserButton;
    public Button deleteButton;


    public void initialize() throws SQLException, ClassNotFoundException {
        tyreTypeField.getItems().setAll(TyreType.values());
        cargoTypeBox.getItems().setAll(CargoType.values());
        driverRadio.setSelected(true);
        emailField.setDisable(true);
        empDateField.setDisable(true);
        adminRadio.setDisable(true);
        medCertDateField.setDisable(false);
        medCertNumField.setDisable(false);
        licenceNumberField.setDisable(false);
        loadDrivers();
        if(LoginWindow.currentManager != null)
        currentUsername.setText(LoginWindow.currentManager.getLogin());
        else
            currentUsername.setText(LoginWindow.currentDriver.getLogin());
        loadTrucks();
        loadCargos();
        loadDeliveries();
        loadManagerBox();
        driverBox.getItems().add(null);
        truckBox.getItems().add(null);

        if(!LoginWindow.currentManager.getIsAdmin()){
            registerButton.setDisable(true);
            updateUserButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    public void createTruck(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            Truck truck = new Truck(makeField.getText(), modelField.getText(), Integer.parseInt(yearField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(fuelCapacityField.getText()), tyreTypeField.getValue());
            Connection connection = DBMethods.connectTotDB();
            String sql = "INSERT INTO `trucks`(`name`, `model`, `year`, `odometer`, `fuel_cap`, `tyre_type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,truck.getName());
            preparedStatement.setString(2,truck.getModel());
            preparedStatement.setString(3,String.valueOf(truck.getYear()));
            preparedStatement.setString(4,String.valueOf(truck.getOdometer()));
            preparedStatement.setString(5,String.valueOf(truck.getFuelTankCapacity()));
            preparedStatement.setString(6,String.valueOf(truck.getTyreType()));

            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);
            loadTrucks();
        } catch (Exception e) {
            infoBox("One of the fields is empty or have wrong format", "");
        }
    }

    public void loadDrivers() throws ClassNotFoundException, SQLException {
        Driver driver = null;
        driverList.getItems().clear();
        Connection conn = DBMethods.connectTotDB();

        String sql = "SELECT * FROM drivers";
        PreparedStatement statement = conn.prepareStatement(sql);

        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()) {
            driver = new Driver(rs.getInt("id"),rs.getString("login"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), LocalDate.parse(rs.getString("birth_date")), LocalDate.parse(rs.getString("med_date")), rs.getString("med_num"), rs.getString("lic_num"));
            driverList.getItems().add(driver);
            driverBox.getItems().add(driver.getId());
        }
        managerList.getItems().clear();
    }
    public void loadManagers() throws ClassNotFoundException, SQLException {
        Manager manager = null;
        managerList.getItems().clear();
        Connection conn = DBMethods.connectTotDB();


        String sql = "SELECT * FROM managers";
        PreparedStatement statement = conn.prepareStatement(sql);

        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()) {
            manager = new Manager(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), LocalDate.parse(rs.getString("birth_date")), rs.getString("email"), LocalDate.parse(rs.getString("emp_date")), Integer.parseInt(rs.getString("is_admin")) == 1);
            managerList.getItems().add(manager);
        }

        DBMethods.disconnect(conn,statement);
        driverList.getItems().clear();

    }

    public void loadManagerBox() throws SQLException, ClassNotFoundException {
        Manager manager = null;
        managerBox.getItems().clear();
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM managers";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
            managerBox.getItems().add(rs.getString(1));

        DBMethods.disconnect(connection,preparedStatement);

    }

    public void loadTrucks() throws ClassNotFoundException, SQLException {
        Truck truck = null;
        trucksList.getItems().clear();
        truckBox.getItems().clear();
        Connection conn = DBMethods.connectTotDB();

        String sql = "SELECT * FROM trucks";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            truck = new Truck(rs.getInt("id"), rs.getString("name"), rs.getString("model"), rs.getInt("year"), rs.getDouble("odometer"), rs.getDouble("fuel_cap"), TyreType.valueOf(rs.getString("tyre_type")));
            trucksList.getItems().add(truck);
            truckBox.getItems().add(rs.getString("id"));
        }

        DBMethods.disconnect(conn,preparedStatement);
    }

    public void loadCargos() throws SQLException, ClassNotFoundException {
        Cargo cargo = null;
        cargoList.getItems().clear();
        cargoBox.getItems().clear();
        Connection conn = DBMethods.connectTotDB();
        String sql = "SELECT * FROM cargos";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            cargo = new Cargo(rs.getInt("id"),rs.getString("title"),rs.getDouble("weigth"),CargoType.valueOf(rs.getString("cargo_type")),rs.getString("description"),rs.getString("customer"),LocalDate.parse(rs.getString("date_created")), LocalDate.parse(rs.getString("date_upd")));
            cargoList.getItems().add(cargo);
            cargoBox.getItems().add(rs.getString("id"));
        }

        DBMethods.disconnect(conn,preparedStatement);

    }

    public void loadDeliveries() throws ClassNotFoundException, SQLException {
        Delivery delivery = null;
        deliveryList.getItems().clear();

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            delivery = new Delivery(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getLong(4),
                    rs.getLong(5),rs.getLong(6),rs.getLong(7),rs.getInt(8),rs.getDate(9).toLocalDate(),
                    rs.getDate(10).toLocalDate(),rs.getInt(11),rs.getInt(12),rs.getBoolean(13));
            deliveryList.getItems().add(delivery);
        }
        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadResManagers() throws ClassNotFoundException, SQLException {
        responsibleManagersList.getItems().clear();

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries_managers WHERE delivery_id =" + ((Delivery)deliveryList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
            responsibleManagersList.getItems().add(rs.getString(2));

        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadCheckpoints() throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String longInfo = "";
        String sql = "SELECT * FROM checkpoints WHERE del_id =" + ((Delivery)deliveryList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            if(rs.getBoolean("long_stop"))
                longInfo = ", long";
            checkpointList.getItems().add(rs.getString("title") + ", " + rs.getString("date_arr") + longInfo);

        }
    }

    public void updateTruck(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if(tyreTypeField.getSelectionModel().getSelectedItem() != null) {
            try {
                Truck selectedTruck = (Truck) trucksList.getSelectionModel().getSelectedItem();
                Truck truck = new Truck(selectedTruck.getId(), makeField.getText(), modelField.getText(), Integer.parseInt(yearField.getText()), Double.parseDouble(odometerField.getText()), Double.parseDouble(fuelCapacityField.getText()), tyreTypeField.getValue());
                Connection connection = DBMethods.connectTotDB();
                String sql = "UPDATE `trucks` SET `name`=?,`model`=?,`year`=?,`odometer`=?,`fuel_cap`=?,`tyre_type`=? WHERE `id` =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, truck.getName());
                preparedStatement.setString(2, truck.getModel());
                preparedStatement.setString(3, String.valueOf(truck.getYear()));
                preparedStatement.setString(4, String.valueOf(truck.getOdometer()));
                preparedStatement.setString(5, String.valueOf(truck.getFuelTankCapacity()));
                preparedStatement.setString(6, String.valueOf(truck.getTyreType()));
                preparedStatement.setString(7, String.valueOf(truck.getId()));

                preparedStatement.execute();
                DBMethods.disconnect(connection, preparedStatement);
                loadTrucks();
            } catch (Exception e) {
                infoBox("One of the fields is empty or have wrong format input", "");
            }
        }else
            infoBox("Please, select Tyre type.","");
    }

    public void deleteTruck(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Truck truck = (Truck)trucksList.getSelectionModel().getSelectedItem();
        Connection connection = DBMethods.connectTotDB();
        String sql = "DELETE FROM `trucks` WHERE id="+truck.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        DBMethods.disconnect(connection,preparedStatement);

        loadTrucks();
    }


    public void comboBoxAction(ActionEvent actionEvent) {
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

    public void getSelectedTruck(MouseEvent mouseEvent) {
        Truck truck = trucksList.getSelectionModel().getSelectedItem();
        makeField.setText(truck.getName());
        modelField.setText(truck.getModel());
        yearField.setText(String.valueOf(truck.getYear()));
        odometerField.setText(String.valueOf(truck.getOdometer()));
        fuelCapacityField.setText(String.valueOf(truck.getFuelTankCapacity()));
        tyreTypeField.setValue(truck.getTyreType());
    }

    public void createCargo(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (cargoTypeBox.getSelectionModel().getSelectedItem() != null){
            try {
                Cargo cargo = new Cargo(titleField.getText(), Double.parseDouble(weightField.getText()), (CargoType) cargoTypeBox.getValue(), descriptionField.getText(), customerField.getText());
                Connection connection = DBMethods.connectTotDB();
                String sql = "INSERT INTO `cargos`(`title`, `weigth`, `cargo_type`, `description`, `customer`, `date_created`, `date_upd`) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, cargo.getTitle());
                preparedStatement.setString(2, String.valueOf(cargo.getWeight()));
                preparedStatement.setString(3, String.valueOf(cargo.getCargoType()));
                preparedStatement.setString(4, cargo.getDescription());
                preparedStatement.setString(5, cargo.getCustomer());
                preparedStatement.setString(6, String.valueOf(LocalDate.now()));
                preparedStatement.setString(7, String.valueOf(LocalDate.of(1, 1, 1)));

                preparedStatement.execute();
                DBMethods.disconnect(connection, preparedStatement);
            } catch (Exception e) {
                infoBox("One of the fields is empty or have wrong format", "");
            }
        loadCargos();
    }else
        infoBox("Please, select cargo type.","");

    }

    public void updateCargo(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if(cargoTypeBox.getSelectionModel().getSelectedItem() !=null) {
            try {
                Cargo selectedCargo = (Cargo) cargoList.getSelectionModel().getSelectedItem();
                Cargo cargo = new Cargo(selectedCargo.getId(), titleField.getText(), Double.parseDouble(weightField.getText()), (CargoType) cargoTypeBox.getValue(), descriptionField.getText(), customerField.getText(), selectedCargo.getDateCreated(), selectedCargo.getDateUpdated());
                Connection connection = DBMethods.connectTotDB();
                String sql = "UPDATE `cargos` SET `title`=?,`weigth`=?,`cargo_type`=?,`description`=?,`customer`=?,`date_created`=?,`date_upd`=? WHERE id =?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, cargo.getTitle());
                preparedStatement.setString(2, String.valueOf(cargo.getWeight()));
                preparedStatement.setString(3, String.valueOf(cargo.getCargoType()));
                preparedStatement.setString(4, cargo.getDescription());
                preparedStatement.setString(5, cargo.getCustomer());
                preparedStatement.setString(6, String.valueOf(LocalDate.now()));
                preparedStatement.setString(7, String.valueOf(LocalDate.of(1, 1, 1)));
                preparedStatement.setString(8, String.valueOf(cargo.getId()));

                preparedStatement.execute();
                DBMethods.disconnect(connection, preparedStatement);
                loadCargos();

            } catch (Exception e) {
                infoBox("One of the fields is empty or have wrong format", "");
            }
        }
        else
            infoBox("Please, select cargo type.","");
    }

    public void deleteCargo(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Cargo cargo = (Cargo)cargoList.getSelectionModel().getSelectedItem();
        Connection connection = DBMethods.connectTotDB();
        String sql = "DELETE FROM `cargos` WHERE id="+cargo.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        DBMethods.disconnect(connection,preparedStatement);

        loadCargos();
    }

    public void getCargoSelectedValue(MouseEvent mouseEvent) {
        Cargo cargo = (Cargo) cargoList.getSelectionModel().getSelectedItem();
        titleField.setText(cargo.getTitle());
        weightField.setText(String.valueOf(cargo.getWeight()));
        descriptionField.setText(cargo.getDescription());
        customerField.setText(cargo.getCustomer());
        cargoTypeBox.setValue(cargo.getCargoType());
    }

    public void selectDriver(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        managerRadio.setSelected(false);
        emailField.setDisable(true);
        empDateField.setDisable(true);
        adminRadio.setDisable(true);
        medCertDateField.setDisable(false);
        medCertNumField.setDisable(false);
        licenceNumberField.setDisable(false);
        loadDrivers();
    }

    public void selectManager(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        driverRadio.setSelected(false);
        emailField.setDisable(false);
        empDateField.setDisable(false);
        adminRadio.setDisable(false);
        medCertDateField.setDisable(true);
        medCertNumField.setDisable(true);
        licenceNumberField.setDisable(true);
        loadManagers();
    }

    public void createUser(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        try {
            if (driverRadio.isSelected()) {
                Driver driver = new Driver(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), LocalDate.parse(medCertDateField.getValue().toString()), medCertNumField.getText(), licenceNumberField.getText());
                String sql = "INSERT INTO drivers(`login`,`password`,`name`,`surname`,`birth_date`,`med_date`,`med_num`,`lic_num`) VALUES(?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, driver.getLogin());
                preparedStatement.setString(2, driver.getPassword());
                preparedStatement.setString(3, driver.getName());
                preparedStatement.setString(4, driver.getSurname());
                preparedStatement.setString(5, String.valueOf(Date.valueOf(driver.getBirthDate())));
                preparedStatement.setString(6, String.valueOf(Date.valueOf(driver.getMedicalCertificateDate())));
                preparedStatement.setString(7, driver.getMedicalCertificationNumber());
                preparedStatement.setString(8, driver.getDriverLicense());

                preparedStatement.execute();
                DBMethods.disconnect(connection, preparedStatement);

                loadDrivers();
            } else {
                Manager manager = new Manager(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), emailField.getText(), LocalDate.parse(empDateField.getValue().toString()), adminRadio.isSelected());
                String sql = "INSERT INTO managers(`login`,`password`,`name`,`surname`,`birth_date`,`email`,`emp_date`,`is_admin`) VALUES(?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, manager.getLogin());
                preparedStatement.setString(2, manager.getPassword());
                preparedStatement.setString(3, manager.getName());
                preparedStatement.setString(4, manager.getSurname());
                preparedStatement.setString(5, String.valueOf(Date.valueOf(manager.getBirthDate())));
                preparedStatement.setString(6, manager.getEmail());
                preparedStatement.setString(7, String.valueOf(manager.getEmploymentDate()));
                preparedStatement.setString(8, String.valueOf(manager.getIsAdmin().compareTo(false)));

                preparedStatement.execute();
                DBMethods.disconnect(connection, preparedStatement);

                loadManagers();
            }
        }catch(Exception e){infoBox("One of the fields is empty or has wrong format", "");}

    }

    public void updateUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
    try {
        if (driverRadio.isSelected()) {
            Driver driver = new Driver(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), LocalDate.parse(medCertDateField.getValue().toString()), medCertNumField.getText(), licenceNumberField.getText());
            String sql = "UPDATE `drivers` SET `login`=?,`password`=?,`name`=?,`surname`=?,`birth_date`=?,`med_date`=?,`med_num`=?,`lic_num`=? WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, driver.getLogin());
            preparedStatement.setString(2, driver.getPassword());
            preparedStatement.setString(3, driver.getName());
            preparedStatement.setString(4, driver.getSurname());
            preparedStatement.setString(5, String.valueOf(Date.valueOf(driver.getBirthDate())));
            preparedStatement.setString(6, String.valueOf(Date.valueOf(driver.getMedicalCertificateDate())));
            preparedStatement.setString(7, driver.getMedicalCertificationNumber());
            preparedStatement.setString(8, driver.getDriverLicense());
            preparedStatement.setString(9, driver.getLogin());


            preparedStatement.execute();
            DBMethods.disconnect(connection, preparedStatement);

            loadDrivers();
        } else {
            Manager manager = new Manager(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), LocalDate.parse(birthDateField.getValue().toString()), emailField.getText(), LocalDate.parse(empDateField.getValue().toString()), adminRadio.isSelected());
            String sql = "UPDATE `managers` SET `login`=?,`password`=?,`name`=?,`surname`=?,`birth_date`=?,`email`=?,`emp_date`=?,`is_admin`=? WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, manager.getLogin());
            preparedStatement.setString(2, manager.getPassword());
            preparedStatement.setString(3, manager.getName());
            preparedStatement.setString(4, manager.getSurname());
            preparedStatement.setString(5, String.valueOf(Date.valueOf(manager.getBirthDate())));
            preparedStatement.setString(6, manager.getEmail());
            preparedStatement.setString(7, String.valueOf(manager.getEmploymentDate()));
            preparedStatement.setString(8, String.valueOf(manager.getIsAdmin().compareTo(false)));
            preparedStatement.setString(9, manager.getLogin());


            preparedStatement.execute();
            DBMethods.disconnect(connection, preparedStatement);

            loadManagers();
        }
    }
    catch (Exception e){infoBox("One of the fields is empty or has wrong format","");}
    }

    public void deleteUser(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        if(managerRadio.isSelected()){
            Manager manager = (Manager)managerList.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM `managers` WHERE `id` ="+manager.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            DBMethods.disconnect(connection, preparedStatement);
            loadManagers();
        }
        else{
            Driver driver = (Driver)driverList.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM `drivers` WHERE `id` ="+ driver.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            DBMethods.disconnect(connection, preparedStatement);
            loadDrivers();
        }
    }

    public void logoutClick(ActionEvent actionEvent) throws IOException {
        LoginWindow.currentManager = null;
        LoginWindow.currentDriver = null;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/LoginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setTitle("Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void editProfClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/EditManagerProfileWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setTitle("Edit personal data");
        stage.setScene(scene);
        stage.show();
    }

    public void getSelectedDriver(MouseEvent mouseEvent) {
        Driver driver = (Driver)driverList.getSelectionModel().getSelectedItem();
        loginField.setText(driver.getLogin());
        passwordField.setText(driver.getPassword());
        nameField.setText(driver.getName());
        surnameField.setText(driver.getSurname());
        birthDateField.setValue(driver.getBirthDate());
        medCertDateField.setValue(driver.getMedicalCertificateDate());
        medCertNumField.setText(driver.getMedicalCertificationNumber());
        licenceNumberField.setText(driver.getDriverLicense());
    }

    public void selectListManager(MouseEvent mouseEvent) {
        Manager manager = (Manager)managerList.getSelectionModel().getSelectedItem();
        loginField.setText(manager.getLogin());
        passwordField.setText(manager.getPassword());
        nameField.setText(manager.getName());
        surnameField.setText(manager.getSurname());
        birthDateField.setValue(manager.getBirthDate());
        emailField.setText(manager.getEmail());
        empDateField.setValue(manager.getEmploymentDate());
        adminRadio.setSelected(manager.getIsAdmin());
    }

    public void toForum(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/ForumWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setTitle("Forum");
        stage.setScene(scene);
        stage.show();
    }

    public void createDeliveryClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {


        Connection connection = DBMethods.connectTotDB();
        PreparedStatement preparedStatement = null;
        String sql;
        Delivery delivery = null;
        if (driverBox.getSelectionModel().getSelectedItem() != null) {
            delivery = new Delivery(startPointField.getText(), endPointField.getText(), Long.valueOf(startLnField.getText()),
                    Long.valueOf(startLtField.getText()), Long.valueOf(endLnField.getText()), Long.valueOf(endLtField.getText()), Integer.parseInt(driverBox.getValue().toString()),
                    departureDateField.getValue(), arrivalDateField.getValue(), Integer.valueOf(truckBox.getValue().toString()), Integer.valueOf(cargoBox.getValue().toString()), processRadio.isSelected());

            sql = "INSERT INTO `deliveries`(`start`, `end`, `start_ln`, `start_lt`, `end_ln`, `end_lt`, `driver_id`, `dep_date`, `arr_date`, `truck_id`, `cargo_id`, `in_process`, `date_created`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, delivery.getStartPoint());
            preparedStatement.setString(2, delivery.getEndPoint());
            preparedStatement.setLong(3, delivery.getStartLn());
            preparedStatement.setLong(4, delivery.getStartLt());
            preparedStatement.setLong(5, delivery.getEndLn());
            preparedStatement.setLong(6, delivery.getEndLt());
            preparedStatement.setInt(7, delivery.getDriver());
            preparedStatement.setDate(8, Date.valueOf(delivery.getDateOfDeparture()));
            preparedStatement.setDate(9, Date.valueOf(delivery.getDateofArrival()));
            preparedStatement.setInt(10, delivery.getTruck());
            preparedStatement.setInt(11, delivery.getCargo());
            preparedStatement.setBoolean(12, processRadio.isSelected());
            preparedStatement.setDate(13, Date.valueOf(LocalDate.now()));
        }
        else {
            delivery = new Delivery(startPointField.getText(), endPointField.getText(), Long.valueOf(startLnField.getText()),
                    Long.valueOf(startLtField.getText()), Long.valueOf(endLnField.getText()), Long.valueOf(endLtField.getText()),
                    departureDateField.getValue(), arrivalDateField.getValue(), Integer.valueOf(cargoBox.getValue().toString()), processRadio.isSelected());

            sql = "INSERT INTO `deliveries`(`start`, `end`, `start_ln`, `start_lt`, `end_ln`, `end_lt`,  `dep_date`, `arr_date`, `cargo_id`, `in_process`, `date_created`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, delivery.getStartPoint());
            preparedStatement.setString(2, delivery.getEndPoint());
            preparedStatement.setLong(3, delivery.getStartLn());
            preparedStatement.setLong(4, delivery.getStartLt());
            preparedStatement.setLong(5, delivery.getEndLn());
            preparedStatement.setLong(6, delivery.getEndLt());
            preparedStatement.setDate(7, Date.valueOf(delivery.getDateOfDeparture()));
            preparedStatement.setDate(8, Date.valueOf(delivery.getDateofArrival()));
            preparedStatement.setInt(9, delivery.getCargo());
            preparedStatement.setBoolean(10, processRadio.isSelected());
            preparedStatement.setDate(11, Date.valueOf(LocalDate.now()));
        }

        preparedStatement.execute();

        sql = "SELECT * FROM deliveries WHERE dep_date =? AND cargo_id=?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, Date.valueOf(delivery.getDateOfDeparture()));
        preparedStatement.setInt(2, delivery.getCargo());

        ResultSet rs = preparedStatement.executeQuery();


        sql = "INSERT INTO `deliveries_managers`(`manager_id`, `delivery_id`) VALUES (?,?)";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, LoginWindow.currentManager.getId());
        if (rs.next())
            preparedStatement.setInt(2, rs.getInt(1));

        preparedStatement.execute();

        DBMethods.disconnect(connection, preparedStatement);

        loadDeliveries();
    }

    public void updateDeliveryClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Delivery delivery = new Delivery(startPointField.getText(),endPointField.getText(),Long.valueOf(startLnField.getText()),
                Long.valueOf(startLtField.getText()),Long.valueOf(endLnField.getText()),Long.valueOf(endLtField.getText()), Integer.parseInt(truckBox.getValue().toString()),
                departureDateField.getValue(),arrivalDateField.getValue(),Integer.valueOf(truckBox.getValue().toString()),Integer.valueOf(cargoBox.getValue().toString()),processRadio.isSelected());

        Connection connection = DBMethods.connectTotDB();
        System.out.println(((Delivery) deliveryList.getSelectionModel().getSelectedItem()).getId());
        String sql = "UPDATE `deliveries` SET `start`=?,`end`=?,`start_ln`=?,`start_lt`=?,`end_ln`=?,`end_lt`=?,`driver_id`=?," +
                "`dep_date`=?,`arr_date`=?,`truck_id`=?,`cargo_id`=?,`in_process`=?,`date_upd`=? WHERE id =" + ((Delivery) deliveryList.getSelectionModel().getSelectedItem()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,delivery.getStartPoint());
        preparedStatement.setString(2,delivery.getEndPoint());
        preparedStatement.setLong(3,delivery.getStartLn());
        preparedStatement.setLong(4,delivery.getStartLt());
        preparedStatement.setLong(5,delivery.getEndLn());
        preparedStatement.setLong(6,delivery.getEndLt());
        preparedStatement.setInt(7,delivery.getDriver());
        preparedStatement.setDate(8,Date.valueOf(delivery.getDateOfDeparture()));
        preparedStatement.setDate(9,Date.valueOf(delivery.getDateofArrival()));
        preparedStatement.setInt(10,delivery.getTruck());
        preparedStatement.setInt(11,delivery.getCargo());
        preparedStatement.setBoolean(12,processRadio.isSelected());
        preparedStatement.setDate(13,Date.valueOf(LocalDate.now()));

        preparedStatement.execute();

        DBMethods.disconnect(connection,preparedStatement);

        loadDeliveries();
    }

    public void deleteDeliveryClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {

        if(!((Delivery)deliveryList.getSelectionModel().getSelectedItem()).isInProcess()) {
            Connection connection = DBMethods.connectTotDB();
            String sql = "DELETE FROM deliveries WHERE id =" + ((Delivery) deliveryList.getSelectionModel().getSelectedItem()).getId();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);

        }
        else
            infoBox("You can not delete the delivery that in process","");

        loadDeliveries();
    }

    public void addManagerClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "INSERT INTO `deliveries_managers`(`manager_id`, `delivery_id`) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,managerBox.getSelectionModel().getSelectedItem().toString());
        preparedStatement.setInt(2,((Delivery)deliveryList.getSelectionModel().getSelectedItem()).getId());
        preparedStatement.execute();

        DBMethods.disconnect(connection,preparedStatement);
        loadResManagers();
    }

    public void getSelectedDelivery(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        Delivery delivery = (Delivery) deliveryList.getSelectionModel().getSelectedItem();
        responsibleManagersList.getItems().clear();
        checkpointList.getItems().clear();

        startPointField.setText(delivery.getStartPoint());
        endPointField.setText(delivery.getEndPoint());
        startLnField.setText(String.valueOf(delivery.getStartLn()));
        startLtField.setText(String.valueOf(delivery.getStartLt()));
        endLnField.setText(String.valueOf(delivery.getEndLn()));
        endLtField.setText(String.valueOf(delivery.getEndLt()));
        driverBox.setValue(delivery.getDriver());
        truckBox.setValue(delivery.getTruck());
        cargoBox.setValue(delivery.getCargo());
        processRadio.setSelected(delivery.isInProcess());
        arrivalDateField.setValue(delivery.getDateofArrival());
        departureDateField.setValue(delivery.getDateOfDeparture());

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM deliveries_managers WHERE delivery_id = "+delivery.getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
            responsibleManagersList.getItems().add(rs.getString("manager_id"));

        loadCheckpoints();
        DBMethods.disconnect(connection,preparedStatement);
    }


    public void deleteManagerClick(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "DELETE FROM deliveries_managers WHERE manager_id =? AND delivery_id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,responsibleManagersList.getSelectionModel().getSelectedItem().toString());
        preparedStatement.setInt(2,((Delivery)deliveryList.getSelectionModel().getSelectedItem()).getId());
        preparedStatement.execute();
        DBMethods.disconnect(connection,preparedStatement);

        loadResManagers();
    }

    public void filterDeliveries(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        deliveryList.getItems().clear();
        Delivery delivery = null;
        Connection connection = DBMethods.connectTotDB();
        PreparedStatement preparedStatement = null;
        if (departureDateField.getValue() == null && arrivalDateField.getValue() == null) {
            String sql = "SELECT * FROM deliveries WHERE in_process =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, processRadio.isSelected());
        } else if (departureDateField.getValue() == null) {
            String sql = "SELECT * FROM deliveries WHERE arr_date =? AND in_process =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(arrivalDateField.getValue()));
            preparedStatement.setBoolean(2, processRadio.isSelected());
        } else if (arrivalDateField.getValue() == null) {
            String sql = "SELECT * FROM deliveries WHERE dep_date =? AND in_process =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(departureDateField.getValue()));
            preparedStatement.setBoolean(2, processRadio.isSelected());

        } else {
            String sql = "SELECT * FROM deliveries WHERE dep_date =? AND arr_date =? AND in_process =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(departureDateField.getValue()));
            preparedStatement.setDate(2, Date.valueOf(arrivalDateField.getValue()));
            preparedStatement.setBoolean(3, processRadio.isSelected());
        }
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            delivery = new Delivery(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4),
                    rs.getLong(5), rs.getLong(6), rs.getLong(7), rs.getInt(8), rs.getDate(9).toLocalDate(),
                    rs.getDate(10).toLocalDate(), rs.getInt(11), rs.getInt(12), rs.getBoolean(13));
            deliveryList.getItems().add(delivery);
        }

        DBMethods.disconnect(connection, preparedStatement);
    }
}
