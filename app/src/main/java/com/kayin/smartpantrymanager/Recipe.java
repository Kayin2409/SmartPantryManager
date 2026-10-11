package com.kayin.smartpantrymanager;

@SuppressWarnings("unused")
public class Recipe {
    private int id;
    private String name;
    private String preparationSteps;

    public Recipe() {}

    public Recipe(String name, String preparationSteps) {
        this.name = name;
        this.preparationSteps = preparationSteps;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}
