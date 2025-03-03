package org.saltaonelove.model;

public class TrainingType {
    private Long id;

    private String name;

    public TrainingType() {
    }

    public TrainingType(String name) {
        this.name = name;
    }

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
}
