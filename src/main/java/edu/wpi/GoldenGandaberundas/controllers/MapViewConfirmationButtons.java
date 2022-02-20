package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MapViewConfirmationButtons {
  @FXML private JFXButton noBtn;
  @FXML private JFXButton yesBtn;
  @FXML Text questionTitle;
  @FXML Text questionText;
  @FXML ImageView icon;
  private MapController map = null;
  private MedEquipment med = null;
  private Location location = null;

  private Boolean selection = null;

  public void initialize() {
    noBtn.setOnMouseReleased(
        e -> {
          selection = false;
          MedEquipmentTbl.getInstance().editEntry(med.getMedID(), "currLoc", location.getNodeID());
          map.setEquipment();
        });
    yesBtn.setOnMouseReleased(
        e -> {
          MedEquipRequest medEquipRequest =
              new MedEquipRequest(
                  RequestTable.getInstance()
                          .readTable()
                          .get(RequestTable.getInstance().readTable().size() - 1)
                          .getRequestID()
                      + 1,
                  location.getNodeID(),
                  CurrentUser.getUser().getEmpID(),
                  null,
                  100,
                  200,
                  123,
                  "Submitted",
                  "",
                  med.getMedID());
          System.out.println("NEW REQ: " + medEquipRequest.toString());
          MedEquipRequestTbl.getInstance().addEntry(medEquipRequest);
          map.refreshMap();
          selection = true;
        });
  }

  public void setMainController(MapController map, MedEquipment med, Location loc) {
    this.map = map;
    this.med = med;
    this.location = loc;
  }
}
