package raphel.bookie.ui.controls;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.ItemDeadlineBinding;

public class ListItemRecyclerAdapter extends RecyclerView.Adapter<ListItemRecyclerAdapter.ViewHolder> {

    private ReadingSession data;
    private ItemListener itemListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeadlineBinding binding = ItemDeadlineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ListItemRecyclerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deadline deadline = data.deadlines.get(position);
        String date = String.valueOf(deadline.date.day)
                + "/" + String.valueOf(deadline.date.month)
                + "/" + String.valueOf(deadline.date.year);

        String content = "TO PAGE: " + deadline.pageToReach + "  |  BY: " + date;
        holder.binding.itmDeadlineTxt.setText(content);
        holder.binding.itmDeadlineBtnDelete.setOnClickListener((View) -> {
            if (itemListener != null) {
                itemListener.delete(data, deadline);
                this.notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null && data.deadlines != null ? data.deadlines.size() : 0;
    }

    public void setData(ReadingSession data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemDeadlineBinding binding;

        public ViewHolder(@NonNull ItemDeadlineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener {
        void delete(ReadingSession session, Deadline deadline);
    }
}