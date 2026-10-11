package com.kayin.smartpantrymanager;

@SuppressWarnings("unused")
public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String name;
    private double quantity;
    private String unit;

    public RecipeIngredient() {}

    public RecipeIngredient(int recipeId, String name, double quantity, String unit) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
