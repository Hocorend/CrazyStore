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

    private Label totalCost;

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
            if(i==1)colConProduct[i].setMinWidth(100);
            if(i==2)colConProduct[i].setMinWidth(150);
            if(i==3)colConProduct[i].setMinWidth(100);
            gpProduct.getColumnConstraints().add(colConProduct[i]);
        }

        printCart(userEmailAddress,gpProduct);

        totalCost = new Label(" Amount: 0");

        gpMain.add(gpProduct,0,1);
        gpMain.add(new Label("Balance: "+ new StoreScene().balance(userEmailAddress)),0,0);

        Scene sceneCart = new Scene(gpMain,500,500);

        cartStage.setScene(sceneCart);
        cartStage.initModality(Modality.APPLICATION_MODAL);
        cartStage.showAndWait();


    }

    public void printCart(String userEmailAddress,GridPane gp){
        int countAvProd = 0;
        int count = 0;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProduct = connection.prepareStatement("SELECT nameProduct,costProduct FROM crazystore.Product " +
                     "WHERE idProduct IN (Select idProduct from crazystore.Cart " +
                     "where countProduct>0 and idUser=(select idUser from crazystore.User " +
                     "where emailAddress='"+userEmailAddress+"'));");
             PreparedStatement psCountAvailableProduct = connection.prepareStatement("SELECT count(*) FROM crazystore.Cart where countProduct>0;");
        ) {


            ResultSet rsCountAvProd = psCountAvailableProduct.executeQuery();
            while (rsCountAvProd.next()){ countAvProd = rsCountAvProd.getInt("count(*)");}
            System.out.println(countAvProd);
            RowConstraints[] rowConstraints = new RowConstraints[countAvProd];
            ResultSet rsProduct = psProduct.executeQuery();
            while (rsProduct.next()){

                rowConstraints[count] = new RowConstraints();
                gp.add(new Text(" "+rsProduct.getString("nameProduct")),0,count);
                gp.add(new Text(" "+rsProduct.getString("costProduct")),1,count);
                gp.add(getAmountCost(userEmailAddress,rsProduct.getString("nameProduct")), 2, count);
                gp.add(new NodeAddButtons(rsProduct.getString("nameProduct"),userEmailAddress,false).newNode(),3,count);

                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Text getAmountCost(String userEmailAddress, String nameProduct){
        Text amountCost = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement psCostProduct = connection.prepareStatement("Select costProduct FROM crazystore.Product " +
                    "where idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');");
            PreparedStatement psAmountInCart = connection.prepareStatement("SELECT countProduct FROM crazystore.Cart " +
                    "where idUser=(SELECT idUser from crazystore.User where emailAddress='"+userEmailAddress+"') " +
                    "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');");) {
            amountCost = new Text(" Amount: 0");

            ResultSet rsCostProduct = psCostProduct.executeQuery();
            ResultSet rsAmountInCart = psAmountInCart.executeQuery();

            int count=0;
            double cost=0;

            while (rsCostProduct.next()){
                cost = rsCostProduct.getDouble("costProduct");
            }
            while (rsAmountInCart.next()){
                count = rsAmountInCart.getInt("countProduct");
            }

            amountCost.setText(" Amount: "+(cost*count));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amountCost;
    }
}
