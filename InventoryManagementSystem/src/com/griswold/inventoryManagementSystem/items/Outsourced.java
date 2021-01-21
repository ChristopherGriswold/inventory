package com.griswold.inventoryManagementSystem.items;

/** A part that is sourced from another company.*/
public class Outsourced extends Part{

  private String companyName;

  /** Constructs a new Outsourced Part instance and sets all of its member variables.
   * @param id the ID of the part.
   * @param name the name of the part.
   * @param price the price of the part.
   * @param stock the current inventory level of this part.
   * @param min the minimum allowable inventory level of this part.
   * @param max the maximum allowable inventory level of this part.
   * @param companyName the name of part supplier.*/
  public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
    super(id, name, price, stock, min, max);
    this.companyName = companyName;
  }

  /** @return the company name.*/
  public String getCompanyName() {
    return companyName;
  }

  /** @param companyName the company name to be set.*/
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

}
