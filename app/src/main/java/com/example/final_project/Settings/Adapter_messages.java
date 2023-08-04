package com.example.final_project.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_messages extends RecyclerView.Adapter<Holder_messages>{

    public List<Messages_groups> list_message=new ArrayList<>();
    private Context context;
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public Adapter_messages(Context context) {
        this.context = context;
    }

    public void add_message(Messages_groups message){
        list_message.add(message);
        notifyItemInserted(list_message.size()-1);
    }

    public interface OnItemClickListener {
        void onItemButtonClick(int position,List<Messages_groups> list_message);
    }

    public int findPositionByKey(String messageKey) {
        for (int i = 0; i < list_message.size(); i++) {
            Messages_groups message = list_message.get(i);
            if (message != null && message.getId().equals(messageKey)) {
                return i;
            }
        }
        return -1;
    }

    public void removeMessage(int position) {
        if (position >= 0 && position < list_message.size()) {
            list_message.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list_message.size());
        }
    }

    @NonNull
    @Override
    public Holder_messages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_view_messages, parent, false);

        return new Holder_messages(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_messages holder, @SuppressLint("RecyclerView") int position) {
        holder.getTxt_name_user_messg().setText(list_message.get(position).getName_user_messg());
        holder.getTxt_time_messg().setText(list_message.get(position).getTime_messg());
        holder.getTxt_name_file().setText(list_message.get(position).getName_file());
        holder.getTxt_weight().setText(list_message.get(position).getWeight());
        holder.getTxt_extension().setText(list_message.get(position).getExtension());
        Glide.with(context).load(list_message.get(position).getImage_user()).into(holder.getImage_user());
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/final-project-d3437.appspot.com/o/Message_files%2Ffile.png?alt=media&token=fbad2126-e500-4576-bfce-372efce40636").into(holder.getImg_image_file());

        holder.getImgbtn_options().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemButtonClick(position, list_message);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_message.size();
    }
}
