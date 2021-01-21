package com.griswold.inventoryManagementSystem.items;

/** A part that is made in-house and has a valid machineID.*/
public class InHouse extends Part{

  private int machineId;

  /** Constructs a new InHouse Part instance and sets all of its member variables.
   * @param id the ID of the part.
   * @param name the name of the part.
   * @param price the price of the part.
   * @param stock the current inventory level of this part.
   * @param min the minimum allowable inventory level of this part.
   * @param max the maximum allowable inventory level of this part.
   * @param machineId the ID of the machine that produced the part.*/
  public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
    super(id, name, price, stock, min, max);
    this.machineId = machineId;
  }

  /** @return the machineId.*/
  public int getMachineId() {
    return machineId;
  }

  /** @param machineId the machineId to set.*/
  public void setMachineId(int machineId) {
    this.machineId = machineId;
  }
}
