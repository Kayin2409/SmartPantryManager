package com.kayin.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

@SuppressWarnings("unused")
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList;
    private final OnItemClickListener listener;

    // Interface for handling clicks on items
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
        void onItemLongClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryList, OnItemClickListener listener) {
        this.pantryList = pantryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryList.get(position);
        holder.textName.setText(item.getName());

        // Format quantity: show as integer if whole number
        String qtyStr;
        if (item.getQuantity() == Math.floor(item.getQuantity())) {
            qtyStr = ((int) item.getQuantity()) + "";
        } else {
            qtyStr = item.getQuantity() + "";
        }
        String formattedQty = qtyStr + " " + item.getUnit();
        holder.textQty.setText(formattedQty);

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            String expiryText = "Expires: " + item.getExpiryDate();
            holder.textExpiry.setText(expiryText);
            holder.textExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.textExpiry.setVisibility(View.GONE);
        }

        // Click listeners
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    @SuppressWarnings("NotifyDataSetChanged")
    public void updateList(List<PantryItem> newList) {
        this.pantryList = newList;
        notifyDataSetChanged();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textQty, textExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_item_name);
            textQty = itemView.findViewById(R.id.text_item_qty);
            textExpiry = itemView.findViewById(R.id.text_item_expiry);
        }
    }
}
