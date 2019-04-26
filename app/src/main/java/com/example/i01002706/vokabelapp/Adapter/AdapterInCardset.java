package com.example.i01002706.vokabelapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.i01002706.vokabelapp.Database.AppDatabase;
import com.example.i01002706.vokabelapp.Database.Card;
import com.example.i01002706.vokabelapp.Database.CardDao;
import com.example.i01002706.vokabelapp.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterInCardset extends RecyclerView.Adapter<AdapterInCardset.ViewHolder> {

    private List<Card> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private OnItemClickListener onClickListener;

    interface OnItemClickListener{
        void onItemClick(int position);
        void onPlayButtonClick(int position);
    }
    private AppDatabase database;
    // data is passed into the constructor
    public AdapterInCardset(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.database = AppDatabase.getDatabase(context);
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_incardset, parent, false);
        return new ViewHolder(view);
    }



    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mData.get(position);
        holder.myTextView.setText(card.getFrage());
        holder.myTextView2.setText(card.getAntwort());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView myTextView2;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.inquestion);
            myTextView2 = itemView.findViewById(R.id.inanswer);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null){
                onClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    public Card getItem(int id) {
        return mData.get(id);
    }

    public void deleteItem(int id, Context context) {

        if (database == null) {
            return;
        }
        database = AppDatabase.getDatabase(context);
        CardDao cardDao = database.cardDao();
        cardDao.delete(mData.get(id));
        mData.remove(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void addCategory(List<Card> cards){
        mData = cards;
    }
    public void addCard(Card card){
        mData.add(card);
        notifyDataSetChanged();
    }
}