package com.kayin.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PantryListFragment extends Fragment {

    private PantryAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView textEmpty;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view_pantry);
        textEmpty = view.findViewById(R.id.text_empty_pantry);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up adapter with click listeners
        adapter = new PantryAdapter(new ArrayList<>(), new PantryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PantryItem item) {
                // Open Add/Edit screen in edit mode
                Intent intent = new Intent(requireActivity(), AddEditItemActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(PantryItem item) {
                // Confirm delete
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Item")
                        .setMessage("Delete " + item.getName() + " from your pantry?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deletePantryItem(item.getId());
                            loadPantryItems();
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            // Open Add/Edit screen in add mode
            Intent intent = new Intent(requireActivity(), AddEditItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload items every time the fragment is shown (after add/edit)
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        if (items.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.updateList(items);
        }
    }
}
