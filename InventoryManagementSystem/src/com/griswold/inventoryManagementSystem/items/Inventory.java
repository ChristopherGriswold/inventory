package com.griswold.inventoryManagementSystem.items;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Application-wide database that holds all instantiated Parts and Products.*/
public class Inventory {
  private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
  private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

  /** @param part the Part to add.*/
  public static void addPart(Part part) {
    allParts.add(part);
  }

  /** @param product the Product to add.*/
  public static void addProduct(Product product) {
    allProducts.add(product);
  }

  /** Lookup part by ID.
   * @param partId the ID of the part to lookup.
   * @return the Part associated with the provided partID. Returns null if no part is found.*/
  public static Part lookupPart(int partId) {
    for (Part part : allParts) {
      if (part.getId() == partId) {
        return part;
      }
    }
    return null;
  }

  /** Lookup product by ID.
   * @param productId the ID of the part to lookup.
   * @return the Product associated with the provided productID. Returns null if no product is found.*/
  public static Product lookupProduct(int productId) {
    for (Product product : allProducts) {
      if (product.getId() == productId) {
        return product;
      }
    }
    return null;
  }

  /** Find all parts that contain the provided string.
   * @param partName search string.
   * @return an ObservableList of type Part that contains all parts in the database matching the
   * search criteria. Returns an empty list if no parts are found.*/
  public static ObservableList<Part> lookupPart(String partName) {
    ObservableList<Part> returnList = FXCollections.observableArrayList();
    for(Part part : allParts) {
      if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
        returnList.add(part);
      }
    }
    return returnList;
  }

  /** Find all products that contain the provided string.
   * @param productName search string.
   * @return an ObservableList of type Product that contains all products in the database matching the
   * search criteria. Returns an empty list if no products are found.*/
  public static ObservableList<Product> lookupProduct(String productName) {
    ObservableList<Product> returnList = FXCollections.observableArrayList();
    for(Product product : allProducts) {
      if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
        returnList.add(product);
      }
    }
    return returnList;
  }

  /** Replaces the part at the specified index with the part provided.
   * @param index index of part to be replaced.
   * @param newPart new part to be inserted.*/
  public static void updatePart(int index, Part newPart) {
    allParts.set(index, newPart);
  }

  /** Replaces the product at the specified index with the product provided.
   * @param index index of part to be replaced.
   * @param newProduct new product to be inserted.*/
  public static void updateProduct(int index, Product newProduct) {
    allProducts.set(index, newProduct);
  }

  /** Remove a part from the part list.
   * @param selectedPart the part to be removed.
   * @return true if part was successfully removed. Returns false if part does not exist in list.*/
  public static boolean deletePart(Part selectedPart) {
    for (Part part : allParts) {
      if (part.getId() == selectedPart.getId()) {
        allParts.remove(part);
        return true;
      }
    }
    return false;
  }

  /** Remove a product from the product list.
   * @param selectedProduct the part to be removed.
   * @return true if product was successfully removed. Returns false if product does not exist in list.*/
  public static boolean deleteProduct(Product selectedProduct) {
    for (Product product : allProducts) {
      if (product.getId() == selectedProduct.getId()) {
        allProducts.remove(product);
        return true;
      }
    }
    return false;
  }

  /** @return the entire part list.*/
  public static ObservableList<Part> getAllParts() {
    return allParts;
  }

  /** @return the entire product list.*/
  public static ObservableList<Product> getAllProducts() {
    return allProducts;
  }
}
