package edu.neu.madcourse.cs5520_a7.stickerService;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.List;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Event;

public class ReceiveHistoryRviewAdapter extends RecyclerView.Adapter<ReceiveHistoryRviewHolder> {

    private final List<Event> events;
    private ItemClickListener listener;

    //Constructor
    public ReceiveHistoryRviewAdapter(List<Event> itemList) {
        this.events = itemList;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ReceiveHistoryRviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ReceiveHistoryRviewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ReceiveHistoryRviewHolder holder, int position) {
        Event currentItem = events.get(position);

        holder.stickerIcon.setImageResource(Integer.parseInt(currentItem.stickerId));
        holder.sender.setText(currentItem.sender);
        holder.time.setText(Instant.ofEpochMilli(currentItem.timestampInMillis).toString());
        if (!currentItem.notifyStatus) {
            holder.status.setText("Unread");
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
