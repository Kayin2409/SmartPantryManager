package com.kayin.smartpantrymanager;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("all")
public class RecipeMatcher {

    // Normalizes ingredient names (e.g., "Tomatoes" -> "tomato")
    public static String normalizeName(String name) {
        if (name == null) return "";
        String lower = name.toLowerCase(Locale.ROOT).trim();
        // Handle simple plurals
        if (lower.endsWith("es") && lower.length() > 3) {
            return lower.substring(0, lower.length() - 2); // tomatoes -> tomato
        } else if (lower.endsWith("s") && !lower.endsWith("ss") && lower.length() > 3) {
            return lower.substring(0, lower.length() - 1); // eggs -> egg
        }
        return lower;
    }

    // Converts quantities to a base unit (grams for weight, ml for volume) for fair comparison
    public static double convertToBaseUnit(double quantity, String unit) {
        if (unit == null) return quantity;
        switch (unit.toLowerCase(Locale.ROOT).trim()) {
            case "kg":
            case "l":
                return quantity * 1000;
            case "tbsp":
                return quantity * 15; // 1 tbsp = 15 ml
            case "tsp":
                return quantity * 5;   // 1 tsp = 5 ml
            case "g":
            case "ml":
            case "unit":
            case "units":
            default:
                return quantity;
        }
    }

    /**
     * Returns how many ingredients are missing or insufficient.
     * 0 means it's a strict match.
     * 1 means it's an "Almost There" match.
     */
    public static int getMissingIngredientCount(List<RecipeIngredient> requiredIngredients, List<PantryItem> pantryItems) {
        int missingCount = 0;

        for (RecipeIngredient required : requiredIngredients) {
            boolean found = false;
            String reqName = normalizeName(required.getName());
            double reqQtyBase = convertToBaseUnit(required.getQuantity(), required.getUnit());

            for (PantryItem pantryItem : pantryItems) {
                String panName = normalizeName(pantryItem.getName());
                if (Objects.equals(panName, reqName)) {
                    double panQtyBase = convertToBaseUnit(pantryItem.getQuantity(), pantryItem.getUnit());
                    // If the pantry has enough of this ingredient
                    if (panQtyBase >= reqQtyBase) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                missingCount++;
            }
        }
        return missingCount;
    }
}
