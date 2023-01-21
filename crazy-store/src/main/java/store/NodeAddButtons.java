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

    private String nameProduct;
    private String userName;

    NodeAddButtons(String nameProduct,String userName){
        this.nameProduct = nameProduct;
        this.userName = userName;
    }

    private int count = 0;

    public Node newNode(){
        Button plus = new Button("+");
        Button minus = new Button("-");

        Label countInCart = new Label(""+count);

        plus.setOnAction(actionEvent -> count++);
        minus.setOnAction(actionEvent -> {
            if(count>0)count--;
        });

        HBox hBox = new HBox(plus,minus,countInCart);
        return hBox;
    }
    public void plusProduct(){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psPlusProduct = connection.prepareStatement("SELECT nameProduct,descriptionProduct,costProduct FROM crazystore.Product WHERE countProduct!=0")) {

            psPlusProduct.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
