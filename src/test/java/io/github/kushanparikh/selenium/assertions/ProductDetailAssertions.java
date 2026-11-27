package io.github.kushanparikh.selenium.assertions;

import io.github.kushanparikh.selenium.pages.ProductDetailPage;

/** Port of assertions/productDetailAssertions.ts. Each check waits, like Playwright's retrying expect(). */
public class ProductDetailAssertions {

    private final ProductDetailPage productDetailPage;

    public ProductDetailAssertions(ProductDetailPage productDetailPage) {
        this.productDetailPage = productDetailPage;
    }
}
