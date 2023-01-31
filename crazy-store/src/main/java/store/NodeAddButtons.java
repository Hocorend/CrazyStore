package store;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
    private Label totalAvailable;
    private boolean displayTotal;

    NodeAddButtons(String nameProduct,String emailAddressUser,boolean displayTotal){
        this.nameProduct = nameProduct;
        this.emailAddressUser = emailAddressUser;
        this.displayTotal = displayTotal;
    }

    private int count = 0;

    public Node newNode(){
        Button plus = new Button("+");
        Button minus = new Button("-");
        totalAvailable = new Label(" Total available: ");
        totalAvailable.setPrefWidth(170);

        countInCart = new Label(""+count);

        plus.setOnAction(actionEvent -> plusProduct());
        minus.setOnAction(actionEvent -> minusProduct());

        setCount();
        HBox hBox = new HBox();
        if(displayTotal){
            hBox.getChildren().add(totalAvailable);
        }
        hBox.getChildren().addAll(plus,minus,countInCart);
        return hBox;
    }

    private void setCount() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psCountLabel = connection.prepareStatement("SELECT countProduct FROM crazystore.Cart " +
                     "where idUser=(SELECT idUser from crazystore.User where emailAddress='" + emailAddressUser + "')" +
                     "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='" + nameProduct + "');");
             PreparedStatement psTotalAvailable = connection.prepareStatement("select countProduct from crazystore.Product "+
                     "where idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"')");
             ) {

            ResultSet rsTotalAvailable = psTotalAvailable.executeQuery();

            while (rsTotalAvailable.next()){
                totalAvailable.setText(" Total available: "+ rsTotalAvailable.getInt("countProduct"));
            }

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
                             "idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');");
             PreparedStatement psExistToCart = connection.prepareStatement(
                     "Update crazystore.Cart set countProduct=countProduct+1 " +
                             "where idUser=(SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"') " +
                             "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"');");
             PreparedStatement psNewToCart = connection.prepareStatement(
                     "INSERT INTO crazystore.Cart (idUser,idProduct,countProduct) VALUES ((SELECT idUser from crazystore.User " +
                             "where emailAddress='"+emailAddressUser+"'),(SELECT idProduct from crazystore.Product " +
                             "WHERE nameProduct='"+nameProduct+"'),1);");
             PreparedStatement psProductAvailable = connection.prepareStatement("Select (select countProduct from crazystore.Cart " +
                     "where idUser=(SELECT idUser from crazystore.User where emailAddress='"+emailAddressUser+"') " +
                     "AND idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"')) < " +
                     "(select countProduct from crazystore.Product " +
                     "where idProduct=(SELECT idProduct from crazystore.Product WHERE nameProduct='"+nameProduct+"')) AS Available;");
             ) {

            ResultSet rsPlus = psPlusProduct.executeQuery();
            while (rsPlus.next()){
                 existProductInCart = rsPlus.getString("idProduct");
            }

            if (existProductInCart!=""){
                ResultSet rsAvailable = psProductAvailable.executeQuery();
                int available = 0;
                while (rsAvailable.next()){
                    available = rsAvailable.getInt("Available");
                }
                if(available==1)psExistToCart.execute();

            } else if (existProductInCart=="") {

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
