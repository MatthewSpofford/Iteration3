package edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AudioVisualTbl extends TableController<AudioVisual, Integer> {

  private static AudioVisualTbl instance = null; // **

  private AudioVisualTbl() throws SQLException { // **
    super(
        "AudioVisual", Arrays.asList(new String[] {"avID", "deviceType", "locID", "description"}));
    String[] cols = {"avID", "deviceType", "locID", "description"};
    createTable();
    objList = new ArrayList<AudioVisual>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static AudioVisualTbl getInstance() { // **
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new AudioVisualTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<AudioVisual> readTable() { // **
    ArrayList tableInfo = new ArrayList<AudioVisual>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new AudioVisual( // **
                r.getInt(1), r.getString(2), r.getString(3), r.getString(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(AudioVisual obj) {
    AudioVisual med = (AudioVisual) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?);");

      // **
      s.setInt(1, med.getAvID());
      s.setString(2, med.getDeviceType());
      s.setString(3, med.getLocID());
      s.setString(4, med.getDescription());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<AudioVisual> readBackup(String fileName) {
    ArrayList<AudioVisual> medList = new ArrayList<AudioVisual>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("avID,deviceType,locID,description"))) { // **
        System.err.println("AudioVisual backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        System.out.println("HELP: " + currentLine);
        AudioVisual med = // **
            new AudioVisual(Integer.parseInt(element[0]), element[1], element[2], element[3]); // **
        medList.add(med); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return medList; // **
  }

  @Override
  public void createTable() {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return;
    }

    System.out.println("SQLite driver registered!");

    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  AudioVisual("
              + "avID INTEGER NOT NULL ,"
              + "deviceType TEXT NOT NULL, "
              + "locID TEXT NOT NULL, "
              + "description TEXT, "
              + "PRIMARY KEY ('avID')"
              + "CONSTRAINT nodeID FOREIGN KEY (locID) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + ");");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AudioVisual getEntry(Integer pkID) { // **
    AudioVisual med = new AudioVisual(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID); // **
        ResultSet r = s.executeQuery();
        r.next();
        med.setAvID(r.getInt(1));
        med.setDeviceType(r.getString(2));
        med.setLocID(r.getString(3));
        med.setDescription(r.getString(4));
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }
}
