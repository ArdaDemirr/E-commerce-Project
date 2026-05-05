package com.advanced.projectspring.dto.corporate;

public class StoreProductsResponseDTO {

    // We send back the database ID so Angular can put it in the URL
    // Example: "Click here to edit -> /store/products/edit/5"
    private Long id;

    private String name;
    private String sku;
    private Double unitPrice;
    private Integer stock;

    // Notice this is a String, not the Category object.
    // The screen only needs to print "Electronics", so we only send the name.
    private String categoryName;

    // Corporate users probably want to know at a glance if their stock is low
    // We can calculate this on the backend (e.g., if stock < 10)
    private boolean isLowStock;

    // Added for UPDATE operations (so Angular can pre-fill the edit form)
    private String description;
    private Long categoryId;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isLowStock() {
        return isLowStock;
    }

    public void setLowStock(boolean isLowStock) {
        this.isLowStock = isLowStock;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
