package store;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.*;

public class NodeAddButtons {
    private static final String url = "jdbc:mysql://192.168.0.102:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    private final String nameProduct;
    private String emailAddressUser;
    private Label countInCart;

    NodeAddButtons(String nameProduct,String emailAddressUser){
        this.nameProduct = nameProduct;
        this.emailAddressUser = emailAddressUser;
    }

    private int count = 0;

    public Node newNode(){
        Button plus = new Button("+");
        Button minus = new Button("-");

        countInCart = new Label(""+count);

        plus.setOnAction(actionEvent -> plusProduct());
        minus.setOnAction(actionEvent -> minusProduct());

        setCount();
        HBox hBox = new HBox(plus,minus,countInCart);
        return hBox;
    }

    private void setCount() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psCountLabel = connection.prepareStatement("SELECT countProduct FROM crazystore.Cart " +
                     "where idUser=(SELECT idUser from crazystore.User where emailAddress='" + emailAddressUser + "')" +
                     "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='" + nameProduct + "');")) {

            ResultSet rsCountLabel = psCountLabel.executeQuery();

            while (rsCountLabel.next()) {
                count = rsCountLabel.getInt("countProduct");
            }

            countInCart.setText(String.valueOf(count));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void plusProduct(){

        String existProductInCart = "";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psPlusProduct = connection.prepareStatement(
                     "Select idProduct from crazystore.Cart" +
                             " where idUser=(SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"') and " +
                             "idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');")) {

            ResultSet rsPlus = psPlusProduct.executeQuery();
            while (rsPlus.next()){
                 existProductInCart = rsPlus.getString("idProduct");
            }

            if (existProductInCart!=""){

                PreparedStatement psExistToCart = connection.prepareStatement(
                "Update crazystore.Cart set countProduct=countProduct+1 " +
                        "where idUser=(SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"') " +
                        "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');");

                psExistToCart.execute();
            } else if (existProductInCart=="") {
                PreparedStatement psNewToCart = connection.prepareStatement(
                        "INSERT INTO crazystore.Cart (idUser,idProduct,countProduct) VALUES ((SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"'),(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"'),1);");
                psNewToCart.execute();
            }
            setCount();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void minusProduct(){

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psExistToCart = connection.prepareStatement(
                     "Update crazystore.Cart set countProduct=countProduct-1 " +
                             "where idUser=(SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"') " +
                             "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');")) {

            if (count==0){}
            else if (count>0){
                psExistToCart.execute();
            }
            setCount();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
