package com.griswold.inventoryManagementSystem.menus;

import com.griswold.inventoryManagementSystem.AppManager;
import com.griswold.inventoryManagementSystem.AppManager.MenuType;
import com.griswold.inventoryManagementSystem.items.Inventory;
import com.griswold.inventoryManagementSystem.items.Part;
import com.griswold.inventoryManagementSystem.items.Product;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/** Menu used to add or modify a product in the inventory.*/
public class ProductMenu extends MenuController{

  @FXML
  private Label titleLabel;
  @FXML
  public Button cancelButton;
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
  private TextField searchTextField;
  @FXML
  private Label productErrorLabel;

  // Part table
  @FXML
  private TableView<Part> topTableView;
  @FXML
  private TableColumn<Part, Integer> topIdColumn;
  @FXML
  private TableColumn<Part, String> topNameColumn;
  @FXML
  private TableColumn<Part, Integer> topInvColumn;
  @FXML
  private TableColumn<Part, Double> topPriceColumn;

  // Product table
  @FXML
  private TableView<Part> bottomTableView;
  @FXML
  private TableColumn<Part, Integer>  bottomIdColumn;
  @FXML
  private TableColumn<Part, String> bottomNameColumn;
  @FXML
  private TableColumn<Part, Integer> bottomInvColumn;
  @FXML
  private TableColumn<Part, Double> bottomPriceColumn;


  private Product selectedProduct;
  private Product tempProduct;
  private String name;
  private double price;
  private int inv;
  private int min;
  private int max;

  /** Initializes the menu. This is called once when the application starts.*/
  public void start() {
    setupTables();
  }

  /** Required to make all table elements viewable.*/
  private void setupTables() {
    topIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    topNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    topInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    topPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    topTableView.setItems(Inventory.getAllParts());

    bottomIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    bottomNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    bottomInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    bottomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
  }

