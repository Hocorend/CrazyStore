package store;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class StoreScene extends CrazyStore {

    private static final String url = "jdbc:mysql://192.168.0.107:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    Label balance = new Label();
    Label nameProduct = new Label();
    Label descProduct = new Label();
    Label costProduct = new Label();
    Button addToCart = new Button();

    public void storeScene(String userName) {
        Stage storeStage = new Stage();
        GridPane gridPane = new GridPane();
        storeStage.setTitle("Crazy Store");
        gridPane.setGridLinesVisible(true);

        ColumnConstraints[] columnConstraints = new ColumnConstraints[2];
        RowConstraints[] rowConstraints = new RowConstraints[2];

        for (int i = 0; i < 2; i++) {
            columnConstraints[i] = new ColumnConstraints();
            rowConstraints[i] = new RowConstraints();
            columnConstraints[0].setPercentWidth(10);
            rowConstraints[0].setPercentHeight(10);
            if(i>0){
                columnConstraints[i].setHgrow(Priority.ALWAYS);
                rowConstraints[i].setVgrow(Priority.ALWAYS);
            }
            gridPane.getColumnConstraints().add(columnConstraints[i]);
            gridPane.getRowConstraints().add(rowConstraints[i]);
        }
        GridPane gpProduct = new GridPane();
        gpProduct.setGridLinesVisible(true);
        ColumnConstraints[] colConProduct = new ColumnConstraints[4];
        RowConstraints[] rowConProduct = new RowConstraints[2];
        for (int i = 0; i < colConProduct.length; i++) {
            colConProduct[i]=new ColumnConstraints();
            colConProduct[i].setPercentWidth(30);
            if(i==1){
                colConProduct[i].setPercentWidth(80);
            }
            gpProduct.getColumnConstraints().add(colConProduct[i]);
        }
        for (int i = 0; i < rowConProduct.length; i++) {
            rowConProduct[i] = new RowConstraints();

            if(i!=0)rowConProduct[i].setPercentHeight(100);
            gpProduct.getRowConstraints().add(rowConProduct[i]);
        }

        nameProduct.setText("Product name");
        GridPane.setHalignment(nameProduct, HPos.CENTER);
        descProduct.setText("Description");
        GridPane.setHalignment(descProduct, HPos.CENTER);
        costProduct.setText("Cost");
        GridPane.setHalignment(costProduct, HPos.CENTER);

        gpProduct.add(nameProduct,0,0);
        gpProduct.add(descProduct,1,0);
        gpProduct.add(costProduct,2,0);

        VBox vBoxNP = new VBox();
        vBoxNP.setSpacing(5);
        gpProduct.add(vBoxNP,0,1);

        VBox vBoxDP = new VBox();
        vBoxDP.setSpacing(5);
        gpProduct.add(vBoxDP,1,1);

        VBox vBoxCP = new VBox();
        vBoxCP.setSpacing(5);
        gpProduct.add(vBoxCP,2,1);

        VBox vBoxButton = new VBox();
        gpProduct.add(vBoxButton,3,1);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProduct = connection.prepareStatement("SELECT nameProduct,descriptionProduct,costProduct FROM crazystore.Product")) {

            ResultSet rsProduct = psProduct.executeQuery();

            while (rsProduct.next()){
                vBoxNP.getChildren().add(new Label(" "+rsProduct.getString("nameProduct")));
                vBoxDP.getChildren().add(new Label(" "+rsProduct.getString("descriptionProduct")));
                vBoxCP.getChildren().add(new Label(" "+rsProduct.getString("costProduct")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        gridPane.add(gpProduct,1,1);

        Scene scene = new Scene(gridPane, 1600, 1000);
        storeStage.setScene(scene);

        storeStage.show();
    }


    //SELECT nameProduct,descriptionProduct,costProduct FROM crazystore.Product;

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
