package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.ProductDetailPage;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

/** Port of assertions/productDetailAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class ProductDetailAssertions {

    private final ProductDetailPage productDetailPage;

    public ProductDetailAssertions(ProductDetailPage productDetailPage) {
        this.productDetailPage = productDetailPage;
    }

    /** The TS reads page.url() once; here the URL is waited for, since the click may not have navigated yet. */
    public void verifyProductDetailPageLoaded() {
        productDetailPage.waits().urlContains("/inventory-item.html");
        productDetailPage.waits().until("title containing 'Swag Labs'", d -> d.getTitle().contains("Swag Labs"));
    }

    public void verifyProductInformation(String expectedName, String expectedPrice, String expectedDescription) {
        productDetailPage.waits().text(productDetailPage.getProductName(), expectedName);
        productDetailPage.waits().text(productDetailPage.getProductPrice(), expectedPrice);
        productDetailPage.waits().text(productDetailPage.getProductDescription(), expectedDescription);
    }

    public void verifyProductNameDisplayed(String expectedName) {
        productDetailPage.waits().text(productDetailPage.getProductName(), expectedName);
        productDetailPage.waits().visible(productDetailPage.getProductName());
    }

    public void verifyProductPriceDisplayed(String expectedPrice) {
        productDetailPage.waits().text(productDetailPage.getProductPrice(), expectedPrice);
        productDetailPage.waits().visible(productDetailPage.getProductPrice());
    }

    public void verifyProductDescriptionDisplayed(String expectedDescription) {
        productDetailPage.waits().text(productDetailPage.getProductDescription(), expectedDescription);
        productDetailPage.waits().visible(productDetailPage.getProductDescription());
    }

    /** Playwright's getAttribute('src') returns the raw attribute, so this uses getDomAttribute, not getAttribute. */
    public void verifyProductImageDisplayed() {
        WebElement img = productDetailPage.waits().visible(productDetailPage.getProductImage());

        String src = img.getDomAttribute("src");
        assertThat(src).isNotBlank();
        assertThat(src).doesNotContain("WithGarbageOnItToBreakTheUrl");
    }

    public void verifyAddToCartButtonVisible() {
        productDetailPage.waits().visible(productDetailPage.getAddToCartButton());
    }

    public void verifyRemoveButtonVisible() {
        productDetailPage.waits().visible(productDetailPage.getRemoveButton());
    }

    public void verifyBackToProductsButtonVisible() {
        productDetailPage.waits().visible(productDetailPage.getBackToProductsButton());
    }

    public void verifyCartBadgeCount(String expectedCount) {
        productDetailPage.waits().text(productDetailPage.getCartBadge(), expectedCount);
    }

    /** Playwright's not.toBeVisible(): waits until the badge is removed or hidden. */
    public void verifyCartBadgeNotVisible() {
        productDetailPage.waits().absent(productDetailPage.getCartBadge());
    }

    public void verifyNavigatedBackToInventory() {
        productDetailPage.waits().urlContains("/inventory.html");
        productDetailPage.waits().until("title containing 'Swag Labs'", d -> d.getTitle().contains("Swag Labs"));
    }
}
