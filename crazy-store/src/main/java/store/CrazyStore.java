package store;

import javafx.application.Application;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class CrazyStore extends Application {

    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        LoginScene loginScene = new LoginScene();
        loginScene.loginScene(stage);
    }
}
