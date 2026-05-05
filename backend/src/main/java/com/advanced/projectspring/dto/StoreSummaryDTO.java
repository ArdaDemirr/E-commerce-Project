package com.advanced.projectspring.dto;

public class StoreSummaryDTO {
    private Long id;
    private String name;
    private String status;

    public StoreSummaryDTO(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
