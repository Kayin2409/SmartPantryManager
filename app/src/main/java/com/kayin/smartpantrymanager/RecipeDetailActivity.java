package com.kayin.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

@SuppressWarnings({"unused", "IfCanBeSwitch"})
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Recipe recipe = findRecipe(dbHelper, recipeId);

        if (recipe != null) {
            setTitle(recipe.getName());
            
            TextView title = findViewById(R.id.text_detail_title);
            TextView ingredientsText = findViewById(R.id.text_detail_ingredients);
            TextView stepsText = findViewById(R.id.text_detail_steps);

            title.setText(recipe.getName());
            stepsText.setText(recipe.getPreparationSteps());

            // Build ingredients string
            List<RecipeIngredient> ingredients = dbHelper.getIngredientsForRecipe(recipeId);
            StringBuilder sb = new StringBuilder();
            for (RecipeIngredient ing : ingredients) {
                String qtyStr = (ing.getQuantity() == Math.floor(ing.getQuantity()))
                        ? ((int) ing.getQuantity()) + ""
                        : ing.getQuantity() + "";

                sb.append("• ").append(ing.getName())
                  .append(" - ").append(qtyStr)
                  .append(" ").append(ing.getUnit()).append("\n");
            }
            ingredientsText.setText(sb.toString());
        }
    }

    private Recipe findRecipe(DatabaseHelper dbHelper, int recipeId) {
        for (Recipe r : dbHelper.getAllRecipes()) {
            if (r.getId() == recipeId) {
                return r;
            }
        }
        return null;
    }
}
