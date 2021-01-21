package com.griswold.inventoryManagementSystem.menus;

/** Defines methods and variables common to all menus.*/
public abstract class MenuController {

  /** Tag used to indicated which version of a menu should be displayed.*/
  public enum MenuVariant {
    ADD,
    MODIFY
  }

  private MenuVariant menuVariant;

  /** Used to perform initialization tasks for the menu. This method is called when the menu is created.*/
  public void start() {

  }

  /** This method is called every time the menu is shown.*/
  public void open() {

  }

  /** @return the menuVariant.*/
  public MenuVariant getMenuVariant() {
    return menuVariant;
  }

  /** @param menuVariant the menuVariant to set.*/
  public void setMenuVariant(MenuVariant menuVariant) {
    this.menuVariant = menuVariant;
  }
}
