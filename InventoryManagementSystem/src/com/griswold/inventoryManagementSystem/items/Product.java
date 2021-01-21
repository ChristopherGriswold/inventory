package com.griswold.inventoryManagementSystem.items;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** An entity that may contain multiple parts.*/
public class Product {

  private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
  private int id;
  private String name;
  private double price;
  private int stock;
  private int min;
  private int max;

  /** Constructs a new product instance and sets all of its member variables.
   * @param id the ID of the product.
   * @param name the name of the product.
   * @param price the price of the product.
   * @param stock the current inventory level of this product.
   * @param min the minimum allowable inventory level of this product.
   * @param max the maximum allowable inventory level of this product.*/
  public Product(int id, String name, double price, int stock, int min, int max) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.min = min;
    this.max = max;
  }

  /** Adds a part to the associated part list.
   * @param part the part to add.*/
  public void addAssociatedPart(Part part) {
    Part newPart = null;
    if (part instanceof InHouse) {
      newPart = new InHouse(part.getId(), part.getName(), part.getPrice(), part.getStock(),
          part.getMin(), part.getMax(), ((InHouse) part).getMachineId());
    } else if (part instanceof Outsourced) {
      newPart = new Outsourced(part.getId(), part.getName(), part.getPrice(), part.getStock(), 
          part.getMin(), part.getMax(), ((Outsourced) part).getCompanyName());
    }
    associatedParts.add(newPart);
  }

  /** Finds the first instance of the specified part in the part list and removes it.
   * @param selectedAssociatedPart the part to delete.
   * @return true if the deletion was successful. Returns false if the part does not exist in the list.*/
  public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
    for (int i = 0; i < associatedParts.size(); i++) {
      if (associatedParts.get(i).getId() == selectedAssociatedPart.getId()) {
        associatedParts.remove(i);
        return true;
      }
    }
    return false;
  }

  /** @return the associated parts list.*/
  public ObservableList<Part> getAllAssociatedParts() {
    return associatedParts;
  }

  /** @return the id.*/
  public int getId() {
    return id;
  }

  /** @param id the id to set.*/
  public void setId(int id) {
    this.id = id;
  }

  /** @return the name.*/
  public String getName() {
    return name;
  }

  /** @param name the name to set.*/
  public void setName(String name) {
    this.name = name;
  }

  /** @return the price.*/
  public double getPrice() {
    return price;
  }

  /** @param price the price to set.*/
  public void setPrice(double price) {
    this.price = price;
  }

  /** @return the stock.*/
  public int getStock() {
    return stock;
  }

  /** @param stock the stock to set.*/
  public void setStock(int stock) {
    this.stock = stock;
  }

  /** @return the min.*/
  public int getMin() {
    return min;
  }

  /** @param min the min to set.*/
  public void setMin(int min) {
    this.min = min;
  }

  /** @return the max.*/
  public int getMax() {
    return max;
  }

  /** @param max the max to set.*/
  public void setMax(int max) {
    this.max = max;
  }

}
