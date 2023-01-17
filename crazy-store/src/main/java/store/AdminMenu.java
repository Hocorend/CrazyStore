package store;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class AdminMenu {

    public void addProductInStore(){
        Stage stage = new Stage();
        stage.setTitle("Product Addition");
        VBox vBox = new VBox();
        Label name = new Label("Name:");
        TextField nameTF = new TextField();
        Label desc = new Label("Description:");
        TextField descTF = new TextField();
        descTF.setMinHeight(100);
        Label cost = new Label("Cost:");
        TextField costTF = new TextField();

        vBox.getChildren().addAll(name,nameTF,desc,descTF,cost,costTF);

        Scene scene = new Scene(vBox,500,500);
        stage.setScene(scene);
        stage.showAndWait();

    }
}
