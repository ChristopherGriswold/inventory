package com.griswold.inventoryManagementSystem.menus;

import com.griswold.inventoryManagementSystem.AppManager;
import com.griswold.inventoryManagementSystem.AppManager.MenuType;
import com.griswold.inventoryManagementSystem.items.Inventory;
import com.griswold.inventoryManagementSystem.items.Part;
import com.griswold.inventoryManagementSystem.items.Product;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/** A dialogue menu used to confirm the deletion or removal of selected parts or products.*/
public class DeleteMenu extends MenuController {

  @FXML
  private Label titleLabel;
  @FXML
  private Label textLabel;
  @FXML
  private Button deleteButton;

  private Part partToDelete;
  private Part partToRemove;
  private Product productToDelete;
  private String itemName;


  /** Sets the menu's text elements.*/
  public void start() {
    titleLabel.setText("Confirm Delete");
    deleteButton.setText("Delete");
    textLabel.setText(itemName + " will be deleted.\nWarning: This cannot be undone!");
  }


  /** Notifies the menu that a delete operation has been requested.
   * @param part the Part to be deleted.*/
  public void delete(Part part) {
    partToDelete = part;
    itemName = part.getName();
    start();
  }

  /** Notifies the menu that a delete operation has been requested.
   * @param product the Product to be deleted.*/
  public void delete(Product product) {
    productToDelete = product;
    itemName = product.getName();
    start();
  }

  /** Notifies the menu that a remove operation has been requested.
   * @param part the Part to be removed.*/
  public void remove(Part part) {
    partToRemove = part;
    itemName = part.getName();
    textLabel.setText(itemName + " will be removed.");
    deleteButton.setText("Remove");
    titleLabel.setText("Confirm Remove");
  }

  /** Responds to a click on the delete button. Performs the pending operation and closes the window.*/
  @FXML
  private void deleteButtonListener() {
    MainMenu mainMenu = ((MainMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.MAIN)).getMenuController());
    if (partToRemove != null) {
      ((ProductMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.PRODUCT)).getMenuController()).removePart();
      partToRemove = null;
    } else if (productToDelete != null) {
      Inventory.deleteProduct(productToDelete);
      mainMenu.getProductTableView().setItems(Inventory.getAllProducts());
      mainMenu.getProductTableView().getSelectionModel().clearSelection();
      productToDelete = null;
    } else if (partToDelete != null) {
      Inventory.deletePart(partToDelete);
      mainMenu.getPartTableView().setItems(Inventory.getAllParts());
      mainMenu.getPartTableView().getSelectionModel().clearSelection();
      partToDelete = null;
    }
    AppManager.closeWindow(MenuType.DELETE);
  }

  /** Responds to a click on the cancel button. Cancels the pending operation and closes the window.*/
  @FXML
  private void cancelButtonListener() {
    partToRemove = null;
    productToDelete = null;
    partToDelete = null;
    AppManager.closeWindow(MenuType.DELETE);
  }
}
