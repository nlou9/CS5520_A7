package edu.neu.madcourse.cs5520_a7.stickerService;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.cs5520_a7.R;

public class SendHistoryRviewHolder extends RecyclerView.ViewHolder {
  public ImageView stickerIcon;
  public TextView count;

  public SendHistoryRviewHolder(View itemView) {
    super(itemView);
    stickerIcon = itemView.findViewById(R.id.send_history_item_icon);
    count = itemView.findViewById(R.id.send_history_count);
  }
}