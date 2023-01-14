package store;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class StoreScene extends CrazyStore{

    public void storeScene(Stage stage) {
        GridPane gridPaneMenu = new GridPane();
        stage.setTitle("Crazy Store");

        LoginScene loginScene = new LoginScene();
        loginScene.loginScene();


        ColumnConstraints[] columnConstraints = new ColumnConstraints[20];
        RowConstraints[] rowConstraints = new RowConstraints[20];

        for (int i = 0; i < 20; i++) {
            columnConstraints[i] = new ColumnConstraints();
            rowConstraints[i] = new RowConstraints();
            columnConstraints[i].setPercentWidth(100 / 10);
            rowConstraints[i].setPercentHeight(100 / 10);
            gridPaneMenu.getColumnConstraints().add(columnConstraints[i]);
            gridPaneMenu.getRowConstraints().add(rowConstraints[i]);
        }


        Scene scene = new Scene(gridPaneMenu, 1600, 1000);
        stage.setScene(scene);

        stage.show();
    }
}
