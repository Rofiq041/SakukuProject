package com.example.sakuku;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvDescription.setText(transaction.getDescription());
        holder.tvAmount.setText(String.format("Rp %.0f", transaction.getAmount()));
        holder.tvDate.setText(transaction.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvAmount, tvDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_transaction_description);
            tvAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvDate = itemView.findViewById(R.id.tv_transaction_date);
        }
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactionList.clear();
        this.transactionList.addAll(newTransactions);
        notifyDataSetChanged();
    }
}