  /** Identifies which version the menu should be displayed and populates the menu with information from the selected item.
   * <p>
   * I corrected the duplication issue by creating a temp product to perform part adds
   * and removals on and only committing those changes once the product is saved, discarding them
   * otherwise. This ensures that only valid changes are committed, that they are only
   * performed once and only the intended product is affected.*/
  @Override
  public void open() {
    productErrorLabel.setText("");
    topTableView.setItems(Inventory.getAllParts());
    tempProduct = new Product(0, "", 0, 0, 0, 0);
    if (getMenuVariant() == MenuVariant.MODIFY) {
      titleLabel.setText("Modify Product");
      selectedProduct = ((MainMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.MAIN)).
          getMenuController()).getProductTableView().getSelectionModel().getSelectedItem();
      tempProduct.setId(selectedProduct.getId());
      tempProduct.setName(selectedProduct.getName());
      tempProduct.setPrice(selectedProduct.getPrice());
      tempProduct.setStock(selectedProduct.getStock());
      tempProduct.setMin(selectedProduct.getMin());
      tempProduct.setMax(selectedProduct.getMax());
      tempProduct.getAllAssociatedParts().setAll(selectedProduct.getAllAssociatedParts());

      idTextField.setText(Integer.toString(selectedProduct.getId()));
      nameTextField.setText(selectedProduct.getName());
      invTextField.setText(Integer.toString(selectedProduct.getStock()));
      priceTextField.setText(Double.toString(selectedProduct.getPrice()));
      maxTextField.setText(Integer.toString(selectedProduct.getMax()));
      minTextField.setText(Integer.toString(selectedProduct.getMin()));

      bottomTableView.setItems(tempProduct.getAllAssociatedParts());

    } else if (getMenuVariant() == MenuVariant.ADD){
      titleLabel.setText("Add Product");
      clearFields();
    }
  }

  /** Responds to the cancel button being clicked. Closes the menu without saving any changes.*/
  @FXML
  private void cancelButtonListener() {
    selectedProduct = null;
    AppManager.closeWindow(MenuType.PRODUCT);
  }

  /** Responds to the save button being clicked. Saves the product and closes the window.*/
  @FXML
  private void saveButtonListener() {
    if (!verifyInput()) {
      return;
    }

    MainMenu mainMenu = ((MainMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.MAIN)).getMenuController());
    tempProduct.setName(name);
    tempProduct.setPrice(price);
    tempProduct.setStock(inv);
    tempProduct.setMin(min);
    tempProduct.setMax(max);

    if (getMenuVariant() == MenuVariant.MODIFY) {
      Inventory.updateProduct(Inventory.getAllProducts().indexOf(selectedProduct), tempProduct);
    } else {
      tempProduct.setId(AppManager.nextId());
      Inventory.addProduct(tempProduct);
    }
    mainMenu.getProductTableView().setItems(Inventory.getAllProducts());
    mainMenu.getProductTableView().getSelectionModel().select(tempProduct);
    selectedProduct = null;
    AppManager.closeWindow(MenuType.PRODUCT);
  }

  /** Responds to the add button being clicked. Adds the selected part to the product.*/
  @FXML
  private void addButtonListener() {
    if (topTableView.getSelectionModel().isEmpty()) {
      return;
    }
    tempProduct.addAssociatedPart(topTableView.getSelectionModel().getSelectedItem());
    bottomTableView.setItems(tempProduct.getAllAssociatedParts());
  }

  /** Responds to the remove button being clicked. If a part is currently selected in the part table view
   * the delete menu is opened and passed the selected part.**/
  @FXML
  private void removeButtonListener() {
    resetTable();
    if (bottomTableView.getSelectionModel().isEmpty()) {
      return;
    }
    ((DeleteMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.DELETE)).getMenuController()).
        remove(bottomTableView.getSelectionModel().getSelectedItem());
    AppManager.showWindow(MenuType.DELETE);
  }

  /** Removes the selected part from the product.*/
  public void removePart() {
    tempProduct.deleteAssociatedPart(bottomTableView.getSelectionModel().getSelectedItem());
    bottomTableView.setItems(tempProduct.getAllAssociatedParts());
  }

  /** Responds to the search text field being clicked. Calls resetTable().*/
  @FXML
  private void searchTextFieldClickedListener() {
    resetTable();
  }

  /** Resets the table and clears the selection.*/
  private void resetTable() {
    topTableView.setItems(Inventory.getAllParts());
    topTableView.getSelectionModel().clearSelection();
  }

  /** Responds to action event on the search field. Initiates part lookup in Inventory and displays
   * the results in the available part table view.*/
  @FXML
  private void searchTextFieldListener() {
    productErrorLabel.setText("");
    try {
      Part part = Inventory.lookupPart(Integer.parseInt(searchTextField.getText()));
      if (part != null) {
        topTableView.setItems(FXCollections.observableArrayList(part));
        topTableView.getSelectionModel().select(part);
        searchTextField.clear();
        return;
      }
    } catch (Exception ignored) {

    }
    ObservableList<Part> parts = Inventory.lookupPart(searchTextField.getText());
    topTableView.setItems(parts);
    if (parts.size() > 0) {
      topTableView.getSelectionModel().selectFirst();
    } else {
      productErrorLabel.setText("Your search produced zero results.");
    }
    searchTextField.clear();
  }

  /** Clears all the text fields in the menu.*/
  private void clearFields() {
    idTextField.clear();
    nameTextField.clear();
    invTextField.clear();
    priceTextField.clear();
    maxTextField.clear();
    minTextField.clear();
    productErrorLabel.setText("");
    bottomTableView.getItems().clear();
  }

  /** Verifies that all input parameters are valid.
   * @return true if everything checks out. If not, an error is displayed and false is returned.*/
  private boolean verifyInput() {
    name = nameTextField.getText().trim();
    if (name.isEmpty()) {
      productErrorLabel.setText("ERROR: Name field is required.");
      return false;
    }
    try {
      price = Double.parseDouble(priceTextField.getText());
    } catch (Exception e) {
      productErrorLabel.setText("ERROR: Not a valid price.");
      return false;
    }
    try {
      inv = Integer.parseInt(invTextField.getText());
    } catch (Exception e) {
      productErrorLabel.setText("ERROR: Inventory must be a whole number.");
      return false;
    }
    try {
      min = Integer.parseInt(minTextField.getText());
    } catch (Exception e) {
      productErrorLabel.setText("ERROR: Min must be a whole number.");
      return false;
    }
    try {
      max = Integer.parseInt(maxTextField.getText());
    } catch (Exception e) {
      productErrorLabel.setText("ERROR: Max must be a whole number.");
      return false;
    }

    if (min > max) {
      productErrorLabel.setText("ERROR: Min cannot be greater than Max.");
      return false;
    } else if (inv < min || inv > max) {
      productErrorLabel.setText("ERROR: Inv must be between Min and Max.");
      return false;
    } else if (min < 0) {
      productErrorLabel.setText("ERROR: Min cannot be negative.");
      return false;
    } else if (price < 0) {
      productErrorLabel.setText("ERROR: Price cannot be negative.");
      return false;
    }
    return true;
  }
}