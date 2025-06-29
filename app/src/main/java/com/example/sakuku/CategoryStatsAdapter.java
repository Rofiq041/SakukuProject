package com.example.sakuku;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryStatsAdapter extends RecyclerView.Adapter<CategoryStatsAdapter.ViewHolder> {

    private List<CategoryStat> categoryStats;

    public CategoryStatsAdapter(List<CategoryStat> categoryStats) {
        this.categoryStats = categoryStats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_statistic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryStat stat = categoryStats.get(position);
        holder.tvCategoryName.setText(stat.getCategoryName());
        holder.tvCategoryPercentage.setText(stat.getPercentage() + "%");
        holder.progressBar.setProgress(stat.getPercentage());
        // Set category icon based on category name
        // For now, using a placeholder icon
        holder.ivCategoryIcon.setImageResource(R.drawable.ic_food);
    }

    @Override
    public int getItemCount() {
        return categoryStats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;
        TextView tvCategoryPercentage;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvCategoryPercentage = itemView.findViewById(R.id.tv_category_percentage);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
