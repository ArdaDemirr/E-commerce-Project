package com.advanced.projectspring.dto;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double unitPrice;
    private Integer stock;
    private String sku;
    private CategorySummaryDTO category;
    private StoreSummaryDTO store;
    private String imageUrl;

    public ProductResponseDTO(Long id, String name, String description,
            Double unitPrice, Integer stock, String sku,
            CategorySummaryDTO category, StoreSummaryDTO store, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.sku = sku;
        this.category = category;
        this.store = store;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public String getSku() {
        return sku;
    }

    public CategorySummaryDTO getCategory() {
        return category;
    }

    public StoreSummaryDTO getStore() {
        return store;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
