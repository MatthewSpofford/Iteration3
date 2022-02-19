package edu.wpi.GoldenGandaberundas.controllers.LaundryControllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

/** Controller class for template file. Template FXML file is templatetemplate.fxml */

// TODO in template FXML file, change manu bar to button bar. Fix SVG Icons spacing on buttons
// TODO possibly add logo above buttons on side panel

public class LaundryServiceController implements Initializable {

  @FXML private JFXCheckBox beddingBox;
  @FXML private JFXCheckBox gownBox;
  @FXML private JFXCheckBox towelBox;
  private boolean bedding = false;
  private boolean gown = false;
  private boolean towel = false;
  @FXML private SearchableComboBox<String> locationSearchBox;
  private String locations;
  @FXML private TextField idField;

  // NEED EMPID,

  @FXML private TableView laundryStockTable;
  @FXML private TableColumn<Laundry, Integer> laundryID;
  @FXML private TableColumn<Laundry, String> laundryName;
  @FXML private TableColumn<Laundry, String> description;
  @FXML private TableColumn<Laundry, Boolean> inStock;

  @FXML TableView laundryTable; // Table object
  @FXML private TableColumn<LaundryRequest, Integer> reqID;
  @FXML private TableColumn<LaundryRequest, String> nodeID;
  @FXML private TableColumn<LaundryRequest, Long> submittedTime;
  @FXML private TableColumn<LaundryRequest, Long> completedTime;
  @FXML private TableColumn<LaundryRequest, Integer> requesterID;
  @FXML private TableColumn<LaundryRequest, Integer> completerID;
  @FXML private TableColumn<LaundryRequest, String> status;
  @FXML private TableColumn<LaundryRequest, Integer> patientID;
  @FXML private TableColumn<LaundryRequest, Integer> laundryReqID;

  private RequestTable requestTableController = RequestTable.getInstance();
  private TableController menuTableController = LaundryTbl.getInstance();
  private TableController requestLaundryController = LaundryRequestTbl.getInstance();

  private TableController locationTableController = LocationTbl.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // menuTableController.loadBackup("TestCSVs/LaundryForTesting.csv");
    // requestTableController.loadBackup("TestCSVs/LaundryRequestsForTesting.csv");
    //    objectTableController = OrderedLaundryTbl.getInstance();
    // objectTableController.loadBackup("TestCSVs/OrderedLaundryForTesting.csv");

    // Factory for laundryStockTable
    laundryID.setCellValueFactory(new PropertyValueFactory<Laundry, Integer>("laundryID"));
    laundryName.setCellValueFactory(new PropertyValueFactory<Laundry, String>("laundryType"));
    description.setCellValueFactory(new PropertyValueFactory<Laundry, String>("description"));
    inStock.setCellValueFactory(new PropertyValueFactory<Laundry, Boolean>("inStock"));

    // Factory for laundryTable
    reqID.setCellValueFactory(new PropertyValueFactory<LaundryRequest, Integer>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<LaundryRequest, String>("locationID"));
    submittedTime.setCellValueFactory(new PropertyValueFactory<LaundryRequest, Long>("timeStart"));
    completedTime.setCellValueFactory(new PropertyValueFactory<LaundryRequest, Long>("timeEnd"));
    requesterID.setCellValueFactory(
        new PropertyValueFactory<LaundryRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<LaundryRequest, Integer>("empCompleter"));
    status.setCellValueFactory(new PropertyValueFactory<LaundryRequest, String>("requestStatus"));
    patientID.setCellValueFactory(new PropertyValueFactory<LaundryRequest, Integer>("patientID"));
    laundryReqID.setCellValueFactory(
        (new PropertyValueFactory<LaundryRequest, Integer>("laundryID")));

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationSearchBox.setItems(oList);

    // Lines relating to the initialization of choice boxes
    // Action listeners for towelBox choice box
    towelBox.setOnAction(
        (event) -> {
          if (towel == true) {
            towel = false;
          } else {
            towel = true;
          }
        });

    // Action listener for towelBox choice box
    gownBox.setOnAction(
        (event) -> {
          if (gown == true) {
            gown = false;
          } else {
            gown = true;
          }
        });

    // Action listener for BeddingBox choice box
    beddingBox.setOnAction(
        (event) -> {
          if (bedding == true) {
            bedding = false;
          } else {
            bedding = true;
          }
        });

    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          locations = selectedItem;
        });

    laundryTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    refresh();
  }

  void onEdit() {
    if (laundryTable.getSelectionModel().getSelectedItem() != null) {
      LaundryRequest selectedItem =
          (LaundryRequest) laundryTable.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editLaundryReqForm.fxml"));
        AnchorPane editForm = load.load();
        editLaundryReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  /** Function for populating the location choice box, called in initialize */
  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }

  /**
   * Makes a delete window to delete requests
   *
   * @throws IOException
   */
  public void makeDelete() throws IOException {
    Parent root = FXMLLoader.load(App.class.getResource("views/deleteLaundryReqForm.fxml"));
    Scene scene = new Scene(root);

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
  }

  public void submit() {
    int idCounter =
        requestTableController
            .readTable()
            .get(requestTableController.readTable().size() - 1)
            .getRequestID();
    int requesterID = Integer.parseInt(idField.getText());
    locations = locationSearchBox.getValue();

    try {
      if (towel == true) {
        LaundryRequest tlr =
            new LaundryRequest(idCounter, locations, requesterID, 123, 0, 0, "Submitted", "", 111);
        LaundryRequestTbl.getInstance().addEntry(tlr);
      }

      if (gown == true) {
        LaundryRequest tlr =
            new LaundryRequest(idCounter, locations, requesterID, 123, 0, 0, "Submitted", "", 333);
        LaundryRequestTbl.getInstance().addEntry(tlr);
      }

      if (bedding == true) {
        LaundryRequest tlr =
            new LaundryRequest(idCounter, locations, requesterID, 123, 0, 0, "Submitted", "", 222);
        LaundryRequestTbl.getInstance().addEntry(tlr);
      }
    } catch (Exception e) {
      System.out.println("Invalid input: " + e);
    }

    refresh();
  }

  public void refresh() {
    menuTableController = LaundryTbl.getInstance();
    requestTableController = RequestTable.getInstance();
    laundryStockTable.getItems().setAll(menuTableController.readTable());
    laundryTable.getItems().setAll(requestLaundryController.readTable());
  }

  @FXML
  public void backupLaundry() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Laundry File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      LaundryTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\laundryBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Requests File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      LaundryRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medLaundryRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBLaundry() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Laundry File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      LaundryTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Laundry Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      LaundryRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }
}
