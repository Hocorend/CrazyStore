package store;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class AdminMenu {

    private static final String url = "jdbc:mysql://192.168.0.107:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";
    private TextField nameTF = new TextField();
    private TextField descTF = new TextField();
    private TextField costTF = new TextField();
    private Button buttonAdd = new Button("Add");
    private Label result = new Label();

    public void addProductInStore(){
        Stage stage = new Stage();
        stage.setTitle("Product Addition");
        VBox vBox = new VBox();
        Label name = new Label("Name:");
        Label desc = new Label("Description:");
        descTF.setMinHeight(100);
        Label cost = new Label("Cost:");

        vBox.getChildren().addAll(name,nameTF,desc,descTF,cost,costTF,buttonAdd,new Separator(),result);

        buttonAdd.setOnAction(actionEvent -> addProduct());
        Scene scene = new Scene(vBox,500,500);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void addProduct(){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProductAvailability = connection.prepareStatement(
                     "SELECT count(*) FROM crazystore.Product where nameProduct='"+nameTF.getText()+"';")) {

            ResultSet rsAvailability = psProductAvailability.executeQuery();

            while (rsAvailability.next()){
                if(rsAvailability.getInt("count(*)")==0){
                    try{
                        PreparedStatement psProduct = connection.prepareStatement(
                            "Insert into crazystore.Product (nameProduct,descriptionProduct,costProduct)" +
                                    " values ('"+nameTF.getText()+"','"+descTF.getText()+"',"+Double.parseDouble(costTF.getText())+");");

                        psProduct.execute();
                    }catch (NumberFormatException e){
                        result.setText("Fill in all fields correctly");
                        continue;
                    }
                    result.setText("New product added");
                }else result.setText("Product exists");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
