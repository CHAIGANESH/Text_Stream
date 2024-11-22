package com.example.textstream;
import android.view.ViewGroup;
import android.widget.*;
import android.view.View;
import android.content.Intent;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;
public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    private List<BlockItem> blockList;
    private List<BlockItem> filteredList;

    public BlockAdapter(List<BlockItem> blockList) {
        this.blockList = blockList;
        this.filteredList = new ArrayList<>(blockList);
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new BlockViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        BlockItem block = filteredList.get(position);
        holder.title.setText(block.getTitle());
        holder.itemView.setOnClickListener(v -> {
            // Launch the associated activity
            Intent intent = new Intent(v.getContext(), block.getActivityClass());
            intent.putExtra("title", block.getTitle());
            intent.putExtra("objectives", block.getObjectives());
            intent.putExtra("notes", block.getNotes());
            intent.putExtra("book", block.getBook());
            intent.putExtra("bookbutton", block.getBookbutton());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // ViewHolder class
    public static class BlockViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
        }
    }

    // Filter method for SearchView
    public void filter(String query) {
        if (query.isEmpty()) {
            filteredList = new ArrayList<>(blockList);
        } else {
            List<BlockItem> result = new ArrayList<>();
            for (BlockItem block : blockList) {
                if (block.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    result.add(block);
                }
            }
            filteredList = result;
        }
        notifyDataSetChanged();
    }
}

