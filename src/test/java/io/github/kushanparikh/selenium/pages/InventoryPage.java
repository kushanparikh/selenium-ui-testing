package io.github.kushanparikh.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Port of pages/inventoryPage.ts (completed in the inventory task). */
public class InventoryPage extends BasePage {

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    /** Playwright getByText('Products'): an element whose text is "Products". */
    public By getSecondaryTitle() {
        return By.xpath("//*[normalize-space(text())='Products']");
    }

    public By getProductImages() {
        return By.cssSelector(".inventory_item_img img");
    }
}
