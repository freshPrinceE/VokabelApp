package com.example.i01002706.vokabelapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class AdapterCardset extends RecyclerView.Adapter<AdapterCardset.ViewHolder> {

    private List<Cardset> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AppDatabase database;
    // data is passed into the constructor
    AdapterCardset(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.database = AppDatabase.getDatabase(context);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_cardset, parent, false);
        return new ViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cardset cardset = mData.get(position);
        holder.myTextView.setText(cardset.getName());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        Button play;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.categoryName);
            itemView.setOnClickListener(this);
            play = itemView.findViewById(R.id.play);
            play.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Cardset getItem(int id) {
        return mData.get(id);
    }

    void deleteItem(int id, Context context) {

        if (database == null) {
            return;
        }
        int id_cardset = getItem(id).getId();
        database = AppDatabase.getDatabase(context);
        CardDao cardDao = database.cardDao();
        CardsetDao cardsetDao = database.cardsetDao();
        cardDao.deleteQuery(id_cardset);
        cardsetDao.delete(getItem(id));
        mData.remove(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void addCategory(List<Cardset> cardsets){
        mData = cardsets;
    }
}