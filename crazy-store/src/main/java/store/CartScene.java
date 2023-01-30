package store;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class CartScene {

    private static final String url = "jdbc:mysql://192.168.0.102:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    public void cartScene(String userEmailAddress){
        Stage cartStage = new Stage();
        GridPane gpMain = new GridPane();
        gpMain.setGridLinesVisible(true);

        ColumnConstraints cCMain = new ColumnConstraints();
        RowConstraints[] rCMain = new RowConstraints[3];
        rCMain[0]=new RowConstraints();
        rCMain[0].setPrefHeight(50);
        rCMain[1]=new RowConstraints();
        rCMain[1].setPrefHeight(450);
        rCMain[2]=new RowConstraints(50);


        gpMain.getColumnConstraints().add(cCMain);
        gpMain.getRowConstraints().addAll(rCMain[0],rCMain[1],rCMain[2]);

        GridPane gpProduct = new GridPane();
        gpProduct.setGridLinesVisible(true);
        ColumnConstraints[] colConProduct = new ColumnConstraints[4];
        for (int i = 0; i < colConProduct.length; i++) {
            colConProduct[i]=new ColumnConstraints();
            if(i==0)colConProduct[i].setMinWidth(150);
            if(i==1)colConProduct[i].setMinWidth(150);
            if(i==2)colConProduct[i].setMinWidth(100);
            if(i==3)colConProduct[i].setMinWidth(100);
            gpProduct.getColumnConstraints().add(colConProduct[i]);
        }

        printCart(userEmailAddress,gpProduct);

        gpMain.add(gpProduct,0,1);
        gpMain.add(new Label("Balance: "+ new StoreScene().balance(userEmailAddress)),0,0);

        Scene sceneCart = new Scene(gpMain,500,500);

        cartStage.setScene(sceneCart);
        cartStage.initModality(Modality.APPLICATION_MODAL);
        cartStage.showAndWait();


    }

    public void printCart(String userEmailAddress,GridPane gp){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProduct = connection.prepareStatement("SELECT nameProduct,costProduct FROM crazystore.Product " +
                     "WHERE idProduct IN (Select idProduct from crazystore.Cart " +
                     "where countProduct>0 and idUser=(select idUser from crazystore.User " +
                     "where emailAddress='"+userEmailAddress+"'));");
             PreparedStatement psCountAvailableProduct = connection.prepareStatement("SELECT count(*) FROM crazystore.Cart where countProduct>0;");
        ) {

            int countAvProd = 0;
            int count = 0;

            ResultSet rsCountAvProd = psCountAvailableProduct.executeQuery();
            while (rsCountAvProd.next()){ countAvProd = rsCountAvProd.getInt("count(*)");}
            System.out.println(countAvProd);
            RowConstraints[] rowConstraints = new RowConstraints[countAvProd];
            ResultSet rsProduct = psProduct.executeQuery();
            while (rsProduct.next()){
                rowConstraints[count] = new RowConstraints();
                gp.add(new Text(" "+rsProduct.getString("nameProduct")),0,count);
                gp.add(new Text(" "+rsProduct.getString("costProduct")),1,count);
                gp.add(new NodeAddButtons(rsProduct.getString("nameProduct"),userEmailAddress).newNode(),3,count);

                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
