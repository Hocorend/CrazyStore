package store;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CartScene extends StoreScene{

    public void cartScene(){
        Stage cartStage = new Stage();
        GridPane gpMain = new GridPane();
        gpMain.setGridLinesVisible(true);

        ColumnConstraints cCMain = new ColumnConstraints();
        RowConstraints[] rCMain = new RowConstraints[2];
        rCMain[0]=new RowConstraints();
        rCMain[1]=new RowConstraints();

        gpMain.getColumnConstraints().add(cCMain);
        gpMain.getRowConstraints().addAll(rCMain[0],rCMain[1]);

        Scene sceneCart = new Scene(gpMain,500,500);

        cartStage.setScene(sceneCart);
        cartStage.initModality(Modality.APPLICATION_MODAL);
        cartStage.showAndWait();
    }
}
