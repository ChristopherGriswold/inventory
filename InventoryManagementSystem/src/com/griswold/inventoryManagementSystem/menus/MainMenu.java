package com.griswold.inventoryManagementSystem.menus;
import com.griswold.inventoryManagementSystem.AppManager;
import com.griswold.inventoryManagementSystem.AppManager.MenuType;
import com.griswold.inventoryManagementSystem.items.InHouse;
import com.griswold.inventoryManagementSystem.items.Inventory;
import com.griswold.inventoryManagementSystem.items.Outsourced;
import com.griswold.inventoryManagementSystem.items.Part;
import com.griswold.inventoryManagementSystem.items.Product;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/** The application's main GUI.*/
public class MainMenu extends MenuController{
  @FXML
  private Label mainErrorLabel;
  @FXML
  private TextField partSearchField;
  @FXML
  private TextField productSearchField;

  // Part table
  @FXML
  private TableView<Part> partTableView;
  @FXML
  private TableColumn<Part, Integer> partIdColumn;
  @FXML
  private TableColumn<Part, String> partNameColumn;
  @FXML
  private TableColumn<Part, Integer> partInvColumn;
  @FXML
  private TableColumn<Part, Double> partPriceColumn;

  // Product table
  @FXML
  private TableView<Product> productTableView;
  @FXML
  private TableColumn<Product, Integer>  productIdColumn;
  @FXML
  private TableColumn<Product, String> productNameColumn;
  @FXML
  private TableColumn<Product, Integer> productInvColumn;
  @FXML
  private TableColumn<Product, Double> productPriceColumn;

  /** Initializes the menu. This is called once when the application starts.*/
  public void start() {
    addFakeTableData();
    setupTables();
    clearErrorLabel();
  }

  /** Required to make all table elements viewable.*/
  private void setupTables() {

    // Part Table
    partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    partTableView.setItems(Inventory.getAllParts());

    // Product Table
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    productTableView.setItems(Inventory.getAllProducts());

  }

  /** Initializes the part and product tables with dummy data.*/
  private void addFakeTableData() {
    // Fake Parts
    Inventory.addPart(new Outsourced(AppManager.nextId(), "FirstPart", 22.95, 1, 1, 10, "Griswold"));
    Inventory.addPart(new Outsourced(AppManager.nextId(), "SecondPart", 32.95, 1, 1, 10, "JoeCo"));
    Inventory.addPart(new InHouse(AppManager.nextId(), "ThirdPart", 129.99, 1, 1, 10, 558));

    // Fake Products
    Inventory.addProduct(new Product(AppManager.nextId(), "Product X", 19.20, 3, 1, 20));
    Inventory.addProduct(new Product(AppManager.nextId(), "Sample", 11.99, 6, 5, 20));
    Inventory.addProduct(new Product(AppManager.nextId(), "Other Product", 139.50, 3, 1, 20));
  }

  /** Refreshes part and product tables with data from the Inventory.*/
  private void updateTables() {
    productTableView.setItems(Inventory.getAllProducts());
    partTableView.setItems(Inventory.getAllParts());
  }

