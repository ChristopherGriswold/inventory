/** The root package of the Inventory Management System.
 * <p>
 * The most challenging error that I had to correct was in regards to the ProductMenu's
 * associated item table-view. I was experiencing undesirable behavior when adding and removing
 * parts from the list. Multiple products were being affected. Details of the correction are
 * provided here: {@link com.griswold.inventoryManagementSystem.menus.ProductMenu#open()
 * com.griswold.inventoryManagementSystem.menus.ProductMenu.open}
 * <p>
 * If I were to update the application I would improve the inventory tracking functionality and
 * make it so that adding a part to a product decreased the available stock and limit the parts
 * that can be added to a product to the stock that is available.
 * */

package com.griswold.inventoryManagementSystem;