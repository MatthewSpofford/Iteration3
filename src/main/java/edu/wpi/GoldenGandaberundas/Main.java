package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.PermissionTbl;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);
    // Initializes the database tables in memory
      
    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    EmployeeTbl.getInstance();
    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    PatientTbl.getInstance();
    LaundryTbl.getInstance();
    MedEquipmentTbl.getInstance().loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    MedicineTbl.getInstance();
    GiftTbl.getInstance();
    PermissionTbl.getInstance().loadBackup("BackupsCSVs/PermissionsForTesting.csv");
    CredentialsTbl.getInstance(); // .loadBackup("BackupsCSVs/CredentialsTbl.csv");
    AudioVisualTbl.getInstance();
    ComputerTbl.getInstance();
    //    FoodTbl.getInstance();
    PathTbl.getInstance();

    LocationTbl.getInstance();
    PathTbl.getInstance();

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodRequestTbl.getInstance();
    ComputerRequestTbl.getInstance();
    AudioVisualRequestTbl.getInstance();

    PathTbl.getInstance(); // .loadBackup("backups/AllLocationEdges.csv");
    EmployeeTbl.getInstance().loadBackup("BackupsCSVs/employeeTbl.csv");
    EmployeePermissionTbl.getInstance().loadBackup("BackupsCSVs/employeePermissionsTbl.csv");
    //    CredentialsTbl.getInstance().addEntry(new Credential(456, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(666, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(123, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(777, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(888, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(999, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(70, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(420, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(69, "p"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(96, "p"));

    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/BackupAudioVisualTbl.csv");
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    points = PathTbl.getInstance().createBranchedLocations(points);

    CredentialsTbl.getInstance().addEntry(new Credential(0, "admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(123, "password"));
    System.out.println(CredentialsTbl.getInstance().getEntry(0).checkPassword("admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(1, "staff"));
    floorMaps.load();

    PathTbl.createStatsMap();
    //    System.out.println(
    //        "PATH: "
    //            + PathTbl.getInstance().createAStarPathwStats("gPATI00103", "HBATH00203")
    //            + '\n'
    //            + "PATH: "
    //            + PathTbl.getInstance().createAStarPathwStats("HRETL00302", "FSERV00301")
    //            + '\n'
    //            + "PATH: "
    //            + PathTbl.getInstance().createAStarPathwStats("FDEPT00301", "HBATH00201")
    //            + '\n'
    //            + "PATH: "
    //            + PathTbl.getInstance().createAStarPathwStats("GPATI00603", "HSTAI00103"));
    PathTbl.printStatsMap();

    //    EmployeePermissionTbl.getInstance();


    //    SimulationController sim = new SimulationController();
    //    sim.update();
  }
}
