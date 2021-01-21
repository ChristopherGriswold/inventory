package com.griswold.inventoryManagementSystem.menus;

import com.griswold.inventoryManagementSystem.AppManager;
import com.griswold.inventoryManagementSystem.AppManager.MenuType;
import com.griswold.inventoryManagementSystem.items.InHouse;
import com.griswold.inventoryManagementSystem.items.Inventory;
import com.griswold.inventoryManagementSystem.items.Outsourced;
import com.griswold.inventoryManagementSystem.items.Part;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/** Menu used to add or modify a part in the inventory.*/
public class PartMenu extends MenuController{


  @FXML
  private RadioButton inHouseRadioButton;
  @FXML
  private RadioButton outsourcedRadioButton;
  @FXML
  private TextField idTextField;
  @FXML
  private TextField nameTextField;
  @FXML
  private TextField invTextField;
  @FXML
  private TextField priceTextField;
  @FXML
  private TextField maxTextField;
  @FXML
  private TextField minTextField;
  @FXML
  private TextField machineIdTextField;
  @FXML
  private Label machineIdLabel;
  @FXML
  private Label partErrorLabel;

  private Part selectedPart;
  private String name;
  private double price;
  private int inv;
  private int min;
  private int max;
  private int machId;
  private String companyName;

  /** Responds to the cancel button being clicked. Closes the menu without saving any changes.*/
  @FXML
  private void cancelButtonListener() {
    AppManager.closeWindow(MenuType.PART);
  }

  /** Responds to the save button being clicked. Saves the part and closes the window.*/
  @FXML
  private void saveButtonListener() {
    if (!verifyInput()) {
      return;
    }

    MainMenu mainMenu = ((MainMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.MAIN)).getMenuController());
    Part part;
    if (inHouseRadioButton.isSelected()) {
      part = new InHouse(AppManager.nextId(), name, price, inv, min, max, machId);
    } else {
      part = new Outsourced(AppManager.nextId(), name, price, inv, min, max, companyName);
    }
    if (getMenuVariant() == MenuVariant.MODIFY) {
      part.setId(selectedPart.getId());
      Inventory.updatePart(Inventory.getAllParts().indexOf(selectedPart), part);
    } else {
      Inventory.addPart(part);
      mainMenu.getPartTableView().setItems(Inventory.getAllParts());
    }
    mainMenu.getPartTableView().setItems(Inventory.getAllParts());
    mainMenu.getPartTableView().getSelectionModel().select(part);
    AppManager.closeWindow(MenuType.PART);
  }

  /** Clears all the text fields in the menu.*/
  private void clearFields() {
    inHouseRadioButton.setSelected(true);
    idTextField.clear();
    nameTextField.clear();
    invTextField.clear();
    priceTextField.clear();
    maxTextField.clear();
    minTextField.clear();
    machineIdTextField.clear();
    partErrorLabel.setText("");
  }


  /** Identifies which version the menu should be displayed and populates the menu with information from the selected item.*/
  public void open() {
    if (getMenuVariant() == MenuVariant.MODIFY) {
      partErrorLabel.setText("");
      selectedPart = ((MainMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.MAIN)).
          getMenuController()).getPartTableView().getSelectionModel().getSelectedItem();
      idTextField.setText(Integer.toString(selectedPart.getId()));
      nameTextField.setText(selectedPart.getName());
      invTextField.setText(Integer.toString(selectedPart.getStock()));
      priceTextField.setText(Double.toString(selectedPart.getPrice()));
      maxTextField.setText(Integer.toString(selectedPart.getMax()));
      minTextField.setText(Integer.toString(selectedPart.getMin()));

      if (selectedPart instanceof InHouse) {
        inHouseRadioButton.setSelected(true);
        machineIdTextField.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
        machineIdLabel.setText("Machine ID");
      } else if (selectedPart instanceof Outsourced){
        outsourcedRadioButton.setSelected(true);
        machineIdTextField.setText(((Outsourced) selectedPart).getCompanyName());
        machineIdLabel.setText("Company Name");
      }
    } else {
      clearFields();
    }
  }

  /** Responds to the in-house radio button being clicked. Sets the text of the machineID label.*/
  @FXML
  private void inHouseRadioButtonListener() {
    machineIdLabel.setText("Machine ID");
  }

  /** Responds to the outsourced radio button being clicked. Sets the text of the company name label.*/
  @FXML
  private void outsourcedRadioButtonListener() {
    machineIdLabel.setText("Company Name");
  }

  /** Verifies that all input parameters are valid.
   * @return true if everything checks out. If not, an error is displayed and false is returned.*/
  private boolean verifyInput() {
    name = nameTextField.getText().trim();
    if (name.isEmpty()) {
      partErrorLabel.setText("ERROR: Name field is required.");
      return false;
    }
    try {
      price = Double.parseDouble(priceTextField.getText());
    } catch (Exception e) {
      partErrorLabel.setText("ERROR: Not a valid price.");
      return false;
    }
    try {
      inv = Integer.parseInt(invTextField.getText());
    } catch (Exception e) {
      partErrorLabel.setText("ERROR: Inventory must be a whole number.");
      return false;
    }
    try {
      min = Integer.parseInt(minTextField.getText());
    } catch (Exception e) {
      partErrorLabel.setText("ERROR: Min must be a whole number.");
      return false;
    }
    try {
      max = Integer.parseInt(maxTextField.getText());
    } catch (Exception e) {
      partErrorLabel.setText("ERROR: Max must be a whole number.");
      return false;
    }
    if (inHouseRadioButton.isSelected()) {
      try {
        machId = Integer.parseInt(machineIdTextField.getText());
      } catch (Exception e) {
        partErrorLabel.setText("ERROR: Machine ID must be a whole number.");
        return false;
      }
    } else {
      companyName = machineIdTextField.getText().trim();
      if (companyName.isEmpty()) {
        partErrorLabel.setText("ERROR: Company Name field is required.");
        return false;
      }
    }

    if (min > max) {
      partErrorLabel.setText("ERROR: Min cannot be greater than Max.");
      return false;
    } else if (inv < min || inv > max) {
      partErrorLabel.setText("ERROR: Inv must be between Min and Max.");
      return false;
    } else if (min < 0) {
      partErrorLabel.setText("ERROR: Min cannot be negative.");
      return false;
    } else if (price < 0) {
      partErrorLabel.setText("ERROR: Price cannot be negative.");
      return false;
    }
    return true;
  }
}
