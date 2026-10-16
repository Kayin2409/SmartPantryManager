package com.kayin.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("unused")
public class AddEditItemActivity extends AppCompatActivity {

    private TextInputEditText editName, editQuantity, editExpiry;
    private AutoCompleteTextView editUnit;
    private TextInputLayout layoutName, layoutQuantity, layoutUnit;
    private DatabaseHelper dbHelper;

    private int itemId = -1; // -1 means "Add mode"
    private boolean isEditMode = false;

    private static final String[] UNITS = {"g", "kg", "ml", "l", "tbsp", "tsp", "unit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        dbHelper = new DatabaseHelper(this);

        // Bind views
        editName = findViewById(R.id.edit_name);
        editQuantity = findViewById(R.id.edit_quantity);
        editUnit = findViewById(R.id.edit_unit);
        editExpiry = findViewById(R.id.edit_expiry);
        layoutName = findViewById(R.id.layout_name);
        layoutQuantity = findViewById(R.id.layout_quantity);
        layoutUnit = findViewById(R.id.layout_unit);
        MaterialButton buttonSave = findViewById(R.id.button_save);
        MaterialButton buttonCancel = findViewById(R.id.button_cancel);

        // Set up unit dropdown
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, UNITS);
        editUnit.setAdapter(unitAdapter);

        // Check if editing an existing item
        itemId = getIntent().getIntExtra("ITEM_ID", -1);
        if (itemId != -1) {
            isEditMode = true;
            setTitle("Edit Ingredient");
            loadExistingItem(itemId);
        } else {
            setTitle("Add Ingredient");
        }

        // Date picker for expiry
        editExpiry.setOnClickListener(v -> showDatePicker());

        // Save button
        buttonSave.setOnClickListener(v -> saveItem());

        // Cancel button
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void loadExistingItem(int id) {
        PantryItem item = dbHelper.getPantryItemById(id);
        if (item != null) {
            editName.setText(item.getName());
            // Show whole numbers cleanly
            String qtyStr = (item.getQuantity() == Math.floor(item.getQuantity()))
                    ? ((int) item.getQuantity()) + ""
                    : item.getQuantity() + "";
            editQuantity.setText(qtyStr);
            editUnit.setText(item.getUnit(), false);
            if (item.getExpiryDate() != null) {
                editExpiry.setText(item.getExpiryDate());
            }
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    // Format as YYYY-MM-DD
                    String date = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d);
                    editExpiry.setText(date);
                }, year, month, day);

        picker.show();
    }

    private void saveItem() {
        // Reset any previous errors
        layoutName.setError(null);
        layoutQuantity.setError(null);
        layoutUnit.setError(null);

        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String qtyStr = editQuantity.getText() != null ? editQuantity.getText().toString().trim() : "";
        String unit = editUnit.getText() != null ? editUnit.getText().toString().trim() : "";
        String expiry = editExpiry.getText() != null ? editExpiry.getText().toString().trim() : "";

        // ===== VALIDATION =====

        // 1. Name must not be empty
        if (TextUtils.isEmpty(name)) {
            layoutName.setError("Ingredient name is required");
            editName.requestFocus();
            return;
        }

        // 2. Quantity must be a valid positive number
        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                layoutQuantity.setError("Quantity must be greater than 0");
                editQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            layoutQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();
            return;
        }

        // 3. Unit must be selected
        if (TextUtils.isEmpty(unit)) {
            layoutUnit.setError("Please select a unit");
            editUnit.requestFocus();
            return;
        }

        // ===== SAVE =====

        PantryItem item = new PantryItem(name, quantity, unit, expiry.isEmpty() ? null : expiry);

        if (isEditMode) {
            item.setId(itemId);
            int rows = dbHelper.updatePantryItem(item);
            if (rows > 0) {
                Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            long newId = dbHelper.addPantryItem(item);
            if (newId > 0) {
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
