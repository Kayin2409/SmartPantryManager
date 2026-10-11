package com.kayin.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Pantry Columns
    public static final String COL_PANTRY_ID = "_id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipe Columns
    public static final String COL_RECIPE_ID = "_id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "preparation_steps";

    // Recipe Ingredient Columns
    public static final String COL_ING_ID = "_id";
    public static final String COL_ING_RECIPE_ID = "recipe_id";
    public static final String COL_ING_NAME = "ingredient_name";
    public static final String COL_ING_QUANTITY = "required_quantity";
    public static final String COL_ING_UNIT = "unit";

    // Create Tables SQL
    private static final String CREATE_TABLE_PANTRY = "CREATE TABLE " + TABLE_PANTRY + "("
            + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_PANTRY_NAME + " TEXT NOT NULL,"
            + COL_PANTRY_QUANTITY + " REAL NOT NULL,"
            + COL_PANTRY_UNIT + " TEXT NOT NULL,"
            + COL_PANTRY_EXPIRY + " TEXT" + ")";

    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + TABLE_RECIPES + "("
            + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_RECIPE_NAME + " TEXT NOT NULL,"
            + COL_RECIPE_STEPS + " TEXT" + ")";

    private static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
            + COL_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_ING_RECIPE_ID + " INTEGER,"
            + COL_ING_NAME + " TEXT NOT NULL,"
            + COL_ING_QUANTITY + " REAL NOT NULL,"
            + COL_ING_UNIT + " TEXT NOT NULL,"
            + "FOREIGN KEY(" + COL_ING_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PANTRY);
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        seedRecipes(db); // Pre-load the 15-20 recipes
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

    // ================= RECIPE METHODS =================

    private void seedRecipes(SQLiteDatabase db) {
        // RECIPE 1: Pancakes
        long r1 = addRecipe(db, "Pancakes", "1. Mix flour, milk, and eggs.\n2. Cook on a hot pan until golden brown.");
        addIngredient(db, r1, "flour", 200, "g");
        addIngredient(db, r1, "milk", 300, "ml");
        addIngredient(db, r1, "eggs", 2, "unit");

        // RECIPE 2: Cheese Omelette
        long r2 = addRecipe(db, "Cheese Omelette", "1. Whisk eggs.\n2. Pour into hot pan.\n3. Add cheese, fold, and serve.");
        addIngredient(db, r2, "eggs", 3, "unit");
        addIngredient(db, r2, "cheese", 50, "g");
        addIngredient(db, r2, "butter", 10, "g");

        // RECIPE 3: Tomato Pasta
        long r3 = addRecipe(db, "Tomato Pasta", "1. Boil pasta.\n2. Fry tomatoes and garlic.\n3. Mix together.");
        addIngredient(db, r3, "pasta", 200, "g");
        addIngredient(db, r3, "tomato", 4, "unit");
        addIngredient(db, r3, "garlic", 2, "unit");
        addIngredient(db, r3, "olive oil", 2, "tbsp");

        // RECIPE 4: Grilled Cheese Sandwich
        long r4 = addRecipe(db, "Grilled Cheese Sandwich", "1. Butter bread slices.\n2. Place cheese between bread.\n3. Grill until cheese is melted.");
        addIngredient(db, r4, "bread", 2, "slices");
        addIngredient(db, r4, "cheese", 50, "g");
        addIngredient(db, r4, "butter", 10, "g");

        // RECIPE 5: French Toast
        long r5 = addRecipe(db, "French Toast", "1. Whisk eggs, milk, and sugar.\n2. Dip bread slices in mixture.\n3. Fry in butter until brown.");
        addIngredient(db, r5, "bread", 3, "slices");
        addIngredient(db, r5, "eggs", 2, "unit");
        addIngredient(db, r5, "milk", 100, "ml");
        addIngredient(db, r5, "sugar", 1, "tbsp");
        addIngredient(db, r5, "butter", 10, "g");

        // RECIPE 6: Fried Rice
        long r6 = addRecipe(db, "Fried Rice", "1. Saute garlic and onion in oil.\n2. Add cooked rice and soy sauce.\n3. Push rice aside and scramble eggs into it.");
        addIngredient(db, r6, "rice", 250, "g");
        addIngredient(db, r6, "eggs", 2, "unit");
        addIngredient(db, r6, "soy sauce", 2, "tbsp");
        addIngredient(db, r6, "garlic", 1, "unit");
        addIngredient(db, r6, "onion", 1, "unit");
        addIngredient(db, r6, "vegetable oil", 1, "tbsp");

        // RECIPE 7: Guacamole & Toast
        long r7 = addRecipe(db, "Guacamole Toast", "1. Toast bread slices.\n2. Mash avocado with lemon juice, minced garlic, and salt.\n3. Spread over toast.");
        addIngredient(db, r7, "bread", 2, "slices");
        addIngredient(db, r7, "avocado", 2, "unit");
        addIngredient(db, r7, "lemon", 1, "unit");
        addIngredient(db, r7, "salt", 1, "tsp");
        addIngredient(db, r7, "garlic", 1, "unit");

        // RECIPE 8: Garlic Butter Pasta
        long r8 = addRecipe(db, "Garlic Butter Pasta", "1. Boil pasta until al dente.\n2. Melt butter and saute garlic.\n3. Toss pasta in garlic butter and parmesan.");
        addIngredient(db, r8, "pasta", 200, "g");
        addIngredient(db, r8, "garlic", 4, "unit");
        addIngredient(db, r8, "butter", 30, "g");
        addIngredient(db, r8, "parmesan cheese", 30, "g");
        addIngredient(db, r8, "olive oil", 1, "tbsp");

        // RECIPE 9: Scrambled Eggs on Toast
        long r9 = addRecipe(db, "Scrambled Eggs on Toast", "1. Whisk eggs with milk and salt.\n2. Melt butter in pan and scramble eggs on low heat.\n3. Serve on toast.");
        addIngredient(db, r9, "eggs", 3, "unit");
        addIngredient(db, r9, "bread", 2, "slices");
        addIngredient(db, r9, "butter", 15, "g");
        addIngredient(db, r9, "milk", 30, "ml");

        // RECIPE 10: Tomato Soup
        long r10 = addRecipe(db, "Tomato Soup", "1. Saute onion and garlic in butter.\n2. Add chopped tomatoes and broth.\n3. Simmer and blend until smooth.");
        addIngredient(db, r10, "tomato", 6, "unit");
        addIngredient(db, r10, "onion", 1, "unit");
        addIngredient(db, r10, "garlic", 2, "unit");
        addIngredient(db, r10, "butter", 20, "g");
        addIngredient(db, r10, "vegetable broth", 500, "ml");

        // RECIPE 11: Banana Smoothie
        long r11 = addRecipe(db, "Banana Smoothie", "1. Place bananas, milk, yogurt, and honey in a blender.\n2. Blend until smooth.");
        addIngredient(db, r11, "banana", 2, "unit");
        addIngredient(db, r11, "milk", 250, "ml");
        addIngredient(db, r11, "honey", 1, "tbsp");
        addIngredient(db, r11, "yogurt", 100, "g");

        // RECIPE 12: Mashed Potatoes
        long r12 = addRecipe(db, "Mashed Potatoes", "1. Boil potatoes until tender.\n2. Mash with butter, warm milk, and salt until creamy.");
        addIngredient(db, r12, "potato", 4, "unit");
        addIngredient(db, r12, "butter", 30, "g");
        addIngredient(db, r12, "milk", 100, "ml");
        addIngredient(db, r12, "salt", 1, "tsp");

        // RECIPE 13: Chicken Salad
        long r13 = addRecipe(db, "Chicken Salad", "1. Cook and slice chicken breast.\n2. Toss lettuce, tomatoes, and chicken.\n3. Dress with olive oil and lemon.");
        addIngredient(db, r13, "chicken breast", 300, "g");
        addIngredient(db, r13, "lettuce", 1, "head");
        addIngredient(db, r13, "tomato", 2, "unit");
        addIngredient(db, r13, "olive oil", 2, "tbsp");
        addIngredient(db, r13, "lemon", 1, "unit");

        // RECIPE 14: Vegetable Stir Fry
        long r14 = addRecipe(db, "Vegetable Stir Fry", "1. Saute garlic in oil.\n2. Add chopped broccoli and carrots.\n3. Stir in soy sauce and cook until tender-crisp.");
        addIngredient(db, r14, "broccoli", 1, "head");
        addIngredient(db, r14, "carrot", 2, "unit");
        addIngredient(db, r14, "soy sauce", 2, "tbsp");
        addIngredient(db, r14, "garlic", 2, "unit");
        addIngredient(db, r14, "vegetable oil", 1, "tbsp");

        // RECIPE 15: Caprese Salad
        long r15 = addRecipe(db, "Caprese Salad", "1. Slice tomatoes and mozzarella.\n2. Layer alternately with fresh basil leaves.\n3. Drizzle with olive oil.");
        addIngredient(db, r15, "tomato", 3, "unit");
        addIngredient(db, r15, "mozzarella", 150, "g");
        addIngredient(db, r15, "basil", 10, "leaves");
        addIngredient(db, r15, "olive oil", 2, "tbsp");

        // RECIPE 16: Macaroni and Cheese
        long r16 = addRecipe(db, "Macaroni and Cheese", "1. Boil macaroni pasta.\n2. Make cheese sauce with butter, flour, milk, and cheese.\n3. Mix pasta into sauce.");
        addIngredient(db, r16, "pasta", 250, "g");
        addIngredient(db, r16, "cheese", 150, "g");
        addIngredient(db, r16, "milk", 200, "ml");
        addIngredient(db, r16, "butter", 25, "g");
        addIngredient(db, r16, "flour", 2, "tbsp");
    }

    private long addRecipe(SQLiteDatabase db, String name, String steps) {
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_RECIPE_STEPS, steps);
        return db.insert(TABLE_RECIPES, null, values);
    }

    private void addIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues values = new ContentValues();
        values.put(COL_ING_RECIPE_ID, recipeId);
        values.put(COL_ING_NAME, name);
        values.put(COL_ING_QUANTITY, qty);
        values.put(COL_ING_UNIT, unit);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)));
                recipe.setPreparationSteps(cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null, COL_ING_RECIPE_ID + " = ?",
                new String[]{recipeId + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RecipeIngredient ing = new RecipeIngredient();
                ing.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ING_ID)));
                ing.setRecipeId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ING_RECIPE_ID)));
                ing.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_ING_NAME)));
                ing.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ING_QUANTITY)));
                ing.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_ING_UNIT)));
                ingredients.add(ing);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    // ================= PANTRY CRUD =================

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");
        if (cursor.moveToFirst()) {
            do {
                PantryItem item = new PantryItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PANTRY_ID)));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_NAME)));
                item.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QUANTITY)));
                item.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)));
                item.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QUANTITY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return db.update(TABLE_PANTRY, values, COL_PANTRY_ID + " = ?",
                new String[]{item.getId() + ""});
    }

    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_PANTRY_ID + " = ?", new String[]{id + ""});
    }
}
