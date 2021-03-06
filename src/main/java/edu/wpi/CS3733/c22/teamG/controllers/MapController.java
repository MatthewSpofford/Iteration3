package edu.wpi.CS3733.c22.teamG.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import edu.wpi.CS3733.c22.teamG.Main;
import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.componentObjects.floorMaps;
import edu.wpi.CS3733.c22.teamG.tableControllers.AStar.PathTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.Location;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Popup;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

/** Controller class for template file. Template FXML file is templatetemplate.fxml */

// TODO in template FXML file, change manu bar to button bar. Fix SVG Icons spacing on buttons
// TODO possibly add logo above buttons on side panel

public class MapController {

  @FXML JFXButton homeBtn; // home btn with icon and text
  @FXML public JFXButton btn;

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane();
  @FXML private GesturePane towerPane = new GesturePane();

  @FXML private Pane nodeDataPane;
  @FXML private Pane sideViewPane;

  public TableController<Location, String> locations = null;
  private ImageView mapImage = null;
  private ImageView sideTower = null;
  private Group imageGroup = null;
  private Pane locNodePane = null;
  private Pane pathNodePane = null;
  private Group animatedPathNodeGroup = null;
  private MapSubController subController = null;
  private String currentFloor = "1";
  private Group equipGroup = null;
  private Group requestGroup = null;
  private List<String> astar = null;
  private String startTemp = null;
  private String endTemp = null;
  private ArrayList<ArrayList<String>> coord = null;

  // side view gridPane setup
  @FXML GridPane gridPane = new GridPane();
  @FXML private Label cleanLabel = new Label();
  @FXML private Label dirtyLabel = new Label();
  @FXML private Label bedLabel = new Label();
  @FXML private Label reclinerLabel = new Label();
  @FXML private Label pumpsLabel = new Label();
  @FXML private Label xrayLabel = new Label();
  @FXML private Label cleanBedLabel = new Label();
  @FXML private Label dirtyBedLabel = new Label();
  @FXML private Label cleanReclinerLabel = new Label();
  @FXML private Label dirtyReclinerLabel = new Label();
  @FXML private Label cleanPumpsLabel = new Label();
  @FXML private Label dirtyPumpsLabel = new Label();
  @FXML private Label xrayLabel2 = new Label();
  @FXML private Label floorLabel = new Label();

  private Rectangle rect;

  private IconDisplay bed3;
  private IconDisplay recliner3;
  private IconDisplay pump3;
  private IconDisplay xray3;
  private IconDisplay bed1;
  private IconDisplay recliner1;
  private IconDisplay pump1;
  private IconDisplay xray1;
  private IconDisplay bedL1;
  private IconDisplay reclinerL1;
  private IconDisplay pumpL1;
  private IconDisplay xrayL1;

  mainController main = null;

  @FXML
  public void setMainController(mainController realMain) {
    this.main = realMain;
  }

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE = "-fx-background-color: #002D59;";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59";

  FXMLLoader subControllerLoader =
      new FXMLLoader(Main.class.getResource("views/mapViewDataEntry.fxml"));

  FXMLLoader equipmentTableControllerLoader =
      new FXMLLoader(Main.class.getResource("views/equipTableView"));

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private ArrayList<Location> currentLocations = null;

  private PathTbl path = PathTbl.getInstance();
  private TableController locationTableController = LocationTbl.getInstance();
  private TableController menuTableController = MedEquipmentTbl.getInstance();

