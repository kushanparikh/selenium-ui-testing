package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.InventoryPage;
import io.github.kushanparikh.selenium.pages.SortOption;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Port of assertions/inventoryAssertions.ts. Checks that are a retrying expect(locator) in the TS wait here;
 * checks the TS makes on a one-shot allTextContents()/inputValue() read once, after waiting for the elements.
 */
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

    public void verifyCartBadgeCount(String expectedCount) {
        inventoryPage.waits().text(inventoryPage.getCartBadge(), expectedCount);
    }

    public void verifyCartLinkContainsCount(String expectedCount) {
        inventoryPage.waits().containsText(inventoryPage.getCartLink(), expectedCount);
    }

    public void verifyRemoveButtonText(String itemTestId, String expectedText) {
        inventoryPage.waits().text(inventoryPage.getRemoveButton(itemTestId), expectedText);
    }

    public void verifyAddToCartButtonExists(String itemTestId) {
        inventoryPage.waits().visible(inventoryPage.getAddToCartButton(itemTestId));
    }

    public void verifyRemoveButtonExists(String itemTestId) {
        inventoryPage.waits().visible(inventoryPage.getRemoveButton(itemTestId));
    }

    /** Waits for the badge to go away; passes at once if it was never there. */
    public void verifyCartBadgeNotVisible() {
        inventoryPage.waits().absent(inventoryPage.getCartBadge());
    }

    public void verifyProductsSorted(SortOption sortOrder) {
        switch (sortOrder) {
            case LOHI, HILO -> verifyProductsSortedByPrice(sortOrder);
            case AZ, ZA -> verifyProductsSortedByAlphabet(sortOrder);
        }
    }

    private void verifyProductsSortedByPrice(SortOption sortOrder) {
        List<Double> prices = texts(inventoryPage.waits().all(inventoryPage.getItemPriceLocator())).stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();

        if (sortOrder == SortOption.LOHI) {
            assertThat(prices).isSorted();
        } else {
            assertThat(prices).isSortedAccordingTo(Comparator.reverseOrder());
        }
    }

    /** JS Array.sort() and Java String.compareTo both order by UTF-16 code unit, so the expected lists match. */
    private void verifyProductsSortedByAlphabet(SortOption sortOrder) {
        List<String> names = texts(inventoryPage.waits().all(inventoryPage.getItemNameLocator()));

        Comparator<String> order = sortOrder == SortOption.AZ ? Comparator.naturalOrder() : Comparator.reverseOrder();
        assertThat(names).isEqualTo(names.stream().sorted(order).toList());
    }

    public void verifyInventoryItemCount(int expectedCount) {
        inventoryPage.waits().until("exactly " + expectedCount + " inventory items",
                d -> d.findElements(inventoryPage.getAllInventoryItems()).size() == expectedCount);
    }

    public void verifyProductNamesExist(List<String> expectedNames) {
        List<String> names = texts(inventoryPage.waits().all(inventoryPage.getItemNameLocator()));
        for (String name : expectedNames) {
            assertThat(names).contains(name);
        }
    }

    public void verifyProductPrices(List<String> expectedPrices) {
        List<String> prices = texts(inventoryPage.waits().all(inventoryPage.getItemPriceLocator()));
        for (String price : expectedPrices) {
            assertThat(prices).contains(price);
        }
    }

    /**
     * Like the TS images.count(): a one-shot read, no wait. Note: saucedemo has no [data-test="inventory-item-img"]
     * (its images carry per-item ids such as inventory-item-sauce-labs-backpack-img), so the TS loop runs zero
     * times and the check passes vacuously. Kept 1:1; fixing the locator is a change to the TS suite first.
     */
    public void verifyAllProductImagesDisplayed() {
        List<WebElement> images = inventoryPage.waits().until("product image lookup",
                d -> d.findElements(inventoryPage.getInventoryItemImage()));

        for (WebElement img : images) {
            inventoryPage.waits().until("visibility of product image", ExpectedConditions.visibilityOf(img));

            // getDomAttribute, like Playwright's getAttribute: the src as written, not the resolved URL property.
            String src = img.getDomAttribute("src");
            assertThat(src).isNotBlank();
            assertThat(src).doesNotContain("WithGarbageOnItToBreakTheUrl");
        }
    }

    /** Playwright's inputValue(): the select's current value property. */
    public void verifyDefaultSortOrder() {
        String sortValue = inventoryPage.waits().visible(inventoryPage.getProductSortContainer()).getDomProperty("value");
        assertThat(sortValue).isEqualTo(SortOption.AZ.value());
    }

    private static List<String> texts(List<WebElement> elements) {
        return elements.stream().map(WebElement::getText).toList();
    }
}
