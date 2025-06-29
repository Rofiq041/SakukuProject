package com.example.sakuku;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;

    public ReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        holder.tvReminderName.setText(reminder.getName());
        holder.tvReminderDetails.setText(reminder.getDate()); // Assuming date is the detail
        holder.tvReminderTime.setText(reminder.getTime());
        holder.tvReminderAmount.setText("Rp " + String.format("%.0f", reminder.getAmount())); // Assuming Reminder has getAmount()
        holder.tvReminderStatus.setText(reminder.getStatus());

        if (reminder.getStatus().equals("Aktif")) {
            holder.tvReminderStatus.setBackgroundResource(R.drawable.bg_rounded_green);
        } else {
            holder.tvReminderStatus.setBackgroundResource(R.drawable.bg_rounded_gray);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvReminderName, tvReminderDetails, tvReminderTime, tvReminderAmount, tvReminderStatus;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReminderName = itemView.findViewById(R.id.tv_reminder_name);
            tvReminderDetails = itemView.findViewById(R.id.tv_reminder_details);
            tvReminderTime = itemView.findViewById(R.id.tv_reminder_time);
            tvReminderAmount = itemView.findViewById(R.id.tv_reminder_amount);
            tvReminderStatus = itemView.findViewById(R.id.tv_reminder_status);
        }
    }
}
