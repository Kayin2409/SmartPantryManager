package com.kayin.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "resource"})
public class SuggestedRecipesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggested_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());

        TextView textEmpty = view.findViewById(R.id.text_empty_message);
        View scrollView = view.findViewById(R.id.scroll_view);
        
        TextView labelStrict = view.findViewById(R.id.label_strict_matches);
        RecyclerView recyclerStrict = view.findViewById(R.id.recycler_strict);
        
        TextView labelAlmost = view.findViewById(R.id.label_almost_there);
        RecyclerView recyclerAlmost = view.findViewById(R.id.recycler_almost_there);

        recyclerStrict.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerAlmost.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 1. Load data
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();

        List<Recipe> strictMatches = new ArrayList<>();
        List<Recipe> almostThereMatches = new ArrayList<>();

        // 2. Run Strict Matching Logic
        for (Recipe recipe : allRecipes) {
            List<RecipeIngredient> requiredIngredients = dbHelper.getIngredientsForRecipe(recipe.getId());
            int missingCount = RecipeMatcher.getMissingIngredientCount(requiredIngredients, pantry);

            switch (missingCount) {
                case 0:
                    strictMatches.add(recipe); // Perfect match
                    break;
                case 1:
                    almostThereMatches.add(recipe); // Almost there
                    break;
                default:
                    // If missingCount >= 2, ignore the recipe.
                    break;
            }
        }

        // 3. Update UI based on results
        RecipeAdapter.OnRecipeClickListener clickListener = recipe -> {
            Intent intent = new Intent(requireActivity(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        };

        if (strictMatches.isEmpty() && almostThereMatches.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            // Setup Strict List
            if (!strictMatches.isEmpty()) {
                labelStrict.setVisibility(View.VISIBLE);
                RecipeAdapter strictAdapter = new RecipeAdapter(strictMatches, clickListener);
                recyclerStrict.setAdapter(strictAdapter);
            } else {
                labelStrict.setVisibility(View.GONE);
            }

            // Setup Almost There List
            if (!almostThereMatches.isEmpty()) {
                labelAlmost.setVisibility(View.VISIBLE);
                RecipeAdapter almostAdapter = new RecipeAdapter(almostThereMatches, clickListener);
                recyclerAlmost.setAdapter(almostAdapter);
            } else {
                labelAlmost.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list when returning to this tab
        if (getView() != null) {
            onViewCreated(getView(), null);
        }
    }
}
