package edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService;

import java.util.Objects;

public class LabRequest {
  private int avID;
  private String deviceType;
  private String locID;
  private String description;

  public LabRequest(int avID, String deviceType, String locID, String description) {
    this.avID = avID;
    this.deviceType = deviceType;
    this.locID = locID;
    this.description = description;
  }

  public LabRequest() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LabRequest that = (LabRequest) o;
    return avID == that.avID
        && Objects.equals(deviceType, that.deviceType)
        && Objects.equals(locID, that.locID)
        && Objects.equals(description, that.description);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(avID)
        .append(",")
        .append(deviceType)
        .append(",")
        .append(locID)
        .append(",")
        .append(description);
    return sb.toString();
  }

  public int getAvID() {
    return avID;
  }

  public void setAvID(int avID) {
    this.avID = avID;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getLocID() {
    return locID;
  }

  public void setLocID(String locID) {
    this.locID = locID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
