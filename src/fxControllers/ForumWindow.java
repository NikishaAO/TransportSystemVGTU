package fxControllers;

import com.mysql.cj.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Comment;
import model.Forum;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ForumWindow {
    public ListView commentList;
    public ListView replyList;

    public TextField commentField;
    public ListView forumList;
    public TextField titleField;

    ArrayList<Forum> forumArrayList;

    ArrayList<Forum> forumsArray = new ArrayList<>();
    ArrayList<Comment> commentArray = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAll();
    }
    public void newForum(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        String sql = "INSERT INTO `forum`(`title`) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,titleField.getText());
        preparedStatement.execute();

        DBMethods.disconnect(connection,preparedStatement);

        loadAll();
    }

    public void newComment(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {


        Connection connection = DBMethods.connectTotDB();

        if(LoginWindow.currentManager == null) {
            String sql = "INSERT INTO `comments`(`text`, `forum_id`, `driver_id`, `date_created`, `date_mod`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,commentField.getText());
            preparedStatement.setString(2,String.valueOf(forumsArray.get(forumList.getSelectionModel().getSelectedIndex()).getId()));
            preparedStatement.setString(3,String.valueOf(LoginWindow.currentDriver.getId()));
            preparedStatement.setString(4,String.valueOf(LocalDate.now()));
            preparedStatement.setString(5,String.valueOf(LocalDate.of(1,1,1)));
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);
        }else{
            String sql = "INSERT INTO `comments`(`text`, `forum_id`, `manager_id`, `date_created`, `date_mod`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,commentField.getText());
            preparedStatement.setString(2,String.valueOf(forumsArray.get(forumList.getSelectionModel().getSelectedIndex()).getId()));
            preparedStatement.setString(3,String.valueOf(LoginWindow.currentManager.getId()));
            preparedStatement.setString(4,String.valueOf(LocalDate.now()));
            preparedStatement.setString(5,String.valueOf(LocalDate.of(1,1,1)));
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);
        }

        commentField.setText("");
        loadAll();
    }

    public void newReply(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();

        String replyCom = commentList.getSelectionModel().getSelectedItem().toString();

        String sql = "SELECT * FROM comments WHERE text = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,replyCom);
        ResultSet rep = preparedStatement.executeQuery();


        if(LoginWindow.currentDriver != null) {
            sql = "INSERT INTO `comments`(`text`, `forum_id`, `driver_id`, `repl_text`, `date_created`, `date_mod`) VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,commentField.getText());
            preparedStatement.setString(2,String.valueOf(forumsArray.get(forumList.getSelectionModel().getSelectedIndex()).getId()));
            preparedStatement.setString(3,String.valueOf(LoginWindow.currentDriver.getId()));
            if(rep.next())
            preparedStatement.setString(4,rep.getString("text"));
            preparedStatement.setString(5,String.valueOf(LocalDate.now()));
            preparedStatement.setString(6,String.valueOf(LocalDate.of(1,1,1)));
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);
        }else {
            sql = "INSERT INTO `comments`(`text`, `forum_id`, `manager_id`, `repl_text`,`date_created`, `date_mod`) VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, commentField.getText());
            preparedStatement.setString(2, String.valueOf(forumsArray.get(forumList.getSelectionModel().getSelectedIndex()).getId()));
            preparedStatement.setString(3, String.valueOf(LoginWindow.currentManager.getId()));
            if(rep.next())
            preparedStatement.setString(4,rep.getString("text"));
            preparedStatement.setString(5, String.valueOf(LocalDate.now()));
            preparedStatement.setString(6, String.valueOf(LocalDate.of(1, 1, 1)));
            preparedStatement.execute();
            DBMethods.disconnect(connection, preparedStatement);

            loadComments();
        }
    }

    public void returnClick(ActionEvent actionEvent) throws IOException {
        if(LoginWindow.currentManager != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/MainWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.setTitle("Management System");
            stage.setScene(scene);
            stage.show();
        }
        else{FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("../view/DriverWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.setTitle("Management System");
            stage.setScene(scene);
            stage.show();}
    }

    public void loadAll() throws ClassNotFoundException, SQLException {
        commentList.getItems().clear();
        forumList.getItems().clear();
        replyList.getItems().clear();
       Connection connection = DBMethods.connectTotDB();
       String sql = "SELECT * FROM forum";
       PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            forumsArray.add(new Forum(rs.getInt("id"), rs.getString("title")));
            forumList.getItems().add(rs.getString("title"));
        }


        DBMethods.disconnect(connection,preparedStatement);
    }

    public void loadComments() throws ClassNotFoundException, SQLException {
        commentList.getItems().clear();
        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM comments WHERE forum_id = "+ forumsArray.get(forumList.getSelectionModel().getSelectedIndex()).getId();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next())
            commentList.getItems().add(rs.getString("text"));

        DBMethods.disconnect(connection,preparedStatement);
    }

    public void selectForum(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        commentList.getItems().clear();
        loadComments();

    }

    public void editComment(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Connection connection = DBMethods.connectTotDB();
        if(LoginWindow.currentDriver != null) {
            String sql = "UPDATE `comments` SET `text`=?,`date_mod`=? WHERE text = ? AND driver_id = " + LoginWindow.currentDriver.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,commentField.getText());
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(3,(commentList.getSelectionModel().getSelectedItem().toString()));
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);

        }
        else {
            String sql = "UPDATE `comments` SET `text`=?,`date_mod`=? WHERE text = ? AND manager_id = " + LoginWindow.currentManager.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,commentField.getText());
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(3,(commentList.getSelectionModel().getSelectedItem().toString()));
            preparedStatement.execute();
            DBMethods.disconnect(connection,preparedStatement);

        }
        loadComments();
    }

    public void selectComment(MouseEvent mouseEvent) throws ClassNotFoundException, SQLException {
        replyList.getItems().clear();

        Connection connection = DBMethods.connectTotDB();
        String sql = "SELECT * FROM comments WHERE repl_text = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,(commentList.getSelectionModel().getSelectedItem().toString()));
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){replyList.getItems().add(rs.getString("text"));}
    }
}