  @FXML
  public void initialize() {
    coord = new ArrayList<ArrayList<String>>();
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    locations = LocationTbl.getInstance();

    // initializes the map views
    // mapImage = new ImageView(floorMaps.groundFloor);
    mapImage = new ImageView(floorMaps.firstFloor);
    // initializes the map group to hold all of the nodes related to the map images
    imageGroup = new Group();
    imageGroup.getChildren().add(mapImage);

    // adds the group and gesture pane to the parent pane
    imagePane.getChildren().add(imageGroup);
    imagePane.getChildren().add(gesturePane);

    // sets the gesture pane to wrap around the image group
    gesturePane.setContent(imageGroup);
    // disables visible scrollbar
    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    // sets scroll to zoom
    gesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    // the most you can zoom out is 0.005 times the original size
    gesturePane.setMinScale(0.005);
    // fits the image to cover the visible pane
    gesturePane.setFitMode(GesturePane.FitMode.COVER);

    // creates new layer to add the interactive nodes to

    pathNodePane = new Pane();
    imageGroup.getChildren().add(pathNodePane);

    locNodePane = new Pane();
    imageGroup.getChildren().add(locNodePane);

    equipGroup = new Group();
    imageGroup.getChildren().add(equipGroup);

    requestGroup = new Group();
    imageGroup.getChildren().add(requestGroup);

    animatedPathNodeGroup = new Group();
    imageGroup.getChildren().add(animatedPathNodeGroup);

    // Sets the side image
    sideViewPane = new Pane();
    sideTower = new ImageView(floorMaps.towerSideview);
    sideTower.setScaleX(.75);
    sideTower.setScaleY(.75);
    towerPane.setContent(sideTower);
    sideViewPane.getChildren().add(towerPane);
    sideViewPane.setMaxSize(0, 0);
    sideViewPane.setTranslateX(350);
    sideViewPane.setTranslateY(-420);
    rect = new Rectangle(310, 38);
    rect.setFill(Color.LIGHTGREEN);
    sideViewPane.getChildren().add(rect);
    rect.setX(50);
    rect.setY(242);

    // set up for side view gridpane
    //    gridPane.setBackground(new Background(#f1f1f1, ))
    //    gridPane.setStyle(
    //        "-fx-background-color: #f1f1f1;  -fx-grid-lines-visible: true; -fx-text-color: black
    // ");
    gridPane.setStyle("-fx-background-color: #ffffffc0;");
    gridPane.setMaxSize(250, 110);
    gridPane.add(floorLabel, 1, 1);
    gridPane.add(cleanLabel, 2, 1);
    gridPane.add(dirtyLabel, 3, 1);
    gridPane.add(bedLabel, 1, 2);
    gridPane.add(cleanBedLabel, 2, 2);
    gridPane.add(dirtyBedLabel, 3, 2);
    gridPane.add(reclinerLabel, 1, 3);
    gridPane.add(cleanReclinerLabel, 2, 3);
    gridPane.add(dirtyReclinerLabel, 3, 3);
    gridPane.add(pumpsLabel, 1, 4);
    gridPane.add(cleanPumpsLabel, 2, 4);
    gridPane.add(dirtyPumpsLabel, 3, 4);
    gridPane.add(xrayLabel, 1, 5);
    gridPane.add(xrayLabel2, 2, 5);
    floorLabel.setText("Floor 1: \n");
    floorLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    cleanLabel.setText("Clean       \n");
    cleanLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    dirtyLabel.setText("Dirty\n");
    dirtyLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    bedLabel.setText("Beds:\n");
    bedLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    reclinerLabel.setText("Recliners:\n");
    reclinerLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    pumpsLabel.setText("Infusion Pumps:  \n");
    pumpsLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    xrayLabel.setText("X-Rays:\n");
    xrayLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");

    cleanBedLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    dirtyBedLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    cleanReclinerLabel.setStyle(
        "-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    dirtyReclinerLabel.setStyle(
        "-fx-text-fill: #002D59; -fx-font-weight: bolder; -fx-font-size: 16px;");
    cleanPumpsLabel.setStyle(
        "-fx-text-fill: #002D59; -fx-font-weight: bolder; -fx-font-size: 16px;");
    dirtyPumpsLabel.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");
    xrayLabel2.setStyle("-fx-text-fill: #002D59; -fx-font-weight: bold; -fx-font-size: 16px;");

    // Sorts equipments into their floors for display later
    ArrayList<MedEquipment> equipments = menuTableController.readTable();
    for (int i = 0; i < equipments.size(); i++) {
      String nodeID = equipments.get(i).getCurrLoc();
      int nodeLength = nodeID.length();
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("03"))
        filteredEquipments3.add(equipments.get(i));
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("01"))
        filteredEquipments1.add(equipments.get(i));
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("L1"))
        filteredEquipmentsL1.add(equipments.get(i));
    }

    // Adding icons on tower
    // floor 3 icons
    int[] equipmentNum = getEquipNum(filteredEquipments3);
    bed3 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bed3.setTranslateX(102);
    bed3.setTranslateY(160);
    bed3.setOnMouseEntered(
        e -> {
          bed3.hideIcon();
        });
    bed3.setOnMouseExited(
        e -> {
          bed3.showIcon();
        });

    recliner3 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    recliner3.setTranslateX(162);
    recliner3.setTranslateY(160);
    recliner3.setOnMouseEntered(
        e -> {
          recliner3.hideIcon();
        });
    recliner3.setOnMouseExited(
        e -> {
          recliner3.showIcon();
        });

    pump3 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pump3.setTranslateX(222);
    pump3.setTranslateY(160);
    pump3.setOnMouseEntered(
        e -> {
          pump3.hideIcon();
        });
    pump3.setOnMouseExited(
        e -> {
          pump3.showIcon();
        });

    xray3 = new IconDisplay(4, equipmentNum[6]);
    xray3.setTranslateX(282);
    xray3.setTranslateY(160);
    xray3.setOnMouseEntered(
        e -> {
          xray3.hideIcon();
        });
    xray3.setOnMouseExited(
        e -> {
          xray3.showIcon();
        });

    // floor 1 icons
    equipmentNum = getEquipNum(filteredEquipments1);
    bed1 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bed1.setTranslateX(102);
    bed1.setTranslateY(238);
    bed1.setOnMouseEntered(
        e -> {
          bed1.hideIcon();
        });
    bed1.setOnMouseExited(
        e -> {
          bed1.showIcon();
        });

    recliner1 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    recliner1.setTranslateX(162);
    recliner1.setTranslateY(238);
    recliner1.setOnMouseEntered(
        e -> {
          recliner1.hideIcon();
        });
    recliner1.setOnMouseExited(
        e -> {
          recliner1.showIcon();
        });

    pump1 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pump1.setTranslateX(222);
    pump1.setTranslateY(238);
    pump1.setOnMouseEntered(
        e -> {
          pump1.hideIcon();
        });
    pump1.setOnMouseExited(
        e -> {
          pump1.showIcon();
        });

    xray1 = new IconDisplay(4, equipmentNum[6]);
    xray1.setTranslateX(282);
    xray1.setTranslateY(238);
    xray1.setOnMouseEntered(
        e -> {
          xray1.hideIcon();
        });
    xray1.setOnMouseExited(
        e -> {
          xray1.showIcon();
        });

    // floor L1 icons
    equipmentNum = getEquipNum(filteredEquipmentsL1);
    bedL1 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bedL1.setTranslateX(102);
    bedL1.setTranslateY(277);
    bedL1.setOnMouseEntered(
        e -> {
          bedL1.hideIcon();
        });
    bedL1.setOnMouseExited(
        e -> {
          bedL1.showIcon();
        });

    reclinerL1 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    reclinerL1.setTranslateX(162);
    reclinerL1.setTranslateY(277);
    reclinerL1.setOnMouseEntered(
        e -> {
          reclinerL1.hideIcon();
        });
    reclinerL1.setOnMouseExited(
        e -> {
          reclinerL1.showIcon();
        });

    pumpL1 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pumpL1.setTranslateX(222);
    pumpL1.setTranslateY(277);
    pumpL1.setOnMouseEntered(
        e -> {
          pumpL1.hideIcon();
        });
    pumpL1.setOnMouseExited(
        e -> {
          pumpL1.showIcon();
        });

    xrayL1 = new IconDisplay(4, equipmentNum[6]);
    xrayL1.setTranslateX(282);
    xrayL1.setTranslateY(277);
    xrayL1.setOnMouseEntered(
        e -> {
          xrayL1.hideIcon();
        });
    xrayL1.setOnMouseExited(
        e -> {
          xrayL1.showIcon();
        });

    sideViewPane
        .getChildren()
        .addAll(
            bed3,
            recliner3,
            pump3,
            xray3,
            bed1,
            recliner1,
            pump1,
            xray1,
            bedL1,
            reclinerL1,
            pumpL1,
            xrayL1);

    imagePane.getChildren().add(gridPane);
    imagePane.getChildren().add(sideViewPane);
    gridPane.setTranslateX(550);
    setFloorView(getEquipNum(filteredEquipments1));

    sideTower.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            main.switchSideView();
          } else if (e.getClickCount() == 1) {
            if (bed3.getDisplay()) {
              bed3.setDisplay(false);
              recliner3.setDisplay(false);
              pump3.setDisplay(false);
              xray3.setDisplay(false);
              bed1.setDisplay(false);
              recliner1.setDisplay(false);
              pump1.setDisplay(false);
              xray1.setDisplay(false);
              bedL1.setDisplay(false);
              reclinerL1.setDisplay(false);
              pumpL1.setDisplay(false);
              xrayL1.setDisplay(false);
            } else {
              bed3.setDisplay(true);
              recliner3.setDisplay(true);
              pump3.setDisplay(true);
              xray3.setDisplay(true);
              bed1.setDisplay(true);
              recliner1.setDisplay(true);
              pump1.setDisplay(true);
              xray1.setDisplay(true);
              bedL1.setDisplay(true);
              reclinerL1.setDisplay(true);
              pumpL1.setDisplay(true);
              xrayL1.setDisplay(true);
            }
          }
        });

    // attempts to center the pane on launch
    gesturePane.zoomBy(0.005, 0.005, new Point2D(2500, 1700));

    // creates button to select visible floor
    HBox floorSelect = createFloorSelector();
    floorSelect.setMaxHeight(25);
    imagePane.getChildren().add(floorSelect);
    // aligns floor selector to the bottom left
    imagePane.setAlignment(floorSelect, Pos.BOTTOM_LEFT);

    try {
      Node subPane = (Node) subControllerLoader.load();
      nodeDataPane.getChildren().add(subPane);
      subController = subControllerLoader.getController();
      subController.setMapController(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // subController.setText(locations.getEntry("FDEPT00101"));

    JFXButton toggleEquip = new JFXButton();
    toggleEquip.setText("Equipment");
    toggleEquip.getStyleClass().add("actionButton");
    toggleEquip.setPrefWidth(110);
    toggleEquip.setOnMouseReleased(
        e -> {
          equipGroup.setVisible(!equipGroup.isVisible());
        });
    // imagePane.getChildren().add(toggleEquip);
    // imagePane.setAlignment(toggleEquip, Pos.TOP_LEFT);
    JFXButton toggleNodes = new JFXButton();
    toggleNodes.setText("Locations");
    toggleNodes.getStyleClass().add("actionButton");
    //    toggleNodes.setStyle("-fx-background-color: #0063a9; -fx-text-fill: white");
    toggleNodes.setPrefWidth(110);
    toggleNodes.setOnMouseReleased(
        e -> {
          locNodePane.setVisible(!locNodePane.isVisible());
        });
    JFXButton toggleRequests = new JFXButton();
    toggleRequests.setText("Requests");
    toggleRequests.setPrefWidth(110);
    toggleRequests.setOnMouseReleased(
        e -> {
          requestGroup.setVisible(!requestGroup.isVisible());
        });
    toggleRequests.getStyleClass().add("actionButton");

    // add path planning open button
    JFXButton openNodes = new JFXButton();
    openNodes.setText("Path Planning");
    openNodes.getStyleClass().add("actionButton");
    openNodes.setPrefWidth(110);
    openNodes.setMaxWidth(110);

    // add searchable combo box for location
    SearchableComboBox<String> startLoc = new SearchableComboBox<>();
    SearchableComboBox<String> endLoc = new SearchableComboBox<>();
    startLoc.setValue("Start Location");
    endLoc.setValue("End Location");
    startLoc.setMaxWidth(110);
    endLoc.setMaxWidth(110);

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    startLoc.setItems(oList);
    endLoc.setItems(oList);

    // add button to generate path
    JFXButton enterPath = new JFXButton();
    enterPath.setText("Find Path");
    enterPath.getStyleClass().add("actionButton");
    enterPath.setMaxWidth(110);

    // clear path button
    JFXButton clearPath = new JFXButton();
    clearPath.setText("Clear Path");
    clearPath.getStyleClass().add("actionButton");
    clearPath.setMaxWidth(110);

    clearPath.setOnMouseReleased(
        (event) -> {
          pathNodePane.getChildren().clear();
          animatedPathNodeGroup.getChildren().clear();
          startTemp = null;
          endTemp = null;
        });

    // creates the nodes list
    JFXNodesList togglePathInputs = new JFXNodesList();
    togglePathInputs.setRotate(270);
    togglePathInputs.spacingProperty().setValue(90);
    togglePathInputs.setMaxWidth(115);
    togglePathInputs.addAnimatedNode(openNodes);
    togglePathInputs.addAnimatedNode(startLoc);
    togglePathInputs.addAnimatedNode(endLoc);
    togglePathInputs.addAnimatedNode(enterPath);
    togglePathInputs.addAnimatedNode(clearPath);

    enterPath.setOnMouseReleased(
        (event) -> {
          String start = (String) startLoc.getSelectionModel().getSelectedItem();
          String end = (String) endLoc.getSelectionModel().getSelectedItem();
          if (!previouslyUsed(start, end)) {
            astar = PathTbl.getInstance().createAStarPath(start, end);
            //            buildPath(astar);
            dividePath(astar);
            animatedPath();
            pathNodePane.setVisible(true);
          }
        });

    HBox buttonHolder = new HBox(toggleNodes, toggleEquip, toggleRequests, togglePathInputs);
    buttonHolder.setAlignment(Pos.TOP_LEFT);
    buttonHolder.setSpacing(6);
    buttonHolder.setMargin(toggleNodes, new Insets(0, 0, 0, 10));
    Group buttonGroup = new Group();
    buttonGroup.getChildren().add(buttonHolder);
    imagePane.getChildren().add(buttonGroup);
    imagePane.setAlignment(buttonGroup, Pos.TOP_LEFT);

    currentFloor = "1";
    setLocations(currentFloor);
    setEquipment();
    setRequest();
    mapImage.setImage(L1);

    nodeDataPane.setManaged(false);
    nodeDataPane.setVisible(false);
  }

  public void createIcon(Location loc) {
    //    LocationPane placeHolder = new LocationPane(loc);
    //    placeHolder.setMinWidth(10);
    //    placeHolder.setMinHeight(10);

    LocationCircle placeHolder = new LocationCircle(loc);
    placeHolder.setRadius(10);

    locNodePane.getChildren().add(placeHolder);
    placeHolder.setLayoutX(loc.getXcoord());
    placeHolder.setLayoutY(loc.getYcoord());
    //    placeHolder.setStyle("-fx-background-color: black");
    placeHolder.setOnMouseEntered(
        e -> {
          //          placeHolder.setStyle("-fx-background-color: blue");
          placeHolder.setFill(Color.BLUE);
        });
    placeHolder.setOnMouseExited(
        e -> {
          //          placeHolder.setStyle("-fx-background-color: black");
          placeHolder.setFill(Color.BLACK);
        });
    placeHolder.setOnMouseDragged(
        e -> {
          gesturePane.setGestureEnabled(false);
          placeHolder.setLayoutX(placeHolder.getLayoutX() + e.getX());
          placeHolder.setLayoutY(placeHolder.getLayoutY() + e.getY());
        });
    placeHolder.setOnMouseReleased(
        e -> {
          gesturePane.setGestureEnabled(true);
          LocationTbl.getInstance()
              .editEntry(
                  placeHolder.location.getNodeID(), "xCoord", (int) placeHolder.getLayoutX());
          LocationTbl.getInstance()
              .editEntry(
                  placeHolder.location.getNodeID(), "yCoord", (int) placeHolder.getLayoutY());
          placeHolder.location.setXcoord((int) placeHolder.getLayoutX());
          placeHolder.location.setYcoord((int) placeHolder.getLayoutY());
          subController.setText(placeHolder.location);
        });
    placeHolder.setOnContextMenuRequested(
        e -> {
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          nodeDataPane.getChildren().clear();
          FXMLLoader controllerLoader =
              new FXMLLoader(Main.class.getResource("views/mapViewDataEntry.fxml"));
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          try {
            Node subPane = (Node) controllerLoader.load();
            nodeDataPane.getChildren().add(subPane);
            MapSubController subController = controllerLoader.getController();
            subController.setMapController(this);
            subController.setText(placeHolder.location);
          } catch (IOException exc) {
            exc.printStackTrace();
          }
        });
  }

  public void createRequestIcon(Request req, Location loc) {
    ReqImageView reqIcon = new ReqImageView(loc, req);
    switch (req.getRequestType()) {
      case "MedEquipDelivery":
        reqIcon.setImage(floorMaps.medicalEquipmentGreen);
        break;
      case "MedicineDelivery":
        reqIcon.setImage(floorMaps.medicineGreen);
        break;
      case "GiftFloral":
        reqIcon.setImage(floorMaps.gift);
        break;
      case "LaundryService":
        reqIcon.setImage(floorMaps.laundry);
        break;
      case "CompServ":
        reqIcon.setImage(floorMaps.computer);
        break;
    }
    reqIcon.setFitHeight(20);
    reqIcon.setFitWidth(20);
    requestGroup.getChildren().add(reqIcon);
    reqIcon.setLayoutX(loc.getXcoord());
    reqIcon.setLayoutY(loc.getYcoord());
    reqIcon.setOnContextMenuRequested(
        e -> {
          nodeDataPane.getChildren().clear();
          FXMLLoader controllerLoader =
              new FXMLLoader(Main.class.getResource("views/mapViewRequestEntry.fxml"));
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          try {
            Node subPane = (Node) controllerLoader.load();
            nodeDataPane.getChildren().add(subPane);
            MapSubReqController subController = controllerLoader.getController();
            subController.setMapController(this);

            subController.setText(reqIcon.request);
          } catch (IOException exc) {
            exc.printStackTrace();
          }
        });
  }

  public void createMedEquipIcon(MedEquipment med, Location loc) {
    MedEqpImageView medIcon = new MedEqpImageView(loc, med);
    medIcon.setFitWidth(20);
    medIcon.setFitHeight(20);

    equipGroup.getChildren().add(medIcon);

    medIcon.setLayoutX(loc.getXcoord() - 5);
    medIcon.setLayoutY(loc.getYcoord() - 5);

    if (medIcon.medEquipment.getMedEquipmentType().trim().toUpperCase(Locale.ROOT).equals("BED")) {
      medIcon.setImage(floorMaps.bedIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("X-RAY")) {
      medIcon.setImage(floorMaps.xRayIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("INFUSION PUMP")) {
      medIcon.setImage(floorMaps.infusionPumpIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("RECLINER")) {
      medIcon.setImage(floorMaps.reclinerIcon);
    } else {
      medIcon.setImage(floorMaps.bedIcon);
    }

    medIcon.setOnMouseEntered(
        e -> {
          medIcon.setStyle("-fx-background-color: green");
        });
    medIcon.setOnMouseExited(
        e -> {
          medIcon.setStyle("-fx-background-color: cyan");
        });

    medIcon.setOnContextMenuRequested(
        e -> {
          Popup popup = new Popup();

          popup.setAnchorX(e.getSceneX());
          popup.setAnchorY(e.getSceneY());
          var popUpLoader = new FXMLLoader(Main.class.getResource("views/mapViewMedEquipLoc.fxml"));
          try {
            AnchorPane popupAnchor = popUpLoader.load();
            popup.getContent().add(popupAnchor);
            popup.show(imagePane.getScene().getWindow());
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          EquipLocEditor popupController = popUpLoader.getController();
          popupController.setMedEquipment(med, this);
          subController.setText(medIcon.location);
        });
    medIcon.setOnMouseDragged(
        e -> {
          gesturePane.setGestureEnabled(false);
          medIcon.setLayoutX(medIcon.getLayoutX() + e.getX());
          medIcon.setLayoutY(medIcon.getLayoutY() + e.getY());
        });
    //
    medIcon.setOnMouseReleased(
        e -> {
          gesturePane.setGestureEnabled(true);
          double mouseX = medIcon.getLayoutX();
          double mouseY = medIcon.getLayoutY();
          double minDist = 100;
          Location snapTo = loc;
          for (Location location : currentLocations) {
            double temp =
                java.awt.geom.Point2D.distance(
                    mouseX, mouseY, location.getXcoord(), location.getYcoord());
            if (temp < minDist) {
              minDist = temp;
              snapTo = location;
            }
          }
          if (!snapTo.getNodeID().equals(loc.getNodeID())) {
            FXMLLoader popupLoader =
                new FXMLLoader(Main.class.getResource("views/confirmationBox.fxml"));
            Popup reqQuestion = new Popup();
            try {
              reqQuestion.getContent().add(popupLoader.load());
              reqQuestion.show(medIcon, e.getScreenX(), e.getScreenY());
              MapViewConfirmationButtons subController = popupLoader.getController();
              subController.setMainController(this, med, snapTo);
              medIcon.setLayoutX(snapTo.getXcoord() - 5);
              medIcon.setLayoutY(snapTo.getYcoord() - 5);

            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          } else {
          }
        });
  }

  public HBox createFloorSelector() {
    JFXButton floorL2 = new JFXButton("L2");
    floorL2.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    JFXButton floorL1 = new JFXButton("L1");
    floorL1.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    JFXButton floor01 = new JFXButton("1");
    floor01.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");
    JFXButton floor02 = new JFXButton("2");
    floor02.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");
    JFXButton floor03 = new JFXButton("3");
    floor03.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    HBox floorSelect = new HBox(floorL2, floorL1, floor01, floor02, floor03);
    HBox.setMargin(floor01, new Insets(0, 2, 0, 3));
    HBox.setMargin(floor02, new Insets(0, 2, 0, 3));
    HBox.setMargin(floorL1, new Insets(0, 2, 0, 3));
    HBox.setMargin(floorL2, new Insets(0, 2, 0, 13));
    HBox.setMargin(floor03, new Insets(0, 2, 0, 3));
    floorL2.setOnAction(
        e -> {
          mapImage.setImage(LL2);
          currentFloor = "L2";
          refreshMap();
          rect.setY(322);
          gridPane.setVisible(false);
        });
    floorL1.setOnAction(
        e -> {
          mapImage.setImage(LL1);
          currentFloor = "L1";
          refreshMap();
          rect.setY(282);
          gridPane.setVisible(true);
          setFloorView(getEquipNum(filteredEquipmentsL1));
          floorLabel.setText("Lower Floor 1");
        });
    floor01.setOnAction(
        e -> {
          mapImage.setImage(L1);
          currentFloor = "1";
          refreshMap();
          rect.setY(242);
          gridPane.setVisible(true);
          setFloorView(getEquipNum(filteredEquipments1));
          floorLabel.setText("Floor 1");
        });
    floor02.setOnAction(
        e -> {
          mapImage.setImage(L2);
          currentFloor = "2";
          refreshMap();
          rect.setY(204);
          gridPane.setVisible(false);
        });
    floor03.setOnAction(
        e -> {
          mapImage.setImage(L3);
          currentFloor = "3";
          refreshMap();
          rect.setY(166);
          gridPane.setVisible(true);
          setFloorView(getEquipNum(filteredEquipments3));
          floorLabel.setText("Floor 3");
        });

    return floorSelect;
  }

  ArrayList<MedEquipment> filteredEquipments3 = new ArrayList<>();
  ArrayList<MedEquipment> filteredEquipments1 = new ArrayList<>();
  ArrayList<MedEquipment> filteredEquipmentsL1 = new ArrayList<>();

  /**
   * sets the side table of equipment
   *
   * @param num the int array of numbers
   */
  public void setFloorView(int[] num) {
    dirtyBedLabel.setText("  " + num[0]);
    cleanBedLabel.setText("  " + num[1]);
    dirtyReclinerLabel.setText("  " + num[2]);
    cleanReclinerLabel.setText("  " + num[3]);
    dirtyPumpsLabel.setText("  " + num[4]);
    cleanPumpsLabel.setText("  " + num[5]);
    xrayLabel2.setText("  " + num[6]);
  }

  /**
   * gets an array of the number of each equipment
   *
   * @param filteredEquipments a list of equipment to sort
   * @return int array of the equipment amounts
   */
  public int[] getEquipNum(ArrayList<MedEquipment> filteredEquipments) {
    int cleanBed = 0;
    int dirtyBed = 0;
    int cleanRecliner = 0;
    int dirtyRecliner = 0;
    int cleanPump = 0;
    int dirtyPump = 0;
    int xray = 0;
    for (int i = 0; i < filteredEquipments.size(); i++) {
      String type = filteredEquipments.get(i).getMedEquipmentType().trim();
      String status = filteredEquipments.get(i).getStatus().trim();
      if (type.equals("Bed")) {
        if (status.equals("Stored")) {
          cleanBed++;
        } else {
          dirtyBed++;
        }
      } else if (type.equals("Recliner")) {
        if (status.equals("Stored")) {
          cleanRecliner++;
        } else {
          dirtyRecliner++;
        }
      } else if (type.equals("Infusion Pump")) {
        if (status.equals("Stored")) {
          cleanPump++;
        } else {
          dirtyPump++;
        }
      } else if (type.equals("X-ray")) {
        xray++;
      }
    }
    return new int[] {cleanBed, dirtyBed, cleanRecliner, dirtyRecliner, cleanPump, dirtyPump, xray};
  }

  public void setLocations(String floor) {
    locNodePane.getChildren().clear();
    currentFloor = floor;
    currentLocations = locations.readTable();
    currentLocations =
        (ArrayList)
            currentLocations.stream()
                .filter(l -> l.getFloor().equals(floor))
                .collect(Collectors.toList());
    for (Location l : currentLocations) {
      createIcon(l);
    }
  }

  public void setEquipment() {
    equipGroup.getChildren().clear();

    TableController<Location, String> locations = LocationTbl.getInstance();
    TableController<MedEquipment, Integer> reqTable = MedEquipmentTbl.getInstance();
    ArrayList<MedEquipment> reqList = reqTable.readTable();
    reqList =
        (ArrayList)
            reqList.stream()
                .filter(
                    l -> {
                      if (locations.getEntry(l.getCurrLoc().trim()) != null) {
                        return (locations.getEntry(l.getCurrLoc().trim()))
                            .getFloor()
                            .equals(currentFloor);
                      }
                      return false;
                    })
                .collect(Collectors.toList());
    for (MedEquipment mer : reqList) {
      createMedEquipIcon(mer, locations.getEntry(mer.getCurrLoc().trim()));
    }
  }

  public void setRequest() {
    requestGroup.getChildren().clear();

    TableController<Location, String> locations = LocationTbl.getInstance();
    TableController<Request, Integer> reqTable = RequestTable.getInstance();
    ArrayList<Request> reqList = reqTable.readTable();
    if (reqList == null || reqList.isEmpty()) {
      return;
    }
    reqList =
        (ArrayList)
            reqList.stream()
                .filter(
                    l -> {
                      if (locations.getEntry(l.getLocationID().trim()) != null) {
                        return (locations.getEntry(l.getLocationID().trim()))
                            .getFloor()
                            .equals(currentFloor);
                      }
                      return false;
                    })
                .collect(Collectors.toList());
    for (Request mer : reqList) {
      createRequestIcon(mer, locations.getEntry(mer.getLocationID().trim()));
    }
  }

  public String getCurrentFloor() {
    return currentFloor;
  }

  public void refreshMap() {
    setLocations(currentFloor);
    setEquipment();
    setRequest();
    refreshPath();
    animatedPath();
  }

  /** Function for populating the location choice box, called in initialize */
  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      // System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }

  /**
   * creates the path for using the a star path
   *
   * @param locs the list of String location node ids
   */
  public void buildPath(List<String> locs) {
    pathNodePane.getChildren().clear();
    for (int i = 0; i < locs.size() - 1; i++) {
      Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
      Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
      PathBar path = new PathBar(loc, loc1);
      pathNodePane.getChildren().add(path);
      if (!path.floor.equals(currentFloor)) {
        path.setVisible(false);
      }
    }
    startTemp = locs.get(0);
    endTemp = locs.get(locs.size() - 1);
  }

  /** refreshes the map to load the path to the appropriate floor (inspired by Will) */
  public void refreshPath() {
    for (Node p : pathNodePane.getChildren()) {
      PathBar pb = (PathBar) p;
      if (!pb.floor.equals(currentFloor)) {
        pb.setVisible(false);
      } else {
        pb.setVisible(true);
      }
    }
  }

  /**
   * checks to see if the previous path was already use
   *
   * @param start the initial starting location
   * @param end the initial ending location
   * @return returns a bool if the locations match
   */
  public boolean previouslyUsed(String start, String end) {
    return start.equals(startTemp) && end.equals(endTemp);
  }

  /**
   * Divides the list of locations from the astar to change between floors
   *
   * @param locs the list of location names from the path
   */
  public void dividePath(List<String> locs) {
    coord.get(0).clear();
    coord.get(1).clear();
    coord.get(2).clear();
    coord.get(3).clear();
    coord.get(4).clear();
    for (int i = 0; i < locs.size(); i++) {
      if (locs.get(i).substring(locs.get(i).length() - 2).equals("L2")) {
        coord.get(0).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 2).equals("L1")) {
        coord.get(1).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("1")) {
        coord.get(2).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("2")) {
        coord.get(3).add(locs.get(i));
      } else {
        coord.get(4).add(locs.get(i));
      }
    }
  }

  /** Generate the path based on the current floor */
  public void animatedPath() {
    animatedPathNodeGroup.getChildren().clear();
    ArrayList<String> locs = new ArrayList<>();
    if (currentFloor.equals("L2")) {
      locs = coord.get(0);
    } else if (currentFloor.equals("L1")) {
      locs = coord.get(1);
    } else if (currentFloor.equals("1")) {
      locs = coord.get(2);
    } else if (currentFloor.equals("2")) {
      locs = coord.get(3);
    } else if (currentFloor.equals("3")) {
      locs = coord.get(4);
    }

    // iterates through the list of locations and adds it to the coords list of the poly line
    ArrayList<Double> coordsList = new ArrayList<>();
    boolean secondLine = false;
    int tempStop = 0;
    for (int i = 0; i < locs.size() - 1; i++) {
      if (locs.get(i).substring(1, 5).equals("ELEV")
              && locs.get(i + 1).substring(1, 5).equals("ELEV")
          || locs.get(i + 1).substring(1, 5).equals("ELEV")
              && locs.get(i).substring(1, 5).equals("ELEV")) {
        secondLine = true;
        tempStop = i + 1;
        break;
      }
      Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
      Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
      coordsList.add((double) (loc.getXcoord()));
      coordsList.add((double) (loc.getYcoord()));
      coordsList.add((double) (loc1.getXcoord()));
      coordsList.add((double) (loc1.getYcoord()));
    }

    // creates the polyline bases on the positions coordinates
    Polyline polyline = new Polyline();
    polyline.getPoints().addAll(coordsList);
    polyline.setStroke(Color.web("#006db3"));
    polyline.setStrokeWidth(10);
    polyline.getStrokeDashArray().setAll(20.0, 20.0);
    animatedPathNodeGroup.getChildren().addAll(polyline);

    // animates the line accordingly
    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(polyline.strokeDashOffsetProperty(), 1000)),
            new KeyFrame(
                Duration.seconds(15), new KeyValue(polyline.strokeDashOffsetProperty(), 0)));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    if (secondLine) {
      ArrayList<Double> coordsList2 = new ArrayList<>();
      for (int i = tempStop; i < locs.size() - 1; i++) {
        Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
        Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
        coordsList2.add((double) (loc.getXcoord()));
        coordsList2.add((double) (loc.getYcoord()));
        coordsList2.add((double) (loc1.getXcoord()));
        coordsList2.add((double) (loc1.getYcoord()));
      }

      // creates the polyline bases on the positions coordinates
      Polyline polyline2 = new Polyline();
      polyline2.getPoints().addAll(coordsList2);
      polyline2.setStroke(Color.web("#006db3"));
      polyline2.setStrokeWidth(10);
      polyline2.getStrokeDashArray().setAll(20.0, 20.0);
      animatedPathNodeGroup.getChildren().addAll(polyline2);

      // animates the line accordingly
      Timeline timeline2 =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(polyline2.strokeDashOffsetProperty(), 1000)),
              new KeyFrame(
                  Duration.seconds(15), new KeyValue(polyline2.strokeDashOffsetProperty(), 0)));
      timeline2.setCycleCount(Timeline.INDEFINITE);
      timeline2.play();
    }
  }

  private class MedEqpImageView extends ImageView {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public MedEqpImageView(Location loc, MedEquipment med) {
      super();
      location = loc;
      medEquipment = med;
    }
  }

  private class ReqImageView extends ImageView {
    public Location location = null;
    public Request request = null;

    public ReqImageView(Location loc, Request req) {
      super();
      location = loc;
      request = req;
    }
  }

  private class LocationPane extends Pane {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public LocationPane(Location loc) {
      super();
      location = loc;
    }

    public LocationPane(Location loc, MedEquipment med) {
      super();
      location = loc;
      medEquipment = med;
    }

    void setLocation(Location loc) {
      location = loc;
    }
  }

  private class LocationCircle extends Circle {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public LocationCircle(Location loc) {
      super();
      location = loc;
    }

    void setLocation(Location loc) {
      location = loc;
    }
  }

  /** Creates a path bar class for path to follow */
  public class PathBar extends Line {
    private String floor = "00";

    public PathBar(Location startLoc, Location endLoc) {
      super(startLoc.getXcoord(), startLoc.getYcoord(), endLoc.getXcoord(), endLoc.getYcoord());
      this.setStrokeWidth(15);
      this.setStroke(Color.rgb(7, 16, 115));
      if (startLoc.getFloor().equals(endLoc.getFloor())) {
        floor = startLoc.getFloor();
      }
    }

    public String getFloor() {
      return floor;
    }
  }

  /** creates a stack pane that displays a number in a navy circle */
  private class EquipLabel extends StackPane {
    private Text text;
    Circle circle;

    public EquipLabel() {
      super();
      circle = new Circle(16, Color.rgb(0, 45, 89));
      text = new Text("");
      text.setFill(Color.WHITE);
      text.setBoundsType(TextBoundsType.VISUAL);
      text.setFont(Font.font("Open Sans", 22));
      super.getChildren().addAll(circle, text);
    }

    public EquipLabel(int display) {
      super();
      circle = new Circle(16, Color.rgb(0, 45, 89));
      text = new Text(display + "");
      text.setFill(Color.WHITE);
      text.setBoundsType(TextBoundsType.VISUAL);
      text.setFont(Font.font("Open Sans", 22));
      super.getChildren().addAll(circle, text);
    }

    /**
     * sets the text of the circle
     *
     * @param text text to display
     */
    public void setText(int text) {
      this.text.setText(text + "");
    }

    /**
     * sets the location of the pane
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setLocation(int x, int y) {
      super.setTranslateX(x);
      super.setTranslateY(y);
    }
  }

  /** displays an icon and the amount on each floor with a mouse hover */
  public class IconDisplay extends StackPane {
    private final ImageView BED = new ImageView(floorMaps.bedIcon);
    private final ImageView RECLINER = new ImageView(floorMaps.reclinerIcon);
    private final ImageView PUMP = new ImageView(floorMaps.infusionPumpIcon);
    private final ImageView XRAY = new ImageView(floorMaps.xRayIcon);
    private ImageView icon;
    private boolean displayed = true;

    private EquipLabel label;

    public IconDisplay(int iconNum, int display) {
      super();
      switch (iconNum) {
        case 1:
          icon = BED;
          break;
        case 2:
          icon = RECLINER;
          break;
        case 3:
          icon = PUMP;
          break;
        default:
          icon = XRAY;
          break;
      }
      setIcon(icon);
      label = new EquipLabel(display);
      this.getChildren().addAll(label, icon);
    }

    /**
     * gets the display statu
     *
     * @return true if icon visible
     */
    public boolean getDisplay() {
      return displayed;
    }

    /**
     * sets the display mode
     *
     * @param bool
     */
    public void setDisplay(boolean bool) {
      displayed = bool;
      if (bool) {
        showIcon();
      } else {
        hideIcon();
      }
    }

    /**
     * sets the label to a new number
     *
     * @param num the number to change it to
     */
    public void setDisplayNum(int num) {
      label.setText(num);
    }

    /**
     * sets the Icon to 0.65 the size
     *
     * @param image
     */
    private void setIcon(ImageView image) {
      image.setScaleX(.65);
      image.setScaleY(.65);
    }

    /** makes the icon invisible */
    public void hideIcon() {
      icon.setVisible(false);
    }

    /** shows the icon */
    public void showIcon() {
      if (displayed) icon.setVisible(true);
    }
  }
}
