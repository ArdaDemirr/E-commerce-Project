package com.advanced.projectspring.dto;

public class CategorySummaryDTO {
    private Long id;
    private String name;

    public CategorySummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
