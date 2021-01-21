package com.griswold.inventoryManagementSystem;

import com.griswold.inventoryManagementSystem.menus.MenuController;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;



/** The application's entry point and Main controller.*/
public class AppManager extends Application {

    /** Used to tag menu windows as belonging to a specific type.*/
    public enum MenuType {
        MAIN,
        PART,
        PRODUCT,
        DELETE
    }

    private static int idCounter;
    private static Stage primaryStage;

    private static final ArrayList<Window> windows = new ArrayList<>();
    private static final int PRIMARY_MIN_WIDTH = 600;
    private static final int PRIMARY_MIN_HEIGHT = 360;
    private static final int PRODUCT_MIN_WIDTH = 520;
    private static final int PRODUCT_MIN_HEIGHT = 600;

    /** Required by the JVM to launch the application.
     * @param args Command line arguments.*/
    public static void main(String[] args) {
        launch(args);
    }

    /** The main entry point for all JavaFX applications. The start method is called after the
     * init method has returned, and after the system is ready for the application to begin running.
     NOTE: This method is called on the JavaFX Application Thread.
     @param stage The primary stage for this application, onto which the application scene can be set.
     Applications may create other stages, if needed, but they will not be primary stages.*/
    @Override
    public void start(Stage stage) throws Exception{
        primaryStage = stage;
        setWindows();
        primaryStage.setScene(Objects.requireNonNull(getWindow(MenuType.MAIN)).getScene());
        primaryStage.setMinHeight(PRIMARY_MIN_HEIGHT);
        primaryStage.setMinWidth(PRIMARY_MIN_WIDTH);
        primaryStage.show();
    }

    /** Creates all menu windows that will be available during the application's lifecycle
     * and adds them to the static windows arrayList.*/
    private void setWindows() throws Exception{
        windows.add(new Window(MenuType.MAIN, "MainMenu"));
        windows.add(new Window(MenuType.PART, "PartMenu", true, false));
        windows.add(new Window(MenuType.PRODUCT, "ProductMenu", true, true,
            PRODUCT_MIN_HEIGHT, PRODUCT_MIN_WIDTH));
        windows.add(new Window(MenuType.DELETE, "DeleteMenu", true, false));
    }

    /** Retrieves the first element of the windows arrayList with the matching MenuType.
     * Returns null if no matching window is found.
     * @param menuType the type of Menu to retrieve.
     * @return the first matching window.*/
    public static Window getWindow(MenuType menuType) {
        for(Window window : windows) {
            if (window.getMenuType() == menuType) {
                return window;
            }
        }
        return null;
    }

    /** Gets the first window of the specified MenuType and displays it on the screen.
     * @param menuType the type of menu to display.*/
    public static void showWindow(MenuType menuType) {
        for(Window window : windows) {
            if (window.getMenuType() == menuType) {
                if (window.stage == null) {
                    primaryStage.setScene(window.getScene());
                    return;
                } else {
                    window.getMenuController().open();
                    window.stage.show();
                }
            }
        }
    }

    /** Gets the first window of the specified MenuType and closes it. If no corresponding
     * open window is found this method does nothing.
     * @param menuType the type of menu to display.*/
    public static void closeWindow(MenuType menuType) {
        for(Window window : windows) {
            if (window.getMenuType() == menuType) {
                window.stage.close();
                return;
            }
        }
    }

    /** Closes the primary window and exits the application.*/
    public static void exitApplication() {
        primaryStage.close();
    }


    /** Used to generate unique part and product Ids.
     * @return the next available ID.*/
    public static int nextId() {
        return ++idCounter;
    }


    /** A container for all of a menu's elements.*/
    public static class Window {
        private String name;
        private final MenuType menuType;
        private final MenuController menuController;
        private final Scene scene;
        private Stage stage;

        /** Constructs a new Window instance.
         * @param  menuType the window's MenuType.
         * @param  name the name of the window.*/
        public Window(MenuType menuType, String name) throws Exception{
            this(menuType, name, false, true);
        }

        /** Constructs a new Window instance.
         * @param  menuType the window's MenuType.
         * @param  name the name of the window.
         * @param isSeparate inhabits separate stage.
         * @param isResizable can be resized by user.*/
        public Window(MenuType menuType, String name, boolean isSeparate, boolean isResizable) throws Exception{
            this(menuType, name, isSeparate, isResizable, 0, 0);
        }

        /** Constructs a new Window instance.
         * @param  menuType the window's MenuType.
         * @param  name the name of the window.
         * @param isSeparate inhabits separate stage.
         * @param isResizable can be resized by user.
         * @param  minHeight minimum allowable height of window.
         * @param  minWidth minimum allowable width of window.*/
        public Window(MenuType menuType, String name, boolean isSeparate, boolean isResizable, int minHeight,
            int minWidth) throws Exception{
            this.name = name;
            this.menuType = menuType;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menus/layouts/" + name + ".fxml"));
            Parent root = loader.load();
            menuController = loader.getController();
            scene = new Scene(root);
            if (isSeparate) {
                stage = new Stage();
                if(!isResizable) {
                    stage.setResizable(false);
                }
                stage.setMinWidth(minWidth);
                stage.setMinHeight(minHeight);
                stage.setScene(scene);
                stage.initOwner(primaryStage);
                stage.initModality(Modality.WINDOW_MODAL);
            }
            menuController.start();
        }

        /** @param name the name to set.*/
        public void setName(String name) {
            this.name = name;
        }

        /** @return the name.*/
        public String getName() {
            return name;
        }

        /** @return the scene.*/
        public Scene getScene() {
            return scene;
        }

        /** @return the menuController.*/
        public MenuController getMenuController() {
            return menuController;
        }

        /** @return the menuType.*/
        public MenuType getMenuType() {
            return menuType;
        }
    }
}
