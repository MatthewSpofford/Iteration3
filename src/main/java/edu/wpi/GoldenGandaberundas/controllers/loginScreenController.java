package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class loginScreenController {
  @FXML AnchorPane anchor;
  @FXML TextField userField;
  @FXML TextField passField;
  @FXML JFXButton loginBtn;
  @FXML ImageView logoImg;
  private TableController empTable = EmployeeTbl.getInstance();
  private TableController credTable = CredentialsTbl.getInstance();

  public loginScreenController() throws SQLException {}

  @FXML
  public void login() throws IOException {
    // logoImg.getParent().toFront();

    System.out.println("LOGGING IN");
    Integer userID = null;
    try {
      userID = Integer.parseInt(userField.getText());
    } catch (NumberFormatException n) {
      if (!userField.getText().trim().toLowerCase(Locale.ROOT).equals("admin")) {
        loginBtn.setText("Please Enter Valid ID");
      } else {
        userID = 000;
      }
    }
    String password = passField.getText();
    if (credTable.entryExists(userID)) {
      if (((Credential) credTable.getEntry(userID)).checkPassword(password)) {
        CurrentUser.setUser(EmployeeTbl.getInstance().getEntry(userID));

        TranslateTransition moveLogo = new TranslateTransition();
        moveLogo.setToX(loginBtn.localToScene(loginBtn.getBoundsInLocal()).getCenterX());
        moveLogo.setByZ(2);
        moveLogo.setDuration(Duration.millis(1500));
        moveLogo.setNode(logoImg);
        moveLogo.play();

        moveLogo.setOnFinished(event -> {
          Stage stage = (Stage) loginBtn.getScene().getWindow();
          try {
            stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/mainTwo.fxml"))));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });



      } else {
        loginBtn.setText("Invalid Login");
      }
    } else {
      loginBtn.setText("Invalid Login");
    }
  }
}
