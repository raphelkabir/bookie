package raphel.bookie.ui.controls;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import raphel.bookie.data.room.Deadline;
import raphel.bookie.databinding.ItemDeadlineBinding;

public class ListItemRecyclerAdapter extends RecyclerView.Adapter<ListItemRecyclerAdapter.ViewHolder> {

    private Deadline longPressedDeadline;

    private final AsyncListDiffer<Deadline> listDiffer = new AsyncListDiffer(this, new DiffUtil.ItemCallback<Deadline>() {
        @Override
        public boolean areItemsTheSame(@NonNull Deadline oldItem, @NonNull Deadline newItem) {
            return oldItem.areItemsTheSame(newItem);
        }
        @Override
        public boolean areContentsTheSame(@NonNull Deadline oldItem, @NonNull Deadline newItem) {
            return oldItem.equals(newItem);
        }
    });

    private ItemListener itemListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeadlineBinding binding = ItemDeadlineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder holder = new ListItemRecyclerAdapter.ViewHolder(binding);
        if (itemListener != null) {
            itemListener.itemCreated(holder.itemView);
        }

        holder.itemView.setOnLongClickListener(v -> {
            longPressedDeadline = listDiffer.getCurrentList().get(holder.getAdapterPosition());
            return false;
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deadline deadline = listDiffer.getCurrentList().get(position);

        holder.binding.itmDeadlineName.setText(deadline.name);
        LocalDate date = LocalDate.of(deadline.date.year, deadline.date.month, deadline.date.day);
        String formattedDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

        String content = deadline.pageToReach + " | "  + formattedDate;
        holder.binding.itmDeadlineTxt.setText(content);
        holder.binding.itmDeadlineSwitch.setChecked(deadline.isActive);
        holder.binding.itmDeadlineSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            deadline.isActive = isChecked;
            if (itemListener != null) {
                itemListener.itemSwitchChanged(deadline);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDiffer.getCurrentList().size();
    }

    public Deadline getLongPressedDeadline() {
        return longPressedDeadline;
    }

    public void submitData(List<Deadline> data) {
        listDiffer.submitList(data);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemDeadlineBinding binding;

        public ViewHolder(@NonNull ItemDeadlineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemListener {
        void itemCreated(View view);
        void itemSwitchChanged(Deadline deadline);
    }
}