package raphel.bookie.ui.controls;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import raphel.bookie.R;
import raphel.bookie.data.Repository;
import raphel.bookie.data.room.DailyTarget;
import raphel.bookie.data.room.Deadline;
import raphel.bookie.data.room.ReadingSession;
import raphel.bookie.databinding.ItemTargetBinding;

public class TodayRecyclerAdapter extends RecyclerView.Adapter<TodayRecyclerAdapter.ViewHolder> {

    private List<ReadingSession> data;
    private ItemListener itemListener;

    @NonNull
    @Override
    public TodayRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTargetBinding binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodayRecyclerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayRecyclerAdapter.ViewHolder holder, int position) {
        ReadingSession session = data.get(position);

        holder.binding.itmTargetTitle.setText(session.book.title);
        holder.setData(session);
        String content = "PAGE: " + session.dailyTarget.pageToReach;
        holder.binding.itmTargetContent.setText(content);
        holder.updateVisual();

        holder.binding.itmTargetSwitch.setOnClickListener((View) -> {
            if (itemListener != null) itemListener.checked(holder, holder.binding.itmTargetSwitch.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setData(List<ReadingSession> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTargetBinding binding;
        private ReadingSession data;

        public ViewHolder(@NonNull ItemTargetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemTargetBinding getBinding() {
            return binding;
        }

        public void setData(ReadingSession data) {
            this.data = data;
        }
        public ReadingSession getData() {
            return data;
        }

        public void updateVisual() {
            binding.itmTargetSwitch.setChecked(data.dailyTarget.pageReached > 0);
            if (data.dailyTarget.pageReached > 0) binding.itmTargetIcon.setImageResource(R.drawable.ic_check);
            else binding.itmTargetIcon.setImageResource(R.drawable.ic_circumference);
        }
    }

    public interface ItemListener {
        void checked(TodayRecyclerAdapter.ViewHolder holder, boolean isChecked);
    }
}
