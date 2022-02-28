package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentSimulation;

public class SimMedEquipment {
  private int medID;
  private String medEquipmentType;
  private String status;
  private String currLoc;

  public SimMedEquipment(int medID, String medEquipmentType, String status, String currLoc) {
    this.medID = medID;
    this.medEquipmentType = medEquipmentType;
    this.status = status;
    this.currLoc = currLoc;
  }

  public SimMedEquipment() {}

  @Override
  public String toString() {
    return medID + ", " + medEquipmentType + ", " + status + ", " + currLoc;
  }

  public int getMedID() {
    return medID;
  }

  public void setMedID(int medID) {
    this.medID = medID;
  }

  public String getMedEquipmentType() {
    return medEquipmentType;
  }

  public void setMedEquipmentType(String medEquipmentType) {
    this.medEquipmentType = medEquipmentType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCurrLoc() {
    return currLoc;
  }

  public void setCurrLoc(String currLoc) {
    this.currLoc = currLoc;
  }

  public boolean equals(Object obj) { // this may be better
    SimMedEquipment o = (SimMedEquipment) obj;
    return this.medID == o.medID
        && this.medEquipmentType.equals(o.medEquipmentType)
        && this.status.equals(o.status)
        && this.currLoc.equals(o.currLoc);
  }
}