  /** Responds to add part button being clicked. Opens the add part menu.*/
  @FXML
  private void addPartButtonListener() {
    updateTables();
    clearErrorLabel();
    PartMenu partMenu = (PartMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.PART)).getMenuController();
    partMenu.setMenuVariant(MenuVariant.ADD);
    AppManager.showWindow(MenuType.PART);
    partTableView.getSelectionModel().clearSelection();
  }

  /** Responds to modify part button being clicked. Opens the modify part menu.*/
  @FXML
  private void modifyPartButtonListener() {
    updateTables();
    clearErrorLabel();
    if (partTableView.getSelectionModel().isEmpty()) {
      return;
    }
    PartMenu partMenu = (PartMenu) Objects.requireNonNull(AppManager.getWindow(MenuType.PART)).getMenuController();
    partMenu.setMenuVariant(MenuVariant.MODIFY);
    AppManager.showWindow(MenuType.PART);
  }

  /** Responds to add product button being clicked. Opens the add product menu.*/
  @FXML
  private void addProductButtonListener() {
    updateTables();
    clearErrorLabel();
    ProductMenu productMenu = (ProductMenu) Objects
        .requireNonNull(AppManager.getWindow(MenuType.PRODUCT)).getMenuController();
    productMenu.setMenuVariant(MenuVariant.ADD);
    AppManager.showWindow(MenuType.PRODUCT);
    productTableView.getSelectionModel().clearSelection();
  }

  /** Responds to modify product button being clicked. Opens the modify product menu.*/
  @FXML
  private void modifyProductButtonListener() {
    updateTables();
    clearErrorLabel();
    if (productTableView.getSelectionModel().isEmpty()) {
      return;
    }
    ProductMenu productMenu = (ProductMenu) Objects
        .requireNonNull(AppManager.getWindow(MenuType.PRODUCT)).getMenuController();
    productMenu.setMenuVariant(MenuVariant.MODIFY);
    AppManager.showWindow(MenuType.PRODUCT);
  }

  /** Responds to action event on the part search field. Initiates part lookup in Inventory and displays
   * the results in the part table view.*/
  @FXML
  private void partSearchFieldListener() {
    clearErrorLabel();
    try {
      Part part = Inventory.lookupPart(Integer.parseInt(partSearchField.getText()));
      if (part != null) {
        partTableView.setItems(FXCollections.observableArrayList(part));
        partTableView.getSelectionModel().select(part);
        partSearchField.clear();
        return;
      }
    } catch (Exception ignored) {

    }
    ObservableList<Part> parts = Inventory.lookupPart(partSearchField.getText());
    partTableView.setItems(parts);
    if (parts.size() > 0) {
      partTableView.getSelectionModel().selectFirst();
    } else {
      mainErrorLabel.setText("Your search produced zero results.");
    }
    partSearchField.clear();
  }

  /** Responds to action event on the product search field. Initiates product lookup in Inventory and displays
   * the results in the product table view.*/
  @FXML
  private void productSearchFieldListener() {
    clearErrorLabel();
    try {
      Product product = Inventory.lookupProduct(Integer.parseInt(productSearchField.getText()));
      if (product != null) {
        productTableView.setItems(FXCollections.observableArrayList(product));
        productTableView.getSelectionModel().select(product);
        productSearchField.clear();
        return;
      }
    } catch (Exception ignored) {

    }
    ObservableList<Product> products = Inventory.lookupProduct(productSearchField.getText());
    productTableView.setItems(products);
    if (products.size() > 0) {
      productTableView.getSelectionModel().selectFirst();
    } else {
      mainErrorLabel.setText("Your search produced zero results.");
    }
    productSearchField.clear();
  }

  /** Responds to delete part button being clicked. If a part is currently selected in the part table view
   * the delete menu is opened and passed the selected part.*/
  @FXML
  private void partDeleteButtonListener() {
    updateTables();
    clearErrorLabel();
    if (partTableView.getSelectionModel().isEmpty()) {
      return;
    }
    DeleteMenu deleteMenu = ((DeleteMenu) Objects
        .requireNonNull(AppManager.getWindow(MenuType.DELETE)).getMenuController());
    deleteMenu.delete(partTableView.getSelectionModel().getSelectedItem());
    AppManager.showWindow(MenuType.DELETE);
  }

  /** Responds to delete product button being clicked. If a product is currently selected in the product table view
   * the delete menu is opened and passed the selected product.*/
  @FXML
  private void productDeleteButtonListener() {
    updateTables();
    clearErrorLabel();
    if (productTableView.getSelectionModel().isEmpty() ) {
      return;
    } else if (productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() > 0) {
      mainErrorLabel.setText("ERROR: Cannot delete product containing associated parts.");
      return;
    }
    DeleteMenu deleteMenu = ((DeleteMenu) Objects
        .requireNonNull(AppManager.getWindow(MenuType.DELETE)).getMenuController());
    deleteMenu.delete(productTableView.getSelectionModel().getSelectedItem());
    AppManager.showWindow(MenuType.DELETE);
  }

  /** Responds to exit part button being clicked. Tells the application manager to exit the application.*/
  @FXML
  private void exitButtonListener() {
    AppManager.exitApplication();
  }

  /** Responds to the part search field being clicked. This resets the part table and clears the selection.*/
  @FXML
  private void partSearchFieldClickedListener() {
    updateTables();
    partTableView.getSelectionModel().clearSelection();
    clearErrorLabel();
  }

  /** Responds to the product search field being clicked. This resets the product table and clears the selection.*/
  @FXML
  private void productSearchFieldClickedListener() {
    updateTables();
    productTableView.getSelectionModel().clearSelection();
    clearErrorLabel();
  }

  /** Sets the text of the main error label to an empty string.*/
  private void clearErrorLabel() {
    mainErrorLabel.setText("");
  }

  /**@return the partTableView*/
  public TableView<Part> getPartTableView() {
    return partTableView;
  }

  /**@return the productTableView*/
  public TableView<Product> getProductTableView() {
    return productTableView;
  }
}
