package com.tanmoy.api_ai_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context context;
    List<NewMessage> messagelist;



    public class MyViewHolder extends ViewHolder{
        public TextView userName, userMessage, messagetime;
        public ImageView userImage;
        private LinearLayout messageCardLayout;

        public MyViewHolder(View view){
            super(view);
            userName = view.findViewById(R.id.user_name);
            userMessage = view.findViewById(R.id.user_message);
            messagetime = view.findViewById(R.id.message_time);
            userImage = view.findViewById(R.id.userImage);
            messageCardLayout = view.findViewById(R.id.msgcardlayout);
        }

    }

    public MessageAdapter(Context context, List<NewMessage> messagelist) {
        this.context = context;
        this.messagelist = messagelist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewMessage message = messagelist.get(position);

        holder.userName.setText(message.getUser());
        holder.userMessage.setText(message.getMessage());
        holder.messagetime.setText(message.getTime());
        if(message.getUser().equalsIgnoreCase("You")){
            holder.messageCardLayout.setBackgroundResource(R.drawable.rounded_rectangle_orange);
            holder.userImage.setImageResource(R.drawable.profile_pic);
        }else{
            holder.messageCardLayout.setBackgroundResource(R.drawable.rounded_rectangle_green);
            holder.userImage.setImageResource(R.drawable.nsulogo);
        }



    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

}
