package store;

import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class StoreScene extends CrazyStore {

    private static final String url = "jdbc:mysql://192.168.0.102:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    private Label balance = new Label();
    private Label nameProduct = new Label();
    private Label descProduct = new Label();
    private Label costProduct = new Label();
    private Button toCart = new Button("Shopping cart");
    private MenuBar menuBar = new MenuBar();
    private Menu adminMenu = new Menu("Admin menu");
    private MenuItem addProduct = new MenuItem("Add product");
    private ScrollPane scrollPane;

    public void storeScene(String emailAddressUser) {
        Stage storeStage = new Stage();
        GridPane gridPane = new GridPane();
        storeStage.setTitle("Crazy Store");
        gridPane.setGridLinesVisible(true);

        // Основная компоновка

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

        //Компоновка хедера

        GridPane gpHeader = new GridPane();
        //gpHeader.setGridLinesVisible(true);
        ColumnConstraints[] colConHeader = new ColumnConstraints[4];
        RowConstraints[] rowConHeader = new RowConstraints[3];
        for (int i = 0; i < colConHeader.length; i++) {
            colConHeader[i]=new ColumnConstraints();
            if(i==0)colConHeader[i].setMinWidth(150);
            if(i==1)colConHeader[i].setMinWidth(900);
            if(i==2)colConHeader[i].setMinWidth(120);
            if(i==3)colConHeader[i].setMinWidth(250);
            gpHeader.getColumnConstraints().add(colConHeader[i]);
        }
        for (int i = 0; i < rowConHeader.length; i++) {
            rowConHeader[i] = new RowConstraints();
            if(i==2)rowConHeader[i].setPercentHeight(40);
            else if (i==0) rowConHeader[i].setPercentHeight(34);
            else rowConHeader[i].setPercentHeight(25);
            gpHeader.getRowConstraints().add(rowConHeader[i]);
        }
        gridPane.add(gpHeader,1,0);

        nameProduct.setText(" Product name");
        nameProduct.setFont(new Font(18));
        nameProduct.setStyle("-fx-text-fill:red");
        nameProduct.setStyle("-fx-font-weight: bold;-fx-text-fill:red");
        GridPane.setValignment(nameProduct, VPos.BOTTOM);

        descProduct.setText(" Description");
        descProduct.setFont(new Font(18));
        descProduct.setStyle("-fx-text-fill:red");
        descProduct.setStyle("-fx-font-weight: bold;-fx-text-fill:red");
        GridPane.setValignment(descProduct, VPos.BOTTOM);

        costProduct.setText(" Cost");
        costProduct.setFont(new Font(18));
        costProduct.setStyle("-fx-text-fill:red");
        costProduct.setStyle("-fx-font-weight: bold;-fx-text-fill:red");
        GridPane.setValignment(costProduct, VPos.BOTTOM);

        adminMenu.getItems().addAll(addProduct);
        adminMenu.setOnAction(actionEvent -> new AdminMenu().addProductInStore());
        menuBar.getMenus().add(adminMenu);

        toCart.setOnAction(actionEvent -> new CartScene().cartScene(emailAddressUser));

        gpHeader.add(nameProduct,0,2);
        gpHeader.add(descProduct,1,2);
        gpHeader.add(costProduct,2,2);
        gpHeader.add(toCart,3,2);

        if (isAdminMode(emailAddressUser)){
            gpHeader.add(menuBar,3,0);}

        //Компонока списка товаров

        GridPane gpProduct = new GridPane();
        gpProduct.setGridLinesVisible(true);
        ColumnConstraints[] colConProduct = new ColumnConstraints[4];
        for (int i = 0; i < colConProduct.length; i++) {
            colConProduct[i]=new ColumnConstraints();
            if(i==0)colConProduct[i].setMinWidth(150);
            if(i==1)colConProduct[i].setMinWidth(900);
            if(i==2)colConProduct[i].setMinWidth(120);
            if(i==3)colConProduct[i].setMinWidth(250);
            gpProduct.getColumnConstraints().add(colConProduct[i]);
        }

        printProduct(emailAddressUser,gpProduct);

        scrollPane = new ScrollPane(gpProduct);
        scrollPane.setPrefViewportHeight(800);
        scrollPane.setPannable(true);

        gridPane.add(scrollPane,1,1);

        Scene scene = new Scene(gridPane, 1600, 800);
        storeStage.setResizable(false);
        storeStage.setScene(scene);


        storeStage.show();
    }

    public void printProduct(String userName,GridPane gp){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProduct = connection.prepareStatement("SELECT nameProduct,descriptionProduct,costProduct FROM crazystore.Product WHERE countProduct!=0");
             PreparedStatement psCountAvailableProduct = connection.prepareStatement("SELECT count(*) FROM crazystore.Product where countProduct>0;");
             ) {

            int countAvProd = 0;
            int count = 0;

            ResultSet rsCountAvProd = psCountAvailableProduct.executeQuery();
            while (rsCountAvProd.next()){ countAvProd = rsCountAvProd.getInt("count(*)");}
            RowConstraints[] rowConstraints = new RowConstraints[countAvProd];
            ResultSet rsProduct = psProduct.executeQuery();
            while (rsProduct.next()){
                rowConstraints[count] = new RowConstraints();
                rowConstraints[count].setPrefHeight(100);
                gp.add(new Text(" "+rsProduct.getString("nameProduct")),0,count);
                gp.add(new Text(" "+rsProduct.getString("descriptionProduct")),1,count);
                gp.add(new Text(" "+rsProduct.getString("costProduct")),2,count);
                gp.add(new NodeAddButtons(rsProduct.getString("nameProduct"),userName).newNode(),3,count);

                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAdminMode(String userName){
        boolean adminMode = false;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psAdminMode = connection.prepareStatement("SELECT adminMode FROM crazystore.User  where emailAddress='" + userName + "';")) {

            ResultSet rsAdminMode = psAdminMode.executeQuery();

            while (rsAdminMode.next()){
                adminMode = rsAdminMode.getBoolean("adminMode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return adminMode;
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
            e.printStackTrace();
        } return balance;
    }
}
