package store;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class LoginScene extends CrazyStore{
    private Driver driver;

    {
        try {
            driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String url = "jdbc:mysql://192.168.0.102:3308/crazystore";
    private static final String username = "root";
    private static final String password = "root";

    private Button enter = new Button();
    private Label emailLabel = new Label();
    private Label passwordLabel = new Label();
    private TextField emailField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Label loginError = new Label();
    public void loginScene(){
        Stage loginStage = new Stage();

        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setTitle("Login");
        GridPane gridPaneMenu = new GridPane();

        ColumnConstraints[] columnConstraints = new ColumnConstraints[5];
        RowConstraints[] rowConstraints = new RowConstraints[5];

        for (int i = 0; i < 5; i++) {
            columnConstraints[i] = new ColumnConstraints();
            rowConstraints[i] = new RowConstraints();
            gridPaneMenu.getColumnConstraints().add(columnConstraints[i]);
            gridPaneMenu.getRowConstraints().add(rowConstraints[i]);
        }

        rowConstraints[0].setPercentHeight(50);
        rowConstraints[4].setPercentHeight(50);
        rowConstraints[1].setPercentHeight(10);
        rowConstraints[2].setPercentHeight(10);
        rowConstraints[3].setPercentHeight(20);
        columnConstraints[0].setPercentWidth(20);
        columnConstraints[2].setPercentWidth(35);


        emailLabel.setText("Email address: ");
        gridPaneMenu.add(emailLabel,1,1);
        gridPaneMenu.add(emailField,2,1);

        passwordLabel.setText("Password: ");
        gridPaneMenu.add(passwordLabel,1,2);
        gridPaneMenu.add(passwordField,2,2);

        enter.setText("Enter");
        gridPaneMenu.add(enter,2,3);

        gridPaneMenu.add(loginError,2,4);

        enter.setOnAction(actionEvent -> {
            try(Connection connection = DriverManager.getConnection(url,username,password);
                PreparedStatement psEmail = connection.prepareStatement("SELECT emailAddress FROM crazystore.User where emailAddress='"+emailField.getText()+"';");
                PreparedStatement psPassword = connection.prepareStatement("SELECT password FROM crazystore.User  where emailAddress='"+emailField.getText()+"';")) {

                ResultSet rsEmail = psEmail.executeQuery();
                if(rsEmail.first()){
                    ResultSet rsPass = psPassword.executeQuery();
                    while (rsPass.next()){
                        String ps = rsPass.getString("password");
                        String psf = passwordField.getText();
                        if (ps.equals(psf)){
                            loginStage.close();
                            new StoreScene().storeScene(emailField.getText());
                        }else loginError.setText("Incorrect login or password");
                    }
                }else loginError.setText("Incorrect login or password");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        Scene scene = new Scene(gridPaneMenu,600,500);
        loginStage.setScene(scene);
        loginStage.setResizable(false);

        loginStage.showAndWait();
    }
}
