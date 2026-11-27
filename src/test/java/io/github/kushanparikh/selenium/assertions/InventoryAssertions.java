package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.InventoryPage;

/** Port of assertions/inventoryAssertions.ts (completed in the inventory task). */
public class InventoryAssertions {

    private final InventoryPage inventoryPage;

    public InventoryAssertions(InventoryPage inventoryPage) {
        this.inventoryPage = inventoryPage;
    }

    public void verifyInventoryLandingDetails() {
        inventoryPage.waits().urlContains("/inventory");
        inventoryPage.waits().until("title containing 'Swag Labs'", d -> d.getTitle().contains("Swag Labs"));
        inventoryPage.waits().visible(inventoryPage.getSecondaryTitle());
    }
}
