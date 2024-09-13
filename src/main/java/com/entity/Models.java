package com.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "model")
public class Models implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 45, nullable = false)
    private String name;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Models() {
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category_id) {
        this.category = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
