package edu.neu.madcourse.cs5520_a7.stickerService;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.cs5520_a7.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendHistoryRviewAdapter extends RecyclerView.Adapter<SendHistoryRviewHolder> {

  private final List<int[]> stickerIdWithCount;

  //Constructor
  public SendHistoryRviewAdapter(Map<String, Integer> countByStickerId) {
    stickerIdWithCount = new ArrayList<>();
    for (String stickerId : countByStickerId.keySet()) {
      stickerIdWithCount.add(
        new int[]{Integer.parseInt(stickerId), countByStickerId.get(stickerId)});
    }
  }


  @Override
  public SendHistoryRviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
      LayoutInflater.from(parent.getContext()).inflate(R.layout.send_history_card, parent, false);
    return new SendHistoryRviewHolder(view);
  }

  @Override
  public void onBindViewHolder(SendHistoryRviewHolder holder, int position) {
    int[] currentItem = stickerIdWithCount.get(position);

    holder.stickerIcon.setImageResource(currentItem[0]);
    holder.count.setText(String.valueOf(currentItem[1]));
  }

  @Override
  public int getItemCount() {
    return stickerIdWithCount.size();
  }
}