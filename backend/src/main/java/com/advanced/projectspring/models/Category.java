package com.advanced.projectspring.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity
@Table(name = "categories")
public class Category {
    @Id // mark as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto create/increment
    private Long id;

    @Column(nullable = false, unique = true) // not null and unique
    private String name;

    // self-referencing many-to-one relationship
    @ManyToOne // Many subcategories can belong to One parent category
    @JoinColumn(name = "parent_id") // the column name in the database - links them
    private Category parent; // parent is actually another category? yes

    // Constructors
    public Category() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
