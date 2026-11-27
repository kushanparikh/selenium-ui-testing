package io.github.kushanparikh.selenium.pages;

/** Port of the TS FilterType ('az' | 'za' | 'lohi' | 'hilo'): the product-sort dropdown's option values. */
public enum SortOption {
    AZ("az"),
    ZA("za"),
    LOHI("lohi"),
    HILO("hilo");

    private final String value;

    SortOption(String value) {
        this.value = value;
    }

    /** The {@code <option value>} in the sort dropdown. */
    public String value() {
        return value;
    }
}
