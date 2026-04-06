package com.advanced.projectspring.dto.corporate;

public class StoreProductsRequestDTO {

    // The user types the product name into a text box
    private String name;

    // The user types the SKU (barcode) into a text box
    private String sku;

    // The user sets the price
    private Double unitPrice;

    // The user sets how many items are in their warehouse
    private Integer stock;

    // The user writes a description for the item
    private String description;

    // The user selects a category from a dropdown list.
    // We ONLY need the ID of the category from Angular, not the full Category
    // object!
    private Long categoryId;

    // Notice what is MISSING:
    // 1. We don't ask for storeId (we get that from their secure login token)
    // 2. We don't ask for productId (because it hasn't been created in the DB yet)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
