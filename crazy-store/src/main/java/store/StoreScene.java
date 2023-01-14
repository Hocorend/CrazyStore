package store;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.sql.*;

public class StoreScene extends CrazyStore {

    private static final String url = "jdbc:mysql://192.168.0.107:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    Label balance = new Label();

    public void storeScene(String userName) {
        Stage storeStage = new Stage();
        GridPane gridPane = new GridPane();
        storeStage.setTitle("Crazy Store");
        gridPane.setGridLinesVisible(true);

        ColumnConstraints[] columnConstraints = new ColumnConstraints[10];
        RowConstraints[] rowConstraints = new RowConstraints[10];

        for (int i = 0; i < 10; i++) {
            columnConstraints[i] = new ColumnConstraints();
            rowConstraints[i] = new RowConstraints();
            columnConstraints[i].setPercentWidth(100 / 10);
            rowConstraints[i].setPercentHeight(100 / 10);
            gridPane.getColumnConstraints().add(columnConstraints[i]);
            gridPane.getRowConstraints().add(rowConstraints[i]);
        }

        balance.setText("Your balance: "+balance(userName));
        gridPane.add(balance,9,0);


        Scene scene = new Scene(gridPane, 1600, 1000);
        storeStage.setScene(scene);

        storeStage.show();
    }

    public String balance(String userName) {
        String balance = "";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psBalance = connection.prepareStatement("SELECT balance FROM crazystore.User  where emailAddress='" + userName + "';")) {

            ResultSet rsBalance = psBalance.executeQuery();

            while (rsBalance.next()){
                balance = rsBalance.getString("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } return balance;
    }
}
