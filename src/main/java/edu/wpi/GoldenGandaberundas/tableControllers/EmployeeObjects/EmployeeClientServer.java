package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmployeeClientServer implements TableController<Employee, Integer> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Employee> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public EmployeeClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Employee> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing

    // createTable();
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  // reads the DB table and returns the information as a ArrayList<Employee>
  @Override
  public ArrayList<Employee> readTable() {
    ArrayList tableInfo = new ArrayList<Employee>();
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Employee(
                r.getInt(1),
                r.getString(2),
                r.getString(3),
                r.getString(4),
                r.getString(5),
                r.getString(6),
                r.getString(7)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(Employee obj) {
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement(
                  " IF NOT EXISTS (SELECT 1 FROM "
                      + tbName
                      + " WHERE "
                      + colNames.get(0)
                      + " = ?)"
                      + "BEGIN"
                      + "    INSERT INTO "
                      + tbName
                      + " VALUES (?, ?, ?, ?, ?, ?, ?)"
                      + "end");
      s.setInt(1, obj.getEmpID());
      s.setInt(2, obj.getEmpID());
      s.setString(3, obj.getFName());
      s.setString(4, obj.getLName());
      s.setString(5, obj.getRole());
      s.setString(6, obj.getEmail());
      s.setString(7, obj.getPhoneNum());
      s.setString(8, obj.getAddress());
      int r = s.executeUpdate();
      if (r != 0) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Employee> readBackup(String fileName) {
    ArrayList<Employee> empList = new ArrayList<Employee>();
    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("empID,fName,lName,perms,role,email,phone number,address"))) {
        System.err.println("employee backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        // check if a line is blank
        if (!currentLine.isEmpty()) {
          String[] element = currentLine.split(","); // separates each element based on a comma
          Employee emp =
              new Employee(
                  Integer.parseInt(element[0]),
                  element[1],
                  element[2],
                  element[3],
                  element[4],
                  element[5],
                  element[6]);
          empList.add(emp); // adds the location to the list
        } else {
          System.out.println("EMPTY");
        }
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return empList;
  }

  @Override
  public void createTable() {
    try {
      PreparedStatement s1 =
          connectionHandler
              .getConnection()
              .prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = connectionHandler.getConnection().createStatement();
      s.execute(
          "CREATE TABLE  Employees(empID INTEGER NOT NULL ,fName TEXT, lName TEXT, role TEXT,email varchar(60) NOT NULL UNIQUE,phoneNumber TEXT,address TEXT, PRIMARY KEY (empID));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Employee getEntry(Integer pkID) {

    Employee emp = new Employee();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connectionHandler
                .getConnection()
                .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        emp.setEmpID(r.getInt(1));
        emp.setFName(r.getString(2));
        emp.setLName(r.getString(3));
        emp.setRole(r.getString(4));
        emp.setEmail(r.getString(5));
        emp.setPhoneNum(r.getString(6));
        emp.setAddress(r.getString(7));
        return emp;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return emp;
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Employee> objList) {
    this.createTable();
    deleteTableData();
    for (Employee emp : objList) {
      if (!this.addEntry(emp)) {
        return false;
      }
    }
    this.objList = this.readTable();

    return true;
  }

  /**
   * Modifies the attribute so that it is equal to value MAKE SURE YOU KNOW WHAT DATA TYPE YOU ARE
   * MODIFYING
   *
   * @param pkid the primary key that represents the row you are modifying
   * @param colName column to be modified
   * @param value new value for column
   * @return true if successful, false otherwise
   */
  // public boolean editEntry(T1 pkid, String colName, Object value)
  public boolean editEntry(Integer pkid, String colName, Object value) {
    //    if (pkid instanceof ArrayList) {
    //      return editEntryComposite((ArrayList<Integer>) pkid, colName, value);
    //    }
    try {

      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement(
                  "UPDATE "
                      + tbName
                      + " SET "
                      + colName
                      + " = ? WHERE ("
                      + colNames.get(0)
                      + ") =(?);");
      s.setObject(1, value);
      s.setObject(2, pkid);
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void writeTable() {
    for (Employee obj : objList) {

      this.addEntry(obj);
    }
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(Integer pkid) {
    //    if (pkid instanceof ArrayList) {
    //      return deleteEntryComposite((ArrayList<Integer>) pkid);
    //    }
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
      s.setObject(1, pkid);
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    if (objList.isEmpty()) {
      return;
    }
    /* Instantiate the writer */
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    /* Get the class type of the objects in the array */
    final Class<?> type = objList.get(0).getClass();

    /* Get the name of all the attributes */
    final ArrayList<Field> classAttributes = new ArrayList<>(List.of(type.getDeclaredFields()));

    boolean doesExtend = Request.class.isAssignableFrom(type);
    if (doesExtend) {
      final Class<?> superType = objList.get(0).getClass().getSuperclass();
      classAttributes.addAll(0, (List.of(superType.getDeclaredFields())));
    }

    /* Write the parsed attributes to the file */
    writer.println(classAttributes.stream().map(Field::getName).collect(Collectors.joining(",")));

    /* For each object, read each attribute and append it to the file with a comma separating */
    PrintWriter finalWriter = writer;
    objList.forEach(
        obj -> {
          finalWriter.println(
              classAttributes.stream()
                  .map(
                      attribute -> {
                        attribute.setAccessible(true);
                        String output = "";
                        try {
                          output = attribute.get(obj).toString();
                        } catch (IllegalAccessException | ClassCastException e) {
                          System.err.println("[CSVUtil] Object attribute access error.");
                        }
                        return output;
                      })
                  .collect(Collectors.joining(",")));
          finalWriter.flush();
        });
    writer.close();
  }

  // drop current table and enter data from CSV
  public ArrayList<Employee> loadBackup(String fileName) {
    createTable();
    ArrayList<Employee> listObjs = readBackup(fileName);

    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      this.objList = listObjs;
      this.writeTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listObjs;
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<Integer>) pkID);
    //    }
    boolean exists = false;
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement(
                  "SELECT count(*) FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");

      s.setObject(1, pkID);

      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        exists = true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Employee> getObjList() {
    return objList;
  }
}
