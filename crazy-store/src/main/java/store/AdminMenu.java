package store;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class AdminMenu {

    private static final String url = "jdbc:mysql://192.168.0.102:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";
    private TextField nameTF = new TextField();
    private TextField descTF = new TextField();
    private TextField costTF = new TextField();
    private Button buttonAdd = new Button("Add");
    private TextField countTF = new TextField();
    private Label result = new Label();

    public void addProductInStore(){
        Stage stage = new Stage();
        stage.setTitle("Product Addition");
        VBox vBox = new VBox();
        Label name = new Label("Name:");
        Label desc = new Label("Description:");
        descTF.setMinHeight(100);
        Label cost = new Label("Cost:");
        CheckBox newProduct = new CheckBox("New product");
        Label count = new Label("Count");


        vBox.getChildren().addAll(name,nameTF,new Separator(),count,countTF,newProduct,buttonAdd,result);
        newProduct.setOnAction(actionEvent -> {
            if (newProduct.isSelected()) {
                vBox.getChildren().addAll(desc,descTF,cost,costTF);
            }else vBox.getChildren().removeAll(desc,descTF,cost,costTF);
        });
        buttonAdd.setOnAction(actionEvent ->{
            if(newProduct.isSelected()) addNewProduct();
            else addProduct();}
        );
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
                if(rsAvailability.getInt("count(*)")!=0){
                    try(PreparedStatement psProduct = connection.prepareStatement(
                            "Update crazystore.Product set countProduct=countProduct+"+Integer.parseInt(countTF.getText()) +
                                    " where nameProduct='"+nameTF.getText()+"';"))
                    {
                        psProduct.execute();

                    }catch (NumberFormatException e){
                        result.setText("Fill in all fields correctly");
                        continue;
                    }
                    result.setText("Added "+countTF.getText()+ " product quantities");
                }else result.setText("Product not exists");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewProduct(){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psProductAvailability = connection.prepareStatement(
                     "SELECT count(*) FROM crazystore.Product where nameProduct='"+nameTF.getText()+"';")) {

            ResultSet rsAvailability = psProductAvailability.executeQuery();

            while (rsAvailability.next()){
                if(rsAvailability.getInt("count(*)")==0){
                    try(PreparedStatement psProduct = connection.prepareStatement(
                            "Insert into crazystore.Product (nameProduct,descriptionProduct,costProduct,countProduct)" +
                                    " values ('"+nameTF.getText()+"','"+descTF.getText()+"',"+Double.parseDouble(costTF.getText())+","+Integer.parseInt(countTF.getText())+");"))
                    {
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
